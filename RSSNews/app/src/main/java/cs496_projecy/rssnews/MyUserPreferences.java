package cs496_projecy.rssnews;

import android.app.FragmentManager;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lord Bob Saget on 5/3/2016.
 */
public class MyUserPreferences extends PreferenceActivity implements View.OnClickListener {

    static MyUserPreferences obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj = this;
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
        this.finish();
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
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Can retrieve arguments from headers XML.
            Log.i("args", "Arguments: " + getArguments());

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.fragmented_preferences_inner);

        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            View rootView = inflater.inflate(R.layout.feed_list, container, false);
            Button btnOk = (Button) (rootView.findViewById(R.id.btnOK));
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateSaveExit(obj);
                }
            });
            return rootView;
        }

        private String checkNonEmpty(int id, String info) {
            TextView textView = (TextView) (getActivity().findViewById(id));
            if (textView != null) {
                String txt = Utils.checkURL(textView.getText().toString());
                if (txt != null) {
                    return txt;
                }
                textView.setHint(info + " is required");
            }
            return null;
        }

        protected void validateSaveExit(MyUserPreferences obj) {
            String link = checkNonEmpty(R.id.txtRSSLink, "Proper Address");
            if (link != null) {
                try {
                    File file = new File(obj.getFilesDir(), getString(R.string.filename));
                    FileOutputStream out = new FileOutputStream(file);
                    OutputStreamWriter outWriter = new OutputStreamWriter(out);
                    outWriter.write(link + "\n");
                    outWriter.close();
                    out.close();
                } catch (Exception e) {
                    Toast.makeText(obj.getBaseContext(),
                            "Saving Link Failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            getActivity().finish();

        }

    }

}
