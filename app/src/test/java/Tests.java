/**
 * Created by DomhnallBoyle on 25/02/2016.
 */
import android.graphics.Rect;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.*;

import rise.myapplication.Engine.IO.FileIO;
import rise.myapplication.Engine.Managers.CustomAssetManager;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.Util.BoundingBox;
import rise.myapplication.Util.CollisionDetector;
import rise.myapplication.Util.GraphicsHelper;
import rise.myapplication.Util.Transform2;
import rise.myapplication.Util.Vector2;
import rise.myapplication.World.LayerViewport;
import rise.myapplication.World.GameObjects.Player;
import rise.myapplication.World.LayerViewport;

public class Tests {

    /*********************************DOMHNALL TESTS**************************/

    @Test
    public void connectToDatabaseTest()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/rise", "csc2021_gp2", "csc2021");
            connection.close();
            assert(connection.isClosed());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void setAndGetValueFromDatabase()
    {
        String name = "";
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/rise", "csc2021_gp2", "csc2021");
            Statement statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS Test (id int primary key AUTO_INCREMENT, name varchar(20));");

            statement.executeUpdate("INSERT INTO Test VALUES (null, 'Domhnall Boyle')");

            ResultSet rset = statement.executeQuery("SELECT * FROM Test WHERE id = 1;");
            while(rset.next())
            {
                name = rset.getString("name");
            }
            assert(name.equalsIgnoreCase("Domhnall Boyle"));
            rset.close();
            statement.close();
            connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testForBottomCollision()
    {
        BoundingBox objectOne = new BoundingBox(100, 100, 50, 50);
        BoundingBox objectTwo = new BoundingBox(180, 180, 50, 50);
        boolean hasCollided = false;
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(objectOne, objectTwo);
        if (collisionType == CollisionDetector.CollisionType.Bottom)
        {
            hasCollided = true;
        }
        assert(hasCollided);
    }

    @Test
    public void testForRightCollision()
    {
        BoundingBox objectOne = new BoundingBox(100, 100, 50, 50);
        BoundingBox objectTwo = new BoundingBox(150, 100, 50, 50);
        boolean hasCollided = false;
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(objectOne, objectTwo);
        if (collisionType == CollisionDetector.CollisionType.Right)
        {
            hasCollided = true;
        }
        assert(hasCollided);
    }

    @Test
    public void testForLeftCollision()
    {
        BoundingBox objectOne = new BoundingBox(100, 100, 50, 50);
        BoundingBox objectTwo = new BoundingBox(50, 100, 50, 50);
        boolean hasCollided = false;
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(objectOne, objectTwo);
        if (collisionType == CollisionDetector.CollisionType.Left)
        {
            hasCollided = true;
        }
        assert(hasCollided);
    }

    @Test
    public void testForTopCollision()
    {
        BoundingBox objectOne = new BoundingBox(100, 100, 50, 50);
        BoundingBox objectTwo = new BoundingBox(100, 50, 50, 50);
        boolean hasCollided = false;
        CollisionDetector.CollisionType collisionType = CollisionDetector.determineAndResolveCollision(objectOne, objectTwo);
        if (collisionType == CollisionDetector.CollisionType.Top)
        {
            hasCollided = true;
        }
        assert(hasCollided);
    }

    @Test
    public void testIntersection()
    {
        BoundingBox objectOne = new BoundingBox(100, 100, 50, 50);
        BoundingBox objectTwo = new BoundingBox(100, 100, 50, 50);
        assert(objectOne.intersects(objectTwo));
    }

    @Test
    public void testAdd()
    {
        Vector2 vectorOne = new Vector2(50, 50);
        Vector2 expectedVector = new Vector2(70, 70);
        vectorOne.add(20, 20);
        assert(vectorOne.x == expectedVector.x && vectorOne.y == expectedVector.y);
    }

    @Test
    public void testDivide() {
        Vector2 vectorOne = new Vector2(50, 50);
        Vector2 expectedVector = new Vector2(25, 25);
        vectorOne.divide(2);
        assert(vectorOne.x == expectedVector.x && vectorOne.y == expectedVector.y);
    }

    @Test
    public void testVectorNormalise()
    {
        Vector2 vectorOne = new Vector2(10, 10);
        vectorOne.normalise();
        Assert.assertEquals(vectorOne.x * vectorOne.x, 0.5f, 0.01f); //use of a delta value
    }

    @Test
    public void testVectorMultiply()
    {
        Vector2 vectorOne = new Vector2(50, 50);
        Vector2 expectedVector = new Vector2(100, 100);
        vectorOne.multiply(2);
        assert(vectorOne.x == expectedVector.x && vectorOne.y == expectedVector.y);
    }

    @Test
    public void testVectorLength()
    {
        Vector2 vectorOne = new Vector2(10, 10);
        assert(vectorOne.length()*vectorOne.length() == vectorOne.lengthSquared());
    }

    @Test
    public void testGraphicsHelper()
    {
        BoundingBox boundingBox = new BoundingBox(-100, -100, 25, 25);
        LayerViewport layerViewport = new LayerViewport(0, 0, 500, 500);
        assert(GraphicsHelper.checkIfVisible(boundingBox, layerViewport));
    }















    /**********************************************************************************************************/
    /************************************************CONOR TESTS***********************************************/


    BoundingBox start,finish;
    Rect startRect, finishRect;
    //initialise generic bounding boxes and rects for testing
    @Before
    public void initialiseBoundingBox(){
        start = new BoundingBox(100,100,50,50);
        finish = new BoundingBox(600,600,100,100);
        startRect = new Rect(50, 150, 150, 50);
        finishRect = new Rect(500, 700, 700, 500);
    }

    /*BOUNDING BOX TESTS*/

    //test if step works when time is 0
    @Test
    public void BoundingBoxTransformTimeZero(){
        BoundingBox result;
        double startTime = 0;
        double finishTime = 5;
        result = Transform2.step(start, finish, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(100,100,50,50);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }

    //test if step works when time is finished
    @Test
    public void BoundingBoxTransformTimeFinished(){
        BoundingBox result;
        double startTime = 5;
        double finishTime = 5;
        result = Transform2.step(start, finish, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(600,600,100,100);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }


    //test if step works moving downwards
    @Test
    public void BoundingBoxTransformMidTimeMoveDown(){
        BoundingBox result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(start, finish, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(300,300,70,70);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }

    //test if step works moving upwards
    @Test
    public void BoundingBoxTransformMidTimeMoveUp(){
        BoundingBox result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(finish, start, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(400,400,80,80);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }

    //test if step works moving right
    @Test
    public void BoundingBoxTransformMidTimeMoveRight(){
        start = new BoundingBox(100,600,100,100);
        finish = new BoundingBox(600,600,100,100);

        BoundingBox result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(start, finish, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(300,600,100,100);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }

    //test if step works moving left
    @Test
    public void BoundingBoxTransformMidTimeMoveLeft(){
        start = new BoundingBox(600,600,100,100);
        finish = new BoundingBox(100,600,100,100);
        BoundingBox result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(start, finish, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(400,600,100,100);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }

    //test if step works moving directly upwards
    @Test
    public void BoundingBoxTransformMidTimeMoveDirectlyUp(){
        start = new BoundingBox(600,100,100,100);
        finish = new BoundingBox(600,600,100,100);
        BoundingBox result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(start, finish, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(600,300,100,100);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }

    //test if step works moving directly downwards
    @Test
    public void BoundingBoxTransformMidTimeMoveDirectlyDown(){
        start = new BoundingBox(600,600,100,100);
        finish = new BoundingBox(600,100,100,100);
        BoundingBox result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(start, finish, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(600,400,100,100);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }

    //test if step works with an x value of 0
    @Test
    public void BoundingBoxTransformMidTimeMoveWithXZero(){
        start = new BoundingBox(0,600,100,100);
        finish = new BoundingBox(0,100,100,100);
        BoundingBox result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(start, finish, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(0,400,100,100);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }

    //test if step works with a y value of 0
    @Test
    public void BoundingBoxTransformMidTimeMoveWithYZero(){
        start = new BoundingBox(600,0,100,100);
        finish = new BoundingBox(100,0,100,100);
        BoundingBox result;
        double startTime = 2;
        double finishTime = 5;
        result = Transform2.step(start, finish, startTime, finishTime);
        BoundingBox expectedResult = new BoundingBox(400,0,100,100);
        assert(expectedResult.getLeft() == result.getLeft() && expectedResult.getRight() == result.getRight() && expectedResult.getBottom() == result.getBottom() && expectedResult.getTop() == result.getTop() && expectedResult.getHalfWidth() == result.getHalfWidth() && expectedResult.getHalfHeight() == result.getHalfHeight());
    }

    //test if bounding box is inside layerViewport
    @Test
    public void testBoxIntersectsLayerViewport(){
        //box not intersecting with viewport
        BoundingBox box = new BoundingBox(-400,-400,100,100);
        LayerViewport viewport = new LayerViewport(270,960,270,960);
        assert(!viewport.intersects(box));

        //box intersecting viewport
        box = new BoundingBox(400,400,100,100);
        assert(viewport.intersects(box));

        //box just touching viewport(but not intersecting)
        box = new BoundingBox(-100,-100,100,100);
        assert(!viewport.intersects(box));
    }

    //test if a point falls within the layerviewport
    @Test
    public void testLayerViewportContainsPoint(){
        LayerViewport viewport = new LayerViewport(270,960,270,960);
        //point contained in viewport
        assert (viewport.contains(270,960));

        //point not contained in viewport
        assert (viewport.contains(-270,-960));
    }

}