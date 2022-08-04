package com.swcns.dopoonserver.domain.finance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
@Entity
public class MonthlyPlan {
    @EmbeddedId
    private MonthlyPlanId id;

    private Integer usableMoney;
}
