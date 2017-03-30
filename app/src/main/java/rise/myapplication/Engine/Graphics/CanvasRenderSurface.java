package rise.myapplication.Engine.Graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Game.Game;
import rise.myapplication.World.GameScreen;

/**
 * Created by malac_000 on 18/11/2015.
 */
public class CanvasRenderSurface extends View implements IRenderSurface {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final CanvasGraphics2D mCanvasGraphics2D;
    private final Game mGame;
    private GameScreen mScreenToRender;
    private ElapsedTime mElapsedTime;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public CanvasRenderSurface(Game game, Context context){
        super(context);
        mGame = game;
        mCanvasGraphics2D = new CanvasGraphics2D();
    }

    //overriding methods from View and IRenderSurface
    @Override
    public View getAsView() {return this;}

    @Override
    public void render (ElapsedTime elapsedTime, GameScreen screenToRender){
        mElapsedTime = elapsedTime;
        mScreenToRender = screenToRender;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        mCanvasGraphics2D.setCanvas(canvas);
        mScreenToRender.draw(mElapsedTime, mCanvasGraphics2D);
        mGame.notifyDrawCompleted();
    }
}
