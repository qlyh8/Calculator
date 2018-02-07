package com.tistory.qlyh8.calculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

/*
 * Created by YUNHEE on 2018-02-07.
 */

public class SettingFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.setting_pref);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        // PreferenceScreen 을 이용해 총 Preference 개수를 알 수 있다.
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        // 모든 preferences 가 checkbox preference 이 아니면 setSummary 메소드를 호출하여
        // preference 및 preference 값 전달
        for (int i = 0; i < count; i++) {
            Preference p = prefScreen.getPreference(i);
            // checkbox preferences 는 이미 summaryOff 와 summary On 를 가지고 있기 때문에 제외
            if (!(p instanceof CheckBoxPreference)) {
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
        }
    }

    /**
     * preference 의 summary 를 갱신한다.
     * preference 가 ListPreference 인지 체크하고
     * ListPreference 일 경우에 그 값과 관련된 label 을 찾는다.
     * @param preference: 갱신할 preference
     * @param value:      갱신될 preference 의 값
     */
    private void setPreferenceSummary(Preference preference, String value) {
        // ListPreference 인지 검사
        if (preference instanceof ListPreference) {
            // ListPreference 에서 선택된 값의 label 을 검색
            ListPreference listPreference = (ListPreference) preference;

            int prefIndex = listPreference.findIndexOfValue(value);
            //prefIndex 값의 유효 체크
            if (prefIndex >= 0) {
                // summary 에 해당 label 을 세팅
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }

    // 바뀐 preference 의 summary 갱신
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // 바뀐 preference 검색
        Preference preference = findPreference(key);

        if (preference != null) {
            if (!(preference instanceof CheckBoxPreference)) {
                // preference 의 summary 갱신
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    // PreferenceScreen 을 불러와서 onSharedPreferenceChanged 리스너 등록
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    // PreferenceScreen 을 불러와서 onSharedPreferenceChanged 리스너 해지
    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
