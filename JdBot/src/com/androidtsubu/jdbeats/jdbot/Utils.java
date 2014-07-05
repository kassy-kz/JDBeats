package com.androidtsubu.jdbeats.jdbot;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Utils {

    // prefereneのファイル
    public static final String PREF_FILE_NAME = "pref_file";
    public static final String PUSH_MESSAGE_1 = "mes1"; 
    public static final String PUSH_MESSAGE_2 = "mes2"; 
    public static final String PUSH_MESSAGE_3 = "mes3"; 
    public static final String PUSH_MESSAGE_4 = "mes4"; 
    public static final String PUSH_MESSAGE_5 = "mes5"; 

    /**
     * push 結果通知に使うintentにぶっこむ値
     */
    public static final String PUSH_RESULT = "push_result";
    public static final int PUSH_RESULT_1 = 1;
    public static final int PUSH_RESULT_2 = 2;
    public static final int PUSH_RESULT_3 = 3;
    public static final int PUSH_RESULT_4 = 4;
    public static final int PUSH_RESULT_5 = 5;

    /**
     * Twitterに投稿する文言を取得する
     * @return
     */
    public static String getPostMessage(int push_type) {
        return push_type + " のメッセージです（スタブ）";
    }    
    
    
    /**
     * アプリ設定を保存する
     * @param context
     * @param itemName 項目名
     * @param itemValue 項目値
     */
    public static void saveToSharedPref(Context context, String itemName, String itemValue) {
        SharedPreferences shPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        Editor e = shPref.edit();
        e.putString(itemName, itemValue);
        e.commit();
    }

    /**
     * アプリ設定をロードする
     * @param context
     * @param itemName 項目名
     * @return
     */
    public static String loadFromSharedPref(Context context, String itemName) {
        SharedPreferences shPref = context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        String itemValue  = shPref.getString(itemName, "");
        return itemValue;
    }
}
