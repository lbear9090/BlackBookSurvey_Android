package com.blackbook.survey;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Constant.AppGlobal;

/**
 *
 * Created by c119 on 26/03/16.
 *
 */
public class SplashscreenActivity extends BaseActivity
{
    private static final int SPLASH_TIME_OUT = 700;
    public TextView txttitle,txtcopyright;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txttitle = (TextView)findViewById(R.id.txt_title);
        txtcopyright = (TextView)findViewById(R.id.txt_copyright);
        txttitle.setTypeface(Sufi_Regular);
        txtcopyright.setTypeface(Sufi_Regular);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        startApp();
                    }
                }, SPLASH_TIME_OUT);
            }
        }, SPLASH_TIME_OUT);
    }

    private void startApp()
    {
        Intent intent;
        boolean datastore = AppGlobal.getBooleanPreference(SplashscreenActivity.this, AppConstant.Prefdatastore);
        if(datastore)
        {
            if(AppGlobal.getBooleanPreference(this,AppConstant.PREF_GUESTLOGIN))
            {
                intent = new Intent(SplashscreenActivity.this,MainActivity.class);
            }
            else
            {
                if(!AppGlobal.getBooleanPreference(this,AppConstant.PREF_USERLOGIN))
                {
                    intent = new Intent(SplashscreenActivity.this,MainActivity.class);
                }
                else
                {
                    intent = new Intent(SplashscreenActivity.this,PresurveyActivity.class);
                }
            }

        }
        else
        {
            intent = new Intent(SplashscreenActivity.this,GetdataActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
