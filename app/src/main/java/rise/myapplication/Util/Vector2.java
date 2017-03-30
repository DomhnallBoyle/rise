package rise.myapplication.Util;

public class Vector2{

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public final static Vector2 Zero = new Vector2(0,0);
    public float x, y;

    // /////////////////////////////////////////////////////////////////////////
    // Constructors
    // /////////////////////////////////////////////////////////////////////////

    public Vector2(){}

    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Vector2(Vector2 other){
        this.x = other.x;
        this.y = other.y;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Vector Methods
    // /////////////////////////////////////////////////////////////////////////

    //returns true if vector's x position and y position are equal to 0
    public boolean isZero(){return x == 0.0f && y == 0.0f;}

    //returns the hypotenuse using pythagoras' theorem
    public float length(){ return (float) Math.sqrt(x*x + y*y);}

    //returns the hypotenuse squared
    public float lengthSquared() { return x*x + y*y;}

    //sets vector's x and y to floats passed in
    public void set(float x, float y){
        this.x = x;
        this.y = y;
    }

    //sets the current vector to one passed in
    public void set(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
    }

    //adds the x and y of the vector to floats passed in
    public void add(float x, float y) {
        this.x += x;
        this.y += y;
    }

    //same as above but a vector is passed in instead
    public void add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
    }

    //substracts the vector passed in from the current vector's x and y
    public void subtract(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    //multiplies the x and y by a scalar passed in
    public void multiply(float scalar) {
        x *= scalar;
        y *= scalar;
    }

    //divides x and y by a scalar passed in
    public void divide(float scalar) {
        x /= scalar;
        y /= scalar;
    }

    public void normalise() {
        float length = length();
        if (length != 0) {
            x /= length;
            y /= length;
        }
    }
}