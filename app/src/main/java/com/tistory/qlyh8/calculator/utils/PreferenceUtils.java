package com.tistory.qlyh8.calculator.utils;

/*
 * Created by YUNHEE on 2018-02-07.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.TextView;

import com.tistory.qlyh8.calculator.R;

import static android.content.Context.VIBRATOR_SERVICE;

public class PreferenceUtils implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Context context;
    private Boolean isVibrate = false;
    private TextView textView;  // 임시

    public PreferenceUtils(Context mContext){
        context = mContext;
    }

    public void getTextView(TextView mTextView){    // 임시
        textView = mTextView;
    }

    // SharedPreferences 에서 값을 가져와 설정
    public void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        // 테마
        setTheme(sharedPreferences.getString(context.getResources().getString(R.string.pref_theme_key),
                context.getResources().getString(R.string.pref_theme_value_black)));

        // 진동 (ON / OFF)
        isVibrate = sharedPreferences.getBoolean(context.getResources().getString(R.string.pref_vibration_key),
                context.getResources().getBoolean(R.bool.pref_vibration_default));

        // 리스너 등록
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    // 테마
    private void setTheme(String newTheme){ // 임시
        if (newTheme.equals(context.getResources().getString(R.string.pref_theme_value_red))) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorKeyPadRed));
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
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
            setTheme(sharedPreferences.getString(context.getResources().getString(R.string.pref_theme_key),
                    context.getResources().getString(R.string.pref_theme_value_black)));
        }
        else if(key.equals(context.getResources().getString(R.string.pref_vibration_key))) {
            isVibrate = sharedPreferences.getBoolean(key,
                    context.getResources().getBoolean(R.bool.pref_vibration_default));
        }
    }
}
