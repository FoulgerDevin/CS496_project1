package cs496_projecy.rssnews;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;
import android.widget.Toast;

import com.example.devin.myapplication.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Created by Devin on 6/8/2016.
 */
class EndpointsAsyncTask extends AsyncTask<String, Void, String> {
    private static MyApi myApiService = null;
    //private Context context;

    @Override
    protected String doInBackground(String... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://rssnews-1333.appspot.com/_ah/api/");
            // end options for devappserver

            myApiService = builder.build();
        }

        String name = params[0];

        try {
            return myApiService.sayHi(name).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}

