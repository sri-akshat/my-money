package com.navi.mymoney.service;

import com.navi.mymoney.model.Fund;
import com.navi.mymoney.model.Portfolio;

import java.time.Month;
import java.util.Map;

public class PortfolioManagerImpl implements PortfolioManager{

    private final Portfolio portfolio;

    public PortfolioManagerImpl(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public void allocate(Map<Fund, Integer> fundAmountMap) {
        portfolio.allocate(fundAmountMap);
    }

    public void change(Map<Fund, Double> marketChangeMap, Month month) {
        portfolio.applyMarketChange(marketChangeMap, month);
    }

    public void addSIP(Fund fund, Integer amount) {
        portfolio.addSIP(fund, amount);
    }

    public Map<Fund, Integer> getBalance(Month month) {
        return portfolio.getBalance(month);
    }

    public Map<Fund, Integer> reBalance() {
        return portfolio.reBalance();
    }
}
