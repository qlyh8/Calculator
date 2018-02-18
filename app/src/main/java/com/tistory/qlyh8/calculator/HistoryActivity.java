package com.tistory.qlyh8.calculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tistory.qlyh8.calculator.adapter.HistoryAdapter;
import com.tistory.qlyh8.calculator.utils.HistoryStorageUtil;
import com.tistory.qlyh8.calculator.utils.NavUtils;
import com.tistory.qlyh8.calculator.utils.ThemeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class HistoryActivity extends AppCompatActivity{

    private NavUtils navUtils = new NavUtils();
    @BindView(R.id.nav_history_recyclerView) RecyclerView navHistoryRecyclerView;
    @BindView(R.id.slide_menu) LinearLayout slideMenu;
    @BindView(R.id.slide_text) ImageView slideText;
    @BindView(R.id.history_pannel) LinearLayout historyPannel;

    private HistoryAdapter historyAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        navUtils.bind(HistoryActivity.this,savedInstanceState);
        slideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navUtils.getSlidingRootNav().openMenu();
            }
        });

        historyAdapter = new HistoryAdapter(this, HistoryStorageUtil.historyRes);
        linearLayoutManager = new LinearLayoutManager(this);
        navHistoryRecyclerView.setLayoutManager(linearLayoutManager);
        navHistoryRecyclerView.setAdapter(historyAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTheme();
        navUtils.setTheme();
    }

    private void setTheme() {
        slideMenu.setBackground(getDrawable(ThemeUtil.themeSlideMenuBg));
        //slideText.setColorFilter(ThemeUtil.themeSlideMenuText);
        historyPannel.setBackground(getDrawable(ThemeUtil.themeBackground));
    }

    @Override
    public void onBackPressed() {

    }
}
