package com.leoart.uaenergyapp.CursorAdapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.leoart.uaenergyapp.R;
import com.leoart.uaenergyapp.VolleySingleton;

/**
 * Created by bogdan on 1/14/14.
 */
public class BlogCursorAdapter extends BaseAdapter {

    public BlogCursorAdapter(Context context, Cursor cursor){
        this.setContext(context);
        this.setCursor(cursor);

        defaultThumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.abc_ab_bottom_solid_dark_holo);

        RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
        //or
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        public NetworkImageView blog_author_img;
        public TextView blogTitle;
        public TextView blogDate;
        public TextView blog_author_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;

        //  if(convertView == null){
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.blog_cell, parent, false);

        vh = new ViewHolder();
        vh.blog_author_name = (TextView) convertView.findViewById(R.id.blog_author_name);
        vh.blogDate = (TextView) convertView.findViewById(R.id.blog_date);
        vh.blogTitle = (TextView) convertView.findViewById(R.id.blog_title);
        vh.blog_author_img = (NetworkImageView) convertView.findViewById(R.id.blog_author_img);
        // }else{
        //     vh = (ViewHolder) convertView.getTag();
        //}

        getCursor().moveToPosition(position);

        String postTitle = getCursor().getString(getCursor().getColumnIndex("link_text"));
        String postAuthorImg = getCursor().getString(getCursor().getColumnIndex("photo"));
        String postAuthorName = getCursor().getString(getCursor().getColumnIndex("author"));
        String postDate = getCursor().getString(getCursor().getColumnIndex("date"));




      //  if(!postTitle.equals("") && !postDate.equals("") && !postAuthorName.equals("") ){
        if(!!postTitle.equals("")){
            vh.blogTitle.setText(postTitle);
        }

        if(!postDate.equals("")){
            vh.blogDate.setText(postDate);
        }

        if(!postAuthorName.equals("")){
            vh.blog_author_name.setText(postAuthorName);
        }

        //}

        if(postAuthorImg != null && !postAuthorImg.equals("")){
            vh.blog_author_img.setImageUrl(postAuthorImg, mImageLoader);
        }

        return convertView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    private Context context;
    private Cursor cursor;
    private Bitmap defaultThumb;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
}
