package rise.myapplication.Engine.Managers;

import java.util.HashMap;
import java.util.Map;

import rise.myapplication.World.GameScreen;

/**
 * Created by newcomputer on 17/11/15.
 */
public class ScreenManager {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final Map<String, GameScreen> mGameScreens;
    private GameScreen mCurrentScreen;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public ScreenManager(){
        mGameScreens = new HashMap<>();
        mCurrentScreen=null;
    }

    public boolean addScreen(GameScreen screen){
        if (mGameScreens.containsKey(screen.getName()))
            return false;
        mGameScreens.put(screen.getName(), screen);
        if (mGameScreens.size() == 1)
            mCurrentScreen = screen;

        return true;
    }

    public boolean setAsCurrentScreen(String name) {
        GameScreen currentScreen = mGameScreens.get(name);
        if (currentScreen != null) {
            mCurrentScreen = currentScreen;
            return true;
        } else
            return false;
    }

    public GameScreen getCurrentScreen() {
        return mCurrentScreen;
    }
    public GameScreen getScreen(String name) {
        return mGameScreens.get(name);
    }

    public boolean removeScreen(String name) {
        GameScreen gameScreen = mGameScreens.remove(name);
        return (gameScreen != null);
    }
    public void dispose() {
        for (GameScreen gameScreen : mGameScreens.values())
            gameScreen.dispose();
    }
}

