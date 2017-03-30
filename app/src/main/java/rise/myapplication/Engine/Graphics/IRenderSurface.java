package rise.myapplication.Engine.Graphics;

import android.view.View;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.World.GameScreen;

/**
 * Created by malac_000 on 18/11/2015.
 */
public interface IRenderSurface {

    // /////////////////////////////////////////////////////////////////////////
    // Abstract methods to be implemented
    // /////////////////////////////////////////////////////////////////////////

    void render(ElapsedTime elapsedTime, GameScreen gameScreen);
    View getAsView();
}
