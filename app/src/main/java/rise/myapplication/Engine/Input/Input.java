package rise.myapplication.Engine.Input;


import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * Created by 40124623 on 12/11/2015.
 */
public class Input {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final AccelerometerHandler mAccelHandler;
    private final KeyHandler mKeyHandler;
    private final TouchHandler mTouchHandler;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Input(Context context, View view){
        mAccelHandler = new AccelerometerHandler(context);
        mKeyHandler = new KeyHandler(view);
        mTouchHandler = new TouchHandler (view);
    }

    public void resetAccumulators (){
        mTouchHandler.resetAccumulator();
        mKeyHandler.resetAccumulator();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public boolean existsTouch (int pointerId) {return mTouchHandler.existsTouch(pointerId);}
    public float getTouchX(int pointerId){ return mTouchHandler.getTouchX(pointerId);}
    public float getTouchY(int pointerId) {return mTouchHandler.getTouchY(pointerId);}
    public List<TouchEvent> getTouchEvents(){return mTouchHandler.getTouchEvents();}
    public float getAccelX(){return mAccelHandler.getAccelX();}
    public float getAccelY() { return mAccelHandler.getAccelY();}
    public float getAccelZ() { return mAccelHandler.getAccelZ();}
    public boolean isKeyPressed(int keyCode){return mKeyHandler.isKeyPressed(keyCode);}
    public List<KeyEvent> getKeyEvents(){return mKeyHandler.getKeyEvents();}

}
