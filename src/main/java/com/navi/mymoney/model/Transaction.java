package com.navi.mymoney.model;

import lombok.Data;

import java.time.Month;

@Data
public class Transaction {
    private final TxnType type;
    private final Integer amount;
    private final Month month;
}
