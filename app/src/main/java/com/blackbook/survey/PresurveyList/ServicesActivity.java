package com.blackbook.survey.PresurveyList;

import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.blackbook.survey.BaseActivity;
import com.blackbook.survey.R;
import com.blackbook.survey.adapter.ExpandableListAdapter;
import com.blackbook.survey.db.DatabaseHelper;
import com.blackbook.survey.interfaces.ExpandableListner;
import com.blackbook.survey.model.SurveyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by c119 on 29/03/16.
 *
 */
public class ServicesActivity extends BaseActivity implements ExpandableListner
{
    private DatabaseHelper db;
    private ExpandableListView expListView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        setTitle("Services");
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
        ArrayList<SurveyType> listDataHeader;
        HashMap<String, List<String>> listDataChild = new HashMap<>();

        listDataHeader = db.GetaAllSurviceType("0");
        int arrsize = listDataHeader.size();

        List<List<String>> dataList = new ArrayList<>();

        for(int i = 0; i < arrsize; i++)
        {
            List<String> tempList = db.GetaDataparentid(listDataHeader.get(i).getId());
            Collections.sort(tempList, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
            dataList.add(tempList);
        }

        for(int i = 0 ; i < arrsize ; i++)
        {
            listDataChild.put(listDataHeader.get(i).getSurvey_type_name(), dataList.get(i));
        }

        Collections.sort(listDataHeader, new Comparator<SurveyType>() {
            @Override
            public int compare(SurveyType o1, SurveyType o2) {
                return o1.getSurvey_type_name().compareToIgnoreCase(o2.getSurvey_type_name());
            }
        });
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

    @Override
    public void onGroupClicked(int group_position, boolean isExpanded) {
        if (expListView.isGroupExpanded(group_position))
        {
            expListView.collapseGroup(group_position);
        }
        else
        {
            expListView.expandGroup(group_position);
        }
    }
}
