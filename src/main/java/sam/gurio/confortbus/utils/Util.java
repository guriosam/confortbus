package sam.gurio.confortbus.utils;

import android.app.Activity;
import android.widget.Toast;

import sam.gurio.confortbus.R;

public class Util {

    public static boolean checkInput(Activity act, String username) {

        //if (username.contains(".")) {
          //  Toast.makeText(act, act.getString(R.string.username_error), Toast.LENGTH_SHORT).show();
         //   return false;
       // }
        if (username.contains("#")) {
            Toast.makeText(act, act.getString(R.string.username_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.contains("$")) {
            Toast.makeText(act, act.getString(R.string.username_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.contains("[")) {
            Toast.makeText(act, act.getString(R.string.username_error), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.contains("]")) {
            Toast.makeText(act, act.getString(R.string.username_error), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
