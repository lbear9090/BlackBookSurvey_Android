package com.blackbook.survey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 *
 * Created by Bogdan Melnychuk on 2/12/15.
 *
 */
public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem>
{
    public IconTreeItemHolder(Context context)
    {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node,final IconTreeItem value)
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.row_first, null, false);
        TextView tvValue = (TextView) view.findViewById(R.id.eventsListEventRowText);
        ImageView imgcheck = (ImageView) view.findViewById(R.id.img_check);
        tvValue.setText(value.text);
        tvValue.setTag(R.string.OrgID, value.ID);

        return view;
    }


    public static class IconTreeItem
    {
        public String text;
        public String ID;

        public IconTreeItem(String text, String ID) {
            this.text = text;
            this.ID = ID;
        }
    }
}
