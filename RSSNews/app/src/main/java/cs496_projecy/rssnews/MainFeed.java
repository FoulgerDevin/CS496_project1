package cs496_projecy.rssnews;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileWriter;
import java.util.ArrayList;
import com.einmalfel.earl.RSSItem;

public class MainFeed extends AppCompatActivity {
    ArrayList<RSSItem> myItemArr;
    ListView mList;
    ArrayList<String> myStrArr = new ArrayList<String>();
    ArrayList<String> mySourceList;
    MyAdapter mAdapter;
    MainService mService;
    boolean mBound = false;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MainService.LocalBinder binder = (MainService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            mySourceList = mService.loadRSS();
            mAdapter.addAll(mySourceList);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        // get UI elements
        mList = (ListView)findViewById(R.id.mainList);

        // Create an adapter and attach it to the list
        mAdapter = new MyAdapter(this, myStrArr);
        mList.setAdapter(mAdapter);

        Intent intent = new Intent(this, MainFeed.class);
        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        if (!mBound) {
            Intent intent = new Intent(this, MainService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy () {
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
        }

        Log.wtf("DESTROY", "Destroyinf");

        saveSources();

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSources();
        Log.wtf("STOP", "stopping");
    }

    private void saveSources() {
        // Save the RSS source list to a file
        new Thread( new Runnable() {
            public void run() {
                try {
                    FileWriter writer = new FileWriter(getString(R.string.filename));
                    for (String str : mySourceList) {
                        writer.write(str);
                    }
                    writer.write("http://rss.cnn.com/rss/cnn_topstories.rss");

                    writer.close();
                }
                catch (Exception e) {
                    Log.i("FILESAVE", "Failed to write to file: " + e.getMessage());
                }
            }
        }).start();
    }
}
