package rise.myapplication.Engine.Audio;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.IOException;

/**
 * Created by DomhnallBoyle on 16/11/2015.
 */
public class Music implements OnCompletionListener {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private static boolean play = true;
    private final MediaPlayer mediaPlayer;
    private boolean isPrepared = false;
    private final String assetFile;
    public static void setPlay(boolean play){Music.play = play;}
    public static boolean getPlay(){return play;}

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    //constructor loads and prepares music file
    public Music(AssetFileDescriptor assetFileDescriptor)
    {
        this.assetFile = assetFileDescriptor.getFileDescriptor().toString();
        this.mediaPlayer = new MediaPlayer();

        try
        {
            this.mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            this.mediaPlayer.prepare();
            this.isPrepared = true;
            this.mediaPlayer.setOnCompletionListener(this);
        }
        catch(IOException e)
        {
            Log.e("Rise", "Could not load music file: " + this.assetFile);
        }
    }

    //method to play music
    public void play()
    {
        if(play)
        {
            if (!this.mediaPlayer.isPlaying())
            {
                try
                {
                    synchronized (this)
                    {
                        if (!this.isPrepared)
                        {
                            this.mediaPlayer.prepare();
                        }
                        this.mediaPlayer.start();
                    }
                }
                catch (Exception e)
                {
                    Log.e("Rise", "Could not play music file" + this.assetFile);
                }
            }
        }
        else
        {
            //stop media playing
            stop();
        }
    }

    //method to stop music
    public void stop()
    {
        this.mediaPlayer.stop();
        synchronized (this)
        {
            this.isPrepared = false;
        }
    }

    //method to pause music
    public void pause()
    {
        if (this.mediaPlayer.isPlaying())
        {
            this.mediaPlayer.pause();
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public void setLooping(boolean looping)
    {
        this.mediaPlayer.setLooping(looping);
    }
    public void setVolume(float volume) {
        this.mediaPlayer.setVolume(volume, volume);
    }
    public void setVolume(float leftVolume, float rightVolume) {
        this.mediaPlayer.setVolume(leftVolume, rightVolume);
    }
    public boolean isPlaying()
    {
        return this.mediaPlayer.isPlaying();
    }
    public boolean isLooping()
    {
        return this.mediaPlayer.isLooping();
    }
    public void dispose()
    {
        if (this.mediaPlayer.isPlaying())
        {
            this.mediaPlayer.stop();
        }
        this.mediaPlayer.release();
    }
    public void onCompletion(MediaPlayer mediaPlayer)
    {
        synchronized (this)
        {
            this.isPrepared = false;
        }
    }
}