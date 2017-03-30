package rise.myapplication.World.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.World.GameScreen;
import rise.myapplication.Util.BoundingBox;
import rise.myapplication.Util.GraphicsHelper;
import rise.myapplication.Util.Vector2;
import rise.myapplication.World.LayerViewport;
import rise.myapplication.World.ScreenViewport;

/**
 * Created by DomhnallBoyle on 19/11/2015.
 */
public class GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final GameScreen mGameScreen;
    private Bitmap mBitmap;
    private final Vector2 position = new Vector2();
    private final BoundingBox mBound = new BoundingBox();
    private final Rect drawSourceRect = new Rect();
    private final Rect drawScreenRect = new Rect();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public GameObject(GameScreen gameScreen)
    {
        mGameScreen = gameScreen;
    }

    public GameObject(float x, float y, Bitmap bitmap, GameScreen gameScreen)
    {
        mGameScreen = gameScreen;
        position.x = x;
        position.y = y;
        mBitmap = bitmap;
        mBound.setX(x);
        mBound.setY(y);
        mBound.setHalfWidth(bitmap.getWidth() / 2.0f);
        mBound.setHalfHeight(bitmap.getHeight() / 2.0f);
    }

    public GameObject(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen)
    {
        mGameScreen = gameScreen;
        position.x = x;
        position.y = y;
        mBitmap = bitmap;
        mBound.setX(x);
        mBound.setY(y);
        mBound.setHalfWidth(width / 2.0f);
        mBound.setHalfHeight(height / 2.0f);
    }

    public GameObject(float x, float y, float width, float height, GameScreen gameScreen)
    {
        mGameScreen = gameScreen;
        position.x = x;
        position.y = y;
        mBound.setX(x);
        mBound.setY(y);
        mBound.setHalfWidth(width / 2.0f);
        mBound.setHalfHeight(height / 2.0f);
    }

    //draw method without paint
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport)
    {
        if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport, screenViewport, drawSourceRect, drawScreenRect))
        {
            graphics2D.drawBitmap(mBitmap, drawSourceRect, drawScreenRect, null);
        }
    }

    //draw method with paint
    public void draw(IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport, Paint paint)
    {
        if (GraphicsHelper.getClippedSourceAndScreenRect(this, layerViewport, screenViewport, drawSourceRect, drawScreenRect))
        {
            graphics2D.drawBitmap(mBitmap, drawSourceRect, drawScreenRect, paint);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public Bitmap getBitmap()
    {
        return mBitmap;
    }
    public void setBitmap(Bitmap bitmap){mBitmap = bitmap;}
    public Rect getDrawScreenRect(){return drawScreenRect;}
    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public BoundingBox getmBound() {
        return mBound;
    }

    public Rect getDrawSourceRect() {
        return drawSourceRect;
    }

    public Vector2 getPosition() {

        return position;
    }


    public void setPosition(float x, float y)
    {
        mBound.setX(position.x);
        position.x = x;
        mBound.setY(position.y);
        position.y = y;
    }
    public BoundingBox getBound()
    {
        mBound.setX(position.x);
        mBound.setY(position.y);
        return mBound;
    }

    public GameScreen getmGameScreen() {
        return mGameScreen;
    }


}

