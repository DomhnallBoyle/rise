package rise.myapplication.Util;

/**
 * Created by User on 19/11/2015.
 */
public class BoundingBox {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private float x, y, halfWidth, halfHeight;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public BoundingBox() {
        x = 0;
        y = 0;
        halfHeight = 1.0f;
        halfWidth = 1.0f;
    }

    public BoundingBox(float x, float y, float halfWidth, float halfHeight){
        this.x = x;
        this.y = y;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    //method to check of the bounding box contains coordinates of another object
    public boolean contains(float x, float y){
        return ( this.x - this.halfWidth < x && this.x + this.halfWidth > x
                && this.y - this.halfHeight < y && this.y + this.halfHeight > y);
    }

    //check to see if 2 bounding boxes interset
    public boolean intersects(BoundingBox other){
        return (this.x - this.halfWidth < other.x + other.halfWidth &&
                this.x + this.halfWidth > other.x - other.halfWidth &&
                this.y - this.halfHeight < other.y + other.halfHeight &&
                this.y + this.halfHeight > other.y - other.halfHeight);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public float getWidth() { return halfWidth * 2.0f; }
    public float getHeight() { return halfHeight * 2.0f; }
    public float getLeft() { return x - halfWidth; }
    public float getRight() { return x + halfWidth; }
    public float getTop() { return y + halfHeight; }
    public float getBottom() { return y - halfHeight; }
    public float getX(){return x;}
    public float getY(){return y;}
    public float getHalfWidth(){return halfWidth;}
    public float getHalfHeight(){return halfHeight;}
    public void setX(float x){this.x = x;}
    public void setY(float y){this.y = y;}
    public void setHalfWidth(float halfWidth){this.halfWidth = halfWidth;}
    public void setHalfHeight(float halfHeight){this.halfHeight = halfHeight;}

}

