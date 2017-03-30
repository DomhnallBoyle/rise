package rise.myapplication.Engine.Managers;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;

import rise.myapplication.Engine.Audio.Music;
import rise.myapplication.Engine.Audio.Sound;
import rise.myapplication.Engine.IO.FileIO;

/**
 * Created by DomhnallBoyle on 16/11/2015.
 */
public class CustomAssetManager {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final HashMap<String, Bitmap> bitmaps;
    private final HashMap<String, Music> music;
    private final HashMap<String, Sound> sounds;

    private final SoundPool soundPool;
    private final FileIO fileIO;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public CustomAssetManager(FileIO fileIO)
    {
        this.fileIO = fileIO;
        this.bitmaps = new HashMap<>();
        this.music = new HashMap<>();
        this.sounds = new HashMap<>();
        this.soundPool = new SoundPool(Sound.MAX_CONCURRENT_SOUNDS, AudioManager.STREAM_MUSIC, 0);
    }

    // /////////////////////////////////////////////////////////////////////////
    // Private Methods to load assets
    // /////////////////////////////////////////////////////////////////////////

    private boolean addBitmap(String assetName, Bitmap bitmap)
    {
        if (!this.bitmaps.containsKey(assetName))
        {
            this.bitmaps.put(assetName, bitmap);
            return true;
        }
        return false;
    }

    private boolean addMusic(String assetName, Music music)
    {
        if (!this.music.containsKey(assetName))
        {
            this.music.put(assetName, music);
            return true;
        }
        return false;
    }

    private boolean addSound(String assetName, Sound sound)
    {
        if (!this.sounds.containsKey(assetName))
        {
            this.sounds.put(assetName, sound);
            return true;
        }
        return false;
    }

    private boolean loadAndAddBitmap(String assetName, String bitmapFile)
    {
        try
        {
            Bitmap bitmap = this.fileIO.loadBitmap(bitmapFile, null);
            return addBitmap(assetName, bitmap);
        }
        catch(IOException e)
        {
            Log.e("Rise", "AssetManager.loadAndAddBitmap: Cannot load bitmap file: " + bitmapFile);
            return false;
        }
    }

    private boolean loadAndAddMusic(String assetName, String musicFile)
    {
        try
        {
            Music music = this.fileIO.loadMusic(musicFile);
            return addMusic(assetName, music);
        }
        catch(IOException e)
        {
            Log.e("Rise", "AssetManager.loadAndAddMusic: Cannot load music file: " + musicFile);
            return false;
        }
    }

    private boolean loadAndAddSound(String assetName, String soundFile)
    {
        try
        {
            Sound sound = this.fileIO.loadSound(soundFile, soundPool);
            return addSound(assetName, sound);
        }
        catch(IOException e)
        {
            Log.e("Rise", "AssetManager.loadAndAddSound: Cannot load sound file: " + soundFile);
            return false;
        }
    }

    //method used for unit tests only
    public boolean testLoadAndAddBitmap(String assetName, String bitmapFile)
    {
        try
        {
            Bitmap bitmap = this.fileIO.loadBitmap(bitmapFile, null);
            if (!this.bitmaps.containsKey(assetName))
            {
                this.bitmaps.put(assetName, bitmap);
                return true;
            }
            return false;
        }
        catch(IOException e)
        {
            Log.e("Rise", "AssetManager.loadAndAddBitmap: Cannot load bitmap file: " + bitmapFile);
            return false;
        }
    }

    //method used for unit tests only
    public boolean testLoadAndAddMusic(String assetName, String musicFile)
    {
        try
        {
            Music music = this.fileIO.loadMusic(musicFile);
            return addMusic(assetName, music);
        }
        catch(IOException e)
        {
            Log.e("Rise", "AssetManager.loadAndAddMusic: Cannot load music file: " + musicFile);
            return false;
        }
    }

    //method used for unit tests only
    public boolean testLoadAndAddSound(String assetName, String soundFile)
    {
        try
        {
            Sound sound = this.fileIO.loadSound(soundFile, soundPool);
            return addSound(assetName, sound);
        }
        catch(IOException e)
        {
            Log.e("Rise", "AssetManager.loadAndAddSound: Cannot load sound file: " + soundFile);
            return false;
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Public Methods to Access Assets
    // /////////////////////////////////////////////////////////////////////////

    public void loadBitmaps()
    {
        loadAndAddBitmap("Exit", "img/exitPlaceholder.png");
        loadAndAddBitmap("Logo", "img/Rise.png");
        loadAndAddBitmap("Rise", "img/Rise.png");
        loadAndAddBitmap("NormalPlatform", "img/normalGreen.png");
        loadAndAddBitmap("ExplodingPlatform", "img/red.png");
        loadAndAddBitmap("BlueEyes", "img/bEyes.png");
        loadAndAddBitmap("SoundOn", "img/soundOn.png");
        loadAndAddBitmap("SoundOff", "img/soundOff.png");
        loadAndAddBitmap("LeftArrow", "img/leftArrow.png");
        loadAndAddBitmap("RightArrow", "img/rightArrow.png");
        loadAndAddBitmap("Accelerometer", "img/accelerometer.png");
        loadAndAddBitmap("LeftRightToggle", "img/leftRightToggle.png");
        loadAndAddBitmap("StoreLogo", "img/RiseStore.png");
        loadAndAddBitmap("BuyButton", "img/buyButton.png");
        loadAndAddBitmap("greyBox", "img/greybox.jpg");
        loadAndAddBitmap("thing", "img/bad.png");
        loadAndAddBitmap("Explosion", "img/explosion.png");
        loadAndAddBitmap("MovingPlatform", "img/blue.png");
        loadAndAddBitmap("Bullet", "img/picachu.png");
        loadAndAddBitmap("EnemyBullet", "img/bullet.png");
        loadAndAddBitmap("SpaceBackground", "img/clashBG.jpg");
        loadAndAddBitmap("TimedBackgroundSpace", "img/trialSpaceBackground.jpg");
        loadAndAddBitmap("Moon", "img/moon.png");
        loadAndAddBitmap("MountainBackground", "img/mountain.jpg");
        loadAndAddBitmap("Cloud", "img/cloud.png");
        loadAndAddBitmap("RocketWarrior", "img/rocketWarrior.png");
        loadAndAddBitmap("playg", "img/playlogo.png");
        loadAndAddBitmap("leadg", "img/leaderboardlogo.png");
        loadAndAddBitmap("optiong", "img/optionslogo.png");
        loadAndAddBitmap("exitg", "img/exitlogo.png");
        loadAndAddBitmap("helpg", "img/helplogo.png");
        loadAndAddBitmap("shopg", "img/shoplogo.png");
        loadAndAddBitmap("Blue Eyes", "img/BlueEyesCard.jpg");
        loadAndAddBitmap("Dark Magician", "img/DarkMagicianCard.jpg");
        loadAndAddBitmap("Time Wizard", "img/timeWizardCrd.jpg");
        loadAndAddBitmap("Shield", "img/Optimized-shield.png");
        loadAndAddBitmap("CoinAnimation", "img/coinSpriteSheet.png");
        loadAndAddBitmap("Coin", "img/coin.png");
        loadAndAddBitmap("BlueEyesLeft", "img/bEyesLeft.png");
        loadAndAddBitmap("BlueEyesRight", "img/bEyesRight.png");
        loadAndAddBitmap("TimeWizard", "img/timeWizard.png");
        loadAndAddBitmap("DarkMagicianLeft", "img/darkMagicianLeft.png");
        loadAndAddBitmap("DarkMagicianRight", "img/darkMagicianRight.png");
        loadAndAddBitmap("Rocket", "img/Optimized-rocket.png");
        loadAndAddBitmap("Magnet", "img/magnet.png");
        loadAndAddBitmap("Springs", "img/springs.png");
        loadAndAddBitmap("Multiplier", "img/scoreMultiplier.PNG");
        loadAndAddBitmap("Multiplier0", "img/multiplier0.png");
        loadAndAddBitmap("Multiplier1", "img/multiplier1.png");
        loadAndAddBitmap("Multiplier2", "img/multiplier2.png");
        loadAndAddBitmap("Multiplier3", "img/multiplier3.png");
        loadAndAddBitmap("Multiplier4", "img/multiplier4.png");
        loadAndAddBitmap("Multiplier5", "img/multiplier5.png");
        loadAndAddBitmap("Pause", "img/pause.png");
        loadAndAddBitmap("Global","img/global.png");
        loadAndAddBitmap("Local","img/local.png");
        loadAndAddBitmap("mainmenu","img/mmlogo.png");
        loadAndAddBitmap("FacebookShare", "img/facebookShare.png");
        loadAndAddBitmap("TwitterShare", "img/twitterShare.png");
        loadAndAddBitmap("magnetCoinAnimation", "img/magnet sprite.png");
        loadAndAddBitmap("springAnimation", "img/springAnimation.png");
        loadAndAddBitmap("Easy", "img/easy.png");
        loadAndAddBitmap("Medium", "img/medium.png");
        loadAndAddBitmap("Hard", "img/hard.png");
        loadAndAddBitmap("weestar","img/weestar.png");
        loadAndAddBitmap("lockedPadlock","img/lockedPadlock.png");
        loadAndAddBitmap("unlockedPadlock","img/unlockedPadlock.png");
        loadAndAddBitmap("RetrySpritesheet", "img/retrySpritesheet.png");
        loadAndAddBitmap("RetryButton", "img/retryConnection.png");
        loadAndAddBitmap("ToggleOn", "img/ToggleOn.png");
        loadAndAddBitmap("ToggleOff", "img/ToggleOff.png");
        loadAndAddBitmap("achieveButt","img/achieveButton.png");
        loadAndAddBitmap("gameOver", "img/end.png");
        loadAndAddBitmap("twoPlayer", "img/twoPlayer.png");
        loadAndAddBitmap("Heart", "img/heart.png");
        loadAndAddBitmap("SmokeParticle", "img/Smoke.png");
        loadAndAddBitmap("SparkParticle", "img/spark.png");
    }

    public void loadSounds()
    {
        loadAndAddSound("ButtonClick", "sounds/buttonClick.mp3");
        loadAndAddSound("Explosion", "sounds/explosion.wav");
        loadAndAddSound("CoinCollect", "sounds/coinSound.mp3");
        loadAndAddSound("NormalJump", "sounds/normal_bar.mp3");
        loadAndAddSound("SpringJump", "sounds/spring_bar.mp3");
    }

    public void loadMusic()
    {
        loadAndAddMusic("theme","music/YuGiOh - Full Version.mp3");
    }

    public Bitmap getBitmap(String assetName)
    {
        return this.bitmaps.get(assetName);
    }

    public Music getMusic(String assetName)
    {
        return this.music.get(assetName);
    }

    public Sound getSound(String assetName)
    {
        return this.sounds.get(assetName);
    }

}
