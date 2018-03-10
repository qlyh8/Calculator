package com.tistory.qlyh8.calculator.utils;

/*
 * Created by YUNHEE on 2018-02-07.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.preference.PreferenceManager;

import com.tistory.qlyh8.calculator.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class PreferenceUtils implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Context context;
    private SharedPreferences preferences;
    private Boolean isVibrate = false;

    public PreferenceUtils(Context mContext){
        context = mContext;
    }

    // SharedPreferences 에서 값을 가져와 설정
    public void setupSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // 테마
        setThemeColor((preferences.getString(
                context.getResources().getString(R.string.pref_theme_key),
                context.getResources().getString(R.string.pref_theme_black_value))));

        // 진동 (ON / OFF)
        isVibrate = preferences.getBoolean(context.getResources().getString(R.string.pref_vibration_key),
                context.getResources().getBoolean(R.bool.pref_vibration_default));

        // 리스너 등록
        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    public SharedPreferences getPreferences(){
        return preferences;
    }

    // 테마 설정
    private void setThemeColor(String newThemeColor){
        if(newThemeColor.equals(context.getResources().getString(R.string.pref_theme_pink_value))) {
            ThemeUtil.themeBackground = R.drawable.ripple_keypad_pink;
            ThemeUtil.themeNumTextColor = R.color.colorPinkKeyPadNum;
            ThemeUtil.themeTextColor = R.color.colorPinkKeyPadSymbol;
            ThemeUtil.themeResultTextColor = R.color.colorPinkResultNum;
            ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
            ThemeUtil.themeSlideMenuBg = R.drawable.menu_pink_bg;
            ThemeUtil.themeSlideMenuText = R.color.colorWhite;
        }
        else if(newThemeColor.equals(context.getResources().getString(R.string.pref_theme_purple_value))){
            ThemeUtil.themeBackground = R.drawable.ripple_keypad_purple;
            ThemeUtil.themeNumTextColor = R.color.colorPurpleKeyPadNum;
            ThemeUtil.themeTextColor = R.color.colorPurpleKeyPadSymbol;
            ThemeUtil.themeResultTextColor = R.color.colorPurpleResultNum;
            ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
            ThemeUtil.themeSlideMenuBg = R.drawable.menu_purple_bg;
            ThemeUtil.themeSlideMenuText = R.color.colorWhite;
        }
        else if(newThemeColor.equals(context.getResources().getString(R.string.pref_theme_orange_value))){
            ThemeUtil.themeBackground = R.drawable.ripple_keypad_orange;
            ThemeUtil.themeNumTextColor = R.color.colorOrangeKeyPadNum;
            ThemeUtil.themeTextColor = R.color.colorOrangeKeyPadSymbol;
            ThemeUtil.themeResultTextColor = R.color.colorOrangeResultNum;
            ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
            ThemeUtil.themeSlideMenuBg = R.drawable.menu_orange_bg;
            ThemeUtil.themeSlideMenuText = R.color.colorWhite;
        }
        else if(newThemeColor.equals(context.getResources().getString(R.string.pref_theme_yellow_value))){
            ThemeUtil.themeBackground = R.drawable.ripple_keypad_yellow;
            ThemeUtil.themeNumTextColor = R.color.colorYellowKeyPadNum;
            ThemeUtil.themeTextColor = R.color.colorYellowKeyPadSymbol;
            ThemeUtil.themeResultTextColor = R.color.colorYellowResultNum;
            ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
            ThemeUtil.themeSlideMenuBg = R.drawable.menu_yellow_bg;
            ThemeUtil.themeSlideMenuText = R.color.colorWhite;
        }
        else if(newThemeColor.equals(context.getResources().getString(R.string.pref_theme_green_value))){
            ThemeUtil.themeBackground = R.drawable.ripple_keypad_green;
            ThemeUtil.themeNumTextColor = R.color.colorGreenKeyPadNum;
            ThemeUtil.themeTextColor = R.color.colorGreenKeyPadSymbol;
            ThemeUtil.themeResultTextColor = R.color.colorGreenResultNum;
            ThemeUtil.themeCalcTextColor = R.color.colorGreenCalcNum;
            ThemeUtil.themeSlideMenuBg = R.drawable.menu_green_bg;
            ThemeUtil.themeSlideMenuText = R.color.colorWhite;
        }
        else if(newThemeColor.equals(context.getResources().getString(R.string.pref_theme_blue_value))){
            ThemeUtil.themeBackground = R.drawable.ripple_keypad_blue;
            ThemeUtil.themeNumTextColor = R.color.colorBlueKeyPadNum;
            ThemeUtil.themeTextColor = R.color.colorBlueKeyPadSymbol;
            ThemeUtil.themeResultTextColor = R.color.colorBlueResultNum;
            ThemeUtil.themeCalcTextColor = R.color.colorBlueCalcNum;
            ThemeUtil.themeSlideMenuBg = R.drawable.menu_blue_bg;
            ThemeUtil.themeSlideMenuText = R.color.colorWhite;
        }
        else if(newThemeColor.equals(context.getResources().getString(R.string.pref_theme_gray_value))){
            ThemeUtil.themeBackground = R.drawable.ripple_keypad_gray;
            ThemeUtil.themeNumTextColor = R.color.colorGrayKeyPadNum;
            ThemeUtil.themeTextColor = R.color.colorGrayKeyPadSymbol;
            ThemeUtil.themeResultTextColor = R.color.colorGrayResultNum;
            ThemeUtil.themeCalcTextColor = R.color.colorGrayCalcNum;
            ThemeUtil.themeSlideMenuBg = R.drawable.menu_gray_bg;
            ThemeUtil.themeSlideMenuText = R.color.colorWhite;
        }
        else if(newThemeColor.equals(context.getResources().getString(R.string.pref_theme_brown_value))){
            ThemeUtil.themeBackground = R.drawable.ripple_keypad_brown;
            ThemeUtil.themeNumTextColor = R.color.colorBrownKeyPadNum;
            ThemeUtil.themeTextColor = R.color.colorBrownKeyPadSymbol;
            ThemeUtil.themeResultTextColor = R.color.colorBrownResultNum;
            ThemeUtil.themeCalcTextColor = R.color.colorBrownCalcNum;
            ThemeUtil.themeSlideMenuBg = R.drawable.menu_brown_bg;
            ThemeUtil.themeSlideMenuText = R.color.colorWhite;
        }
        else{
            ThemeUtil.themeBackground = R.drawable.ripple_keypad;
            ThemeUtil.themeNumTextColor = R.color.colorWhite;
            ThemeUtil.themeTextColor = R.color.colorKeyPadRed;
            ThemeUtil.themeResultTextColor = R.color.colorKeyPadNum;
            ThemeUtil.themeCalcTextColor = R.color.colorCalcNum;
            ThemeUtil.themeSlideMenuBg = R.drawable.menu_bg;
            ThemeUtil.themeSlideMenuText = R.color.colorWhite;
        }
    }

    // 터치 진동 (ON / OFF)
    public void setVibration(){
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);

        if (vibrator == null)
            return;

        if(isVibrate){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            else
                vibrator.vibrate(100);
        }
        else{
            vibrator.cancel();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(context.getResources().getString(R.string.pref_theme_key))) {
            setThemeColor((sharedPreferences.getString(
                    context.getResources().getString(R.string.pref_theme_key),
                    context.getResources().getString(R.string.pref_theme_black_value))));
        }
        else if(key.equals(context.getResources().getString(R.string.pref_vibration_key))) {
            isVibrate = sharedPreferences.getBoolean(key,
                    context.getResources().getBoolean(R.bool.pref_vibration_default));
        }
    }
}
