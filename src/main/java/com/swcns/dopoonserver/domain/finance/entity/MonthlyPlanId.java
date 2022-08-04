package com.swcns.dopoonserver.domain.finance.entity;

import com.swcns.dopoonserver.domain.card.type.BillCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor
@Getter @Builder
@Embeddable
public class MonthlyPlanId implements Serializable {
    private LocalDate yearAndMonth;

    @Enumerated(EnumType.STRING)
    private BillCategory category;

    @ManyToOne
    @JoinColumn
    private Goal goal;
}
