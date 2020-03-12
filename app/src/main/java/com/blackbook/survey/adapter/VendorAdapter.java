package com.blackbook.survey.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;

import com.blackbook.survey.Utils.StringMatcher;

import java.util.List;

/**
 *
 * Created by jcaruso on 11/8/2017.
 *
 */

public class VendorAdapter extends ArrayAdapter<String> implements SectionIndexer {

    private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public VendorAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public int getPositionForSection(int section) {
        // If there is no item for current section, previous section will be selected
        for (int i = section; i >= 0; i--) {
            for (int j = 0; j < getCount(); j++) {
                if (i == 0) {
                    // For numeric section
                    for (int k = 0; k <= 9; k++) {
                        if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(k)))
                            return j;
                    }
                } else {
                    if (StringMatcher.match(String.valueOf(getItem(j).charAt(0)), String.valueOf(mSections.charAt(i))))
                        return j;
                }
            }
        }
        return 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        String[] sections = new String[mSections.length()];
        for (int i = 0; i < mSections.length(); i++)
            sections[i] = String.valueOf(mSections.charAt(i));
        return sections;
    }
}