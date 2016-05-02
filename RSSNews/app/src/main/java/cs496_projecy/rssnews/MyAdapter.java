package cs496_projecy.rssnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by David on 4/29/2016.
 */
public class MyAdapter extends ArrayAdapter<String> {
    int count = 0;

    public MyAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the item we are trying to represent
        String item = getItem(position);

        // Check if we are recycling an existing view
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Now get the data fields for the list_item view:
        TextView title = (TextView)convertView.findViewById(R.id.title);
        TextView summary = (TextView)convertView.findViewById(R.id.summary);
        ImageView image = (ImageView)convertView.findViewById(R.id.image);

        // Now add our data into the view
        title.setText("This is title #" + count);
        summary.setText(item);
        image.setImageResource(R.mipmap.ic_launcher);
        count++;

        // Finish
        return convertView;
    }


}
