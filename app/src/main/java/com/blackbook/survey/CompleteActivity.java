package com.blackbook.survey;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 *
 * Created by jcaruso on 12/4/2017.
 *
 */

public class CompleteActivity extends BaseActivity implements View.OnClickListener {
    Button btnexit, btnemail, btnnew;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        btnexit = (Button)findViewById(R.id.btn_exit);
        btnemail = (Button)findViewById(R.id.btn_email);
        btnnew = (Button)findViewById(R.id.btn_new);

        btnexit.setOnClickListener(this);
        btnemail.setOnClickListener(this);
        btnnew.setOnClickListener(this);

        btnexit.setTypeface(Sufi_Regular);
        btnemail.setTypeface(Sufi_Regular);
        btnnew.setTypeface(Sufi_Regular);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.blackbookmarketresearch.com/"));
                startActivity(browserIntent);
                break;

            case R.id.btn_email:
                utils.sendmail();
                break;

            case R.id.btn_new:
                Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
        }
    }
}
