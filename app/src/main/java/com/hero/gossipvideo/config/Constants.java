package com.hero.gossipvideo.config;

import com.ltc.lib.utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/4/25.
 */
public class Constants {

    public static final String[] DEF_UID = {
                "3954997595552586",
                "3943816990545003",
                "3955029983931561"
    };

    public static final Map<String, String> DEF_ACCESS_TOKEN = new HashMap<String, String>();
    static {
        DEF_ACCESS_TOKEN.put("3954997595552586", "07deb6636ac647e4d379836fb0e74d6b");
        DEF_ACCESS_TOKEN.put("3943816990545003", "120cd9ce210fa615f8c7974b0d436c0d");
        DEF_ACCESS_TOKEN.put("3955029983931561", "c4d0ceac183c929c02536dc2720d49ab");
    }

    public static String randomUid() {
        final int length = DEF_UID.length;
        int random = Utils.random(DEF_UID.length);
        if (random >= length) {
            random = length - 1;
        }
        return DEF_UID[random];
    }
}
