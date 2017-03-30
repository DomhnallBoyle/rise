package rise.myapplication.Game;

import android.app.Fragment;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rise.myapplication.Engine.Graphics.CanvasRenderSurface;
import rise.myapplication.Engine.Graphics.IRenderSurface;
import rise.myapplication.Engine.IO.DatabaseHelper;
import rise.myapplication.Engine.IO.FileIO;
import rise.myapplication.Engine.Input.Input;
import rise.myapplication.Engine.Managers.CustomAssetManager;
import rise.myapplication.Engine.Managers.ScreenManager;
import rise.myapplication.Engine.IO.SharedPreferences;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.World.GameScreen;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract class Game extends Fragment {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //login info
    public String username, password;
    public boolean loggedIn = false;

    private int mTargetFramesPerSecond = 5;
    private float mAverageFramesPerSecond;
    private CustomAssetManager mAssetManager;
    private ScreenManager mScreenManager;
    private Input mInput;
    private FileIO mFileIO;
    private IRenderSurface mRenderSurface;
    private GameLoop mLoop;
    private int mScreenWidth = -1, mScreenHeight = -1;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private int player1Score = 0, player2Score = 0;
    private boolean twoPlayerToggle = false;
    private Player player1, player2;

    // /////////////////////////////////////////////////////////////////////////
    // Overriding methods from Fragment
    // /////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoop = new GameLoop();
        mFileIO = new FileIO(getActivity().getApplicationContext());
        mAssetManager = new CustomAssetManager(mFileIO);
        mScreenManager = new ScreenManager();
        sharedPreferences = new SharedPreferences();
        sharedPreferences.loadScores();
        databaseHelper = new DatabaseHelper("jdbc:mysql://db4free.net:3306/rise");
        getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRenderSurface = new CanvasRenderSurface(this, getActivity());
        View view = mRenderSurface.getAsView();
        mInput = new Input(getActivity(), view);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        GameScreen.setScreenWidth(mScreenWidth);
        GameScreen.setScreenHeight(mScreenHeight);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // If needed, resume the current game screen
        if (mScreenManager.getCurrentScreen() != null)
            mScreenManager.getCurrentScreen().resume();

        // Resume the game loop
        mLoop.resume();
    }

    @Override
    public void onPause() {

        mLoop.pause();

        if (mScreenManager.getCurrentScreen() != null)
            mScreenManager.getCurrentScreen().pause();

        super.onPause();
    }

    @Override
    public void onDestroy() {

        mScreenManager.dispose();

        super.onDestroy();
    }

    public boolean onBackPressed() {
        return false;
    }

    private void doUpdate(ElapsedTime elapsedTime) {
        (mInput).resetAccumulators();

        GameScreen gameScreen = mScreenManager.getCurrentScreen();
        if (gameScreen != null)
            gameScreen.update(elapsedTime);

        notifyUpdateCompleted();
    }

    public void notifyUpdateCompleted() {
        mLoop.notifyUpdateCompleted();
    }

    private void doDraw(ElapsedTime elapsedTime) {
        GameScreen gameScreen = mScreenManager.getCurrentScreen();
        if (gameScreen != null)
            mRenderSurface.render(elapsedTime, gameScreen);
    }

    public void notifyDrawCompleted() {
        mLoop.notifyDrawCompleted();
    }


    // /////////////////////////////////////////////////////////////////////////
    // Private GameLoop class
    // /////////////////////////////////////////////////////////////////////////

    private class GameLoop implements Runnable {

        //inner boolean lock class
        class BooleanLock {
            boolean isLocked;

            public BooleanLock(boolean isLocked) {
                this.isLocked = isLocked;
            }

        }

        // /////////////////////////////////////////////////////////////////////////
        // Properties
        // /////////////////////////////////////////////////////////////////////////

        final BooleanLock update, draw;
        Thread renderThread = null;
        volatile boolean running = false;
        final ElapsedTime elapsedTime;
        long targetStepPeriod;
        final double maximumStepPeriodScale = 3.0f;
        long startRun, startStep, endStep, sleepTime, overSleepTime;

        // /////////////////////////////////////////////////////////////////////////
        // Constructor
        // /////////////////////////////////////////////////////////////////////////

        public GameLoop() {
            targetStepPeriod = 1000000000 / mTargetFramesPerSecond;
            elapsedTime = new ElapsedTime();
            update = new BooleanLock(false);
            draw = new BooleanLock(false);
        }

        @Override
        public void run() {

            if (mScreenManager.getCurrentScreen() == null) {
                String errorTag = "Rise Error:";
                String errorMessage = "You need to add a game screen to the screen manager.";
                Log.e(errorTag, errorMessage);
                throw new RuntimeException(errorTag + errorMessage);
            }

            startRun = System.nanoTime() - targetStepPeriod;
            startStep = startRun;
            overSleepTime = 0L;

            try {
                while (running) {

                    // Update the timing information
                    long currentTime = System.nanoTime();
                    elapsedTime.totalTime = (currentTime - startRun) / 1000000000.0;
                    elapsedTime.stepTime = (currentTime - startStep) / 1000000000.0;
                    startStep = currentTime;

                    // Weighted average update of the average number of frames
                    // per second
                    mAverageFramesPerSecond = 0.85f * mAverageFramesPerSecond
                            + 0.15f * (1.0f / (float) elapsedTime.stepTime);

                    // If needed ensure the reported step time is not abnormally large
                    if (elapsedTime.stepTime > (targetStepPeriod / 1000000000.0) * maximumStepPeriodScale)
                        elapsedTime.stepTime =
                                (targetStepPeriod / 1000000000.0) * maximumStepPeriodScale;

                    // Trigger an update
                    synchronized (update) {
                        update.isLocked = true;
                    }
                    doUpdate(elapsedTime);
                    // Wait for the update to complete before progressing
                    synchronized (update) {
                        if (update.isLocked) {
                            update.wait();
                        }
                    }

                    // Trigger a draw request
                    synchronized (draw) {
                        draw.isLocked = true;
                    }
                    doDraw(elapsedTime);
                    // Wait for the draw to complete before progressing
                    // If a plan-update-draw approach was employed the
                    // wait for the draw would be tested post plan completion.
                    synchronized (draw) {
                        if (draw.isLocked) {
                            draw.wait();
                        }
                    }

                    // Measure how long the update/draw took to complete and
                    // how long to sleep until the next cycle is due. This may
                    // be a negative number (we've exceeded the 'available'
                    // time).
                    endStep = System.nanoTime();
                    sleepTime = (targetStepPeriod - (endStep - startStep))
                            - overSleepTime;

                    // If needed put the thread to sleep
                    if (sleepTime > 0) {
                        Thread.sleep(sleepTime / 1000000L); // Covert ns into ms

                        // Determine how much longer we slept than was
                        // originally requested, we'll correct for this error
                        // next frame
                        overSleepTime = (System.nanoTime() - endStep)
                                - sleepTime;
                    } else {
                        overSleepTime = 0L;
                    }
                }

            } catch (InterruptedException e) {
                Log.e("Error", e.getMessage());
            }
        }

        public void notifyDrawCompleted() {
            synchronized (draw) {
                draw.isLocked = false;
                draw.notifyAll();
            }
        }

        public void notifyUpdateCompleted() {
            synchronized (update) {
                update.isLocked = false;
                update.notifyAll();
            }
        }

        public void pause() {
            running = false;
            while (true) {
                try {
                    renderThread.join();
                    return;
                } catch (InterruptedException e) {
                    Log.e("Error", e.getMessage());
                }
            }
        }

        public void resume() {
            running = true;

            draw.isLocked = false;
            update.isLocked = false;

            renderThread = new Thread(this);
            renderThread.start();
        }

        public ElapsedTime getElapsedTime() {
            return elapsedTime;
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public GameLoop getGameLoop() {
        return mLoop;
    }

    public int getmTargetFramesPerSecond() {
        return mTargetFramesPerSecond;
    }

    public void setmTargetFramesPerSecond(int targetFramesPerSecond) {
        mTargetFramesPerSecond = targetFramesPerSecond;
        if (mLoop != null)
            mLoop.targetStepPeriod = 1000000000 / mTargetFramesPerSecond;
    }

    public float getmAverageFramesPerSecond() {
        return mAverageFramesPerSecond;
    }

    public CustomAssetManager getAssetManager() {
        return mAssetManager;
    }

    public ScreenManager getScreenManager() {
        return mScreenManager;
    }

    public Input getInput() {
        return mInput;
    }

    public FileIO getFileIO() {
        return mFileIO;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public void setPlayer1score(int score) {
        player1Score = score;
    }

    public void setPlayer2score(int score) {
        player2Score = score;
    }

    public void setTwoPlayerToggle(boolean twoPlayer) {
        twoPlayerToggle = twoPlayer;
    }

    public void setPlayer1(Player player) {
        player1 = player;
    }

    public void setPlayer2(Player player) {
        player2 = player;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public boolean getTwoPlayerToggle() {
        return twoPlayerToggle;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    private int difficulty = 0;

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public ElapsedTime getElapsedTime()
    {
        return getGameLoop().getElapsedTime();
    }
}

