package com.blackbook.survey.PresurveyList;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackbook.survey.BaseActivity;
import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.R;
import com.blackbook.survey.Utils.Utils;
import com.blackbook.survey.adapter.IconTreeItemHolder;
import com.blackbook.survey.db.DatabaseHelper;
import com.blackbook.survey.model.OrganizationType;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;

/**
 *
 * Created by c119 on 29/03/16.
 *
 */
public class OrganizationActivity extends BaseActivity
{
    public DatabaseHelper db;

    public RelativeLayout containerView;
    public ArrayList<OrganizationType> arr_parentorg, arr_middleorg, arr_endorg;
    public EditText edtother;
    boolean edtopen;

    //previousnode clears the checkboxes for the parent nodes.
    public TreeNode root, previousnode;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);
        setTitle("Organizations");
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        previousnode = null;
        edtopen = false;

        initview();
    }

    private void initview()
    {
        db = new DatabaseHelper(OrganizationActivity.this);
        db.openDataBase();

        containerView = (RelativeLayout)findViewById(R.id.container);

        edtother = (EditText)findViewById(R.id.edt_other);
        edtother.setVisibility(View.GONE);

        edtother.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0)
            {
                String text = edtother.getText().toString();
                if(text.length() <= 0 || text.equals(" "))
                {
                    AppGlobal.setStringPreference(OrganizationActivity.this, "Other - "+text, AppConstant.Preforgtypetext);
                    AppGlobal.setStringPreference(OrganizationActivity.this, "10", AppConstant.PreforgtypeId);
                }

                if(text.length() == 0)
                {
                    AppGlobal.setStringPreference(OrganizationActivity.this, "", AppConstant.Preforgtypetext);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
            {

            }
        });

        prepareListData();
    }

    private void prepareListData()
    {
        root = TreeNode.root();
        arr_parentorg = db.Getorganizationdata("0");

        //loop through the array of organizations
        for(int i = 0; i < arr_parentorg.size(); i++)
        {
            //Create the root node.
            TreeNode topRoot = new TreeNode(new IconTreeItemHolder.IconTreeItem(arr_parentorg.get(i).getOrganization_type_name(), arr_parentorg.get(i).getId()));

            //Check if it's other.
            if(!arr_parentorg.get(i).getOrganization_type_name().equalsIgnoreCase("other"))
            {
                //if it's not other....
                int subchildsize = db.getorgsubchid(arr_parentorg.get(i).getId());

                if(subchildsize > 0)
                {
                    //Now get all the mid level children.
                    arr_middleorg = db.Getorganizationdata(arr_parentorg.get(i).getId());

                    //Loop through all the mid level children
                    for(int j=0 ;j < arr_middleorg.size() ; j++)
                    {
                        //Add those children to the tree.
                        TreeNode meddleroot = new TreeNode(new IconTreeItemHolder.IconTreeItem(arr_middleorg.get(j).getOrganization_type_name(), arr_middleorg.get(j).getId()));

                        //Now get all the low level children.
                        int childsize = db.getorgsubchid(arr_middleorg.get(j).getId());

                        //Loop through all the low level children
                        if(childsize > 0)
                        {

                            // Add the low level children to the list.
                            arr_endorg = db.Getorganizationdata(arr_middleorg.get(j).getId());
                            for(int k = 0; k < arr_endorg.size() ;k++)
                            {
                                TreeNode endroot = new TreeNode(new IconTreeItemHolder.IconTreeItem(arr_endorg.get(k).getOrganization_type_name(), arr_endorg.get(k).getId()));
                                meddleroot.addChildren(endroot);
                            }
                        }

                        topRoot.addChildren(meddleroot);
                    }
                }
            }

            root.addChildren(topRoot);
        }

        AndroidTreeView tView = new AndroidTreeView(this,root);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setDefaultViewHolder(IconTreeItemHolder.class);
        tView.setDefaultNodeClickListener(nodeClickListener);

        containerView.addView(tView.getView());
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener()
    {
        @Override
        public void onClick(TreeNode node, Object value)
        {

            String ID = null;
            //Clear the check from the list to only show the last item checked.
            if(previousnode != null)
            {
                View pv = previousnode.getViewHolder().getView().findViewById(R.id.img_check);
                if(pv instanceof ImageView)
                {
                    pv.setVisibility(View.INVISIBLE);
                }
            }

            View view = node.getViewHolder().getView().findViewById(R.id.eventsListEventRowText);
            if (view instanceof TextView) {
                ID = view.getTag(R.string.OrgID).toString();
            }

            //Now lets get the selected item.
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            String selectedval = node.getPath();

            if(item.text.equalsIgnoreCase("other") && !selectedval.contains(":"))
            {
                if(edtopen)
                {
                    edtother.setVisibility(View.GONE);
                    edtopen = false;
                }
                else
                {
                    edtother.setVisibility(View.VISIBLE);
                    edtopen = true;
                }

                return;
            }

            StringBuilder sb = new StringBuilder();

            //We have a multi level selection, either 2 or 3 levels deep.
            if (selectedval.contains(":")) {

                //This seperator just tells us how many levels deep to go.
                String[] separated = selectedval.split(":");
                String  nameOne = null,
                        nameTwo = null,
                        nameThree = null;

                Log.i(Utils.TAG, "Number of levels deep: " + separated.length);

                for (int x = 0; x < separated.length; x++) {
                    Log.i(Utils.TAG, "Processing level: " + x);

                    OrganizationType ot = new OrganizationType();

                    //Lets work our way down.
                    ot = db.GetParent(ID);

                    Log.i(Utils.TAG, "Found name: " + ot.getOrganization_type_name());
                    Log.i(Utils.TAG, "Found ID: " + ot.getId());

                    if (x == 0) {
                        nameOne = ot.getOrganization_type_name();
                    } else if (x == 1) {
                        nameTwo = ot.getOrganization_type_name();
                    } else if (x == 2) {
                        nameThree = ot.getOrganization_type_name();
                    }


                    if (x == 0) {
                        AppGlobal.setStringPreference(OrganizationActivity.this, ot.getId(), AppConstant.PreforgtypeId);
                    }

                    ID = ot.getParent_id();
                }

                Log.i(Utils.TAG, "nameThree: " + nameThree);
                Log.i(Utils.TAG, "nameTwo: " + nameTwo);
                Log.i(Utils.TAG, "nameOne: " + nameOne);

                if (nameThree != null) {
                    sb.append(nameThree);
                    sb.append("->");
                }

                sb.append(nameTwo);
                sb.append("->");

                sb.append(nameOne);

                AppGlobal.setStringPreference(OrganizationActivity.this, sb.toString(), AppConstant.Preforgtypetext);

            } else {
                //This is for a top level item only selection.
                OrganizationType ot = db.GetOrganizationtypename(item.text);

                sb.append(ot.getOrganization_type_name());

                AppGlobal.setStringPreference(OrganizationActivity.this, ot.getOrganization_type_name(), AppConstant.Preforgtypetext);
                AppGlobal.setStringPreference(OrganizationActivity.this, ot.getId(), AppConstant.PreforgtypeId);
            }


            View v = node.getViewHolder().getView().findViewById(R.id.img_check);
            if (v instanceof ImageView) {
                v.setVisibility(View.VISIBLE);
                previousnode = node;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}