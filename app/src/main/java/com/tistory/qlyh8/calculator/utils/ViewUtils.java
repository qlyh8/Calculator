package com.tistory.qlyh8.calculator.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tistory.qlyh8.calculator.R;

import java.util.ArrayList;

/*
 * Created by YUNHEE on 2018-01-23.
 */

public class ViewUtils {

    private Context context;
    private LinearLayout rootLayout;    // 수식 뷰
    private int defaultTextWidth = 99999;    // 디폴트 텍스트 너비

    public ViewUtils(Context mContext, LinearLayout mRootLayout){
        context = mContext;
        rootLayout = mRootLayout;
    }

    // 리니어 레이아웃 생성
    private LinearLayout setLinearLayout(ArrayList<String> arrayList){
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setTag("calcLayout"+arrayList.size());
        return layout;
    }

    // 숫자 텍스트 생성
    public void setNumTextView(ArrayList<String> arrayList, String number){
        // 리니어 레이아웃 생성
        LinearLayout layout = setLinearLayout(arrayList);
        rootLayout.addView(layout);

        TextView textView = new TextView(context);
        textView.setText(number);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.calcNumberTextSize));
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        textView.setGravity(Gravity.CENTER);
        textView.setTag("calcTextView"+arrayList.size());
        layout.addView(textView);
    }

    // 연산자 텍스트 생성
    public void setSymbolTextView(ArrayList<String> arrayList, String symbol){
        // 리니어 레이아웃 생성
        LinearLayout layout = setLinearLayout(arrayList);
        rootLayout.addView(layout);

        // 텍스트 뷰 생성
        TextView textView = new TextView(context);
        textView.setText(symbol);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.calcOperatorTextSize));
        textView.setTextColor(ContextCompat.getColor(context, R.color.colorKeyPadRed));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTag("calcTextView"+arrayList.size());
        layout.addView(textView);
    }

    // 분수 텍스트 생성
    public void setFractionTextView(ArrayList<String> arrayList, String number){
        // 리니어 레이아웃 생성
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setTag("calcFractionLayout"+arrayList.size());
        rootLayout.addView(layout);

        // 분자 텍스트 뷰 생성
        TextView TopTextView = new TextView(context);
        TopTextView.setText("");
        TopTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.calcNumberTextSize));
        TopTextView.setTextColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        TopTextView.setGravity(Gravity.CENTER);
        TopTextView.setTag("calcFractionTopTextView"+arrayList.size());
        layout.addView(TopTextView);

        // 라인 생성
        View line = new View(context);
        line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 3);
        params.setMargins(5, 0, 5, 0);
        line.setLayoutParams(params);
        line.setTag("calcFractionLine"+arrayList.size());
        layout.addView(line);

        // 분모 텍스트 뷰 생성
        TextView BottomTextView = new TextView(context);
        BottomTextView.setText(number);
        BottomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.calcNumberTextSize));
        BottomTextView.setTextColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        BottomTextView.setGravity(Gravity.CENTER);
        BottomTextView.setTag("calcFractionBottomTextView"+arrayList.size());
        layout.addView(BottomTextView);

        // 분모 텍스트 너비에 맞게 라인 길이 변경
        BottomTextView.measure(0,0);
        LinearLayout.LayoutParams newParams = new LinearLayout.LayoutParams(BottomTextView.getMeasuredWidth(), 3);
        newParams.setMargins(5,0,5,0);
        line.setLayoutParams(newParams);
    }

    // 분수 텍스트 생성
    public void setFractionHistoryTextView(int key, String bottomNum, String topNum){
        // 리니어 레이아웃 생성
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setTag("calcFractionLayout"+key);
        rootLayout.addView(layout);

        // 분자 텍스트 뷰 생성
        TextView TopTextView = new TextView(context);
        TopTextView.setText(topNum);
        TopTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.calcNumberTextSize));
        TopTextView.setTextColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        TopTextView.setGravity(Gravity.CENTER);
        TopTextView.setTag("calcFractionTopTextView"+key);
        layout.addView(TopTextView);

        // 라인 생성
        View line = new View(context);
        line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 3);
        params.setMargins(5, 0, 5, 0);
        line.setLayoutParams(params);
        line.setTag("calcFractionLine"+key);
        layout.addView(line);

        // 분모 텍스트 뷰 생성
        TextView BottomTextView = new TextView(context);
        BottomTextView.setText(bottomNum);
        BottomTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.calcNumberTextSize));
        BottomTextView.setTextColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        BottomTextView.setGravity(Gravity.CENTER);
        BottomTextView.setTag("calcFractionBottomTextView"+key);
        layout.addView(BottomTextView);

        // 분모 텍스트 너비에 맞게 라인 길이 변경
        BottomTextView.measure(0,0);
        LinearLayout.LayoutParams newParams = new LinearLayout.LayoutParams(BottomTextView.getMeasuredWidth(), 3);
        newParams.setMargins(5,0,5,0);
        line.setLayoutParams(newParams);
    }

    // 텍스트 뷰 변경 (= str)
    public void changeTextView(ArrayList<String> arrayList, String target, String str){
        TextView textView = rootLayout.findViewWithTag(target + arrayList.size());
        textView.setText(str);
    }

    // 텍스트 뷰 변경 (+= str)
    @SuppressLint("SetTextI18n")
    public void appendTextView(ArrayList<String> arrayList, String target, String str){
        TextView textView = rootLayout.findViewWithTag(target + arrayList.size());
        textView.setText(textView.getText() + str);
    }


    // 뷰 삭제
    public void removeView (ArrayList<String> arrayList, String targetType, String targetTag){
        switch (targetType){
            case "layout":
                LinearLayout layout = rootLayout.findViewWithTag(targetTag + arrayList.size());
                rootLayout.removeView(layout);
                break;
            case "textView":
                TextView textView = rootLayout.findViewWithTag(targetTag + arrayList.size());
                rootLayout.removeView(textView);
                break;
            case "line":
                View line = rootLayout.findViewWithTag(targetTag + arrayList.size());
                rootLayout.removeView(line);
                break;
            default:
                break;
        }
    }

    // 분수 선 조정
    public void changeFractionLine(ArrayList<String> arrayList){
        TextView topTextView = rootLayout.findViewWithTag("calcFractionTopTextView" + arrayList.size());
        TextView bottomTextView = rootLayout.findViewWithTag("calcFractionBottomTextView" + arrayList.size());

        // 디폴트 텍스트 너비를 구한다.
        if(topTextView.getText().length() == 1){
            topTextView.measure(0,0);
            if(defaultTextWidth > topTextView.getMeasuredWidth())
               defaultTextWidth = topTextView.getMeasuredWidth();
        }

        // 분자와 분모 중 너비가 큰 것에 맞춰 라인을 조정한다.
        LinearLayout.LayoutParams newParams;
        if(topTextView.getText().length() > bottomTextView.getText().length())
            newParams = new LinearLayout.LayoutParams(topTextView.getText().length() * defaultTextWidth, 3);
        else
            newParams = new LinearLayout.LayoutParams(bottomTextView.getText().length() * defaultTextWidth, 3);

        newParams.setMargins(5,0,5,0);
        View line = rootLayout.findViewWithTag("calcFractionLine" + arrayList.size());
        line.setLayoutParams(newParams);
    }

    public void changeHistoryFractionLine(int key){
        TextView topTextView = rootLayout.findViewWithTag("calcFractionTopTextView" + key);
        TextView bottomTextView = rootLayout.findViewWithTag("calcFractionBottomTextView" + key);

        // 디폴트 텍스트 너비를 구한다.
        if(topTextView.getText().length() == 1){
            topTextView.measure(0,0);
            if(defaultTextWidth > topTextView.getMeasuredWidth())
                defaultTextWidth = topTextView.getMeasuredWidth();
        }

        // 분자와 분모 중 너비가 큰 것에 맞춰 라인을 조정한다.
        LinearLayout.LayoutParams newParams;
        if(topTextView.getText().length() > bottomTextView.getText().length())
            newParams = new LinearLayout.LayoutParams(topTextView.getText().length() * defaultTextWidth, 3);
        else
            newParams = new LinearLayout.LayoutParams(bottomTextView.getText().length() * defaultTextWidth, 3);

        newParams.setMargins(5,0,5,0);
        View line = rootLayout.findViewWithTag("calcFractionLine" + key);
        line.setLayoutParams(newParams);
    }
}
