
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

public class JDBeatsParse {

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

    public String doPost() throws UnsupportedEncodingException, ClientProtocolException,
            IOException {
        UrlEncodedFormEntity entry = new UrlEncodedFormEntity(this.params);
        HttpPost httpPost = new HttpPost(this.url);

        httpPost.setEntity(entry);
        httpPost.addHeader("X-Parse-Application-Id", "vKXdOzEY3Q79XkTgwk45GjZdXPxxzq8aqaBhcXIP");
        httpPost.addHeader("X-Parse-REST-API-Key", "ZDhnOI1v8GRPjq6rYzLdWTkW7NZoyFGCNl0SYBY2");
        httpPost.addHeader("Content-Type", "application/json");

        return this.doHttpRequest(httpPost);
    }

    protected String doHttpRequest(HttpUriRequest request) throws ClientProtocolException,
            IOException {
        String responseText = new String();
        HttpResponse response = this.httpClient.execute(request);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        response.getEntity().writeTo(byteArrayOutputStream);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            responseText = byteArrayOutputStream.toString();
        }
        byteArrayOutputStream.close();

        return responseText;
    }
}
