package com.tistory.qlyh8.calculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tistory.qlyh8.calculator.Utils.NavUtils;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class HistoryActivity extends AppCompatActivity{

    private NavUtils navUtils = new NavUtils();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        navUtils.bind(HistoryActivity.this,savedInstanceState);

    }
}
