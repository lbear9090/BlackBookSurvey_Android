package com.blackbook.survey.PresurveyList;

import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.blackbook.survey.BaseActivity;
import com.blackbook.survey.R;
import com.blackbook.survey.adapter.ExpandableRoleListAdapter;
import com.blackbook.survey.db.DatabaseHelper;
import com.blackbook.survey.interfaces.ExpandableListner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by c119 on 29/03/16.
 *
 */
public class RoleActivity extends BaseActivity implements ExpandableListner
{
    public DatabaseHelper db;

    public ExpandableListView expListView;
    public List<String> listDataHeader;
    public HashMap<String, List<String>> listDataChild;
    public ExpandableRoleListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initview();
    }

    private void initview() {
        db = new DatabaseHelper(RoleActivity.this);
        db.openDataBase();

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setGroupIndicator(null);

        prepareListData();
    }

    private void prepareListData()
    {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader = db.GetaRoleDataparentid("0");
        int arrsize = listDataHeader.size();

        List<List<String>> dataList = new ArrayList<>();

        for (int i = 1; i <= arrsize; i++) {
            List<String> tempList = db.GetaRoleDataparentid(String.valueOf(i));
            dataList.add(tempList);
        }

        for (int i = 0; i < arrsize; i++) {
            listDataChild.put(listDataHeader.get(i), dataList.get(i));
        }

        listAdapter = new ExpandableRoleListAdapter(RoleActivity.this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

    }

    @Override
    public void onGroupClicked(int group_position, boolean isExpanded)
    {
        if (expListView.isGroupExpanded(group_position))
        {
            expListView.collapseGroup(group_position);
        }
        else
        {
            expListView.expandGroup(group_position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(RoleActivity.this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
