package com.leoart.uaenergyapp.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.leoart.uaenergyapp.R;
import com.leoart.uaenergyapp.model.Analytic;
import com.leoart.uaenergyapp.model.Anons;
import com.leoart.uaenergyapp.model.Blogs;
import com.leoart.uaenergyapp.model.Comments;
import com.leoart.uaenergyapp.model.CompanyNews;
import com.leoart.uaenergyapp.model.Library;
import com.leoart.uaenergyapp.model.Post;
import com.leoart.uaenergyapp.model.Publications;
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
    private static final int DATABASE_VERSION = 16;

    private static String DB_PATH = "/data/data/com.leoart.android.uaenergy/databases/";

    private static final String TAG = "DBHelper";

    // the DAO object we use to access the SimpleData table
    private Dao<Post, Integer> postsDao = null;
    private Dao<Comments, Integer> commentsDao = null;
    private Dao<Analytic, Integer> analyticDao = null;
    private Dao<Publications, Integer> publicationsDao = null;
    private Dao<CompanyNews, Integer> companyNewsDao = null;
    private Dao<Anons, Integer> anonsDao = null;
    private Dao<Blogs, Integer> blogsDao = null;

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
            TableUtils.createTable(connectionSource, Analytic.class);
            TableUtils.createTable(connectionSource, Anons.class);
            TableUtils.createTable(connectionSource, Blogs.class);
            TableUtils.createTable(connectionSource, Comments.class);
            TableUtils.createTable(connectionSource, CompanyNews.class);
            TableUtils.createTable(connectionSource, Library.class);
            TableUtils.createTable(connectionSource, Publications.class);
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

            TableUtils.dropTable(connectionSource, Analytic.class, true);
            TableUtils.dropTable(connectionSource, Anons.class, true);
            TableUtils.dropTable(connectionSource, Blogs.class, true);
            TableUtils.dropTable(connectionSource, Comments.class, true);
            TableUtils.dropTable(connectionSource, CompanyNews.class, true);
            TableUtils.dropTable(connectionSource, Library.class, true);
            TableUtils.dropTable(connectionSource, Publications.class, true);

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

    public Dao<Comments, Integer> getCommentsDao() throws SQLException {
        if(commentsDao == null){
            commentsDao = getDao(Comments.class);
        }
        return  commentsDao;
    }

    public Dao<Analytic, Integer> getAnalyticDao() throws SQLException {
        if(analyticDao == null){
            analyticDao = getDao(Analytic.class);
        }
        return analyticDao;
    }

    public Dao<Publications, Integer> getPublicationsDao() throws SQLException {
        if(publicationsDao == null){
            publicationsDao = getDao(Publications.class);
        }
        return publicationsDao;
    }

    public Dao<CompanyNews, Integer> getCompanyNewsDao() throws SQLException {
        if(companyNewsDao == null){
            companyNewsDao = getDao(CompanyNews.class);
        }
        return companyNewsDao;
    }

    public Dao<Anons, Integer> getAnonsDao() throws SQLException {
        if(anonsDao == null){
            anonsDao = getDao(Anons.class);
        }
        return anonsDao;
    }

    public Dao<Blogs, Integer> getBlogsDao() throws SQLException {
        if(blogsDao == null){
            blogsDao = getDao(Blogs.class);
        }
        return blogsDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        postsDao = null;
        commentsDao = null;
    }


    public void parseBlogsSafe(final String url){
        new Thread(new Runnable() {
            public void run() {
                try {
                    parseBlogs(url);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        }).start();
    }


    //TODO add data parsing
    public void parseBlogs(String url) throws SQLException, IOException {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Logger.getLogger(UaEnergyParser.class.getName()).log(Level.SEVERE, null, ex);
        }


        Element content = doc.getElementById("post-list");

        Elements authorLinks = content.select("div.blog-author");
        Elements avatarLinks = content.getElementsByTag("img");
        Elements blogTitle = content.select("div.blog-title");

        int len = avatarLinks.size();

        int j = 0;
        Log.d(TAG, "Adding blogs to DB");
        for(int  i = 0; i < len; i++){
            Blogs post = new Blogs();
            post.setId(i);
            post.setAuthor(authorLinks.get(i).text());
            post.setPhoto(avatarLinks.get(i).attr("src"));
            post.setLinkText(blogTitle.get(i).text());
            post.setLink(blogTitle.get(i).select("a[href]").attr("abs:href"));

            post.setDate("");


            blogsDao.createOrUpdate(post);
        }


    }



    public void parseAnonsSafe(final String url){
        new Thread(new Runnable() {
            public void run() {
                try {
                    parseAnons(url);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        }).start();
    }


    public void parseAnons(String url) throws SQLException, IOException {

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

        int len = info.size();

        // parsing news info
        Elements infoS = content.getElementsByTag("p");


        int j = 0;


        Log.d(TAG, "Adding anons to DB");
        for(int  i = 0; i < len; i++){
            Anons post = new Anons();
            post.setId(i);
            post.setLink(links.get(i).attr("href"));
            post.setLinkText(links.get(i).text());
            post.setInfo(info.get(i).text());
            anonsDao.createOrUpdate(post);
        }


    }



    public void parseCompanyNewsSafe(final String url){
        new Thread(new Runnable() {
            public void run() {
                try {
                    parseCompanyNews(url);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        }).start();
    }


    public void parseCompanyNews(String url) throws SQLException, IOException {

            Document doc = null;
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException ex) {
                Logger.getLogger(UaEnergyParser.class.getName()).log(Level.SEVERE, null, ex);
            }


            Element content = doc.getElementById("post-list");

            // parsing posts links
            Elements links = content.getElementsByTag("a");

            // parsing posts dates
            Elements dates = content.select("div.postlist-date");

            System.out.println(links.size());


            int len = dates.size();

            // parsing news info
            Elements infoS = content.getElementsByTag("p");

            String date = null;
            int j = 0;


                Log.d(TAG, "Adding companyNews to DB");
                for(int  i = 0; i < len; i++){
                    CompanyNews post = new CompanyNews();
                    if(isThisDateValid(infoS.get(i).text(),"dd.MM.yyyy" )){
                        date = infoS.get(i).text();
                    }
                    post.setId(i);
                    post.setLink(links.get(i).attr("href"));
                    post.setLinkText(links.get(i).text());
                    post.setDate(date);
                    companyNewsDao.createOrUpdate(post);
                }


    }


    public void parsePostsNews(final String url, final String category){
        new Thread(new Runnable() {
            public void run() {
                try {
                   parseData(url, category);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }catch (SQLException e1){
                    e1.printStackTrace();
                }
            }
        }).start();
    }



    public  void parseData(String url, String category) throws SQLException, IOException {

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

        if(category.equals("news")){
            Log.d(TAG, "Adding posts to DB");
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
            return;
        }

        if(category.equals("comments")){
            Log.d(TAG, "Adding comments to DB");
            for(int  i = 0; i < len; i++){
                Comments comments = new Comments();
                if(isThisDateValid(infoS.get(i).text(),"dd.MM.yyyy" )){
                    date = infoS.get(i).text();
                }
                comments.setId(i);
                comments.setLink(links.get(i).attr("href"));
                comments.setLinkText(links.get(i).text());
                comments.setInfo(info.get(i).text());
                comments.setDate(date);
                commentsDao.createOrUpdate(comments);
            }
            return;
        }

        if(category.equals("analytic")){
            Log.d(TAG, "Adding analytic data to DB");
            for(int  i = 0; i < len; i++){
                Analytic analytic = new Analytic();
                if(isThisDateValid(infoS.get(i).text(),"dd.MM.yyyy" )){
                    date = infoS.get(i).text();
                }
                analytic.setId(i);
                analytic.setLink(links.get(i).attr("href"));
                analytic.setLinkText(links.get(i).text());
                analytic.setInfo(info.get(i).text());
                analytic.setDate(date);
                analyticDao.createOrUpdate(analytic);
            }
            return;
        }

        if(category.equals("publications")){
            Log.d(TAG, "Adding publications data to DB");
            for(int  i = 0; i < len; i++){
                Publications analytic = new Publications();
                if(isThisDateValid(infoS.get(i).text(),"dd.MM.yyyy" )){
                    date = infoS.get(i).text();
                }
                analytic.setId(i);
                analytic.setLink(links.get(i).attr("href"));
                analytic.setLinkText(links.get(i).text());
                analytic.setInfo(info.get(i).text());
                analytic.setDate(date);
                publicationsDao.createOrUpdate(analytic);
            }
            return;
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
        db.delete("analytic", null, null);
        db.delete("anons", null, null);
        db.delete("blogs", null, null);
        db.delete("comments", null, null);
        db.delete("company_news", null, null);
        db.delete("library", null, null);
        db.delete("publications", null, null);
    }

}
