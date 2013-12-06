package com.leoart.uaenergyapp.orm;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Bogdan on 06.12.13.
 */
public class DatabaseManager {
    private DBHelper databaseHelper = null;

    //gets a helper once one is created ensures it doesnt create a new one
    public DBHelper getHelper(Context context)
    {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(context, DBHelper.class);
        }
        return databaseHelper;
    }

    //releases the helper once usages has ended
    public void releaseHelper(DBHelper helper)
    {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
