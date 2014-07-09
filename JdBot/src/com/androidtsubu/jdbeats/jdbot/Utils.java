package com.androidtsubu.jdbeats.jdbot;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.NotificationCompat;

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
    
    // 投稿のヘッダとフッタ
    private static final String POST_HEADER = "@miguse ";
    private static final String POST_FOOTER = " #jdbeats";


    /**
     * Twitterに投げるべき文言を返すメソッド
     * @param context コンテキスト
     * @param push_type 番号 PUSH_RESULT_1 〜　5をいれること
     * @return 投稿文字列
     */
    public static String getPostMessage(Context context, int push_type) {
        String postBody;
        switch (push_type) {
            
            case PUSH_RESULT_1:
                postBody = loadFromSharedPref(context, PUSH_MESSAGE_1);
                break;
            case PUSH_RESULT_2:
                postBody = loadFromSharedPref(context, PUSH_MESSAGE_2);
                break;
            case PUSH_RESULT_3:
                postBody = loadFromSharedPref(context, PUSH_MESSAGE_3);
                break;
            case PUSH_RESULT_4:
                postBody = loadFromSharedPref(context, PUSH_MESSAGE_4);
                break;
            case PUSH_RESULT_5:
                postBody = loadFromSharedPref(context, PUSH_MESSAGE_5);
                break;

            default:
                postBody = "エラーです";
                break;
        }
        
        // ヘッダ、ボディ、フッタの結合を返す。
        return POST_HEADER + postBody + POST_FOOTER;
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
    
    /**
     * ノティフィケーションを表示する
     * @param con
     * @param iconResource
     * @param titleText
     * @param contentText
     */
    public static void showNotification(Context con, int iconResource, String titleText, String contentText) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(con)
                .setSmallIcon(iconResource)
                .setContentTitle(titleText)
                .setContentText(contentText);
        NotificationManager mNotificationManager =
            (NotificationManager) con.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());    
    }

}
