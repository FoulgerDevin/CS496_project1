package cs496_projecy.rssnews;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.einmalfel.earl.RSSItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lord Bob Saget on 5/3/2016.
 */
public class MyUserPreferences extends PreferenceActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add a button to the header list.
        if (hasHeaders()) {
            Button button = new Button(this);
            button.setText("Return to Feeds");
            button.setOnClickListener(this);
            setListFooter(button);

        }

    }


    @Override
    public void onClick (View v){
            startActivity(new Intent(this, MainFeed.class));
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return PreferenceFragment.class.getName().equals(fragmentName) ||
                Prefs1Fragment.class.getName().equals(fragmentName) ||
                Prefs2Fragment.class.getName().equals(fragmentName);
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class Prefs1Fragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            if (sharedPref == null) {
                // Make sure default values are applied.  In a real app, you would
                // want this in a shared function that is used to retrieve the
                // SharedPreferences wherever they are needed.
                PreferenceManager.setDefaultValues(getActivity(),
                        R.xml.advanced_preferences, false);
            }

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.fragmented_preferences);
        }
    }

    /**
     * This fragment contains a second-level set of preference that you
     * can get to by tapping an item in the first preferences fragment.
     */
    public static class Prefs2Fragment extends PreferenceFragment {

        private List<RSSItem> rssItemList = new ArrayList<>();

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        // Can retrieve arguments from headers XML.
            Log.i("args","Arguments: "+ getArguments());

        // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.fragmented_preferences_inner);

        }
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            return inflater.inflate(R.layout.feed_list, null);
        }

        private void addRSSLink() {
            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
        }
/*
        @Override
        public void onCreate(Bundle savedInstanceState) {


            Button btnOk = (Button) (this.findViewById(R.id.btnOK));
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateSaveExit();
                }
            });
        }

        private String checkNonEmpty(int id, String info) {
            TextView textView = (TextView) (findViewById(id));
            if (textView != null) {
                CharSequence txt = textView.getText();
                if (txt != null) {
                    int len = txt.length();
                    if (len > 0) {
                        return txt.toString();
                    }
                }
                textView.setHint(info + " is required");
            }
            return null;
        }

        private void validateSaveExit() {
            String author = checkNonEmpty(R.id.txtRSSLink, "author");
            String title = checkNonEmpty(R.id.txtTitle, "title");
            if (author != null && title != null) {
                // return result

            }
*/
    }
/*
    private class MyTask extends AsyncTask<String, Float, ArrayList<RSSItem>> {

        @Override
        protected ArrayList<RSSItem> doInBackground(String... params) {
            try {
                File file = new File(this.getFilesDir(), getString(R.string.filename));
                InputStream in = new FileInputStream(file);
            } catch (Exception e) {

            }
            return null;
        }
    }
*/

}
