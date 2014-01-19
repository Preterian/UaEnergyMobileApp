package com.leoart.uaenergyapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.leoart.uaenergyapp.fragment.AnalyticFragment;
import com.leoart.uaenergyapp.fragment.AnonsFragment;
import com.leoart.uaenergyapp.fragment.BlogsFragment;
import com.leoart.uaenergyapp.fragment.CommentsFragment;
import com.leoart.uaenergyapp.fragment.CompanyNewsFragment;
import com.leoart.uaenergyapp.fragment.FragmentNavigationDrawer;
import com.leoart.uaenergyapp.fragment.PostsFragment;
import com.leoart.uaenergyapp.fragment.PublicationsFragment;

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




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

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
