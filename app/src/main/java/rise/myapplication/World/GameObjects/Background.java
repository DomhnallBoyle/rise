package rise.myapplication.World.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Util.Timer;
import rise.myapplication.Util.Scale;
import rise.myapplication.World.GameScreen;
import rise.myapplication.World.LayerViewport;
import rise.myapplication.World.ScreenViewport;

/**
 * Created by 40124186 on 10/02/2016.
 */
public class Background extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final int LOWER_LIMIT = 15;
    private final int UPPER_LIMIT = 30;
    private final Bitmap background, timedBackground;
    private final ArrayList<SideImage> sideImages;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Background(float startX, float startY, GameScreen gameScreen, Bitmap background, Bitmap timedBackground, Bitmap sideImage)
    {
        super(startX, startY, Scale.getX(100), Scale.getY(100), gameScreen);
        this.background = background;
        this.timedBackground = timedBackground;
        this.sideImages = new ArrayList<>();
        sideImages.add(new SideImage(Scale.getX(-3), Scale.getY(70), SideImage.Side.LEFT, gameScreen, sideImage));
        sideImages.add(new SideImage(Scale.getX(98),Scale.getY(30), SideImage.Side.RIGHT, gameScreen, sideImage));
    }

    //updating the background - updating the side images
    public void update(GameObject gameObject)
    {
        for (int i=0; i<sideImages.size(); i++)
        {
            sideImages.get(i).update(gameObject);
        }
    }

    //drawing the game background
    public void draw(Rect backgroundRect, Timer timer, ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport, Paint paint)
    {
        graphics2D.drawBitmap(background, null, backgroundRect, null);
        if ((elapsedTime.totalTime - timer.getStartTime() > LOWER_LIMIT) && (elapsedTime.totalTime - timer.getStartTime() < UPPER_LIMIT))
        {
            graphics2D.drawBitmap(timedBackground, null, backgroundRect, paint);
        }
        for (int i=0; i<sideImages.size(); i++)
        {
            sideImages.get(i).draw(elapsedTime, graphics2D, layerViewport, screenViewport);
        }
    }

}
