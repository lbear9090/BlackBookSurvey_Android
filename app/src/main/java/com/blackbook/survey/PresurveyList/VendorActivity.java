package com.blackbook.survey.PresurveyList;

import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.blackbook.survey.BaseActivity;
import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.R;
import com.blackbook.survey.Utils.IndexableListView;
import com.blackbook.survey.adapter.VendorAdapter;
import com.blackbook.survey.db.DatabaseHelper;

import java.util.List;

public class VendorActivity extends BaseActivity
{
    private DatabaseHelper db;
    private IndexableListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(VendorActivity.this);
        db.openDataBase();

        mListView = (IndexableListView) findViewById(R.id.list);
        mListView.setTextFilterEnabled(true);

        List<String> countries = populateCountries();
        VendorAdapter adapter = new VendorAdapter(this,R.layout.row_vendor_item, countries);
        mListView.setAdapter(adapter);
        mListView.setFastScrollEnabled(true);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppGlobal.setStringPreference(VendorActivity.this, mListView.getItemAtPosition(position).toString(), AppConstant.Prefvendorstext);
                finish();
            }
        });
    }

    private List<String> populateCountries()
    {
        List<String> countries;
        countries = db.GetAllVendors();
        return countries;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(VendorActivity.this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}