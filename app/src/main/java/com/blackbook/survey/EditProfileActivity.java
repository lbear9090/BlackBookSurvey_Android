package com.blackbook.survey;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.Constant.WsConstant;
import com.blackbook.survey.asynktask.AsyncPostService;
import com.blackbook.survey.interfaces.WsResponseListener;
import com.blackbook.survey.model.Common;
import com.blackbook.survey.model.ResponseObject;
import com.blackbook.survey.model.ResponseResult;
import com.blackbook.survey.model.User;
import com.google.gson.Gson;

/**
 *
 * Created by c119 on 21/04/16.
 *
 */
public class EditProfileActivity extends BaseActivity implements View.OnClickListener,WsResponseListener
{
    private EditText edtname, edtemail, edtphone;

    private User obj;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        edtname = findViewById(R.id.edt_name);
        edtemail = findViewById(R.id.edt_email);
        edtphone = findViewById(R.id.edt_phone);

        edtname.setTypeface(Sufi_Regular);
        edtemail.setTypeface(Sufi_Regular);
        edtphone.setTypeface(Sufi_Regular);

        Button btnsaveprofile = findViewById(R.id.btn_save_profile);
        btnsaveprofile.setOnClickListener(this);
        btnsaveprofile.setTypeface(Sufi_Regular);

        Gson gson = new Gson();
        String json = AppGlobal.getStringPreference(this ,AppConstant.PREF_USER_OBJ);
        obj = gson.fromJson(json, User.class);

        edtname.setText(obj.getUsername().trim());
        edtemail.setText(obj.getEmail_id().trim());
        edtphone.setText(obj.getPhone_number().trim());

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_save_profile:
                if (validateProfile()) {
                    callsaveprofile();
                }

                break;
        }
    }

    public boolean validateProfile()
    {
        if(edtname.getText().toString().length() <= 0 || edtname.getText().toString().equals("") || edtname.getText().toString().equals(" ")) {
            AppGlobal.showToast(this, getResources().getString(R.string.str_enter_name));
            return false;

        }

        if(edtemail.getText().toString().length() <= 0 || edtemail.getText().toString().equals("")|| edtemail.getText().toString().equals(" ")) {
            AppGlobal.showToast(this,getResources().getString(R.string.str_enter_email));
            return false;

        }

        if(!AppGlobal.checkEmail(edtemail.getText().toString())) {
            AppGlobal.showToast(this,getResources().getString(R.string.str_enter_validemail));
            return false;

        }

        if(edtphone.getText().toString().length() <= 0 || edtphone.getText().toString().equals("") || edtphone.getText().toString().equals(" ")) {
            AppGlobal.showToast(this,getResources().getString(R.string.str_enter_phone));
            return false;

        }
        
        if (!AppGlobal.checkPhone(edtphone.getText().toString())) {
            AppGlobal.showToast(this, getResources().getString(R.string.str_enter_validphone));

            return false;
        }
        return true;
    }

    private void callsaveprofile()
    {
        Common cm = new Common();
        cm.setUserID(obj.getId().trim());

        if (obj.getFacebook_id() != null) {
            cm.setFacebookID(obj.getFacebook_id().trim());
        }

        if (obj.getGoogle_id() != null) {
            cm.setGoogleID(obj.getGoogle_id().trim());
        }

        if (obj.getTwitter_id() != null) {
            cm.setTwitterID(obj.getTwitter_id().trim());
        }

        cm.setUsername(edtname.getText().toString().trim());
        cm.setEmailID(edtemail.getText().toString().trim());
        cm.setPhone(edtphone.getText().toString().trim());
        cm.setDeviceToken(obj.getDevice_token().trim());
        cm.setDeviceType(obj.getDevice_type().trim());

        if (AppGlobal.isNetwork(EditProfileActivity.this))
        {
            try
            {
                new AsyncPostService(EditProfileActivity.this,getResources().getString(R.string.Saving_profile),WsConstant.Req_Save_Profile,cm,true,true)
                        .execute(WsConstant.WS_SAVE_PROFILE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            AppGlobal.showToast(EditProfileActivity.this, getResources().getString(R.string.str_no_internet));
        }
    }

    @Override
    public void onDelieverResponse(String serviceType, Object data, Exception error)
    {
        if (error == null)
        {
            if (serviceType.equalsIgnoreCase(WsConstant.Req_Save_Profile))
            {
                ResponseObject resObj = ((ResponseResult) data).getResult();
                if(resObj.getError_status().equalsIgnoreCase("NO"))
                {
                    User uobj = new User();
                    uobj.setId(resObj.getArr_user().get(0).getId());
                    uobj.setUsername(resObj.getArr_user().get(0).getUsername());
                    uobj.setFacebook_id(resObj.getArr_user().get(0).getFacebook_id());
                    uobj.setPassword(resObj.getArr_user().get(0).getPassword());
                    uobj.setFirstname(resObj.getArr_user().get(0).getFirstname());
                    uobj.setLastname(resObj.getArr_user().get(0).getLastname());
                    uobj.setEmail_id(resObj.getArr_user().get(0).getEmail_id());
                    uobj.setPhone_number(resObj.getArr_user().get(0).getPhone_number());
                    uobj.setDevice_token(resObj.getArr_user().get(0).getDevice_token());
                    uobj.setDevice_type(resObj.getArr_user().get(0).getDevice_type());
                    uobj.setCreated_date(resObj.getArr_user().get(0).getCreated_date());
                    uobj.setModified_date(resObj.getArr_user().get(0).getModified_date());
                    uobj.setIs_deleted(resObj.getArr_user().get(0).getIs_deleted());

                    if (!AppGlobal.getStringPreference(this,AppConstant.PREF_USER_OBJ).equals(""))
                    {
                        AppGlobal.removepref(this, AppConstant.PREF_USER_OBJ);
                    }

                    Gson gson = new Gson();
                    String json = gson.toJson(uobj);
                    AppGlobal.setStringPreference(this,json,AppConstant.PREF_USER_OBJ);

                    AppGlobal.setBooleanPreference(this,true,AppConstant.PREF_USERLOGIN);

                    finish();
                }
            }
        }
    }
}
