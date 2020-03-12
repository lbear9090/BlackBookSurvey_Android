package com.blackbook.survey;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.Constant.WsConstant;
import com.blackbook.survey.Utils.Utils;
import com.blackbook.survey.asynktask.AsyncPostService;
import com.blackbook.survey.db.DatabaseHelper;
import com.blackbook.survey.db.dbconstant;
import com.blackbook.survey.interfaces.WsResponseListener;
import com.blackbook.survey.model.ResponseObject;
import com.blackbook.survey.model.ResponseResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * Created by c119 on 04/04/16.
 *
 */
public class GetdataActivity extends BaseActivity implements WsResponseListener
{
    public DatabaseHelper db;
    public ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdata);

        initview();
    }

    private void initview()
    {
        db = new DatabaseHelper(GetdataActivity.this);
        db.openDataBase();

        filldata();
    }

    private void filldata()
    {
        if (AppGlobal.isNetwork(GetdataActivity.this)) {
            try {
                new AsyncPostService(GetdataActivity.this, getResources().getString(R.string.str_getdata), WsConstant.Req_alldata, true, false).execute(WsConstant.WS_All_DATA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            boolean datastore = AppGlobal.getBooleanPreference(GetdataActivity.this, AppConstant.Prefdatastore);
            if (!datastore)
            {
                new CsvInsertTask().execute();
            }
        }
    }

    private class CsvInsertTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            pd = new ProgressDialog(GetdataActivity.this);
            pd.setMessage("Inserting Data...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            //for surveyType
            ArrayList<String> Starr = getparsearray("Services.csv");
            Log.v("SurveytypeArray", Starr.toString());

            for(int i = 0;i < Starr.size(); i++)
            {
                if(i != 0)
                {
                    try
                    {
                        String line = Starr.get(i).trim();
                        String arrst[] = line.split(";");

                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Stid, arrst[0].trim());
                        cv.put(dbconstant.Stname, arrst[1].trim());
                        cv.put(dbconstant.Stlevel, arrst[3].trim());
                        cv.put(dbconstant.Stparntid, arrst[2].trim());
                        cv.put(dbconstant.Stcreateddate,"");
                        cv.put(dbconstant.Stmodifieddate,"");
                        cv.put(dbconstant.Stisdeleted, arrst[4].trim());

                        db.insertSingleRow(dbconstant.Table_SurveyType, cv);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //for vendors
            ArrayList<String> Vendorarr = getparsearray("Vendors.csv");
            Log.v("VendorArray", Vendorarr.toString());

            for(int i = 0;i < Vendorarr.size(); i++)
            {
                if(i != 0)
                {
                    try
                    {
                        String line = Vendorarr.get(i).trim();
                        String arrvendor[] = line.split(";");

                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Vid,arrvendor[0]);
                        cv.put(dbconstant.Vname, arrvendor[1]);
                        cv.put(dbconstant.Vcreateddate, "");
                        cv.put(dbconstant.Vmodifieddate, "");
                        cv.put(dbconstant.Visdeleted, arrvendor[2]);

                        db.insertSingleRow(dbconstant.Table_Vendors, cv);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //for OrganizationType

            ArrayList<String> organizationtypearr = getparsearray("Organizations.csv");
            Log.v("OrganizationTypeArray", organizationtypearr.toString());

            for(int i = 0;i < organizationtypearr.size(); i++)
            {
                if(i != 0)
                {
                    try
                    {
                        String line = organizationtypearr.get(i).trim();
                        String arrorganizationtype[] = line.split(";");

                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Otid, arrorganizationtype[0].trim());
                        cv.put(dbconstant.Otame, arrorganizationtype[1].trim());
                        cv.put(dbconstant.Otlevel, arrorganizationtype[3].trim());
                        cv.put(dbconstant.Otparentid, arrorganizationtype[2].trim());
                        cv.put(dbconstant.Otisoptional, arrorganizationtype[4].trim());
                        cv.put(dbconstant.Otcreateddate,"");
                        cv.put(dbconstant.Otmodifieddate,"");
                        cv.put(dbconstant.Otisdeleted, arrorganizationtype[5].trim());

                        db.insertSingleRow(dbconstant.Table_OrganizationType, cv);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //for Roles
            ArrayList<String> rolesarr = getparsearray("Roles.csv");
            Log.v("RolesArray", rolesarr.toString());

            for(int i = 0;i < rolesarr.size(); i++)
            {
                if(i != 0)
                {
                    try
                    {
                        String line = rolesarr.get(i).trim();
                        String arrroles[] = line.split(";");

                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Rid, arrroles[0].trim());
                        cv.put(dbconstant.Rname, arrroles[1].trim());
                        cv.put(dbconstant.Rlevel, arrroles[3].trim());
                        cv.put(dbconstant.Rparentid, arrroles[2].trim());
                        cv.put(dbconstant.Risoptional, arrroles[4].trim());
                        cv.put(dbconstant.Rcreateddate,"");
                        cv.put(dbconstant.Rmodifieddate,"");
                        cv.put(dbconstant.Risdeleted, arrroles[5].trim());

                        db.insertSingleRow(dbconstant.Table_Roles, cv);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //for Questions
            ArrayList<String> questionsarr = getparsearray("Questions.csv");
            Log.v("QuestionsarrArray", questionsarr.toString());

            for(int i = 0;i < questionsarr.size(); i++)
            {
                if(i != 0)
                {
                    try
                    {
                        String line = questionsarr.get(i).trim();
                        String arrquestions[] = line.split(";");

                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Qid,arrquestions[0].trim());
                        cv.put(dbconstant.Qtitle,arrquestions[1].trim());
                        if(!arrquestions[2].equalsIgnoreCase("null"))
                        {
                            cv.put(dbconstant.Qdescription,arrquestions[2].trim());
                        }
                        else
                        {
                            cv.put(dbconstant.Qdescription,"");
                        }
                        cv.put(dbconstant.Qtype,arrquestions[3].trim());
                        cv.put(dbconstant.Qformat,arrquestions[4].trim());
                        cv.put(dbconstant.Qoptioncount,arrquestions[5].trim());
                        cv.put(dbconstant.Qcreateddate,"");
                        cv.put(dbconstant.Qmodifieddate,"");
                        cv.put(dbconstant.Qisdeleted,arrquestions[6].trim());

                        db.insertSingleRow(dbconstant.Table_Question, cv);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //for ScoreMatrix
            ArrayList<String> scorematrixarr = getparsearray("ScoreMatrix.csv");
            Log.v("ScoreMatrixArray", scorematrixarr.toString());

            for(int i = 0;i < scorematrixarr.size(); i++)
            {
                if(i != 0)
                {
                    try
                    {
                        String line = scorematrixarr.get(i).trim();
                        String arrscorematrix[] = line.split(";");

                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Smid, arrscorematrix[0].trim());
                        cv.put(dbconstant.Smtitle,arrscorematrix[1].trim());
                        cv.put(dbconstant.Smdescription,arrscorematrix[2].trim());
                        cv.put(dbconstant.Smstartrange,convertodouble(arrscorematrix[3].trim()));
                        cv.put(dbconstant.Smendrange,convertodouble(arrscorematrix[4].trim()));
                        cv.put(dbconstant.Smcreateddate,"");
                        cv.put(dbconstant.Smmodifieddate,"");
                        cv.put(dbconstant.Smisdeleted,arrscorematrix[5].trim());

                        db.insertSingleRow(dbconstant.Table_ScoreMatrix, cv);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //for prefrences

            ArrayList<String> prefrencesarr = getparsearray("Preferences.csv");
            Log.v("PrefrencesArray", prefrencesarr.toString());

            for(int i = 0;i < prefrencesarr.size(); i++)
            {
                if(i != 0)
                {
                    try
                    {
                        String line = prefrencesarr.get(i).trim();
                        String arrprefrences[] = line.split(";");

                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Pid,arrprefrences[0].trim());
                        cv.put(dbconstant.Ptext,arrprefrences[1].trim());
                        cv.put(dbconstant.Pcreateddate,"");
                        cv.put(dbconstant.Pmodifieddate,"");
                        cv.put(dbconstant.Pisdeleted,arrprefrences[2].trim());

                        db.insertSingleRow(dbconstant.Table_Prefrences, cv);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            //for countrycode

            ArrayList<String> countrycodearr = getparsearray("CountryCode.csv");
            Log.v("CountryCodeArray", countrycodearr.toString());

            for(int i = 0;i < countrycodearr.size(); i++)
            {
                if(i != 0)
                {
                    try
                    {
                        String line = countrycodearr.get(i).trim();
                        String arrcountrycode[] = line.split(",");

                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Ccid,arrcountrycode[0].trim());
                        cv.put(dbconstant.Cciso2,arrcountrycode[1].trim());
                        cv.put(dbconstant.Ccshortname,arrcountrycode[2].trim());
                        cv.put(dbconstant.Cclongname,arrcountrycode[3].trim());
                        cv.put(dbconstant.Cciso3,arrcountrycode[4].trim());
                        cv.put(dbconstant.Cccallingcode,arrcountrycode[5].trim());

                        db.insertSingleRow(dbconstant.Table_Countrycode, cv);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            pd.dismiss();
            AppGlobal.setBooleanPreference(GetdataActivity.this, true, AppConstant.Prefdatastore);
            Intent i = new Intent(GetdataActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private class InsertTask extends AsyncTask<Void, Void, Void> {
        private ResponseObject resObj;

        private InsertTask(ResponseObject resObj) {
            this.resObj = resObj;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(GetdataActivity.this);
            pd.setMessage("Inserting Data...");
            pd.setIndeterminate(true);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            int srveytypesize = resObj.getArr_surveytype().size();
            if (srveytypesize > 0) {
                try {
                    for (int i = 0; i < srveytypesize; i++) {
                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Stid, resObj.getArr_surveytype().get(i).getId());
                        cv.put(dbconstant.Stname, resObj.getArr_surveytype().get(i).getSurvey_type_name());
                        cv.put(dbconstant.Stlevel, resObj.getArr_surveytype().get(i).getLevel());
                        cv.put(dbconstant.Stparntid, resObj.getArr_surveytype().get(i).getParent_id());
                        cv.put(dbconstant.Stcreateddate, resObj.getArr_surveytype().get(i).getCreated_date());
                        cv.put(dbconstant.Stmodifieddate, resObj.getArr_surveytype().get(i).getModified_date());
                        cv.put(dbconstant.Stisdeleted, resObj.getArr_surveytype().get(i).getIs_deleted());

                        db.insertSingleRow(dbconstant.Table_SurveyType, cv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Log.i(Utils.TAG, "Error inserting into table: " + e.getMessage());
                }
            }

            int vendorsize = resObj.getArr_vendors().size();

            Log.i(Utils.TAG, "Found this many vendorsize: " + vendorsize);
            if (vendorsize > 0) {
                try {
                    for (int i = 0; i < vendorsize; i++) {
                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Vid, resObj.getArr_vendors().get(i).getId());
                        cv.put(dbconstant.Vname, resObj.getArr_vendors().get(i).getVendor_name());
                        cv.put(dbconstant.Vcreateddate, resObj.getArr_vendors().get(i).getCreated_date());
                        cv.put(dbconstant.Vmodifieddate, resObj.getArr_vendors().get(i).getModified_date());
                        cv.put(dbconstant.Visdeleted, resObj.getArr_vendors().get(i).getIs_deleted());

                        db.insertSingleRow(dbconstant.Table_Vendors, cv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Log.i(Utils.TAG, "Error inserting into table: " + e.getMessage());
                }
            }

            int ratesize = resObj.getArr_rates().size();

            Log.i(Utils.TAG, "Found this many ratesize: " + ratesize);
            if (ratesize > 0) {
                try {
                    for (int i = 0; i < ratesize; i++) {
                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Rateid, resObj.getArr_rates().get(i).getId());
                        cv.put(dbconstant.Ratename, resObj.getArr_rates().get(i).getRate_name());
                        cv.put(dbconstant.Ratecreateddate, resObj.getArr_rates().get(i).getCreated_date());
                        cv.put(dbconstant.Ratemodifieddate, resObj.getArr_rates().get(i).getModified_date());
                        cv.put(dbconstant.Rateisdeleted, resObj.getArr_rates().get(i).getIs_deleted());

                        db.insertSingleRow(dbconstant.Table_Rates, cv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Log.i(Utils.TAG, "Error inserting into table: " + e.getMessage());
                }
            }

            int organizationtypesize = resObj.getArr_organizationtype().size();

            Log.i(Utils.TAG, "Found this many organizationtypesize: " + organizationtypesize);
            if (organizationtypesize > 0) {
                try {
                    for (int i = 0; i < organizationtypesize; i++) {
                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Otid, resObj.getArr_organizationtype().get(i).getId());
                        cv.put(dbconstant.Otame, resObj.getArr_organizationtype().get(i).getOrganization_type_name());
                        cv.put(dbconstant.Otlevel, resObj.getArr_organizationtype().get(i).getLevel());
                        cv.put(dbconstant.Otparentid, resObj.getArr_organizationtype().get(i).getParent_id());
                        cv.put(dbconstant.Otisoptional, resObj.getArr_organizationtype().get(i).getIs_optional());
                        cv.put(dbconstant.Otcreateddate, resObj.getArr_organizationtype().get(i).getCreated_date());
                        cv.put(dbconstant.Otmodifieddate, resObj.getArr_organizationtype().get(i).getModified_date());
                        cv.put(dbconstant.Otisdeleted, resObj.getArr_organizationtype().get(i).getIs_deleted());

                        db.insertSingleRow(dbconstant.Table_OrganizationType, cv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Log.i(Utils.TAG, "Error inserting into table: " + e.getMessage());
                }
            }

            int rolessize = resObj.getArr_roles().size();
            Log.i(Utils.TAG, "Found this many rolessize: " + rolessize);

            if (rolessize > 0) {
                try {
                    for (int i = 0; i < rolessize; i++) {
                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Rid, resObj.getArr_roles().get(i).getId());
                        cv.put(dbconstant.Rname, resObj.getArr_roles().get(i).getRole_name());
                        cv.put(dbconstant.Rlevel, resObj.getArr_roles().get(i).getLevel());
                        cv.put(dbconstant.Rparentid, resObj.getArr_roles().get(i).getParent_id());
                        cv.put(dbconstant.Risoptional, resObj.getArr_roles().get(i).getIs_optional());
                        cv.put(dbconstant.Rcreateddate, resObj.getArr_roles().get(i).getCreated_date());
                        cv.put(dbconstant.Rmodifieddate, resObj.getArr_roles().get(i).getModified_date());
                        cv.put(dbconstant.Risdeleted, resObj.getArr_roles().get(i).getIs_deleted());

                        db.insertSingleRow(dbconstant.Table_Roles, cv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Log.i(Utils.TAG, "Error inserting into table: " + e.getMessage());
                }
            }

            int questionssize = resObj.getArr_questions().size();
            Log.i(Utils.TAG, "Found this many questionssize: " + questionssize);

            if (questionssize > 0) {
                try {
                    for (int i = 0; i < questionssize; i++) {
                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Qid, resObj.getArr_questions().get(i).getId());
                        cv.put(dbconstant.Qtitle, resObj.getArr_questions().get(i).getQuestion_title());
                        cv.put(dbconstant.Qdescription, (resObj.getArr_questions().get(i).getQuestion_description() == null) ? "" : resObj.getArr_questions().get(i).getQuestion_description());
                        cv.put(dbconstant.Qtype, resObj.getArr_questions().get(i).getQuestion_type());
                        cv.put(dbconstant.Qformat, resObj.getArr_questions().get(i).getQuestion_format());
                        cv.put(dbconstant.Qoptioncount, resObj.getArr_questions().get(i).getOption_count());
                        cv.put(dbconstant.Qcreateddate, resObj.getArr_questions().get(i).getCreated_date());
                        cv.put(dbconstant.Qmodifieddate, resObj.getArr_questions().get(i).getModified_date());
                        cv.put(dbconstant.Qisdeleted, resObj.getArr_questions().get(i).getIs_deleted());

                        db.insertSingleRow(dbconstant.Table_Question, cv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Log.i(Utils.TAG, "Error inserting into table: " + e.getMessage());
                }
            }

            int scorematrixsize = resObj.getArr_scorematrix().size();
            Log.i(Utils.TAG, "Found this many scorematrixsize: " + scorematrixsize);

            if (scorematrixsize > 0) {
                try {
                    for (int i = 0; i < scorematrixsize; i++) {
                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Smid, resObj.getArr_scorematrix().get(i).getId());
                        cv.put(dbconstant.Smtitle, resObj.getArr_scorematrix().get(i).getMatrix_title());
                        cv.put(dbconstant.Smdescription, resObj.getArr_scorematrix().get(i).getMatrix_description());
                        cv.put(dbconstant.Smstartrange, convertodouble(resObj.getArr_scorematrix().get(i).getStart_range()));
                        cv.put(dbconstant.Smendrange, convertodouble(resObj.getArr_scorematrix().get(i).getEnd_range()));
                        cv.put(dbconstant.Smcreateddate, resObj.getArr_scorematrix().get(i).getCreated_date());
                        cv.put(dbconstant.Smmodifieddate, resObj.getArr_scorematrix().get(i).getModified_date());
                        cv.put(dbconstant.Smisdeleted, resObj.getArr_scorematrix().get(i).getIs_deleted());

                        db.insertSingleRow(dbconstant.Table_ScoreMatrix, cv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int preferencessize = resObj.getArr_preferences().size();
            Log.i(Utils.TAG, "Found this many preferencessize: " + preferencessize);
            if (preferencessize > 0) {
                try {
                    for (int i = 0; i < preferencessize; i++) {
                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Pid, resObj.getArr_preferences().get(i).getId());
                        cv.put(dbconstant.Ptext, resObj.getArr_preferences().get(i).getPreference_text());
                        cv.put(dbconstant.Pcreateddate, resObj.getArr_preferences().get(i).getCreated_date());
                        cv.put(dbconstant.Pmodifieddate, resObj.getArr_preferences().get(i).getModified_date());
                        cv.put(dbconstant.Pisdeleted, resObj.getArr_preferences().get(i).getIs_deleted());

                        db.insertSingleRow(dbconstant.Table_Prefrences, cv);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //for countrycode

            ArrayList<String> countrycodearr = getparsearray("CountryCode.csv");
            Log.i(Utils.TAG, "countrycodearr fom csv: " + countrycodearr.toString());

            for(int i = 0;i < countrycodearr.size(); i++)
            {
                if(i != 0)
                {
                    try
                    {
                        String line = countrycodearr.get(i).trim();
                        String arrcountrycode[] = line.split(",");

                        ContentValues cv = new ContentValues();

                        cv.put(dbconstant.Ccid,arrcountrycode[0].trim());
                        cv.put(dbconstant.Cciso2,arrcountrycode[1].trim());
                        cv.put(dbconstant.Ccshortname,arrcountrycode[2].trim());
                        cv.put(dbconstant.Cclongname,arrcountrycode[3].trim());
                        cv.put(dbconstant.Cciso3,arrcountrycode[4].trim());
                        cv.put(dbconstant.Cccallingcode,arrcountrycode[5].trim());

                        db.insertSingleRow(dbconstant.Table_Countrycode, cv);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pd.dismiss();
            AppGlobal.setBooleanPreference(GetdataActivity.this, true, AppConstant.Prefdatastore);
            Intent i = new Intent(GetdataActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public ArrayList<String> getparsearray(String csvfile)
    {
        ArrayList<String> allLinedStringsList = null;

        try
        {
            InputStream is = getResources().getAssets().open(csvfile);

            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));

            StringBuilder builder = new StringBuilder();
            String aux = "";

            while ((aux = buffer.readLine()) != null) {
                builder.append(aux + "\n");
            }

            buffer.close();
            String fileContent = builder.toString();
            fileContent.trim();

            String allLinedStrings[] = fileContent.split("\n");

            allLinedStringsList = new ArrayList<String>(Arrays.asList(allLinedStrings));

            for (String string : allLinedStringsList) {
                if (string == null && string.length() == 0) {
                    allLinedStringsList.remove(string);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return allLinedStringsList;
    }

    public double convertodouble(String val)
    {
        double d = Double.parseDouble(val);
        DecimalFormat df = new DecimalFormat("0.00");
        String dx = df.format(d);
        d=Double.valueOf(dx);
        return d;
    }

    @Override
    public void onDelieverResponse(String serviceType, Object data, Exception error)
    {
        if (error == null)
        {

            if (serviceType.equalsIgnoreCase(WsConstant.Req_alldata))
            {
                ResponseObject resObj = ((ResponseResult) data).getResult();
                if(resObj.getStatus().equalsIgnoreCase("DONE"))
                {
                    new InsertTask(resObj).execute();
                }
            }
        }
    }
}
