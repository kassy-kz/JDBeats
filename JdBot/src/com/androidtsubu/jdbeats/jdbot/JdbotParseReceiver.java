package com.androidtsubu.jdbeats.jdbot;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class JdbotParseReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle extra = intent.getExtras();
            String data = extra.getString("com.parse.Data");
            JSONObject json = new JSONObject(data);
            
            int pushType = Integer.parseInt(json.getString("msg"));
            String msg = Utils.getPostMessage(pushType);
            PostTwitterTask task = new PostTwitterTask(context);
            task.execute(msg);
        } catch (JSONException e) {
            
        } catch (NumberFormatException e) {
            
        }
    }

}
