package com.example.dates.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceApp {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public SharedPreferenceApp(Context context) {
        preferences = context.getSharedPreferences("dates", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static com.example.dates.Helper.SharedPreferenceApp getInstance(Context context) {
        return new com.example.dates.Helper.SharedPreferenceApp(context);
    }

    public void saveText(String Key, String Value) {
        editor.putString(Key, Value).apply();
    }

    public String getText(String Key, String defaultValue) {
        String data = preferences.getString(Key, defaultValue);
        return data;
    }

    public void clear() {
        editor.clear().apply();
    }

}