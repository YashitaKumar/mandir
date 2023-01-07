package com.example.mandir;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



public class PrefConfig {
    private static final String LIST_KEY="posistions";

    public static void writeListInPref(Context context, List<Integer> positions)
    {

        Gson gson = new Gson();
        String jsonString = gson.toJson(positions);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LIST_KEY,jsonString);
        editor.apply();

    }

    public static void writeFirstTime(Context context,String time)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("First",time);
        editor.apply();

    }
    public static String readFirstTime(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return(preferences.getString("First",""));

    }
    public static List<Integer> readListFromPref(Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = preferences.getString(LIST_KEY,"");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        List<Integer> list = gson.fromJson(jsonString,type);
        return list;


    }
}
