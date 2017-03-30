package rise.myapplication.Screens;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Game.Game;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.World.GameScreen;

/**
 * Created by 40133490 on 25/02/2016.
 */
public class LoadingScreen extends GameScreen {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //Game object
    private final Game mGame;

    //A ProgressDialog object
    private ProgressDialog progressDialog;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public LoadingScreen(Game game){
        super("LoadingScreen", game);

        //execute the private AsyncTask LoadViewTask
        new LoadViewTask().execute();
        mGame = game;
    }

    //update screen
    public void update(ElapsedTime elapsedTime)
    {

    }

    //draw screen
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){}

    //To use the AsyncTask, it must be subclassed
    private class LoadViewTask extends AsyncTask<Void, Integer, Void>
    {
        //Before running code in separate thread
        @Override
        protected void onPreExecute()
        {
            //Create a new progress dialog
            progressDialog = new ProgressDialog(MainActivity.getContext());
            //Set the progress dialog to display a horizontal progress bar
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //Set the dialog title to 'Loading...'
            progressDialog.setTitle("Loading...");
            //Set the dialog message to 'Loading application View, please wait...'
            progressDialog.setMessage("Loading the most super mega awesome game ever");
            //This dialog can't be canceled by pressing the back key
            progressDialog.setCancelable(false);
            //This dialog isn't indeterminate
            progressDialog.setIndeterminate(false);
            //The maximum number of items is 100
            progressDialog.setMax(100);
            //Set the current progress to zero
            progressDialog.setProgress(0);
            //Display the progress dialog
            progressDialog.show();
        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params)
        {
            /* This is just a code that delays the thread execution 4 times,
             * during 850 milliseconds and updates the current progress. This
             * is where the code that is going to be executed on a background
             * thread must be placed.
             */
            try
            {
                //Get the current thread's token
                synchronized (this)
                {
                    mGame.getAssetManager().loadBitmaps();
                    publishProgress(33);
                    this.wait(850);
                    mGame.getAssetManager().loadMusic();
                    publishProgress(66);
                    this.wait(850);

                    mGame.getDatabaseHelper().connect();
                    /*mGame.username = "conor";
                    mGame.password = "conor1";
                    mGame.loggedIn = mGame.getDatabaseHelper().login(mGame.username, mGame.password);
                    Log.e("Logged in: ", ""+mGame.loggedIn);*/
                    mGame.getAssetManager().loadSounds();
                    publishProgress(100);
                    this.wait(850);
                    //Wait 850 milliseconds
                }
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values)
        {
            //set the current progress of the progress dialog
            progressDialog.setProgress(values[0]);
        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result) {
            //close the progress dialog
            progressDialog.dismiss();
            mGame.getScreenManager().removeScreen("LoadingScreen");
            GameSplashScreen splashScreen = new GameSplashScreen(mGame);
            mGame.getScreenManager().addScreen(splashScreen);
        }
    }
}
