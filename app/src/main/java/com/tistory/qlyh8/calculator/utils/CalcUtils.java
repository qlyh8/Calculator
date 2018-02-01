package com.tistory.qlyh8.calculator.utils;

import android.content.Context;
import android.util.Log;

import com.tistory.qlyh8.calculator.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

/*
 * Created by YUNHEE on 2018-01-24.
 */

// 계산 유틸
public class CalcUtils {

    private String add, subtract, multiply, divide, fraction, plusMinus;

    public CalcUtils(Context context){
        add = context.getResources().getString(R.string.add);
        subtract = context.getResources().getString(R.string.subtract);
        multiply = context.getResources().getString(R.string.multiply);
        divide = context.getResources().getString(R.string.divide);
        plusMinus = context.getResources().getString(R.string.plusMinus);
        fraction = "@";
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

    // "@"를 "÷"으로 변환
    public String convertToDivide(ArrayList<String> oldList){
        // 복제
        ArrayList<String> tempList = new ArrayList<>();
        tempList.addAll(oldList);

        // 변환
        for(int i = 0; i < tempList.size() ; i++){
            if(tempList.get(i).contains(fraction)){
                String splitStr[] = tempList.get(i).split(fraction);
                tempList.set(i, splitStr[1] + divide + splitStr[0]);
            }
        }

        // tempList 를 String 으로 변환
        String strList = "";
        for(int i = 0; i < tempList.size() ; i++)
            strList += tempList.get(i);

        return strList;
    }

    // "±"를 "－"으로 변환
    private String convertToMinus(String number){
        if(number.contains(plusMinus))
            return number.replace(plusMinus, "-");
        else
            return number;
    }

    // 사칙연산
    public String calculate(String strList) throws Exception {

        String newResult = "0";

        // 숫자만 골라낸다.
        StringTokenizer tokenNumber = new StringTokenizer(strList, add + subtract + multiply + divide);
        // 연산자만 골라낸다.
        StringTokenizer tokenOperator = new StringTokenizer(strList, "1234567890." + plusMinus);

        // 숫자를 담을 스택
        Stack<String> stack = new Stack<>();
        // 첫 번째 숫자를 스택에 넣는다.
        stack.push(convertToMinus(tokenNumber.nextToken()));
        // 곱셈과 나눗셈이 있으면 스택의 마지막 숫자를 꺼내 계산한 후 계산한 값으로 다시 스택에 넣는다.
        // 뺄셈은 해당 숫자를 -1과 곱셈하여 다시 스택에 넣는다.
        // 최종 스택에는 곱셈과 나눗셈, 뺄셈을 처리한 값만 존재한다.
        while (tokenNumber.hasMoreTokens()){
            String number = convertToMinus(tokenNumber.nextToken());    // 피연산자
            String operator = tokenOperator.nextToken();    // 연산자
            BigDecimal value;   // 스택에 마지막으로 들어간 숫자

            switch (operator){
                case "×":
                    value = new BigDecimal(convertToMinus(stack.pop()));
                    stack.push(value.multiply(new BigDecimal(number)).stripTrailingZeros().toPlainString());
                    break;
                case "÷":
                    value = new BigDecimal(convertToMinus(stack.pop()));
                    // 소수 17자리에서 반올림
                    stack.push(value.divide(new BigDecimal(number), 16,
                            RoundingMode.HALF_EVEN).stripTrailingZeros().toPlainString());
                    break;
                case "＋":
                    stack.push(number);
                    break;
                case "－":
                    stack.push(new BigDecimal("-1").multiply(new BigDecimal(number)).stripTrailingZeros().toPlainString());
                    break;
                default:
                    break;
            }
        }

        // 뺄셈, 곱셈, 나눗셈을 수행한 값들을 모두 더한다.
        while(!stack.isEmpty()){
            newResult = new BigDecimal(newResult).add(new BigDecimal(stack.pop())).stripTrailingZeros().toPlainString();
        }

        return newResult;
    }
}
