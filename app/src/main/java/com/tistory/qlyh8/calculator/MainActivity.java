package com.tistory.qlyh8.calculator;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tistory.qlyh8.calculator.model.HistoryObject;
import com.tistory.qlyh8.calculator.utils.CalcFractionUtils;
import com.tistory.qlyh8.calculator.utils.CalcUtils;
import com.tistory.qlyh8.calculator.utils.HistoryStorageUtil;
import com.tistory.qlyh8.calculator.utils.NavUtils;
import com.tistory.qlyh8.calculator.utils.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitHelper;

/*
 * Created by YUNHEE on 2018-01-12.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.root_calc_scroll) public HorizontalScrollView scrollRootView; // 스크롤 뷰
    @BindView(R.id.layout_root_calc) public LinearLayout rootLayout;    // 수식 뷰
    @BindView(R.id.resultView) public TextView resultTextView;  // 결과값 뷰
    @BindView(R.id.fraction_layout) public ConstraintLayout fractionLayout; // 분수 결과값 레이아웃
    @BindView(R.id.text_denominator) public TextView denominatorTextView;   // 분모 결과값 뷰
    @BindView(R.id.text_numerator) public TextView numeratorTextView;   // 분자 결괴값 뷰
    @BindView(R.id.text_whole) public TextView wholeTextView;   // 대분수 결과값 뷰
    @BindView(R.id.slide_menu) public LinearLayout slideMenu;
    private NavUtils navUtils = new NavUtils();

    ArrayList<String> arrayList;    // 값을 저장할 배열 리스트
    String result;  // 결과값
    String[] fractionResult; // 분수 결과값
    float defaultFractionTextSize;  // 분수 결과값 기본 텍스트 크기

    String zero, point, minus, divide, fraction, plusMinus;

    ViewUtils viewUtils;
    CalcUtils calcUtils;
    CalcFractionUtils calcFractionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 화면 꺼짐 방지

        AutofitHelper.create(resultTextView);   // 텍스트 길이 자동 조정
        AutofitHelper.create(numeratorTextView);
        AutofitHelper.create(denominatorTextView);
        AutofitHelper.create(wholeTextView);

        zero = getResources().getString(R.string.zero); // "0"
        point = getResources().getString(R.string.point);   // "."
        minus = getResources().getString(R.string.subtract);    // "－"
        divide = getResources().getString(R.string.divide); // "÷"
        fraction = getResources().getString(R.string.fractionOperator); // "@"
        plusMinus = getResources().getString(R.string.plusMinus);   // "±"

        arrayList = new ArrayList<>();
        fractionResult = new String[3];
        resultTextView.setText(zero);
        defaultFractionTextSize = numeratorTextView.getTextSize();

        scrollRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                scrollRootView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        viewUtils = new ViewUtils(this, rootLayout);
        calcUtils = new CalcUtils(this);
        calcFractionUtils = new CalcFractionUtils(this);

        navUtils.bind(MainActivity.this,savedInstanceState);
        slideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navUtils.getSlidingRootNav().openMenu();
            }
        });

        resultInit();
        textWidthInit();
    }

    // 초기화
    public void resultInit(){
        result = zero;    // 결과값 초기화
        fractionResult = new String[]{zero, zero, zero};    // 분수 결과값 초기화
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 값 숨김
    }

    // 수식 텍스트 기본 너비 구하기 (분수선 조정하기 위해 필요)
    public void textWidthInit(){
        viewUtils.setNumTextView(arrayList, zero);
        TextView textView = rootLayout.findViewWithTag("calcTextView"+zero);
        textView.measure(0,0);
        viewUtils.defaultTextWidth = textView.getMeasuredWidth();
        rootLayout.removeAllViews();
    }

    //"0~9" 버튼 클릭
    @SuppressLint("SetTextI18n")
    public void clickBtnNumber(View view) {
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김

        // 수식창이 비었을 때 배열과 뷰에 텍스트를 추가한다.
        if(arrayList.isEmpty()){
            arrayList.add(String.valueOf(view.getTag()));
            viewUtils.setNumTextView(arrayList, String.valueOf(view.getTag()));
            return;
        }

        String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 수식
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 마지막 수식이 기호일 때 텍스트를 추가한다.
        if(calcUtils.isSymbol(lastValue)){
            arrayList.add(String.valueOf(view.getTag()));
            viewUtils.setNumTextView(arrayList, String.valueOf(view.getTag()));
            return;
        }

        // 마지막 수식이 0일 때, 0을 지우고 텍스트를 추가한다.
        if(lastValue.equals(zero)){
            arrayList.set(arrayList.size()-1, String.valueOf(view.getTag()));
            viewUtils.changeTextView(arrayList, "calcTextView", String.valueOf(view.getTag()));
            return;
        }

        // 마지막 수식이 "-0"일 때, 0을 지우고 텍스트를 추가한다.
        if(lastValue.equals(plusMinus+zero)){
            arrayList.set(arrayList.size()-1, plusMinus + String.valueOf(view.getTag()));
            viewUtils.changeTextView(arrayList, "calcTextView", minus + String.valueOf(view.getTag()));
            return;
        }

        // 분수일 때, 분자에 값을 넣는다.
        if(lastValue.contains(fraction)){
            // 마지막 수식의 분자가 "0"일 때, 0을 지우고 텍스트를 추가한다.
            if(!lastStrOfLastVal.equals(fraction) && lastValue.split(fraction)[1].equals(zero)) {
                arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length()-1) + String.valueOf(view.getTag()));
                viewUtils.changeTextView(arrayList, "calcFractionTopTextView", String.valueOf(view.getTag()));
            }
            else if(!lastStrOfLastVal.equals(fraction) && lastValue.split(fraction)[1].equals(plusMinus+zero)){
                // 마지막 수식의 분자가 "－0"일 때, 0을 지우고 텍스트를 추가한다.
                arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length()-1) + String.valueOf(view.getTag()));
                viewUtils.changeTextView(arrayList, "calcFractionTopTextView", minus + String.valueOf(view.getTag()));
            }
            else {
                arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));
                viewUtils.appendTextView(arrayList, "calcFractionTopTextView", String.valueOf(view.getTag()));
            }
            viewUtils.changeFractionLine(arrayList);    // 분자가 분모보다 너비가 커지면 라인을 조정한다.
        }
        else{
            arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));
            viewUtils.appendTextView(arrayList, "calcTextView", String.valueOf(view.getTag()));
        }
    }

    // "." 버튼 클릭
    @SuppressLint("SetTextI18n")
    public void clickBtnPoint(View view){
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김

        // 배열 값이 비어있지 않아야 한다.
        if(arrayList.isEmpty())
            return;

        String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 값
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 마지막 값이 기호와 "."이 아니여야 하고, "."은 한 번만 들어가야 한다.
        if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(plusMinus)
                && !lastStrOfLastVal.equals(point) && !lastStrOfLastVal.equals(fraction)){
            // 마지막 수식이 분수일 때
            if(lastValue.contains(fraction) && !lastValue.split(fraction)[1].contains(point)){
                arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));
                viewUtils.appendTextView(arrayList, "calcFractionTopTextView", String.valueOf(view.getTag()));
                viewUtils.changeFractionLine(arrayList);    // 분자가 분모보다 너비가 커지면 라인을 조정한다.
            }
            else{ // 분수가 아닐 때
                if(!lastValue.contains(point)){
                    arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));
                    viewUtils.appendTextView(arrayList, "calcTextView", String.valueOf(view.getTag()));
                }
            }
        }
    }

    // "±" 버튼 클릭
    public void clickBtnPlusMinus(View view) {
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김

        // 수식창이 비었을 때 배열과 뷰에 텍스트를 추가한다.
        if(arrayList.isEmpty()){
            arrayList.add(String.valueOf(view.getTag()));
            viewUtils.setNumTextView(arrayList, minus);
            return;
        }

        String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 수식
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 마지막 수식이 연산기호일 때
        if(calcUtils.isSymbol(lastValue)){
            arrayList.add(String.valueOf(view.getTag()));
            viewUtils.setNumTextView(arrayList, minus);
            return;
        }

        if(lastValue.contains(fraction)){ // 분수일 때
            if(lastStrOfLastVal.equals(fraction)){
                arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));
                viewUtils.changeTextView(arrayList, "calcFractionTopTextView", minus);
            } else {
                String numerator = lastValue.split(fraction)[1];
                if(numerator.contains(plusMinus)){
                    arrayList.set(arrayList.size()-1, lastValue.split(fraction)[0] + fraction + numerator.substring(1));
                    viewUtils.changeTextView(arrayList,"calcFractionTopTextView", numerator.substring(1));
                } else {
                    arrayList.set(arrayList.size()-1,
                            lastValue.split(fraction)[0] + fraction + String.valueOf(view.getTag()) + numerator);
                    viewUtils.changeTextView(arrayList,"calcFractionTopTextView", minus + numerator);
                }
                viewUtils.changeFractionLine(arrayList);    // 분자가 분모보다 너비가 커지면 라인을 조정한다.
            }
        }
        else{ // 분수가 아닐 때
            if(lastValue.contains(plusMinus)){
                if(lastValue.equals(plusMinus)){
                    viewUtils.removeView(arrayList, "textView", "calcTextView");
                    viewUtils.removeView(arrayList, "layout", "calcLayout");
                    arrayList.remove(arrayList.size()-1);
                } else {
                    arrayList.set(arrayList.size()-1, lastValue.substring(1));
                    viewUtils.changeTextView(arrayList, "calcTextView", lastValue.substring(1));
                }
            } else{
                arrayList.set(arrayList.size()-1, String.valueOf(view.getTag()) + lastValue);
                viewUtils.changeTextView(arrayList, "calcTextView", minus + lastValue);
            }
        }
    }

    // "+,－,×,÷" 버튼 클릭
    public void clickBtnSymbol(View view) {
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김

        // 배열 값이 비어있지 않아야 한다.
        if(arrayList.isEmpty())
            return;

        String lastValue = arrayList.get(arrayList.size()-1);   // 마지막 값
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 마지막 값이 기호가 아니고 "."이 아니여야 한다.
        if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(point)
                && !lastStrOfLastVal.equals(plusMinus) && !lastStrOfLastVal.equals(fraction)){
           if(arrayList.size() >= 2 && arrayList.get(arrayList.size()-2).equals(divide)){
                    // 0으로 나눌 수 없다.
                if(!lastValue.equals(zero) && !lastValue.equals(plusMinus + zero)){
                    arrayList.add(String.valueOf(view.getTag()));
                    viewUtils.setSymbolTextView(arrayList, String.valueOf(view.getTag()));
                }
            } else {
                arrayList.add(String.valueOf(view.getTag()));
                viewUtils.setSymbolTextView(arrayList, String.valueOf(view.getTag()));
            }
        }
    }

    // "ㅡ" (분수) 버튼 클릭
    public void clickBtnFraction(View view){
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김

        // 배열 값이 비어있지 않아야 한다.
        if(arrayList.isEmpty())
            return;

        String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 값
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 마지막 수식이 연산기호, ".", "0", "-0", 분수이면 안된다.
        if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(point)
                && !lastValue.equals(zero) && !lastValue.equals(plusMinus+zero)
                && !lastValue.contains(fraction)) {
            // 분수 뷰로 변경한다. (분모) @ (분자)
            arrayList.set(arrayList.size()-1, lastValue + fraction);
            viewUtils.removeView(arrayList, "textView", "calcTextView");
            viewUtils.removeView(arrayList, "layout", "calcLayout");
            if(lastValue.contains(plusMinus))
                viewUtils.setFractionTextView(arrayList, lastValue.replace(plusMinus, minus));
            else
                viewUtils.setFractionTextView(arrayList, lastValue);
        }
    }

    // "←" 버튼 클릭
    public void clickBtnClear(View view) {
        resultInit();   // 결과값 초기화

        // 배열 값이 비어있지 않아야 한다.
        if(arrayList.isEmpty())
            return;

        String lastValue = arrayList.get(arrayList.size()-1);   // 마지막 값

        // 값의 길이가 1보다 작을 때, 해당 뷰와 해당 배열 위치의 값을 제거한다.
        if(lastValue.length() <= 1) {
            viewUtils.removeView(arrayList, "textView", "calcTextView");
            viewUtils.removeView(arrayList, "layout", "calcLayout");
            arrayList.remove(arrayList.size() - 1);
            return;
        }

        // 분수일 때
        if(lastValue.contains(fraction)) {
            String[] value;

            // 분자에 값이 없을 때 ( ex: 3 6 @ ) line 을 삭제하고 자연수로 만든다.
            if(lastValue.substring(lastValue.length()-1).equals(fraction)){
                value = lastValue.split(fraction);
                String bottomValue = value[0]; // 분모

                arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length() - 1));
                viewUtils.removeView(arrayList, "textView", "calcFractionTopTextView");
                viewUtils.removeView(arrayList, "textView", "calcFractionBottomTextView");
                viewUtils.removeView(arrayList, "line", "calcFractionLine");
                viewUtils.removeView(arrayList, "layout", "calcFractionLayout");
                if(bottomValue.contains(plusMinus))
                    viewUtils.setNumTextView(arrayList, bottomValue.replace(plusMinus, minus));
                else
                    viewUtils.setNumTextView(arrayList, bottomValue);
            }
            else{
                // 분모, 분자에 모두 값이 있을 때 ( ex: 3 6 @ 2 4 ) 분자를 지운다.
                value = lastValue.split(fraction);
                String topValue = value[1]; // 분자

                arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length()-1));
                if(topValue.contains(plusMinus))
                    viewUtils.changeTextView(arrayList,"calcFractionTopTextView", topValue.substring(0, topValue.length()-1).replace(plusMinus, minus));
                else
                    viewUtils.changeTextView(arrayList,"calcFractionTopTextView", topValue.substring(0, topValue.length()-1));
                viewUtils.changeFractionLine(arrayList);
            }
        }
        else {
            // 배열 마지막 값과 마지막 뷰를 변경한다.
            arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length()-1));
            if(lastValue.contains(plusMinus))
                viewUtils.changeTextView(arrayList, "calcTextView", lastValue.substring(0, lastValue.length()-1).replace(plusMinus, minus));
            else
                viewUtils.changeTextView(arrayList, "calcTextView", lastValue.substring(0, lastValue.length()-1));
        }
    }

    // "C" 버튼 클릭
    public void clickBtnAllClear(View view) {
        rootLayout.removeAllViews();    // 수식 뷰 안의 모든 뷰 삭제
        resultTextView.setText(zero);    // 결과 값을 0으로 초기화
        arrayList.clear();  // 리스트의 모든 내용 삭제
        resultInit(); // 결과값 초기화
    }

    // "=" 버튼 클릭
    public void clickBtnResult(View view) {
        // 배열 값이 비어있지 않아야 한다.
        if(arrayList.isEmpty())
            return;

        String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 수식
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 마지막 수식은 숫자여야 하고, 0으로 나눌 수 없다.
        if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(point) && !lastStrOfLastVal.equals(fraction)
                && !(arrayList.size() >= 2 && arrayList.get(arrayList.size()-2).equals(divide) && lastValue.equals(zero))){

            // 초기화 ("="을 입력한 후 "C" 버튼을 누르지 않고 계속 수식을 입력할 경우를 위해)
            resultInit();

            // "@"를 "÷"으로 변환
            String strList = calcUtils.convertToDivide(arrayList);
            try {
                result = calcUtils.calculate(strList);  // 사칙연산
            }
            catch (Exception e){
                result = zero;
                Log.e("asd", "calcUtils.calculate(): " + e);
            }
            resultTextView.setText(result);

            // 분수 사칙연산
            try {
                fractionResult = calcFractionUtils.fractionCalculate(arrayList);

                numeratorTextView.setText(fractionResult[0]); // 분자
                denominatorTextView.setText(fractionResult[1]);   // 분모
                if(!fractionResult[2].equals(zero))
                    wholeTextView.setText(fractionResult[2]);   // 대분수
                else
                    wholeTextView.setText("");   // 대분수

                // 화면에 맞게 텍스트크기 조정
                numeratorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultFractionTextSize);
                denominatorTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, numeratorTextView.getTextSize());
                wholeTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, numeratorTextView.getTextSize());
                fractionLayout.setVisibility(View.VISIBLE);
            }
            catch (Exception e){
                fractionResult = new String[]{zero, zero, zero};
                fractionLayout.setVisibility(View.INVISIBLE);
                Log.e("asd", "calcFractionUtils.fractionCalculate(): " + e);
            }

            setHistoryData(arrayList, result, fractionResult);
        }
    }

    public void setHistoryData(ArrayList<String> calc, String result, String[] fractionResult) {
        HistoryObject tempData;
        ArrayList<String> historyCalcList = new ArrayList<>();
        historyCalcList.addAll(calc);
        tempData = new HistoryObject(historyCalcList, result, fractionResult);
        HistoryStorageUtil.historyRes.add(tempData);
    }
}
