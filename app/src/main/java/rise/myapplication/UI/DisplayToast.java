package rise.myapplication.UI;

        import android.os.AsyncTask;
        import android.widget.Toast;

        import rise.myapplication.Game.MainActivity;

/**
 * Created by 40133490 on 01/03/2016.
 */
public class DisplayToast extends AsyncTask<Void, Void, Void> {

    // /////////////////////////////////////////////////////////////////////////
    // Properties
    // /////////////////////////////////////////////////////////////////////////

    //text to be displayed in toast
    private final String displayText;

    // /////////////////////////////////////////////////////////////////////////
    // Constructor
    // /////////////////////////////////////////////////////////////////////////

    public DisplayToast(String displayText){
        super();
        this.displayText = displayText;
    }

    //this method is performed in the background of the AsyncTask
    protected Void doInBackground(Void... param) {
        //Do some work
        return null;
    }

    //this method executes after the doInBackground method
    protected void onPostExecute(Void param) {
        Toast toast = Toast.makeText(MainActivity.getContext(), displayText, Toast.LENGTH_SHORT);
        toast.show();
    }
}