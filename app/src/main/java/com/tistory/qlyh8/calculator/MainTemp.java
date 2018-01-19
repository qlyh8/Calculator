package com.tistory.qlyh8.calculator;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by cmtyx on 2018-01-12.
 */

public class MainTemp extends AppCompatActivity {

    @BindView(R.id.layout_root_calc) public LinearLayout rootLayout;    // 수식 뷰

    ArrayList<String> arrayList;    // 값을 저장할 배열 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_calc);
        setContentView(R.layout.activity_temp);
        ButterKnife.bind(this);

        arrayList = new ArrayList<>();
    }

   //"0~9" 버튼 클릭
    @SuppressLint("SetTextI18n")
    public void clickBtnNumber(View view) {
        // 수식창이 비었을 때 배열과 뷰에 텍스트를 추가한다.
        if(arrayList.isEmpty()){
            arrayList.add(String.valueOf(view.getTag()));
            setNumTextView(String.valueOf(view.getTag()));
        }
        else{
            // 첫 수식이 0일 때 0을 지우고 해당 텍스트로 채운다.
            if(arrayList.size() == 1 && arrayList.get(0).equals("0")){
                arrayList.set(arrayList.size()-1, String.valueOf(view.getTag()));
                changeTextView("calcTextView", String.valueOf(view.getTag()));
            }
            else {
                String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 수식

                // 마지막 수식이 기호일 때 텍스트를 추가한다.
                if(isSymbol(lastValue)){
                    arrayList.add(String.valueOf(view.getTag()));
                    setNumTextView(String.valueOf(view.getTag()));
                }
                else{
                    if(lastValue.equals("0")){
                        // 마지막 수식이 0일 때, 0을 지우고 텍스트를 추가한다.
                        arrayList.set(arrayList.size()-1, String.valueOf(view.getTag()));
                        changeTextView("calcTextView", String.valueOf(view.getTag()));
                    }
                    else{
                        // 배열 마지막 값을 변경하고 마지막 뷰에 텍스트를 추가한다.
                        arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));

                        if(lastValue.contains("@")){
                            // 분자에 값을 넣는다.
                            appendTextView("calcFractionTopTextView", String.valueOf(view.getTag()));
                        }
                        else{
                            appendTextView("calcTextView", String.valueOf(view.getTag()));
                        }
                    }
                }
            }
        }
    }

    // "." 버튼 클릭
    @SuppressLint("SetTextI18n")
    public void clickBtnPoint(View view){
        // 배열 값이 비어있지 않아야 한다.
        if(!arrayList.isEmpty()){
            String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 값
            // 마지막 값이 기호와 "."이 아니여야 한다.
            if(!isSymbol(lastValue) && !lastValue.substring(lastValue.length()-1).equals(".")){
                arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));
                // 마지막 수식이 분수일 때
                if(lastValue.contains("@")){
                    // 분자에 값을 넣을 때
                    if(lastValue.contains("@") && !lastValue.substring(lastValue.length()-1).equals("@")){
                        appendTextView("calcFractionTopTextView", String.valueOf(view.getTag()));
                    }
                   else {
                        // 분모에 값을 넣을 때
                        appendTextView("calcFractionBottomTextView", String.valueOf(view.getTag()));
                    }
                }
                else{
                    appendTextView("calcTextView", String.valueOf(view.getTag()));
                }
            }
        }
    }

    // "ㅡ" (분수) 버튼 클릭
    public void clickBtnFraction (View view){
        if(!arrayList.isEmpty()){
            if(arrayList.size() == 1 && arrayList.get(0).equals("0")){
            }
            else {
                String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 값

                if(!isSymbol(lastValue) && !lastValue.substring(lastValue.length()-1).equals(".")){
                    if(!lastValue.equals("0")){
                        // 분수 뷰로 변경한다. (분모) @ (분자)
                        arrayList.set(arrayList.size()-1, lastValue+"@");
                        removeView("textView", "calcTextView");
                        removeView("layout", "calcLayout");
                        setFractionTextView(lastValue);
                    }
                }
            }
        }
    }

    // "+,－,×,÷" 버튼 클릭
    public void clickBtnSymbol(View view) {
        // 배열 값이 비어있을 때 기호가 들어가면 안된다.
        if(!arrayList.isEmpty()){
            String lastValue = arrayList.get(arrayList.size()-1);   // 마지막 값
            // 마지막 값이 기호가 아니여야 한다.
            if(!isSymbol(lastValue)){
                // 마지막 값이 분수일 때, 분수 안에는 기호가 들어가면 안된다.
                if(lastValue.contains("@") && !lastValue.substring(lastValue.length()-1).equals("@")){
                    arrayList.add(String.valueOf(view.getTag()));
                    setSymbolTextView(String.valueOf(view.getTag()));
                }
                else {
                    arrayList.add(String.valueOf(view.getTag()));
                    setSymbolTextView(String.valueOf(view.getTag()));
                }
            }
        }
    }

    // "←" 버튼 클릭
    public void clickBtnClear(View view) {
        if(arrayList.size() != 0){
            String lastValue = arrayList.get(arrayList.size()-1);   // 마지막 값

            // 값의 길이가 1일 때 해당 뷰와 해당 배열 위치의 값을 제거한다.
            if(lastValue.length() == 1){
                removeView("textView", "calcTextView");
                removeView("layout", "calcLayout");
                arrayList.remove(arrayList.size()-1);
            }
            else{
                // 분수일 때
                if(lastValue.contains("@")) {
                    String[] value;

                    // 분자에 값이 없을 때 ( ex: 3 6 @ ) line 을 삭제하고 자연수로 만든다.
                    if(lastValue.substring(lastValue.length()-1).equals("@")){
                        value = lastValue.split("@");
                        String bottomValue = value[0]; // 분모

                        arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length() - 1));
                        removeView("textView", "calcFractionTopTextView");
                        removeView("textView", "calcFractionBottomTextView");
                        removeView("line", "calcFractionLine");
                        removeView("layout", "calcFractionLayout");
                        setNumTextView(bottomValue);
                    }
                    else{
                        // 분모, 분자에 모두 값이 있을 때 ( ex: 3 6 @ 2 4 ) 분자를 지운다.
                        value = lastValue.split("@");
                        String topValue = value[1]; // 분자

                        arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length()-1));
                        changeTextView("calcFractionTopTextView", topValue.substring(0, topValue.length()-1));
                    }
                }
                else {
                    // 배열 마지막 값과 마지막 뷰를 변경한다.
                    arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length()-1));
                    changeTextView("calcTextView", lastValue.substring(0, lastValue.length()-1));
                }
            }
        }
    }

    // "C" 버튼 클릭
    public void clickBtnAllClear(View view) {
        arrayList.clear();  // 리스트의 모든 내용 삭제
        rootLayout.removeAllViews();    // 수식 뷰 안의 모든 뷰 삭제
    }

    // "=" 버튼 클릭
    public void clickBtnResult(View view) {
        // 임시 토스트
        StringBuilder str = new StringBuilder();
        for (int i=0 ; i<arrayList.size() ; i++){
            str.append(arrayList.get(i));
        }
        Toast.makeText(getBaseContext(), "Size: "+arrayList.size()+"\n"+str.toString(), Toast.LENGTH_SHORT).show();
    }

    // 숫자 텍스트 생성
    public void setNumTextView (String number){
        // 리니어 레이아웃 생성
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setTag("calcLayout"+arrayList.size());
        rootLayout.addView(layout);

        // 텍스트 뷰 생성
        TextView textView = new TextView(this);
        textView.setText(number);
        textView.setTextSize(35);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorKeyPadNum));
        textView.setGravity(Gravity.CENTER);
        textView.setTag("calcTextView"+arrayList.size());
        layout.addView(textView);
    }

    // 기호 텍스트 생성
    public void setSymbolTextView (String symbol){
        // 리니어 레이아웃 생성
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        layout.setTag("calcLayout"+arrayList.size());
        rootLayout.addView(layout);

        // 텍스트 뷰 생성
        TextView textView = new TextView(this);
        textView.setText(symbol);
        textView.setTextSize(25);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorKeyPadRed));
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTag("calcTextView"+arrayList.size());
        layout.addView(textView);
    }

    // 분수 텍스트 생성
    public void setFractionTextView (String number){
        // 리니어 레이아웃 생성
        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setTag("calcFractionLayout"+arrayList.size());
        rootLayout.addView(layout);

        // 분자 텍스트 뷰 생성
        TextView TopTextView = new TextView(this);
        TopTextView.setText("");
        TopTextView.setTextSize(35);
        TopTextView.setTextColor(ContextCompat.getColor(this, R.color.colorKeyPadNum));
        TopTextView.setGravity(Gravity.CENTER);
        TopTextView.setTag("calcFractionTopTextView"+arrayList.size());
        layout.addView(TopTextView);

        // 라인 생성
        View line = new View(this);
        line.setBackgroundColor(ContextCompat.getColor(this, R.color.colorKeyPadNum));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 3);
        params.setMargins(5, 0, 5, 0); // pass int values for left,top,right,bottom
        line.setLayoutParams(params);
        line.setTag("calcFractionLine"+arrayList.size());
        layout.addView(line);

        // 분모 텍스트 뷰 생성
        TextView BottomTextView = new TextView(this);
        BottomTextView.setText(number);
        BottomTextView.setTextSize(35);
        BottomTextView.setTextColor(ContextCompat.getColor(this, R.color.colorKeyPadNum));
        BottomTextView.setGravity(Gravity.CENTER);
        BottomTextView.setTag("calcFractionBottomTextView"+arrayList.size());
        layout.addView(BottomTextView);
    }

    // 텍스트 뷰 변경 (= str)
    public void changeTextView(String target, String str){
        TextView textView = rootLayout.findViewWithTag(target + arrayList.size());
        textView.setText(str);
    }

    // 텍스트 뷰 변경 (+= str)
    @SuppressLint("SetTextI18n")
    public void appendTextView(String target, String str){
        TextView textView = rootLayout.findViewWithTag(target + arrayList.size());
        textView.setText(textView.getText() + str);
    }

    // 뷰 삭제
    public void removeView (String targetType, String targetTag){
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

    // 숫자인지 기호인지 판별
    public boolean isSymbol (String str){
        boolean result;

        switch (str){
            case "＋" :
                result = true;
                break;
            case "－" :
                result = true;
                break;
            case "×" :
                result = true;
                break;
            case "÷" :
                result = true;
                break;
            default:
                result = false;
                break;
        }
        return result;
    }
}
