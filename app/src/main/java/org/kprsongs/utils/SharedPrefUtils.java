package org.kprsongs.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtils {
    public static String PREF_NAME = "LyricsPref";
    Context context;

    public static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    }

    public static void putData(Context context, String key, String value) {
        getSharedPref(context).edit().putString(key, value).commit();
    }

    public static void putData(Context context, String key, int value) {
        getSharedPref(context).edit().putInt(key, value).commit();
    }

    public static void putData(Context context, String key, boolean value) {
        getSharedPref(context).edit().putBoolean(key, value).commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getSharedPref(context).getString(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getSharedPref(context).getBoolean(key, defaultValue);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getSharedPref(context).getInt(key, defaultValue);
    }

    public void clearSharePref()
    {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
