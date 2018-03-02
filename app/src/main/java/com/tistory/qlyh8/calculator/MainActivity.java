package com.tistory.qlyh8.calculator;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.tistory.qlyh8.calculator.model.HistoryObject;
import com.tistory.qlyh8.calculator.utils.CalcFractionUtils;
import com.tistory.qlyh8.calculator.utils.CalcUtils;
import com.tistory.qlyh8.calculator.utils.HistoryStorageUtil;
import com.tistory.qlyh8.calculator.utils.NavUtils;
import com.tistory.qlyh8.calculator.utils.PreferenceUtils;
import com.tistory.qlyh8.calculator.utils.ThemeUtil;
import com.tistory.qlyh8.calculator.utils.ViewUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitHelper;

/*
 * Created by YUNHEE on 2018-01-12.
 */

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {

    @BindView(R.id.root_calc_scroll) public HorizontalScrollView scrollRootView; // 스크롤 뷰
    @BindView(R.id.layout_root_calc) public LinearLayout rootLayout;    // 수식 뷰
    @BindView(R.id.resultView) public TextView resultTextView;  // 결과값 뷰
    @BindView(R.id.fraction_layout) public ConstraintLayout fractionLayout; // 분수 결과값 레이아웃
    @BindView(R.id.text_denominator) public TextView denominatorTextView;   // 분모 결과값 뷰
    @BindView(R.id.text_numerator) public TextView numeratorTextView;   // 분자 결괴값 뷰
    @BindView(R.id.text_whole) public TextView wholeTextView;   // 대분수 결과값 뷰
    @BindView(R.id.text_view) public View lineTextView;   // 분수 결과값 라인
    @BindView(R.id.slide_menu) public LinearLayout slideMenu;

    //** Theme **
    @BindView(R.id.result_pannel) public LinearLayout resultPanel;
    @BindView(R.id.btn_add) public Button btnAdd;
    @BindView(R.id.btn_substract) public Button btnSubstract;
    @BindView(R.id.btn_multiply) public Button btnMultiply;
    @BindView(R.id.btn_divide) public Button btnDivide;
    @BindView(R.id.btnPlusMinus) public Button btnPlusMinus;
    @BindView(R.id.btn_point) public Button btnPoint;
    @BindView(R.id.btn_back) public Button btnBack;
    @BindView(R.id.btn_clear) public Button btnClear;
    @BindView(R.id.btn_fraction) public Button btnFraction;
    @BindView(R.id.btn_result) public Button btnResult;
    @BindView(R.id.slide_text) public ImageView slideText;
    @BindView(R.id.image_back) public ImageView imageBack;
    @BindView(R.id.result_equal) public ImageView resultEqual;
    @BindView(R.id.result_bg) public ImageView resultBg;

    @BindView(R.id.btn0) public Button btn0;@BindView(R.id.btn1) public Button btn1;@BindView(R.id.btn2) public Button btn2;@BindView(R.id.btn3) public Button btn3;
    @BindView(R.id.btn4) public Button btn4;@BindView(R.id.btn5) public Button btn5;@BindView(R.id.btn6) public Button btn6;@BindView(R.id.btn7) public Button btn7;
    @BindView(R.id.btn8) public Button btn8;@BindView(R.id.btn9) public Button btn9;

    private ArrayList<String> arrayList;    // 값을 저장할 배열 리스트
    private String result;  // 결과값
    private String[] fractionResult; // 분수 결과값
    private float defaultFractionTextSize;  // 분수 결과값 기본 텍스트 크기

    private String zero, point, minus, divide, fraction, plusMinus, fractionWhole;

    private ViewUtils viewUtils;
    private CalcUtils calcUtils;
    private CalcFractionUtils calcFractionUtils;
    private PreferenceUtils preferenceUtils;
    private NavUtils navUtils = new NavUtils();

    private InterstitialAd mInterstitialAd;

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

        MobileAds.initialize(this, "ca-app-pub-8190322516789280~8271453833");
        interstitialAd();

        scrollRootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                scrollRootView.fullScroll(View.FOCUS_RIGHT);
            }
        });

        viewUtils = new ViewUtils(this, rootLayout);
        calcUtils = new CalcUtils(this);
        calcFractionUtils = new CalcFractionUtils(this);
        preferenceUtils = new PreferenceUtils(this);
        preferenceUtils.setupSharedPreferences();

        slideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navUtils.getSlidingRootNav().openMenu();
            }
        });

        valueInit();
        resultInit();
        textWidthInit();
        setLongClickInit();

        navUtils.bind(MainActivity.this,savedInstanceState);
    }

    // 전면광고
    public void interstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8190322516789280/5691971074");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded(){
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("asd", "The interstitial wasn't loaded yet.");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTheme();
        navUtils.setTheme();
    }

    private void setTheme() {
        //Theme
        resultPanel.setBackground(getDrawable(ThemeUtil.themeBackground));
        //keyPadTheme
        btn0.setBackground(getDrawable(ThemeUtil.themeBackground));
        btn1.setBackground(getDrawable(ThemeUtil.themeBackground));
        btn2.setBackground(getDrawable(ThemeUtil.themeBackground));
        btn3.setBackground(getDrawable(ThemeUtil.themeBackground));
        btn4.setBackground(getDrawable(ThemeUtil.themeBackground));
        btn5.setBackground(getDrawable(ThemeUtil.themeBackground));
        btn6.setBackground(getDrawable(ThemeUtil.themeBackground));
        btn7.setBackground(getDrawable(ThemeUtil.themeBackground));
        btn8.setBackground(getDrawable(ThemeUtil.themeBackground));
        btn9.setBackground(getDrawable(ThemeUtil.themeBackground));
        btnPlusMinus.setBackground(getDrawable(ThemeUtil.themeBackground));
        btnPoint.setBackground(getDrawable(ThemeUtil.themeBackground));

        btnAdd.setBackground(getDrawable(ThemeUtil.themeBackground));
        btnSubstract.setBackground(getDrawable(ThemeUtil.themeBackground));
        btnMultiply.setBackground(getDrawable(ThemeUtil.themeBackground));
        btnDivide.setBackground(getDrawable(ThemeUtil.themeBackground));
        btnBack.setBackground(getDrawable(ThemeUtil.themeBackground));
        btnClear.setBackground(getDrawable(ThemeUtil.themeBackground));
        btnFraction.setBackground(getDrawable(ThemeUtil.themeBackground));
        btnResult.setBackground(getDrawable(ThemeUtil.themeBackground));

        int keypadTheme = ThemeUtil.themeNumTextColor;
        //TextColorTheme
        btn0.setTextColor(getResources().getColor(keypadTheme));
        btn1.setTextColor(getResources().getColor(keypadTheme));
        btn2.setTextColor(getResources().getColor(keypadTheme));
        btn3.setTextColor(getResources().getColor(keypadTheme));
        btn4.setTextColor(getResources().getColor(keypadTheme));
        btn5.setTextColor(getResources().getColor(keypadTheme));
        btn6.setTextColor(getResources().getColor(keypadTheme));
        btn7.setTextColor(getResources().getColor(keypadTheme));
        btn8.setTextColor(getResources().getColor(keypadTheme));
        btn9.setTextColor(getResources().getColor(keypadTheme));
        btnPlusMinus.setTextColor(getResources().getColor(keypadTheme));
        btnPoint.setTextColor(getResources().getColor(keypadTheme));
        resultTextView.setTextColor(getResources().getColor(keypadTheme));

        btnAdd.setTextColor(getResources().getColor(ThemeUtil.themeTextColor));
        btnSubstract.setTextColor(getResources().getColor(ThemeUtil.themeTextColor));
        btnMultiply.setTextColor(getResources().getColor(ThemeUtil.themeTextColor));
        btnDivide.setTextColor(getResources().getColor(ThemeUtil.themeTextColor));
        btnClear.setTextColor(getResources().getColor(ThemeUtil.themeTextColor));
        btnFraction.setTextColor(getResources().getColor(ThemeUtil.themeTextColor));
        btnResult.setTextColor(getResources().getColor(ThemeUtil.themeTextColor));
        imageBack.setColorFilter(getResources().getColor(ThemeUtil.themeTextColor));

        //result
        resultBg.setColorFilter(getResources().getColor(ThemeUtil.themeTextColor));
        //resultEqual.setColorFilter(getResources().getColor(keypadTheme));

        //Slide Menu
        slideMenu.setBackground(getDrawable(ThemeUtil.themeSlideMenuBg));
        //slideText.setColorFilter(ThemeUtil.themeSlideMenuText);

        // 분수 결과값
        denominatorTextView.setTextColor(getResources().getColor(ThemeUtil.themeResultTextColor));
        numeratorTextView.setTextColor(getResources().getColor(ThemeUtil.themeResultTextColor));
        wholeTextView.setTextColor(getResources().getColor(ThemeUtil.themeResultTextColor));
        lineTextView.setBackground(getDrawable(ThemeUtil.themeResultTextColor));
    }

    // 변수 초기화
    public void valueInit(){
        zero = getResources().getString(R.string.zero); // "0"
        point = getResources().getString(R.string.point);   // "."
        minus = getResources().getString(R.string.subtract);    // "－"
        divide = getResources().getString(R.string.divide); // "÷"
        fraction = getResources().getString(R.string.fractionOperator); // "@"
        plusMinus = getResources().getString(R.string.plusMinus);   // "±"
        fractionWhole = getResources().getString(R.string.fractionWholeOperator);   // "#"

        arrayList = new ArrayList<>();
        fractionResult = new String[3];
        resultTextView.setText(zero);
        numeratorTextView.setText(zero);
        denominatorTextView.setText("1");
        wholeTextView.setText("");
        defaultFractionTextSize = numeratorTextView.getTextSize();
    }

    // 결과값 초기화
    public void resultInit(){
        result = zero;
        fractionResult = new String[]{zero, "1", zero};
    }

    // 수식 텍스트 기본 너비 구하기 (분수선 조정하기 위해 필요)
    public void textWidthInit(){
        viewUtils.setNumTextView(arrayList, zero);
        TextView textView = rootLayout.findViewWithTag("calcTextView"+zero);
        textView.measure(0,0);
        viewUtils.defaultTextWidth = textView.getMeasuredWidth();
        rootLayout.removeAllViews();
    }

    // 롱클릭 리스너 등록
    public void setLongClickInit(){
        findViewById(R.id.btn0).setOnLongClickListener(this);
        findViewById(R.id.btn1).setOnLongClickListener(this);
        findViewById(R.id.btn2).setOnLongClickListener(this);
        findViewById(R.id.btn3).setOnLongClickListener(this);
        findViewById(R.id.btn4).setOnLongClickListener(this);
        findViewById(R.id.btn5).setOnLongClickListener(this);
        findViewById(R.id.btn6).setOnLongClickListener(this);
        findViewById(R.id.btn7).setOnLongClickListener(this);
        findViewById(R.id.btn8).setOnLongClickListener(this);
        findViewById(R.id.btn9).setOnLongClickListener(this);
        findViewById(R.id.btnPlusMinus).setOnLongClickListener(this);
    }

    //"0~9" 버튼 클릭
    @SuppressLint("SetTextI18n")
    public void clickBtnNumber(View view) {
        preferenceUtils.setVibration();

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

        // 대분수 있을 시 입력할 수 없다.
        if(lastValue.contains(fractionWhole))
            return;

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
        preferenceUtils.setVibration();

        // 배열 값이 비어있지 않아야 한다.
        if(arrayList.isEmpty())
            return;

        String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 값
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 대분수 있을 시 입력할 수 없다.
        if(lastValue.contains(fractionWhole))
            return;

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
        preferenceUtils.setVibration();

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

        // 대분수 있을 시 입력할 수 없다.
        if(lastValue.contains(fractionWhole))
            return;

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
            }
            viewUtils.changeFractionLine(arrayList);    // 분자가 분모보다 너비가 커지면 라인을 조정한다.
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
            } else {
                arrayList.set(arrayList.size()-1, String.valueOf(view.getTag()) + lastValue);
                viewUtils.changeTextView(arrayList, "calcTextView", minus + lastValue);
            }
        }
    }

    // "+,－,×,÷" 버튼 클릭
    public void clickBtnSymbol(View view) {
        preferenceUtils.setVibration();

        // 배열 값이 비어있지 않아야 한다.
        if(arrayList.isEmpty())
            return;

        String lastValue = arrayList.get(arrayList.size()-1);   // 마지막 값
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 대분수 있을 때
        if(lastValue.contains(fractionWhole)){
            arrayList.add(String.valueOf(view.getTag()));
            viewUtils.setSymbolTextView(arrayList, String.valueOf(view.getTag()));
            return;
        }

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

    // "F" (분수) 버튼 클릭
    public void clickBtnFraction(View view){
        preferenceUtils.setVibration();

        // 배열 값이 비어있지 않아야 한다.
        if(arrayList.isEmpty())
            return;

        String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 값
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 대분수 있을 시 입력할 수 없다.
        if(lastValue.contains(fractionWhole))
            return;

        // 마지막 수식이 연산기호, ".", "0", "-0", 분수이면 안된다.
        if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(point)
                && !lastValue.equals(zero) && !lastValue.contains(fraction)
                && !lastValue.equals(plusMinus) && !lastValue.equals(plusMinus+zero)) {
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
        preferenceUtils.setVibration();
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
                viewUtils.removeView(arrayList, "textView", "calcFractionWholeTextView");
                viewUtils.removeView(arrayList, "line", "calcFractionLine");
                viewUtils.removeView(arrayList, "layout", "calcFractionLayout");
                viewUtils.removeView(arrayList, "layout", "calcWholeLayout");
                viewUtils.removeView(arrayList, "layout", "calcLayout");
                if(bottomValue.contains(plusMinus))
                    viewUtils.setNumTextView(arrayList, bottomValue.replace(plusMinus, minus));
                else
                    viewUtils.setNumTextView(arrayList, bottomValue);
            }
            else if(lastValue.contains(fractionWhole)) {    // 대분수 값이 존재할 때
                value = lastValue.split(fractionWhole); // 0: 대분수     1: 분자,분모

                if(value[0].length() == 1){ // ( ex: 1 # 3 6 @ 2 4 )
                    arrayList.set(arrayList.size()-1, value[1]);
                    viewUtils.changeTextView(arrayList,"calcFractionWholeTextView", "");
                }
                else {  // ( ex: 1 2 # 3 6 @ 2 4 )
                    String newWhole = value[0].substring(0, value[0].length()-1);
                    arrayList.set(arrayList.size()-1, newWhole + fractionWhole + value[1]);
                    if(newWhole.contains(plusMinus))
                        viewUtils.changeTextView(arrayList,"calcFractionWholeTextView", newWhole.replace(plusMinus, minus));
                    else
                        viewUtils.changeTextView(arrayList,"calcFractionWholeTextView", newWhole);
                }
            }
            else {
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
        preferenceUtils.setVibration();
        rootLayout.removeAllViews();    // 수식 뷰 안의 모든 뷰 삭제
        resultTextView.setText(zero);    // 결과 값을 0으로 초기화
        numeratorTextView.setText(zero);
        denominatorTextView.setText("1");
        wholeTextView.setText("");
        arrayList.clear();  // 리스트의 모든 내용 삭제
        resultInit(); // 결과값 초기화
    }

    // "=" 버튼 클릭
    public void clickBtnResult(View view) {
        preferenceUtils.setVibration();

        // 배열 값이 비어있지 않아야 한다.
        if(arrayList.isEmpty())
            return;

        String lastValue = arrayList.get(arrayList.size()-1);    // 마지막 수식
        String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
        if(lastValue.length() != 0)
            lastStrOfLastVal = lastValue.substring(lastValue.length()-1);

        // 마지막 수식은 숫자여야 하고, 0으로 나눌 수 없다.
        if(!calcUtils.isSymbol(lastValue) && !lastStrOfLastVal.equals(point)
                && !lastStrOfLastVal.equals(fraction)  && !lastStrOfLastVal.equals(plusMinus)
                && !(arrayList.size() >= 2 && arrayList.get(arrayList.size()-2).equals(divide) && lastValue.equals(zero))
                && !(arrayList.size() >= 2 && arrayList.get(arrayList.size()-2).equals(divide) && lastValue.equals(plusMinus+zero))){

            // 초기화 ("="을 입력한 후 "C" 버튼을 누르지 않고 계속 수식을 입력할 경우를 위해)
            resultInit();

            // 사칙연산
            try {
                result = calcUtils.calculate(arrayList);
            }
            catch (Exception e){
                result = zero;
                Log.e("asd", "calcUtils.calculate(): " + e);
            }
            resultTextView.setText(result);

            // 분수 사칙연산
            try {
                fractionResult = calcFractionUtils.fractionCalculate(arrayList);
            }
            catch (Exception e){
                fractionResult = new String[]{zero, "1", zero};
                Log.e("asd", "calcFractionUtils.fractionCalculate(): " + e);
            }
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

            setHistoryData(arrayList, result, fractionResult);
        }
    }

    public void setHistoryData(ArrayList<String> calc, String result, String[] fractionResult) {
        HistoryObject tempData;
        ArrayList<String> historyCalcList = new ArrayList<>();
        historyCalcList.addAll(calc);
        tempData = new HistoryObject(historyCalcList, result, fractionResult);
        HistoryStorageUtil.historyRes.add(0, tempData);
    }

    // 롱클릭 시 수식에 대분수 추가
    @Override
    public boolean onLongClick(View v) {
        if(v.getId() == R.id.btn1 || v.getId() == R.id.btn2  || v.getId() == R.id.btn3
                || v.getId() == R.id.btn4 || v.getId() == R.id.btn5 || v.getId() == R.id.btn6
                || v.getId() == R.id.btn7 || v.getId() == R.id.btn8 || v.getId() == R.id.btn9
                || v.getId() == R.id.btn0 || v.getId() == R.id.btnPlusMinus){

            if(arrayList.isEmpty())
                return false;

            String lastValue = arrayList.get(arrayList.size() - 1);  // 마지막 수식
            String lastStrOfLastVal = "";    // 마지막 값의 마지막 글자
            if (lastValue.length() != 0)
                lastStrOfLastVal = lastValue.substring(lastValue.length() - 1);
            String plusMinusSymbol = lastValue.charAt(0) + "";

            // 분자 및 분모에 자연수값이 있을 때, 대분수 수식 가능
            if(lastValue.contains(fraction) && !lastStrOfLastVal.equals(fraction) && !lastValue.contains(point)){
                // "±" 기호는 대분수에만 존재해야 한다.
                if(lastValue.contains(fractionWhole) && lastValue.contains(plusMinus) && !plusMinusSymbol.equals(plusMinus))
                    return false;

                // "±" 롱클릭시
                if(v.getTag().equals(plusMinus)){
                    if(lastValue.contains(plusMinus)){  // "±" 존재할 때
                        String splitStr[] = lastValue.split(fractionWhole);
                        if(splitStr[0].length() == 1){
                            arrayList.set(arrayList.size()-1, splitStr[1]);
                            viewUtils.changeTextView(arrayList, "calcFractionWholeTextView", "");
                        } else {
                            String positive = splitStr[0].substring(1);
                            arrayList.set(arrayList.size()-1, positive + fractionWhole + splitStr[1]);
                            viewUtils.changeTextView(arrayList, "calcFractionWholeTextView", positive);
                        }
                    }
                    else {   // "±" 없을 때
                        if(lastValue.contains(fractionWhole)){
                            String splitStr[] = lastValue.split(fractionWhole);
                            arrayList.set(arrayList.size()-1, plusMinus + lastValue);
                            viewUtils.changeTextView(arrayList, "calcFractionWholeTextView", minus + splitStr[0]);
                        }
                        else {
                            arrayList.set(arrayList.size()-1, plusMinus + fractionWhole + lastValue);
                            viewUtils.changeTextView(arrayList, "calcFractionWholeTextView", minus);
                        }
                    }
                    return true;
                }

                if(lastValue.contains(fractionWhole)){  // 대분수에 값이 있을 경우
                    String splitWhole[] = lastValue.split(fractionWhole);   // 0: 대분수 1: 분자,분모

                    // 0을 지우고 텍스트를 추가한다.
                    if(splitWhole[0].equals(zero)){
                        arrayList.set(arrayList.size()-1, v.getTag().toString() + fractionWhole + splitWhole[1]);
                        viewUtils.changeTextView(arrayList, "calcFractionWholeTextView", v.getTag().toString());
                    }
                    else if(splitWhole[0].equals(plusMinus+zero)){  //  "-0"을 지우고 텍스트를 추가한다.
                        arrayList.set(arrayList.size()-1, plusMinus + v.getTag().toString() + fractionWhole + splitWhole[1]);
                        viewUtils.changeTextView(arrayList, "calcFractionWholeTextView", minus + v.getTag().toString());
                    }
                    else {
                        arrayList.set(arrayList.size()-1, splitWhole[0] + v.getTag().toString() + fractionWhole + splitWhole[1]);
                        viewUtils.appendTextView(arrayList, "calcFractionWholeTextView", v.getTag().toString());
                    }
                }
                else {  // 대분수에 값이 없을 경우
                    arrayList.set(arrayList.size()-1, v.getTag().toString() + fractionWhole + lastValue);
                    viewUtils.changeTextView(arrayList, "calcFractionWholeTextView", v.getTag().toString());
                }
            }
            return true;
        }
        else
            return false;
    }

    protected void onDestroy() {
        super.onDestroy();
        //리스너 해지
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(preferenceUtils);
    }
}
