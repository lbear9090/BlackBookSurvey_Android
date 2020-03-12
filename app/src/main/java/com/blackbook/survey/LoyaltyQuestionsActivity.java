package com.blackbook.survey;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blackbook.survey.Constant.AppConstant;
import com.blackbook.survey.Utils.Utils;
import com.blackbook.survey.db.DatabaseHelper;
import com.blackbook.survey.model.Questions;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;

/**
 *
 * Created by c119 on 02/04/16.
 *
 */
public class LoyaltyQuestionsActivity extends BaseActivity implements View.OnClickListener
{
    private TextView txtquestiontitle;
    private Button imgprevious, imgnext, imgfinish;
    private DatabaseHelper db;
    private ArrayList<Questions> arrquestions;

    private ArrayList<Integer> arr_loyalty;

    private int index;
    private int arrsize;
    private DiscreteSeekBar seekBar;

    public String newString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyaltyquestions);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        index = 0;

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            newString = extras.getString("AnswerArray");
        }

        arr_loyalty = new ArrayList<>(3);
        for(int i=0 ;i < 3;i++)
        {
            arr_loyalty.add(0);
        }

        initview();
    }

    private void initview()
    {
        db = new DatabaseHelper(LoyaltyQuestionsActivity.this);
        db.openDataBase();

        txtquestiontitle = (TextView)findViewById(R.id.txt_question_title);
        txtquestiontitle.setTypeface(Sufi_Regular);

        imgnext = (Button)findViewById(R.id.btn_next);

        imgfinish = (Button)findViewById(R.id.btn_finish);
        imgfinish.setVisibility(View.GONE);

        imgprevious = (Button)findViewById(R.id.btn_previous);
        imgprevious.setVisibility(View.GONE);

        imgprevious.setOnClickListener(this);
        imgnext.setOnClickListener(this);
        imgfinish.setOnClickListener(this);

        seekBar = (DiscreteSeekBar) findViewById(R.id.sbSelection);
        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                arr_loyalty.set(index,seekBar.getProgress());
            }
        });

        setqoestions();
    }

    private void setqoestions()
    {
        arrquestions = db.GetAllQuestions("Loyalty");
        arrsize = arrquestions.size();

        txtquestiontitle.setText(arrquestions.get(index).getQuestion_title().trim());
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
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_next:
                if(index < arrsize-1)
                {
                    index++;

                    //Make the previous visible if we're past the first question.
                    if(index > 0)
                    {
                        imgprevious.setVisibility(View.VISIBLE);
                    }

                    //If we're on the last question hide the next and show the finish.
                    if(index == arrsize-1)
                    {
                        imgnext.setVisibility(View.GONE);
                        imgfinish.setVisibility(View.VISIBLE);
                    }

                    //Set the question number and question.
                    txtquestiontitle.setText(index+1+")"+" "+arrquestions.get(index).getQuestion_title().trim());

                    int dd = arr_loyalty.get(index);
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
            case R.id.btn_finish:
                StringBuilder sbque = new StringBuilder();
                sbque.setLength(0);

                StringBuilder sbans = new StringBuilder();
                sbans.setLength(0);
                sbans.append(newString);

                String[] arrans = newString.split(",");
                int arrsize = arrans.length;

                for(int i = 0 ; i < arrsize ; i++)
                {
                    sbque.append(String.valueOf(i+1));
                    if (i < arrsize - 1)
                    {
                        sbque.append(",");
                    }
                }

                int arrloyaltysize = arr_loyalty.size();
                for(int i = 0; i < arrloyaltysize ; i++)
                {
                    sbque.append(",");
                    sbque.append(String.valueOf(arrsize+i+1));

                    sbans.append(",");
                    sbans.append(String.valueOf(arr_loyalty.get(i)));
                }

                String answettext = sbans.toString().replaceAll("\\s+", "");

                Log.i(Utils.TAG, "finalque:::" + sbque.toString());
                Log.i(Utils.TAG, "finalans:::" + answettext);

                Intent i = new Intent(this,SubmitActivity.class);
                i.putExtra("question_string",sbque.toString());
                i.putExtra("answer_string",answettext);
                startActivityForResult(i, 0);
                break;

            case R.id.btn_previous:
                index--;
                if(index == 0)
                {
                    imgprevious.setVisibility(View.GONE);
                }

                imgnext.setVisibility(View.VISIBLE);
                imgfinish.setVisibility(View.GONE);

                txtquestiontitle.setText(index+1 + ")" + " " + arrquestions.get(index).getQuestion_title().trim());


                int dd = arr_loyalty.get(index);
                switch (dd) {
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

                break;
        }
    }
}
