package rise.myapplication.Util;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;

import java.util.List;

import rise.myapplication.Game.Game;
import rise.myapplication.Game.MainActivity;
import rise.myapplication.UI.DisplayToast;
import rise.myapplication.World.GameObjects.Player;

/**
 * Created by 40124186 on 15/03/2016.
 */
public class Facebook extends AsyncTask<Void, Void, Void>{

    private Intent mIntent;

    public Facebook(Intent intent)
    {
        mIntent = intent;
    }

    //The code to be executed in a background thread.
    @Override
    protected Void doInBackground(Void... params)
    {
        boolean facebookAppFound = false;

        //checking to see if facebook app installed on device
        List<ResolveInfo> matches = MainActivity.getContext().getPackageManager().queryIntentActivities(mIntent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                mIntent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;
                break;
            }
        }

        //if the facebook app is not found, open up facebook in browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?";
            mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
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
