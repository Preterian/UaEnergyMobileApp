package com.leoart.uaenergyapp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.leoart.uaenergyapp.CursorAdapter.PostsCursorAdapter;
import com.leoart.uaenergyapp.R;
import com.leoart.uaenergyapp.UaEnergyApp;
import com.leoart.uaenergyapp.model.Post;

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

        try{
            UaEnergyApp.getDatabaseHelper().parsePosts();
            postsDao =  UaEnergyApp.getDatabaseHelper().getPostsDao();
        }catch (SQLException e){
            e.printStackTrace();
        }

        ListView lvMain = (ListView) view.findViewById(R.id.lvMain);


        mAdapter = new PostsCursorAdapter(UaEnergyApp.context, getCursor());



        lvMain.setAdapter(mAdapter);

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

    private boolean loading = false;
    private boolean loadedAll = false;

    private PostsCursorAdapter mAdapter;
    private Dao<Post, Integer> postsDao;

    protected Cursor getCursor(){
        Cursor cursor = UaEnergyApp.getDatabaseHelper().getReadableDatabase().query(Post.TABLE_NAME, new String[]{"id", "link", "link_text", "link_info", "date"}, null, null, null, null, null);
        return cursor;
    }

}
