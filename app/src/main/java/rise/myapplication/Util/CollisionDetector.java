package rise.myapplication.Util;

import rise.myapplication.World.GameObjects.GameObject;

/**
 * Created by 40126424 on 10/12/2015.
 */
public class CollisionDetector {

    /**
     * Type of collision
     */
    public enum CollisionType {
        None, Top, Bottom, Left, Right
    }

    /**
     * Determine if the two specified bounding boxes are in collision
     *
     * @param one
     *            First bounding box
     * @param two
     *            Second bounding box
     * @return boolean true if the boxes overlap, false otherwise
     */
    private static boolean isCollision(BoundingBox one, BoundingBox two) {
        return (one.getX() - one.getHalfWidth() < two.getX() + two.getHalfWidth()
                && one.getX() + one.getHalfWidth() > two.getX() - two.getHalfWidth()
                && one.getY() - one.getHalfHeight() < two.getY() + two.getHalfHeight() && one.getY()
                + one.getHalfHeight() > two.getY() - two.getHeight());
    }

    /**
     * Determine the type of collision between the two game objects. If the two
     * objects overlap, then they are separated. The first game object will be
     * repositioned. The second game object will not be moved following a
     * collision.
     *
     * CollisionType.None is returned if there are no collisions.
     *
     * @param gameObjectOne
     *            First game object box
     * @param gameObjectTwo
     *            Second game object box
     * @return Collision type
     */

    public static CollisionType determineAndResolvePlatformCollision(GameObject gameObjectOne, GameObject gameObjectTwo){
        CollisionType collisionType = CollisionType.None;

        BoundingBox one = gameObjectOne.getBound();
        BoundingBox two = gameObjectTwo.getBound();

        if (isCollision(one, two)) {
            // Determine the side of *least intersection*
            float collisionDepth = Float.MAX_VALUE;

            // Check the top side
            float tOverlap = (two.getY() + two.getHalfHeight())
                    - (one.getY() - one.getHalfHeight());
            if (tOverlap > 0.0f && tOverlap < collisionDepth) {
                collisionType = CollisionType.Top;
                collisionDepth = tOverlap;
            }

            // Check the bottom side
            float bOverlap = (one.getY() + one.getHalfHeight())
                    - (two.getY() - two.getHalfHeight());
            if (bOverlap > 0.0f && bOverlap < collisionDepth) {
                collisionType = CollisionType.Bottom;
                collisionDepth = bOverlap;
            }

            // Check the right overlap
            float rOverlap = (one.getX() + one.getHalfWidth()) - (two.getX() - two.getHalfWidth());
            if (rOverlap > 0.0f && rOverlap < collisionDepth) {
                collisionType = CollisionType.Right;
                collisionDepth = rOverlap;
            }

            // Check the left overlap
            float lOverlap = (two.getX() + two.getHalfWidth()) - (one.getX() - one.getHalfWidth());
            if (lOverlap > 0.0f && lOverlap < collisionDepth) {
                collisionType = CollisionType.Left;
                collisionDepth = lOverlap;
            }

            // Separate if needed
            switch (collisionType) {
                case Top:
                    //gameObjectOne.position.y += collisionDepth;
                    break;
                case Bottom:
                    //gameObjectOne.position.y -= collisionDepth;
                    break;
                case Right:
                    //gameObjectOne.position.x -= collisionDepth;
                    break;
                case Left:
                    //gameObjectOne.position.x += collisionDepth;
                    break;
                case None:
                    break;
            }
        }

        return collisionType;
    }

    public static CollisionType determineAndResolveCollision(GameObject gameObjectOne, GameObject gameObjectTwo) {CollisionType collisionType = CollisionType.None;

        BoundingBox one = gameObjectOne.getBound();
        BoundingBox two = gameObjectTwo.getBound();

        if (isCollision(one, two)) {
            // Determine the side of *least intersection*
            float collisionDepth = Float.MAX_VALUE;

            // Check the top side
            float tOverlap = (two.getY() + two.getHalfHeight())
                    - (one.getY() - one.getHalfHeight());
            if (tOverlap > 0.0f && tOverlap < collisionDepth) {
                collisionType = CollisionType.Top;
                collisionDepth = tOverlap;
            }

            // Check the bottom side
            float bOverlap = (one.getY() + one.getHalfHeight())
                    - (two.getY() - two.getHalfHeight());
            if (bOverlap > 0.0f && bOverlap < collisionDepth) {
                collisionType = CollisionType.Bottom;
                collisionDepth = bOverlap;
            }

            // Check the right overlap
            float rOverlap = (one.getX() + one.getHalfWidth()) - (two.getX() - two.getHalfWidth());
            if (rOverlap > 0.0f && rOverlap < collisionDepth) {
                collisionType = CollisionType.Right;
                collisionDepth = rOverlap;
            }

            // Check the left overlap
            float lOverlap = (two.getX() + two.getHalfWidth()) - (one.getX() - one.getHalfWidth());
            if (lOverlap > 0.0f && lOverlap < collisionDepth) {
                collisionType = CollisionType.Left;
                collisionDepth = lOverlap;
            }

            // Separate if needed
            switch (collisionType) {
                case Top:
                    gameObjectOne.getPosition().y += collisionDepth;
                    break;
                case Bottom:
                    gameObjectOne.getPosition().y -= collisionDepth;
                    break;
                case Right:
                    gameObjectOne.getPosition().x -= collisionDepth;
                    break;
                case Left:
                    gameObjectOne.getPosition().x += collisionDepth;
                    break;
                case None:
                    break;
            }
        }

        return collisionType;
    }

    //this method will be used for the Unit Tests
    public static CollisionType determineAndResolveCollision(BoundingBox one, BoundingBox two){
        CollisionType collisionType = CollisionType.None;

        if (isCollision(one, two)) {
            // Determine the side of *least intersection*
            float collisionDepth = Float.MAX_VALUE;

            // Check the top side
            float tOverlap = (two.getY() + two.getHalfHeight()) - (one.getY() - one.getHalfHeight());
            if (tOverlap > 0.0f && tOverlap < collisionDepth) {
                collisionType = CollisionType.Top;
                collisionDepth = tOverlap;
            }

            // Check the bottom side
            float bOverlap = (one.getY() + one.getHalfHeight()) - (two.getY() - two.getHalfHeight());
            if (bOverlap > 0.0f && bOverlap < collisionDepth) {
                collisionType = CollisionType.Bottom;
                collisionDepth = bOverlap;
            }

            // Check the right overlap
            float rOverlap = (one.getX() + one.getHalfWidth()) - (two.getX() - two.getHalfWidth());
            if (rOverlap > 0.0f && rOverlap < collisionDepth) {
                collisionType = CollisionType.Right;
                collisionDepth = rOverlap;
            }

            // Check the left overlap
            float lOverlap = (two.getX() + two.getHalfWidth()) - (one.getX() - one.getHalfWidth());
            if (lOverlap > 0.0f && lOverlap < collisionDepth) {
                collisionType = CollisionType.Left;
                collisionDepth = lOverlap;
            }

            // Separate if needed
            switch (collisionType) {
                case Top:
                    //gameObjectOne.position.y += collisionDepth;
                    break;
                case Bottom:
                    //gameObjectOne.position.y -= collisionDepth;
                    break;
                case Right:
                    //gameObjectOne.position.x -= collisionDepth;
                    break;
                case Left:
                    //gameObjectOne.position.x += collisionDepth;
                    break;
                case None:
                    break;
            }
        }

        return collisionType;
    }


}
