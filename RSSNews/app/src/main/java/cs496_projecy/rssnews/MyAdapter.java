package cs496_projecy.rssnews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.einmalfel.earl.RSSItem;

import java.util.ArrayList;

/**
 * Created by David on 4/29/2016.
 *
 */
public class MyAdapter extends ArrayAdapter<RSSItem> {

    public MyAdapter(Context context, ArrayList<RSSItem> items) {
        super(context, 0, items);
        //Log.d("In the constructor", "of adapter");
        //RSSItem item = items.get(0);
        //Log.i("There should an item", item.getDescription());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the item we are trying to represent
        Log.d("In get view", "in get view");
        RSSItem rssContent = getItem(position);
        String aTitle = rssContent.getTitle();
        String description = rssContent.getDescription();
        String date = rssContent.getPublicationDate().toString();
        final String link = rssContent.getLink();
        Log.d("LINK: ", link);

        // Check if we are recycling an existing view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Now get the data fields for the list_item view:
        TextView title = (TextView)convertView.findViewById(R.id.title);
        TextView summary = (TextView)convertView.findViewById(R.id.summary);
        TextView adate = (TextView)convertView.findViewById(R.id.date);

        // Now add our data into the view
        title.setText(aTitle);
        summary.setText(description);
        adate.setText(date);

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri feedUri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, feedUri);
                v.getContext().startActivity(intent);
            }
        });
        // Finish
        return convertView;
    }
}
