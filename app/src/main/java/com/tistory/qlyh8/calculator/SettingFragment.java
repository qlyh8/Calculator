package com.tistory.qlyh8.calculator;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.tistory.qlyh8.calculator.utils.ThemeUtil;

/*
 * Created by YUNHEE on 2018-02-07.
 */

public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_pref);
    }

    @Override
    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        super.setPreferenceScreen(preferenceScreen);

        Preference p = preferenceScreen.getPreference(0);

        Spannable title = new SpannableString(p.getTitle().toString());
        title.setSpan(new ForegroundColorSpan(getResources().getColor(ThemeUtil.themeNumTextColor)),
                0, title.length(), 0);
        p.setTitle(title);
    }
}
