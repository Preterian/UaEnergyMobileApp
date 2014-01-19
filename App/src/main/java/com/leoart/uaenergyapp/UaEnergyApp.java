package com.leoart.uaenergyapp;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.leoart.uaenergyapp.model.FullPost;
import com.leoart.uaenergyapp.model.Post;
import com.leoart.uaenergyapp.orm.DBHelper;
import com.leoart.uaenergyapp.orm.DataBaseHelper;

import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
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
        installCache();
      //  clearDataBase();
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


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void installCache() {
        Log.d(Tag, "Installing HTTP cache...");
        try {
            File httpCacheDir = new File(UaEnergyApp.CACHE_DIR, "http");
            long httpCacheSize = UaEnergyApp.CACHE_SIZE;
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.e(Tag, "Error while installing cache: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void uninstallCache() {
        Log.d(Tag, "Uninstalling cache...");
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            try {
                cache.delete();
            } catch (IOException e) {
                Log.e(Tag, "Error while uninstalling cache: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }


    public static void clearDataBase(){
        Log.d(Tag, "Clearing dataBase");
        getDatabaseHelper().reset();
        /*DataBaseHelper db = UaEnergyApp.getDatabaseHelper();

        try{
            Dao<Post, Integer> postDao = db.getDao(Post.class);

            DeleteBuilder<Post, Integer> deletePostBuilder = postDao.deleteBuilder();
            deletePostBuilder.delete();


        }catch (SQLException e){
            e.printStackTrace();
        }*/
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

    public static UaEnergyApp getAppInstance(){
        return appInstance;
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

    private static UaEnergyApp appInstance;

    public static final long CACHE_SIZE = 30 * 1024 * 1024; // 50Mb

    public static File CACHE_DIR;

}
