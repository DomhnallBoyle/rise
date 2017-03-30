package rise.myapplication.UI;


import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.TouchHandler;
import rise.myapplication.World.GameObjects.GameObject;
import rise.myapplication.World.GameScreen;
import rise.myapplication.Util.BoundingBox;
import rise.myapplication.World.LayerViewport;
import rise.myapplication.World.ScreenViewport;

/**
 * Created by 40126424 on 10/12/2015.
 */
public class SimpleControl extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    boolean paused=false;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Create a new control.
     *
     * @param x
     *            Centre y location of the control
     * @param y
     *            Centre x location of the control
     * @param width
     *            Width of the control
     * @param height
     *            Height of the control
     * @param bitmapName
     *            Bitmap used to represent this control
     * @param gameScreen
     *            Gamescreen to which this control belongs
     */
    public SimpleControl(float x, float y, float width, float height, String bitmapName, GameScreen gameScreen) {
        super(x, y, width, height, gameScreen.getGame().getAssetManager().getBitmap(bitmapName), gameScreen);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    /*
 * (non-Javadoc)
 *
 * @see
 * uk.ac.qub.eeecs.gage.world.GameObject#draw(uk.ac.qub.eeecs.gage.engine
 * .ElapsedTime, uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D,
 * uk.ac.qub.eeecs.gage.world.LayerViewport,
 * uk.ac.qub.eeecs.gage.world.ScreenViewport)
 */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Assumed to be in screen space so just draw the whole thing
        getDrawScreenRect().set((int) (getPosition().x - getBound().getHalfWidth()),
                (int) (getPosition().y - getBound().getHalfWidth()),
                (int) (getPosition().x + getBound().getHalfWidth()),
                (int) (getPosition().y + getBound().getHalfHeight()));

        graphics2D.drawBitmap(getBitmap(), null, getDrawScreenRect(), null);
    }

    /**
     * Determine if this control has been activated (touched).
     *
     * @return boolean true if the control has been touched, false otherwise
     */
    public boolean isActivated() {

        // Consider any touch events occurring in this update
        Input input = getmGameScreen().getGame().getInput();

        // Check if any of the touch events were on this control
        BoundingBox bound = getBound();

        for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
            if (input.existsTouch(idx)) {
                if (bound.contains(input.getTouchX(idx), input.getTouchY(idx))) {
                    paused = !paused;
                    return true;
                }
            }
        }
        return false;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public boolean isPaused(){
        return paused;
    }

    //check if control contains x and y coordinate
    public boolean contains(int x, int y)
    {
        if (this.getDrawScreenRect().contains(x, y))
        {
            return true;
        }
        return false;
    }

}














