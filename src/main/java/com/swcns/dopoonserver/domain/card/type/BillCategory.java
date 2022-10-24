package com.swcns.dopoonserver.domain.card.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor @Getter
public enum BillCategory {
    FOOD(35.0),
    TRANSPORT(15.0),
    HOBBY(70.0),
    ETC(25.0);


    private static boolean arrayContains(List<String> items, String keyword) {
        boolean condition = false;
        for(String item: items)
            condition |= keyword.contains(item);

        return condition;
    }

    private static List<String> CANDIDATES_FOOD = List.of(
        "교통", "철도", "도로", "에어", "항공"
    );
    private static List<String> CANDIDATES_TRANSPORT = List.of(
        "무신사", "룩핀"
    );
    private static List<String> CANDIDATES_HOBBY = List.of(
        "스타벅스", "하삼동커피"
    );

    private double maximumReducePercent;

    public static BillCategory parseFromString(String category) {
        if(arrayContains(CANDIDATES_TRANSPORT, category)) {
            return TRANSPORT;
        }else if (arrayContains(CANDIDATES_HOBBY, category)) {
            return HOBBY;
        }else if (arrayContains(CANDIDATES_FOOD, category)) {
            return FOOD;
        }else {
            return ETC;
        }
    }
}
