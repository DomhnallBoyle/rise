package rise.myapplication.UI;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import rise.myapplication.Util.BoundingBox;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.TouchHandler;
import rise.myapplication.Engine.Managers.CustomAssetManager;
import rise.myapplication.World.GameObjects.GameObject;
import rise.myapplication.World.GameScreen;

/**
 * Created by 40126424 on 10/12/2015.
 */
public class PushButton extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //Possible button states
    protected enum ButtonState { DEFAULT, PUSHED }

     //Current button state
    protected ButtonState mButtonState = ButtonState.DEFAULT;

     //Name of the graphical asset used to represent the default button state
    protected final Bitmap mDefaultBitmap;

    //sets boolean visible or invisible
    protected boolean visible;

    //Name of paint object
    protected final Paint paint;

    //Name of GameScreen button links to
    protected GameScreen nextScreen;

    //true when button is first pushed down, false while held down
    private boolean mPushTriggered;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public PushButton(float x, float y, float width, float height, String defaultBitmap, GameScreen gameScreen) {
        super(x,y, width, height, gameScreen.getGame().getAssetManager().getBitmap(defaultBitmap), gameScreen);

        //set initial variable values
        CustomAssetManager assetStore = gameScreen.getGame().getAssetManager();
        mDefaultBitmap = assetStore.getBitmap(defaultBitmap);
        visible = true;
        paint = new Paint();
        nextScreen = gameScreen;
      }

     //Update the button
    public void update(ElapsedTime elapsedTime) {
        // Consider any touch events occurring in this update
        Input input = getmGameScreen().getGame().getInput();

        // Check if any of the touch events were on this control
        BoundingBox bound = getBound();
        for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
            if (input.existsTouch(idx)) {
                if (bound.contains(input.getTouchX(idx), input.getTouchY(idx))) {
                    if(mButtonState.equals(ButtonState.DEFAULT)) {
                        //play sound if available
                        nextScreen.getGame().getAssetManager().getSound("ButtonClick").play();
                        mPushTriggered = true;
                        mButtonState = ButtonState.PUSHED;
                    }
                    return;
                }
            }
        }

        // If we have not returned by this point, then there is no touch event on the button
        if(mButtonState.equals(ButtonState.PUSHED)) {
            setBitmap(mDefaultBitmap);
            mButtonState = ButtonState.DEFAULT;
            mPushTriggered = false;
        }
    }

    // This will only be returned as true once per push -
    public boolean pushTriggered() {
        if(mPushTriggered) {
            mPushTriggered = false;
            return true;
        }
        return false;
    }

    //set button image
    public void setBitmap(String bitmap, GameScreen gameScreen){
        CustomAssetManager assetStore = gameScreen.getGame().getAssetManager();
        setBitmap(assetStore.getBitmap(bitmap));
    }

    //draw PushButton
    public void draw(IGraphics2D graphics2D) {
            getDrawScreenRect().set((int) (getPosition().x - getBound().getHalfWidth()),
                    (int) (getPosition().y - getBound().getHalfHeight()),
                    (int) (getPosition().x + getBound().getHalfWidth()),
                    (int) (getPosition().y + getBound().getHalfHeight()));
            if (mButtonState.equals(ButtonState.PUSHED)) {
                //paint.setAlpha(130);
              paint.setColorFilter(new PorterDuffColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY));
            } else {
                paint.reset();
                paint.setAlpha(255);
            }
            graphics2D.drawBitmap(getBitmap(), null, getDrawScreenRect(), paint);
    }

    //draw PushButton
    public void draw(IGraphics2D graphics2D, Paint newPaint) {
        getDrawScreenRect().set((int) (getPosition().x - getBound().getHalfWidth()),
                (int) (getPosition().y - getBound().getHalfHeight()),
                (int) (getPosition().x + getBound().getHalfWidth()),
                (int) (getPosition().y + getBound().getHalfHeight()));
        if (mButtonState.equals(ButtonState.PUSHED)) {
            paint.setAlpha(130);
        } else {
            paint.setAlpha(255);
        }
        graphics2D.drawBitmap(getBitmap(), null, getDrawScreenRect(), newPaint);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public void setVisible(boolean visible){this.visible = visible;}
    public boolean isVisible(){return visible;}
}
