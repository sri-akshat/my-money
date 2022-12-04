package com.navi.mymoney.model;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class PortfolioTest {

    @Before
    public void cleanUp() {
        Fund.equity.marketChangeMap.clear();
        Fund.gold.marketChangeMap.clear();
        Fund.debt.marketChangeMap.clear();
    }

    @Test
    public void testAllocate() {
        Map<Fund, Integer> fundAmtMap = Map.of(
                Fund.equity, 6000,
                Fund.debt, 3000,
                Fund.gold, 1000);
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(fundAmtMap);
        Map<Fund, Integer> balance = portfolio.getBalance(Month.JANUARY);
        Assert.assertEquals(3, balance.size());
        Assert.assertEquals(6000, balance.getOrDefault(Fund.equity, 0).intValue());
        Assert.assertEquals(3000, balance.getOrDefault(Fund.debt, 0).intValue());
        Assert.assertEquals(1000, balance.getOrDefault(Fund.gold, 0).intValue());
    }

    @Test
    public void testBalancePostAllocate() {
        Map<Fund, Integer> fundAmtMap = Map.of(
                Fund.equity, 6000,
                Fund.debt, 3000,
                Fund.gold, 1000);
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(fundAmtMap);

        Map<Fund, Integer> balance = portfolio.getBalance(Month.FEBRUARY);
        Assert.assertEquals(3, balance.size());
        Assert.assertEquals(6000, balance.getOrDefault(Fund.equity, 0).intValue());
        Assert.assertEquals(3000, balance.getOrDefault(Fund.debt, 0).intValue());
        Assert.assertEquals(1000, balance.getOrDefault(Fund.gold, 0).intValue());
    }

    @Test
    public void testChangePostAllocate() {
        Map<Fund, Integer> fundAmtMap = Map.of(
                Fund.equity, 6000,
                Fund.debt, 3000,
                Fund.gold, 1000);
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(fundAmtMap);

        Map<Fund, Double> marketChangeMap = getMarketChangeMap(4.0, 10.0, 2.0);

        portfolio.applyMarketChange(marketChangeMap, Month.JANUARY);
        Map<Fund, Integer> balance = portfolio.getBalance(Month.JANUARY);
        Assert.assertEquals(3, balance.size());
        Assert.assertEquals(6240, balance.getOrDefault(Fund.equity, 0).intValue());
        Assert.assertEquals(3300, balance.getOrDefault(Fund.debt, 0).intValue());
        Assert.assertEquals(1020, balance.getOrDefault(Fund.gold, 0).intValue());
    }

    @Test
    public void testAddSIP() {
        Map<Fund, Integer> fundAmtMap = Map.of(
                Fund.equity, 6000,
                Fund.debt, 3000,
                Fund.gold, 1000);
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(fundAmtMap);

        Map<Fund, Double> marketChangeMap = getMarketChangeMap(4.0, 10.0, 2.0);

        portfolio.applyMarketChange(marketChangeMap, Month.JANUARY);
        Map<Fund, Integer> balance = portfolio.getBalance(Month.JANUARY);
        Assert.assertEquals(3, balance.size());
        Assert.assertEquals(6240, balance.getOrDefault(Fund.equity, 0).intValue());
        Assert.assertEquals(3300, balance.getOrDefault(Fund.debt, 0).intValue());
        Assert.assertEquals(1020, balance.getOrDefault(Fund.gold, 0).intValue());

        portfolio.addSIP(Fund.equity, 2000);
        portfolio.addSIP(Fund.debt, 1000);
        portfolio.addSIP(Fund.gold, 500);

        Assert.assertEquals(3, balance.size());
        Assert.assertEquals(6240, balance.getOrDefault(Fund.equity, 0).intValue());
        Assert.assertEquals(3300, balance.getOrDefault(Fund.debt, 0).intValue());
        Assert.assertEquals(1020, balance.getOrDefault(Fund.gold, 0).intValue());

        Map<Fund, Integer> febBalance = portfolio.getBalance(Month.FEBRUARY);
        Assert.assertEquals(3, febBalance.size());
        Assert.assertEquals(8240, febBalance.getOrDefault(Fund.equity, 0).intValue());
        Assert.assertEquals(4300, febBalance.getOrDefault(Fund.debt, 0).intValue());
        Assert.assertEquals(1520, febBalance.getOrDefault(Fund.gold, 0).intValue());
    }

    @Test
    public void testCombinationOfAddSIPAndChange() {
        Map<Fund, Integer> fundAmtMap = Map.of(
                Fund.equity, 6000,
                Fund.debt, 3000,
                Fund.gold, 1000);
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(fundAmtMap);

        portfolio.addSIP(Fund.equity, 2000);
        portfolio.addSIP(Fund.debt, 1000);
        portfolio.addSIP(Fund.gold, 500);

        portfolio.applyMarketChange(getMarketChangeMap(4.0, 10.0, 2.0), Month.JANUARY);
        portfolio.applyMarketChange(getMarketChangeMap(-10.0, 40.0, 0.0), Month.FEBRUARY);
        portfolio.applyMarketChange(getMarketChangeMap(12.5, 12.5, 12.5), Month.MARCH);
        portfolio.applyMarketChange(getMarketChangeMap(8.0, -3.0, 7.0), Month.APRIL);
        portfolio.applyMarketChange(getMarketChangeMap(13.0, 21.0, 10.5), Month.MAY);
        portfolio.applyMarketChange(getMarketChangeMap(10.0, 8.0, -5.0), Month.JUNE);

        Map<Fund, Integer> marBalance = portfolio.getBalance(Month.MARCH);
        Assert.assertEquals(3, marBalance.size());
        Assert.assertEquals(10593, marBalance.getOrDefault(Fund.equity, 0).intValue());
        Assert.assertEquals(7897, marBalance.getOrDefault(Fund.debt, 0).intValue());
        Assert.assertEquals(2272, marBalance.getOrDefault(Fund.gold, 0).intValue());

        Map<Fund, Integer> junBalance = portfolio.getBalance(Month.JUNE);
        Assert.assertEquals(3, junBalance.size());
        Assert.assertEquals(21590, junBalance.getOrDefault(Fund.equity, 0).intValue());
        Assert.assertEquals(13664, junBalance.getOrDefault(Fund.debt, 0).intValue());
        Assert.assertEquals(4112, junBalance.getOrDefault(Fund.gold, 0).intValue());
    }

    @Test
    public void testReBalance() {
        Map<Fund, Integer> fundAmtMap = Map.of(
                Fund.equity, 6000,
                Fund.debt, 3000,
                Fund.gold, 1000);
        Portfolio portfolio = new Portfolio();
        portfolio.allocate(fundAmtMap);

        portfolio.addSIP(Fund.equity, 2000);
        portfolio.addSIP(Fund.debt, 1000);
        portfolio.addSIP(Fund.gold, 500);

        portfolio.applyMarketChange(getMarketChangeMap(4.0, 10.0, 2.0), Month.JANUARY);
        portfolio.applyMarketChange(getMarketChangeMap(-10.0, 40.0, 0.0), Month.FEBRUARY);
        portfolio.applyMarketChange(getMarketChangeMap(12.5, 12.5, 12.5), Month.MARCH);
        portfolio.applyMarketChange(getMarketChangeMap(8.0, -3.0, 7.0), Month.APRIL);
        portfolio.applyMarketChange(getMarketChangeMap(13.0, 21.0, 10.5), Month.MAY);
        portfolio.applyMarketChange(getMarketChangeMap(10.0, 8.0, -5.0), Month.JUNE);

        portfolio.reBalance();

        Map<Fund, Integer> balance = portfolio.getBalance(Month.JUNE);
        Assert.assertEquals(3, balance.size());
        Assert.assertEquals(23619, balance.getOrDefault(Fund.equity, 0).intValue());
        Assert.assertEquals(11809, balance.getOrDefault(Fund.debt, 0).intValue());
        Assert.assertEquals(3936, balance.getOrDefault(Fund.gold, 0).intValue());

    }


    private Map<Fund, Double> getMarketChangeMap(Double equityChange, Double debtChange, Double goldChange) {
        Map<Fund, Double> marketChangeMap = new HashMap<>();
        marketChangeMap.put(Fund.equity, equityChange);
        marketChangeMap.put(Fund.debt, debtChange);
        marketChangeMap.put(Fund.gold, goldChange);
        return marketChangeMap;
    }


}