package com.swcns.dopoonserver.domain.finance.entity;

import com.swcns.dopoonserver.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class CurrentGoal extends Goal {
    @OneToOne
    @JoinColumn
    private User user;
    private boolean isDecided;

    public void decideGoal() {
        this.isDecided = true;
    }

    @OneToMany(mappedBy = "id.goal")
    private List<MonthlyPlan> monthlyPlanList;
    public void addMonthlyPlan(MonthlyPlan plan) {
        monthlyPlanList.add(plan);
    }

    @Builder
    public CurrentGoal(Long id, String productName, Integer productCost, String productImageUrl, LocalDate startAt, LocalDate finishAt, User user, boolean isDecided, List<MonthlyPlan> monthlyPlanList) {
        super(id, productName, productCost, productImageUrl, startAt, finishAt);
        this.user = user;
        this.isDecided = isDecided;
        this.monthlyPlanList = monthlyPlanList;
    }

    public Goal getParentInstance() {
        try {
            return (Goal)super.clone();
        } catch (Exception ex) {
            return null;
        }
    }
}
