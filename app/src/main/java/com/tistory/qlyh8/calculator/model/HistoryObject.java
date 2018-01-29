package com.tistory.qlyh8.calculator.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class HistoryObject {
    private ArrayList<String> calc;
    private String result;
    private BigInteger[] fractionResult;

    public HistoryObject(ArrayList<String> calc, String result, BigInteger[] fractionResult) {

        this.calc = calc;
        this.result = result;
        this.fractionResult = fractionResult;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<String> getCalc() {
        return calc;
    }

    public void setCalc(ArrayList<String> calc) {
        this.calc = calc;
    }

    public BigInteger[] getFractionResult() {
        return fractionResult;
    }

    public void setFractionResult(BigInteger[] fractionResult) {
        this.fractionResult = fractionResult;
    }
}
