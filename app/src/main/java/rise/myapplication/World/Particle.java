package rise.myapplication.World;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;

import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Game.Game;

/**
 * Created by 40124186 on 14/03/2016.
 */
public class Particle {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////
    
    public static final int ALIVE = 0;
    public static final int DEAD = 1;
    private int state; 
    private Bitmap bitmap;
    private float x, y; 
    private double xVelocity, yVelocity;
    private float age;
    private float lifetime;
    private Paint paint;
    private int alpha;
    private static Bitmap base;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Particle(Bitmap bitmap, int x, int y, int lifetime, int maxSpeed) {
        this.x = x;
        this.y = y;
        state = ALIVE;
        if (base==null) {
            base = bitmap;
        }
        this.bitmap = bitmap;
        this.lifetime = lifetime;
        age = 0;
        alpha = 0xff;
        xVelocity = (randomDouble(0, maxSpeed * 2) - maxSpeed);
        yVelocity = (randomDouble(0, maxSpeed * 2) - maxSpeed);
        paint = new Paint();

        //slowing down the speed of particles
        if (xVelocity * xVelocity + yVelocity * yVelocity > maxSpeed * maxSpeed) {
            xVelocity *= 0.7;
            yVelocity *= 0.7;
        }
    }

    //update method for each particle
    public void update() {
        //if the state is not DEAD (ALIVE)
        if (state != DEAD)
        {
            //increment xVelocity and yVelocity to x and y
            x += xVelocity;
            y += yVelocity;

            if (alpha <= 0)
            { //if the alpha blend integer is less than 0 (can't see it anymore), change state to DEAD
                state = DEAD;
            }
            else
            {
                age++; //increment age
                float factor = (age/lifetime) * 2;
                alpha = (int) (0xff - (0xff * factor)); //change alpha blend colour
                paint.setAlpha(alpha); //set alpha to paint
            }
            if (age >= lifetime) //if the particle's age is greater than its lifetime, set state to DEAD
            {
                state = DEAD;
            }
        }

    }

    //method to draw each particle
    public void draw(IGraphics2D graphics2D) {
        graphics2D.drawBitmap(bitmap, x, y, paint);
    }

    //setup the particles again
    public void setup(int x, int y, int lifetime, int maxSpeed)
    {
        this.x = x;
        this.y = y;
        state = ALIVE;
        this.lifetime = lifetime;
        age = 0;
        alpha = 0xff;
        xVelocity = (randomDouble(0, maxSpeed * 2) - maxSpeed);
        yVelocity = (randomDouble(0, maxSpeed * 2) - maxSpeed);
        paint = new Paint();
        if (xVelocity * xVelocity + yVelocity * yVelocity > maxSpeed * maxSpeed) {
            xVelocity *= 0.7;
            yVelocity *= 0.7;
        }
    }

    //returns a random double between a min and max value
    private double randomDouble(double min, double max) {
        return min + (max - min) * Math.random();
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    //returns whether or not the particle is alive
    public boolean isAlive() {
        return state == ALIVE;
    }
}


