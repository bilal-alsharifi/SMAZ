package com.digitalsoft.smartreader.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.ViewConfiguration;

import java.lang.reflect.Field;

/**
 * Created by Bilalo89 on 7/6/13.
 */
public class GeneralHelper
{
    public static void browseURL(Activity context, String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url));
        context.startActivity(intent);
    }
    public static void saveStringInSharedPreferences(Activity context, String key, String value)
    {
        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String loadStringFromSharedPreferences(Activity context, String key)
    {
        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
        return preferences.getString(key, null);
    }
    public static void saveFloatInSharedPreferences(Activity context, String key, float value)
    {
        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }
    public static float loadFloatFromSharedPreferences(Context context, String key)
    {
        SharedPreferences preferences = context.getSharedPreferences("preferences", 0);
        return preferences.getFloat(key, 0);
    }
    public static void doFirstLaunchInitialization(final Activity context)
    {
        String initialized = loadStringFromSharedPreferences(context, "initialized");
        if (initialized == null)
        {
            DataBaseHelper.addAllDomains(context, 1);
            saveFloatInSharedPreferences(context, "strictness", Config.acceptedWeightThreshold);
            saveStringInSharedPreferences(context, "initialized", "true");
        }
    }
    public static int countOccurrences(String str, String subStr)
    {
        int lastIndex = 0;
        int count = 0;
        while(lastIndex != -1)
        {
            lastIndex = str.indexOf(subStr,lastIndex);
            if( lastIndex != -1)
            {
                count ++;
                lastIndex+=subStr.length();
            }
        }
        return  count;
    }
    public static void getOverflowMenu(Activity context)
    {
        try
        {
            ViewConfiguration config = ViewConfiguration.get(context);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null)
            {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
