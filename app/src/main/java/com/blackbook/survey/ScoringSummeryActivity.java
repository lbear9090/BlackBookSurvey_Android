package com.blackbook.survey;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.adapter.ScroingSummeryAdpter;
import com.blackbook.survey.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by c119 on 02/04/16.
 *
 */
public class ScoringSummeryActivity extends BaseActivity
{
    public ListView listsummery;
    public DatabaseHelper db;
    public ArrayList<String> arrquestions;
    public ScroingSummeryAdpter ssa;
    public String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoringsummery);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            newString = extras.getString("AnswerArray");
            if (newString != null) {
                newString = newString.replaceAll("[\\[\\](){}]", "");
            }
        }

        initview();
    }

    private void initview()
    {
        db = new DatabaseHelper(ScoringSummeryActivity.this);
        db.openDataBase();

        listsummery = (ListView)findViewById(R.id.list);

        getquestions();

    }

    private void getquestions()
    {
        List<String> items = Arrays.asList(newString.split("\\s*,\\s*"));
        arrquestions = db.GetQuestions("KPI");
        ssa = new ScroingSummeryAdpter(this,arrquestions,items);
        listsummery.setAdapter(ssa);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(resultCode)
        {
            case 1001:
                setResult(AppConstant.RESULT_CLOSE_ALL);
                finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_next, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

            case R.id.next:
                Intent i = new Intent(this,LoyaltyQuestionsActivity.class);
                i.putExtra("AnswerArray",newString);
                startActivityForResult(i, 0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
