package com.tistory.qlyh8.calculator.model;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by cmtyx on 2018-01-28.
 */

public class HistoryObject {
    private ArrayList<String> calc;
    private BigDecimal result;
    private long[] fractionResult;

    public HistoryObject(ArrayList<String> calc, BigDecimal result, long[] fractionResult) {
        this.calc = calc;
        this.result = result;
        this.fractionResult = fractionResult;
    }

    public ArrayList<String> getCalc() {
        return calc;
    }

    public void setCalc(ArrayList<String> calc) {
        this.calc = calc;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public long[] getFractionResult() {
        return fractionResult;
    }

    public void setFractionResult(long[] fractionResult) {
        this.fractionResult = fractionResult;
    }
}
