package rise.myapplication.World;

import android.graphics.Rect;

import rise.myapplication.Util.Scale;

/**
 * Created by User on 19/11/2015.
 */
public class ScreenViewport {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public int left, top, right, bottom, width, height;
    private final Rect rect = new Rect();

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public ScreenViewport() {
        this.left = 0;
        this.top = 0;
        this.right = Scale.getX(100);
        this.bottom = Scale.getY(100);

        width = right - left;
        height = bottom - top;
    }

    public ScreenViewport(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;

        width = right - left;
        height = bottom - top;
    }

    //sets the screenViewports properties
    public void set(int left, int top, int right, int bottom) {

        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;

        width = right - left;
        height = bottom - top;
    }

    //returns center X coordinate
    public final int centerX() { return (left + right) >> 1; }

    //returns center Y coordinate
    public final int centerY() { return (top + bottom) >> 1;  }

    //method to check if screenViewport contains coordinates
    public boolean contains(int left, int top, int right, int bottom) {
        return this.left < this.right && this.top < this.bottom
                && this.left <= left && this.top <= top && this.right >= right
                && this.bottom >= bottom;
    }

    //method to return the screenViewport as a Rect
    public Rect toRect() {
        rect.left = left;
        rect.right = right;
        rect.top = top;
        rect.bottom = bottom;
        return rect;
    }
}
