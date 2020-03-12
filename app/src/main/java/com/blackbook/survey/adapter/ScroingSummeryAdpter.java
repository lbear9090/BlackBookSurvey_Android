package com.blackbook.survey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blackbook.survey.BaseActivity;
import com.blackbook.survey.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by c119 on 13/04/16.
 *
 */
public class ScroingSummeryAdpter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<String> listarray;
    private LayoutInflater layoutInflater;
    private List<String> answerarr;

    public ScroingSummeryAdpter(Context context, ArrayList<String> questionlist,List<String> answerlist)
    {
        mContext = context;
        listarray = questionlist;
        answerarr = answerlist;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return listarray.size();
    }

    @Override
    public Object getItem(int position)
    {
        return listarray.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.row_summery, null);
            holder = new ViewHolder();
            holder.realmain = (RelativeLayout) convertView.findViewById(R.id.rela_main);
            holder.txtquestion = (TextView) convertView.findViewById(R.id.txt_question);
            holder.txtquestion.setTypeface(BaseActivity.Sufi_Regular);
            holder.points = (TextView) convertView.findViewById(R.id.btn_point);
            holder.points.setTypeface(BaseActivity.Sufi_Regular);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position % 2 == 1)
        {
            holder.realmain.setBackgroundColor(mContext.getResources().getColor(R.color.color_white));
        }
        else
        {
            holder.realmain.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
        }

        holder.txtquestion.setText(position+1+")"+" "+listarray.get(position));
        holder.points.setText(answerarr.get(position));

        return convertView;
    }

    private class ViewHolder
    {
        TextView txtquestion;
        RelativeLayout realmain;
        TextView points;
    }

}
