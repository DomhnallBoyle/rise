package rise.myapplication.UI;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.World.GameObjects.GameObject;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Util.Scale;


/**
 * Created by DomhnallBoyle on 14/12/2015.
 */
public class Animation {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final Bitmap animationFrames;
    private final Rect sourceRect = new Rect();
    private final Rect screenRect = new Rect();
    private final int frameWidth, frameHeight, frameCount;
    private int currentFrame, imageCount = 0;
    private double frameTimer, framePeriod;
    private boolean isLooping = false, isPlaying = false;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Animation(Bitmap animationFrames, int frameCount)
    {
        this.animationFrames = animationFrames;
        this.frameCount = frameCount;
        frameHeight = animationFrames.getHeight();
        frameWidth = animationFrames.getWidth() / frameCount;
    }

    //method to play animation
    public void play(double animationPeriod, boolean loop)
    {
        framePeriod = animationPeriod / (double)frameCount;
        currentFrame = -1;
        isLooping = loop;
        isPlaying = true;
    }

    //method to stop the animation
    public void stop()
    {
        isPlaying = false;
    }

    //updating the animation after an elapsed time to display next frame
    public void update(ElapsedTime elapsedTime)
    {
        if (!isPlaying)
            return;

        // If this is the first time update has been called since the
        // play method was called then reset the current frame and timer
        if (currentFrame == -1) {
            currentFrame = 0;
            frameTimer = 0.0;
        }

        // Update the amount of time accumulated against this frame
        frameTimer += elapsedTime.stepTime;

        // If the frame display duration has been exceeded then try to
        // go on to the next frame, looping or stopping if the end of
        // the animation has been reached.
        if (frameTimer >= framePeriod) {
            currentFrame++;
            if (currentFrame >= frameCount) {
                if (isLooping) {
                    currentFrame = 0;
                } else {
                    currentFrame = frameCount - 1;
                    isPlaying = false;
                }
            }
            // 'Reset' the frame timer
            frameTimer -= framePeriod;
        }
    }

    public void draw(IGraphics2D graphics2D, GameObject gameObject, Paint paint)
    {
        setSourceRect();
        getScreenRect().set(gameObject.getDrawScreenRect().left - Scale.getX(5), gameObject.getDrawScreenRect().top - Scale.getY(5), gameObject.getDrawScreenRect().right + Scale.getX(5), gameObject.getDrawScreenRect().bottom + Scale.getX(5));
        graphics2D.drawBitmap(animationFrames, sourceRect, screenRect, paint);
        imageCount++;
    }

    public void draw(IGraphics2D graphics2D, float topX, float topY, float bottomX, float bottomY, Paint paint)
    {
        setSourceRect();
        getScreenRect().set((int)topX, (int)topY, (int)bottomX, (int)bottomY);
        graphics2D.drawBitmap(animationFrames, sourceRect, screenRect, paint);
        imageCount++;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public int getFrameCount()
    {
        return frameCount;
    }
    public int getImageCount()
    {
        return imageCount;
    }
    public void setImageCount(int imageCount)
    {
        this.imageCount = imageCount;
    }
    public void setSourceRect() {
        if(currentFrame >= 0)
            sourceRect.set(currentFrame * frameWidth, 0, currentFrame * frameWidth + frameWidth, frameHeight);
    }
    public Rect getSourceRect(){return sourceRect;}
    public Rect getScreenRect(){return screenRect;}
    public Bitmap getBitmap()
    {
        return animationFrames;
    }
    public boolean getIsPlaying(){return isPlaying;}

}
