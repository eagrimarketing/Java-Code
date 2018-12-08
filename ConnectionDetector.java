package my.project.agrim;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionDetector {

    public static boolean isNetworkAvailable(final Context context)
    {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo()!= null;
    }


}
