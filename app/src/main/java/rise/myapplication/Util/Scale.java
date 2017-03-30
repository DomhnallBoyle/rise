package rise.myapplication.Util;

import rise.myapplication.World.GameScreen;

/**
 * Created by 40133490 on 14/12/2015.
 */
public class Scale {

    // /////////////////////////////////////////////////////////////////////////
    // Methods to determine scaling of on screen objects. Will scale to device size
    // /////////////////////////////////////////////////////////////////////////

    //double versions
    public static int getX(double percentageWidth){
        return (int)((GameScreen.getScreenWidth()/100.0)*percentageWidth);
    }

    public static int getY(double percentageHeight){
        return (int)((GameScreen.getScreenHeight()/100.0)*percentageHeight);
    }


    //float versions
    public static int getX(float percentageWidth){
        return (int)((GameScreen.getScreenWidth()/100.0)*percentageWidth);
    }

    public static int getY(float percentageHeight){
        return (int)((GameScreen.getScreenHeight()/100.0)*percentageHeight);
    }


    //long versions
    public static int getX(long percentageWidth){
        return (int)((GameScreen.getScreenWidth()/100.0)*percentageWidth);
    }

    public static int getY(long percentageHeight){
        return (int)((GameScreen.getScreenHeight()/100.0)*percentageHeight);
    }
}