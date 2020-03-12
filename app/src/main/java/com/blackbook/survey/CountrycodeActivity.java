package com.blackbook.survey;

import android.app.ListActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blackbook.survey.adapter.AlphabetCodeAdapter;
import com.blackbook.survey.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 *
 * Created by c119 on 01/04/16.
 *
 */
public class CountrycodeActivity extends ListActivity implements View.OnClickListener
{
    public TextView txtheader;
    public ImageView imgback;
    public DatabaseHelper db;
    private GestureDetector mGestureDetector;
    private int sideIndexHeight;
    private static float sideIndexX;
    private static float sideIndexY;
    private List<Object[]> alphabet = new ArrayList<Object[]>();
    private HashMap<String, Integer> sections = new HashMap<String, Integer>();
    private AlphabetCodeAdapter adapter;
    private int indexListSize;
    public EditText edtsearch;

    private class SideIndexGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            sideIndexX = sideIndexX - distanceX;
            sideIndexY = sideIndexY - distanceY;

            if (sideIndexX >= 0 && sideIndexY >= 0)
            {
                displayListItem();
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countrycode);

        db = new DatabaseHelper(CountrycodeActivity.this);
        db.openDataBase();

        adapter = new AlphabetCodeAdapter(CountrycodeActivity.this);

        txtheader = (TextView)findViewById(R.id.txt_header);
        txtheader.setTypeface(BaseActivity.Sufi_Regular);

        imgback = (ImageView)findViewById(R.id.img_back);
        imgback.setOnClickListener(this);

        edtsearch = (EditText) findViewById(R.id.edt_search);
        edtsearch.setTypeface(BaseActivity.Sufi_Regular);

        edtsearch.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void afterTextChanged(Editable arg0)
            {
                // TODO Auto-generated method stub
                String text = edtsearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }
        });

        //actual coding
        mGestureDetector = new GestureDetector(this, new SideIndexGestureListener());

        List<String> countries = populateCountries();
        Collections.sort(countries, new Comparator<String>() {
            public int compare(String v1, String v2) {
                return v1.compareToIgnoreCase(v2);
            }
        });

        List<AlphabetCodeAdapter.Row> rows = new ArrayList<>();
        int start = 0;
        int end = 0;
        String previousLetter = null;
        Object[] tmpIndexItem = null;
        Pattern numberPattern = Pattern.compile("[0-9]");

        for (String country : countries)
        {
            String firstLetter = country.toUpperCase().substring(0, 1);

            // Group numbers together in the scroller
            if (numberPattern.matcher(firstLetter).matches()) {
                firstLetter = "#";
            }

            // If we've changed to a new letter, add the previous letter to the alphabet scroller
            if (previousLetter != null && !firstLetter.equalsIgnoreCase(previousLetter)) {
                end = rows.size() - 1;
                tmpIndexItem = new Object[3];
                tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
                tmpIndexItem[1] = start;
                tmpIndexItem[2] = end;
                alphabet.add(tmpIndexItem);

                start = end + 1;
            }

            // Check if we need to add a header row
            if (!firstLetter.equalsIgnoreCase(previousLetter)) {
                rows.add(new AlphabetCodeAdapter.Section(firstLetter));
                sections.put(firstLetter, start);
            }

            // Add the country to the list
            rows.add(new AlphabetCodeAdapter.Item(country));
            previousLetter = firstLetter;
        }

        if (previousLetter != null) {
            // Save the last letter
            tmpIndexItem = new Object[3];
            tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
            tmpIndexItem[1] = start;
            tmpIndexItem[2] = rows.size() - 1;
            alphabet.add(tmpIndexItem);
        }

        adapter.setRows(rows);
        setListAdapter(adapter);

        updateList();

    }

    private List<String> populateCountries()
    {
        List<String> countries;
        countries = db.GetAllCountries();
        return countries;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.img_back:
                finish();
                break;

        }
    }

    public void updateList() {
        LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
        sideIndex.removeAllViews();
        indexListSize = alphabet.size();
        if (indexListSize < 1) {
            return;
        }

        int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
        int tmpIndexListSize = indexListSize;
        while (tmpIndexListSize > indexMaxSize) {
            tmpIndexListSize = tmpIndexListSize / 2;
        }
        double delta;
        if (tmpIndexListSize > 0) {
            delta = indexListSize / tmpIndexListSize;
        } else {
            delta = 1;
        }

        TextView tmpTV;
        for (double i = 1; i <= indexListSize; i = i + delta) {
            Object[] tmpIndexItem = alphabet.get((int) i - 1);
            String tmpLetter = tmpIndexItem[0].toString();

            tmpTV = new TextView(this);
            tmpTV.setText(tmpLetter);
            tmpTV.setGravity(Gravity.CENTER);
            tmpTV.setTextSize(13);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            tmpTV.setLayoutParams(params);
            sideIndex.addView(tmpTV);
        }

        sideIndexHeight = sideIndex.getHeight();

        sideIndex.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // now you know coordinates of touch
                sideIndexX = event.getX();
                sideIndexY = event.getY();

                // and can display a proper item it country list
                displayListItem();

                return false;
            }
        });
    }

    public void displayListItem() {
        LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
        sideIndexHeight = sideIndex.getHeight();
        // compute number of pixels for every side index item
        double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

        // compute the item index for given event position belongs to
        int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

        // get the item (we can do it since we know item index)
        if (itemPosition < alphabet.size()) {
            Object[] indexItem = alphabet.get(itemPosition);
            int subitemPosition = sections.get(indexItem[0]);

            //ListView listView = (ListView) findViewById(android.R.id.list);
            getListView().setSelection(subitemPosition);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
