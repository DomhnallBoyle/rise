package rise.myapplication.Screens;

import android.graphics.Rect;

import rise.myapplication.UI.DisplayToast;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Audio.Music;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Game.Game;
import rise.myapplication.Util.Scale;
import rise.myapplication.Util.Transform2;
import rise.myapplication.World.GameScreen;

/**
 * Created by ConorMurray on 10/12/2015.
 */
public class GameSplashScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //background + logo rect
    private final Rect doodleBackground;
    private final Rect rise;

    //if first frame
    private boolean firstDraw = true;

    //time animation starts
    private double startTime = 0.00;

    //how long screen has been running for
    private float timeElapsed;

    //music object
    private final Music music;

    //length of splash screen animation
    private final float SPLASH_SCREEN_TIME = 5.0f;

    //starting Rect position
    private final Rect START_RECT = new Rect(Scale.getX(35), Scale.getY(42), Scale.getX(65), Scale.getY(45));

    //final Rect position
    private final Rect FINISH_RECT = new Rect(Scale.getX(10), Scale.getY(2), Scale.getX(90), Scale.getY(15));

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public GameSplashScreen(Game game)
    {
        super("GameSplashScreen", game);
        //left top right bottom
        doodleBackground = new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight());
        rise = new Rect(START_RECT);
        music = game.getAssetManager().getMusic("theme");

        //displaying toasts on outcome of global database connection
        if(mGame.getDatabaseHelper().getConnected())
        {
            new DisplayToast("Connected Globally").execute();
        }
        else
        {
            new DisplayToast("Enable WIFI connection").execute();
        }
    }

    //update screen
    @Override
    public void update(ElapsedTime elapsedTime) {
        //change to menu after 5 seconds
        if(elapsedTime.totalTime - startTime > SPLASH_SCREEN_TIME){
            mGame.getScreenManager().removeScreen(this.getName());
            MenuScreen menuScreen = new MenuScreen(mGame);
            mGame.getScreenManager().addScreen(menuScreen);
            music.play();
            music.setLooping(true);
        }
    }

    //draw screen
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        //set startTime on firstDraw
        if(firstDraw){
            startTime = elapsedTime.totalTime;
            firstDraw = false;
        }
        //check current time
        timeElapsed = (float)(elapsedTime.totalTime - startTime);

        //set logo position
        rise.set(Transform2.step(START_RECT, FINISH_RECT, timeElapsed, SPLASH_SCREEN_TIME));

        //draw elements
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SpaceBackground"), null, doodleBackground, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Rise"), null, rise, null);
    }
}