package rise.myapplication.World.GameObjects;

import android.graphics.Bitmap;

import java.util.Random;

import rise.myapplication.Util.Scale;
import rise.myapplication.World.GameScreen;

/**
 * Created by DomhnallBoyle on 14/12/2015.
 */
public class Platform extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public static final double PLATFORM_HEIGHT = 3;
    public static final double PLATFORM_WIDTH = 15;
    public enum PlatformType{NORMAL, EXPLODING, MOVING}
    public enum Movement{LEFT, RIGHT}
    private final PlatformType platformType;
    private Movement movement;
    private boolean isGone;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Platform(float startX, float startY, PlatformType platformType, GameScreen gameScreen)
    {
        super(startX, startY, Scale.getX(PLATFORM_WIDTH), Scale.getY(PLATFORM_HEIGHT), gameScreen);
        setBitmap(findBitmap(platformType));
        this.platformType = platformType;
        this.isGone = false;
    }

    //updating the platforms based on their type
    public void update(GameObject gameObject)
    {
        switch(this.getPlatformType())
        {
            //if moving, depending on type of movement, add or subtract 2 from their position
            case MOVING:
                if (movement == movement.LEFT)
                {
                    getPosition().x-=2;
                }
                else
                {
                    getPosition().x+=2;
                }
                //if the platform is greater than screen width, move it left
                if (getBound().getX()+getBound().getHalfWidth() >= getmGameScreen().getGame().getScreenWidth())
                {
                    movement = movement.LEFT;
                }
                //if the platform is less than screen width, move it right
                if (getBound().getX()-getBound().getHalfWidth() <= 0)
                {
                    movement = movement.RIGHT;
                }
        }
        //never allow the player to go up more than 42% of the screen
        if (gameObject.getBound().getTop() >= Scale.getY(42))
        {
            getPosition().y-=10+((Player) gameObject).getOverflowY();
        }
    }

    //explode the platform if of type EXPLODE
    public void explode()
    {
        getmGameScreen().getGame().getAssetManager().getSound("Explosion").play();
        this.isGone = true;
    }

    //depending on platform type, set the bitmap
    private Bitmap findBitmap(PlatformType platformType)
    {
        Bitmap bitmap = null;
        switch(platformType)
        {
            case NORMAL:
                bitmap = getmGameScreen().getGame().getAssetManager().getBitmap("NormalPlatform");
                break;
            case MOVING:
                bitmap = getmGameScreen().getGame().getAssetManager().getBitmap("MovingPlatform");
                movement = getMovement();
                break;
            case EXPLODING:
                bitmap = getmGameScreen().getGame().getAssetManager().getBitmap("ExplodingPlatform");
                break;
        }
        return bitmap;
    }

    private Movement getMovement()
    {
        Random rand = new Random();
        Movement movement = null;
        int i = rand.nextInt(100);
        //if i is even, movement is RIGHT, else LEFT
        if (i%2==0)
        {
            movement = movement.RIGHT;
        }
        else
        {
            movement = movement.LEFT;
        }
        return movement;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public PlatformType getPlatformType(){return platformType;}
    public Movement getMovementEnum(){return movement;}
    public boolean isGone(){return isGone;}
    public void setX(float x){
        getPosition().x = x;
    }
    public void setY(float y){
        getPosition().y = y;
    }
    public void setIsGone(boolean isGone){this.isGone = isGone;}
}
