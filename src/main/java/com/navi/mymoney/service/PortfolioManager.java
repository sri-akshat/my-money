package com.navi.mymoney.service;

import com.navi.mymoney.model.Fund;

import java.time.Month;
import java.util.Map;

public interface PortfolioManager {
    void allocate(Map<Fund, Integer> fundAmountMap);
    void change(Map<Fund, Double> marketChangeMap, Month month);
    void addSIP(Fund fund, Integer amount);
    Map<Fund, Integer> getBalance(Month month);
    Map<Fund, Integer> reBalance();
}
