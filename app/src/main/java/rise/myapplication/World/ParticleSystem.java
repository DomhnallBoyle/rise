package rise.myapplication.World;

import android.graphics.Bitmap;

import java.util.Random;

import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Util.Scale;

/**
 * Created by 40124186 on 14/03/2016.
 */
public class ParticleSystem {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public static final int ALIVE = 0;
    public static final int DEAD = 1;
    private final static int LIFETIME = 150;
    private final static int MAX_SPEED = 30;
    private Particle[] mParticles;
    private int mState;
    private Bitmap bitmap;
    private Random random;
    private int x, y;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public ParticleSystem(Bitmap bitmap, int numberOfParticles, int x, int y) {
        mState = ALIVE;
        mParticles = new Particle[numberOfParticles];

        for (int i = 0; i < mParticles.length; i++) {
            mParticles[i] = new Particle(bitmap, Scale.getX(x), Scale.getY(y), LIFETIME, MAX_SPEED);
        }
        random = new Random();
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    //method to update the particle system
    public void update() {
        //if the particle system state is not DEAD (ALIVE)
        if (mState != DEAD) {
            //set isDead to true
            boolean isDead = true;

            //loop through the array of particles
            //if the particle is alive, update the particle and set isDead to false
            for (int i = 0; i < mParticles.length; i++) {
                if (mParticles[i].isAlive()) {
                    mParticles[i].update();
                    isDead = false;
                }
            }

            //this if statement will only excecute when each particle is DEAD
            if (isDead)
            {
                mState = DEAD;
                setupAgain(); //set up the particles again
            }
        }
    }

    //method to draw each particle on screen
    public void draw(IGraphics2D graphics2D) {

        //loops through the array of particles
        for(int i = 0; i < mParticles.length; i++) {
            //if the particles is alive, draw it
            if (mParticles[i].isAlive()) {
                mParticles[i].draw(graphics2D);
            }
        }
    }

    //method to setup the particle system again
    private void setupAgain()
    {
        //set the particle system state to ALIVE
        mState = ALIVE;

        //for each particle in the array, set up its x and y position again etc...
        for (int i = 0; i < mParticles.length; i++) {
            mParticles[i].setup(Scale.getX(x), Scale.getY(y), LIFETIME, MAX_SPEED);
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public boolean isDead() {
        return mState == DEAD;
    }
}
