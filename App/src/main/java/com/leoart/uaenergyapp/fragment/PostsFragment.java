package com.leoart.uaenergyapp.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.leoart.uaenergyapp.CursorAdapter.PostsCursorAdapter;
import com.leoart.uaenergyapp.R;
import com.leoart.uaenergyapp.UaEnergyApp;
import com.leoart.uaenergyapp.model.Post;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

/**
 * Created by Bogdan on 06.12.13.
 */

public class PostsFragment extends Fragment {

    private static final String TAG = "PostsFragment";
    final String LOG_TAG = "myLogs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.posts, container, false);

        try {
            // UaEnergyApp.getDatabaseHelper().parsePosts();
            postsDao = UaEnergyApp.getDatabaseHelper().getPostsDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        lvMain = (ListView) view.findViewById(R.id.lvMain);


        mAdapter = new PostsCursorAdapter(UaEnergyApp.context, getCursor());


        lvMain.setAdapter(mAdapter);
        lvMain.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView lw, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // Make your calculation stuff here. You have all your
                // needed info from the parameters of this function.

                // Sample calculation to determine if the last
                // item is fully visible.
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    // Last item is fully visible.
                  /*  currentPage++;
                    loadMore(currentPage);

                    refreshAdapter(); */

                    new loadMoreListView().execute();

                    Log.d(LOG_TAG, "Laaaaast One scrolled!!!");

                }
            }
        });

        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long ID) {


                Cursor cursor = mAdapter.getCursor();
                cursor.moveToPosition(position);

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                Post post;
                try {
                    post = postsDao.queryForId(id);
                    if (post != null) {
                        Log.d(TAG, "Some post was choosed = "
                                + post.getLinkText());


                        FragmentManager myFragmentManager = getFragmentManager();

                        FullPostFragment fragment = new FullPostFragment();


                        Bundle bundle = new Bundle();
                        bundle.putString("postUrl", post.getLink());
                        fragment.setArguments(bundle);

                        FragmentTransaction fragmentTransaction = myFragmentManager
                                .beginTransaction();
                        fragmentTransaction.replace(R.id.flContent, fragment);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();


                    }
                } catch (SQLException e) {
                    Log.e(TAG, "SQL Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        });


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

        UaEnergyApp.getDatabaseHelper().parsePostsNews(newsPostsUrl);

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

    public void refreshAdapter(){
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mAdapter != null){
                        mAdapter.setCursor(getCursor());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }


    private void loadMore(int page) {
        String str = newsPostsUrl.concat("/").concat(String.valueOf(page));
        UaEnergyApp.getDatabaseHelper().parsePostsNews(str);

       refreshAdapter();
    }

    private int currentPage = 1;

    private boolean loading = false;
    private boolean loadedAll = false;

    private PostsCursorAdapter mAdapter;
    private Dao<Post, Integer> postsDao;

    private  ListView lvMain = null;

    private String newsPostsUrl = "http://ua-energy.org/post/view/1";

    protected Cursor getCursor() {
        Cursor cursor = UaEnergyApp.getDatabaseHelper().getReadableDatabase().query(Post.TABLE_NAME, new String[]{"id", "link", "link_text", "link_info", "date"}, null, null, null, null, null);
        return cursor;
    }


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
          /*  pDialog = new ProgressDialog(
                    getActivity());
            pDialog.setMessage("Please wait..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        protected Void doInBackground(Void... unused) {


                    // increment current page
                    currentPage += 1;

                    // Next page request
                    String URL = newsPostsUrl.concat("/") + currentPage;


            try {
               UaEnergyApp.getDatabaseHelper(). parseData(URL);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }catch (SQLException e1){
                e1.printStackTrace();
            }


            getActivity().runOnUiThread(new Runnable() {
               @Override
                public void run() {

                    // get listview current position - used to maintain scroll position
                    int currentPosition = lvMain.getFirstVisiblePosition();

                    // Appending new data to menuItems ArrayList
                   // adapter = new ListViewAdapter(
                    //        AndroidListViewWithLoadMoreButtonActivity.this,
                     //       menuItems);
                    refreshAdapter();
                    lvMain.setAdapter(mAdapter);
                    // Setting new scroll position
                  //  lvMain.setSelectionFromTop(currentPosition + 1, 0);

                }
            });

            return (null);
        }

        protected void onPostExecute(Void unused) {
            // closing progress dialog
           // pDialog.dismiss();
        }
    }



}
