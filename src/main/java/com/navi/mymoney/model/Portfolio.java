package com.navi.mymoney.model;

import com.navi.mymoney.exception.CannotRebalanceException;
import lombok.Data;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class Portfolio {
    private List<FundAccount> fundAccountList;

    public void allocate(Map<Fund, Integer> fundAmountMap) {
        fundAccountList = fundAmountMap.entrySet().stream()
                .map(entry -> new FundAccount(entry.getKey(), new Transaction(TxnType.BUY, entry.getValue(), Month.JANUARY)))
                .collect(Collectors.toList());
    }

    public Map<Fund, Integer> getBalance(Month month) {
        Map<Fund, Integer> fundBalanceMap = new TreeMap<>(Comparator.comparing(Fund::getType));
        fundAccountList.stream().forEach((fundAccount) -> fundBalanceMap.put( fundAccount.getFund(), fundAccount.getBalance(month)));
        return fundBalanceMap;
    }

    public void applyMarketChange(Map<Fund, Double> marketChangeMap, Month month) {
        marketChangeMap.forEach((fund, marketChange) -> {
            fund.getMarketChange().put(month, marketChange);
        });
    }

    public void addSIP(Fund fund, Integer amount) {
        for (Month month : Month.values()) {
            if(month != Month.JANUARY) {
                fundAccountList.stream().filter(fundAccount -> fundAccount.getFund() == fund).findFirst()
                        .ifPresent(fundAccount -> fundAccount.getTransactions().add(new Transaction(TxnType.BUY, amount, month)));
            }
        }
    }

    public Map<Fund, Integer> reBalance() {
        Map<Fund, Integer> reBalancedMap = new HashMap<>();
        Integer initialBalance = getInitialInvestmentSplit().values().stream().collect(Collectors.summingInt(Integer::intValue));
        Map<Fund, Integer> latestBalance = getLatestBalance();
        double multiplier =  (double) (latestBalance.values().stream().collect(Collectors.summingInt(Integer::intValue))) / (double)initialBalance;
        getInitialInvestmentSplit().forEach((fund, amount) -> {
            reBalancedMap.put(fund, (int) (amount * multiplier));
        });

        fundAccountList.stream().forEach(fundAccount -> {
            fundAccount.adjustBalance(reBalancedMap.get(fundAccount.getFund()) - latestBalance.get(fundAccount.getFund()));
        });
        return reBalancedMap;
    }

    private Map<Fund, Integer> getLatestBalance() {
        Month lastMonth = (Month) ((TreeMap) Fund.equity.marketChangeMap).lastKey();
        if(lastMonth.getValue() < Month.JUNE.getValue()) {
            throw new CannotRebalanceException("Cannot reBalance before June");
        }
        return this.getBalance(lastMonth);
    }

    private Map<Fund, Integer> getInitialInvestmentSplit() {
        return fundAccountList.stream().collect(Collectors.toMap(fundAccount -> fundAccount.getFund(), fundAccount -> fundAccount.getInitialBalance()));
    }
}
