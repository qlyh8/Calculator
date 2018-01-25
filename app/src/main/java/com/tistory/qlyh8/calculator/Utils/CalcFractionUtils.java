package com.tistory.qlyh8.calculator.Utils;

/*
 * Created by YUNHEE on 2018-01-26.
 */

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

// 분수 계산 유틸
public class CalcFractionUtils {

    // long 형 덧셈 예외 처리
    private long addition(long num1, long num2) {
        double doubleValue = (double) num1 + num2;
        long longValue = num1 + num2;

        if ((long) doubleValue != longValue) {
            throw new ArithmeticException("long type addition overflow");
        } else {
            return longValue;
        }
    }

    // long 형 곱셈 예외 처리
    private long multiplication(long num1, long num2) {
        double doubleValue = (double) num1 * num2;
        long longValue = num1 * num2;

        if ((long) doubleValue != longValue) {
            throw new ArithmeticException("long type multiplication overflow");
        } else {
            return longValue;
        }
    }

    // long 형 나눗셈 예외 처리
    private long division(long num1, long num2) {
        double doubleValue = (double) num1 / num2;
        long longValue = num1 / num2;

        if ((long) doubleValue != longValue) {
            throw new ArithmeticException("long type division overflow");
        } else {
            return longValue;
        }
    }

    // 소수를 분수로 변환
    private long[] convertToFraction(BigDecimal value){
        long[] result = new long[2];

        String[] parts = value.toString().split("\\.");
        BigDecimal denominator = BigDecimal.TEN.pow(parts[1].length()); // 분모
        BigDecimal numerator = (new BigDecimal(parts[0]).multiply(denominator)).add(new BigDecimal(parts[1])); // 분자

        // 최대공약수
        long gcd = BigInteger.valueOf(numerator.longValue()).gcd(BigInteger.valueOf(denominator.longValue())).longValue();
        result[0] = division(numerator.longValue(), gcd); // 분자
        result[1] = division(denominator.longValue(), gcd);   // 분모

        return result;
    }

    // 분자 분모 가져오기
    private long[] getFraction(String str){
        long[] resultFraction = new long[2];

        // 분수일 경우
        if(str.contains("@")){
            String[] fraction = str.split("@");
            long[] num1 = new long[2];    // 분자
            long[] num2 = new long[2];    // 분모

            if(str.contains(".")){   // 분자나 분모에 소수가 들어갈 경우, 다시 분수로 각각 변환
                // 분자
                if(fraction[1].contains(".")) {
                    num1 = convertToFraction(new BigDecimal(String.valueOf(fraction[1])));
                } else {
                    num1[0] = Long.parseLong(fraction[1]);
                    num1[1] = 1;
                }
                // 분모
                if(fraction[0].contains(".")) {
                    num2 = convertToFraction(new BigDecimal(String.valueOf(fraction[0])));
                } else {
                    num2[0] = Long.parseLong(fraction[0]);
                    num2[1] = 1;
                }
            }
            else {  // 분수이면서 소수가 아닌 경우
                num1[0] = Long.parseLong(fraction[1]);
                num1[1] = 1;
                num2[0] = Long.parseLong(fraction[0]);
                num2[1] = 1;
            }

            resultFraction[0] = multiplication(num1[0], num2[1]); // 분자 계산
            resultFraction[1] = multiplication(num1[1], num2[0]); // 분모 계산

            // 최대공약수
            long gcd = BigInteger.valueOf(resultFraction[0]).gcd(BigInteger.valueOf(resultFraction[1])).longValue();
            resultFraction[0] = division(resultFraction[0], gcd);    // 분자
            resultFraction[1] = division(resultFraction[1], gcd);    // 분모
        }
        else {
            if(str.contains(".")){  // 분수가 아니면서 소수일 경우
                resultFraction = convertToFraction(new BigDecimal(str));
            }
            else{   // 분수가 아니면서 소수도 아닐 경우
                resultFraction[0] = Long.parseLong(str);  // 분자
                resultFraction[1] = 1;  // 분모
            }
        }

        return resultFraction;
    }

    // 분수 덧셈
    private String fractionAddition(String str1, String str2){
        long[] num1 = getFraction(str1); // 0: 분자, 1: 분모
        long[] num2 = getFraction(str2); // 0: 분자, 1: 분모

        long denominator = multiplication(num1[1], num2[1]); // 분모 계산
        long numerator = addition(multiplication(num1[0], num2[1]), multiplication(num2[0],num1[1])); // 분자 계산

        // 최대공약수
        long gcd = BigInteger.valueOf(numerator).gcd(BigInteger.valueOf(denominator)).longValue();
        numerator = division(numerator, gcd); // 분자
        denominator = division(denominator, gcd);   // 분모

        return String.valueOf(denominator) + "@" + String.valueOf(numerator);
    }

    // 분수 곱셈
    private String fractionMultiplication(String str1, String str2){
        long[] num1 = getFraction(str1); // 0: 분자, 1: 분모
        long[] num2 = getFraction(str2); // 0: 분자, 1: 분모

        long denominator = multiplication(num1[1], num2[1]); // 분모 계산
        long numerator = multiplication(num1[0], num2[0]); // 분자 계산

        // 최대공약수
        long gcd = BigInteger.valueOf(numerator).gcd(BigInteger.valueOf(denominator)).longValue();
        numerator = division(numerator, gcd); // 분자
        denominator = division(denominator, gcd);   // 분모

        return String.valueOf(denominator) + "@" + String.valueOf(numerator);
    }

    // 분수 나눗셈
    private String fractionDivision(String str1, String str2){
        long[] num1 = getFraction(str1); // 0: 분자, 1: 분모
        long[] num2 = getFraction(str2); // 0: 분자, 1: 분모

        long denominator = multiplication(num1[1], num2[0]); // 분모 계산
        long numerator = multiplication(num1[0], num2[1]); // 분자 계산

        // 최대공약수
        long gcd = BigInteger.valueOf(numerator).gcd(BigInteger.valueOf(denominator)).longValue();
        numerator = division(numerator, gcd); // 분자
        denominator = division(denominator, gcd);   // 분모

        return String.valueOf(denominator) + "@" + String.valueOf(numerator);
    }

    // 분수 사칙연산
    public long[] fractionCalculate(ArrayList<String> arrayList){

        long[] result = new long[2];

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
        result[0] = Long.parseLong(strFraction[1]);   // 분자
        result[1] = Long.parseLong(strFraction[0]);   // 분모

        return result;
    }
}
