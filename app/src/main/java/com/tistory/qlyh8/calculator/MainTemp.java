package com.tistory.qlyh8.calculator;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitHelper;

/*
 * Created by cmtyx on 2018-01-12.
 */

public class MainTemp extends AppCompatActivity {

    @BindView(R.id.layout_root_calc) public LinearLayout rootLayout;    // 수식 뷰
    @BindView(R.id.resultView) public TextView resultTextView;  // 결과값 뷰
    ArrayList<String> arrayList;    // 값을 저장할 배열 리스트
    double result;  // 결과값
    int intResult;  // 정수형 결과값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_calc);
        setContentView(R.layout.activity_temp);
        ButterKnife.bind(this);

        AutofitHelper.create(resultTextView);   // 텍스트 길이 자동 조정
        resultTextView.setText("0");
        arrayList = new ArrayList<>();
        result = 0d;
        intResult = 0;
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
                            // 분자가 분모보다 너비가 커지면 라인을 조정한다.
                            changeFractionLine();
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
            if(!isSymbol(lastValue) && !lastValue.substring(lastValue.length()-1).equals(".") && !lastValue.substring(lastValue.length()-1).equals("@")){
                arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));
                // 마지막 수식이 분수일 때
                if(lastValue.contains("@")){
                    // 분자에 값을 넣을 때
                    if(lastValue.contains("@") && !lastValue.substring(lastValue.length()-1).equals("@")){
                        appendTextView("calcFractionTopTextView", String.valueOf(view.getTag()));
                        // 분자가 분모보다 너비가 커지면 라인을 조정한다.
                        changeFractionLine();
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
                    if(!lastValue.equals("0") && !lastValue.contains("@")){
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
            // 마지막 값이 기호가 아니고 "."이 아니여야 한다.
            if(!isSymbol(lastValue) && !lastValue.substring(lastValue.length()-1).equals(".") && !lastValue.substring(lastValue.length()-1).equals("@")){
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
                        changeFractionLine();
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
        resultTextView.setText("0");    // 결과 값을 0으로 초기화
        result = 0d;    // 결과값 초기화
        intResult = 0;  // 결과값 초기화
    }

    // "=" 버튼 클릭
    public void clickBtnResult(View view) {

        String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 수식
        String lastStrOfLastVal = lastValue.substring(lastValue.length()-1);    // 마지막 수식의 마지막 글자

        // 마지막 수식은 숫자여야 한다.
        if(!isSymbol(lastValue) && !lastStrOfLastVal.equals(".") && !lastStrOfLastVal.equals("@")){

            // 초기화 ("="을 입력한 후 "C" 버튼을 누르지 않고 계속 수식을 입력할 경우를 위해)
            result = 0;
            intResult = 0;

            /* String str = "";
            for (int i=0 ; i<arrayList.size() ; i++) {str += arrayList.get(i);}
            Log.d("asd", "1.원래수식: "+ str); */

            // 분수를 나눗셈으로 변환
            for(int i = 0 ; i < arrayList.size() ; i++){
                if(arrayList.get(i).contains("@")){
                    String splitStr[] = arrayList.get(i).split("@");
                    arrayList.set(i, splitStr[1] + "÷" + splitStr[0]);
                }
            }

            // arrayList 를 String 으로 변환
             String strList = "";
             for(int i=0 ; i<arrayList.size() ; i++)
                 strList += arrayList.get(i);
            // Log.d("asd", "2.변환한수식: " + strList);

            // 숫자만 골라낸다.
            StringTokenizer tokenNumber = new StringTokenizer(strList, "＋－×÷");
            /*String strNum = "";
            while (tokenNumber.hasMoreTokens()){strNum += tokenNumber.nextToken() + " ";}
            Log.d("asd", "3.숫자: " + strNum);*/

            // 연산자만 골라낸다.
            StringTokenizer tokenOperator = new StringTokenizer(strList, "1234567890.");
            /*String strOper = "";
            while (tokenOperator.hasMoreTokens()){strOper += tokenOperator.nextToken() + " ";}\
            Log.d("asd", "4.수식: " + strOper);*/

            // 숫자를 담을 스택
            Stack<Double> stack = new Stack<>();
            // 첫 번째 숫자를 스택에 넣는다.
            stack.push(Double.parseDouble(tokenNumber.nextToken()));
            // 곱셈과 나눗셈이 있으면 스택의 마지막 숫자를 꺼내 계산한 후 계산한 값으로 다시 스택에 넣는다.
            // 뺄셈은 해당 숫자를 -1과 곱셈하여 다시 스택에 넣는다.
            // 최종 스택에는 곱셈과 나눗셈, 뺄셈을 처리한 값만 존재한다.
            while (tokenNumber.hasMoreTokens()){
                String number = tokenNumber.nextToken();    // 피연산자
                String operator = tokenOperator.nextToken();    // 연산자
                Double value;   // 스택에 마지막으로 들어간 숫자

                switch (operator){
                    case "×":
                        value = stack.pop();
                        value *= Double.parseDouble(number);
                        stack.push(value);
                        break;
                    case "÷":
                        value = stack.pop();
                        value /= Double.parseDouble(number);
                        stack.push(value);
                        break;
                    case "＋":
                        stack.push(Double.parseDouble(number));
                        break;
                    case "－":
                        stack.push(-1 * (Double.parseDouble(number)));
                        break;
                    default:
                        break;
                }
            }

            // 뺄셈, 곱셈, 나눗셈을 수행한 값들을 모두 더한다.
            while(!stack.isEmpty()){
                result += stack.pop();
            }

            // 소수점 이하 숫지를 0을 채우지않으며, 14자리까지만 나오게 한다.
            DecimalFormat newFormat = new DecimalFormat("#.##############");
            result =  Double.valueOf(newFormat.format(result));

            // 결과가 정수일 경우 정수형으로 변환
            intResult = (int)result;
            if(result == intResult)
                resultTextView.setText(String.valueOf(intResult));
            else
                resultTextView.setText(String.valueOf(result));
        }
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

        // 분모 텍스트 너비에 맞게 라인 길이 변경
        BottomTextView.measure(0,0);
        LinearLayout.LayoutParams newParams = new LinearLayout.LayoutParams(BottomTextView.getMeasuredWidth(), 3);
        newParams.setMargins(5, 0, 5, 0); // pass int values for left,top,right,bottom
        line.setLayoutParams(newParams);
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

    // 분수 선 조정
    public void changeFractionLine(){
        // 분자와 분모 중 너비가 큰 것에 맞춰 라인을 조정한다.
        TextView topTextView = rootLayout.findViewWithTag("calcFractionTopTextView" + arrayList.size());
        topTextView.measure(0,0);
        int topValueWidth = topTextView.getMeasuredWidth();
        TextView bottomTextView = rootLayout.findViewWithTag("calcFractionBottomTextView" + arrayList.size());
        bottomTextView.measure(0,0);
        int bottomValueWidth = bottomTextView.getMeasuredWidth();

        LinearLayout.LayoutParams newParams;
        if(topValueWidth > bottomValueWidth)
            newParams = new LinearLayout.LayoutParams(topValueWidth, 3);
        else
            newParams = new LinearLayout.LayoutParams(bottomValueWidth, 3);

        newParams.setMargins(5, 0, 5, 0); // pass int values for left,top,right,bottom
        View line = rootLayout.findViewWithTag("calcFractionLine" + arrayList.size());
        line.setLayoutParams(newParams);
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
