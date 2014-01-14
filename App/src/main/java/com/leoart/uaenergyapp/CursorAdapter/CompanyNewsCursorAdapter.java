package com.leoart.uaenergyapp.CursorAdapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.leoart.uaenergyapp.R;

/**
 * Created by bogdan on 1/14/14.
 */
public class CompanyNewsCursorAdapter extends BaseAdapter {

    public CompanyNewsCursorAdapter(Context context, Cursor cursor){
        this.setContext(context);
        this.setCursor(cursor);
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
        public TextView postDate;
        public TextView postTitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;

        //  if(convertView == null){
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.company_news_cell, parent, false);

        vh = new ViewHolder();
        vh.postDate = (TextView) convertView.findViewById(R.id.company_news_date);
        vh.postTitle = (TextView) convertView.findViewById(R.id.company_news_title);
        // }else{
        //     vh = (ViewHolder) convertView.getTag();
        //}

        getCursor().moveToPosition(position);

        String postTitle = getCursor().getString(getCursor().getColumnIndex("link_text"));
        String postDate = getCursor().getString(getCursor().getColumnIndex("date"));

        if( !postTitle.equals("") && !postDate.equals("")){
            vh.postTitle.setText(postTitle);
            vh.postDate.setText(postDate);
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
}