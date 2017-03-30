package rise.myapplication.Screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.List;
import java.util.Locale;

import rise.myapplication.Util.Achievements;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.TouchEvent;
import rise.myapplication.Game.Game;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.Util.Scale;
import rise.myapplication.UI.PushButton;
import rise.myapplication.World.GameScreen;

/**
 * Created by Malachy on 17/02/2016.
 */
public class AchievementScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final Rect helpLogo, background, padlockIcon, padlockIcon2, padlockIcon3, padlockIcon4, padlockIcon5, padlockIcon6, padlockIcon7, padlockIcon8, padlockIcon9;
    private final PushButton backButton;
    private final Achievements achievements;
    private final Paint paint;
    private final Player player;

    private String[] descriptions = {"Achievement 1: Get your First Coin in the Game",
                                    "Achievement 1: Get your First Coin in the Game",
                                    "Achievement 2: Shoot your First Enemy in the Game",
                                    "Achievement 3: Score 1000 points in the Game",
                                    "Achievement 4: Collect 10 Coins",
                                    "Achievement 5: Kill 10 Enemies",
                                    "Achievement 6: Score 2000 points in the Game",
                                    "Achievement 7: Collect 50 Coins",
                                    "Achievement 8: Kill 20 Enemies",
                                    "Achievement 9: Collect 3000 points"};

    private boolean clicked=false;
    private final Paint togglePaint = new Paint();
    private String textToDraw="";

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public AchievementScreen(Game game, Player player)
    {
        super("AchievementScreen", game);
        achievements=player.getAchievements();
        int spacingX = game.getScreenWidth();
        int spacingY = game.getScreenHeight();
        helpLogo = new Rect(Scale.getX(20),Scale.getY(0),Scale.getX(80),Scale.getY(15));
        padlockIcon = new Rect(Scale.getX(10),Scale.getY(15),Scale.getX(30),Scale.getY(35));
        padlockIcon2 = new Rect(Scale.getX(40),Scale.getY(15),Scale.getX(60),Scale.getY(35));
        padlockIcon3 = new Rect(Scale.getX(70),Scale.getY(15),Scale.getX(90),Scale.getY(35));
        padlockIcon4 = new Rect(Scale.getX(10),Scale.getY(40),Scale.getX(30),Scale.getY(60));
        padlockIcon5= new Rect(Scale.getX(40),Scale.getY(40),Scale.getX(60),Scale.getY(60));
        padlockIcon6 = new Rect(Scale.getX(70),Scale.getY(40),Scale.getX(90),Scale.getY(60));
        padlockIcon7 = new Rect(Scale.getX(10),Scale.getY(65),Scale.getX(30),Scale.getY(85));
        padlockIcon8 = new Rect(Scale.getX(40),Scale.getY(65),Scale.getX(60),Scale.getY(85));
        padlockIcon9= new Rect(Scale.getX(70),Scale.getY(65),Scale.getX(90),Scale.getY(85));
        background = new Rect(0,0,spacingX,spacingY);
        this.player=player;
        backButton = new PushButton(Scale.getX(95), Scale.getY(15), Scale.getX(10), Scale.getY(10), "Exit", this);

        paint = new Paint();
        Typeface plain = Typeface.createFromAsset(MainActivity.getContext().getAssets(), String.format(Locale.US, "Fonts/%s", "Tiki Tropic Bold.ttf"));
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTypeface(bold);
        paint.setFakeBoldText(true);
        paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
        togglePaint.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.OVERLAY));
    }

    //overriding the update method from the Gamescreen
    @Override
    public void update(ElapsedTime elapsedTime) {

        //update the back button
        backButton.update(elapsedTime);

        // Process any touch events occurring since the update
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);
            if (backButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y ) && touchEvent.type == TouchEvent.TOUCH_UP) {
                mGame.getScreenManager().removeScreen(this.getName());
                MenuScreen menuScreen = new MenuScreen(mGame);
                mGame.getScreenManager().addScreen(menuScreen);
            }
        }
        List<TouchEvent> touchEvents1 = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents1.get(0);
            if (touchEvent.type == TouchEvent.TOUCH_UP) {
                if (padlockIcon.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    clicked=true;
                   setTextToDraw(descriptions[0]);
                }
                if (padlockIcon2.contains((int) touchEvent.x, (int) touchEvent.y)) {
                   clicked=true;
                    setTextToDraw(descriptions[1]);
                }
                if (padlockIcon3.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    clicked=true;
                    setTextToDraw(descriptions[2]);
                }
                if (padlockIcon4.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    clicked=true;
                    setTextToDraw(descriptions[3]);
                }
                if (padlockIcon5.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    clicked=true;
                    setTextToDraw(descriptions[4]);
                }
                if (padlockIcon6.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    clicked=true;
                    setTextToDraw(descriptions[5]);
                }
                if (padlockIcon7.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    clicked=true;
                    setTextToDraw(descriptions[6]);
                }
                if (padlockIcon8.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    clicked=true;
                    setTextToDraw(descriptions[7]);
                }
                if (padlockIcon9.contains((int) touchEvent.x, (int) touchEvent.y)) {
                    clicked=true;
                    setTextToDraw(descriptions[8]);
                }
            }
        }

        //achievements.checkAchievements(player);
        if(player.getAchievements().getCoinAchievementAchieved()) {
            setString(0, achievements.getGainFirstCoin());
        }
        if(player.getAchievements().getKillsAchievementAchieved()) {
            setString(1, achievements.getGainFirstEnemy());
        }
        if(player.getAchievements().getScoreAchievement1Achieved()){
            setString(2, achievements.getGainFirstAchievement());
        }
        if(player.getAchievements().getCoinAchievement1Achieved()){
            setString(3, achievements.getTenthCoinAchievement());
        }
        if(player.getAchievements().getEnemyAchievement1Achieved()){
            setString(4, achievements.getGainTenthEnemyAchievement());
        }
        if(player.getAchievements().getScoreAchievement2Achieved()){
            setString(5, achievements.getGainScoreAchievement2Achievement());
        }
        if(player.getAchievements().getCoinAchievement2Achieved()){
            setString(6, achievements.getGainFiftyCoinAchievement());
        }
        if(player.getAchievements().getKillsAchievement2Achieved()){
            setString(7, achievements.getGainTwentyEnemyAchievement());
        }
        if(player.getAchievements().getScoreAchievement3Achieved()){
            setString(8, achievements.getGainScoreAchievement3Achievement());
        }
    }

    //overrideing the draw method from the Gamescreen
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        //drawing bitmaps to screen
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SpaceBackground"), null, background, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("achieveButt"), null, helpLogo, null);

        //if an achievement has been clicked, draw the appropriate text
        if(clicked=true){
            graphics2D.drawText(getTextToDraw(),Scale.getX(10),Scale.getY(95),paint);
        }

        //depending on what achievments the player has, draw whether the padlock is locked or unlocked
        if(player.getAchievements().getCoinAchievementAchieved()) {
           graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("unlockedPadlock"), null, padlockIcon, paint);
        }
        else{
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("lockedPadlock"), null, padlockIcon, paint);
        }

        if(player.getAchievements().getScoreAchievement1Achieved()){
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("unlockedPadlock"), null, padlockIcon2, paint);
        }
        else{
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("lockedPadlock"), null, padlockIcon2, paint);
        }
        if(player.getAchievements().getKillsAchievementAchieved()){
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("unlockedPadlock"), null, padlockIcon3, paint);
        }
        else{
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("lockedPadlock"), null, padlockIcon3, paint);
        }
        if(player.getAchievements().getCoinAchievement1Achieved()){
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("unlockedPadlock"), null, padlockIcon4, paint);
        }
        else{
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("lockedPadlock"), null, padlockIcon4, paint);
        }
        if(player.getAchievements().getEnemyAchievement1Achieved()){
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("unlockedPadlock"), null, padlockIcon5, paint);
        }
        else{
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("lockedPadlock"), null, padlockIcon5, paint);
        }
        if(player.getAchievements().getScoreAchievement2Achieved()){
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("unlockedPadlock"), null, padlockIcon6, paint);
        }
        else{
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("lockedPadlock"), null, padlockIcon6, paint);
        }
        if(player.getAchievements().getCoinAchievement2Achieved()){
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("unlockedPadlock"), null, padlockIcon7, paint);
        }
        else{
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("lockedPadlock"), null, padlockIcon7, paint);
        }
        if(player.getAchievements().getKillsAchievement2Achieved()){
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("unlockedPadlock"), null, padlockIcon8, paint);
        }
        else{
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("lockedPadlock"), null, padlockIcon8, paint);
        }
        if(player.getAchievements().getScoreAchievement3Achieved()){
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("unlockedPadlock"), null, padlockIcon9, paint);
        }
        else{
            graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("lockedPadlock"), null, padlockIcon9, paint);
        }

        //draw the backbutton
        backButton.draw(graphics2D);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    //method to set the description of the achievements at a particular index
    private void setString(int index, String string)
    {
        descriptions[index] = string;
    }

    //method set the text to draw string on screen
    private void setTextToDraw(String description)
    {
        this.textToDraw = description;
    }

    private String getTextToDraw(){return textToDraw;}


}
