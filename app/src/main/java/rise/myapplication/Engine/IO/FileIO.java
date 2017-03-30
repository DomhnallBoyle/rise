package rise.myapplication.Engine.IO;

import android.content.Context;
import android.content.res.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import rise.myapplication.Engine.Audio.Music;
import rise.myapplication.Engine.Audio.Sound;
import rise.myapplication.R;

/**
 * Created by DomhnallBoyle on 16/11/2015.
 */
public class FileIO {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final Context context;
    private final AssetManager assetManager;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public FileIO(Context context)
    {
        this.context = context;
        this.assetManager = context.getAssets();
    }

    //method to load bitmap images
    public Bitmap loadBitmap(String fileName, Bitmap.Config format) throws IOException
    {
        Options options = new Options();
        options.inPreferredConfig = format;
        InputStream in = null;
        Bitmap bitmap = null;
        try
        {
            in = this.assetManager.open(fileName);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
            {
                String message = this.context.getApplicationContext().getResources().getString(R.string.WARNING_TAG) + "Could not load Bimap: " + fileName;
                throw new IOException(message);
            }
        }
        catch(IOException e)
        {
            String message = this.context.getApplicationContext().getResources().getString(R.string.WARNING_TAG) + "Could not load Bitmap: " + fileName;
            throw new IOException(message);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch(IOException e)
                {
                    Log.e("Error", e.getMessage());
                }
            }
        }
        return bitmap;
    }



    //method to load music
    public Music loadMusic(String fileName) throws IOException
    {
        try
        {
            AssetFileDescriptor assetDescriptor = assetManager.openFd(fileName);
            return new Music(assetDescriptor);
        }
        catch(IOException e)
        {
            String message = this.context.getApplicationContext().getResources().getString(R.string.WARNING_TAG) + "Could not load music file: " + fileName;
            throw new IOException(message);
        }
    }

    //method to load sound
    public Sound loadSound(String fileName, SoundPool soundPool) throws IOException
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = this.assetManager.openFd(fileName);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            return new Sound(soundPool, soundId);
        }
        catch(IOException e)
        {
            String message = this.context.getApplicationContext().getResources().getString(R.string.WARNING_TAG) + "Could not load sound file: " + fileName;
            throw new IOException(message);
        }
    }
}
