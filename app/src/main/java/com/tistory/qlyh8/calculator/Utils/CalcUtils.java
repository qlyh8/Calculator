package com.tistory.qlyh8.calculator.Utils;

import android.util.Log;

import java.math.BigDecimal;
import java.math.BigInteger;
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
            newResult += stack.pop();
        }

        // 소수점 이하 숫지를 0을 채우지않으며, 14자리까지만 나오게 한다.
        DecimalFormat newFormat = new DecimalFormat("#.##############");
        newResult =  Double.valueOf(newFormat.format(newResult));

        return newResult;
    }

    // 소수를 분수로 변환
    private int[] convertToFraction(BigDecimal value){
        int[] result = new int[2];

        String[] parts = value.toString().split("\\.");
        BigDecimal denominator = BigDecimal.TEN.pow(parts[1].length()); // 분모
        BigDecimal numerator = (new BigDecimal(parts[0]).multiply(denominator)).add(new BigDecimal(parts[1])); // 분자

        // 최대공약수
        int gcd = BigInteger.valueOf(numerator.intValue()).gcd(BigInteger.valueOf(denominator.intValue())).intValue();
        result[0] = numerator.intValue() / gcd; // 분자
        result[1] = denominator.intValue() / gcd;   // 분모

        return result;
    }

    // 분자 분모 가져오기
    private int[] getFraction(String str){
        int[] resultFraction = new int[2];

        // 분수일 경우
        if(str.contains("@")){
            String[] fraction = str.split("@");
            int[] num1 = new int[2];    // 분자
            int[] num2 = new int[2];    // 분모

            if(str.contains(".")){   // 분자나 분모에 소수가 들어갈 경우, 다시 분수로 각각 변환
                // 분자
                if(fraction[1].contains(".")) {
                    num1 = convertToFraction(new BigDecimal(String.valueOf(fraction[1])));
                } else {
                    num1[0] = Integer.parseInt(fraction[1]);
                    num1[1] = 1;
                }
                // 분모
                if(fraction[0].contains(".")) {
                    num2 = convertToFraction(new BigDecimal(String.valueOf(fraction[0])));
                } else {
                    num2[0] = Integer.parseInt(fraction[0]);
                    num2[1] = 1;
                }
            }
            else {  // 분수이면서 소수가 아닌 경우
                num1[0] = Integer.parseInt(fraction[1]);
                num1[1] = 1;
                num2[0] = Integer.parseInt(fraction[0]);
                num2[1] = 1;
            }

            resultFraction[0] = num1[0] * num2[1]; // 분자 계산
            resultFraction[1] = num1[1] * num2[0]; // 분모 계산

            // 최대공약수
            int gcd = BigInteger.valueOf(resultFraction[0]).gcd(BigInteger.valueOf(resultFraction[1])).intValue();
            resultFraction[0] = resultFraction[0] / gcd;    // 분자
            resultFraction[1] = resultFraction[1] / gcd;    // 분모
        }
        else {
            if(str.contains(".")){  // 분수가 아니면서 소수일 경우
                resultFraction = convertToFraction(new BigDecimal(str));
            }
            else{   // 분수가 아니면서 소수도 아닐 경우
                resultFraction[0] = Integer.parseInt(str);  // 분자
                resultFraction[1] = 1;  // 분모
            }
        }

        return resultFraction;
    }

    // 분수 덧셈
    private String fractionAddition(String str1, String str2){
        int[] num1 = getFraction(str1); // 0: 분자, 1: 분모
        int[] num2 = getFraction(str2); // 0: 분자, 1: 분모

        int denominator = num1[1] * num2[1]; // 분모 계산
        int numerator = (num1[0] * num2[1]) + (num2[0] * num1[1]); // 분자 계산

        // 최대공약수
        int gcd = BigInteger.valueOf(numerator).gcd(BigInteger.valueOf(denominator)).intValue();
        numerator = numerator / gcd; // 분자
        denominator = denominator / gcd;   // 분모

        return String.valueOf(denominator) + "@" + String.valueOf(numerator);
    }

    // 분수 곱셈
    private String fractionMultiplication(String str1, String str2){
        int[] num1 = getFraction(str1); // 0: 분자, 1: 분모
        int[] num2 = getFraction(str2); // 0: 분자, 1: 분모

        int denominator = num1[1] * num2[1]; // 분모 계산
        int numerator = num1[0] * num2[0]; // 분자 계산

        // 최대공약수
        int gcd = BigInteger.valueOf(numerator).gcd(BigInteger.valueOf(denominator)).intValue();
        numerator = numerator / gcd; // 분자
        denominator = denominator / gcd;   // 분모

        return String.valueOf(denominator) + "@" + String.valueOf(numerator);
    }

    // 분수 나눗셈
    private String fractionDivision(String str1, String str2){
        int[] num1 = getFraction(str1); // 0: 분자, 1: 분모
        int[] num2 = getFraction(str2); // 0: 분자, 1: 분모

        int denominator = num1[1] * num2[0]; // 분모 계산
        int numerator = num1[0] * num2[1]; // 분자 계산

        // 최대공약수
        int gcd = BigInteger.valueOf(numerator).gcd(BigInteger.valueOf(denominator)).intValue();
        numerator = numerator / gcd; // 분자
        denominator = denominator / gcd;   // 분모

        return String.valueOf(denominator) + "@" + String.valueOf(numerator);
    }

    // 분수 사칙연산
    public int[] fractionCalculate(ArrayList<String> arrayList){

        int[] result = new int[2];

        // arrayList 를 String 으로 변환
        String strList = "";
        for(int i = 0; i < arrayList.size() ; i++)
            strList += arrayList.get(i);

        // 숫자만 골라낸다.
        StringTokenizer tokenNumber = new StringTokenizer(strList, "＋－×÷");
        // 연산자만 골라낸다.
        StringTokenizer tokenOperator = new StringTokenizer(strList, "1234567890.@");

        // 숫자를 담을 스택
        Stack<String> stack = new Stack<>();
        // 첫 번째 숫자를 스택에 넣는다.
        stack.push(tokenNumber.nextToken());
        // 곱셈과 나눗셈이 있으면 스택의 마지막 숫자를 꺼내 계산한 후 계산한 값으로 다시 스택에 넣는다.
        // 뺄셈은 해당 숫자를 -1과 곱셈하여 다시 스택에 넣는다.
        // 최종 스택에는 곱셈과 나눗셈, 뺄셈을 처리한 값만 존재한다.
        while (tokenNumber.hasMoreTokens()){
            String number = tokenNumber.nextToken();    // 피연산자
            String operator = tokenOperator.nextToken();    // 연산자
            String value;   // 스택에 마지막으로 들어간 숫자

            switch (operator){
                case "×":
                    value = stack.pop();
                    value = fractionMultiplication(value, number);
                    stack.push(value);
                    break;
                case "÷":
                    value = stack.pop();
                    value = fractionDivision(value, number);
                    stack.push(value);
                    break;
                case "＋":
                    stack.push(number);
                    break;
                case "－":
                    stack.push(fractionMultiplication("1@-1", number));
                    break;
                default:
                    break;
            }
        }

        // 뺄셈, 곱셈, 나눗셈을 수행한 값들을 모두 더한다.
        String strResult = "1@0";
        while(!stack.isEmpty()){
            strResult = fractionAddition(strResult, stack.pop());
        }

        String[] strFraction = strResult.split("@");
        result[0] = Integer.parseInt(strFraction[1]);   // 분자
        result[1] = Integer.parseInt(strFraction[0]);   // 분모

        return result;
    }
}
