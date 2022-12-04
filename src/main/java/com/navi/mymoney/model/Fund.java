package com.navi.mymoney.model;

import lombok.Getter;

import java.time.Month;
import java.util.Map;
import java.util.TreeMap;


public class Fund {
    @Getter
    private final FundType type;

    private Fund(FundType type) {
        this.type = type;
    }

    public enum FundType {
        EQUITY, DEBT, GOLD
    }

    public static Fund gold = new Fund(FundType.GOLD);
    public static Fund equity = new Fund(FundType.EQUITY);
    public static Fund debt = new Fund(FundType.DEBT);

    Map<Month, Double> marketChangeMap = new TreeMap<>();

    public Map<Month, Double> getMarketChange() {
        return marketChangeMap;
    }
}
