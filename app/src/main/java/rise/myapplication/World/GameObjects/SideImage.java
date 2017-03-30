package rise.myapplication.World.GameObjects;

import android.graphics.Bitmap;

import rise.myapplication.Util.Scale;
import rise.myapplication.World.GameScreen;


/**
 * Created by 40126424 on 17/12/2015.
 */
public class SideImage extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public enum Side{LEFT,RIGHT}
    private final Side side;
    private final boolean isGone;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public SideImage(float startX, float startY, Side side, GameScreen gameScreen, Bitmap bitmap){
        super(startX, startY, Scale.getX(45), Scale.getY(35), gameScreen);
        setBitmap(bitmap);
        this.side = side;
        this.isGone=false;
    }

    //side images update method
    public void update(GameObject gameObject) {
        //if the gameObject's top bound >= 42% of the screen
        if (gameObject.getBound().getTop() >= Scale.getY(42)) {
            getPosition().y -= 2;
        }

        //if the side image's position is < -10% of screen
        //reset its position to top of screen depending on LEFT or RIGHT side image
        if (getPosition().y < Scale.getY(-10)) {
            switch (getSide()) {
                case LEFT:
                    setPosition(Scale.getX(-3), Scale.getY(140));
                    break;
                case RIGHT:
                    setPosition(Scale.getX(98), Scale.getY(140));
                    break;
            }
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public Side getSide(){
        return side;
    }
    public boolean isGone(){
        return isGone;
    }

}