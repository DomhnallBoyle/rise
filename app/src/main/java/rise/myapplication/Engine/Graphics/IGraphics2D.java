package rise.myapplication.Engine.Graphics;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by newcomputer on 17/11/15.
 */
public interface IGraphics2D {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    int getSurfaceWidth();
    int getSurfaceHeight();

    // /////////////////////////////////////////////////////////////////////////
    // Abstract Methods to be implemented
    // /////////////////////////////////////////////////////////////////////////

    void clipRect(Rect clipRegion);
    void clear(int colour);
    void drawRect(Rect rect, Paint paint);
    void drawText(String text, float x, float y, Paint paint);
    void drawBitmap(Bitmap bitmap, float x, float y, Paint paint);
    void drawBitmap(Bitmap bitmap, Rect srcRect, Rect desRect, Paint paint);
    void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint);

}

