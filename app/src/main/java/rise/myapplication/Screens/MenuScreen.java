package rise.myapplication.Screens;

/**
 * Created by Ciaran Duncan on 27/11/15.
 */

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rise.myapplication.World.ParticleSystem;
import rise.myapplication.UI.DisplayToast;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.TouchEvent;
import rise.myapplication.Game.Game;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.Util.Scale;
import rise.myapplication.UI.PushButton;
import rise.myapplication.World.GameScreen;


public class MenuScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //background/logo rects
    private final Rect logo;
    private final Rect Background;
    private final Rect twoPlayer;

    //PushButton objects
    private final PushButton playGame;
    private final PushButton twoPlayerToggle;
    private final PushButton leaderboard;
    private final PushButton options;
    private final PushButton exit;
    private final PushButton basket;
    private final PushButton howTo;
    private final PushButton achievements;
    private final ArrayList<PushButton> buttons;

    //Paint object
    private final Paint paint;

    //time variables
    private float timeElapsed;
    private double startTime = 0.00;
    private final int FADE_IN_LENGTH = 3;
    private boolean firstDraw = true;
    private static boolean firstLoad = true;

    //Player object
    private final Player mPlayer;

    private Random random = new Random();
    private ParticleSystem particleSystem = new ParticleSystem(mGame.getAssetManager().getBitmap("SparkParticle"), 80, 50, 50);

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    //this constructor is called the first time the game is loaded
    public MenuScreen(Game game) {
        super("MenuScreen", game);

        //Scaling factors using scaling classa
        Background = new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight());
        logo = new Rect(Scale.getX(10),Scale.getY(2),Scale.getX(90),Scale.getY(15));
        twoPlayer = new Rect(Scale.getX(64), Scale.getY(16), Scale.getX(96), Scale.getY(21));

        //setup PushButtons
        buttons = new ArrayList<>();
        playGame = new PushButton(Scale.getX(35),Scale.getY(25),Scale.getX(60),Scale.getY(16), "playg",  this);
        if (mGame.getTwoPlayerToggle()) {
            twoPlayerToggle = new PushButton(Scale.getX(80), Scale.getY(25), Scale.getX(20), Scale.getY(7), "ToggleOn",  this);
        } else {
            twoPlayerToggle = new PushButton(Scale.getX(80), Scale.getY(25), Scale.getX(20), Scale.getY(7), "ToggleOff", this);
        }
        leaderboard = new PushButton(Scale.getX(45),Scale.getY(40),Scale.getX(60),Scale.getY(16), "leadg",  this);
        options = new PushButton(Scale.getX(55),Scale.getY(56),Scale.getX(60),Scale.getY(16), "optiong",  this);
        exit = new PushButton(Scale.getX(67),Scale.getY(71),Scale.getX(55),Scale.getY(16), "exitg",  this);
        basket = new PushButton(Scale.getX(25),Scale.getY(92),Scale.getX(49),Scale.getY(16), "shopg",  this);
        howTo = new PushButton(Scale.getX(74.5),Scale.getY(92),Scale.getX(49),Scale.getY(16), "helpg",  this);
        achievements = new PushButton(Scale.getX(20),Scale.getY(80),Scale.getX(20),Scale.getY(8), "weestar",  this);
        paint = new Paint();
        startTime = timeElapsed;

        //add PushButtons to ArrayList
        buttons.add(playGame);
        buttons.add(twoPlayerToggle);
        buttons.add(leaderboard);
        buttons.add(options);
        buttons.add(exit);
        buttons.add(basket);
        buttons.add(howTo);
        buttons.add(achievements);

        // Load the player
        //TODO change to scaled value
        mPlayer = new Player(Scale.getX(50), 200.0f, "", this);

        //load achievements
        mPlayer.getAchievements().setCoinAchievementAchieved(getGame().getSharedPreferences().loadAchievement("First Coin Achievement Achieved"));
        mPlayer.getAchievements().setKillsAchievementAchieved(getGame().getSharedPreferences().loadAchievement("First Kill Achievement Achieved"));
        mPlayer.getAchievements().setScoreAchievement1Achieved(getGame().getSharedPreferences().loadAchievement("1000 Points Achievement Achieved"));
        mPlayer.getAchievements().setCoinAchievement1Achieved(getGame().getSharedPreferences().loadAchievement("Ten Coins Achievement Achievement"));
        mPlayer.getAchievements().setKillsAchievement1Achieved(getGame().getSharedPreferences().loadAchievement("Ten Enemies Killed Achievement"));
        mPlayer.getAchievements().setScoreAchievement2Achieved(getGame().getSharedPreferences().loadAchievement("2000 Points Achievement Achieved"));
        mPlayer.getAchievements().setCoinAchievement2Achieved(getGame().getSharedPreferences().loadAchievement("Fifty Coin Achievement Achieved"));
        mPlayer.getAchievements().setKillsAchievement2Achieved(getGame().getSharedPreferences().loadAchievement("Fifty Kills Achievement Achieved"));
        mPlayer.getAchievements().setScoreAchievement3Achieved(getGame().getSharedPreferences().loadAchievement("3000 Points Achievement Achieved"));

        //load coins
        mPlayer.setNumOfCoins(getGame().getSharedPreferences().loadInt("coins", 0));

        //load enemies killed
        mPlayer.setEnemiesKilled(getGame().getSharedPreferences().loadInt("enemiesKilled", 0));

        //load shopLevels
        for(int i = 0; i < mPlayer.getAttributeLevels().length; i++){
            mPlayer.setAttributeLevel(i, getGame().getSharedPreferences().loadInt("ShopLevel"+i,0));
        }
    }

    //this constructor after the game is first loaded when the player goes back to menu screen
    public MenuScreen(Game game, Player player) {
        super("MenuScreen", game);

        //Scaling factors using scaling classa
        Background = new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight());
        logo = new Rect(Scale.getX(10),Scale.getY(2),Scale.getX(90),Scale.getY(15));
        twoPlayer = new Rect(Scale.getX(64), Scale.getY(16), Scale.getX(96), Scale.getY(21));

        //setup PushButtons
        buttons = new ArrayList<>();
        playGame = new PushButton(Scale.getX(35),Scale.getY(25),Scale.getX(60),Scale.getY(16), "playg",  this);
        if (mGame.getTwoPlayerToggle()) {
            twoPlayerToggle = new PushButton(Scale.getX(80), Scale.getY(25), Scale.getX(20), Scale.getY(7), "ToggleOn",  this);
        } else {
            twoPlayerToggle = new PushButton(Scale.getX(80), Scale.getY(25), Scale.getX(20), Scale.getY(7), "ToggleOff", this);
        }
        leaderboard = new PushButton(Scale.getX(45),Scale.getY(40),Scale.getX(60),Scale.getY(16), "leadg",  this);
        options = new PushButton(Scale.getX(55),Scale.getY(56),Scale.getX(60),Scale.getY(16), "optiong",  this);
        exit = new PushButton(Scale.getX(67),Scale.getY(71),Scale.getX(55),Scale.getY(16), "exitg",  this);
        basket = new PushButton(Scale.getX(25),Scale.getY(92),Scale.getX(49),Scale.getY(16), "shopg",  this);
        howTo = new PushButton(Scale.getX(74.5),Scale.getY(92),Scale.getX(49),Scale.getY(16), "helpg",  this);
        achievements = new PushButton(Scale.getX(20),Scale.getY(80),Scale.getX(20),Scale.getY(8), "weestar",  this);
        paint = new Paint();
        startTime = timeElapsed;

        //add PushButtons to ArrayList
        buttons.add(playGame);
        buttons.add(twoPlayerToggle);
        buttons.add(leaderboard);
        buttons.add(options);
        buttons.add(exit);
        buttons.add(basket);
        buttons.add(howTo);
        buttons.add(achievements);

        // Create the player
        mPlayer = player;
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        //update buttons
        for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).update(elapsedTime);
        }

        // Process any touch events occurring since the update
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);
            if(touchEvent.type == TouchEvent.TOUCH_UP) {
                //playGame clicked
                if (playGame.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    MainGameScreen c = new MainGameScreen(mGame, mPlayer);
                    mGame.getScreenManager().addScreen(c);
                    mPlayer.resetScore();
                }

                //twoPlayerToggle clicked
                if (twoPlayerToggle.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    //turn toggle off
                    if (mGame.getTwoPlayerToggle()){
                        mGame.setTwoPlayerToggle(false);
                        twoPlayerToggle.setBitmap("ToggleOff", this);
                        new DisplayToast("Single Player Mode Selected").execute();
                    }
                    //turn toggle on
                    else if (!mGame.getTwoPlayerToggle()){
                        mGame.setTwoPlayerToggle(true);
                        twoPlayerToggle.setBitmap("ToggleOn", this);
                        new DisplayToast("Two Player Mode Selected").execute();
                    }

                }

                //howTo clicked
                if (howTo.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    HowToScreen HowToScreen = new HowToScreen(mGame);
                    mGame.getScreenManager().addScreen(HowToScreen);
                }

                //basket clicked
                if (basket.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    ShopScreen shop = new ShopScreen(mGame, "MainMenu", mPlayer);
                    mGame.getScreenManager().addScreen(shop);
                }

                //options clicked
                if (options.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    OptionsScreen options = new OptionsScreen(mGame, "MainMenu", mPlayer);
                    mGame.getScreenManager().addScreen(options);
                }

                //leaderboard clicked
                if (leaderboard.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    Leaderboard leaderboard = new Leaderboard(mGame, mPlayer);
                    mGame.getScreenManager().addScreen(leaderboard);
                }

                //achievements clicked
                if (achievements.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    AchievementScreen achievement = new AchievementScreen(mGame, mPlayer);
                    mGame.getScreenManager().addScreen(achievement);
                }

                //exit clicked
                if (exit.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    System.exit(0);
                }
            }
        }
        particleSystem.update();
    }

    //draw MenuScreen
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        //if firstDraw set startTime
        if(firstDraw){
            startTime = elapsedTime.totalTime;
            firstDraw = false;
        }

        //set timeElapsed
        timeElapsed = (float)(elapsedTime.totalTime - startTime);

        //draw the background BLUE
        graphics2D.clear(Color.BLUE);

        //draw the space background
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SpaceBackground"), null, Background, paint);

        //draw the particles in the particle system
        particleSystem.draw(graphics2D);

        //draw the game logo
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Logo"), null, logo, paint);

        //fade in effect
        if(firstLoad){
            if(timeElapsed < FADE_IN_LENGTH)
            {
                paint.setAlpha((int)(timeElapsed * (255/FADE_IN_LENGTH)));
            }
            else
            {
                paint.setAlpha(255);
                firstLoad = false;
            }
            //draw PushButtons
            for(int i = 0; i < buttons.size(); i++)
            {
                buttons.get(i).draw(graphics2D, paint);
            }
        }
        else
        {
            //draw PushButtons
            for(int i = 0; i < buttons.size(); i++)
            {
                buttons.get(i).draw(graphics2D);
            }
        }

        //draw the 2 player image
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("twoPlayer"), null, twoPlayer, paint);

        //reset Alpha value
        paint.setAlpha(255);
    }
}