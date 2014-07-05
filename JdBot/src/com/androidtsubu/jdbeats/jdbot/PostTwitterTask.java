package com.androidtsubu.jdbeats.jdbot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.parse.ParseTwitterUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class PostTwitterTask extends AsyncTask<String, Integer, Integer> {

    // アプリケーション・コンテキスト
    private Context mContext;

    /**
     * コンストラクタ
     * @param context アプリケーション・コンテキスト
     */
    public PostTwitterTask(Context context) {
        mContext = context;
    }

    /**
     * バックグラウンとで実行する処理
     * @param params Twitterで投入する文字列
     * @return HTTP通信コード
     */
    @Override
    protected Integer doInBackground(String... params) {
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/statuses/update.json");
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        org.apache.http.HttpResponse response = null;
        try {
            
            if (params.length <= 0) {
                return HttpURLConnection.HTTP_INTERNAL_ERROR;
            }
            
            postParams.add(new BasicNameValuePair("status", params[0]));
            httpPost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
            ParseTwitterUtils.getTwitter().signRequest(httpPost);
            response = client.execute(httpPost);
        } catch (ClientProtocolException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (IOException e) {
        }
        return response.getStatusLine().getStatusCode();
    }

    /**
     * Twitter投稿結果
     * @param statusCode 投稿結果
     */
    @Override
    protected void onPostExecute(Integer statusCode) {
        super.onPostExecute(statusCode);
        if (statusCode == HttpURLConnection.HTTP_OK) {
            Toast.makeText(mContext, "���e����", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "���s", Toast.LENGTH_LONG).show();
        }
    }
}
