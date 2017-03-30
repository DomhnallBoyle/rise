package rise.myapplication.Screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import rise.myapplication.UI.Animation;
import rise.myapplication.UI.DisplayToast;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.TouchEvent;
import rise.myapplication.Game.Game;
import rise.myapplication.World.LayerViewport;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.World.GameObjects.Platform;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.Util.Scale;
import rise.myapplication.World.ScreenViewport;
import rise.myapplication.World.GameScreen;
import rise.myapplication.UI.PushButton;

/**
 * Created by DomhnallBoyle on 02/12/2015.
 */
public class ShopScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //declare rects
    private final Rect doodleBackground;
    private final Rect blackBackground;
    private final Rect storeLogo;

    //declare PushButtons
    private final PushButton buyButton;
    private final PushButton backButton;

    //declare ArrayList of PushButtons
    private final ArrayList<PushButton> buttons;
    private final String previousScreen;
    private final Rect[] mPlatforms;

    //declare base platform
    private Platform platform;

    //declare shop item rects
    private Rect shield, rocket, heartLife, multiplier, magnet, springShoes;

    //declare Player object
    private final Player mPlayer;

    //declare Viewports
    private final ScreenViewport mScreenViewport;
    private final LayerViewport mLayerViewport;

    //declare Paint objects
    private final Paint backgroundPaint;
    private final Paint textPaint;

    //declare coin animation
    private final Animation coinAnimation;

    //declare ints to store shop item pressed
    private int textToDraw, pressed;

    //declare shop item descriptions
    private final String[] descriptions = {"This item protects you from enemies",
                                    "This item will project you into space.",
                                    "This item will put a spring in your step.",
                                    "Multiplies your coins & score for a time period.",
                                    "This item will act as a magnet for coins.",
                                    "You gain a life. You can use 1 life per game.",
                                     };

    //declare cost HashMap
    private final HashMap<String, Integer> costs;

    //declare insufficientFunds boolean
    private boolean insufficientFunds = false;

    private static final int NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE = 5;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public ShopScreen(Game game, String fromScreen, Player player)
    {
        super("ShopScreen", game);

        //set previous screen
        this.previousScreen = fromScreen;

        //TODO set achievements = player achievements
        /*mPlayer = new Player(Scale.getX(50), 200.0f, player.getName(), this);
        mPlayer.setNumOfCoins(player.getNumOfCoins());
        mPlayer.setBitmap(player.getBitmap());
        mPlayer.setPosition(Scale.getX(50), Scale.getY(43));
        mPlayer.setShopLevels(player.getShopLevels());
        mPlayer.setScoreValue(player.getScoreValue());*/
        
        //set Player object
        player.reset();
        mPlayer = player;
        mPlayer.setNumOfCoins(0);

        //set item costs
        costs = new HashMap<>();
        costs.put("Shield", 5);
        costs.put("Rocket", 5);
        costs.put("Heart", 100);
        costs.put("Multiplier", 5);
        costs.put("Magnet", 5);
        costs.put("Springs", 5);

        //set paint objects
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.DKGRAY);
        Typeface plain = Typeface.createFromAsset(MainActivity.getContext().getAssets(), String.format(Locale.US, "Fonts/%s", "Tiki Tropic Bold.ttf"));
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        textPaint = new Paint();
        textPaint.setTextSize(50);
        textPaint.setTypeface(bold);
        textPaint.setColor(Color.WHITE);

        //setup coin animation
        coinAnimation = new Animation(mGame.getAssetManager().getBitmap("CoinAnimation"), 8);
        coinAnimation.play(2, true);

        //setup viewports
        mScreenViewport = new ScreenViewport();
        mLayerViewport = new LayerViewport(Scale.getX(50), Scale.getY(60), Scale.getX(50), Scale.getY(60));

        //set rect positions
        doodleBackground = new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight());
        blackBackground = new Rect(0, Scale.getY(85), Scale.getX(100), Scale.getY(100));
        storeLogo = new Rect(Scale.getX(20), Scale.getY(2), Scale.getX(80), Scale.getY(15));

        //set PushButton positions
        backButton = new PushButton(Scale.getX(97.5), Scale.getY(17.5), Scale.getX(5), Scale.getY(5), "Exit", this);
        buttons = new ArrayList<>();
        buyButton = new PushButton(Scale.getX(50), Scale.getY(77.5), Scale.getX(20), Scale.getY(5), "BuyButton", this);

        //setup buttons ArrayList
        buttons.add(buyButton);
        buttons.add(backButton);
        mPlatforms = new Rect[6];

        //create platforms and items
        createPlatforms();
        createItems();
    }

    //setup platform objects
    private void createPlatforms() {
        platform = new Platform(Scale.getX(50), Scale.getY(35), Platform.PlatformType.NORMAL, this);
        mPlatforms[0] = new Rect(Scale.getX(10), Scale.getY(30), Scale.getX(30), Scale.getY(32));
        mPlatforms[1] = new Rect(Scale.getX(10), Scale.getY(45), Scale.getX(30), Scale.getY(47));
        mPlatforms[2] = new Rect(Scale.getX(10), Scale.getY(60), Scale.getX(30), Scale.getY(62));
        mPlatforms[3] = new Rect(Scale.getX(70), Scale.getY(30), Scale.getX(90), Scale.getY(32));
        mPlatforms[4] = new Rect(Scale.getX(70), Scale.getY(45), Scale.getX(90), Scale.getY(47));
        mPlatforms[5] = new Rect(Scale.getX(70), Scale.getY(60), Scale.getX(90), Scale.getY(62));
    }

    //setup item objects
    private void createItems()
    {
        shield = new Rect(Scale.getX(14), Scale.getY(22), Scale.getX(27), Scale.getY(30));
        rocket = new Rect(Scale.getX(14), Scale.getY(37), Scale.getX(27), Scale.getY(45));
        springShoes = new Rect(Scale.getX(14), Scale.getY(52), Scale.getX(27), Scale.getY(60));
        multiplier = new Rect(Scale.getX(74), Scale.getY(22), Scale.getX(87), Scale.getY(30));
        magnet = new Rect(Scale.getX(74), Scale.getY(37), Scale.getX(87), Scale.getY(45));
        heartLife = new Rect(Scale.getX(74), Scale.getY(52), Scale.getX(87), Scale.getY(60));
    }

    //update ShopScreen
    @Override
    public void update(ElapsedTime elapsedTime) {
        //set Input object
        Input input = mGame.getInput();

        //update buttons
        for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).update(elapsedTime);
        }

        //check TouchEvents
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);
            if(touchEvent.type == TouchEvent.TOUCH_UP) {
                //if backButton pressed and last screen was MainMenu
                if (backButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y) && this.previousScreen.equalsIgnoreCase("MainMenu")) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    MenuScreen menuScreen = new MenuScreen(mGame, mPlayer);
                    mGame.getScreenManager().addScreen(menuScreen);
                }
                //if backButton pressed and last screen was GameOver
                if (backButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y) && this.previousScreen.equalsIgnoreCase("GameOver")) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    GameOverScreen gameOverScreen = new GameOverScreen(mGame, mPlayer);
                    mGame.getScreenManager().addScreen(gameOverScreen);
                }
                //if buyButton pressed
                if (buyButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    //update levels if purchase successful
                    if (checkRequirements(textToDraw)) {
                        switch (pressed) {
                            case 0:
                                if (mPlayer.getAttributeLevel(0) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                                    mPlayer.increaseLevelAt(0); //shield
                                break;
                            case 1:
                                if (mPlayer.getAttributeLevel(1) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                                    mPlayer.increaseLevelAt(1); //rocket
                                break;
                            case 2:
                                if (mPlayer.getAttributeLevel(2) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                                    mPlayer.increaseLevelAt(2); //spring shoes
                                break;
                            case 3:
                                if (mPlayer.getAttributeLevel(3) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                                    mPlayer.increaseLevelAt(3); //multiplier
                                break;
                            case 4:
                                if (mPlayer.getAttributeLevel(4) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                                    mPlayer.increaseLevelAt(4); //magnet
                                break;
                            case 5:
                                if (mPlayer.getAttributeLevel(5) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                                {
                                    mPlayer.increaseLevelAt(5); //heart lifes
                                }
                                break;
                        }
                        mPlayer.increaseAttributes();

                        //save shopLevels
                        for(int i = 0; i < mPlayer.getAttributeLevels().length; i++){
                            getGame().getSharedPreferences().saveInt("ShopLevel" + i, mPlayer.getAttributeLevels()[i]);
                            getGame().getSharedPreferences().saveInt("coins",mPlayer.getNumOfCoins());
                        }
                    } else {
                        insufficientFunds = true;
                    }
                }
                //if shield clicked
                if (shield.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    pressed = 0;
                    setTextToDraw(1);
                    insufficientFunds = false;
                }
                //if rocket clicked
                if (rocket.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    pressed = 1;
                    setTextToDraw(2);
                    insufficientFunds = false;
                }
                //if springs clicked
                if (springShoes.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    pressed = 2;
                    setTextToDraw(3);
                    insufficientFunds = false;
                }
                //if score multiplier clicked
                if (multiplier.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    pressed = 3;
                    setTextToDraw(4);
                    insufficientFunds = false;
                }
                //if magnet clicked
                if (magnet.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    pressed = 4;
                    setTextToDraw(5);
                    insufficientFunds = false;
                }
                //if coin multiplier clicked
                if (heartLife.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    pressed = 5;
                    setTextToDraw(6);
                    insufficientFunds = false;
                }
            }
        }
        //update player
        mPlayer.update(elapsedTime, platform);

        //update coin animation
        coinAnimation.update(elapsedTime);
    }

    //draw ShopScreen
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //draw background and store logo
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SpaceBackground"), null, doodleBackground, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("StoreLogo"), null, storeLogo, null);

        //draw buttons
        for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).draw(graphics2D);
        }

        //draw black background
        graphics2D.drawRect(blackBackground, backgroundPaint);

        //draw shopLevel progress
        for (int i=0; i<mPlatforms.length; i++)
        {
            switch(mPlayer.getAttributeLevels()[i])
            {
                case 0: graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Multiplier0"), null, mPlatforms[i], null); break;
                case 1: graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Multiplier1"), null, mPlatforms[i], null); break;
                case 2: graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Multiplier2"), null, mPlatforms[i], null); break;
                case 3: graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Multiplier3"), null, mPlatforms[i], null); break;
                case 4: graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Multiplier4"), null, mPlatforms[i], null); break;
                case 5: graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Multiplier5"), null, mPlatforms[i], null); break;
                default: graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Multiplier5"), null, mPlatforms[i], null); break;
            }
        }

        //draw shop items
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Shield"), null, shield, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Rocket"), null, rocket, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Heart"), null, heartLife, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Multiplier"), null, multiplier, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Magnet"), null, magnet, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Springs"), null, springShoes, null);

        //draw player
        mPlayer.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        //draw base platform
        platform.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

        //draw text
        drawText(graphics2D);

        //draw coin animation
        coinAnimation.setSourceRect();
        coinAnimation.getScreenRect().set(Scale.getX(42), Scale.getY(17), Scale.getX(52), Scale.getY(22));
        graphics2D.drawBitmap(coinAnimation.getBitmap(), coinAnimation.getSourceRect(), coinAnimation.getScreenRect(), null);

        //draw number of coins available
        graphics2D.drawText("" + mPlayer.getNumOfCoins(), Scale.getX(55), Scale.getY(21), textPaint);

        //set insufficient fund text
        if (insufficientFunds)
        {
            setTextToDraw(0);
            graphics2D.drawText("You do not have enough coins to buy this", Scale.getX(2), Scale.getY(89), textPaint);
        }
    }

    //set text to be drawn
    private void setTextToDraw(int description)
    {
        this.textToDraw = description;
    }

    //display appropriate text when item pressed
    private void drawText(IGraphics2D graphics2D)
    {
        switch(textToDraw)
        {
            case 1: graphics2D.drawText("Shield:", Scale.getX(2), Scale.getY(89), textPaint);
                    graphics2D.drawText(descriptions[0], Scale.getX(2), Scale.getY(92), textPaint);
                    graphics2D.drawText("Cost: " + costs.get("Shield").toString(), Scale.getX(2), Scale.getY(95), textPaint);
                    break;
            case 2: graphics2D.drawText("Rocket:", Scale.getX(2), Scale.getY(89), textPaint);
                    graphics2D.drawText(descriptions[1], Scale.getX(2), Scale.getY(92), textPaint);
                    graphics2D.drawText("Cost: " + costs.get("Rocket").toString(), Scale.getX(2), Scale.getY(95), textPaint);
                    break;
            case 3: graphics2D.drawText("Springs:", Scale.getX(2), Scale.getY(89), textPaint);
                    graphics2D.drawText(descriptions[2], Scale.getX(2), Scale.getY(92), textPaint);
                    graphics2D.drawText("Cost: " + costs.get("Multiplier").toString(), Scale.getX(2), Scale.getY(95), textPaint);
                    break;
            case 4: graphics2D.drawText("Multiplier:", Scale.getX(2), Scale.getY(89), textPaint);
                    graphics2D.drawText(descriptions[3], Scale.getX(2), Scale.getY(92), textPaint);
                    graphics2D.drawText("Cost: " + costs.get("Magnet").toString(), Scale.getX(2), Scale.getY(95), textPaint);
                    break;
            case 5: graphics2D.drawText("Magnet:", Scale.getX(2), Scale.getY(89), textPaint);
                    graphics2D.drawText(descriptions[4], Scale.getX(2), Scale.getY(92), textPaint);
                    graphics2D.drawText("Cost: " + costs.get("Springs").toString(), Scale.getX(2), Scale.getY(95), textPaint);
                    break;
            case 6: graphics2D.drawText("Heart:", Scale.getX(2), Scale.getY(89), textPaint);
                    graphics2D.drawText(descriptions[5], Scale.getX(2), Scale.getY(92), textPaint);
                    graphics2D.drawText("Cost: " + costs.get("Heart").toString(), Scale.getX(2), Scale.getY(95), textPaint);
                    break;
            default:
                    break;
        }
    }

    //check if player has enough coins to purchase item
    private boolean checkRequirements(int textToDraw) {
        boolean hasEnoughCoins = false;
        switch (textToDraw) {
            case 1:
                if (mPlayer.getNumOfCoins() < costs.get("Shield")) {
                    hasEnoughCoins = false;
                }
                else
                {
                    hasEnoughCoins = true;
                    if (mPlayer.getAttributeLevel(0) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                    {
                        mPlayer.setNumOfCoins(mPlayer.getNumOfCoins() - costs.get("Shield"));
                    }
                    else
                    {
                        new DisplayToast("Cannot buy anymore upgrades").execute();
                    }
                }
                break;
            case 2:
                if (mPlayer.getNumOfCoins() < costs.get("Rocket")) {
                    hasEnoughCoins = false;
                }
                else
                {
                    hasEnoughCoins = true;
                    if (mPlayer.getAttributeLevel(1) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                    {
                        mPlayer.setNumOfCoins(mPlayer.getNumOfCoins() - costs.get("Rocket"));
                    }
                    else
                    {
                        new DisplayToast("Cannot buy anymore upgrades").execute();
                    }
                }
                break;
            case 3:
                if (mPlayer.getNumOfCoins() < costs.get("Multiplier")) {
                    hasEnoughCoins = false;
                }
                else
                {
                    hasEnoughCoins = true;
                    if (mPlayer.getAttributeLevel(2) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                    {
                        mPlayer.setNumOfCoins(mPlayer.getNumOfCoins() - costs.get("Multiplier"));
                    }
                    else
                    {
                        new DisplayToast("Cannot buy anymore upgrades").execute();
                    }
                }
                break;
            case 4:
                if (mPlayer.getNumOfCoins() < costs.get("Magnet")) {
                    hasEnoughCoins = false;
                }
                else
                {
                    hasEnoughCoins = true;
                    if (mPlayer.getAttributeLevel(3) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                    {
                        mPlayer.setNumOfCoins(mPlayer.getNumOfCoins() - costs.get("Magnet"));
                    }
                    else
                    {
                        new DisplayToast("Cannot buy anymore upgrades").execute();
                    }
                }
                break;
            case 5:
                if (mPlayer.getNumOfCoins() < costs.get("Springs")) {
                    hasEnoughCoins = false;
                }
                else
                {
                    hasEnoughCoins = true;
                    if (mPlayer.getAttributeLevel(4) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                    {
                        mPlayer.setNumOfCoins(mPlayer.getNumOfCoins() - costs.get("Springs"));
                    }
                    else
                    {
                        new DisplayToast("Cannot buy anymore upgrades").execute();
                    }
                }
                break;
            case 6:
                if (mPlayer.getNumOfCoins() < costs.get("Heart")) {
                    hasEnoughCoins = false;
                }
                else
                {
                    hasEnoughCoins = true;
                    if (mPlayer.getAttributeLevel(5) < NUMBER_OF_ALLOWED_COLLECTIBLE_TYPE)
                    {
                        mPlayer.setNumOfCoins(mPlayer.getNumOfCoins() - costs.get("Heart"));
                    }
                    else
                    {
                        new DisplayToast("Cannot buy anymore upgrades").execute();
                    }
                }
                break;
        }
        return hasEnoughCoins;
    }
}
