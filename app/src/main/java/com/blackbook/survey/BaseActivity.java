package com.blackbook.survey;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.Utils.Utils;

/**
 *
 * Created by c119 on 12/04/16.
 *
 */
public class BaseActivity extends AppCompatActivity
{
    public static Typeface Sufi_Regular,Sufi_Medium,Sufi_Bold;
    public Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Sufi_Regular = AppGlobal.setCustomFont(this, AppGlobal.SufiRegular);
        Sufi_Medium = AppGlobal.setCustomFont(this, AppGlobal.SufiMedium);
        Sufi_Bold = AppGlobal.setCustomFont(this, AppGlobal.SufiBold);

        utils = new Utils(this);
    }
}