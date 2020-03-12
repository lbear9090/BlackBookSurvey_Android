package com.blackbook.survey.PresurveyList;

import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.blackbook.survey.BaseActivity;
import com.blackbook.survey.R;
import com.blackbook.survey.adapter.ExpandableListAdapter;
import com.blackbook.survey.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by c119 on 29/03/16.
 *
 */
public class ServicesActivity extends BaseActivity
{
    private DatabaseHelper db;
    private ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initview();
    }

    private void initview()
    {
        db = new DatabaseHelper(ServicesActivity.this);
        db.openDataBase();

        expListView = (ExpandableListView)findViewById(R.id.lvExp);
        expListView.setGroupIndicator(null);

        prepareListData();
    }

    private void prepareListData()
    {
        List<String> listDataHeader;
        HashMap<String, List<String>> listDataChild = new HashMap<>();

        listDataHeader = db.GetaDataparentid("0");
        int arrsize = listDataHeader.size();

        List<List<String>> dataList = new ArrayList<>();

        for(int i = 1; i <= arrsize; i++)
        {
            List<String> tempList = db.GetaDataparentid(String.valueOf(i));
            dataList.add(tempList);
        }

        for(int i = 0 ; i < arrsize ; i++)
        {
            listDataChild.put(listDataHeader.get(i), dataList.get(i));
        }

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(ServicesActivity.this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(ServicesActivity.this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
