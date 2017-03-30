package rise.myapplication.World;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rise.myapplication.UI.Animation;
import rise.myapplication.Engine.Managers.CustomAssetManager;
import rise.myapplication.UI.SimpleControl;
import rise.myapplication.Util.Scale;
import rise.myapplication.World.GameObjects.Enemy;
import rise.myapplication.World.GameObjects.Platform;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.World.GameObjects.Projectile;

/**
 * Created by 40124186 on 17/12/2015.
 */
public abstract class Level {

    protected final float LEVEL_WIDTH = Scale.getX(120);
    protected final float LEVEL_HEIGHT = Scale.getX(120);
    protected float mDifficultyNumber;
    protected final int mScreenWidth;
    protected final int mScreenHeight;
    protected int mNumberOfGroundPlatforms;
    protected int mNumberOfRandomPlatforms;
    protected int mNumberOfEnemies;
    protected final ScreenViewport mScreenViewport;
    protected final LayerViewport mLayerViewport;
    protected SimpleControl mLeft, mRight;
    protected final List<SimpleControl> mControls;
    protected final Rect mBackground;
    protected final Animation mAnimation;
    protected final Random mRandom;
    protected final Player mPlayer;
    protected final List<Platform> mPlatforms;
    protected final List<Enemy> mEnemies;
    protected final List<Projectile> mProjectiles;
    public enum Difficulty{EASY, MEDIUM, HARD}
    protected final Difficulty mDifficulty;
    private Paint paint;

    public Level(GameScreen gameScreen, CustomAssetManager assetManager, Difficulty difficulty)
    {

        mScreenWidth = gameScreen.getGame().getScreenWidth();
        mScreenHeight = gameScreen.getGame().getScreenHeight();
        mScreenViewport = new ScreenViewport();
        mLayerViewport = new LayerViewport(540, 960, 540, 960);
        mBackground = new Rect(0, 0, mScreenWidth, mScreenHeight);
        mControls = new ArrayList<>(2);
        mPlayer = new Player(Scale.getX(50), 200.0f, "Default", gameScreen);
        mPlatforms = new ArrayList<>();
        mEnemies = new ArrayList<>();
        mProjectiles = new ArrayList<>();
        mAnimation = new Animation(assetManager.getBitmap("Explosion"), 12);
        mRandom = new Random();
        mDifficulty = difficulty;
        switch(difficulty)
        {
            case EASY: mDifficultyNumber = 1; mNumberOfRandomPlatforms = 20; mNumberOfEnemies = 4; break;
            case MEDIUM:mDifficultyNumber = 1; mNumberOfRandomPlatforms = 15; mNumberOfEnemies = 6; break;
            case HARD: mDifficultyNumber = 1; mNumberOfRandomPlatforms = 10; mNumberOfEnemies = 8; break;
        }
        createControls(gameScreen);
        createPlatforms(gameScreen);
        createEnemies(gameScreen);

    }
    
    private void createControls(GameScreen gameScreen)
    {
        mLeft = new SimpleControl(Scale.getX(10), Scale.getY(90), 100.0f, 100.0f, "LeftArrow", gameScreen);
        mRight = new SimpleControl(Scale.getX(90), Scale.getY(90), 100.0f, 100.0f, "RightArrow", gameScreen);
        mControls.add(mLeft);
        mControls.add(mRight);
    }
    
    private void createPlatforms(GameScreen gameScreen)
    {
        mNumberOfGroundPlatforms = 2;
        int randomNumber;
        for (int i=0; i<mNumberOfGroundPlatforms; i++)
        {
            mPlatforms.add(new Platform(Scale.getX(100), Scale.getY(Platform.PLATFORM_HEIGHT) * 3, Platform.PlatformType.NORMAL, gameScreen));
        }
        for (int i=0; i<mNumberOfRandomPlatforms; i++)
        {
            randomNumber = mRandom.nextInt(100);
            if (randomNumber<50)
            {
                mPlatforms.add(new Platform((mRandom.nextFloat() * LEVEL_WIDTH), (mRandom.nextFloat() * (LEVEL_HEIGHT - Scale.getY(Platform.PLATFORM_HEIGHT) / 2)), Platform.PlatformType.NORMAL, gameScreen));
            }
            else if (randomNumber>=50 && randomNumber<80)
            {
                mPlatforms.add(new Platform((mRandom.nextFloat() * LEVEL_WIDTH), (mRandom.nextFloat() * (LEVEL_HEIGHT - Scale.getY(Platform.PLATFORM_HEIGHT) / 2)), Platform.PlatformType.MOVING, gameScreen));
            }
            else
            {
                mPlatforms.add(new Platform((mRandom.nextFloat() * LEVEL_WIDTH), (mRandom.nextFloat() * (LEVEL_HEIGHT - Scale.getY(Platform.PLATFORM_HEIGHT) / 2)), Platform.PlatformType.EXPLODING, gameScreen));
            }
        }
    }

    private void createEnemies(GameScreen gameScreen)
    {
        for (int i = 0; i < mNumberOfEnemies; i++) {
            if (i%2==0)
            {
                mEnemies.add(new Enemy((mRandom.nextFloat() * LEVEL_WIDTH), Scale.getY(120), Enemy.enemyType.MOVING, gameScreen));
            }
            else
            {
                mEnemies.add(new Enemy((mRandom.nextFloat() * LEVEL_WIDTH), Scale.getY(120), Enemy.enemyType.STILL, gameScreen));
            }
        }
    }

    protected abstract void update();
    protected abstract void draw();

}
