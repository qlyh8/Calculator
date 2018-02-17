package com.tistory.qlyh8.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tistory.qlyh8.calculator.utils.NavUtils;
import com.tistory.qlyh8.calculator.utils.ThemeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by YUNHEE on 2018-02-07.
 */

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.slide_menu) LinearLayout slideMenu;
    @BindView(R.id.slide_text) ImageView slideText;
    @BindView(R.id.setting_pannel) LinearLayout settingBg;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTheme();
    }

    private void setTheme() {
        slideMenu.setBackground(getDrawable(ThemeUtil.themeSlideMenuBg));
        slideText.setColorFilter(ThemeUtil.themeSlideMenuText);
        settingBg.setBackground(getDrawable(ThemeUtil.themeBackground));
    }

    @Override
    public void onBackPressed() {}
}
