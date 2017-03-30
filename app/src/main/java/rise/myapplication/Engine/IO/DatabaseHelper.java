package rise.myapplication.Engine.IO;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.os.AsyncTask;

import java.sql.*;
import java.util.Locale;

import rise.myapplication.Engine.Graphics.IGraphics2D;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.UI.DisplayToast;
import rise.myapplication.Util.Scale;

/**
 * Created by 40124186 on 12/02/2016.
 */
public class DatabaseHelper{

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private final String url;
    private Connection connection;
    private PreparedStatement pStatement;
    private Statement statement;
    private ResultSet players ;
    private ResultSet loginResult;
    private ResultSet challenges;
    private final Paint paint;
    private boolean isConnected;
    private final boolean delay;
    //private double startTime;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public DatabaseHelper(String url)
    {
        this.url = url;
        Typeface plain = Typeface.createFromAsset(MainActivity.getContext().getAssets(), String.format(Locale.US, "Fonts/%s", "Tiki Tropic Bold.ttf"));
        Typeface bold = Typeface.create(plain, Typeface.BOLD);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(70);
        paint.setTypeface(bold);
        paint.setFakeBoldText(true);
        paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
        isConnected = false;
        delay = false;
        connection = null;
    }

    /*public boolean connect()
    {
        startTime = elapsedTime.totalTime;
        Connect connect = new Connect();
        connect.execute();
        while(elapsedTime.totalTime - startTime < 6){
            if(isConnected){
                return true;
            }
        }
        connect.cancel(true);
        return false;
    }*/

    //method to connect to global database
    public boolean connect()
    {
        //if you have an internet connection, try to connect to database
        if (hasInternetConnection())
        {
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(url, "csc2021_gp2", "csc2021");
                isConnected = true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            isConnected = false;
        }
        return isConnected;
    }

    //private method to check if the user has WIFI or 4G
    //Note: it takes a while to connect to Database via QUB_WIFI (like 5 minutes approx)
    private boolean hasInternetConnection()
    {
        boolean hasInternetConnection = false;
        ConnectivityManager cm = (ConnectivityManager) MainActivity.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        //WifiConfiguration wifiConfiguration = new WifiConfiguration();

        if ((wifiNetwork != null && wifiNetwork.isConnected()) || (mobileNetwork != null && mobileNetwork.isConnected()))
        {
            hasInternetConnection = true;
        }
        return hasInternetConnection;
    }

    /*private Security getSecurity(WifiConfiguration config) {
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_PSK)) {
            return Security.SECURITY_PSK;
        }
        if (config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.WPA_EAP) || config.allowedKeyManagement.get(WifiConfiguration.KeyMgmt.IEEE8021X)) {
            return Security.SECURITY_EAP;
        }
        return (config.wepKeys[0] != null) ? Security.SECURITY_WEP : Security.SECURITY_NONE;
    }


    private class Connect extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute()
        {

        }

        //The code to be executed in a background thread.
        @Override
        protected Void doInBackground(Void... params)
        {
            try
            {
                //Get the current thread's token
                synchronized (this)
                {
                    try
                    {
                        Class.forName("com.mysql.jdbc.Driver");
                        connection = DriverManager.getConnection(url, "csc2021_gp2", "csc2021");
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        //Update the progress
        @Override
        protected void onProgressUpdate(Integer... values)
        {

        }

        //after executing the code in the thread
        @Override
        protected void onPostExecute(Void result)
        {
            isConnected = true;
        }
    }*/

    //saves player to global database
    public void savePlayer(String name, int score)
    {
        //creates a PreparedStatement to prevent SQL Injection
        //passes SQL Query into prepare statement to insert player into the Players table
        try
        {
            pStatement = connection.prepareStatement("INSERT INTO Players VALUES (?, ?, ?)");
            pStatement.setString(1, null);
            pStatement.setString(2, name);
            pStatement.setInt(3, score);
            pStatement.executeUpdate();
            pStatement.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    //get the players from the global database
    //uses a statement and an SQL select query to do this
    //query selects the top 10 players from the database
    public void getPlayers()
    {
        try
        {
            statement = connection.createStatement();
            players = statement.executeQuery("SELECT * FROM Players ORDER BY score DESC LIMIT 10");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    //attempt to login
    public boolean login(String name, String password)throws SQLException{
        try
        {
            //create and execute query
            pStatement = connection.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?");
            pStatement.setString(1, name);
            pStatement.setString(2, password);
            loginResult = pStatement.executeQuery();
            //if resultSet is not empty
            if(loginResult.isBeforeFirst()){
                return true;
            }
            pStatement.close();


        }
        catch(SQLException e)
        {
            e.printStackTrace();
            loginFailed();
        }
        return false;
    }

    //run when login failed
    public void loginFailed(){
        new DisplayToast("Login failed").execute();
    }

    //get players challenges
    public ResultSet getChallenges(String username){
        try
        {
            //create and execute query
            pStatement = connection.prepareStatement("SELECT * FROM Challenges WHERE challenged = ?");
            pStatement.setString(1, username);
            challenges = pStatement.executeQuery();
            pStatement.close();

            //if challenges is not empty
            if(challenges.isBeforeFirst()){
                return challenges;
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //draws the top 10 players selected from the database
    public void draw(IGraphics2D graphics2D)
    {
        try
        {
            while(players.next())
            {
                graphics2D.drawText(Integer.toString(players.getRow()), Scale.getX(10), Scale.getY(30 + (5* players.getRow())), paint);
                graphics2D.drawText(players.getString("name"), Scale.getX(40), Scale.getY(30 + (5*players.getRow())), paint);
                graphics2D.drawText(Integer.toString(players.getInt("score")), Scale.getX(77), Scale.getY(30 + (5*players.getRow())), paint);
            }
            players.beforeFirst();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    // /////////////////////////////////////////////////////////////////////////
    // Getters & Setters
    // /////////////////////////////////////////////////////////////////////////

    public boolean getConnected(){return isConnected;}
    public boolean getDelayed(){return delay;}
    public void setConnected(boolean isConnected){this.isConnected = isConnected;}
}