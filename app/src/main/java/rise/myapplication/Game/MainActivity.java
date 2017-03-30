package rise.myapplication.Game;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import rise.myapplication.R;


public class MainActivity extends Activity {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    private Game mGame;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //make sure the window has no title, is fullscreen, and does not lock when inactive
        Window window = getWindow();
        window.requestFeature(window.FEATURE_NO_TITLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_fragment);

        //we have one activity, one fragment and lots of screens

        FragmentManager fm = getFragmentManager();
        mGame = (Game)fm.findFragmentById(R.id.activity_fragment_id);

        mContext = this;

        if(mGame==null){
            mGame=new RiseGame();
            fm.beginTransaction().add(R.id.activity_fragment_id, mGame).commit();
        }
    }



    @Override
    public void onBackPressed()
    {
        if (mGame.getScreenManager().getCurrentScreen().getName().equalsIgnoreCase("MenuScreen"))
        {
            System.exit(0);
        }
        if (!mGame.onBackPressed())
        {
            super.onBackPressed();
        }
    }

    /*@Override
    protected void onStop()
    {
        super.onStop();
        System.exit(0);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static Context getContext(){
        return mContext;
    }




}
