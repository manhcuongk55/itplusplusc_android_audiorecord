package cafe.adriel.androidaudiorecorder.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;

import cafe.adriel.androidaudiorecorder.utils.TextUtil;


/**
 * Created by duckien on 20/02/2017.
 */

public class StorageManager {
    private static SharedPreferences getSharedPreferences(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static boolean setValue(Context context, String key, String value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static String getStringValue(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return TextUtil.EMPTY_STRING;
        }
        return preferences.getString(key, TextUtil.EMPTY_STRING);
    }

    public static String getStringValue(Context context, String key, String defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getString(key, TextUtil.EMPTY_STRING);
    }

    public static boolean setStringValue(Context context, String key, String value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean setBooleanValue(Context context, String key, boolean value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }
        return preferences.getBoolean(key, defaultValue);
    }

    public static int getIntValue(Context context, String key, int defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }
        String savedValue = preferences.getString(key, String.valueOf(defaultValue));
        if (TextUtils.isDigitsOnly(savedValue)) {
            return Integer.valueOf(savedValue);
        }
        return defaultValue;
    }

    public static boolean setIntValue(Context context, String key, int value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, String.valueOf(value));
        return editor.commit();
    }


    public static void checkForNullKey(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
    }

    public static boolean putListString(Context context, String key, ArrayList<String> stringList) {
        SharedPreferences preferences = getSharedPreferences(context);
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, TextUtils.join("‚‗‚", myStringList));
        return editor.commit();
    }


    public static ArrayList<String> getListString(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    public static long getLongValue(Context context, String key, long defaultValue) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return defaultValue;
        }
        String savedValue = preferences.getString(key, String.valueOf(defaultValue));
        if (TextUtils.isDigitsOnly(savedValue)) {
            return Long.valueOf(savedValue);
        }
        return defaultValue;
    }

    public static boolean setLongValue(Context context, String key, long value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, String.valueOf(value));
        return editor.commit();
    }


}
