package com.navi.mymoney.model;

import lombok.Data;

@Data
public class SIP {
    private Investor investor;
    private Fund fund;
    private Integer amount;
    private Integer duration;
}
