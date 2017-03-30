package rise.myapplication.Screens;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Audio.Music;
import rise.myapplication.Engine.Audio.Sound;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Input.TouchEvent;
import rise.myapplication.Game.Game;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.Util.Scale;
import rise.myapplication.UI.PushButton;
import rise.myapplication.World.GameScreen;

/**
 * Created by ConorMurray on 10/12/2015.
 */
public class OptionsScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //declare rects
    private final Rect doodleBackground, controlIcon, themeImage, characterCard, optionsLogo, difficultyRect;

    //declare PushButtons
    private final PushButton controlLeftArrow, controlRightArrow, soundButton, backButton, themeLeftArrow, themeRightArrow;
    private final PushButton characterLeftArrow, characterRightArrow, difficultyLeftArrow, difficultyRightArrow;
    private final ArrayList<PushButton> buttons;

    //control Strings
    private final String [] controls = {"Buttons", "Accelerometer"};
    private final String [] controlImages = {"LeftRightToggle", "Accelerometer"};
    public static int control = 0;

    //themes
    private final String [] themes = {"Space", "Mountain"};
    private final String [] themeImages = {"SpaceBackground", "MountainBackground"};
    public static int theme = 0;

    //characters
    private final String []characters ={"Blue Eyes","Dark Magician","Time Wizard"};
    private final String []characterImages ={"Blue Eyes", "Dark Magician", "Time Wizard"};
    public static int character = 0;

    //difficulty
    private final String[] difficultyImages = {"Easy", "Medium", "Hard"};
    public static int difficulty = 0;

    //previous screen
    private final String previousScreen;

    //Player object
    private final Player mPlayer;

    //Paint object
    Paint paint = new Paint();

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public OptionsScreen(Game game, String fromScreen, Player player)
    {
        super("OptionsScreen", game);
        //set previous screen
        this.previousScreen = fromScreen;

        //set player
        mPlayer = player;

        //setup PushButtons
        buttons = new ArrayList<>();
        controlLeftArrow = new PushButton(Scale.getX(27.5), Scale.getY(20), Scale.getX(15), Scale.getY(10), "LeftArrow", this);
        controlRightArrow = new PushButton(Scale.getX(72.5), Scale.getY(20), Scale.getX(15), Scale.getY(10), "RightArrow", this);
        backButton = new PushButton(Scale.getX(95), Scale.getY(15), Scale.getX(10), Scale.getY(10), "Exit", this);
        soundButton = new PushButton(Scale.getX(50), Scale.getY(35), Scale.getX(20), Scale.getY(10), "SoundOn", this);
        themeLeftArrow = new PushButton(Scale.getX(27.5), Scale.getY(50), Scale.getX(15), Scale.getY(10), "LeftArrow", this);
        themeRightArrow = new PushButton(Scale.getX(72.5), Scale.getY(50), Scale.getX(15), Scale.getY(10), "RightArrow", this);
        characterLeftArrow = new PushButton(Scale.getX(22.5), Scale.getY(70), Scale.getX(15), Scale.getY(10),"LeftArrow", this);
        characterRightArrow=  new PushButton(Scale.getX(77.5), Scale.getY(70), Scale.getX(15), Scale.getY(10), "RightArrow", this);
        difficultyLeftArrow = new PushButton(Scale.getX(22.5), Scale.getY(90), Scale.getX(15), Scale.getY(10), "LeftArrow", this);
        difficultyRightArrow = new PushButton(Scale.getX(77.5), Scale.getY(90), Scale.getX(15), Scale.getY(10), "RightArrow", this);

        //adding push buttons to arraylist
        buttons.add(controlLeftArrow);
        buttons.add(controlRightArrow);
        buttons.add(backButton);
        buttons.add(soundButton);
        buttons.add(themeLeftArrow);
        buttons.add(themeRightArrow);
        buttons.add(characterLeftArrow);
        buttons.add(characterRightArrow);
        buttons.add(difficultyLeftArrow);
        buttons.add(difficultyRightArrow);

        //setup rects
        doodleBackground = new Rect(0, 0, Scale.getX(100), Scale.getY(100));
        controlIcon = new Rect(Scale.getX(40), Scale.getY(15), Scale.getX(60), Scale.getY(27));
        themeImage = new Rect(Scale.getX(35), Scale.getY(45), Scale.getX(65), Scale.getY(55));
        characterCard = new Rect(Scale.getX(30), Scale.getY(60), Scale.getX(70), Scale.getY(80));
        optionsLogo = new Rect(Scale.getX(20),Scale.getY(0),Scale.getX(80),Scale.getY(15));
        difficultyRect = new Rect(Scale.getX(35), Scale.getY(85), Scale.getX(65), Scale.getY(95));
    }

    //update OptionsScreen
    @Override
    public void update(ElapsedTime elapsedTime)
    {

        Input input = mGame.getInput();

        for(int i = 0; i < buttons.size(); i++)
        {
            buttons.get(i).update(elapsedTime);
        }


        List<TouchEvent> touchEvents = input.getTouchEvents();
        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);
            if(touchEvent.type == TouchEvent.TOUCH_UP) {
                //backbutton clicked. Depending on the previous screen
                if (backButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y) && this.previousScreen.equalsIgnoreCase("MainMenu")) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    MenuScreen menuScreen = new MenuScreen(mGame, mPlayer);
                    mGame.getScreenManager().addScreen(menuScreen);
                }
                else if (backButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y) && this.previousScreen.equalsIgnoreCase("GameOver")) {
                    mGame.getScreenManager().removeScreen(this.getName());
                    GameOverScreen gameOverScreen = new GameOverScreen(mGame, null);
                    mGame.getScreenManager().addScreen(gameOverScreen);
                }
                //soundbutton clicked
                else if (soundButton.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    Sound.setPlay(!Sound.getPlay());
                    Music.setPlay(!Music.getPlay());
                    if (Music.getPlay()) {
                        mGame.getAssetManager().getMusic("theme").play();
                    } else {
                        mGame.getAssetManager().getMusic("theme").pause();
                    }
                }
                //loop over each options using left and right arrows
                //themeArrows clicked
                else if (themeRightArrow.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    if (theme >= themes.length - 1) {
                        theme = 0;
                    } else {
                        theme += 1;
                    }
                }
                else if (themeLeftArrow.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    if (theme == 0) {
                        theme = themes.length - 1;
                    } else {
                        theme -= 1;
                    }
                }
                //accelerometer controls clicked
                else if (controlRightArrow.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    if (control >= controls.length - 1) {
                        control = 0;
                        mPlayer.setAccelerometerToggle(!mPlayer.getAccelerometerToggle());
                    } else {
                        control += 1;
                        mPlayer.setAccelerometerToggle(!mPlayer.getAccelerometerToggle());
                    }
                }
                else if (controlLeftArrow.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    if (control == 0) {
                        control = controls.length - 1;
                        mPlayer.setAccelerometerToggle(!mPlayer.getAccelerometerToggle());
                    } else {
                        control -= 1;
                        mPlayer.setAccelerometerToggle(!mPlayer.getAccelerometerToggle());
                    }
                }
                //character selection arrows clicked
                else if (characterRightArrow.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    if (character >= characters.length - 1) {
                        character = 0;
                    } else {
                        character += 1;
                    }
                    mPlayer.setPlayerBitmap();
                }
                else if (characterLeftArrow.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)) {
                    if (character == 0) {
                        character = characters.length - 1;
                    } else {
                        character -= 1;
                    }
                    mPlayer.setPlayerBitmap();
                }
                //difficulty arrows clicked
                else if (difficultyLeftArrow.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)){
                    if (difficulty == 0) {
                        difficulty = difficultyImages.length - 1;
                        mGame.setDifficulty(difficulty);
                    } else {
                        difficulty -= 1;
                        mGame.setDifficulty(difficulty);
                    }
                }
                else if (difficultyRightArrow.getDrawScreenRect().contains((int) touchEvent.x, (int) touchEvent.y)){
                    if (difficulty >= difficultyImages.length - 1) {
                        difficulty = 0;
                        mGame.setDifficulty(difficulty);
                    } else {
                        difficulty += 1;
                        mGame.setDifficulty(difficulty);
                    }
                }
            }
        }
    }

    //draw OptionsScreen
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {

        //draw background
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("SpaceBackground"), null, doodleBackground, null);

        //draw sound image
        if(Sound.getPlay()){
            soundButton.setBitmap(mGame.getAssetManager().getBitmap("SoundOn"));
        }else{
            soundButton.setBitmap(mGame.getAssetManager().getBitmap("SoundOff"));
        }

        //draw PushButtons
        for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).draw(graphics2D);
        }

        //draw options logo
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap("optiong"), null, optionsLogo, null);
        //draw accelerometer image
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap(controlImages[control]), null, controlIcon, null);
        //draw theme image
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap(themeImages[theme]), null, themeImage, null);
        //draw character image
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap(characterImages[character]), null, characterCard, null);
        //draw difficulty image
        graphics2D.drawBitmap(mGame.getAssetManager().getBitmap(difficultyImages[difficulty]), null, difficultyRect, null);
    }

}