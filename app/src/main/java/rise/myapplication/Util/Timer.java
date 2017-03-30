package rise.myapplication.Util;


/**
 * Created by 40126424 on 17/12/2015.
 */
public class Timer {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private double startTime = 0.00;
    private boolean firstDraw = true;
    private float timeElapsed;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Timer(double startTime, boolean firstDraw){
        this.startTime=startTime;
        this.firstDraw=firstDraw;
    }

    //method to update the timer
    public void update(ElapsedTime elapsedTime,boolean isPaused){
        if (firstDraw) {
            startTime = elapsedTime.totalTime;
            firstDraw = false;
        }
        if(!isPaused) {
            timeElapsed += (float) (elapsedTime.stepTime);
        }
    }


    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public int getTimeElapsed(){
        return (int)timeElapsed;
    }
    public double getStartTime(){
        return  startTime;
    }
    public void setStartTime(double startTime)
    {
        this.startTime = startTime;
    }
    public void setTimeHighScoreWasSet(double theTime){
        theTime=timeElapsed;
    }
}
