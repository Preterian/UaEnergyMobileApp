package com.leoart.uaenergyapp.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.leoart.uaenergyapp.R;
import com.leoart.uaenergyapp.model.Post;
import com.leoart.uaenergyapp.parser.UaEnergyParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Bogdan on 06.12.13.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something
    // appropriate for your app
    private static final String DATABASE_NAME = "uaenergy.db";
    // any time you make changes to your database objects, you may have to
    // increase the database version
    private static final int DATABASE_VERSION = 13;

    private static String DB_PATH = "/data/data/com.leoart.android.uaenergy/databases/";

    private static final String TAG = "DBHelper";

    // the DAO object we use to access the SimpleData table
    private Dao<Post, Integer> postsDao = null;

    private final Context myContext;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION,
                R.raw.ormlite_config);
        myContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Post.class);
        } catch (SQLException e) {
            Log.e(TAG, "Error while creating DB: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource, Post.class, true);

            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Error while upgrading DB: " + e.getMessage());
            e.printStackTrace();
        }

    }


    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Post, Integer> getPostsDao() throws SQLException {
        if (postsDao == null) {
            postsDao = getDao(Post.class);
        }
        return postsDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        postsDao = null;
    }


    public void parsePostsNews(final String url){
        new Thread(new Runnable() {
            public void run() {
                try {
                   parseData(url);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        }).start();
    }



    public  void parseData(String url) throws SQLException, IOException {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Logger.getLogger(UaEnergyParser.class.getName()).log(Level.SEVERE, null, ex);
        }


        Element content = doc.getElementById("post-list");

        // parsing posts links
        Elements links = content.getElementsByTag("a");
        // parsing posts info
        Elements info = content.select("p.info");
        // parsing posts dates
        Elements dates = content.select("div.postlist-date");

        System.out.println(links.size());
        System.out.println(info.size());

        int len = info.size();

        // parsing news info
        Elements infoS = content.getElementsByTag("p");

        String date = null;
        int j = 0;
        for(int  i = 0; i < len; i++){
            Post post = new Post();
            if(isThisDateValid(infoS.get(i).text(),"dd.MM.yyyy" )){
                date = infoS.get(i).text();
            }
            post.setId(i);
            post.setLink(links.get(i).attr("href"));
            post.setLinkText(links.get(i).text());
            post.setInfo(info.get(i).text());
            post.setDate(date);
            postsDao.createOrUpdate(post);
        }
    }



    public static boolean isThisDateValid(String dateToValidate, String dateFromat){

        if(dateToValidate == null){
            return false;
        }

        if(dateToValidate.length()!=10){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {

            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
            System.out.println(date);

        } catch (ParseException e) {

            e.printStackTrace();
            return false;
        }

        return true;
    }


    public void reset(){
        SQLiteDatabase db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete("post", null, null);
    }

}
