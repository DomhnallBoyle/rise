package rise.myapplication.Util;

import android.graphics.Rect;

import rise.myapplication.Util.BoundingBox;

/**
 * Created by 40133490 on 17/02/2016.
 */
public class Transform2 {

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    //BoundingBox version
    public static BoundingBox step(BoundingBox startBox, BoundingBox finishBox, double startTime, double totalTime) {
        if (startTime >= 0) {
            //calculate new values
            float x = (float) ((finishBox.getX() - startBox.getX()) * (startTime / totalTime));
            float y = (float) ((finishBox.getY() - startBox.getY()) * (startTime / totalTime));
            float halfWidth = (float) ((finishBox.getHalfWidth() - startBox.getHalfWidth()) * (startTime / totalTime));
            float halfHeight = (float) ((finishBox.getHalfHeight() - startBox.getHalfHeight()) * (startTime / totalTime));
            BoundingBox returnBox = new BoundingBox(startBox.getX() + x, startBox.getY() + y, startBox.getHalfWidth() + halfWidth, startBox.getHalfHeight() + halfHeight);
            return returnBox;
        }
        return startBox;
    }

    //Rect version
    public static Rect step(Rect startRect, Rect finishRect, double startTime, double totalTime){
        if(startTime>=0) {
            //calculate new values
            int leftX = (int) (((finishRect.left - startRect.left) * (startTime / totalTime)));
            int topY = (int) ((finishRect.top - startRect.top) * (startTime / totalTime));
            int rightX = (int) ((finishRect.right - startRect.right) * (startTime / totalTime));
            int bottomY = (int) ((finishRect.bottom - startRect.bottom) * (startTime / totalTime));

            Rect returnRect = new Rect(startRect.left + leftX, startRect.top + topY, startRect.right + rightX,startRect.bottom + bottomY);
            return returnRect;
        }
        return startRect;
    }
}