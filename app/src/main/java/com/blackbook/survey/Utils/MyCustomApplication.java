package com.blackbook.survey.Utils;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;

/**
 *
 * Created by jcaruso on 12/4/2017.
 *
 */

public class MyCustomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Twitter.initialize(this);
    }
}
