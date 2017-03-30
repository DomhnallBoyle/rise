package rise.myapplication.Game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rise.myapplication.Game.Game;
import rise.myapplication.Screens.GameSplashScreen;
import rise.myapplication.Screens.LoadingScreen;
import rise.myapplication.Screens.MenuScreen;

/**
 * Created by Ciaran Duncan on 19/11/15.
 */
public class RiseGame extends Game {

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public RiseGame() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTargetFramesPerSecond(20);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        LoadingScreen loadingScreen = new LoadingScreen(this);
        getScreenManager().addScreen(loadingScreen);

        //GameSplashScreen splashScreen = new GameSplashScreen(this);
        //getScreenManager().addScreen(splashScreen);

        return view;
    }

    @Override
    public boolean onBackPressed() {
        // If we are already at the menu screen then exit
        if(getScreenManager().getCurrentScreen().getName().equals("MenuScreen"))
            return false;

        // Go back to the menu screen
        getScreenManager().removeScreen(getScreenManager().getCurrentScreen().getName());
        MenuScreen menuScreen = new MenuScreen(this);
        getScreenManager().addScreen(menuScreen);
        return true;
    }
}