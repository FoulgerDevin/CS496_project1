package cs496_projecy.rssnews;

import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class MainFeed extends AppCompatActivity {
    ArrayList<String> myStrArr;
    ListView mList;
    MyAdapter mAdapter;
    String link = ("http://rss.nytimes.com/services/xml/rss/nyt/World.xml");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        mList = (ListView)findViewById(R.id.mainList);

        myStrArr = new ArrayList<String>();

        // test

        //myStrArr.add("String 1");
        //myStrArr.add("String 2");
        //myStrArr.add("String 3");

        new RetrieveFeedTask().execute();

        // Create an adapter and attach it to the list
        mAdapter = new MyAdapter(this, myStrArr);
        mList.setAdapter(mAdapter);

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
        }

        return super.onOptionsItemSelected(item);
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, ArrayList<String>> {

        String RSS;

        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            InputStream inputStream = null;

            //ArrayList<String> myStrArr = new ArrayList<String>();
            try {
                inputStream = new URL(link).openConnection().getInputStream();
                Feed feed = EarlParser.parseOrThrow(inputStream, 0);
                Log.i(RSS, "Processing feed: " + feed.getTitle());
                for (Item item : feed.getItems()) {
                    String title = item.getTitle();
                    myStrArr.add(title);
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



