package rise.myapplication.Screens;

import android.graphics.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rise.myapplication.UI.Animation;
import rise.myapplication.UI.DisplayToast;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.TouchEvent;
import rise.myapplication.Game.Game;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.Util.Scale;
import rise.myapplication.World.GameScreen;
import rise.myapplication.UI.PushButton;

/**
 * Created by newcomputer on 11/02/16.
 */

public class Leaderboard extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final Rect leaderboardLogo, background;
    private final ArrayList<PushButton> buttons;
    private final PushButton globalLeaderboardButton, localLeaderboardButton, mainMenu, retryConnectToDatabase;
    private boolean isGlobalLeaderboard, isLocalLeaderboard;
    private final Player player;
    private final Paint paint;
    private final Animation connectToDatabase;
    private double startTime = 0;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Leaderboard(Game game, Player mPlayer)
    {
        super("Leaderboard", game);

        //instantiating Rects
        leaderboardLogo = new Rect(0, 0, game.getScreenWidth(), Scale.getY(15));
        background = new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight());

        //setting the paint
        Typeface plain = Typeface.createFromAsset(MainActivity.getContext().getAssets(), String.format(Locale.US, "Fonts/%s", "Tiki Tropic Bold.ttf"));
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        paint.setTypeface(bold);
        paint.setFakeBoldText(true);
        paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);

        //creating an arraylist of push buttons and adding them to it
        buttons = new ArrayList<>();
        mainMenu = new PushButton(Scale.getX(50), Scale.getY(95), Scale.getX(40), Scale.getY(10), "mainmenu", this);
        globalLeaderboardButton = new PushButton(Scale.getX(35), Scale.getY(19.5), Scale.getX(30), Scale.getY(11),"Global", this );
        localLeaderboardButton = new PushButton(Scale.getX(65), Scale.getY(19.5), Scale.getX(30), Scale.getY(11), "Local", this);
        retryConnectToDatabase = new PushButton(Scale.getX(50), Scale.getY(50), Scale.getX(20), Scale.getY(20), "RetryButton", this);
        buttons.add(mainMenu);
        buttons.add(globalLeaderboardButton);
        buttons.add(localLeaderboardButton);

        //creating the player
        this.player = mPlayer;

        //creating the loading database animation
        connectToDatabase = new Animation(mGame.getAssetManager().getBitmap("RetrySpritesheet"), 8);
    }


    @Override
    public void update(ElapsedTime elapsedTime) {

        //updating the push buttons
        for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).update(elapsedTime);
        }
        retryConnectToDatabase.update(elapsedTime);

        //updating the animation
        connectToDatabase.update(elapsedTime);

        //if the animaton is playing
        if (connectToDatabase.getIsPlaying())
        {
            //if the time taken from when you pressed the retry connection button > 5 seconds
            if (elapsedTime.totalTime - startTime > 5)
            {
                //try connecting to database, stop the animation and set the retry button's position
                mGame.getDatabaseHelper().connect();
                connectToDatabase.stop();
                retryConnectToDatabase.setPosition(Scale.getX(50), Scale.getY(50));

                //if connected, get the players from the database
                if (mGame.getDatabaseHelper().getConnected())
                {
                    mGame.getDatabaseHelper().getPlayers();
                }
                else
                {
                    new DisplayToast("Cannot connect to Database. Enable WIFI connection").execute();
                }
            }
        }

        // Process any touch events occurring since the update
        //checking if any buttons have been pushed and taking necessary action
        Input input = mGame.getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);

            if(touchEvent.type == TouchEvent.TOUCH_UP) {

                if (mainMenu.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    MenuScreen menuScreen = new MenuScreen(mGame, player);
                    mGame.getScreenManager().addScreen(menuScreen);
                }

                if (globalLeaderboardButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    isGlobalLeaderboard = true;
                    isLocalLeaderboard = false;
                    //if connected, get players from database
                    if (mGame.getDatabaseHelper().connect())
                    {
                        mGame.getDatabaseHelper().getPlayers();
                    }
                    else
                    {
                        retryConnectToDatabase.setPosition(Scale.getX(50), Scale.getY(50));
                    }
                }

                if (localLeaderboardButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    isLocalLeaderboard = true;
                    isGlobalLeaderboard = false;
                    retryConnectToDatabase.setPosition(Scale.getX(-20), Scale.getY(-20));
                }

                //if you select the rety connection button and it is the global leaderboard
                if (retryConnectToDatabase.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y))
                {
                    if (isGlobalLeaderboard)
                    {
                        //play the animation and set the start time
                        connectToDatabase.play(2, true);
                        retryConnectToDatabase.setPosition(Scale.getX(-20), Scale.getY(-20));
                        startTime = elapsedTime.totalTime;
                    }
                }
            }
        }
    }

    //method to draw onto the Leaderboard screen
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        //drawing bitmaps
        graphics2D.clear(Color.BLACK);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SpaceBackground"), null, background, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("leadg"), null, leaderboardLogo, null);

        //drawing the push buttons
        for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).draw(graphics2D);
        }

        //drawing animation if playing
        if (connectToDatabase.getIsPlaying())
        {
            connectToDatabase.draw(graphics2D, Scale.getX(40), Scale.getY(40), Scale.getX(60), Scale.getY(60), paint);
            if (connectToDatabase.getImageCount() == connectToDatabase.getFrameCount())
            {
                connectToDatabase.setImageCount(0);
            }
        }

        //drawing the global scores only if the global button has been pushed and the database is connected to the online database
        if (isGlobalLeaderboard) {
            if (mGame.getDatabaseHelper().getConnected())
            {
                graphics2D.drawText("Rank:", Scale.getX(6), Scale.getY(30), paint);
                graphics2D.drawText("Name:", Scale.getX(40), Scale.getY(30), paint);
                graphics2D.drawText("Score:", Scale.getX(75), Scale.getY(30), paint);
                mGame.getDatabaseHelper().draw(graphics2D);
            }
            else
            {
                retryConnectToDatabase.draw(graphics2D);
            }
        }

        //drawing the local scores only if the local button has been pushed
        if (isLocalLeaderboard)
        {
            graphics2D.drawText("Rank:", Scale.getX(6), Scale.getY(30), paint);
            graphics2D.drawText("Name:", Scale.getX(40), Scale.getY(30), paint);
            graphics2D.drawText("Score:", Scale.getX(75), Scale.getY(30), paint);
            mGame.getSharedPreferences().draw(graphics2D);
        }

    }

}

