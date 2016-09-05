package com.hero.gossipvideo.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/1/1.
 */
@DatabaseTable(tableName = "t_news")
public class News implements Serializable {

    @DatabaseField(columnName = "id", generatedId = true)
    public long id;

    @DatabaseField(columnName = "unique")
    public String unique;

    @DatabaseField(columnName = "updateTime")
    public long updateTime;

    @DatabaseField(columnName = "title")
    public String title;

    @DatabaseField(columnName = "url", canBeNull = true)
    public String url;

    @DatabaseField(columnName = "picSmall")
    public String picSmall;

    @DatabaseField(columnName = "date")
    public String pubTime;

    @DatabaseField(columnName = "category")
    public String category;

    @DatabaseField(columnName = "authorName")
    public String authorName;

    @DatabaseField(columnName = "source")
    public String source;

    @DatabaseField(columnName = "readNum")
    public String readNum;

    @DatabaseField(columnName = "showType")
    public int showType;

}
