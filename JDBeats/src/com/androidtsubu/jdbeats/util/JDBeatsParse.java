
package com.androidtsubu.jdbeats.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;

public class JDBeatsParse extends AsyncTask<String,Integer, String>{

    protected String url;
    protected HttpClient httpClient;
    protected List<NameValuePair> params = new ArrayList<NameValuePair>(1);

    public JDBeatsParse(String url) {
        this.httpClient = new DefaultHttpClient();
        this.url = url;
    }

    public void addParam(String key, String value) {
        this.params.add(new BasicNameValuePair(key, value));
    }

    @Override
    protected String doInBackground(String... params) {
        UrlEncodedFormEntity entry = null;
        try {
            entry = new UrlEncodedFormEntity(this.params);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(this.url);

        httpPost.setEntity(entry);
        httpPost.addHeader("X-Parse-Application-Id", "vKXdOzEY3Q79XkTgwk45GjZdXPxxzq8aqaBhcXIP");
        httpPost.addHeader("X-Parse-REST-API-Key", "ZDhnOI1v8GRPjq6rYzLdWTkW7NZoyFGCNl0SYBY2");
        httpPost.addHeader("Content-Type", "application/json");
        
        String responseText = new String();
        HttpResponse response = null;
        try {
            response = this.httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            response.getEntity().writeTo(byteArrayOutputStream);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            responseText = byteArrayOutputStream.toString();
        }
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return responseText;
    }
}
