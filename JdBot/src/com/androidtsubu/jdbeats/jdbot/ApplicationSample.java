
package com.androidtsubu.jdbeats.jdbot;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseTwitterUtils;
import com.parse.PushService;

public class ApplicationSample extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // ParseÇÃèâä˙âª
        Parse.initialize(this, "vKXdOzEY3Q79XkTgwk45GjZdXPxxzq8aqaBhcXIP",
                "mLNQM1KQocz82N2InlWvmoeZaiqI3kcChflLbbDO");
        
        PushService.setDefaultPushCallback(this, Twitter.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // TwitterÇóòóp
        ParseTwitterUtils.initialize("XstXUMNnzhyeVOmd6GGjw",
                "yNsZx6d1L9q6N8pRCjWpv5N3blWSNTCWnGMwyLYCmw");

    }
}
