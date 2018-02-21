package com.tistory.qlyh8.calculator;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/*
 * Created by YUNHEE on 2018-02-07.
 */

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_pref);
    }
}
