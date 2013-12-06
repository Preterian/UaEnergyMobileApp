package com.leoart.uaenergyapp.orm;

import android.content.Context;


import com.j256.ormlite.dao.Dao;
import com.leoart.uaenergyapp.model.Post;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Bogdan on 06.12.13.
 */
public class PostsRepository {
    private DBHelper db;
    Dao<Post, Integer> postsDao;

    public PostsRepository(Context ctx)
    {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            db = dbManager.getHelper(ctx);
            postsDao = db.getPostsDao();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
    }

    public int create(Post comment)
    {
        try {
            return postsDao.create(comment);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int update(Post comment)
    {
        try {
            return postsDao.update(comment);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }
    public int delete(Post comment)
    {
        try {
            return postsDao.delete(comment);
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return 0;
    }

    public List getAll()
    {
        try {
            return postsDao.queryForAll();
        } catch (SQLException e) {
            // TODO: Exception Handling
            e.printStackTrace();
        }
        return null;
    }
}
