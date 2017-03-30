package rise.myapplication.World.GameObjects;

import android.graphics.Bitmap;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Util.Scale;
import rise.myapplication.World.GameScreen;
import rise.myapplication.Util.BoundingBox;
import rise.myapplication.Util.Transform2;

/**
 * Created by 40124186 on 17/12/2015.
 */
public class Item extends GameObject {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public enum itemType{COIN, MAGNET, ROCKET, SPRINGS, SHIELD, MULTIPLIER}
    private Platform platform;
    private boolean isGone;
    private final itemType type;
    private BoundingBox magnetStartBox;
    private boolean isMagnetised = false;
    private double magnetStartTime;
    public static final float ITEM_WIDTH = Scale.getX(4);
    public static final float ITEM_HEIGHT = Scale.getY(4);

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Item(float startX, float startY, Bitmap bitmap, Platform platform, itemType type, GameScreen gameScreen)
    {
        super(startX, startY, ITEM_WIDTH, ITEM_HEIGHT, bitmap, gameScreen);
        this.platform = platform;
        this.type = type;
        this.isGone = false;
    }

    //item update method
    public void update(ElapsedTime elapsedTime, GameObject gameObject) {
        //if player has magnet change coin movement
        if(!isMagnetised && ((Player) gameObject).getHasMagnet()){
            magnetStartBox = this.getBound();
            magnetStartTime = elapsedTime.totalTime;
            isMagnetised = true;
        }
        if(isMagnetised && type.equals(itemType.COIN))
        {
            BoundingBox magnetBox = Transform2.step(magnetStartBox, gameObject.getBound(), elapsedTime.totalTime - magnetStartTime, ((Player) gameObject).getMagnetAllowedTime());
            this.setPosition(magnetBox.getLeft(),magnetBox.getTop());
        }
        else{
            if (platform.getPlatformType() == Platform.PlatformType.MOVING && platform.getMovementEnum() == Platform.Movement.LEFT) {
                getPosition().x -= 2;
            }
            if (platform.getPlatformType() == Platform.PlatformType.MOVING && platform.getMovementEnum() == Platform.Movement.RIGHT) {
                getPosition().x += 2;
            }
            if (gameObject.getBound().getTop() >= Scale.getY(42)) {
                getPosition().y -= 10+((Player) gameObject).getOverflowY();
            }
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public void setIsGone(boolean isGone)
    {
        this.isGone = isGone;
    }
    public boolean IsGone() {return isGone;}
    public void setIsMagnetised(boolean newVal){isMagnetised = newVal;}
    public itemType getType(){
        return type;
    }
    public void setPlatform(Platform newPlatform){
        platform = newPlatform;
        getPosition().x =  platform.getBound().getX();
        getPosition().y = platform.getBound().getTop() + Item.ITEM_HEIGHT;
    }
}
