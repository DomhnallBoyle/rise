package rise.myapplication.World.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.World.GameScreen;
import rise.myapplication.Util.Vector2;

/**
 * Created by DomhnallBoyle on 28/11/2015.
 */
public class Sprite extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public static final float DEFAULT_MAX_ACCELERATION = Float.MAX_VALUE;
    public static final float DEFAULT_MAX_VELOCITY = Float.MAX_VALUE;

    //not sure if we need these next two lines
    public static final float DEFAULT_MAX_ANGULAR_ACCELERATION = Float.MAX_VALUE;
    public static final float DEFAULT_MAX_ANGULAR_VELOCITY = Float.MAX_VALUE;

    public final Vector2 velocity = new Vector2();
    public final Vector2 acceleration = new Vector2();

    public final float maxAcceleration = DEFAULT_MAX_ACCELERATION;
    public final float maxVelocity = DEFAULT_MAX_VELOCITY;

    public float orientation, angularVelocity, angularAcceleration;

    public final float maxAngularAcceleration = DEFAULT_MAX_ANGULAR_ACCELERATION;
    public final float maxAngularVelocity = DEFAULT_MAX_ANGULAR_VELOCITY;

    protected Matrix drawMatrix = new Matrix();

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public Sprite(GameScreen gameScreen)
    {
        super(gameScreen);
    }

    public Sprite(float x, float y, Bitmap bitmap, GameScreen gameScreen)
    {
        super(x, y, bitmap, gameScreen);
    }

    public Sprite(float x, float y, float width, float height, Bitmap bitmap, GameScreen gameScreen)
    {
        super(x, y, width, height, bitmap, gameScreen);
    }

    public Sprite(float x, float y, float width, float height, GameScreen gameScreen)
    {
        super(x, y, width, height, gameScreen);
    }

    public void update(ElapsedTime elapsedTime)
    {
        float dt = (float) elapsedTime.stepTime;

        // Ensure the maximum acceleration isn't exceeded
        if (acceleration.lengthSquared() > maxAcceleration * maxAcceleration) {
            acceleration.normalise();
            acceleration.multiply(maxAcceleration);
        }

        // Update the velocity using the acceleration and ensure the
        // maximum velocity has not been exceeded
        velocity.add(acceleration.x * dt, acceleration.y * dt);

        if (velocity.lengthSquared() > maxVelocity * maxVelocity) {
            velocity.normalise();
            velocity.multiply(maxVelocity);
        }

        // Update the position using the velocity
        getPosition().add(velocity.x * dt, velocity.y * dt);

        // Ensure the maximum angular acceleration isn't exceeded
        if (angularAcceleration < -maxAngularAcceleration
                || angularAcceleration > maxAngularAcceleration) {
            angularAcceleration = Math.signum(angularAcceleration)
                    * maxAngularAcceleration;
        }

        // Update the angular velocity using the angular acceleration and
        // ensure the maximum angular velocity has not been exceeded
        angularVelocity += angularAcceleration * dt;

        if (angularVelocity < -maxAngularVelocity
                || angularVelocity > maxAngularVelocity) {
            angularVelocity = Math.signum(angularVelocity) * maxAngularVelocity;
        }

        // Update the orientation using the angular velocity
        orientation += angularVelocity * dt;

    }

    /*@Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport)
    {
        //are we having a screen viewport??
        if (GraphicsHelper.getSourceAndScreenRect(this, layerViewport, screenViewport, drawSourceRect, drawScreenRect))
        {
            float scaleX = (float)drawScreenRect.width() / (float)drawSourceRect.width();
            float scaleY = (float) drawScreenRect.height() / (float) drawSourceRect.height();

            drawMatrix.reset();
            drawMatrix.postScale(scaleX, scaleY);
            drawMatrix.postRotate(orientation, scaleX * mBitmap.getWidth() / 2.0f, scaleY * mBitmap.getHeight() / 2.0f);
            drawMatrix.postTranslate(drawScreenRect.left, drawScreenRect.top);

            graphics2D.drawBitmap(mBitmap, drawMatrix, null);
        }
    }*/
}
