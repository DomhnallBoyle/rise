package rise.myapplication.World;

import rise.myapplication.Util.BoundingBox;

/**
 * Created by Ciaran Duncan on 19/11/15.
 */

public class LayerViewport {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public float x, y, halfWidth, halfHeight;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public LayerViewport() {
        this.x = 270;
        this.y = 960;
        this.halfWidth = 270;
        this.halfHeight = 960;
    }

    public LayerViewport(float x, float y, float halfWidth, float halfHeight) {
        this.x = x;
        this.y = y;
        this.halfWidth = halfWidth;
        this.halfHeight = halfHeight;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public void set(float x, float y, float halfWidth, float halfHeight) {
        this.x = x;
        this.y = y;
        this.halfHeight = halfHeight;
        this.halfWidth = halfWidth;
    }
    public float getWidth() {
        return halfWidth * 2.0f;
    }
    public float getHeight() {
        return halfHeight * 2.0f;
    }
    public float getLeft() {
        return x - halfWidth;
    }
    public float getRight() {
        return x + halfWidth;
    }
    public float getTop() {
        return y + halfHeight;
    }
    public float getBottom() {
        return y - halfHeight;
    }

    //method to determine if coordinates are contained within the layerviewport
    public boolean contains(float x, float y) {
        return (x - halfWidth < x && x + halfWidth > x
                && y - halfHeight < y && y + halfHeight > y);
    }
    
    //method to determine if bounding box intersects with layerviewport
    public boolean intersects(BoundingBox bound) {
        return (x - halfWidth < bound.getX() + bound.getHalfWidth() &&
                x + halfWidth > bound.getX() - bound.getHalfWidth() &&
                y - halfHeight < bound.getY() + bound.getHalfHeight() &&
                y + halfHeight > bound.getY() - bound.getHalfWidth());
    }
}

