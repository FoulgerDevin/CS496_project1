package cs496_projecy.rssnews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainFeed extends AppCompatActivity {
    ArrayList<String> myStrArr;
    ListView mList;
    MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);

        mList = (ListView)findViewById(R.id.mainList);

        // test
        myStrArr = new ArrayList<String>();
        myStrArr.add("String 1");
        myStrArr.add("String 2");
        myStrArr.add("String 3");

        // Create an adapter and attach it to the list
        mAdapter = new MyAdapter(this, myStrArr);
        mList.setAdapter(mAdapter);

        mAdapter.addAll(myStrArr);
    }


}
