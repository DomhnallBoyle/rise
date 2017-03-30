package rise.myapplication.Engine.Graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by malac_000 on 18/11/2015.
 */
public class CanvasGraphics2D implements IGraphics2D {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private Canvas mCanvas;
    private int mWidth;
    private int mHeight;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public CanvasGraphics2D(){}

    public void setCanvas(Canvas canvas){
        mCanvas = canvas;
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();
    }


    //overriding methods from IGraphics2D
    @Override
    public void drawBitmap(Bitmap bitmap, Rect srcRect, Rect desRect, Paint paint) {
        mCanvas.drawBitmap(bitmap, srcRect, desRect, paint);
    }

    @Override
    public void drawBitmap(Bitmap bitmap, Matrix matrix, Paint paint) {
        mCanvas.drawBitmap(bitmap, matrix, paint);
    }

    @Override
    public void drawBitmap(Bitmap bitmap, float x, float y, Paint paint) {
        mCanvas.drawBitmap(bitmap, x, y, paint);
    }

    @Override
    public void drawText(String text, float x, float y, Paint paint) {
        mCanvas.drawText(text, x, y, paint);
    }

    @Override
    public void clear(int color) {
        mCanvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
    }

    @Override
    public void drawRect(Rect rect, Paint paint)
    {
        mCanvas.drawRect(rect, paint);
    }

    @Override
    public void clipRect(Rect clipRegion) {
        mCanvas.clipRect(clipRegion);
    }

    @Override
    public int getSurfaceWidth() {
        return mWidth;
    }

    @Override
    public int getSurfaceHeight() {
        return mHeight;
    }

}
