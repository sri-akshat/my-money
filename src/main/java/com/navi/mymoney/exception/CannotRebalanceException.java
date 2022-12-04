package com.navi.mymoney.exception;

public class CannotRebalanceException extends RuntimeException {
    private static final String msgCode = "CANNOT_REBALANCE";

    public CannotRebalanceException(String message) {
        super(message);
    }

    public String getMsgCode() {
        return msgCode;
    }
}
