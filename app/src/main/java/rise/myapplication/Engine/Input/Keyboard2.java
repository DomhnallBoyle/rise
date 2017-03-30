package rise.myapplication.Engine.Input;

import android.content.Context;
import android.os.AsyncTask;
import android.view.inputmethod.InputMethodManager;

import rise.myapplication.Game.MainActivity;

/**
 * Created by DomhnallBoyle on 12/03/2016.
 */
public class Keyboard2 extends AsyncTask<Void, Void, Void> {

    public Keyboard2()
    {

    }

    //this method is performed in the background of the AsyncTask
    protected Void doInBackground(Void... param) {
        //Do some work
        return null;
    }

    //this method executes after the doInBackground method
    protected void onPostExecute(Void param) {

        InputMethodManager imm = (InputMethodManager) MainActivity.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);

        }

    }
}
