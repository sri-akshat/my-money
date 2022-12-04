package com.navi.mymoney.output;

import com.navi.mymoney.model.Fund;

import java.util.Map;

public class ConsoleAdapter {
    public void print(Map<Fund, Integer> fundIntegerMap) {

        fundIntegerMap.forEach((fund, integer) -> System.out.print(integer + " "));
        System.out.println();
    }

    public void print(String message) {
        System.out.print(message);
        System.out.println();
    }
}
