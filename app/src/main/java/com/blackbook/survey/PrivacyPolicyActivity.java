package com.blackbook.survey;

import android.os.Bundle;
import androidx.core.app.NavUtils;
import android.view.MenuItem;

/**
 *
 * Created by jcaruso on 11/1/2017.
 *
 */

public class PrivacyPolicyActivity extends BaseActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initview();
    }


    private void initview() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(PrivacyPolicyActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
