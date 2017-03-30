package rise.myapplication.Util;

import rise.myapplication.Util.ElapsedTime;

/**
 * Created by 40126424 on 03/02/2016.
 */
//TODO make score a variable in Player class and remove this
public class Score {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //score variables
    public int initialScore=0;
    public int score;

    //time variables
    private float timeElapsed;
    private final double startTime=0.00;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Score(int initialScore){
        this.initialScore=initialScore;
    }

    //update score
    public void update(ElapsedTime elapsedTime){
        timeElapsed=(float)(elapsedTime.totalTime-startTime);
        score+=2;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public int getScore(){
        return score;
    }
    public void setScore(int score){
        this.score=score;
    }

}
