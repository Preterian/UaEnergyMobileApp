package com.leoart.uaenergyapp.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leoart.uaenergyapp.R;
import com.leoart.uaenergyapp.UaEnergyApp;
import com.leoart.uaenergyapp.model.FullPost;
import com.leoart.uaenergyapp.parser.UaEnergyParser;
import com.leoart.uaenergyapp.utils.Rest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Bogdan on 08.12.13.
 */
public class FullPostFragment extends Fragment {


    final String LOG_TAG = "FullPostFRAGMENT";

    String postLink = null;

    TextView postDateView;
    TextView postAuthorView;
    TextView postTitleView;
    TextView postBodyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.full_post, container, false);


        postAuthorView = (TextView) view.findViewById(R.id.full_post_author);
        postDateView = (TextView) view.findViewById(R.id.full_post_date);
        postTitleView = (TextView) view.findViewById(R.id.full_post_title);
        postBodyView = (TextView) view.findViewById(R.id.full_post_aritcle);

        //String url = null;
        Bundle extras = getArguments();
        //if (extras != null) {
        final String  url = extras.getString("postUrl");
        // }
        postLink = url;

        final ProgressDialog pDialog = new ProgressDialog(
                getActivity());
        pDialog.setMessage("Зачекайте будь ласка...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.show();

        view.setVisibility(View.INVISIBLE);

        Log.d(LOG_TAG, url);

        new Thread(new Runnable() {
            public void run() {

                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
                } catch (IOException ex) {
                    Logger.getLogger(UaEnergyParser.class.getName()).log(Level.SEVERE, null, ex);
                }

                //parsing post-title
                Element postTitle = doc.getElementById("left-page-col").select("h2").first();

                //parsing post-author
                Element postAuthor = doc.getElementById("post-info").select("p").first();

                //parsing post-date
                Element postDate = doc.getElementById("post-info").select("p").last();

                // parsing post-body
                Element content = doc.getElementById("post-body");

                Element postBody = content.select("p").first();


                FullPost fullPost = new FullPost();

                fullPost.setPostAuthor(postAuthor.text());
                fullPost.setPostDate(postDate.text());
                fullPost.setPostTitle(postTitle.text());
                fullPost.setPostBody(postBody.text());

                final String fullPostPostDate = fullPost.getPostDate();
                final String fullPostAuthor = fullPost.getPostAuthor();
                final String fullPostTitle = fullPost.getPostTitle();
                final String fullPostBody = fullPost.getPostBody();


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        pDialog.dismiss();

                        postAuthorView.setText(fullPostAuthor);

                        // if(fullPost.getPostDate() != null && !fullPost.getPostDate().equals(""))
                        postDateView.setText(fullPostPostDate);

                        // if(fullPost.getPostTitle() != null && !fullPost.getPostTitle().equals(""))
                        postTitleView.setText(fullPostTitle);

                        // if(fullPost.getPostBody() != null && !fullPost.getPostBody().equals(""))
                        postBodyView.setText(fullPostBody);

                        view.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).start();


        return view;
    }

    protected static final int BTN_SHARE = 0x1020;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.add(0, BTN_SHARE, 0, R.string.share);
        item.setIcon(R.drawable.ic_action_share);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (Rest.isNetworkOnline()) {
                    //should share current link
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, postLink);
                    startActivity(Intent.createChooser(shareIntent, "Share..."));
                }

                return false;
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "Fragment1 onAttach");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "Fragment1 onCreate");
        setHasOptionsMenu(true);
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

}
