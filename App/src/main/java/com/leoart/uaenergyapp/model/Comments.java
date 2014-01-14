package com.leoart.uaenergyapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bogdan on 1/14/14.
 */
@DatabaseTable(tableName = "comments")
public class Comments {
    public static final String TABLE_NAME = "comments";

    @DatabaseField(id = true)
    private int id;
    @DatabaseField(canBeNull = false, columnName = "link")
    private String link;
    @DatabaseField(canBeNull = false, columnName = "link_text")
    private String linkText;
    @DatabaseField(canBeNull = false, columnName = "link_info")
    private String info;
    @DatabaseField(canBeNull = false, columnName = "date")
    private String date;

    public Comments(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
