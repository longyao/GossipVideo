package com.hero.gossipvideo.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hero.gossipvideo.MainApplication;

public class PreUtils {

    private static final SharedPreferences sp;
    private static final SharedPreferences.Editor ed;
    static {
        sp = getDefaultSharedPreferences();
        ed = sp.edit();
    }

    public static SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(
                MainApplication.getInstance().getApplicationContext());
    }

    public static void putInt(String name, int value) {
        ed.putInt(name, value);
        ed.commit();
    }

    //从共享文件获取指定的值，找不到则返回-1
    public static int getInt(String name, int def) {
        return sp.getInt(name, def);
    }

    public static void putString(String name, String value) {
        ed.putString(name, value);
        ed.commit();
    }

    //从共享文件获取指定的值，找不到则返回一个空串
    public static String getString(String name, String def) {
        return sp.getString(name, def);
    }
    
    public static void putBoolean(String name, boolean flag) {
        ed.putBoolean(name, flag);
        ed.commit();
    }
    
    public static boolean getBoolean(String name, boolean def) {
        return sp.getBoolean(name, def);
    }

    public static void putLong(String name, long value) {
        ed.putLong(name, value);
        ed.commit();
    }

    public static long getLong(String name, long def) {
        return sp.getLong(name, def);
    }

    public static void clear() {
        ed.clear();
        ed.commit();
    }

    public static void remove(String name) {
        ed.remove(name);
        ed.commit();
    }
}
