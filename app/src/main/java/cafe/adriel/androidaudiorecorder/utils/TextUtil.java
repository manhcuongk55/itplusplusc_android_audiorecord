package cafe.adriel.androidaudiorecorder.utils;

import android.content.Context;
import android.text.TextUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

public class TextUtil {
    public static final String EMPTY_STRING = "";
    public static final String CHARACTER_QUESTION_MARK = "?";
    public static final String CHARACTER_EQUATION = "=";
    public static final String CHARACTER_AND = "&";
    public static final String CHARACTER_SLASH = "/";
    public static final String CHARACTER_COMMA = ",";
    public static final String CHARACTER_NEWLINE = "\n";
    public static final String CHARACTER_UNDER_SCORE = "_";
    public static final String NULL_STRING = "null";

    /**
     * Parse the list of strings by splitting with comma character
     *
     * @param input
     * @return ArrayList<String>
     * @throws JSONException
     */
    public static ArrayList<String> parseListFromJsonByComma(String input) throws JSONException {
        if (TextUtils.isEmpty(input)) {
            return null;
        }
        return (ArrayList<String>) Arrays.asList(input.split(","));
    }

    /**
     * Get Currency Symbol
     *
     * @param context
     * @return currency symbol character
     */
    public static String getCurrenySymbol(Context context) {
        Locale currentLocal = context.getResources().getConfiguration().locale;
        String symbol;
        try {
            symbol = Currency.getInstance(currentLocal).getSymbol();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            symbol = Currency.getInstance(Locale.US).getSymbol();
        }
        return symbol;
    }

    /**
     * Check if the input string is null or empty or "null" character sequence
     *
     * @param text String
     * @return boolean
     */
    public static boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || NULL_STRING.equalsIgnoreCase(text);
    }

    /**
     * haitt22
     * check array string empty
     *
     * @param text
     * @return
     */
    public static boolean isEmptyArray(String text) {
        text = text.replaceAll("\\[", "").replaceAll("\\]", "");
        return TextUtils.isEmpty(text) || NULL_STRING.equalsIgnoreCase(text);
    }

    /**
     * Build the saved file name format as a_b_c
     *
     * @param params
     * @return string
     */
    public static String buildFileName(Object[] params) {
        String result = EMPTY_STRING;
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                result += params[i].toString();
                if (i < params.length - 1) result += CHARACTER_UNDER_SCORE;
            }
        }
        return result;
    }

}
