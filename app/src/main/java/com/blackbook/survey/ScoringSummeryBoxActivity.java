package com.blackbook.survey;

import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.blackbook.survey.db.DatabaseHelper;

import java.text.DecimalFormat;

/**
 *
 * Created by c119 on 12/04/16.
 *
 */
public class ScoringSummeryBoxActivity extends BaseActivity
{
    public TextView txt579, txt732, txt870, txt1000;
    public TextView txtdeal, txtdealsummery, txtneutral, txtneutralsummery, txtsatisfactory, txtsatisfactorysummery,
            txtoverwhelming, txtoverwhelmingsummery;
    public DatabaseHelper db;
    public DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoringsummerybox);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initview();
    }

    private void initview()
    {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(ScoringSummeryBoxActivity.this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
