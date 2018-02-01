package com.tistory.qlyh8.calculator.utils;

/*
 * Created by YUNHEE on 2018-01-26.
 */

import android.content.Context;
import android.util.Log;

import com.tistory.qlyh8.calculator.R;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

// 분수 계산 유틸
public class CalcFractionUtils {

    private String add, subtract, multiply, divide, fraction, point, plusMinus;

    public CalcFractionUtils(Context context){
        add = context.getResources().getString(R.string.add);
        subtract = context.getResources().getString(R.string.subtract);
        multiply = context.getResources().getString(R.string.multiply);
        divide = context.getResources().getString(R.string.divide);
        point = context.getResources().getString(R.string.point);
        plusMinus = context.getResources().getString(R.string.plusMinus);
        fraction = "@";
    }

    // "±"를 "－"으로 변환
    private String convertToMinus(String number){
        if(number.contains(plusMinus))
            return number.replaceAll(plusMinus, "-");
        else
            return number;
    }

    // 소수를 분수로 변환
    private BigInteger[] convertToFraction(String value) throws Exception {
        BigInteger[] result = new BigInteger[2];

        String[] parts = value.split("\\.");
        BigDecimal denominator = BigDecimal.TEN.pow(parts[1].length()); // 분모
        BigDecimal numerator = (new BigDecimal(parts[0]).multiply(denominator)).add(new BigDecimal(parts[1])); // 분자

        // 최대공약수
        BigInteger gcd = (new BigInteger(""+numerator)).gcd((new BigInteger(""+denominator)));
        result[0] = (new BigInteger(""+numerator)).divide(gcd);   // 분자
        result[1] = (new BigInteger(""+denominator)).divide(gcd); // 분모

        return result;
    }

    // 분자 분모 가져오기
    private BigInteger[] getFraction(String str) throws Exception {
        BigInteger[] resultFraction = new BigInteger[]{BigInteger.valueOf(0), BigInteger.valueOf(1)};   // 분자,분모

        try {
            // 분수일 경우
            if(str.contains(fraction)){
                BigInteger[] num1 = new BigInteger[]{BigInteger.valueOf(0), BigInteger.valueOf(1)};    // str 의 분자
                BigInteger[] num2 = new BigInteger[]{BigInteger.valueOf(0), BigInteger.valueOf(1)};    // str 의 분모

                String[] fractionArr = str.split(fraction);
                if(str.contains(point)){   // 분자나 분모에 소수가 들어갈 경우, 다시 분수로 각각 변환
                    // 분자
                    if(fractionArr[1].contains(point))
                        num1 = convertToFraction(String.valueOf(fractionArr[1]));
                    else
                        num1[0] = new BigInteger(fractionArr[1]);

                    // 분모
                    if(fractionArr[0].contains(point))
                        num2 = convertToFraction(String.valueOf(fractionArr[0]));
                    else
                        num2[0] = new BigInteger(fractionArr[0]);
                }
                else {  // 분수이면서 소수가 아닌 경우
                    num1[0] = new BigInteger(fractionArr[1]);  // 분자
                    num2[0] = new BigInteger(fractionArr[0]);  // 분모
                }

                resultFraction[0] = num1[0].multiply(num2[1]); // 분자 계산
                resultFraction[1] = num1[1].multiply(num2[0]); // 분모 계산

                // 최대공약수
                BigInteger gcd = resultFraction[0].gcd(resultFraction[1]);
                resultFraction[0] = resultFraction[0].divide(gcd);    // 분자
                resultFraction[1] = resultFraction[1].divide(gcd);    // 분모
            }
            else {
                if(str.contains(point))
                    resultFraction = convertToFraction(str);    // 분수가 아니면서 소수일 경우
                else
                    resultFraction[0] = new BigInteger(str);  //    분수가 아니면서 소수도 아닐 경우
            }
        } catch (Exception e) {
            Log.e("asd", "getFraction(): " + e);
        }

        return resultFraction;
    }

    // 분수 간 사칙연산
    private String getFractionResult(String str1, String str2, String strCalc) throws Exception {
        BigInteger[] num1 = new BigInteger[2]; // str1의 분자(0), 분모(1)
        BigInteger[] num2 = new BigInteger[2]; // str2의 분자(0), 분모(1)
        BigInteger denominator, numerator;

        try {
            num1 = getFraction(str1);
            num2 = getFraction(str2);
        } catch (Exception e) {
            Log.e("asd", "getFractionResult(): " + e);
        }

        switch (strCalc){
            case "addition":
                numerator = (num1[0].multiply(num2[1])).add(num2[0].multiply(num1[1])); // 분자 계산
                denominator = num1[1].multiply(num2[1]); // 분모 계산
                break;
            case "multiplication":
                numerator = num1[0].multiply(num2[0]); // 분자 계산
                denominator = num1[1].multiply(num2[1]); // 분모 계산
                break;
            case "division":
                numerator = num1[0].multiply(num2[1]); // 분자 계산
                denominator = num1[1].multiply(num2[0]); // 분모 계산
                break;
            default:
                numerator = new BigInteger("0");
                denominator = new BigInteger("0");
                break;
        }

        // 최대공약수
        BigInteger gcd = numerator.gcd(denominator);
        numerator = numerator.divide(gcd); // 분자
        denominator = denominator.divide(gcd);   // 분모

        return String.valueOf(denominator) + fraction + String.valueOf(numerator);
    }

    // 대분수 만들기
    private String[] setWhole(String[] result){
        BigInteger absNumerator = new BigInteger(result[0]).abs();  // 분자
        BigInteger absDenominator = new BigInteger(result[1]).abs();    // 분모
        BigInteger whole;   // 대분수

        // 분자가 분모보다 크고, 분자가 분모로 나누어 떨어지지 않을 때, 대분수 처리
        if(((absNumerator).compareTo(absDenominator) == 1)
                && ((absNumerator).mod(absDenominator)).compareTo(new BigInteger("0")) != 0){
            whole = (absNumerator).divide(absDenominator); // 대분수

            if(result[0].contains("-"))
                whole = whole.negate();
            if(result[1].contains("-"))
                whole = whole.negate();

            result[0] = (absNumerator).mod(absDenominator).toString(); // 분자
            result[1] = absDenominator.toString(); // 분모
            result[2] = whole.toString();
        }
        else {
            int count = 0;
            if(result[0].contains("-")){
                result[0] = new BigInteger(result[0]).negate().toString();
                count++;
            }
            if(result[1].contains("-")){
                result[1] = new BigInteger(result[1]).negate().toString();
                count++;
            }
            if(count == 1)
                result[2] = subtract;
        }

        return result;
    }

    // 분수 수식연산
    public String[] fractionCalculate(ArrayList<String> arrayList) throws Exception{

        String[] result = new String[]{"0", "0", "0"};

        // arrayList 를 String 으로 변환
        String strList = "";
        for(int i = 0; i < arrayList.size() ; i++)
            strList += arrayList.get(i);

        try {
            StringTokenizer tokenNumber = new StringTokenizer(strList, add+subtract+multiply+divide); // 숫자
            StringTokenizer tokenOperator = new StringTokenizer(strList, "1234567890"+point+fraction+plusMinus);   // 연산자

            Stack<String> stack = new Stack<>();    // 숫자를 담을 스택

            stack.push(convertToMinus(tokenNumber.nextToken()));    // 첫 번째 숫자를 스택에 넣는다.
            while (tokenNumber.hasMoreTokens()){
                String number = convertToMinus(tokenNumber.nextToken());    // 피연산자
                String operator = tokenOperator.nextToken();    // 연산자
                String value;   // 스택에 마지막으로 들어간 숫자

                switch (operator){
                    case "×":
                        value = convertToMinus(stack.pop());
                        stack.push(getFractionResult(value, number, "multiplication"));
                        break;
                    case "÷":
                        value = convertToMinus(stack.pop());
                        stack.push(getFractionResult(value, number, "division"));
                        break;
                    case "＋":
                        stack.push(number);
                        break;
                    case "－":
                        stack.push(getFractionResult("1"+fraction+"-1", number, "multiplication"));
                        break;
                    default:
                        break;
                }
            }

            // 뺄셈, 곱셈, 나눗셈을 수행한 값들을 모두 더한다.
            String strResult = "1"+fraction+"0";
            while(!stack.isEmpty()){
                strResult = getFractionResult(strResult, stack.pop(), "addition");
            }

            String[] strFraction = strResult.split(fraction);
            result[0] = strFraction[1];   // 분자
            result[1] = strFraction[0];   // 분모

            result = setWhole(result);  // 대분수 만들기
        } catch (Exception e) {
            Log.e("asd", "fractionCalculate(): " + e);
        }

        return result;
    }
}
