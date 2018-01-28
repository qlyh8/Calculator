package com.tistory.qlyh8.calculator.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

/*
 * Created by YUNHEE on 2018-01-24.
 */

// 계산 유틸
public class CalcUtils {

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
            if(tempList.get(i).contains("@")){
                String splitStr[] = tempList.get(i).split("@");
                tempList.set(i, splitStr[1] + "÷" + splitStr[0]);
            }
        }

        // tempList 를 String 으로 변환
        String strList = "";
        for(int i = 0; i < tempList.size() ; i++)
            strList += tempList.get(i);

        return strList;
    }

    // double 형 예외처리
    private double doubleValue(double value){
        if (Double.isInfinite(value) || Double.isNaN(value))
            throw new ArithmeticException("double type overflow");
        else
            return value;
    }

    // 사칙연산
    public double calculate(String strList){

        double newResult = 0d;

        // 숫자만 골라낸다.
        StringTokenizer tokenNumber = new StringTokenizer(strList, "＋－×÷");
        // 연산자만 골라낸다.
        StringTokenizer tokenOperator = new StringTokenizer(strList, "1234567890.");

        // 숫자를 담을 스택
        Stack<Double> stack = new Stack<>();
        // 첫 번째 숫자를 스택에 넣는다.
        stack.push(doubleValue(Double.parseDouble(tokenNumber.nextToken())));
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
                    value *= doubleValue(Double.parseDouble(number));
                    stack.push(doubleValue(value));
                    break;
                case "÷":
                    value = stack.pop();
                    value /= doubleValue(Double.parseDouble(number));
                    stack.push(doubleValue(value));
                    break;
                case "＋":
                    stack.push(doubleValue(Double.parseDouble(number)));
                    break;
                case "－":
                    stack.push(doubleValue(-1 * (Double.parseDouble(number))));
                    break;
                default:
                    break;
            }
        }

        // 뺄셈, 곱셈, 나눗셈을 수행한 값들을 모두 더한다.
        while(!stack.isEmpty()){
            newResult += stack.pop();

            if (Double.isInfinite(newResult) || Double.isNaN(newResult))
                throw new ArithmeticException("double type overflow");
        }

        // 소수점 이하 숫지를 0을 채우지않으며, 14자리까지만 나오게 한다.
        DecimalFormat newFormat = new DecimalFormat("#.##############");
        newResult =  Double.valueOf(newFormat.format(newResult));

        return newResult;
    }
}
