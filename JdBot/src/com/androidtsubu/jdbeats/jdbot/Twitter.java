
package com.androidtsubu.jdbeats.jdbot;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

public class Twitter extends Activity {

    private Button btnAuth = null;
    private Button btnPost = null;
    private EditText mEditText1;
    private EditText mEditText2;
    private EditText mEditText3;
    private EditText mEditText4;
    private EditText mEditText5;

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
        
        // EditTextアイテムの設定
        mEditText1 = (EditText) findViewById(R.id.edit1);
        mEditText2 = (EditText) findViewById(R.id.edit2);
        mEditText3 = (EditText) findViewById(R.id.edit3);
        mEditText4 = (EditText) findViewById(R.id.edit4);
        mEditText5 = (EditText) findViewById(R.id.edit5);
    }

    // Twitterにログインする
    public void loginTwitter() {
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException err) {
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "NG", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "　OK", Toast.LENGTH_LONG).show();
                    btnPost.setEnabled(true);
                }
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
            org.apache.http.HttpResponse response = null;
            try {
                Calendar clen = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(
                        "HH:mm");
                String strGetTime = sdf.format(clen.getTime());
                strGetTime = strGetTime + "てすとだぉ♪";
                postParams.add(new BasicNameValuePair("status",strGetTime ));
                httpPost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));
                ParseTwitterUtils.getTwitter().signRequest(httpPost);
                response = client.execute(httpPost);
            } catch (ClientProtocolException e) {
            } catch (UnsupportedEncodingException e) {
            } catch (IOException e) {
            }
            return response.getStatusLine().getStatusCode();
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
    
    /**
     * EditTextの文言を保存する
     */
    private void saveYellMessage() {
        String edit1 = mEditText1.getText().toString();
        Utils.saveToSharedPref(this, Utils.PUSH_MESSAGE_1, edit1);
        String edit2 = mEditText1.getText().toString();
        Utils.saveToSharedPref(this, Utils.PUSH_MESSAGE_2, edit2);
        String edit3 = mEditText1.getText().toString();
        Utils.saveToSharedPref(this, Utils.PUSH_MESSAGE_3, edit3);
        String edit4 = mEditText1.getText().toString();
        Utils.saveToSharedPref(this, Utils.PUSH_MESSAGE_4, edit4);
        String edit5 = mEditText1.getText().toString();
        Utils.saveToSharedPref(this, Utils.PUSH_MESSAGE_5, edit5);
    }
    
    /**
     * EditTextに文言を設定する
     */
    private void loadYellMessage() {
        String str1 = Utils.loadFromSharedPref(this, Utils.PUSH_MESSAGE_1);
        mEditText1.setText(str1);
        String str2 = Utils.loadFromSharedPref(this, Utils.PUSH_MESSAGE_2);
        mEditText2.setText(str2);
        String str3 = Utils.loadFromSharedPref(this, Utils.PUSH_MESSAGE_3);
        mEditText3.setText(str3);
        String str4 = Utils.loadFromSharedPref(this, Utils.PUSH_MESSAGE_4);
        mEditText4.setText(str4);
        String str5 = Utils.loadFromSharedPref(this, Utils.PUSH_MESSAGE_5);
        mEditText5.setText(str5);
    }
    
    /**
     * onResume
     */
    @Override
    public void onResume() {
        super.onResume();
        loadYellMessage();
    }
    
    /**
     * onPause
     */
    @Override
    public void onPause() {
        super.onPause();
        saveYellMessage();
    }
}

