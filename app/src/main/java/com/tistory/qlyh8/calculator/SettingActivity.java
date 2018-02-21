package com.tistory.qlyh8.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tistory.qlyh8.calculator.adapter.ThemeAdapter;
import com.tistory.qlyh8.calculator.model.ThemeObject;
import com.tistory.qlyh8.calculator.utils.NavUtils;
import com.tistory.qlyh8.calculator.utils.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by YUNHEE on 2018-02-07.
 */

public class SettingActivity extends AppCompatActivity implements ThemeAdapter.OnThemeClickListener{

    @BindView(R.id.slide_menu) LinearLayout slideMenu;
    @BindView(R.id.slide_text) ImageView slideText;
    @BindView(R.id.setting_pannel) LinearLayout settingBg;
    @BindView(R.id.setting_recycler_view) RecyclerView themeRecyclerView;

    private ThemeAdapter adapter;
    private List<ThemeObject> res;
    private LinearLayoutManager linearLayoutManager;
    private NavUtils navUtils = new NavUtils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        navUtils.bind(SettingActivity.this, savedInstanceState);
        slideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navUtils.getSlidingRootNav().openMenu();
            }
        });

        initTheme();
    }

    private void initTheme() {
        res = new ArrayList<>();
        res.add(new ThemeObject(getDrawable(R.drawable.theme),"Black"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme2), "Pink"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme3), "Purple"));
        adapter = new ThemeAdapter(SettingActivity.this, res, this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        themeRecyclerView.setAdapter(adapter);
        themeRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTheme();
    }

    private void setTheme() {
        slideMenu.setBackground(getDrawable(ThemeUtil.themeSlideMenuBg));
        //slideText.setColorFilter(ThemeUtil.themeSlideMenuText);
        settingBg.setBackground(getDrawable(ThemeUtil.themeBackground));
    }

    @Override
    public void onBackPressed() {}


    @Override
    public void setThemeValue(int num) {
        //테마 설정
        switch (num) {
            case 0 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad;
                ThemeUtil.themeTextColor = R.color.colorKeyPadRed;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 1 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_pink;
                ThemeUtil.themeTextColor = R.color.colorDarkPink;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_pink_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 2 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_purple;
                ThemeUtil.themeTextColor = R.color.colorDarkPurple;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_purple_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
        }
        finish();
    }
}
