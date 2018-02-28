package com.tistory.qlyh8.calculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    @BindView(R.id.setting_text_theme) TextView themeTextView;

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
        res.add(new ThemeObject(getDrawable(R.drawable.theme_black),"Black"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme_pink), "Pink"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme_purple), "Purple"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme_orange), "Orange"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme_yellow), "Yellow"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme_green), "Green"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme_blue), "Blue"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme_gray), "Gray"));
        res.add(new ThemeObject(getDrawable(R.drawable.theme_brown), "Brown"));
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
        themeTextView.setTextColor(getResources().getColor(ThemeUtil.themeNumTextColor));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public void setThemeValue(int num) {
        //테마 설정
        switch (num) {
            case 0 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad;
                ThemeUtil.themeNumTextColor = R.color.colorWhite;
                ThemeUtil.themeTextColor = R.color.colorKeyPadRed;
                ThemeUtil.themeResultTextColor = R.color.colorKeyPadNum;
                ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 1 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_pink;
                ThemeUtil.themeNumTextColor = R.color.colorPinkKeyPadNum;
                ThemeUtil.themeTextColor = R.color.colorPinkKeyPadSymbol;
                ThemeUtil.themeResultTextColor = R.color.colorPinkResultNum;
                ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_pink_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 2 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_purple;
                ThemeUtil.themeNumTextColor = R.color.colorPurpleKeyPadNum;
                ThemeUtil.themeTextColor = R.color.colorPurpleKeyPadSymbol;
                ThemeUtil.themeResultTextColor = R.color.colorPurpleResultNum;
                ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_purple_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 3 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_orange;
                ThemeUtil.themeNumTextColor = R.color.colorOrangeKeyPadNum;
                ThemeUtil.themeTextColor = R.color.colorOrangeKeyPadSymbol;
                ThemeUtil.themeResultTextColor = R.color.colorOrangeResultNum;
                ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_orange_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 4 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_yellow;
                ThemeUtil.themeNumTextColor = R.color.colorYellowKeyPadNum;
                ThemeUtil.themeTextColor = R.color.colorYellowKeyPadSymbol;
                ThemeUtil.themeResultTextColor = R.color.colorYellowResultNum;
                ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_yellow_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 5 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_green;
                ThemeUtil.themeNumTextColor = R.color.colorGreenKeyPadNum;
                ThemeUtil.themeTextColor = R.color.colorGreenKeyPadSymbol;
                ThemeUtil.themeResultTextColor = R.color.colorGreenResultNum;
                ThemeUtil.themeCalcTextColor = R.color.colorGreenCalcNum;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_green_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 6 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_blue;
                ThemeUtil.themeNumTextColor = R.color.colorBlueKeyPadNum;
                ThemeUtil.themeTextColor = R.color.colorBlueKeyPadSymbol;
                ThemeUtil.themeResultTextColor = R.color.colorBlueResultNum;
                ThemeUtil.themeCalcTextColor = R.color.colorBlueCalcNum;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_blue_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 7 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_gray;
                ThemeUtil.themeNumTextColor = R.color.colorGrayKeyPadNum;
                ThemeUtil.themeTextColor = R.color.colorGrayKeyPadSymbol;
                ThemeUtil.themeResultTextColor = R.color.colorGrayResultNum;
                ThemeUtil.themeCalcTextColor = R.color.colorGrayCalcNum;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_gray_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
            case 8 :
                ThemeUtil.themeBackground = R.drawable.ripple_keypad_brown;
                ThemeUtil.themeNumTextColor = R.color.colorBrownKeyPadNum;
                ThemeUtil.themeTextColor = R.color.colorBrownKeyPadSymbol;
                ThemeUtil.themeResultTextColor = R.color.colorBrownResultNum;
                ThemeUtil.themeCalcTextColor = R.color.colorBrownCalcNum;
                ThemeUtil.themeSlideMenuBg = R.drawable.menu_brown_bg;
                ThemeUtil.themeSlideMenuText = R.color.colorWhite;
                break;
        }
        finish();
    }
}
