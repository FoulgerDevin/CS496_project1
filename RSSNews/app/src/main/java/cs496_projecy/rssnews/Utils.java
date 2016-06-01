package cs496_projecy.rssnews;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;
import android.content.ContextWrapper;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Lord Bob Saget on 5/22/2016.
 */
public class Utils {

    public static String checkURL(String s) {
        URL url;
        try {
            url = new URL(s);
        } catch (MalformedURLException e) {
            return null;
        }
        return url.getHost();
    }

    public static boolean isNetwork (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    public static boolean isWifiNetwork (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo.getType() == connectivityManager.TYPE_WIFI);
    }

}
