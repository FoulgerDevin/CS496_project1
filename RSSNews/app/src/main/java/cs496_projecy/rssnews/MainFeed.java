package cs496_projecy.rssnews;

import android.media.browse.MediaBrowser;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import com.einmalfel.earl.ItunesItem;
import com.einmalfel.earl.MediaItem;
import com.einmalfel.earl.RSSCategory;
import com.einmalfel.earl.RSSEnclosure;
import com.einmalfel.earl.RSSGuid;
import com.einmalfel.earl.RSSItem;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainFeed extends AppCompatActivity {
    ArrayList<RSSItem> myStrArr;
    ListView mList;
    MyAdapter mAdapter;
    String link = ("http://rss.nytimes.com/services/xml/rss/nyt/World.xml");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        mList = (ListView)findViewById(R.id.mainList);

        myStrArr = new ArrayList<RSSItem>();

        // test

        //myStrArr.add("String 1");
        //myStrArr.add("String 2");
        //myStrArr.add("String 3");

        new RetrieveFeedTask().execute();

        // Create an adapter and attach it to the list
        mAdapter = new MyAdapter(this, myStrArr);
        mList.setAdapter(mAdapter);

    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, ArrayList<RSSItem>> {

        String RSS;

        @Override
        protected ArrayList<RSSItem> doInBackground(Void... params) {

            InputStream inputStream = null;

            //ArrayList<String> myStrArr = new ArrayList<String>();
            try {
                inputStream = new URL(link).openConnection().getInputStream();
                Feed feed = EarlParser.parseOrThrow(inputStream, 0);
                Log.i(RSS, "Processing feed: " + feed.getTitle());
                for (Item item : feed.getItems()) {
                    String title = item.getTitle();
                    String description = item.getDescription();
                    String author = item.getAuthor();
                    Date pubDate = item.getPublicationDate();
                    //List<RSSCategory> category = new List<RSSCategory>();

                    //List<RSSEnclosure> enclosure = new List<RSSEnclosure>();

                    RSSItem rssContent = new RSSItem(title, null, description, author, null,
                            null, null, null, pubDate, null, null, null);

                    myStrArr.add(rssContent);
                    Log.i(RSS, "Item title: " + (title == null ? "N/A" : title));
                    Log.d("Hello", "It is in loop");
                }

                return myStrArr;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}



