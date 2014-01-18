package com.leoart.uaenergyapp;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.leoart.uaenergyapp.fragment.AnalyticFragment;
import com.leoart.uaenergyapp.fragment.AnonsFragment;
import com.leoart.uaenergyapp.fragment.BlogsFragment;
import com.leoart.uaenergyapp.fragment.CommentsFragment;
import com.leoart.uaenergyapp.fragment.CompanyNewsFragment;
import com.leoart.uaenergyapp.fragment.FragmentNavigationDrawer;
import com.leoart.uaenergyapp.fragment.HomeFragment;
import com.leoart.uaenergyapp.fragment.PostsFragment;
import com.leoart.uaenergyapp.fragment.PublicationsFragment;
import com.leoart.uaenergyapp.model.Anons;
import com.leoart.uaenergyapp.model.Publications;
import com.leoart.uaenergyapp.utils.Rest;

import java.util.List;

/**
 * Created by Bogdan on 07.12.13.
 */
public class MyMainActivity extends FragmentActivity {
    private static final String LOG_TAG = "MyMainActivity";
    private FragmentNavigationDrawer dlDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_activity_main);
        // Find our drawer view
        dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
        // Setup drawer view
        dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer),
                R.layout.drawer_nav_item, R.id.flContent);
        // Add nav items
       // dlDrawer.addNavItem("Головна", "Головна", HomeFragment.class);
        dlDrawer.addNavItem("Новини", "Новини", PostsFragment.class);
        dlDrawer.addNavItem("Анонси", "Анонси", AnonsFragment.class);
        dlDrawer.addNavItem("Коментарі", "Коментарі", CommentsFragment.class);
        dlDrawer.addNavItem("Новини компаній", "Новини компаній", CompanyNewsFragment.class);
        dlDrawer.addNavItem("Аналітика", "Аналітика", AnalyticFragment.class);
        dlDrawer.addNavItem("Публікації", "Публікації", PublicationsFragment.class);
        dlDrawer.addNavItem("Бібліотека", "Бібліотека", PostsFragment.class);
        dlDrawer.addNavItem("Блоги", "Блоги", BlogsFragment.class);
        dlDrawer.addNavItem("Про нас", "Про нас", PostsFragment.class);

        // Select default
        if (savedInstanceState == null) {
            dlDrawer.selectDrawerItem(0);
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        if (dlDrawer.isDrawerOpen()) {
           // menu.findItem(R.id.mi_test).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    protected static final int BTN_REFRESH = 0x1020;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        MenuItem item = menu.add(0, BTN_REFRESH, 0, R.string.refresh);
        item.setIcon(R.drawable.ic_menu_refresh);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (Rest.isNetworkOnline()) {
                    //should refresh current screen
                    if(getVisibleFragment().toString().contains("Posts")){
                        Log.d(LOG_TAG, "Posts should be refreshed...");
                        PostsFragment.refreshScreen();
                        return false;
                    }

                    if(getVisibleFragment().toString().contains("Anons")){
                        Log.d(LOG_TAG, "Anons should be refreshed...");
                        return false;
                    }

                    if(getVisibleFragment().toString().contains("Publications")){
                        Log.d(LOG_TAG, "Publications should be refreshed...");
                        return false;
                    }

                    if(getVisibleFragment().toString().contains("Analytic")){
                        Log.d(LOG_TAG, "Analytics should be refreshed...");
                        return false;
                    }

                    if(getVisibleFragment().toString().contains("Blogs")){
                        Log.d(LOG_TAG, "Blogs should be refreshed...");
                        return false;
                    }

                    if(getVisibleFragment().toString().contains("Comments")){
                        Log.d(LOG_TAG, "Comments should be refreshed...");
                        return false;
                    }

                    if(getVisibleFragment().toString().contains("CompanyNews")){
                        Log.d(LOG_TAG, "CompanyNews should be refreshed...");
                        return false;
                    }
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_refresh) {
            ////////////////


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MyMainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment.isVisible())
                return fragment;
        }
        return null;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dlDrawer.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
    }
}
