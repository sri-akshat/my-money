package com.navi.mymoney.model;

import lombok.Data;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Data
public class FundAccount {
    private Fund fund;
    private List<Transaction> transactions;

    public FundAccount(Fund fund, Transaction txn) {
        this.fund = fund;
        this.transactions = new ArrayList<>();
        this.transactions.add(txn);
    }


    public Integer getTxnBalance(Month month) {
        return getTxnBalance(month, TxnType.BUY);
    }

    public Integer getBalance(Month month) {
        Integer lastBal = 0;
        for (Month m: Month.values()) {
            lastBal += getTxnBalance(m);
            lastBal += (int) Math.floor(lastBal * fund.getMarketChange().getOrDefault(m, 0.0)/100);
            lastBal += getTxnBalance(m, TxnType.ADJUSTMENT);
            if(m == month) {
                break;
            }
        }
        return lastBal;
    }

    private Integer getTxnBalance(Month month, TxnType txnType) {
        return transactions.stream().filter(txn -> txn.getMonth().getValue() == month.getValue() && txn.getType() == txnType).mapToInt(Transaction::getAmount).sum();
    }

    public Integer getInitialBalance() {
        return transactions.stream().filter(txn -> txn.getMonth() == Month.JANUARY).mapToInt(Transaction::getAmount).sum();
    }

    public void adjustBalance(Integer amount) {
        transactions.add(new Transaction(TxnType.ADJUSTMENT, amount, Month.JUNE));
    }
}
