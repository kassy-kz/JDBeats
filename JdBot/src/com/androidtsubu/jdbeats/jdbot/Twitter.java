
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

        // Fุท้{^
        btnAuth = (Button) findViewById(R.id.btnAuth);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTwitter();
            }
        });

        // eท้{^
        btnPost = (Button) findViewById(R.id.btnPost);
        btnPost.setEnabled(ParseTwitterUtils.getTwitter().getAuthToken() != null);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseTwitterUtils.getTwitter().getAuthToken() != null) {
                    PostTask task = new PostTask();
                    task.execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Fุตฤศข", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    // TwitterษOCท้
    public void loginTwitter() {
        ParseTwitterUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException err) {
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "NG", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "@OK", Toast.LENGTH_LONG).show();
                    btnPost.setEnabled(true);
                }
            }
        });
    }

    // ueXgv๐eท้
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
                strGetTime = strGetTime + "ฤทฦพง๔";
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
                Toast.makeText(getApplicationContext(), "eฎน", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "ธs", Toast.LENGTH_LONG).show();
            }
        }
    }
}
