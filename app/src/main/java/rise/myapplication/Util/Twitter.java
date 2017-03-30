package rise.myapplication.Util;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.List;

import rise.myapplication.Game.Game;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.World.GameObjects.Player;

/**
 * Created by 40124186 on 15/03/2016.
 */
public class Twitter extends AsyncTask<Void, Void, Void>{

    private Game mGame;
    private Player mPlayer;
    private Intent mIntent;

    public Twitter(Game game, Player player, Intent intent)
    {
        mGame = game;
        mPlayer = player;
        mIntent = intent;
    }

    //The code to be executed in a background thread.
    @Override
    protected Void doInBackground(Void... params)
    {
        boolean twitterAppFound = false;

        //checking to see if twitter app is installed on device
        List<ResolveInfo> matches = MainActivity.getContext().getPackageManager().queryIntentActivities(mIntent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                mIntent.setPackage(info.activityInfo.packageName);
                twitterAppFound = true;
                break;
            }
        }

        //if the app is not found, open up twitter in browser
        if (!twitterAppFound) {
            if (!mGame.getTwoPlayerToggle())
            {
                mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=I just scored " + mPlayer.getScoreValue() + " in Rise. Can you beat me?"));
            }
            else
            {
                mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?"));
            }
        }

        //start activity
        MainActivity.getContext().startActivity(mIntent);

        return null;
    }

    //after executing the code in the thread
    @Override
    protected void onPostExecute(Void result)
    {

    }

}
