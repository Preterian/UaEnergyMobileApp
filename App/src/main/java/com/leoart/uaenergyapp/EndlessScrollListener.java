package com.leoart.uaenergyapp;

import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by bogdan on 1/7/14.
 */
public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private static final String TAG = "EndlessScrollListener";
    private boolean loading = true;

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (!(loading) && (totalItemCount - visibleItemCount) <= (firstVisibleItem)) {
            Log.d(TAG, "Load Next Page!");
            loading = true;
        }
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
