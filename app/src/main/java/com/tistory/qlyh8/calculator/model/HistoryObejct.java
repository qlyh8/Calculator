package com.tistory.qlyh8.calculator.model;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class HistoryObejct {
    private String calc;
    private double result;
    private long[] fractionResult;

    public HistoryObejct(String calc, double result, long[] fractionResult) {
        this.calc = calc;
        this.result = result;
        this.fractionResult = fractionResult;
    }

    public String getCalc() {
        return calc;
    }

    public void setCalc(String calc) {
        this.calc = calc;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public long[] getFractionResult() {
        return fractionResult;
    }

    public void setFractionResult(long[] fractionResult) {
        this.fractionResult = fractionResult;
    }
}
