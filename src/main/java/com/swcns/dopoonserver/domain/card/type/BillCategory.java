package com.swcns.dopoonserver.domain.card.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum BillCategory {
    FOOD(35.0),
    TRANSPORT(15.0),
    HOBBY(70.0),
    ETC(25.0);

    private double maximumReducePercent;
}
