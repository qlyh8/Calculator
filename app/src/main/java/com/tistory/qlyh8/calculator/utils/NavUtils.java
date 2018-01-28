package com.tistory.qlyh8.calculator.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tistory.qlyh8.calculator.HistoryActivity;
import com.tistory.qlyh8.calculator.MainActivity;
import com.tistory.qlyh8.calculator.R;
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
        TextView temp2 = (TextView)myActivity.findViewById(R.id.meTemp);
        temp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity,MainActivity.class);
                myActivity.startActivity(intent);
            }
        });

        TextView temp = (TextView)myActivity.findViewById(R.id.myTemp);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(myActivity,HistoryActivity.class);
                myActivity.startActivity(intent);
            }
        });
    }
}
