package com.tistory.qlyh8.calculator;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tistory.qlyh8.calculator.Utils.CalcFractionUtils;
import com.tistory.qlyh8.calculator.Utils.CalcUtils;
import com.tistory.qlyh8.calculator.Utils.ViewUtils;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

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
    @BindView(R.id.fraction_layout) public LinearLayout fractionLayout; // 결과 분수 레이아웃
    @BindView(R.id.text_denominator) public TextView denominatorTextView;   // 분모 결과값 뷰
    @BindView(R.id.text_numerator) public TextView numeratorTextView;   // 분자 결괴값 뷰
    @BindView(R.id.fraction_view) public View fractionLine;    // 분수 선 뷰
    @BindView(R.id.text_whole) public TextView wholeTextView;   // 대분수 결과값 뷰
    @BindView(R.id.slide_menu) public LinearLayout slideMenu;
    private SlidingRootNav slidingRootNav;

    ArrayList<String> arrayList;    // 값을 저장할 배열 리스트
    double result;  // 결과값
    long longResult;  // 정수 결과값
    long[] fractionResult; // 분수 결과값
    float defaultFractionResultTextSize;    // 분수 결과값 기본 크기

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

        arrayList = new ArrayList<>();
        fractionResult = new long[3];
        resultTextView.setText("0");
        defaultFractionResultTextSize = numeratorTextView.getTextSize();    // 기본 크기 가져오기

        scrollRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                scrollRootView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        viewUtils = new ViewUtils(this, rootLayout);
        calcUtils = new CalcUtils();
        calcFractionUtils = new CalcFractionUtils();

        slidingRootNav = new SlidingRootNavBuilder(this)
                // .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.activity_nav)
                .inject();
        slideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingRootNav.openMenu();
            }
        });

        resultInit();
    }

    // 초기화
    public void resultInit(){
        result = 0d;    // 결과값 초기화
        longResult = 0L;  // 정수 결과값 초기화
        fractionResult[0] = 0L;    // 분수(분자) 결과값 초기화
        fractionResult[1] = 0L;    // 분수(분모) 결과값 초기화
        fractionResult[2] = 0L;    // 분수(대분수) 결과값 초기화
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 값 숨김
    }

    //"0~9" 버튼 클릭
    @SuppressLint("SetTextI18n")
    public void clickBtnNumber(View view) {
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김
        // 수식창이 비었을 때 배열과 뷰에 텍스트를 추가한다.
        if(arrayList.isEmpty()){
            arrayList.add(String.valueOf(view.getTag()));
            viewUtils.setNumTextView(arrayList, String.valueOf(view.getTag()));
        }
        else{
            // 첫 수식이 0일 때 0을 지우고 해당 텍스트로 채운다.
            if(arrayList.size() == 1 && arrayList.get(0).equals("0")){
                arrayList.set(arrayList.size()-1, String.valueOf(view.getTag()));
                viewUtils.changeTextView(arrayList, "calcTextView", String.valueOf(view.getTag()));
            }
            else {
                String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 수식
                String lastStrOfLastVal = lastValue.substring(lastValue.length()-1);    // 마지막 값의 마지막 글자

                // 마지막 수식이 기호일 때 텍스트를 추가한다.
                if(calcUtils.isSymbol(lastValue)){
                    arrayList.add(String.valueOf(view.getTag()));
                    viewUtils.setNumTextView(arrayList, String.valueOf(view.getTag()));
                }
                else{
                    if(lastValue.equals("0")){
                        // 마지막 수식이 0일 때, 0을 지우고 텍스트를 추가한다.
                        arrayList.set(arrayList.size()-1, String.valueOf(view.getTag()));
                        viewUtils.changeTextView(arrayList, "calcTextView", String.valueOf(view.getTag()));
                    }
                    else{
                        // 배열 마지막 값을 변경하고 마지막 뷰에 텍스트를 추가한다.
                        // 분자에 값을 넣는다.
                        if(lastValue.contains("@")){
                            // 마지막 수식의 분자가 0일 때, 0을 지우고 텍스트를 추가한다.
                            if(!lastStrOfLastVal.equals("@") && lastValue.split("@")[1].equals("0")){
                                arrayList.set(arrayList.size()-1,
                                        lastValue.substring(0, lastValue.length()-1) + String.valueOf(view.getTag()));
                                viewUtils.changeTextView(arrayList, "calcFractionTopTextView" , String.valueOf(view.getTag()));
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
                }
            }
        }
    }

    // "." 버튼 클릭
    @SuppressLint("SetTextI18n")
    public void clickBtnPoint(View view){
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김
        // 배열 값이 비어있지 않아야 한다.
        if(!arrayList.isEmpty()){
            String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 값
            String lastStrOfLastVal = lastValue.substring(lastValue.length()-1);    // 마지막 값의 마지막 글자

            // 마지막 값이 기호와 "."이 아니여야 한다.
            if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(".") && !lastStrOfLastVal.equals("@")){
                // 마지막 수식이 분수일 때
                if(lastValue.contains("@")){
                    // "." 은 한 번만 들어가야 한다.
                    if(!lastValue.split("@")[1].contains(".")){
                        arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));
                        viewUtils.appendTextView(arrayList, "calcFractionTopTextView", String.valueOf(view.getTag()));
                        viewUtils.changeFractionLine(arrayList);    // 분자가 분모보다 너비가 커지면 라인을 조정한다.
                    }
                }
                else{
                    // "." 은 한 번만 들어가야 한다.
                    if(!lastValue.contains(".")){
                        arrayList.set(arrayList.size()-1, lastValue + String.valueOf(view.getTag()));
                        viewUtils.appendTextView(arrayList, "calcTextView", String.valueOf(view.getTag()));
                    }
                }
            }
        }
    }

    // "ㅡ" (분수) 버튼 클릭
    public void clickBtnFraction(View view){
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김
        // 배열 값이 비어있지 않아야 한다.
        if(!arrayList.isEmpty()){
            // 수식이 0일 때 입력되지 않아야 한다.
            if(!(arrayList.size() == 1 && arrayList.get(0).equals("0"))){

                String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 값
                String lastStrOfLastVal = lastValue.substring(lastValue.length()-1);    // 마지막 수식의 마지막 글자

                if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(".")){
                    if(!lastValue.equals("0") && !lastValue.contains("@")){
                        // 분수 뷰로 변경한다. (분모) @ (분자)
                        arrayList.set(arrayList.size()-1, lastValue+"@");
                        viewUtils.removeView(arrayList, "textView", "calcTextView");
                        viewUtils.removeView(arrayList, "layout", "calcLayout");
                        viewUtils.setFractionTextView(arrayList, lastValue);
                    }
                }
            }
        }
    }

    // "+,－,×,÷" 버튼 클릭
    public void clickBtnSymbol(View view) {
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김
        // 배열 값이 비어있을 때 기호가 들어가면 안된다.
        if(!arrayList.isEmpty()){
            String lastValue = arrayList.get(arrayList.size()-1);   // 마지막 값
            String lastStrOfLastVal = lastValue.substring(lastValue.length()-1);    // 마지막 수식의 마지막 글자

            // 마지막 값이 기호가 아니고 "."이 아니여야 한다.
            if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(".") && !lastStrOfLastVal.equals("@")){
                // 마지막 값이 분수일 때, 분수 안에는 기호가 들어가면 안된다.
                if(lastValue.contains("@") && !lastStrOfLastVal.equals("@")){
                    arrayList.add(String.valueOf(view.getTag()));
                    viewUtils.setSymbolTextView(arrayList, String.valueOf(view.getTag()));
                }
                else if(!(arrayList.size() >= 3 && arrayList.get(arrayList.size()-2).equals("÷") && lastValue.equals("0"))){
                    // 0으로 나눌 수 없다.
                    arrayList.add(String.valueOf(view.getTag()));
                    viewUtils.setSymbolTextView(arrayList, String.valueOf(view.getTag()));
                }
            }
        }
    }

    // "←" 버튼 클릭
    public void clickBtnClear(View view) {
        fractionLayout.setVisibility(View.INVISIBLE);   // 분수 결과값 숨김
        // 배열 값이 비어있지 않아야 한다.
        if(!arrayList.isEmpty()){
            String lastValue = arrayList.get(arrayList.size()-1);   // 마지막 값

            // 값의 길이가 1일 때 해당 뷰와 해당 배열 위치의 값을 제거한다.
            if(lastValue.length() == 1){
                viewUtils.removeView(arrayList, "textView", "calcTextView");
                viewUtils.removeView(arrayList, "layout", "calcLayout");
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
                        viewUtils.removeView(arrayList, "textView", "calcFractionTopTextView");
                        viewUtils.removeView(arrayList, "textView", "calcFractionBottomTextView");
                        viewUtils.removeView(arrayList, "line", "calcFractionLine");
                        viewUtils.removeView(arrayList, "layout", "calcFractionLayout");
                        viewUtils.setNumTextView(arrayList, bottomValue);
                    }
                    else{
                        // 분모, 분자에 모두 값이 있을 때 ( ex: 3 6 @ 2 4 ) 분자를 지운다.
                        value = lastValue.split("@");
                        String topValue = value[1]; // 분자

                        arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length()-1));
                        viewUtils.changeTextView(arrayList,"calcFractionTopTextView", topValue.substring(0, topValue.length()-1));
                        viewUtils.changeFractionLine(arrayList);
                    }
                }
                else {
                    // 배열 마지막 값과 마지막 뷰를 변경한다.
                    arrayList.set(arrayList.size()-1, lastValue.substring(0, lastValue.length()-1));
                    viewUtils.changeTextView(arrayList, "calcTextView", lastValue.substring(0, lastValue.length()-1));
                }
            }
        }
    }

    // "C" 버튼 클릭
    public void clickBtnAllClear(View view) {
        rootLayout.removeAllViews();    // 수식 뷰 안의 모든 뷰 삭제
        resultTextView.setText("0");    // 결과 값을 0으로 초기화
        arrayList.clear();  // 리스트의 모든 내용 삭제
        resultInit(); // 결과값 초기화
    }

    // "=" 버튼 클릭
    public void clickBtnResult(View view) {
        // 배열 값이 비어있지 않아야 한다.
        if(!arrayList.isEmpty()) {
            String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 수식
            String lastStrOfLastVal = lastValue.substring(lastValue.length()-1);    // 마지막 수식의 마지막 글자

            // 마지막 수식은 숫자여야 하고, 0으로 나눌 수 없다.
            if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(".") && !lastStrOfLastVal.equals("@")
                    && !(arrayList.size() >= 3 && arrayList.get(arrayList.size()-2).equals("÷") && lastValue.equals("0"))){

                // 초기화 ("="을 입력한 후 "C" 버튼을 누르지 않고 계속 수식을 입력할 경우를 위해)
                resultInit();

                // "@"를 "÷"으로 변환
                String strList = calcUtils.convertToDivide(arrayList);

                // 사칙연산
                try {
                    result = calcUtils.calculate(strList);
                }
                catch (Exception e){
                    result = 0;
                    longResult = 0L;
                }

                // 결과가 정수일 경우 정수형으로 변환
                longResult = (long)result;
                if(result == longResult)
                    resultTextView.setText(String.valueOf(longResult));
                else
                    resultTextView.setText(String.valueOf(result));

                // 분수 사칙연산
                try {
                    fractionResult = calcFractionUtils.fractionCalculate(arrayList);

                    numeratorTextView.setText(String.valueOf(fractionResult[0])); // 분자
                    numeratorTextView.setTextSize(defaultFractionResultTextSize);   // 화면 크기에 맞춤
                    denominatorTextView.setText(String.valueOf(fractionResult[1]));   // 분모
                    denominatorTextView.setTextSize(defaultFractionResultTextSize); // 화면 크기에 맞춤
                    //viewUtils.setResultFractionLine(fractionLine, numeratorTextView, denominatorTextView);  // 분수선 조정
                    if(fractionResult[2] != 0){
                        wholeTextView.setText(String.valueOf(fractionResult[2]));   // 대분수
                        wholeTextView.setTextSize(defaultFractionResultTextSize);   // 화면 크기에 맞춤
                    }
                    else{
                        wholeTextView.setText("");   // 대분수
                    }

                    fractionLayout.setVisibility(View.VISIBLE);
                }
                catch (Exception e){
                    fractionResult[0] = 0L;
                    fractionResult[1] = 0L;
                    fractionResult[2] = 0L;
                    fractionLayout.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
