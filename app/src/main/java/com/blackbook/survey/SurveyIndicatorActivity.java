package com.blackbook.survey;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Constant.AppGlobal;
import com.blackbook.survey.Utils.Utils;
import com.blackbook.survey.db.DatabaseHelper;
import com.blackbook.survey.model.Questions;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by c119 on 01/04/16.
 *
 */
public class SurveyIndicatorActivity extends BaseActivity implements View.OnClickListener
{
    private TextView txtquestiontitle, txtquestiondesc;
    private Button imgprevious, imgnext, imgfinish;
    private TextView txtpercentage;
    private DatabaseHelper db;
    private ArrayList<Questions> arrquestions;

    private ArrayList<Integer> arr_radio;

    private int index;
    private int arrsize;
    private DiscreteSeekBar seekBar;
    int color;
    int colorGreen;
    int colorYellow;
    int colorWhite;
    int colorRed;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyindicator);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        index = 0;

        arr_radio = new ArrayList<>(18);
        for(int i=0 ;i < 18;i++)
        {
            arr_radio.add(0);
        }

        initview();

        colorGreen = ContextCompat.getColor(this, R.color.green);
        colorYellow = ContextCompat.getColor(this, R.color.yellow);
        colorRed = ContextCompat.getColor(this, R.color.megento);
        colorWhite  = ContextCompat.getColor(this, R.color.color_white);
    }

    private void initview()
    {
        db = new DatabaseHelper(SurveyIndicatorActivity.this);
        db.openDataBase();

        imgprevious = findViewById(R.id.btn_previous);
        imgprevious.setVisibility(View.GONE);

        imgnext = findViewById(R.id.btn_next);

        imgfinish = findViewById(R.id.btn_finish);
        imgfinish.setVisibility(View.GONE);

        imgprevious.setOnClickListener(this);
        imgnext.setOnClickListener(this);
        imgfinish.setOnClickListener(this);

        txtquestiontitle = findViewById(R.id.txt_question_title);
        txtquestiondesc = findViewById(R.id.txt_question_desc);
        txtpercentage = findViewById(R.id.txt_percentage);

        txtpercentage.setTypeface(Sufi_Regular);
        txtquestiontitle.setTypeface(Sufi_Regular);
        txtquestiondesc.setTypeface(Sufi_Regular);

        seekBar = findViewById(R.id.sbSelection);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                boolean colorChanged = false;

                if(value < 4) {
                    if(color != colorRed){
                        color = colorRed;
                        colorChanged = true;
                    }
                } else if(value < 6) {
                    if (color != colorYellow) {
                        color = colorYellow;
                        colorChanged = true;
                    }
                } else if (value < 7) {

                    if (color != colorWhite) {
                        color = colorWhite;
                        colorChanged = true;
                    }
                } else {
                    if(color != colorGreen){
                        color = colorGreen;
                        colorChanged = true;
                    }
                }

                if(colorChanged) {
                    seekBar.setScrubberColor(color);

                    MotionEvent motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0);
                    seekBar.dispatchTouchEvent(motionEvent);
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                arr_radio.set(index,seekBar.getProgress());
            }
        });

        setqoestions();
    }

    private void setqoestions()
    {
        arrquestions = db.GetAllQuestions("KPI");
        arrsize = arrquestions.size();

        txtquestiontitle.setText(index + 1 + ")" + " " + arrquestions.get(index).getQuestion_title().trim());
        txtquestiondesc.setText(arrquestions.get(index).getQuestion_description().trim());
    }

    private double calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if(!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_finish:
                Log.i(Utils.TAG, "radioarray:::" + arr_radio.toString());

                double d = calculateAverage(arr_radio);
                DecimalFormat df = new DecimalFormat("#.##");
                String dx = df.format(d);
                d=Double.valueOf(dx);
                String id = db.getscoreid(d);

                AppGlobal.setStringPreference(SurveyIndicatorActivity.this, String.valueOf(d), AppConstant.Prefscorematrixaverage);
                AppGlobal.setStringPreference(SurveyIndicatorActivity.this, id, AppConstant.PrefscorematrixId);

                Log.i(Utils.TAG, "Average:::" + AppGlobal.getStringPreference(SurveyIndicatorActivity.this, AppConstant.Prefscorematrixaverage));
                Log.i(Utils.TAG, "scoreid:::" + AppGlobal.getStringPreference(SurveyIndicatorActivity.this, AppConstant.PrefscorematrixId));

                Intent i = new Intent(this,ScoringSummeryActivity.class);
                i.putExtra("AnswerArray",arr_radio.toString());
                startActivityForResult(i, 0);
                break;

            case R.id.btn_previous:
                if(index > 0)
                {
                    index--;
                    if(index == 0)
                    {
                        imgprevious.setVisibility(View.GONE);
                    }

                    int cCom = (int)((index/18.0) * 100.0);
                    txtpercentage.setText(cCom+"% "+"Completed");

                    imgnext.setVisibility(View.VISIBLE);
                    imgfinish.setVisibility(View.GONE);

                    txtquestiontitle.setText(index+1+")"+" "+arrquestions.get(index).getQuestion_title().trim());
                    txtquestiondesc.setText(arrquestions.get(index).getQuestion_description().trim());


                    int dd = arr_radio.get(index);
                    switch(dd)
                    {
                        case 0:
                            seekBar.setProgress(0);
                            break;

                        case 1:
                            seekBar.setProgress(1);
                            break;

                        case 2:
                            seekBar.setProgress(2);
                            break;

                        case 3:
                            seekBar.setProgress(3);
                            break;

                        case 4:
                            seekBar.setProgress(4);
                            break;

                        case 5:
                            seekBar.setProgress(5);
                            break;

                        case 6:
                            seekBar.setProgress(6);
                            break;

                        case 7:
                            seekBar.setProgress(7);
                            break;

                        case 8:
                            seekBar.setProgress(8);
                            break;

                        case 9:
                            seekBar.setProgress(9);
                            break;

                        case 10:
                            seekBar.setProgress(10);
                            break;
                    }
                }

                break;

            case R.id.btn_next:
                if(index < arrsize-1)
                {
                    index++;
                    if(index > 0)
                    {
                        imgprevious.setVisibility(View.VISIBLE);
                    }

                    if(index == arrsize-1)
                    {
                        imgnext.setVisibility(View.GONE);
                        imgfinish.setVisibility(View.VISIBLE);
                    }

                    txtquestiontitle.setText(index+1+")"+" "+arrquestions.get(index).getQuestion_title().trim());
                    txtquestiondesc.setText(arrquestions.get(index).getQuestion_description().trim());

                    int cCom = (int)((index/18.0) * 100.0);
                    txtpercentage.setText(cCom+"% "+"Completed");

                    int dd = arr_radio.get(index);
                    switch(dd)
                    {
                        case 0:
                            seekBar.setProgress(0);
                            break;

                        case 1:
                            seekBar.setProgress(1);
                            break;

                        case 2:
                            seekBar.setProgress(2);
                            break;

                        case 3:
                            seekBar.setProgress(3);
                            break;

                        case 4:
                            seekBar.setProgress(4);
                            break;

                        case 5:
                            seekBar.setProgress(5);
                            break;

                        case 6:
                            seekBar.setProgress(6);
                            break;

                        case 7:
                            seekBar.setProgress(7);
                            break;

                        case 8:
                            seekBar.setProgress(8);
                            break;

                        case 9:
                            seekBar.setProgress(9);
                            break;

                        case 10:
                            seekBar.setProgress(10);
                            break;
                    }

                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(resultCode)
        {
            case 1001:
                setResult(AppConstant.RESULT_CLOSE_ALL);
                finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(SurveyIndicatorActivity.this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
