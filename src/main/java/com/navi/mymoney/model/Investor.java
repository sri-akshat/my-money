package com.navi.mymoney.model;

import lombok.Data;

@Data
public class Investor {
    private Integer id;
    private String name;
    private Portfolio portfolio;
}
