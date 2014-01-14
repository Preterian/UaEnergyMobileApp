package com.leoart.uaenergyapp.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by bogdan on 1/14/14.
 */
@DatabaseTable(tableName = "library")
public class Library {
    public static final String TABLE_NAME = "library";

    @DatabaseField(id = true)
    private int id;
    @DatabaseField(canBeNull = false, columnName = "link")
    private String link;
    @DatabaseField(canBeNull = false, columnName = "link_text")
    private String linkText;
    @DatabaseField(canBeNull = false, columnName = "photo")
    private String photo;

    public Library() {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
