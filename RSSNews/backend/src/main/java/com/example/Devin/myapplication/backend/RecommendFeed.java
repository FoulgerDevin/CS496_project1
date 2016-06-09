package com.example.Devin.myapplication.backend;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 * Created by Devin on 6/8/2016.
 *
 */
@SuppressWarnings("serial")
public class RecommendFeed extends HttpServlet {
    private static final Logger _logger = Logger.getLogger(
            RecommendFeed.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {

            try {
                _logger.info("Cron job hs been executed");
                //PUT LOGIC HERE
                DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
                Query feedQuery = new Query("link");
                PreparedQuery query = ds.prepare(feedQuery);//.asList(FetchOptions.Builder.withDefaults());
                ArrayList<String> links = new ArrayList<String>();
                for (Entity result : query.asIterable()) {
                    String link = (String) result.getProperty("name");
                    links.add(link);
                }
            } catch (Exception e) {
                _logger.info("Cron job failed");
            }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}