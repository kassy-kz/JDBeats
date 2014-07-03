
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

import com.androidtsubu.jdbeats.jd.R;
import android.app.Activity;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.signpost.http.HttpResponse;

public class Twitter extends Activity {

    private Button btnAuth = null;
    private Button btnPost = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_layout);

        // 認証するボタン
        btnAuth = (Button) findViewById(R.id.btnAuth);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTwitter();
            }
        });

        // 投稿するボタン
        btnPost = (Button) findViewById(R.id.btnPost);
        btnPost.setEnabled(ParseTwitterUtils.getTwitter().getAuthToken() != null);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseTwitterUtils.getTwitter().getAuthToken() != null) {
                    PostTask task = new PostTask();
                    task.execute();
                } else {
                    Toast.makeText(getApplicationContext(), "認証してない", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Twitterにログインする
    public void loginTwitter() {
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "NG", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "　OK", Toast.LENGTH_LONG).show();
                    btnPost.setEnabled(true);
                }
            }

            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                // TODO Auto-generated method stub
                
            }
        });
    }

    // 「テスト」を投稿する
    class PostTask extends AsyncTask<String, Integer, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("https://api.twitter.com/1.1/statuses/update.json");
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            HttpResponse response = null;
            try {
                postParams.add(new BasicNameValuePair("status", "テスト"));
                httpPost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
                ParseTwitterUtils.getTwitter().signRequest(httpPost);
                response = (HttpResponse) client.execute(httpPost);
            } catch (ClientProtocolException e) {
            } catch (UnsupportedEncodingException e) {
            } catch (IOException e) {
            }
            return ((org.apache.http.HttpResponse) response).getStatusLine().getStatusCode();
        }

        @Override
        protected void onPostExecute(Integer statusCode) {
            super.onPostExecute(statusCode);
            if (statusCode == HttpURLConnection.HTTP_OK) {
                Toast.makeText(getApplicationContext(), "投稿完了", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "失敗", Toast.LENGTH_LONG).show();
            }
        }
    }
}
