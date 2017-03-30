package rise.myapplication.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import rise.myapplication.Game.MainActivity;
import rise.myapplication.R;

/**
 * Created by DomhnallBoyle on 12/03/2016.
 */
public class DialogBox extends AsyncTask<Void, Void, Void> {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private String message;
    private boolean gone = false;

    public DialogBox(String message)
    {
        this.message = message;
        builder = new AlertDialog.Builder(MainActivity.getContext());
    }

    public boolean go()
    {
        execute();
        return true;
    }

    //this method is performed in the background of the AsyncTask
    protected Void doInBackground(Void... param) {

        return null;
    }

    //this method executes after the doInBackground method
    protected void onPostExecute(Void param) {
        //Toast toast = Toast.makeText(MainActivity.getContext(), displayText, Toast.LENGTH_SHORT);
        //toast.show();

        //Get the current thread's token
            synchronized (this)
            {
                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
    }

    public boolean getIsGone()
    {
        return gone;
    }
}
