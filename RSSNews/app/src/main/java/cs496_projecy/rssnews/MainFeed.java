package cs496_projecy.rssnews;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import com.einmalfel.earl.RSSCategory;
import com.einmalfel.earl.RSSEnclosure;
import com.einmalfel.earl.RSSItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainFeed extends AppCompatActivity {
    ArrayList<RSSItem> myItemArray;
    ListView mList;
    MyAdapter mAdapter;
    String link = ("http://rss.nytimes.com/services/xml/rss/nyt/World.xml");

    /** List of RSS source URLs */
    ArrayList<String> mySourceList;
    /** Reference to main service, to access public methods */
    MainService mService;
    boolean mBound = false;

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        // Stuff to do after establishing connection
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MainService.LocalBinder binder = (MainService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            // Load the sources from file
            mySourceList = mService.loadRSS();
            for (String str : mySourceList)
                Log.wtf("ASD", str);
            new RetrieveFeedTask().execute();
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

        mList = (ListView)findViewById(R.id.mainList);

        myItemArray = new ArrayList<RSSItem>();

        // Create an adapter and attach it to the list
        mAdapter = new MyAdapter(this, myItemArray);
        mList.setAdapter(mAdapter);



        // Start the MainService
        Intent intent = new Intent(this, MainFeed.class);
        startService(intent);
    }

    /** Bind to service if we are unbound. */
    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        if (!mBound) {
            Intent intent = new Intent(this, MainService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    /** When stopping, save all sources in case we die. */
    @Override
    protected void onStop() {
        super.onStop();
        saveSources();
        Log.wtf("STOP", "stopping");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /** On destroy, save sources and unbind. */
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

    //This method populates the Options menu within the main activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
// This Method will create the MyUserPreferences Activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.preferences:
            {
                Intent intent = new Intent(this, MyUserPreferences.class);
                //intent.setClassName(this, ".MyUserPreferences");
                startActivity(intent);
                return true;
            }
            case R.id.refresh:
            {
                Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    class RetrieveFeedTask extends AsyncTask<Void, RSSItem, ArrayList<RSSItem>> {

        String RSS;

        @Override
        protected ArrayList<RSSItem> doInBackground(Void... params) {

            InputStream inputStream = null;
            for (String url : mySourceList) {
                if (url == null)
                    continue;
                try {
                    inputStream = new URL(url).openConnection().getInputStream();
                    Feed feed = EarlParser.parseOrThrow(inputStream, 0);
                    Log.i(RSS, "Processing feed: " + feed.getTitle());
                    for (Item item : feed.getItems()) {
                        String title = item.getTitle();
                        String description = item.getDescription();
                        String author = item.getAuthor();
                        URL articleLink = new URL(item.getLink());
                        Date pubDate = item.getPublicationDate();

                    /*
                     * Unfortunately, these lists are REQUIRED to create an RSSItem
                     */
                        List<RSSCategory> categories = new ArrayList<RSSCategory>();
                        RSSCategory category = new RSSCategory("String", "String");
                        categories.add(category);

                        List<RSSEnclosure> enclosures = new ArrayList<RSSEnclosure>();
                        RSSEnclosure enclosure = new RSSEnclosure(articleLink, 2, "String");
                        enclosures.add(enclosure);

                        RSSItem rssContent = new RSSItem(title, articleLink, description, author, categories,
                                null, enclosures, null, pubDate, null, null, null);


                        publishProgress(rssContent);
                        //Log.i(RSS, "Item title: " + (title == null ? "N/A" : title));
                        //Log.d("Hello", "It is in loop");
                    }

                } catch (Exception e) {
                    Log.wtf("PARSE", "Failed to parse " + e.getMessage());
                    e.printStackTrace();
                }
            }
            return myItemArray;
        }

        @Override
        protected void onProgressUpdate(RSSItem... progressItem) {
            Log.d("UPDATE", "Added RSSItem to feed");
            myItemArray.add(progressItem[0]);
        }

        @Override
        protected void onPostExecute (ArrayList<RSSItem> items) {
            mAdapter.sort(itemComparator);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     *  Save all RSS sources from mySourceList into a file on the phone.
     */
    private void saveSources() {
        // Save the RSS source list to a file
        try {
            Log.i("FILEDIR", MainFeed.this.getFilesDir().toString());
            File file = new File(MainFeed.this.getFilesDir(), getString(R.string.filename));
            FileOutputStream out = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(out);
            for (String str : mySourceList) {
                writer.write(str + '\n');
            }
            writer.write("http://rss.nytimes.com/services/xml/rss/nyt/World.xml\n");
            writer.close();
        }
        catch (Exception e) {
            Log.i("FILESAVE", "Failed to write to file: " + e.getMessage());
        }
    }

    public static Comparator<RSSItem> itemComparator
            = new Comparator<RSSItem>() {
        public int compare(RSSItem item1, RSSItem item2) {
            try {
//                Log.i("COMPARE", Long.toString(item2.getPublicationDate().getTime() - item1.getPublicationDate().getTime()));
//                Log.i("COMPARE", "\titem1: " + item1.getTitle());
//                Log.i("COMPARE", "\titem2: " + item2.getTitle());
                if(item2.getPublicationDate().getTime() < item1.getPublicationDate().getTime())
                    return -1;
                else if (item2.getPublicationDate().getTime() == item1.getPublicationDate().getTime())
                    return 0;
                return 1;
            }
            catch (Exception e) {
                Log.wtf("ASD", "FAIL");
            }
            return 0;
        }
    };
}



