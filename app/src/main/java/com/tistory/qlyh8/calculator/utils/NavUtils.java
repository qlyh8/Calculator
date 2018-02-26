package com.tistory.qlyh8.calculator.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tistory.qlyh8.calculator.HistoryActivity;
import com.tistory.qlyh8.calculator.MainActivity;
import com.tistory.qlyh8.calculator.R;
import com.tistory.qlyh8.calculator.SettingActivity;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class NavUtils {

    private SlidingRootNav slidingRootNav;
    private Activity myActivity;

    public void bind(Activity activity, Bundle savedInstanceState) {
        myActivity = activity;
        slidingRootNav = new SlidingRootNavBuilder(myActivity)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.activity_nav)
                .inject();
        setTheme();
        setClickEvent();
    }

    public SlidingRootNav getSlidingRootNav() {
        return slidingRootNav;
    }

    public void setTheme() {
        LinearLayout panel = myActivity.findViewById(R.id.nav_pannel);
        panel.setBackground(myActivity.getDrawable(ThemeUtil.themeBackground));

        TextView title_modern = myActivity.findViewById(R.id.nav_title_modern);
        title_modern.setTextColor(myActivity.getResources().getColor(ThemeUtil.themeNumTextColor));
        TextView title_calculator = myActivity.findViewById(R.id.nav_title_calculator);
        title_calculator.setTextColor(myActivity.getResources().getColor(ThemeUtil.themeNumTextColor));

        View btn1 = myActivity.findViewById(R.id.meTemp);
        btn1.setBackground(myActivity.getDrawable(ThemeUtil.themeSlideMenuBg));
        View btn2 = myActivity.findViewById(R.id.myTemp);
        btn2.setBackground(myActivity.getDrawable(ThemeUtil.themeSlideMenuBg));
        View btn3 = myActivity.findViewById(R.id.nav_setting);
        btn3.setBackground(myActivity.getDrawable(ThemeUtil.themeSlideMenuBg));
    }

    public void setClickEvent() {
        View temp2 = myActivity.findViewById(R.id.meTemp);
        temp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity,MainActivity.class);
                myActivity.startActivity(intent);
            }
        });

        View temp = myActivity.findViewById(R.id.myTemp);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity,HistoryActivity.class);
                myActivity.startActivity(intent);
            }
        });

        View setting = myActivity.findViewById(R.id.nav_setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity, SettingActivity.class);
                myActivity.startActivity(intent);
            }
        });
    }
}
