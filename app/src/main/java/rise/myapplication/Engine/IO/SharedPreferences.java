package rise.myapplication.Engine.IO;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Util.HighScore;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.Util.Scale;

/**
 * Created by 40126424 on 16/02/2016.
 */
public class SharedPreferences {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //paint object
    private final Paint paint;

    //highscores
    private final ArrayList<HighScore>highScores;

    //sharedpreferences
    private final android.content.SharedPreferences prefs;

    //sharedpreferences editor
    private final android.content.SharedPreferences.Editor editor;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public SharedPreferences(){
        highScores= new ArrayList<>();

        Typeface plain = Typeface.createFromAsset(MainActivity.getContext().getAssets(), String.format(Locale.US, "Fonts/%s", "Tiki Tropic Bold.ttf"));
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        paint.setFakeBoldText(true);
        paint.setTypeface(bold);
        paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);

        prefs = MainActivity.getContext().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    //save achievement locally
    public void saveAchievement(String name, boolean value){
        editor.putBoolean(name, value);
        editor.commit();
    }

    //load saved achievement
    public boolean loadAchievement(String name){
        return prefs.getBoolean(name, false);
    }

    //save int
    public void saveInt(String name, int value){
        editor.putInt(name, value);
        editor.commit();
    }

    //load int
    public int loadInt(String name, int defValue){
        return prefs.getInt(name, defValue);
    }

    //save new score
    public void saveScore(String name, int score){
        //add to highscores arraylist
        highScores.add(new HighScore(name, score));

        //sort highscores in order
        Collections.sort(highScores, new Comparator<HighScore>() {
            @Override
            public int compare(HighScore lhs, HighScore rhs) {
                return rhs.getScore() - lhs.getScore();
            }
        });

        //save size of highscores
        editor.putInt("size", highScores.size());

        //save all scores in order
        for(int i=0;i<highScores.size();i++){
            editor.putString("names_" + i, highScores.get(i).getName());
            editor.putInt("scores_" + i, highScores.get(i).getScore());
        }
        //commit changes
        editor.commit();
    }

    //load highscores
    public void loadScores(){
        for(int i=0; i<prefs.getInt("size",0);i++){
            highScores.add(new HighScore(prefs.getString("names_"+i,""),prefs.getInt("scores_"+i,100000)));
        }
    }
    
    //save new string
    public void saveString(String name)
    {
        editor.putString("PlayerName", name);
        editor.commit();
    }
    
    //load string
    public String loadString()
    {
        return prefs.getString("PlayerName", "");
    }

    //draw
    public void draw(IGraphics2D graphics2D)
    {

        int size = prefs.getInt("size",0);

        for(int i=0;i<size;i++) {
            if (!(i > 9))
            {
                graphics2D.drawText(Integer.toString(i+1),  Scale.getX(10), Scale.getY(35 + (5 * i)), paint);
                graphics2D.drawText(prefs.getString("names_"+i, ""), Scale.getX(40), Scale.getY(35 + (5 * i)), paint);
                graphics2D.drawText("" + prefs.getInt("scores_"+i, 0), Scale.getX(77), Scale.getY(35 + (5*i)), paint);
            }
        }

    }
}