package rise.myapplication.Util;

import android.graphics.Bitmap;
import android.graphics.Rect;

import rise.myapplication.Game.Game;
import rise.myapplication.World.GameObjects.GameObject;
import rise.myapplication.World.LayerViewport;
import rise.myapplication.World.ScreenViewport;

/**
 * Created by DomhnallBoyle on 19/11/2015.
 */
public class GraphicsHelper {

    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////

    public static final boolean checkIfPastWidth(GameObject gameObject, LayerViewport layerViewport, ScreenViewport screenViewport, Rect sourceRect, Rect screenRect)
    {
        BoundingBox spriteBound = gameObject.getBound();

        if (spriteBound.getX() - spriteBound.getHalfWidth() < layerViewport.x + layerViewport.halfWidth && spriteBound.getX() + spriteBound.getHalfWidth() > layerViewport.x - layerViewport.halfWidth)
        {
            Bitmap spriteBitmap = gameObject.getBitmap();
            sourceRect.set(0, 0, spriteBitmap.getWidth(), spriteBitmap.getHeight());

            float screenXScale = (float)screenViewport.width / (2 * layerViewport.halfWidth);
            float screenYScale = (float) screenViewport.height / (2 * layerViewport.halfHeight);

            // Determine the screen rectangle
            float screenX = screenViewport.left + screenXScale * ((spriteBound.getX() - spriteBound.getHalfWidth()) - (layerViewport.x - layerViewport.halfWidth));
            float screenY = screenViewport.top + screenYScale * ((layerViewport.y + layerViewport.halfHeight) - (spriteBound.getY() + spriteBound.getHalfHeight()));

            screenRect.set((int) screenX, (int) screenY, (int) (screenX + (spriteBound.getHalfWidth() * 2) * screenXScale), (int) (screenY + (spriteBound.getHalfHeight() * 2) * screenYScale));

            return true;
        }
        return false;
    }

    public static final boolean checkIfPastHeight(GameObject gameObject, LayerViewport layerViewport, ScreenViewport screenViewport, Rect sourceRect, Rect screenRect)
    {
        BoundingBox spriteBound = gameObject.getBound();

        if (spriteBound.getY() + spriteBound.getHalfHeight() > layerViewport.y - layerViewport.halfHeight)
        {
            Bitmap spriteBitmap = gameObject.getBitmap();
            sourceRect.set(0, 0, spriteBitmap.getWidth(), spriteBitmap.getHeight());

            float screenXScale = (float)screenViewport.width / (2 * layerViewport.halfWidth);
            float screenYScale = (float) screenViewport.height / (2 * layerViewport.halfHeight);

            // Determine the screen rectangle
            float screenX = screenViewport.left + screenXScale * ((spriteBound.getX() - spriteBound.getHalfWidth()) - (layerViewport.x - layerViewport.halfWidth));
            float screenY = screenViewport.top + screenYScale * ((layerViewport.y + layerViewport.halfHeight) - (spriteBound.getY() + spriteBound.getHalfHeight()));

            screenRect.set((int) screenX, (int) screenY, (int) (screenX + (spriteBound.getHalfWidth() * 2) * screenXScale), (int) (screenY + (spriteBound.getHalfHeight() * 2) * screenYScale));

            return true;
        }
        return false;
    }

    public static final boolean getSourceAndScreenRect(GameObject gameObject, LayerViewport layerViewport, ScreenViewport screenViewport, Rect sourceRect, Rect screenRect)
    {
        BoundingBox spriteBound = gameObject.getBound();

        //determine if the sprite falls within the layer viewport
        if (spriteBound.getX() - spriteBound.getHalfWidth() < layerViewport.x + layerViewport.halfWidth &&
                spriteBound.getX() + spriteBound.getHalfWidth() > layerViewport.x - layerViewport.halfWidth &&
                spriteBound.getY() - spriteBound.getHalfHeight() < layerViewport.y + layerViewport.halfHeight &&
                spriteBound.getY() + spriteBound.getHalfHeight() > layerViewport.y - layerViewport.halfHeight)
        {
            Bitmap spriteBitmap = gameObject.getBitmap();
            sourceRect.set(0, 0, spriteBitmap.getWidth(), spriteBitmap.getHeight());

            float screenXScale = (float)screenViewport.width / (2 * layerViewport.halfWidth);
            float screenYScale = (float) screenViewport.height / (2 * layerViewport.halfHeight);

            // Determine the screen rectangle
            float screenX = screenViewport.left + screenXScale * ((spriteBound.getX() - spriteBound.getHalfWidth()) - (layerViewport.x - layerViewport.halfWidth));
            float screenY = screenViewport.top + screenYScale * ((layerViewport.y + layerViewport.halfHeight) - (spriteBound.getY() + spriteBound.getHalfHeight()));

            screenRect.set((int) screenX, (int) screenY, (int) (screenX + (spriteBound.getHalfWidth() * 2) * screenXScale), (int) (screenY + (spriteBound.getHalfHeight() * 2) * screenYScale));

            return true;
        }
        return false;
    }

    public static final boolean getClippedSourceAndScreenRect(GameObject gameObject, LayerViewport layerViewport, ScreenViewport screenViewport, Rect sourceRect, Rect screenRect)
    {
        // Get the bounding box for the specified sprite
        BoundingBox spriteBound = gameObject.getBound();

        // Determine if the sprite falls within the layer viewport
        if (spriteBound.getX() - spriteBound.getHalfWidth() < layerViewport.x + layerViewport.halfWidth &&
                spriteBound.getX() + spriteBound.getHalfWidth() > layerViewport.x - layerViewport.halfWidth &&
                spriteBound.getY() - spriteBound.getHalfHeight() < layerViewport.y + layerViewport.halfHeight &&
                spriteBound.getY() + spriteBound.getHalfHeight() > layerViewport.y - layerViewport.halfHeight) {

            // Work out what region of the sprite is visible within the layer viewport,

            float sourceX = Math.max(0.0f, (layerViewport.x - layerViewport.halfWidth) - (spriteBound.getX() - spriteBound.getHalfWidth()));
            float sourceY = Math.max(0.0f, (spriteBound.getY() + spriteBound.getHalfHeight()) - (layerViewport.y + layerViewport.halfHeight));

            float sourceWidth = ((spriteBound.getHalfWidth() * 2 - sourceX) - Math.max(0.0f, (spriteBound.getX() + spriteBound.getHalfWidth()) - (layerViewport.x + layerViewport.halfWidth)));
            float sourceHeight = ((spriteBound.getHalfHeight() * 2 - sourceY) - Math.max(0.0f, (layerViewport.y - layerViewport.halfHeight) - (spriteBound.getY() - spriteBound.getHalfHeight())));

            // Determining the scale factor for mapping the bitmap onto this
            // Rect and set the sourceRect value.

            Bitmap spriteBitmap = gameObject.getBitmap();

            float sourceScaleWidth = (float) spriteBitmap.getWidth() / (2 * spriteBound.getHalfWidth());
            float sourceScaleHeight = (float) spriteBitmap.getHeight() / (2 * spriteBound.getHalfHeight());

            sourceRect.set((int) (sourceX * sourceScaleWidth), (int) (sourceY * sourceScaleHeight), (int) ((sourceX + sourceWidth) * sourceScaleWidth), (int) ((sourceY + sourceHeight) * sourceScaleHeight));

            // Determine =which region of the screen viewport (relative to the
            // canvas) we will be drawing to.

            // Determine the x- and y-aspect rations between the layer and screen viewports
            float screenXScale = (float) screenViewport.width / (2 * layerViewport.halfWidth);
            float screenYScale = (float) screenViewport.height / (2 * layerViewport.halfHeight);

            float screenX = screenViewport.left + screenXScale * Math.max(0.0f, ((spriteBound.getX() - spriteBound.getHalfWidth()) - (layerViewport.x - layerViewport.halfWidth)));
            float screenY = screenViewport.top + screenYScale * Math.max(0.0f, ((layerViewport.y + layerViewport.halfHeight) - (spriteBound.getY() + spriteBound.getHalfHeight())));

            // Set the region to the canvas to which we will draw
            screenRect.set((int) screenX, (int) screenY, (int) (screenX + sourceWidth * screenXScale), (int) (screenY + sourceHeight * screenYScale));

            return true;
        }

        // Not visible
        return false;
    }

    public static final boolean checkIfVisible(BoundingBox spriteBound, LayerViewport layerViewport)
    {
        // Determine if the sprite falls within the layer viewport
        if (spriteBound.getX() - spriteBound.getHalfWidth() < layerViewport.x + layerViewport.halfWidth &&
                spriteBound.getX() + spriteBound.getHalfWidth() > layerViewport.x - layerViewport.halfWidth &&
                spriteBound.getY() - spriteBound.getHalfHeight() < layerViewport.y + layerViewport.halfHeight &&
                spriteBound.getY() + spriteBound.getHalfHeight() > layerViewport.y - layerViewport.halfHeight) {
            return true;
        }
        // Not visible
        return false;
    }

    public static void create3To2AspectRatioScreenViewport(Game game, ScreenViewport screenViewport) {

        // Create the screen viewport, size it to provide a 3:2 aspect
        float aspectRatio = (float) game.getScreenWidth() / (float) game.getScreenHeight();

        if (aspectRatio > 1.5f) { // 16:10/16:9
            int viewWidth = (int) (game.getScreenHeight() * 1.5f);
            int viewOffset = (game.getScreenWidth() - viewWidth) / 2;
            screenViewport.set(0, Scale.getY(0), game.getScreenWidth(), Scale.getY(100));
        } else { // 4:3
            int viewHeight = (int) (game.getScreenWidth() / 1.5f);
            int viewOffset = (game.getScreenHeight() - viewHeight) / 2;
            screenViewport.set(0, Scale.getY(0), game.getScreenWidth(), Scale.getY(100));

        }
    }

}
