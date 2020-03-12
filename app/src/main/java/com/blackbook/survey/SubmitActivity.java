package com.blackbook.survey;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.Constant.WsConstant;
import com.blackbook.survey.Utils.Utils;
import com.blackbook.survey.asynktask.AsyncPostService;
import com.blackbook.survey.interfaces.WsResponseListener;
import com.blackbook.survey.model.Common;
import com.blackbook.survey.model.ResponseObject;
import com.blackbook.survey.model.ResponseResult;
import com.blackbook.survey.model.User;
import com.google.gson.Gson;

/**
 *
 * Created by c119 on 31/03/16.
 *
 */
public class SubmitActivity extends BaseActivity implements View.OnClickListener, WsResponseListener
{
    public TextView txtvarify;
    public Button btnsubmit;
    public String sendpref, senduserpref;

    public RelativeLayout relaeditext;
    public User obj;
    public String finalquestion, finalanswers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            finalquestion = extras.getString("question_string");
            finalanswers = extras.getString("answer_string");
        }

        initview();
    }

    private void initview()
    {
        relaeditext = (RelativeLayout)findViewById(R.id.rela_editext);

        txtvarify = (TextView)findViewById(R.id.txt_varify);
        txtvarify.setTypeface(Sufi_Regular);

        btnsubmit = (Button)findViewById(R.id.btn_submit);
        btnsubmit.setOnClickListener(this);
        btnsubmit.setTypeface(Sufi_Regular);

        Gson gson = new Gson();
        String json = AppGlobal.getStringPreference(this, AppConstant.PREF_USER_OBJ);
        obj = gson.fromJson(json, User.class);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.img_back:
                finish();
                break;

            case R.id.btn_submit:
                sendpref = AppGlobal.getStringPreference(this, AppConstant.Prefsendpref);
                senduserpref = AppGlobal.getStringPreference(this, AppConstant.Prefsenduserpref);

                if (AppGlobal.isNetwork(SubmitActivity.this))
                {
                    Log.i(Utils.TAG, "Submission of survay.");

                    String scoreobtain = AppGlobal.getStringPreference(this, AppConstant.Prefscorematrixaverage);
                    String scorematrixid = AppGlobal.getStringPreference(this, AppConstant.PrefscorematrixId);

                    Common cm = new Common();
                    cm.setUserID(obj.getId().trim());
                    cm.setDeviceToken(obj.getDevice_token().trim());
                    cm.setDeviceType(AppConstant.Const_Android);
                    cm.setRoleID(AppGlobal.getStringPreference(this, AppConstant.PrefrolesId));
                    cm.setOrganizationTypeID(AppGlobal.getStringPreference(this, AppConstant.PreforgtypeId));

                    if(AppGlobal.getStringPreference(this,AppConstant.PreforgtypeId).equalsIgnoreCase("10"))
                    {
                        String othertxt = AppGlobal.getStringPreference(this,AppConstant.Preforgtypetext);
                        String newString = othertxt.replace("Other - ", "");
                        cm.setOtherTitle(newString.trim());
                    }

                    if(AppGlobal.getStringPreference(this,AppConstant.PrefvendorsId).equalsIgnoreCase("1480"))
                    {
                        String venothertxt = AppGlobal.getStringPreference(this,AppConstant.Prefvendorstext);
                        String newvenstring = venothertxt.replace("Other - ", "");
                        cm.setOtherVendorTitle(newvenstring);
                    }

                    cm.setSurveyTypeID(AppGlobal.getStringPreference(this,AppConstant.PrefsurveytypeId));
                    cm.setVendorID(AppGlobal.getStringPreference(this, AppConstant.PrefvendorsId));
                    cm.setRateID(AppGlobal.getStringPreference(this, AppConstant.PrefrateId));
                    cm.setSurveyScoreObtained(scoreobtain);
                    cm.setScoreMatrixID(scorematrixid);
                    cm.setPreferences(sendpref);
                    cm.setUserPreferences(senduserpref);
                    cm.setQuestionIDs(finalquestion);
                    cm.setResponseTexts(finalanswers);

                    try
                    {
                        new AsyncPostService(SubmitActivity.this,getResources().getString(R.string.str_submitdata),WsConstant.Req_SURVEYSTATISTICSFORUSER, cm, true, true).execute(WsConstant.WS_SURVEY_STATISTICS_FOR_USER);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    AppGlobal.showToast(this,getResources().getString(R.string.str_no_internet));
                }

                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDelieverResponse(String serviceType, Object data, Exception error)
    {
        if (error == null)
        {
            if (serviceType.equalsIgnoreCase(WsConstant.Req_SURVEYSTATISTICSFORUSER))
            {
                ResponseObject resObj = ((ResponseResult) data).getResult();
                if(resObj.getError_status().equalsIgnoreCase("NO"))
                {
                    Intent intent = new Intent(this, CompleteActivity.class);
                    startActivity(intent);
                }
            }

            if (serviceType.equalsIgnoreCase(WsConstant.Req_SURVEYSTATISTICS))
            {
                ResponseObject resObj = ((ResponseResult) data).getResult();
                if(resObj.getError_status().equalsIgnoreCase("NO"))
                {
                    Intent intent = new Intent(this, CompleteActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
