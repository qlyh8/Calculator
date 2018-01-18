package com.tistory.qlyh8.calculator;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by cmtyx on 2018-01-12.
 */

public class MainTemp extends AppCompatActivity {

    @BindView(R.id.layout_cal) public LinearLayout layoutCal;
    @BindView(R.id.btn_num1) public Button btnNum1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_calc);
        setContentView(R.layout.activity_temp);
        ButterKnife.bind(this);
    }

    public void clickBtnNum1(View view) {}

    public void clickBtnClear(View view) {}

    public void clickBtnAllClear(View view) {
        layoutCal.removeAllViews();
    }

    public void clickBtnPlus(View view) {
        TextView textPlus = new TextView(this);
        textPlus.setText(R.string.plus);
        textPlus.setTextSize(25);
        textPlus.setTextColor(ContextCompat.getColor(this, R.color.colorKeyPadRed));
        textPlus.setTypeface(Typeface.DEFAULT_BOLD);
        textPlus.setGravity(Gravity.CENTER);

        layoutCal.addView(textPlus);
    }
}
