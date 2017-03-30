package rise.myapplication.Engine.Audio;

import android.media.SoundPool;

/**
 * Created by DomhnallBoyle on 16/11/2015.
 */
public class    Sound {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    public static final int MAX_CONCURRENT_SOUNDS = 20;
    private static boolean play = true;
    private final int soundId;
    private final SoundPool soundPool;
    private float volume;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public Sound(SoundPool soundPool, int soundId)
    {
        this.soundId = soundId;
        this.soundPool = soundPool;
        this.volume = 20.0f;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public static void setPlay(boolean play){Sound.play = play;}
    public static boolean getPlay(){return play;}
    public void play()
    {
        if(play)
            this.soundPool.play(this.soundId, this.volume, this.volume, 0, 0, 1);
    }
    public void play(float volume)
    {
        if(play)
            this.soundPool.play(this.soundId, volume, volume, 0, 0, 1);
    }
    public void play(float leftVolume, float rightVolume)
    {
        if(play)
            this.soundPool.play(this.soundId, leftVolume, rightVolume, 0, 0, 1);
    }
    public void setVolume(float volume)
    {
        this.volume = volume;
    }
    public void dispose()
    {
        this.soundPool.unload(this.soundId);
    }

}
