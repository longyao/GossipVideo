package com.hero.gossipvideo.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hero.gossipvideo.db.model.News;
import com.ltc.lib.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longtc on 15-3-18.
 */
public class NewsContent implements Serializable {

    @Expose
    @SerializedName("data")
    public NewsData data;

    public static class NewsData implements Serializable {

        @Expose
        @SerializedName("page")
        public int page;

        @Expose
        @SerializedName("list")
        public List<Article> articles;
    }

    public static class Article implements Serializable {

        @Expose
        @SerializedName("title")
        public String title;

        @Expose
        @SerializedName("id")
        public String id;

        @Expose
        @SerializedName("readnum")
        public String readNum;

        @Expose
        @SerializedName("pic")
        public String pic;

        @Expose
        @SerializedName("cat")
        public Category category;

        @Expose
        @SerializedName("author")
        public Author author;
    }

    public static class Category implements Serializable {

        @Expose
        @SerializedName("name")
        String name;
    }

    public static class Author implements Serializable {

        @Expose
        @SerializedName("name")
        String name;
    }

    public List<News> transToNews() {

        if (Utils.isEmpty(data.articles)) {
            return null;
        }

        List<News> result = new ArrayList<News>();
        for (Article c : data.articles) {

            if (Utils.isEmpty(c.pic)) {
                continue;
            }

            String cat = c.category == null ? "" : c.category.name;
            if ("视频".equals(cat) || "gif".equalsIgnoreCase(cat)) {
                continue;
            }

            News news = new News();
            news.unique = c.id;
            news.title = c.title;
            news.readNum = c.readNum;
            news.picSmall = c.pic;
            news.category = c.category == null ? "" : c.category.name;
            news.authorName = c.author == null ? "" : c.author.name;
            result.add(news);
        }

        return result;
    }
}
