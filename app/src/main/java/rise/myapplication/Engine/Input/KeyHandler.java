package rise.myapplication.Engine.Input;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.view.View;
import android.view.View.OnKeyListener;

import rise.myapplication.Util.Pool;
import rise.myapplication.R;

public class KeyHandler implements OnKeyListener {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final boolean[] mPressedKeys = new boolean[128];
    private final Pool<KeyEvent> mKeyEventPool;
    private final List<KeyEvent> mUnconsumedKeyEvents = new ArrayList<>();
    private final List<KeyEvent> mKeyEvents = new ArrayList<>();
    private final int KEY_POOL_SIZE = 100;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public KeyHandler(View view) {

        //create event pool of 100 keyevents
        mKeyEventPool = new Pool<>(new Pool.ObjectFactory<KeyEvent>() {
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        }, KEY_POOL_SIZE);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }


    @Override
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {

        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
        {
            String warningTag = v.getContext().getApplicationContext().getResources().getString(R.string.WARNING_TAG);
            String warningMessage = "ACTION_MULTIPLE event type encountered within" + this.getClass().toString();
            Log.w(warningTag, warningMessage);
            return false;
        }

        synchronized (this) {

            KeyEvent keyEvent = mKeyEventPool.get();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();

            //if ACTION_DOWN in the view, the keycode pressed will equal true
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN;
                if (keyCode > 0 && keyCode < 127)
                    mPressedKeys[keyCode] = true;
            }
            //if ACTION_UP in the view, the keycode pressed will equal false
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP;
                if (keyCode > 0 && keyCode < 127)
                    mPressedKeys[keyCode] = false;
            }

            mUnconsumedKeyEvents.add(keyEvent);
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode) {
        if (keyCode < 0 || keyCode > 127)
            return false;
        return mPressedKeys[keyCode];
    }

    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
            return mKeyEvents;
        }
    }

    public void resetAccumulator() {
        synchronized (this) {
            int len = mKeyEvents.size();
            for (int i = 0; i < len; i++) {
                mKeyEventPool.add(mKeyEvents.get(i));
            }
            mKeyEvents.clear();
            mKeyEvents.addAll(mUnconsumedKeyEvents);
            mUnconsumedKeyEvents.clear();
        }
    }
}
