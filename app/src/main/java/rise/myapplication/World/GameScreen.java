package rise.myapplication.World;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Game.Game;

/**
 * Created by Ciaran Duncan on 17/11/15.
 */
public abstract class GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public final Game mGame;
    private final String mName;
    private static int screenWidth, screenHeight;
    protected boolean pause = false;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public GameScreen(String name, Game game) {
        mName = name;
        mGame = game;
    }

    //abstract methods that have to be overriden
    public abstract void update(ElapsedTime elapsedTime);
    public abstract void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D);

    public void pause() {
        pause = true;
    }

    public void resume() {
        pause = false;
    }

    public void dispose() {
    }


    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public String getName() {
        return mName;
    }
    public Game getGame() {
        return mGame;
    }
    public static int getScreenWidth(){return screenWidth;}
    public static int getScreenHeight(){return screenHeight;}
    public static void setScreenWidth(int screenWidth){GameScreen.screenWidth = screenWidth;}
    public static void setScreenHeight(int screenHeight){GameScreen.screenHeight = screenHeight;}

}
