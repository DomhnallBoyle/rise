package rise.myapplication.Engine.Input;

/**
 * Created by DomhnallBoyle on 12/02/2016.
 */
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.List;
import java.util.Locale;

import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.World.GameScreen;
import rise.myapplication.Util.Scale;

/**
 * Created by DomhnallBoyle on 11/02/2016.
 */
public class KeyBoard {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final String[] characters = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "_", "-", "SPACE", "UPPERCASE", "DEL", "DONE"};
    private final String[] uppercase = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private final String[] lowercase = {"a", "b", "c", "d", "e", "f", "g", "h", "i", " j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private final Rect[] buttons; //includes all characters
    private final Rect background;

    private final Paint backgroundPaint;
    private final Paint rectPaint;
    private final Paint textPaint;

    private String concatenated = "";
    private boolean upperCase = false, isShowing = false;

    private static final int NUMBER_OF_CHARACTERS = 42;
    private static final int FIRST_ROW_NUMBERS = 9;
    private static final int SECOND_ROW_CHARACTERS = 19;
    private static final int THIRD_ROW_CHARACTERS = 29;
    private static final int FOURTH_ROW_CHARACTERS = 37;
    private static final int FOURTH_ROW_SPACE = 38;
    private static final int FIFTH_ROW_CASE = 39;
    private static final int FIFTH_ROW_DEL = 40;
    private static final int FIFTH_ROW_DONE = 41;
    private static final int SPACING_APART = 10;
    private static final int LETTER_LENGTH = 25;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public KeyBoard()
    {
        //creating array of button rects and background
        buttons = new Rect[NUMBER_OF_CHARACTERS];
        background = new Rect(Scale.getX(0), Scale.getY(65), Scale.getX(100), Scale.getY(100));

        Typeface plain = Typeface.createFromAsset(MainActivity.getContext().getAssets(), String.format(Locale.US, "Fonts/%s", "Tiki Tropic Bold.ttf"));
        Typeface bold = Typeface.create(plain, Typeface.BOLD);

        //creating paint objects
        backgroundPaint = new Paint();
        rectPaint = new Paint();
        textPaint = new Paint();
        textPaint.setTypeface(bold);

        //setting paint properties
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);
        rectPaint.setColor(Color.DKGRAY);
        rectPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(70);

        //set up button rects in array
        instantiate();
    }

    //method to set up positions of buttons rects
    private void instantiate()
    {
        //spacing for each line on keyboard
        int spacing1 = 0, spacing2 = 0, spacing3 = 0, spacing4 = 0;

        //create each button and seperate them using spacing
        for (int i=0; i<buttons.length; i++)
        {
            if (i<=FIRST_ROW_NUMBERS)
            {
                buttons[i] = new Rect(Scale.getX(2.5+spacing1), Scale.getY(67), Scale.getX(7.5+spacing1), Scale.getY(71));
                spacing1+=SPACING_APART;
            }
            else if (i>FIRST_ROW_NUMBERS && i<=SECOND_ROW_CHARACTERS)
            {
                buttons[i] = new Rect(Scale.getX(2.5+spacing2), Scale.getY(74), Scale.getX(7.5+spacing2), Scale.getY(78));
                spacing2+=SPACING_APART;
            }
            else if (i>SECOND_ROW_CHARACTERS && i<=THIRD_ROW_CHARACTERS)
            {
                buttons[i] = new Rect(Scale.getX(2.5+spacing3), Scale.getY(81), Scale.getX(7.5+spacing3), Scale.getY(85));
                spacing3+=SPACING_APART;
            }
            else if (i>THIRD_ROW_CHARACTERS && i<=FOURTH_ROW_CHARACTERS)
            {
                buttons[i] = new Rect(Scale.getX(2.5+spacing4), Scale.getY(88), Scale.getX(7.5+spacing4), Scale.getY(92));
                spacing4+=SPACING_APART;
            }
            else if (i==FOURTH_ROW_SPACE)
            {
                buttons[i] = new Rect(Scale.getX(2.5+spacing4), Scale.getY(88), Scale.getX(7.5+spacing4+10), Scale.getY(92));
            }
            else if (i==FIFTH_ROW_CASE)
            {
                buttons[i] = new Rect(Scale.getX(12.5), Scale.getY(95), Scale.getX(47), Scale.getY(99));
            }
            else if (i==FIFTH_ROW_DEL)
            {
                buttons[i] = new Rect(Scale.getX(52.5), Scale.getY(95), Scale.getX(67), Scale.getY(99));
            }
            else
            {
                buttons[i] = new Rect(Scale.getX(72.5), Scale.getY(95), Scale.getX(87), Scale.getY(99));
            }
        }
    }

    //method to draw the keyboard
    public void draw(IGraphics2D graphics2D)
    {
        //if the keyboard is showing
        if (isShowing)
        {
            //draw the background
            graphics2D.drawRect(background, backgroundPaint);

            //for all the buttons in the button array
            for (int i=0; i<buttons.length; i++)
            {
                if (i<=FIRST_ROW_NUMBERS)
                {
                    graphics2D.drawRect(buttons[i], rectPaint);
                    graphics2D.drawText(characters[i], Scale.getX((5 * i) + (5 * i) + 3.5), Scale.getY(70), textPaint);
                }
                else if (i>FIRST_ROW_NUMBERS && i<=SECOND_ROW_CHARACTERS)
                {
                    graphics2D.drawRect(buttons[i], rectPaint);
                    graphics2D.drawText(characters[i], Scale.getX((5*(i-10))+(5*(i-10))+3.5), Scale.getY(77), textPaint);
                }
                else if (i>SECOND_ROW_CHARACTERS && i<=THIRD_ROW_CHARACTERS)
                {
                    graphics2D.drawRect(buttons[i], rectPaint);
                    graphics2D.drawText(characters[i], Scale.getX((5*(i-20))+(5*(i-20))+3.5), Scale.getY(84), textPaint);
                }
                else if (i>THIRD_ROW_CHARACTERS && i<=FOURTH_ROW_CHARACTERS)
                {
                    graphics2D.drawRect(buttons[i], rectPaint);
                    graphics2D.drawText(characters[i], Scale.getX((5*(i-30))+(5*(i-30))+3.5), Scale.getY(91), textPaint);
                }
                else if (i==FOURTH_ROW_SPACE)
                {
                    graphics2D.drawRect(buttons[i], rectPaint);
                    graphics2D.drawText(characters[i], Scale.getX((5*(i-30))+(5*(i-30))+3.0), Scale.getY(91), textPaint);
                }
                else if (i==FIFTH_ROW_CASE)
                {
                    graphics2D.drawRect(buttons[i], rectPaint);
                    graphics2D.drawText(characters[i], Scale.getX(16), Scale.getY(98), textPaint);
                }
                else if (i==FIFTH_ROW_DEL)
                {
                    graphics2D.drawRect(buttons[i], rectPaint);
                    graphics2D.drawText(characters[i], Scale.getX(55), Scale.getY(98), textPaint);
                }
                else
                {
                    graphics2D.drawRect(buttons[i], rectPaint);
                    graphics2D.drawText(characters[i], Scale.getX(74), Scale.getY(98), textPaint);
                }
            }
        }
    }

    //updating the keyboard
    //method returns a string representing what the user types (player's name)
    public String update(GameScreen gameScreen) {

        //if uppercase button pressed, change letters to uppercase, else lowercase if lowercase button pressed
        if (upperCase)
        {
            for (int i=0; i<=LETTER_LENGTH; i++)
            {
                characters[i+SPACING_APART] = uppercase[i];
            }
        }
        else
        {
            for (int i=0; i<=LETTER_LENGTH; i++)
            {
                characters[i+SPACING_APART] = lowercase[i];
            }
        }

        Input input = gameScreen.getGame().getInput();
        List<TouchEvent> touchEvents = input.getTouchEvents();

        if (touchEvents.size() > 0) {
            TouchEvent touchEvent = touchEvents.get(0);

            if(touchEvent.type == TouchEvent.TOUCH_UP)
            {
                for (int i = 0; i < buttons.length; i++)
                {
                    if (i <= FOURTH_ROW_CHARACTERS)
                    {
                        if (buttons[i].contains((int) touchEvent.x, (int) touchEvent.y))
                        {
                            concatenated += characters[i];
                            checkLength();
                        }
                    }
                }
                if (buttons[FOURTH_ROW_SPACE].contains((int) touchEvent.x, (int) touchEvent.y))
                {
                    concatenated += " ";
                    checkLength();
                }
                else if (buttons[FIFTH_ROW_CASE].contains((int) touchEvent.x, (int) touchEvent.y))
                {
                    if (upperCase)
                    {
                        upperCase = false;
                        characters[FIFTH_ROW_CASE] = "UPPERCASE";
                    }
                    else
                    {
                        upperCase = true;
                        characters[FIFTH_ROW_CASE] = "LOWERCASE";
                    }
                }
                else if (buttons[FIFTH_ROW_DEL].contains((int) touchEvent.x, (int) touchEvent.y))
                {
                    //delete the last letter by using the substring method
                    if (!(concatenated.length() == 0))
                    {
                        concatenated = concatenated.substring(0, concatenated.length() - 1);
                    }
                }
                else if (buttons[FIFTH_ROW_DONE].contains((int) touchEvent.x, (int) touchEvent.y))
                {
                    isShowing = false;
                }
            }
        }
        return concatenated;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public boolean getCase()
    {
        return upperCase;
    }
    public boolean getShowing()
    {
        return isShowing;
    }
    public void setShowing(boolean isShowing)
    {
        this.isShowing = isShowing;
    }
    //validation method to make sure string is not greater than 10
    private void checkLength()
    {
        if (concatenated.length() > 10)
        {
            concatenated = concatenated.substring(0, 10);
        }
    }
}
