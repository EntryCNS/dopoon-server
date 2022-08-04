package com.swcns.dopoonserver.domain.finance.entity;

import com.swcns.dopoonserver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn
    private User user;

    private String productName;

    private Integer productCost;

    private String productImageUrl;

    @CreatedDate
    private LocalDate startAt;

    private LocalDate finishAt;

    private boolean isDecided;

    public void decideGoal() {
        this.isDecided = true;
    }

    @OneToMany(mappedBy = "id.goal")
    private List<MonthlyPlan> monthlyPlanList;
    public void addMonthlyPlan(MonthlyPlan plan) {
        monthlyPlanList.add(plan);
    }
}
