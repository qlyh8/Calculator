package com.tistory.qlyh8.calculator.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        setClickEvent();
    }

    public SlidingRootNav getSlidingRootNav() {
        return slidingRootNav;
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
