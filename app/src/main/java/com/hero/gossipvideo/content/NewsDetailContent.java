package com.hero.gossipvideo.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hero.gossipvideo.MainApplication;
import com.ltc.lib.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/3/12.
 */
public class NewsDetailContent {

    private static final String IMG_TAG = "<img src='%1$s'/>";
    private static final String STYLE =
            "<style>" +
                "body{margin:0}" +
                "h1{padding-left:10px;padding-right:10px;}" +
                "img{width:100%;height:auto}" +
                "p{padding-left:10px;padding-right:10px;}" +
                ".paddingLR0{padding-left:0;padding-right:0}" +
            "</style>";
    private static final String JS =
            "<script>\n" +
                "var imgs = document.querySelectorAll('img');\n" +
                "var arr = [], len = imgs.length;\n" +
                "for(var i = 0;i<len;i++){\n" +
                "    arr.push(imgs[i]);\n" +
                "};\n" +
                "arr.forEach(function(img,index){\n" +
                "    img.parentNode.classList.add('paddingLR0');\n" +
                "})\n" +
            "</script>";

    @Expose
    @SerializedName("data")
    public Data data;

    public static class Data {

        @Expose
        @SerializedName("contents")
        public String contents;

        @Expose
        @SerializedName("resources")
        public Resources resources;

        public String getToProcessContents() {

            if (resources == null
                    || Utils.isEmpty(resources.imgs)
                    || Utils.isEmpty(contents)) {
                return contents;
            }

            String result = contents;
            for (int i = 1; i < resources.imgs.size(); i++) {
                final Img img = resources.imgs.get(i);
                String replace = "<!--IMG#" + i + "-->";
                String dist = String.format(IMG_TAG, img.url);
                result = result.replaceAll(replace, dist);
            }

            return (STYLE + result + JS);
        }
    }

    public static class Resources {

        @Expose
        @SerializedName("IMG")
        public List<Img> imgs;
    }

    public static class Img {

        @Expose
        @SerializedName("width")
        public int width;

        @Expose
        @SerializedName("height")
        public int height;

        @Expose
        @SerializedName("src")
        public String url;
    }
}
