package com.blackbook.survey;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener, WsResponseListener, GoogleApiClient.OnConnectionFailedListener
        //, LinkedInManagerResponse
{
    /*
     * Facebook Variables.
     */
    private CallbackManager callbackManager;
    private String fb_id, g_id, t_id, username, firstname, lastname, emailid;

    TwitterAuthClient twitterAuthClient;

    private static ArrayList<String> arr_permisions = new ArrayList<String>() {{
        add("user_friends");
        add("email");
        add("public_profile");
    }};

    /*
     * Google login variables.
     */
    private GoogleApiClient mGoogleApiClient;


    /*
     * Twitter login variables.
     */
    private TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        callbackManager = CallbackManager.Factory.create();
        twitterAuthClient = new TwitterAuthClient();

        initview();

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton signInButton = (SignInButton) findViewById(R.id.img_login_with_g);
        signInButton.setOnClickListener(this);

//        TwitterConfig config = new TwitterConfig.Builder(this)
//                .logger(new DefaultLogger(Log.DEBUG))
//                .twitterAuthConfig(new TwitterAuthConfig("CONSUMER_KEY", "CONSUMER_SECRET"))
//                .debug(true)
//                .build();
//        Twitter.initialize(config);

        Twitter.initialize(this);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.img_login_with_tw);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
            }

            @Override
            public void failure(TwitterException exception) {
                // Upon error, show a toast message indicating that authorization request failed.
                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initview()
    {
        TextView txtmessage = (TextView)findViewById(R.id.txt_message);
        txtmessage.setTypeface(Sufi_Regular);

        Button btnguest = (Button)findViewById(R.id.btn_continue_asguest);
        btnguest.setOnClickListener(this);
        btnguest.setTypeface(Sufi_Regular);

        ImageView btnloginwithfb = (ImageView) findViewById(R.id.img_login_with_fb);
        btnloginwithfb.setOnClickListener(this);

        if (AppGlobal.isNetwork(this)) {
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {

                                    try
                                    {
                                        Log.i(Utils.TAG, "fb-detail" + String.valueOf(object));

                                        fb_id = object.getString("id");
                                        username = object.getString("name");
                                        firstname = object.getString("first_name");
                                        lastname = object.getString("last_name");

                                        if (object.getString("email") != null)
                                        {
                                            emailid = object.getString("email");
                                        }
                                        else
                                        {
                                            emailid = "";
                                        }

                                        Common cm = new Common();
                                        cm.setFacebookID(fb_id);

                                        if (AppGlobal.isNetwork(MainActivity.this))
                                        {
                                            try
                                            {
                                                new AsyncPostService(MainActivity.this,getResources().getString(R.string.Please_wait),WsConstant.Req_Userby_Fbid, cm, true, true).execute(WsConstant.WS_USERBY_FBID);
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                        else
                                        {
                                            AppGlobal.showToast(MainActivity.this, getResources().getString(R.string.str_no_internet));
                                        }

                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }

                            });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,name,first_name,last_name,link,birthday,picture,email,gender");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException exception) {
                }
            });
        }
        else
        {
            AppGlobal.showToast(MainActivity.this, getResources().getString(R.string.str_no_internet));
        }

        ImageView btnloginwithli = (ImageView) findViewById(R.id.img_login_with_li);
        btnloginwithli.setOnClickListener(this);

        Button btnprivacy = (Button) findViewById(R.id.btnPrivacyPolicy);
        btnprivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });

        Button btnterms = (Button) findViewById(R.id.btnTermsService);
        btnterms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TermsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppGlobal.hideProgressDialog(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // handle cancelled Twitter login (resets TwitterCore.*AuthHandler.AuthState)
        if(twitterAuthClient.getRequestCode() == requestCode) {
            //TODO:
            username = data.getExtras().get("screen_name").toString();
            t_id = data.getExtras().get("user_id").toString();

            User uobj = new User();
            uobj.setId("");
            uobj.setUsername(username);
            uobj.setTwitter_id(t_id);
            uobj.setPassword("");
            uobj.setFirstname("");
            uobj.setLastname("");
            uobj.setEmail_id(emailid);
            uobj.setPhone_number("");
            uobj.setDevice_token("");
            uobj.setDevice_type(AppConstant.Const_Android);
            uobj.setCreated_date("");
            uobj.setModified_date("");
            uobj.setIs_deleted("");

            if (! AppGlobal.getStringPreference(this, AppConstant.PREF_USER_OBJ).equals(""))
            {
                AppGlobal.removepref(this, AppConstant.PREF_USER_OBJ);
            }

            Gson gson = new Gson();
            String json = gson.toJson(uobj);
            AppGlobal.setStringPreference(this,json,AppConstant.PREF_USER_OBJ);

            Common cm = new Common();
            cm.setTwitterID(t_id);

            if (AppGlobal.isNetwork(MainActivity.this))
            {
                try
                {
                    new AsyncPostService(MainActivity.this, getResources().getString(R.string.Please_wait), WsConstant.Req_Userby_Tid, cm, true, true).execute(WsConstant.WS_USERBY_FBID);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                AppGlobal.showToast(MainActivity.this, getResources().getString(R.string.str_no_internet));
            }

        } else if (requestCode == AppConstant.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);

        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.i(Utils.TAG, "Google Signin handleSignInResult success: " + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            if (acct != null) {
                g_id = acct.getId();
                username = acct.getGivenName() + " " + acct.getFamilyName();
                firstname = acct.getGivenName();
                lastname = acct.getFamilyName();
                emailid = acct.getEmail();
            }

            User uobj = new User();
            uobj.setId("");
            uobj.setUsername(username);
            uobj.setGoogle_id(g_id);
            uobj.setPassword("");
            uobj.setFirstname(firstname);
            uobj.setLastname(lastname);
            uobj.setEmail_id(emailid);
            uobj.setPhone_number("");
            uobj.setDevice_token("");
            uobj.setDevice_type(AppConstant.Const_Android);
            uobj.setCreated_date("");
            uobj.setModified_date("");
            uobj.setIs_deleted("");

            if (! AppGlobal.getStringPreference(this, AppConstant.PREF_USER_OBJ).equals(""))
            {
                AppGlobal.removepref(this, AppConstant.PREF_USER_OBJ);
            }

            Gson gson = new Gson();
            String json = gson.toJson(uobj);
            AppGlobal.setStringPreference(this,json,AppConstant.PREF_USER_OBJ);

            Common cm = new Common();
            cm.setGoogleID(g_id);

            if (AppGlobal.isNetwork(MainActivity.this))
            {
                try
                {
                    new AsyncPostService(MainActivity.this, getResources().getString(R.string.Please_wait), WsConstant.Req_Userby_Gid, cm, true, true).execute(WsConstant.WS_USERBY_FBID);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                AppGlobal.showToast(MainActivity.this, getResources().getString(R.string.str_no_internet));
            }

        } else {
            Log.i(Utils.TAG, result.getStatus().toString());
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.img_login_with_fb:
                AppGlobal.setBooleanPreference(this, false, AppConstant.PREF_GUESTLOGIN);
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, arr_permisions);
                break;
            case R.id.img_login_with_li:
                AppGlobal.setBooleanPreference(this, false, AppConstant.PREF_GUESTLOGIN);
//                LinkedInRequestManager linkedInRequestManager = new LinkedInRequestManager(MainActivity.this, this, getResources().getString(R.string.linkedin_client_id), getResources().getString(R.string.linkedin_client_secret), getResources().getString(R.string.linkedin_redirect_url));
//                linkedInRequestManager.showAuthenticateView(LinkedInRequestManager.MODE_BOTH_OPTIONS);
                break;
            case R.id.btn_continue_asguest:
                Log.i(Utils.TAG, "Guest registration");
                AppGlobal.setBooleanPreference(this, true, AppConstant.PREF_GUESTLOGIN);
                Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(i);
                break;

            case R.id.img_login_with_g:
                AppGlobal.setBooleanPreference(this, false, AppConstant.PREF_GUESTLOGIN);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, AppConstant.RC_SIGN_IN);
                break;

        }
    }

    @Override
    public void onDelieverResponse(String serviceType, Object data, Exception error)
    {
        Log.i(Utils.TAG, "API response, check to see what from and if it was an error.");

        if (error == null)
        {
            if ((serviceType.equalsIgnoreCase(WsConstant.Req_Userby_Fbid))
                    || (serviceType.equalsIgnoreCase(WsConstant.Req_Userby_Gid))
                    || (serviceType.equalsIgnoreCase(WsConstant.Req_Userby_Tid)))
            {
                boolean isskip;
                ResponseObject resObj = ((ResponseResult) data).getResult();
                if(resObj.getError_status().equalsIgnoreCase("Error"))
                {
                    Log.i(Utils.TAG, "This is a new user, we're gonna save the user.");
                    isskip = false;

                    Common uobj = new Common();

                    uobj.setFacebookID(fb_id);
                    uobj.setGoogleID(g_id);
                    uobj.setTwitterID(t_id);
                    uobj.setUsername(username);
                    uobj.setEmailID(emailid);
                    uobj.setPhone("");
                    uobj.setDeviceToken("");
                    uobj.setDeviceType(AppConstant.Const_Android);

                    if (! AppGlobal.getStringPreference(this,AppConstant.PREF_USER_OBJ).equals(""))
                    {
                        AppGlobal.removepref(this, AppConstant.PREF_USER_OBJ);
                    }

                    Gson gson = new Gson();
                    String json = gson.toJson(uobj);
                    AppGlobal.setStringPreference(this, json, AppConstant.PREF_USER_OBJ);

                    new AsyncPostService(MainActivity.this,getResources().getString(R.string.Saving_profile), WsConstant.Req_Save_Profile, uobj,true ,true).execute(WsConstant.WS_SAVE_PROFILE);
                }
                else
                {
                    Log.i(Utils.TAG, "This is an existing user, we're gonna save the user.");
                    isskip = true;
                    if(resObj.getArr_user().size() > 0)
                    {
                        Common cobj = new Common();
                        cobj.setUserID(resObj.getArr_user().get(0).getId());
                        cobj.setUsername(resObj.getArr_user().get(0).getUsername());
                        cobj.setFacebookID(resObj.getArr_user().get(0).getFacebook_id());
                        cobj.setGoogleID(resObj.getArr_user().get(0).getGoogle_id());
                        cobj.setTwitterID(resObj.getArr_user().get(0).getTwitter_id());
                        cobj.setEmailID(resObj.getArr_user().get(0).getEmail_id());
                        cobj.setPhone(resObj.getArr_user().get(0).getPhone_number());
                        cobj.setDeviceToken(resObj.getArr_user().get(0).getDevice_token());
                        cobj.setDeviceType(resObj.getArr_user().get(0).getDevice_type());

                        if (! AppGlobal.getStringPreference(this,AppConstant.PREF_USER_OBJ).equals(""))
                        {
                            AppGlobal.removepref(this, AppConstant.PREF_USER_OBJ);
                        }

                        User uobj = new User();
                        uobj.setId(resObj.getArr_user().get(0).getId());
                        uobj.setUsername(resObj.getArr_user().get(0).getUsername());
                        uobj.setFacebook_id(resObj.getArr_user().get(0).getFacebook_id());
                        uobj.setGoogle_id(resObj.getArr_user().get(0).getGoogle_id());
                        uobj.setTwitter_id(resObj.getArr_user().get(0).getTwitter_id());
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

                        Gson gson = new Gson();
                        String json = gson.toJson(uobj);
                        AppGlobal.setStringPreference(this, json, AppConstant.PREF_USER_OBJ);

                        new AsyncPostService(MainActivity.this,getResources().getString(R.string.Saving_profile),WsConstant.Req_Save_Profile, cobj, true, true).execute(WsConstant.WS_SAVE_PROFILE);
                    }
                }

                if (isskip) {
                    Intent i = new Intent(MainActivity.this, PresurveyActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(i);
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Survey LinedIn", "onConnectionFailed");
    }

}
