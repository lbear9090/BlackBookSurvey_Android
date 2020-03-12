package com.blackbook.survey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.blackbook.survey.BaseActivity;
import com.blackbook.survey.R;
import com.blackbook.survey.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * Created by c119 on 15/04/16.
 *
 */
public class AlphabetCodeAdapter extends BaseAdapter
{
    private List<Row> rows;
    private List<Row> filterArray;
    private DatabaseHelper db;
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;
    private int selected_grp=-1;

    public AlphabetCodeAdapter(Context ctx)
    {
        db = new DatabaseHelper(ctx);
        db.openDataBase();
    }

    public static abstract class Row {

    }

    public static final class Section extends Row {
        public final String text;

        public Section(String text) {
            this.text = text;
        }
    }

    public static final class Item extends Row {
        public final String text;

        public Item(String text) {
            this.text = text;
        }
    }


    public void setRows(List<Row> rows)
    {
        this.rows = rows;
        filterArray = new ArrayList<>();
        filterArray.addAll(rows);
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public Row getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof Section) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = convertView;

        if(getItemViewType(position) == 0)
        { // Item
            if (view == null)
            {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row_code_item, parent, false);

                childViewHolder = new ChildViewHolder();
                childViewHolder.textView = (TextView) view.findViewById(R.id.textView1);
                childViewHolder.textcode = (TextView) view.findViewById(R.id.txtcode);
                childViewHolder.textView.setTypeface(BaseActivity.Sufi_Regular);
                childViewHolder.textcode.setTypeface(BaseActivity.Sufi_Regular);
                childViewHolder.imgcheck = (ImageView) view.findViewById(R.id.imgcheck);

                view.setTag(childViewHolder);

                childViewHolder.textView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        selected_grp = (int) v.getTag();
                        notifyDataSetChanged();
                    }
                });
            }
            else
            {
                childViewHolder = (ChildViewHolder)view.getTag();
            }


        }
        else
        { // Section
            if (view == null)
            {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row_code_section, parent, false);

                groupViewHolder = new GroupViewHolder();
                groupViewHolder.mGroupText = (TextView) view.findViewById(R.id.textView1);
                groupViewHolder.mGroupText.setTypeface(BaseActivity.Sufi_Regular);

                view.setTag(groupViewHolder);
            }
            else
            {
                groupViewHolder = (GroupViewHolder) view.getTag();
            }


        }

        if(getItemViewType(position) == 0)
        {
            Item item = (Item) getItem(position);
            childViewHolder.textView.setText(item.text);
            String textCode = "+" + db.getCountrycode(item.text.trim());
            childViewHolder.textcode.setText(textCode);
            childViewHolder.textView.setTag(position);

            if(position == selected_grp)
            {
                childViewHolder.imgcheck.setVisibility(View.VISIBLE);
            }
            else
            {
                childViewHolder.imgcheck.setVisibility(View.INVISIBLE);
            }

        }
        else
        {
            Section section = (Section) getItem(position);
            groupViewHolder.mGroupText.setText(section.text);
        }

        return view;
    }

    private final class ChildViewHolder
    {
        TextView textView,textcode;
        ImageView imgcheck;
    }

    private final class GroupViewHolder
    {
        TextView mGroupText;
    }


    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        rows.clear();

        if (charText.length() == 0)
        {
            rows.addAll(filterArray);
            selected_grp = -1;
        }
        else
        {
            String firstLetter;
            String previousLetter=null;
            firstLetter = charText.substring(0, 1);

            for (Row obj : filterArray)
            {
                if (obj instanceof Item)
                {
                    AlphabetCodeAdapter.Item i1 = (AlphabetCodeAdapter.Item) obj;
                    String searchtxt = i1.text;
                    if (searchtxt.toLowerCase(Locale.getDefault()).startsWith(charText))
                    {
                        if(!firstLetter.equalsIgnoreCase(previousLetter))
                            rows.add(new AlphabetCodeAdapter.Section(firstLetter.toUpperCase()));
                        rows.add(obj);
                        previousLetter = firstLetter;
                    }
                }

            }

        }
        notifyDataSetChanged();
    }
}
