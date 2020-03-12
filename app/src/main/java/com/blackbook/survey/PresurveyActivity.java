package com.blackbook.survey;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.PresurveyList.OrganizationActivity;
import com.blackbook.survey.PresurveyList.RateActivity;
import com.blackbook.survey.PresurveyList.RoleActivity;
import com.blackbook.survey.PresurveyList.ServicesActivity;
import com.blackbook.survey.PresurveyList.VendorActivity;
import com.blackbook.survey.Utils.Utils;
import com.blackbook.survey.db.DatabaseHelper;

/**
 *
 * Created by c119 on 28/03/16.
 *
 */
public class PresurveyActivity extends BaseActivity implements View.OnClickListener
{
    public RelativeLayout relacontent, reladialog, relatxtservice, relatxtvendor, relatxtorganization, relatxtrole, rela_txtrate;

    public TextView txt579, txt732, txt870, txt1000;
    public TextView txtdeal, txtdealsummery, txtneutral, txtneutralsummery, txtsatisfactory, txtsatisfactorysummery,
            txtoverwhelming, txtoverwhelmingsummery;

    public TextView txtservice, txtvendor, txtorganization, txtrole, txtrate, lblservice, lblvendor, lblorganization, lblrole, lblrate;
    public Button btnstartsurvey;
    public DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presurvey);

        initview();
    }

    private void initview()
    {
        db = new DatabaseHelper(PresurveyActivity.this);

        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Prefsurveytypetext);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Prefvendorstext);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Preforgtypetext);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Prefrolestext);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Prefratetext);

        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.PrefsurveytypeId);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.PrefvendorsId);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.PreforgtypeId);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.PrefrolesId);

        relacontent = (RelativeLayout)findViewById(R.id.rela_content);

        reladialog = (RelativeLayout)findViewById(R.id.rela_dialog);
        reladialog.setOnClickListener(this);

        relatxtservice = (RelativeLayout)findViewById(R.id.rela_txtservice);
        relatxtvendor = (RelativeLayout)findViewById(R.id.rela_txtvendor);
        relatxtorganization = (RelativeLayout)findViewById(R.id.rela_txtorganization);
        relatxtrole = (RelativeLayout)findViewById(R.id.rela_txtrole);
        rela_txtrate = (RelativeLayout)findViewById(R.id.rela_txtrate);

        relatxtservice.setOnClickListener(this);
        relatxtvendor.setOnClickListener(this);
        relatxtorganization.setOnClickListener(this);
        relatxtrole.setOnClickListener(this);
        rela_txtrate.setOnClickListener(this);

        lblservice = (TextView)findViewById(R.id.lbl_service);
        lblvendor = (TextView)findViewById(R.id.lbl_vendor);
        lblorganization = (TextView)findViewById(R.id.lbl_organization);
        lblrole = (TextView)findViewById(R.id.lbl_role);
        lblrate = (TextView)findViewById(R.id.lbl_rate);

        lblservice.setTypeface(Sufi_Regular);
        lblvendor.setTypeface(Sufi_Regular);
        lblorganization.setTypeface(Sufi_Regular);
        lblrole.setTypeface(Sufi_Regular);
        lblrate.setTypeface(Sufi_Regular);

        txtservice = (TextView)findViewById(R.id.txt_service);
        txtvendor = (TextView)findViewById(R.id.txt_vendor);
        txtorganization = (TextView)findViewById(R.id.txt_organization);
        txtrole = (TextView)findViewById(R.id.txt_role);
        txtrate = (TextView)findViewById(R.id.txt_rate);

        txtservice.setTypeface(Sufi_Regular);
        txtvendor.setTypeface(Sufi_Regular);
        txtorganization.setTypeface(Sufi_Regular);
        txtrole.setTypeface(Sufi_Regular);
        txtrate.setTypeface(Sufi_Regular);

        btnstartsurvey = (Button)findViewById(R.id.btn_startsurvey);
        btnstartsurvey.setOnClickListener(this);
        btnstartsurvey.setTypeface(Sufi_Regular);

        txt579 = (TextView)findViewById(R.id.txt_579);
        txt732 = (TextView)findViewById(R.id.txt_732);
        txt870 = (TextView)findViewById(R.id.txt_870);
        txt1000 = (TextView)findViewById(R.id.txt_1000);

        txt579.setTypeface(Sufi_Regular);
        txt732.setTypeface(Sufi_Regular);
        txt870.setTypeface(Sufi_Regular);
        txt1000.setTypeface(Sufi_Regular);

        txtdeal = (TextView)findViewById(R.id.txt_deal);
        txtdealsummery = (TextView)findViewById(R.id.txt_deal_summery);

        txtdeal.setTypeface(Sufi_Bold);
        txtdealsummery.setTypeface(Sufi_Regular);

        txtneutral = (TextView)findViewById(R.id.txt_neutral);
        txtneutralsummery = (TextView)findViewById(R.id.txt_neutral_summery);

        txtneutral.setTypeface(Sufi_Bold);
        txtneutralsummery.setTypeface(Sufi_Regular);

        txtsatisfactory = (TextView)findViewById(R.id.txt_satisfactory);
        txtsatisfactorysummery = (TextView)findViewById(R.id.txt_satisfactory_summery);

        txtsatisfactory.setTypeface(Sufi_Bold);
        txtsatisfactorysummery.setTypeface(Sufi_Regular);

        txtoverwhelming = (TextView)findViewById(R.id.txt_overwhelming);
        txtoverwhelmingsummery = (TextView)findViewById(R.id.txt_overwhelming_summery);

        txtoverwhelming.setTypeface(Sufi_Bold);
        txtoverwhelmingsummery.setTypeface(Sufi_Regular);

        txt579.setText(getResources().getString(R.string.str_579));
        txtdeal.setText(getResources().getString(R.string.str_deal));
        txtdealsummery.setText(getResources().getString(R.string.str_deal_summery));

        txt732.setText(getResources().getString(R.string.str_732));
        txtneutral.setText(getResources().getString(R.string.str_neutral));
        txtneutralsummery.setText(getResources().getString(R.string.str_neutral_summery));

        txt870.setText(getResources().getString(R.string.str_870));
        txtsatisfactory.setText(getResources().getString(R.string.str_satisfactory));
        txtsatisfactorysummery.setText(getResources().getString(R.string.str_satisfactory_summery));

        txt1000.setText(getResources().getString(R.string.str_1000));
        txtoverwhelming.setText(getResources().getString(R.string.str_overwhelming));
        txtoverwhelmingsummery.setText(getResources().getString(R.string.str_overwhelming_summery));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(!AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefsurveytypetext).equals(""))
        {
            txtservice.setText(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefsurveytypetext));
        }

        if(!AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefvendorstext).equals(""))
        {
            txtvendor.setText(AppGlobal.getStringPreference(PresurveyActivity.this,AppConstant.Prefvendorstext));

            String vendorid = db.Getvendorid(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefvendorstext));
            AppGlobal.setStringPreference(this, vendorid, AppConstant.PrefvendorsId);
        }

        if(!AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Preforgtypetext).equals(""))
        {
            txtorganization.setText(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Preforgtypetext));
        }

        if(!AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefrolestext).equals(""))
        {
            txtrole.setText(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefrolestext));
        }

        if(!AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefratetext).equals(""))
        {
            txtrate.setText(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefratetext));

            String rateid = db.Getrateid(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefratetext));
            AppGlobal.setStringPreference(this, rateid, AppConstant.PrefrateId);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        txtservice.setText(getResources().getString(R.string.str_select_service));
        txtvendor.setText(getResources().getString(R.string.str_select_vendor));
        txtorganization.setText(getResources().getString(R.string.str_select_organization));
        txtrole.setText(getResources().getString(R.string.str_select_role));
        txtrate.setText(getResources().getString(R.string.str_select_rating));

        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Prefsurveytypetext);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Prefvendorstext);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Preforgtypetext);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Prefrolestext);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.Prefratetext);

        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.PrefsurveytypeId);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.PrefvendorsId);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.PreforgtypeId);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.PrefrolesId);
        AppGlobal.setStringPreference(PresurveyActivity.this, "", AppConstant.PrefrateId);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(resultCode)
        {
            case 1001:

                finish();
                Intent i = new Intent(PresurveyActivity.this,MainActivity.class);
                startActivity(i);

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.blackbookmarketresearch.com/"));
                startActivity(browserIntent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v)
    {
       switch (v.getId())
       {
           case R.id.rela_txtservice:
               Intent i = new Intent(this, ServicesActivity.class);
               startActivity(i);
               break;

           case R.id.rela_txtvendor:
               Intent ivendor = new Intent(this, VendorActivity.class);
               startActivity(ivendor);
               break;

           case R.id.rela_txtorganization:
               Intent iorg = new Intent(this, OrganizationActivity.class);
               startActivity(iorg);
               break;

           case R.id.rela_txtrole:
               Intent irole = new Intent(this, RoleActivity.class);
               startActivity(irole);
               break;

           case R.id.rela_txtrate:
               Intent irate = new Intent(this, RateActivity.class);
               startActivity(irate);
               break;

           case R.id.btn_startsurvey:

                //if (checkSurvey()) {
                    Intent i12 = new Intent(this, SurveyIndicatorActivity.class);
                    startActivityForResult(i12, 0);
                //}

               break;
       }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_presurvey, menu);

        if(AppGlobal.getBooleanPreference(this, AppConstant.PREF_GUESTLOGIN))
        {
            MenuItem item = menu.findItem(R.id.logout);
            item.setVisible(false);

            item = menu.findItem(R.id.editprofile);
            item.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.key_preformance:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstant.KeyPerformanceURL));
                startActivity(browserIntent);
                break;

            case R.id.vendors:
                Intent vendorIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AppConstant.VendorsURL));
                startActivity(vendorIntent);
                break;

            case R.id.support:
                utils.sendmail();
                break;

            case R.id.scoring:
                Intent skIntent = new Intent(PresurveyActivity.this,ScoringSummeryBoxActivity.class);
                startActivity(skIntent);
                break;

            case R.id.editprofile:
                Intent iedt = new Intent(PresurveyActivity.this,EditProfileActivity.class);
                iedt.putExtra("wanttoskip",true);
                iedt.putExtra("whichact",",preseuveyact");
                startActivity(iedt);
                break;

            case R.id.logout:
                AppGlobal.setBooleanPreference(this,false,AppConstant.PREF_USERLOGIN);
                AppGlobal.removepref(this, AppConstant.PREF_USER_OBJ);
                finish();
                Intent ilog = new Intent(this,MainActivity.class);
                startActivity(ilog);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    private boolean checkSurvey() {
//
//        if(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefsurveytypetext).equals(""))
//        {
//            AppGlobal.showToast(PresurveyActivity.this, getResources().getString(R.string.select_service));
//            return false;
//        }
//
//        if(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefvendorstext).equals(""))
//        {
//            AppGlobal.showToast(PresurveyActivity.this, getResources().getString(R.string.select_vendor));
//            return false;
//        }
//
//        if(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Preforgtypetext).equals(""))
//        {
//            AppGlobal.showToast(PresurveyActivity.this, getResources().getString(R.string.select_organization));
//            return false;
//        }
//
//        if(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefrolestext).equals(""))
//        {
//            AppGlobal.showToast(PresurveyActivity.this, getResources().getString(R.string.select_role));
//            return false;
//        }
//
//        if(AppGlobal.getStringPreference(PresurveyActivity.this, AppConstant.Prefratetext).equals(""))
//        {
//            AppGlobal.showToast(PresurveyActivity.this, getResources().getString(R.string.select_rating));
//            return false;
//        }
//
//        return true;
//    }
}
