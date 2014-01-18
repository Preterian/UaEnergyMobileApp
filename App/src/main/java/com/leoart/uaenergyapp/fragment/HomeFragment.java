package com.leoart.uaenergyapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.j256.ormlite.dao.Dao;
import com.leoart.uaenergyapp.CursorAdapter.PostsCursorAdapter;
import com.leoart.uaenergyapp.R;
import com.leoart.uaenergyapp.UaEnergyApp;
import com.leoart.uaenergyapp.VolleySingleton;
import com.leoart.uaenergyapp.model.Analytic;
import com.leoart.uaenergyapp.parser.UaEnergyParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bogdan on 1/15/14.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    final String LOG_TAG = "HomeFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.posts, container, false);

        home_screen_img =  (NetworkImageView) view.findViewById(R.id.home_screen_img);

        new loadMoreListView().execute();

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "Fragment1 onAttach");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 onCreate");

        // UaEnergyApp.clearDataBase();

        RequestQueue queue = VolleySingleton.getInstance(UaEnergyApp.context).getRequestQueue();
        //or
        mImageLoader = VolleySingleton.getInstance(UaEnergyApp.context).getImageLoader();



    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 onActivityCreated");
    }

    public void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Fragment1 onStart");
    }

    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Fragment1 onResume");
    }

    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "Fragment1 onPause");
    }

    public void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Fragment1 onStop");
    }

    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "Fragment1 onDestroyView");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "Fragment1 onDestroy");
    }

    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "Fragment1 onDetach");
    }

    private String siteUrl = "http://ua-energy.org";

    ProgressDialog pDialog;

    /**
     * Async Task that send a request to url
     * Gets new list view data
     * Appends to list view
     * */
    private class loadMoreListView extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Showing progress dialog before sending http request
            Log.d(LOG_TAG, "Setting progressDialog");
            pDialog = new ProgressDialog(
                    getActivity());
            pDialog.setMessage("Почекайте будь ласка, дані завантажуються..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected Void doInBackground(Void... unused) {



            Document doc = null;
            try {
                doc = Jsoup.connect(siteUrl).get();
            } catch (IOException ex) {
                Logger.getLogger(UaEnergyParser.class.getName()).log(Level.SEVERE, null, ex);
            }


            Element content = doc.getElementById("main-news");

            // parsing posts links
            Elements links = content.getElementsByTag("a");


            final Elements mainPostImgLink = content.getElementsByTag("img");
            Elements blogTitle = content.select("div.blog-title");




            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    home_screen_img.setImageUrl(mainPostImgLink.get(0).attr("src"), mImageLoader);
                }
            });

            return (null);
        }

        protected void onPostExecute(Void unused) {
            // closing progress dialog
            Log.d(LOG_TAG, "Dismissing progress Dialog");
            pDialog.dismiss();
        }
    }


    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    public NetworkImageView home_screen_img;
    public TextView home_screen_title;
    public TextView home_screen_description;


}