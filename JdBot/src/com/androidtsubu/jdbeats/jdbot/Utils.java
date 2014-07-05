package com.androidtsubu.jdbeats.jdbot;

public class Utils {

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
}
