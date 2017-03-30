/**
 * Created by 40133490 on 14/03/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.InstrumentationTestCase;

import com.google.dexmaker.dx.dex.file.ItemType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;

import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Engine.IO.DatabaseHelper;
import rise.myapplication.Engine.IO.FileIO;
import rise.myapplication.Engine.Managers.CustomAssetManager;
import rise.myapplication.Game.Game;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.Game.RiseGame;
import rise.myapplication.Screens.LoadingScreen;
import rise.myapplication.Screens.MainGameScreen;
import rise.myapplication.Screens.MenuScreen;
import rise.myapplication.Util.BoundingBox;
import rise.myapplication.Util.ElapsedTime;
import rise.myapplication.Util.Scale;
import rise.myapplication.Util.Transform2;
import rise.myapplication.World.GameObjects.Item;
import rise.myapplication.World.GameObjects.Platform;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.World.GameScreen;


public class ApplicationTest extends InstrumentationTestCase{
    //setup variables
    Context context;
    Game game;
    Rect startRect;
    Rect finishRect;
    Rect result;
    GameScreen gameScreen;

    //run before tests
    @Before
    public void setUp(){
        game = new RiseGame();
        context = getInstrumentation().getTargetContext();
        startRect = new Rect(50, 150, 150, 50);
        finishRect = new Rect(500, 700, 700, 500);
        System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
        //initialise mock objects
        MockitoAnnotations.initMocks(this);
    }

    /*************************************************************************/
    /*********************************DOMHNALL TESTS**************************/

    @Test
    public void testCustomAssetManager()
    {
        boolean loadedAssets = false;
        FileIO fileIO = new FileIO(context);
        CustomAssetManager customAssetManager = new CustomAssetManager(fileIO);
        if(customAssetManager.testLoadAndAddBitmap("Test1", "img/Rise.png") && customAssetManager.testLoadAndAddMusic("Test2", "music/YuGiOh - Full Version.mp3") && customAssetManager.testLoadAndAddSound("Test3", "sounds/buttonClick.mp3"))
        {
            loadedAssets = true;
        }
        assertEquals(loadedAssets, true);
    }

    @Test
    public void testSharedPreferences()
    {
        boolean match = false;
        SharedPreferences prefs = context.getSharedPreferences("prefsKey", Context.MODE_PRIVATE);
        String example = "This is a shared preferences read write test";
        prefs.edit().putString("StringKey", example).apply();
        if (prefs.getString("StringKey", "").equalsIgnoreCase(example))
        {
            match = true;
        }
        assertEquals(match, true);
    }

    @Test
    public void testInternetConnection()
    {
        boolean hasInternetConnection = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //WifiConfiguration wifiConfiguration = new WifiConfiguration();
        if ((wifiNetwork != null && wifiNetwork.isConnected()) || (mobileNetwork != null && mobileNetwork.isConnected()))
        {
            hasInternetConnection = true;
        }
        assertEquals(hasInternetConnection, true);
    }









    /************************************************************************************/
    /***********************************CONOR TESTS**************************************/

    //test loginFailed() runs when exception thrown
    @Test
    public void testDatabaseHelperException(){
        DatabaseHelper mockDBHelper = Mockito.mock(DatabaseHelper.class);
        try {
            Mockito.doThrow(new SQLException()).when(mockDBHelper).login(Mockito.anyString(), Mockito.anyString());
            mockDBHelper.login("Test", "Test");
        }catch(SQLException e){
            mockDBHelper.loginFailed();
        }
        Mockito.verify(mockDBHelper).loginFailed();
    }

    /*can't test static methods
    @Test
    public void testScaleX(){
        GameScreen gameScreen = Mockito.mock(GameScreen.class);
        Mockito.doReturn(1000).when(gameScreen).getScreenWidth();
        Mockito.doReturn(1000).when(gameScreen).getScreenHeight();
        Scale scale = new Scale();
        int width = scale.getX(100);
        assertEquals(1000,width);
    }
    */

    /*
    I tried to test this method but the getScoreValue wasn't set when i used the getter, tried many methods to get it to work as shown below but to no avail
      and i didn't have time to get to the bottom of it
    @Test
    public void testPlayerCollectibles(){

        //declare mock items
        Player mockPlayer = Mockito.mock(Player.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(mockPlayer.getScoreValue()).thenCallRealMethod();
        //Mockito.doCallRealMethod().when(mockPlayer).setScoreValue(Mockito.any(Integer.class));
        //mockPlayer.setScoreValue(100);
        //int score = mockPlayer.getScoreValue();
        assertEquals(100, 100);
        
        ArrayList<Item> collectibles = new ArrayList<>();
        Item mockItem = Mockito.mock(Item.class);

        //call real methods
        Mockito.doCallRealMethod().when(mockPlayer).checkForCollisions(collectibles);
        Mockito.doCallRealMethod().when(mockPlayer).getNumOfCoins();

        //set default returns
        Mockito.doReturn(false).when(mockItem).IsGone();
        Mockito.doReturn(Item.itemType.COIN).when(mockItem).getType();

        mockPlayer.checkForCollisions(collectibles);
        int coins = mockPlayer.getNumOfCoins();
        assertEquals(1,coins);
    }
    */


    @Test
    public void testRectTransformTimeZero(){
        double startTime = 0;
        double finishTime = 5;
        result = Transform2.step(startRect, finishRect, startTime, finishTime);
        Rect expectedResult = new Rect(50,150,150,50);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
    }

    //test if step works when time is finished
    @Test
    public void testRectTransformTimeFinished(){
        double startTime = 5;
        double finishTime = 5;
        result = Transform2.step(startRect, finishRect, startTime, finishTime);
        Rect expectedResult = new Rect(500,700,700,500);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
    }


    //test if step works moving downwards
    @Test
    public void testRectTransformMidTimeMoveDown(){
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(startRect, finishRect, startTime, finishTime);
        Rect expectedResult = new Rect(230,370,370,230);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
    }

    //test if step works moving upwards
    @Test
    public void testRectTransformMidTimeMoveUp(){
        Rect result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(finishRect, startRect, startTime, finishTime);
        Rect expectedResult = new Rect(320,480,480,320);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
    }

    //test if step works moving right
    @Test
    public void testRectTransformMidTimeMoveRight(){
        startRect = new Rect(0,700,200,500);
        finishRect = new Rect(500,700,700,500);
        Rect result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(startRect, finishRect, startTime, finishTime);
        Rect expectedResult = new Rect(200,700,400,500);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
    }

    //test if step works moving left
    @Test
    public void testRectTransformMidTimeMoveLeft(){
        startRect = new Rect(500,700,700,500);
        finishRect = new Rect(0,700,200,500);
        Rect result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(startRect, finishRect, startTime, finishTime);
        Rect expectedResult = new Rect(300,700,500,500);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
    }

    //test if step works moving directly upwards
    @Test
    public void testRectTransformMidTimeMoveDirectlyUp(){
        startRect = new Rect(500,200,700,0);
        finishRect = new Rect(500,700,700,500);
        Rect result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(startRect, finishRect, startTime, finishTime);
        Rect expectedResult = new Rect(500,400,700,200);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
    }

    //test if step works moving directly downwards
    @Test
    public void testRectTransformMidTimeMoveDirectlyDown(){
        startRect = new Rect(500,700,700,500);
        finishRect = new Rect(500,200,700,0);
        Rect result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(startRect, finishRect, startTime, finishTime);
        Rect expectedResult = new Rect(500,500,700,300);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
    }

    //test if step works with an x value of 0
    @Test
    public void testRectTransformMidTimeMoveWithNegativeX(){
        startRect = new Rect(-100,700,100,500);
        finishRect = new Rect(-600,200,-400,0);
        Rect result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(startRect, finishRect, startTime, finishTime);
        Rect expectedResult = new Rect(-300,500,-100,300);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
    }

    //test if step works with a y value of 0
    @Test
    public void testRectTransformMidTimeMoveWithNegativeY(){
        startRect = new Rect(500,100,700,-100);
        finishRect = new Rect(0,-400,200,-600);
        Rect result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(startRect, finishRect, startTime, finishTime);
        Rect expectedResult = new Rect(300,-100,500,-300);
        assertEquals(expectedResult.left, result.left);
        assertEquals(expectedResult.right, result.right);
        assertEquals(expectedResult.top, result.top);
        assertEquals(expectedResult.bottom, result.bottom);
        assertEquals(expectedResult.width(), result.width());
    }
}