package com.leoart.uaenergyapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.leoart.uaenergyapp.model.FullPost;
import com.leoart.uaenergyapp.orm.DBHelper;
import com.leoart.uaenergyapp.orm.DataBaseHelper;

import java.util.concurrent.ExecutorService;

/**
 * Created by Bogdan on 06.12.13.
 */
public class UaEnergyApp extends Application {
    public static String Tag = "UaEnergyApp";
    public static Context context;


    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(Tag, "Started UaEnergyApp");

        context = getApplicationContext();
    }

    @Override
    public void onTerminate(){
        Log.d(Tag, "Terminating App");
        if(databaseHelper != null){
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
        super.onTerminate();

        Log.d(Tag, "App terminated");
    }


    public void clearDB(){

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        Log.d(Tag, "App Configuration Changed");
    }

    public static ExecutorService getThreadExecutor() {
        return threadExecutor;
    }

    public static DataBaseHelper getDatabaseHelper() {
        if(databaseHelper == null){
            databaseHelper = OpenHelperManager.getHelper(UaEnergyApp.context, DataBaseHelper.class);
        }
        return databaseHelper;
    }

    public static void setDatabaseHelper(DataBaseHelper databaseHelper) {
        UaEnergyApp.databaseHelper = databaseHelper;
    }

    private static DataBaseHelper databaseHelper;

    private static ExecutorService threadExecutor;

    private static FullPost fullPost = new FullPost();

    public static FullPost getFullPost() {
        return fullPost;
    }

    public static void setFullPost(FullPost fullPostN) {
        fullPost = fullPostN;
    }

    public String getFullPostUrl() {
        return fullPostUrl;
    }

    public void setFullPostUrl(String fullPostUrl) {
        this.fullPostUrl = fullPostUrl;
    }

    private String fullPostUrl;

}
