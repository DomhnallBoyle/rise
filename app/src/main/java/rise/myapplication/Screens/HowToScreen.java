package rise.myapplication.Screens;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.List;
import java.util.Locale;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.TouchEvent;
import rise.myapplication.Game.Game;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.Util.Scale;
import rise.myapplication.World.GameScreen;
import rise.myapplication.UI.PushButton;

public class HowToScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final Rect helpLogo, background;
    private final PushButton backButton;
    private final Rect ordinaryPlatform, movingPlatform, explodingPlatform, enemy1, enemy2;
    private final Rect collect1, collect2, collect3, collect4, collect5, collect6;
    private final Paint paint;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public HowToScreen(Game game)
    {
        super("HowToScreen", game);

        //Rects for the logo and background
        helpLogo = new Rect(Scale.getX(20), Scale.getY(0), Scale.getX(80), Scale.getY(15));
        background = new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight());

        //rects for the various in game objects
        ordinaryPlatform = new Rect(Scale.getX(10),Scale.getY(30),Scale.getX(25),Scale.getY(33));
        movingPlatform = new Rect(Scale.getX(30),Scale.getY(35),Scale.getX(45),Scale.getY(38));
        explodingPlatform = new Rect(Scale.getX(50),Scale.getY(30),Scale.getX(65),Scale.getY(33));
        enemy1 = new Rect(Scale.getX(75),Scale.getY(50),Scale.getX(95),Scale.getY(65));
        enemy2 = new Rect(Scale.getX(50),Scale.getY(50),Scale.getX(70),Scale.getY(65));
        collect1=new Rect(Scale.getX(20),Scale.getY(75),Scale.getX(35),Scale.getY(85));
        collect2=new Rect(Scale.getX(50),Scale.getY(75),Scale.getX(65),Scale.getY(85));
        collect3 = new Rect(Scale.getX(70),Scale.getY(75),Scale.getX(95),Scale.getY(85));
        collect4=new Rect(Scale.getX(20),Scale.getY(88),Scale.getX(35),Scale.getY(98));
        collect5 = new Rect(Scale.getX(50),Scale.getY(88),Scale.getX(65),Scale.getY(98));
        collect6 = new Rect (Scale.getX(70),Scale.getY(88),Scale.getX(95),Scale.getY(98));

        //creating the backbutton
        backButton = new PushButton(Scale.getX(95), Scale.getY(15), Scale.getX(10), Scale.getY(10), "Exit", this);

        //creating and setting up the paint object
        Typeface plain = Typeface.createFromAsset(MainActivity.getContext().getAssets(), String.format(Locale.US, "Fonts/%s", "Tiki Tropic Bold.ttf"));
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        paint.setTypeface(bold);
        paint.setFakeBoldText(true);
        paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
    }

    //override the GameScreen's update method
    @Override
    public void update(ElapsedTime elapsedTime) {

        //update the backbutton
        backButton.update(elapsedTime);

        // Process any touch events occurring since the update
        Input input = mGame.getInput();

        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);
            //return to menu screen if the back button is pressed
            if (backButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y ) && touchEvent.type == TouchEvent.TOUCH_UP) {
                mGame.getScreenManager().removeScreen(this.getName());
                MenuScreen menuScreen = new MenuScreen(mGame);
                mGame.getScreenManager().addScreen(menuScreen);
            }
        }
    }

    //override the GameScreen's draw method
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        //draw the background and logo
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SpaceBackground"), null, background, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("helpg"), null, helpLogo, null);

        //draw the back button
        backButton.draw(graphics2D);

        //draw the text
        graphics2D.drawText("Jump on these:", Scale.getX(15), Scale.getY(25), paint);
        graphics2D.drawText("Avoid These:", Scale.getX(60), Scale.getY(45), paint);
        graphics2D.drawText("Collect These:", Scale.getX(15), Scale.getY(70), paint);

        //draw all the various in game objects
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("NormalPlatform"), null, ordinaryPlatform, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("MovingPlatform"), null, movingPlatform, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("ExplodingPlatform"), null, explodingPlatform, null);

        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("thing"), null, enemy1, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("RocketWarrior"), null, enemy2, null);

        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Coin"), null, collect1, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Rocket"), null, collect2, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Magnet"), null, collect3, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Springs"), null, collect4, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Heart"), null, collect5, null);
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("Shield"), null, collect6, null);
    }

}
