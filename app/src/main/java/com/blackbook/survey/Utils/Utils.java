package com.blackbook.survey.Utils;

import android.content.Context;
import android.content.Intent;

/**
 *
 * Created by jcaruso on 11/6/2017.
 *
 */

public class Utils {
    public static String TAG = "PROJECTCARUSO";
    private Context c;

    public Utils(Context c) {
        this.c = c;
    }

    public void sendmail()
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@brown-wilson.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "2018 Black Book Survey ");
        email.putExtra(Intent.EXTRA_TEXT,"");

        email.setType("message/rfc822");

        c.startActivity(Intent.createChooser(email, "Choose an Email client :"));

    }
}
