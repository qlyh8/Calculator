package com.tistory.qlyh8.calculator.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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

    public Context context;
    public LinearLayout rootLayout;    // 수식 뷰
    public int defaultTextWidth = 99999;    // 디폴트 텍스트 너비

    public ViewUtils(Context mContext, LinearLayout mRootLayout){
        context = mContext;
        rootLayout = mRootLayout;
    }

    // 리니어 레이아웃 생성
    public LinearLayout setLinearLayout(ArrayList<String> arrayList){
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
        textView.setTextSize(35);
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
        textView.setTextSize(25);
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
        TopTextView.setTextSize(35);
        TopTextView.setTextColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        TopTextView.setGravity(Gravity.CENTER);
        TopTextView.setTag("calcFractionTopTextView"+arrayList.size());
        layout.addView(TopTextView);

        // 라인 생성
        View line = new View(context);
        line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 3);
        params.setMargins(5, 0, 5, 0); // pass int values for left,top,right,bottom
        line.setLayoutParams(params);
        line.setTag("calcFractionLine"+arrayList.size());
        layout.addView(line);

        // 분모 텍스트 뷰 생성
        TextView BottomTextView = new TextView(context);
        BottomTextView.setText(number);
        BottomTextView.setTextSize(35);
        BottomTextView.setTextColor(ContextCompat.getColor(context, R.color.colorKeyPadNum));
        BottomTextView.setGravity(Gravity.CENTER);
        BottomTextView.setTag("calcFractionBottomTextView"+arrayList.size());
        layout.addView(BottomTextView);

        // 분모 텍스트 너비에 맞게 라인 길이 변경
        BottomTextView.measure(0,0);
        LinearLayout.LayoutParams newParams = new LinearLayout.LayoutParams(BottomTextView.getMeasuredWidth(), 3);
        newParams.setMargins(5, 0, 5, 0); // pass int values for left,top,right,bottom
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

        newParams.setMargins(5, 0, 5, 0); // pass int values for left,top,right,bottom
        View line = rootLayout.findViewWithTag("calcFractionLine" + arrayList.size());
        line.setLayoutParams(newParams);
    }
}
