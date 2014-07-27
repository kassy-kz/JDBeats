
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
    // Parseの初期化
    Parse.initialize(this, "vKXdOzEY3Q79XkTgwk45GjZdXPxxzq8aqaBhcXIP",
        "mLNQM1KQocz82N2InlWvmoeZaiqI3kcChflLbbDO");

    PushService.setDefaultPushCallback(this, CheeringBot.class);
    ParseInstallation.getCurrentInstallation().saveInBackground();

    // Twitterを利用
    ParseTwitterUtils.initialize("XstXUMNnzhyeVOmd6GGjw",
        "yNsZx6d1L9q6N8pRCjWpv5N3blWSNTCWnGMwyLYCmw");

    // Push通知のsubscribe
    PushService.subscribe(this, "AWP", CheeringBot.class);
  }
}
