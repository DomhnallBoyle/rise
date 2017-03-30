package rise.myapplication.Screens;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rise.myapplication.Util.Facebook;
import rise.myapplication.Util.Twitter;
import rise.myapplication.UI.DisplayToast;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.KeyBoard;
import rise.myapplication.Engine.Input.TouchEvent;
import rise.myapplication.Game.Game;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.Util.Scale;
import rise.myapplication.UI.PushButton;
import rise.myapplication.World.GameScreen;

/**
 * Created by DomhnallBoyle on 02/12/2015.
 */
public class GameOverScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final Rect background, gameOver, showKeyboard;
    private PushButton playAgain, mainMenu, shop, facebookButton, twitterButton;
    private final ArrayList<PushButton> buttons;
    private final Player mPlayer;
    private final Paint textPaint, keyBoardButtonPaint;
    private final KeyBoard keyBoard;
    private Intent intent;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public GameOverScreen(Game game, Player player)
    {
        super("GameOverScreen", game);

        //save player coins
        mGame.getSharedPreferences().saveInt("coins", player.getNumOfCoins());

        //save enemies killed
        mGame.getSharedPreferences().saveInt("enemiesKilled", player.getEnemiesKilled());

        //instantiating rects
        background = new Rect(0, 0, Scale.getX(100), Scale.getY(100));
        gameOver = new Rect(Scale.getX(20), Scale.getY(11), Scale.getX(80), Scale.getY(25));
        buttons = new ArrayList<>();
        loadButtons();
        showKeyboard = new Rect(Scale.getX(25), Scale.getY(32), Scale.getX(80), Scale.getY(37));

        //setting up the player
        this.mPlayer = player;
        mPlayer.reset();

        //setting up the paint objects
        Typeface plain = Typeface.createFromAsset(MainActivity.getContext().getAssets(), String.format(Locale.US, "Fonts/%s", "Tiki Tropic Bold.ttf"));
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        textPaint=new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(70);
        textPaint.setTypeface(bold);
        textPaint.setFakeBoldText(true);
        textPaint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
        keyBoardButtonPaint = new Paint();

        keyBoardButtonPaint.setAlpha(100);

        //if multiplayer is on, display whether player 1 or player 2 won the game
        if (mGame.getTwoPlayerToggle())
        {
            if (mGame.getPlayer1Score() > mGame.getPlayer2Score())
            {
                new DisplayToast("Player 1 wins").execute();
            }
            else if (mGame.getPlayer2Score() > mGame.getPlayer1Score())
            {
                new DisplayToast("Player 2 wins").execute();
            }
            else
            {
                new DisplayToast("Draw game").execute();
            }
        }

        //loading the keyboard
        keyBoard = new KeyBoard();

        //setting the player name from the shared preferences
        mPlayer.setName(mGame.getSharedPreferences().loadString());

        //setting up the intent for Facebook & Twitter integration
        //intent used to create a new activity
        intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    //overriding the GameScreen's update method
    @Override
    public void update(ElapsedTime elapsedTime) {

        //updating the keyboard buttons
        for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).update(elapsedTime);
        }

        // Process any touch events occurring since the update
        //checking to see if any buttons have been pressed
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);
            if(touchEvent.type == TouchEvent.TOUCH_UP) {

                if (playAgain.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y) && playAgain.isVisible()) {
                    //validation to make sure the player's name is not empty
                    if (!(mPlayer.getName().isEmpty()))
                    {
                        //saving the player's name and score value to shared preferences
                        mGame.getSharedPreferences().saveString(mPlayer.getName());
                        mGame.getSharedPreferences().saveScore(mPlayer.getName(), mPlayer.getScoreValue());

                        //if the database is connected globally, save the player's name and score value to the online database
                        if (mGame.getDatabaseHelper().getConnected())
                        {
                            mGame.getDatabaseHelper().savePlayer(mPlayer.getName(), mPlayer.getScoreValue());
                        }
                        else
                        {
                            new DisplayToast("Could not save score globally, check internet connection").execute();
                        }

                        //change screen and reset the player
                        mGame.getScreenManager().removeScreen(this.getName());
                        mPlayer.reset();
                        MainGameScreen mG = new MainGameScreen(mGame, mPlayer);
                        mGame.getScreenManager().addScreen(mG);
                        mPlayer.resetScore();

                    }
                    else
                    {
                        new DisplayToast("Please enter a player name").execute();
                    }
                }

                if (mainMenu.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y) && mainMenu.isVisible()) {
                    if (!mGame.getTwoPlayerToggle())
                    {
                        if (!(mPlayer.getName().isEmpty()))
                        {
                            //saving the player's name and score value to shared preferences
                            mGame.getSharedPreferences().saveString(mPlayer.getName());
                            mGame.getSharedPreferences().saveScore(mPlayer.getName(), mPlayer.getScoreValue());

                            //if the database is connected globally, save the player's name and score value to the online database
                            if (mGame.getDatabaseHelper().getConnected())
                            {
                                mGame.getDatabaseHelper().savePlayer(mPlayer.getName(), mPlayer.getScoreValue());
                            }
                            else
                            {
                                new DisplayToast("Could not save score globally, check internet connection").execute();
                            }

                            //change screen and reset the player
                            mGame.getScreenManager().removeScreen(this.getName());
                            mPlayer.reset();
                            MenuScreen menuScreen = new MenuScreen(mGame, mPlayer);
                            mGame.getScreenManager().addScreen(menuScreen);

                        }
                        else
                        {
                            new DisplayToast("Please enter a player name").execute();
                        }
                    }
                    else
                    {
                        mGame.getScreenManager().removeScreen(this.getName());
                        MenuScreen menuScreen = new MenuScreen(mGame);
                        mGame.getScreenManager().addScreen(menuScreen);
                    }
                }

                if (shop.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y) && shop.isVisible()) {
                    //change screen and reset the player
                    mGame.getScreenManager().removeScreen(this.getName());
                    mGame.getSharedPreferences().saveString(mPlayer.getName());
                    mPlayer.reset();
                    mPlayer.setPosition(Scale.getX(50), Scale.getY(50));
                    ShopScreen shopScreen = new ShopScreen(mGame, "GameOver", mPlayer);
                    mGame.getScreenManager().addScreen(shopScreen);
                }

                //if you press the show keyboard rect, show the keyboard (only show keyboard if not on multiplayer)
                if (showKeyboard.contains((int) touchEvent.x, (int) touchEvent.y) && !mGame.getTwoPlayerToggle()) {
                    mGame.getAssetManager().getSound("ButtonClick").play();
                    keyBoard.setShowing(true);
                  //  new Keyboard2().execute();
                }

                //share to facebook
                if (facebookButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y) && facebookButton.isVisible()) {

                    //if multiplayer is not activated, save the player's score
                    if (!mGame.getTwoPlayerToggle())
                    {
                        //saving the player's name and score value to shared preferences
                        mGame.getSharedPreferences().saveString(mPlayer.getName());
                        mGame.getSharedPreferences().saveScore(mPlayer.getName(), mPlayer.getScoreValue());

                        //if the database is connected globally, save the player's name and score value to the online database
                        if (mGame.getDatabaseHelper().getConnected())
                        {
                            mGame.getDatabaseHelper().savePlayer(mPlayer.getName(), mPlayer.getScoreValue());
                        }
                        else
                        {
                            new DisplayToast("Could not save score globally, check internet connection").execute();
                        }
                    }

                    new Facebook(intent).execute();
                }

                //share to twitter
                if (twitterButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y) && twitterButton.isVisible()) {

                    //if multiplayer is not activated, save the player's score
                    if (!mGame.getTwoPlayerToggle())
                    {
                        //saving the player's name and score value to shared preferences
                        mGame.getSharedPreferences().saveString(mPlayer.getName());
                        mGame.getSharedPreferences().saveScore(mPlayer.getName(), mPlayer.getScoreValue());

                        //if the database is connected globally, save the player's name and score value to the online database
                        if (mGame.getDatabaseHelper().getConnected())
                        {
                            mGame.getDatabaseHelper().savePlayer(mPlayer.getName(), mPlayer.getScoreValue());
                        }
                        else
                        {
                            new DisplayToast("Could not save score globally, check internet connection").execute();
                        }
                    }

                    new Twitter(mGame, mPlayer, intent).execute();
                }
            }

        }

        //if the keyboard is showing, update the keyboard and perform validation on the player's name
        if (keyBoard.getShowing())
        {
            mPlayer.setName(keyBoard.update(this));
            mPlayer.checkName(10);
        }

        //set the keyboard button' visibility depending on if the keyboard is showing or not
        for(int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setVisible(!keyBoard.getShowing());
        }
    }

    //override the GameScreen's draw method
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        //drawing bitmaps for backrgound and gameover
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SpaceBackground"), null, background, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("gameOver"), null, gameOver, null);

        //draw the buttons if they're visible
        for(int i = 0; i < buttons.size(); i++) {
            if(buttons.get(i).isVisible()){
                buttons.get(i).draw(graphics2D);
            }
        }

        //draw the players score, if multiplayer is activated, draw 2 player's scores
        if (!mGame.getTwoPlayerToggle()){
            graphics2D.drawRect(showKeyboard, keyBoardButtonPaint);
            graphics2D.drawText("Final Score:    " + mPlayer.getScoreValue(), Scale.getX(25), Scale.getY(30), textPaint);
            graphics2D.drawText("Enter Name:   " + mPlayer.getName(), Scale.getX(25), Scale.getY(35), textPaint);
            keyBoard.draw(graphics2D);
        }
        else
        {
            //don't draw the keyboard if multiplayer is activated
            graphics2D.drawText("Player 1 Score:   " + mGame.getPlayer1Score(), Scale.getX(25), Scale.getY(30), textPaint);
            graphics2D.drawText("Player 2 Score:   " + mGame.getPlayer2Score(), Scale.getX(25), Scale.getY(35), textPaint);
        }

    }

    //method to load the buttons to the screen
    private void loadButtons()
    {
        //instantiating the push buttons
        playAgain = new PushButton(Scale.getX(50), Scale.getY(47), Scale.getX(50), Scale.getY(14), "playg", this);
        mainMenu = new PushButton(Scale.getX(50), Scale.getY(62), Scale.getX(50), Scale.getY(14),"mainmenu", this );
        shop = new PushButton(Scale.getX(50), Scale.getY(77), Scale.getX(50), Scale.getY(14),"shopg",this);
        facebookButton = new PushButton(Scale.getX(37.5), Scale.getY(87.5), Scale.getX(25), Scale.getY(5),"FacebookShare",this);
        twitterButton = new PushButton(Scale.getX(62.5), Scale.getY(87.5), Scale.getX(25), Scale.getY(5),"TwitterShare",this);

        //add buttons to arraylist
        buttons.add(mainMenu);
        //not adding the shop button or play again button if there is 2 player mode selected
        if (!mGame.getTwoPlayerToggle())
        {
            buttons.add(playAgain);
            buttons.add(shop);
        }
        buttons.add(facebookButton);
        buttons.add(twitterButton);

    }

}
