package cs496_projecy.rssnews;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import com.einmalfel.earl.RSSItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by David on 5/1/2016.
 * This file should
 */
public class MainService extends Service {
    /** Create custom binder for onBind() */
    private final IBinder mBinder = new LocalBinder();

    /** List of RSS sources loaded from storage file */
    ArrayList<String> sourceList = new ArrayList<String>();

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    /** Returns a reference to this service so the Activity can access its public methods */
    public class LocalBinder extends Binder {
        MainService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MainService.this;
        }
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Serivce bound", Toast.LENGTH_LONG).show();
        return mBinder;
    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    /**
     * Load RSS source URLs from a file stored on the phone.
     * Puts all of the RSS sources into an array, which is returned.
     * Does not download or check the URLs.
     */
    public ArrayList<String> loadRSS() {
        // Open and read the file, line by line
        try {
            File file = new File(this.getFilesDir(), getString(R.string.filename));
            InputStream in = new FileInputStream(file);
            if (in != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;

                // read the file line-by-line, adding each source to the sourceList
                do {
                    line = reader.readLine();

                    if (!sourceList.contains(line) && (line != null)) {
                        sourceList.add(line);
                    }

                    Log.i("FILEREAD", "Read line: " + line);
                }
                while (line != null);
            }
        } catch (Exception e) {
            Log.i("FILELOAD", "Failed to load file: " + e.getMessage());
            Log.i("FILELOAD", "Stack Trace: " + e.getStackTrace());
        }


        return sourceList;
    }

    /**
     * Get list of RSS items from the URLs stored in sourceList.
     * Iterate through sourceList and
     */
    public ArrayList<RSSItem> getRSSItems(int start, int end) {

        return null;
    }

    /**
     * Download RSS data, and parse it into an RSSItem ArrayList.
     */
    private boolean downloadRSS(String url) {
        try {
            InputStream inputStream = new URL(url).openConnection().getInputStream();
            Feed feed = EarlParser.parseOrThrow(inputStream, 0);
            Log.i("ITEM", "Processing feed: " + feed.getTitle());
            for (Item item : feed.getItems()) {
                String title = item.getTitle();
                Log.i("ITEM", "Item title: " + (title == null ? "N/A" : title));
            }
        }
        catch (Exception e) {
            Log.i("ITEM", "Failed to get feed" + e.getMessage());
            return false;
        }
        return true;
    }

}