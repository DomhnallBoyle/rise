package rise.myapplication.Screens;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import rise.myapplication.UI.Animation;
import rise.myapplication.World.GameObjects.Background;
import rise.myapplication.Util.BoundingBox;
import rise.myapplication.UI.DialogBox;
import rise.myapplication.UI.DisplayToast;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.World.GameObjects.Enemy;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.TouchEvent;
import rise.myapplication.Game.Game;
import rise.myapplication.Util.GraphicsHelper;
import rise.myapplication.World.GameObjects.Item;
import rise.myapplication.World.LayerViewport;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.World.GameObjects.Platform;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.World.GameObjects.Projectile;
import rise.myapplication.Util.Scale;
import rise.myapplication.World.ScreenViewport;
import rise.myapplication.UI.SimpleControl;
import rise.myapplication.Util.Timer;
import rise.myapplication.World.GameScreen;

/**
 * Created by 40126424 on 10/12/2015.
 */
public class MainGameScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //set game rects
    private final Rect backgroundRect;
    private final Rect topToolbar;
    private final Rect PauseMenu;
    private final Animation explosionAnimation;
    private final Animation coinAnimation;

    //set level height and width
    private final float LEVEL_WIDTH = Scale.getX(120);
    private final float LEVEL_HEIGHT = Scale.getY(120);

    //screen and layer viewports
    private final ScreenViewport mScreenViewport;
    private final LayerViewport mLayerViewport;

    //movement and pause buttons
    private final SimpleControl moveLeft;
    private final SimpleControl moveRight;
    private final SimpleControl pause;
    private final List<SimpleControl> mControls = new ArrayList<>();

    //chance of spawning new objects
    private int randPlatform = 0, randCollectable = 0, randBadGuy = 0;
    private int platformDistance = 5;
    private final int MAX_PLATFORM_DISTANCE_EASY = 25;
    private final int MAX_PLATFORM_DISTANCE_MED = 30;
    private final int MAX_PLATFORM_DISTANCE_HARD = 40;
    private final int PLATFORM_SPAWN_Y = 120;

    //game object arraylists
    private final ArrayList<Platform> mPlatforms;
    private final ArrayList<Enemy> mBadGuys;
    private final ArrayList<Projectile> mProjectiles;
    private final ArrayList<Projectile> mEnemyProjectiles;
    private final ArrayList<Item> mCollectables;

    private final ArrayList<Platform> availablePlatforms;
    private final ArrayList<Enemy> availableBadGuys;
    private final ArrayList<Projectile> availableProjectiles;
    private final ArrayList<Projectile> availableEnemyProjectiles;
    private final ArrayList<Item> availableCollectables;

    //chance of spawns(in terms of updates)
    private final int enemySpawnChance = 120;
    private final int collectableSpawnChance = 30;
    private int platformSpawnChance = 1;

    //random number object
    private final Random random = new Random();

    //paint objects
    private final Paint paint;

    //player object
    private final Player mPlayer;
    private Player mPlayer2;

    //timer object
    private final Timer timer;

    //background object
    private final Background background;

    private DialogBox dialogBox;
    private double time = 0;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public MainGameScreen(Game game, Player player) {
        super("MainGameScreen", game);

        // Create the view ports
        mLayerViewport = new LayerViewport(Scale.getX(50), Scale.getY(60), Scale.getX(50), Scale.getY(60));
        mScreenViewport = new ScreenViewport();

        //set paint options
        Typeface plain = Typeface.createFromAsset(MainActivity.getContext().getAssets(), String.format(Locale.US, "Fonts/%s", "Tiki Tropic Bold.ttf"));
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        paint= new Paint();
        paint.setColor(Color.WHITE);
        paint.setTypeface(bold);
        paint.setTextSize(50);

        //setup timers
        timer = new Timer(0,true);

        //set background
        backgroundRect = new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight());
        switch(OptionsScreen.theme)
        {
            case 0:  background = new Background(Scale.getX(0), Scale.getY(0), this, getGame().getAssetManager().getBitmap("SpaceBackground"), getGame().getAssetManager().getBitmap("TimedBackgroundSpace"), getGame().getAssetManager().getBitmap("Moon")); break;
            case 1:  background = new Background(Scale.getX(0), Scale.getY(0), this, getGame().getAssetManager().getBitmap("MountainBackground"), getGame().getAssetManager().getBitmap("TimedBackgroundSpace"), getGame().getAssetManager().getBitmap("Cloud")); break;
            default: background = new Background(Scale.getX(0), Scale.getY(0), this, getGame().getAssetManager().getBitmap("SpaceBackground"), getGame().getAssetManager().getBitmap("TimedBackgroundSpace"), getGame().getAssetManager().getBitmap("Moon")); break;
        }

        //setup top toolbar
        topToolbar= new Rect(0,0,Scale.getX(100),Scale.getY(7));

        //setup pause menu
        PauseMenu = new Rect(Scale.getX(20),Scale.getY(20), Scale.getX(80),Scale.getY(90));

        //setup button movement controls
        moveLeft = new SimpleControl(Scale.getX(10), Scale.getY(90), 150.0f, 150.0f, "LeftArrow", this);
        mControls.add(moveLeft);

        moveRight = new SimpleControl(Scale.getX(90), Scale.getY(90), 150.0f, 150.0f, "RightArrow", this);
        mControls.add(moveRight);

        //setup pause button
        pause = new SimpleControl(Scale.getX(65), Scale.getY(3.5), 200.0f, 200.0f, "Pause", this);
        mControls.add(pause);

        //set player reference
        this.mPlayer = player;
        this.mPlayer2 = null;

        //if the game is set to two player, and current player is the 1st, create a second player and store in mGame
        if (mGame.getTwoPlayerToggle() && !mPlayer.getIsSecondPlayer()){
            mGame.setPlayer1(mPlayer);
            this.mPlayer2 = new Player(Scale.getX(50), 200.0f, "", this);
            mPlayer2.setIsSecondPlayer(true);
            mGame.setPlayer2(mPlayer2);
        }

        // Set number of each game object
        int mNumGroundPlatforms = 1;
        int nNumRandomPlatforms = 30;
        int mNumRandombadguys = 2;
        int mNumCoins = 8;
        int mNumSprings = 2;
        int mNumMagnets = 2;
        int mNumRockets = 2;
        int mNumMultipliers = 2;
        int mNumShields = 2;

        //initialise GameObject ArrayLists
        mPlatforms = new ArrayList<>();
        availablePlatforms = new ArrayList<>();
        mProjectiles = new ArrayList<>();
        availableProjectiles = new ArrayList<>();
        mEnemyProjectiles = new ArrayList<>();
        availableEnemyProjectiles = new ArrayList<>();
        mBadGuys = new ArrayList<>();
        availableBadGuys = new ArrayList<>();
        mCollectables = new ArrayList<>();
        availableCollectables = new ArrayList<>();

        //add ground platforms(makes game initially playable)
        for (int idx = 0; idx < mNumGroundPlatforms; idx++) {
            mPlatforms.add(new Platform(Scale.getX(100), Scale.getY(Platform.PLATFORM_HEIGHT) * 3, Platform.PlatformType.NORMAL, this));
        }

        // Create a number of platforms at random positions. They are not created in the
        // first 300 units of the level (this is our safe area for creating the player in).
        for (int idx = 0; idx < nNumRandomPlatforms; idx++) {
            if (idx<10)
            {
                mPlatforms.add(new Platform((random.nextFloat() * LEVEL_WIDTH), (random.nextFloat() * (LEVEL_HEIGHT - Scale.getY(Platform.PLATFORM_HEIGHT) / 2)), Platform.PlatformType.NORMAL, this));
            }
            else if (idx>=10 && idx < 20)
            {
                mPlatforms.add(new Platform((random.nextFloat() * LEVEL_WIDTH), (random.nextFloat() * (LEVEL_HEIGHT - Scale.getY(Platform.PLATFORM_HEIGHT) / 2)), Platform.PlatformType.EXPLODING, this));
            }
            else
            {
                mPlatforms.add(new Platform((random.nextFloat() * LEVEL_WIDTH), (random.nextFloat() * (LEVEL_HEIGHT - Scale.getY(Platform.PLATFORM_HEIGHT) / 2)), Platform.PlatformType.MOVING, this));
            }
        }


        //create a number of enemies at random positions
        for (int idx = 0; idx < mNumRandombadguys; idx++) {
            if (idx%2==0)
            {
                mBadGuys.add(new Enemy((random.nextFloat() * LEVEL_WIDTH), Scale.getY(120), Enemy.enemyType.MOVING, this));
            }
            else
            {
                mBadGuys.add(new Enemy((random.nextFloat() * LEVEL_WIDTH), Scale.getY(120), Enemy.enemyType.STILL, this));
            }

        }

        //create a number of coins at random positions
        int rand;
        for (int i=0; i<mNumCoins; i++)
        {
            rand = random.nextInt(30);
            if (mPlatforms.get(rand).getPlatformType() == Platform.PlatformType.NORMAL || mPlatforms.get(rand).getPlatformType() == Platform.PlatformType.MOVING)
            {
                mCollectables.add(new Item(mPlatforms.get(rand).getBound().getX(), mPlatforms.get(rand).getBound().getTop()+Item.ITEM_HEIGHT/2, game.getAssetManager().getBitmap("Coin"), mPlatforms.get(rand), Item.itemType.COIN, this));
            }
        }

        //add springs to available collectable pool
        for (int i=0; i<mNumSprings; i++)
        {
            availableCollectables.add(new Item(mPlatforms.get(0).getBound().getX(), mPlatforms.get(0).getBound().getTop()+Item.ITEM_HEIGHT, game.getAssetManager().getBitmap("Springs"), mPlatforms.get(0), Item.itemType.SPRINGS, this));
        }

        //add magnets to available collectable pool
        for (int i=0; i<mNumMagnets; i++)
        {
            availableCollectables.add(new Item(mPlatforms.get(0).getBound().getX(), mPlatforms.get(0).getBound().getTop()+Item.ITEM_HEIGHT/2, game.getAssetManager().getBitmap("Magnet"), mPlatforms.get(0), Item.itemType.MAGNET, this));
        }

        //add rockets to available collectable pool
        for (int i=0; i<mNumRockets; i++)
        {
            availableCollectables.add(new Item(mPlatforms.get(0).getBound().getX(), mPlatforms.get(0).getBound().getTop()+Item.ITEM_HEIGHT/2, game.getAssetManager().getBitmap("Rocket"), mPlatforms.get(0), Item.itemType.ROCKET, this));
        }

        //add shields to available collectable pool
        for (int i=0; i<mNumShields; i++)
        {
            availableCollectables.add(new Item(mPlatforms.get(0).getBound().getX(), mPlatforms.get(0).getBound().getTop()+Item.ITEM_HEIGHT/2, game.getAssetManager().getBitmap("Shield"), mPlatforms.get(0), Item.itemType.SHIELD, this));
        }

        //add multipliers to available collectable pool
        for (int i=0; i<mNumMultipliers; i++)
        {
            availableCollectables.add(new Item(mPlatforms.get(0).getBound().getX(), mPlatforms.get(0).getBound().getTop()+Item.ITEM_HEIGHT/2, game.getAssetManager().getBitmap("Multiplier"), mPlatforms.get(0), Item.itemType.MULTIPLIER, this));
        }

        //setup animations
        explosionAnimation = new Animation(mGame.getAssetManager().getBitmap("Explosion"), 12);
        explosionAnimation.play(1.2, true);
        coinAnimation = new Animation(mGame.getAssetManager().getBitmap("CoinAnimation"), 8);
        coinAnimation.play(2, true);

        dialogBox = new DialogBox("Player 2's turn!");

        if (mPlayer.getAttributeLevel(5) > 0 )
        {
            mPlayer.increaseLife();
            mPlayer.setAttributeLevel(5, mPlayer.getAttributeLevel(5) - 1);
        }

        paint.setTextSize(50);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Update and Draw
    // /////////////////////////////////////////////////////////////////////////

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.ac.qub.eeecs.gage.world.GameScreen#update(uk.ac.qub.eeecs.gage.engine
     * .ElapsedTime)
     */
    @Override
    public void update(ElapsedTime elapsedTime) {
        //if the game is not paused
        if (!super.pause)
        {
            pause.isActivated();

            if (pause.isPaused())
            {
                timer.update(elapsedTime,true);
            }
            else {

                //update timer
                timer.update(elapsedTime,false);

                //set platform distance depending on game difficulty
                switch (mGame.getDifficulty())
                {
                    case 0: platformDistance = 5 + (mPlayer.getScoreValue() / 200);
                        if(platformDistance > MAX_PLATFORM_DISTANCE_EASY)
                            platformDistance = MAX_PLATFORM_DISTANCE_EASY;
                        break;
                    case 1: platformDistance = 5 + (mPlayer.getScoreValue() / 100);
                        if(platformDistance > MAX_PLATFORM_DISTANCE_MED)
                            platformDistance = MAX_PLATFORM_DISTANCE_MED;
                        break;
                    case 2: platformDistance = 5 + (mPlayer.getScoreValue() / 50);
                        if(platformDistance > MAX_PLATFORM_DISTANCE_HARD)
                            platformDistance = MAX_PLATFORM_DISTANCE_HARD;
                        break;
                    default: platformDistance = 5 + (mPlayer.getScoreValue() / 100);
                        if(platformDistance > MAX_PLATFORM_DISTANCE_MED)
                            platformDistance = MAX_PLATFORM_DISTANCE_MED;
                        break;
                }

                // Update the player
                //accelerometer or button control updates
                if (mPlayer.getAccelerometerToggle())
                {
                    //accelerometer update method
                    mPlayer.update(elapsedTime, mPlatforms, mBadGuys, mCollectables);
                }
                else
                {
                    //button control update method
                    mPlayer.update(elapsedTime, moveLeft.isActivated(), moveRight.isActivated(), mPlatforms, mBadGuys, mCollectables);
                }

                //chance to spawn new platform
                //availablePlatforms.size()>0 &&  random.nextInt() % platformSpawnChance == 0
                if(availablePlatforms.size()>0 && mPlatforms.get(mPlatforms.size() - 1).getBound().getTop() < Scale.getY(PLATFORM_SPAWN_Y - platformDistance))
                {
                    randPlatform = random.nextInt(availablePlatforms.size());
                    mPlatforms.add(availablePlatforms.get(randPlatform));
                    mPlatforms.get(mPlatforms.size() - 1).setX(Scale.getX(random.nextInt(100)));
                    mPlatforms.get(mPlatforms.size() - 1).setY(Scale.getY(PLATFORM_SPAWN_Y));
                    availablePlatforms.remove(randPlatform);
                }

                //update the platforms
                for (int i = 0; i < mPlatforms.size(); i++)
                {
                    if (!GraphicsHelper.checkIfPastWidth(mPlatforms.get(i), mLayerViewport, mScreenViewport, mPlatforms.get(i).getDrawSourceRect(), mPlatforms.get(i).getDrawScreenRect()) ||
                            !GraphicsHelper.checkIfPastHeight(mPlatforms.get(i), mLayerViewport, mScreenViewport, mPlatforms.get(i).getDrawSourceRect(), mPlatforms.get(i).getDrawScreenRect())) {
                        availablePlatforms.add(mPlatforms.get(i));
                        mPlatforms.remove(i);
                        i--;
                    }
                    else
                    {
                        mPlatforms.get(i).update(mPlayer);
                    }
                }

                //chance spawn random collectible
                if(availableCollectables.size() > 0 && random.nextInt() % collectableSpawnChance == 0 && mPlatforms.get(mPlatforms.size() - 1).getBound().getTop() > Scale.getY(100))
                {
                    randCollectable = random.nextInt(availableCollectables.size());
                    mCollectables.add(availableCollectables.get(randCollectable));
                    mCollectables.get(mCollectables.size()-1).setPlatform(mPlatforms.get(mPlatforms.size() - 1));
                    mCollectables.get(mCollectables.size()-1).setIsMagnetised(false);
                    availableCollectables.remove(randCollectable);
                }

                //update the collectibles
                for (int i=0; i<mCollectables.size(); i++)
                {
                    if (!GraphicsHelper.checkIfPastWidth(mCollectables.get(i), mLayerViewport, mScreenViewport, mCollectables.get(i).getDrawSourceRect(), mCollectables.get(i).getDrawScreenRect()) ||
                            !GraphicsHelper.checkIfPastHeight(mCollectables.get(i), mLayerViewport, mScreenViewport, mCollectables.get(i).getDrawSourceRect(), mCollectables.get(i).getDrawScreenRect())) {
                        availableCollectables.add(mCollectables.get(i));
                        mCollectables.remove(mCollectables.get(i));
                        i--;
                    } else {
                        mCollectables.get(i).update(elapsedTime, mPlayer);
                    }
                }

                //chance to spawn random bad guy
                if(availableBadGuys.size() > 0 && random.nextInt() % enemySpawnChance == 0)
                {
                    randBadGuy = random.nextInt(availableBadGuys.size());
                    mBadGuys.add(availableBadGuys.get(randBadGuy));
                    mBadGuys.get(mBadGuys.size()-1).setX(random.nextFloat() * LEVEL_WIDTH);
                    mBadGuys.get(mBadGuys.size()-1).setY(Scale.getY(120));
                    availableBadGuys.remove(randBadGuy);
                }

                //update the badguys
                for (int i = 0; i < mBadGuys.size(); i++)
                {
                    if (!GraphicsHelper.checkIfPastWidth(mBadGuys.get(i), mLayerViewport, mScreenViewport, mBadGuys.get(i).getDrawSourceRect(), mBadGuys.get(i).getDrawScreenRect()) ||
                            !GraphicsHelper.checkIfPastHeight(mBadGuys.get(i), mLayerViewport, mScreenViewport, mBadGuys.get(i).getDrawSourceRect(), mBadGuys.get(i).getDrawScreenRect()))
                    {
                        availableBadGuys.add(mBadGuys.get(i));
                        mBadGuys.remove(i);
                        i--;
                    }
                    else
                    {
                        mBadGuys.get(i).update(elapsedTime, mPlayer, mProjectiles, mEnemyProjectiles);
                    }
                }

                //fire enemy projectiles
                for (int i=0; i<mBadGuys.size(); i++)
                {
                    if (mPlayer.getHasBounced() && mBadGuys.get(i).getEnemies() == Enemy.enemyType.STILL)
                    {
                        if(availableEnemyProjectiles.size()>0)
                        {
                            mEnemyProjectiles.add(availableEnemyProjectiles.get(0));
                            mEnemyProjectiles.get(mEnemyProjectiles.size()-1).setEnemyValues(mBadGuys.get(i).getPosition().x, mBadGuys.get(i).getPosition().y, (mPlayer.getPosition().x), (mPlayer.getPosition().y));
                            availableEnemyProjectiles.remove(0);
                            mPlayer.setHasBounced(false);
                        }else
                        {
                            Projectile projectile = new Projectile(mBadGuys.get(i).getPosition().x, mBadGuys.get(i).getPosition().y, (mPlayer.getPosition().x), (mPlayer.getPosition().y), Projectile.ProjectileType.ENEMY, this);
                            mEnemyProjectiles.add(projectile);
                            mPlayer.setHasBounced(false);
                        }
                    }
                }

                //update the animations
                explosionAnimation.update(elapsedTime);
                coinAnimation.update(elapsedTime);

                int maxNumProjectiles;

                switch (mGame.getDifficulty()){
                    case 0: maxNumProjectiles = 10; break;
                    case 1: maxNumProjectiles = 5; break;
                    case 2: maxNumProjectiles = 3; break;
                    default: maxNumProjectiles = 10; break;
                }

                //check if player fires projectile
                Input input = mGame.getInput();
                List<TouchEvent> touchEvents = input.getTouchEvents();
                if (touchEvents.size() > 0)
                {
                    TouchEvent touchEvent = touchEvents.get(0);
                    if (touchEvent.type == touchEvent.TOUCH_DOWN && mProjectiles.size()<maxNumProjectiles && !moveLeft.contains((int) touchEvent.x, (int) touchEvent.y) && !moveRight.contains((int) touchEvent.x, (int) touchEvent.y))
                    {
                        if(availableProjectiles.size()>0)
                        {
                            mProjectiles.add(availableProjectiles.get(0));
                            mProjectiles.get(mProjectiles.size() - 1).setPlayerValues(mPlayer.getPosition().x, mPlayer.getPosition().y, touchEvent.x, touchEvent.y);
                            availableProjectiles.remove(0);
                        }
                        else
                        {
                            Projectile projectile = new Projectile(mPlayer.getPosition().x, mPlayer.getPosition().y, touchEvent.x, touchEvent.y, Projectile.ProjectileType.FRIENDLY, this);
                            mProjectiles.add(projectile);
                        }
                    }
                }

                //checks position of projectiles, moves unseen projectiles to available pool
                for (int j = 0; j < mProjectiles.size(); j++) {
                    if (mProjectiles.get(j).getPosition().x < 0 ||
                            mProjectiles.get(j).getPosition().x > Scale.getX(100) ||
                            mProjectiles.get(j).getBound().getTop() > Scale.getY(115)||
                            mProjectiles.get(j).getBound().getTop() < Scale.getY(0))
                    {
                        availableProjectiles.add(mProjectiles.get(j));
                        mProjectiles.remove(j);
                        j--;
                    }
                }

                //update projectiles
                if (!(mProjectiles.size() == 0))
                {
                    for (int i = 0; i < mProjectiles.size(); i++) {
                        mProjectiles.get(i).update(elapsedTime, mEnemyProjectiles);
                    }
                }

                //update enemy projectiles
                if (!(mEnemyProjectiles.size() == 0))
                {
                    for (int i=0; i<mEnemyProjectiles.size(); i++)
                    {
                        mEnemyProjectiles.get(i).update(elapsedTime);
                    }

                    //checks position of enemy projectiles, moves unseen projectiles to available pool
                    for (int j = 0; j < mEnemyProjectiles.size(); j++)
                    {
                        if (mEnemyProjectiles.get(j).getPosition().x < Scale.getX(0) ||
                                mEnemyProjectiles.get(j).getPosition().x > Scale.getX(100) ||
                                mEnemyProjectiles.get(j).getPosition().y > Scale.getY(100)||
                                mEnemyProjectiles.get(j).getPosition().y < Scale.getY(0))
                        {
                            availableEnemyProjectiles.add(mEnemyProjectiles.get(j));
                            mEnemyProjectiles.remove(j);
                            j--;
                        }
                    }


                }

                //update background
                background.update(mPlayer);

                // Ensure the player cannot leave the confines of the world
                BoundingBox playerBound = mPlayer.getBound();
                if (playerBound.getLeft() < -playerBound.getHalfWidth())
                    mPlayer.setPosition(getScreenWidth() - mPlayer.getBound().getHalfWidth(), mPlayer.getPosition().y);
                else if (playerBound.getRight() > LEVEL_WIDTH + playerBound.getHalfWidth())
                    mPlayer.setPosition(0 + mPlayer.getBound().getHalfWidth(), mPlayer.getPosition().y);

                //check if player falls off screen
                if (playerBound.getTop() <= 0)
                {
                    //checks if game ia two player
                    if(mGame.getTwoPlayerToggle())
                    {
                        //if the current player is the second, launch the game over screen, passing in the original player
                        if (mPlayer.getIsSecondPlayer())
                        {
                            mGame.getScreenManager().removeScreen(this.getName());
                            mGame.setPlayer2score(mPlayer.getScoreValue());
                            GameOverScreen gameOverScreen = new GameOverScreen(mGame, mGame.getPlayer1());
                            mGame.getScreenManager().addScreen(gameOverScreen);
                        }
                        //if currently two player mode, and is first player, store first player in mGame, launch main game again with player two
                        else if (!mPlayer.getIsSecondPlayer())
                        {
                            time = elapsedTime.totalTime;
                            pause();
                            dialogBox.go();
                        }
                    }
                    //if one player mode, just go straight to game over screen with current player
                    else{
                        //if the player has bought a life, they get a second chance
                        if (mPlayer.getNumOfLifes() == 1) {
                            new DisplayToast("Second Chance").execute();
                            //destroy the enemies
                            for(Enemy enemy: mBadGuys)
                            {
                                enemy.explode();
                            }
                            //destroy the projectiles
                            for(Projectile projectile: mEnemyProjectiles)
                            {
                                projectile.setIsGone(true);
                            }
                            //set a platform below the player so they don't die instantly
                            mPlatforms.get(0).setPosition(Scale.getX(50), Scale.getY(30));
                            mPlayer.setPosition(Scale.getX(50), Scale.getY(40));
                            //decrease player's life
                            mPlayer.decreaseLife();
                        }
                        else
                        {
                            mGame.getScreenManager().removeScreen(this.getName());
                            GameOverScreen gameOverScreen = new GameOverScreen(mGame, mPlayer);
                            mGame.getScreenManager().addScreen(gameOverScreen);
                        }
                    }
                }
                else if (playerBound.getTop() > LEVEL_HEIGHT)
                    mPlayer.getPosition().y += (playerBound.getTop() + LEVEL_HEIGHT);

                // Ensure the viewport cannot leave the confines of the world
                if (mLayerViewport.getLeft() < 0)
                    mLayerViewport.x -= mLayerViewport.getLeft();
                else if (mLayerViewport.getRight() > LEVEL_WIDTH)
                    mLayerViewport.x -= (mLayerViewport.getRight() - LEVEL_WIDTH);

                if (mLayerViewport.getBottom() < 0)
                    mLayerViewport.y -= mLayerViewport.getBottom();
                else if (mLayerViewport.getTop() > LEVEL_HEIGHT)
                    mLayerViewport.y -= (mLayerViewport.getTop() - LEVEL_HEIGHT);

                //check if player collides with enemy
                if (mPlayer.getHasCollided())
                {
                    //if playing 2 player
                    if(mGame.getTwoPlayerToggle())
                    {
                        //if second player's go, go straight to the GameOverScreen
                        if (mPlayer.getIsSecondPlayer())
                        {
                            mGame.getScreenManager().removeScreen(this.getName());
                            mGame.setPlayer2score(mPlayer.getScoreValue());
                            GameOverScreen gameOverScreen = new GameOverScreen(mGame, mGame.getPlayer1());
                            mGame.getScreenManager().addScreen(gameOverScreen);
                        }
                        //else display dialog box and pause game
                        else if (!mPlayer.getIsSecondPlayer())
                        {
                            time = elapsedTime.totalTime;
                            pause();
                            dialogBox.go();
                        }
                    }
                    else
                    {
                        //if the player has bought a life, they get a second chance
                        if (mPlayer.getNumOfLifes() == 1) {
                            //set the player has not collided
                            mPlayer.setHasCollided(false);
                            new DisplayToast("Second Chance").execute();
                            //destroy enemies
                            for(Enemy enemy: mBadGuys)
                            {
                                enemy.explode();
                            }
                            //destroy projectiles
                            for(Projectile projectile: mEnemyProjectiles)
                            {
                                projectile.setIsGone(true);
                            }
                            //set a platform below the player so they don't die instantly
                            mPlatforms.get(0).setPosition(Scale.getX(50), Scale.getY(30));
                            mPlayer.setPosition(Scale.getX(50), Scale.getY(40));
                            //decrease the player's life
                            mPlayer.decreaseLife();
                        }
                        else
                        {
                            mGame.getScreenManager().removeScreen(this.getName());
                            GameOverScreen gameOverScreen = new GameOverScreen(mGame, mPlayer);
                            mGame.getScreenManager().addScreen(gameOverScreen);
                        }
                    }
                }
            }
        }
        //else if the game is paused (used for a delay between 2 player)
        else
        {
            if (time != 0)
            {
                //if 5 seconds has passed, play the game with 2 player
                if (elapsedTime.totalTime - time > 5)
                {
                    mGame.getScreenManager().removeScreen(this.getName());
                    mGame.setPlayer1score(mPlayer.getScoreValue());
                    mGame.setPlayer1(mPlayer);
                    MainGameScreen mainGameScreen = new MainGameScreen(mGame, mGame.getPlayer2());
                    mGame.getScreenManager().addScreen(mainGameScreen);
                }
            }
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * uk.ac.qub.eeecs.gage.world.GameScreen#draw(uk.ac.qub.eeecs.gage.engine
     * .ElapsedTime, uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D)
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        //draw background
        background.draw(backgroundRect, timer, elapsedTime, graphics2D, mLayerViewport, mScreenViewport, paint);

        // Draw each of the platforms
        for (int i=0; i<mPlatforms.size(); i++)
        {
            if (!mPlatforms.get(i).isGone())
            {
                mPlatforms.get(i).draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
            else
            {
                //drawing the explosive platform animation
                explosionAnimation.draw(graphics2D, mPlatforms.get(i), paint);
                if (explosionAnimation.getImageCount() == explosionAnimation.getFrameCount())
                {
                    availablePlatforms.add(mPlatforms.get(i));
                    availablePlatforms.get(availablePlatforms.size()-1).setIsGone(false);
                    mPlatforms.remove(i);
                    i--;
                    explosionAnimation.setImageCount(0);
                }
            }
        }

        //draw collectibles
        for (int i=0; i<mCollectables.size(); i++)
        {
            if (!mCollectables.get(i).IsGone())
            {
                mCollectables.get(i).draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
            else
            {
                mCollectables.get(i).setIsGone(false);
                availableCollectables.add(mCollectables.get(i));
                mCollectables.remove(mCollectables.get(i));
                i--;
            }
        }

        //draw enemies
        for(int i=0; i<mBadGuys.size();i++) {
            if (!mBadGuys.get(i).isGone())
            {
                mBadGuys.get(i).draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
            else
            {
                explosionAnimation.draw(graphics2D, mBadGuys.get(i), paint);
                if (explosionAnimation.getImageCount() == explosionAnimation.getFrameCount())
                {
                    mBadGuys.get(i).setIsGone(false);
                    availableBadGuys.add(mBadGuys.get(i));
                    mBadGuys.remove(i);
                    i--;
                    explosionAnimation.setImageCount(0);
                }
            }
        }

        //check if projectiles have left screen
        for(int i = 0; i < mProjectiles.size(); i++){
            if(mProjectiles.get(i).isGone())
            {
                availableProjectiles.add(mProjectiles.get(i));
                availableProjectiles.get(availableProjectiles.size()-1).setIsGone(false);
                mProjectiles.remove(i);
            }
        }

        //check if enemy projectiles have left screen
        for (int i=0; i<mEnemyProjectiles.size(); i++)
        {
            if (mEnemyProjectiles.get(i).isGone())
            {
                availableEnemyProjectiles.add(mEnemyProjectiles.get(i));
                availableEnemyProjectiles.get(availableEnemyProjectiles.size()-1).setIsGone(false);
                mEnemyProjectiles.remove(i);
            }
        }

        //draw top bar
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("greyBox"), null, topToolbar, paint);

        //draw player
        mPlayer.draw(graphics2D, mLayerViewport, mScreenViewport, mPlayer.getPaint());

        //draw the controls
        for (SimpleControl simpleControl : mControls)
            simpleControl.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        //draw projectiles
        if (!(mProjectiles.size()==0))
        {
            for (int i=0; i<mProjectiles.size(); i++)
            {
                mProjectiles.get(i).draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
        }

        //draw enemy projectiles
        if (!(mEnemyProjectiles.size() == 0))
        {
            for (int i=0; i<mEnemyProjectiles.size(); i++)
            {
                mEnemyProjectiles.get(i).draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
        }

        //draw pause screen
        if (pause.isPaused()){
            adjustOpacity(mGame.getAssetManager().getBitmap("greyBox"), 150, graphics2D);
            graphics2D.drawText("Your Game is Paused", Scale.getX(30), Scale.getY(50), paint);
        }

        //draw top bar stats
        graphics2D.drawText("Time: " + timer.getTimeElapsed(), Scale.getX(5), Scale.getY(5), paint);
        graphics2D.drawText("Score: " + mPlayer.getScoreValue(), Scale.getX(30), Scale.getY(5), paint);

        //drawing coin animation
        coinAnimation.draw(graphics2D, Scale.getX(80), Scale.getY(1), Scale.getX(89), Scale.getY(6), paint);
        graphics2D.drawText(": " + mPlayer.getNumOfCoins(), Scale.getX(90), Scale.getY(5), paint);
    }

    private void adjustOpacity(Bitmap bitmap, int opacity, IGraphics2D graphics2D)
    {
        Paint paint1 = new Paint();
        paint1.setAlpha(opacity);
        graphics2D.drawBitmap(bitmap, null, PauseMenu, paint1);
    }


}