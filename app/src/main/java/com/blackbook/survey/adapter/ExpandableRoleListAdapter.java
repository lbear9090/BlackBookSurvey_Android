package com.blackbook.survey.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackbook.survey.BaseActivity;
import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.R;
import com.blackbook.survey.db.DatabaseHelper;
import com.blackbook.survey.interfaces.ExpandableListner;

import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by c119 on 12/04/16.
 *
 */
public class ExpandableRoleListAdapter extends BaseExpandableListAdapter
{
    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<String, List<String>> _listDataChild;
    private int selected_grp = -1, selected_child = -1;
    private ExpandableListner expandlistner;
    public DatabaseHelper db;

    public ExpandableRoleListAdapter(Context context, List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.expandlistner = (ExpandableListner) context;

        db = new DatabaseHelper(_context);
        db.openDataBase();
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent)
    {
        GroupViewHolder groupViewHolder;

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

            groupViewHolder = new GroupViewHolder();
            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.lblListHeader);
            groupViewHolder.mGroupText.setTypeface(BaseActivity.Sufi_Regular);
            groupViewHolder.mparentImg = (ImageView) convertView.findViewById(R.id.imgparent);

            groupViewHolder.mGroupText.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = (int) v.getTag();

                    if(getChildrenCount(pos)>0)
                    {
                        expandlistner.onGroupClicked(pos,isExpanded);
                    }
                    else
                    {
                        selected_grp = pos;
                        String vv = (String) getGroup(selected_grp);
                        AppGlobal.setStringPreference(_context, vv, AppConstant.Prefrolestext);

                        String parentid = db.Getroleparentid(vv);
                        AppGlobal.setStringPreference(_context,parentid,AppConstant.PrefrolesId);
                        //AppGlobal.showToast(_context,parentid);

                        notifyDataSetChanged();
                        ((Activity) _context).finish();
                    }
                }
            });

            convertView.setTag(groupViewHolder);
        }
        else
        {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }


        if(groupPosition==selected_grp)
        {
            if(getChildrenCount(groupPosition)>0)
            {
                groupViewHolder.mparentImg.setVisibility(View.INVISIBLE);
            }
            else
            {
                groupViewHolder.mparentImg.setVisibility(View.VISIBLE);
            }

        }
        else
        {

            groupViewHolder.mparentImg.setVisibility(View.INVISIBLE);
        }

        groupViewHolder.mGroupText.setText(headerTitle);
        groupViewHolder.mGroupText.setTag(groupPosition);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        ChildViewHolder childViewHolder;

        final String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, null);

            childViewHolder = new ChildViewHolder();
            childViewHolder.mChildText = (TextView) convertView.findViewById(R.id.lblListItem);
            childViewHolder.mChildText.setTypeface(BaseActivity.Sufi_Regular);
            childViewHolder.mCheckImg = (ImageView) convertView.findViewById(R.id.imgcheck);

            convertView.setTag(childViewHolder);
            childViewHolder.mChildText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String) v.getTag();
                    String[] arr = tag.split(",");
                    int grp = Integer.parseInt(arr[0]);
                    int childp = Integer.parseInt(arr[1]);

                    selected_grp = grp;
                    selected_child = childp;

                    String vv = (String) getGroup(selected_grp);
                    String vv1 = (String) getChild(grp, childp);
                    AppGlobal.setStringPreference(_context,vv+"->"+vv1,AppConstant.Prefrolestext);

                    String parentid = db.Getroleparentid(vv);
                    String childid = db.Getrolechildid(vv1,parentid);

                    AppGlobal.setStringPreference(_context,childid,AppConstant.PrefrolesId);
                    //AppGlobal.showToast(_context,childid);

                    notifyDataSetChanged();

                    ((Activity) _context).finish();
                }
            });
        }
        else
        {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }


        if (groupPosition == selected_grp && childPosition == selected_child)
        {
            childViewHolder.mCheckImg.setVisibility(View.VISIBLE);
        }
        else
        {
            childViewHolder.mCheckImg.setVisibility(View.INVISIBLE);
        }

        childViewHolder.mChildText.setText(childText);
        childViewHolder.mChildText.setTag(groupPosition + "," + childPosition);

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public final class ChildViewHolder
    {
        TextView mChildText;
        ImageView mCheckImg;
    }

    public final class GroupViewHolder
    {
        TextView mGroupText;
        ImageView mparentImg;
    }

}
