package com.swcns.dopoonserver.domain.finance.entity;

import com.swcns.dopoonserver.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class GoalHistory extends Goal {
    @ManyToOne
    @JoinColumn
    private User user;
    private Boolean isSuccess;

    public static GoalHistory getInstanceByParent(Goal parent, User owner, boolean success) {
        GoalHistory oldGoal = new GoalHistory();
        BeanUtils.copyProperties(parent, oldGoal);
        oldGoal.user = owner;
        oldGoal.isSuccess = success;

        return oldGoal;
    }

    @Builder
    public GoalHistory(Long id, String productName, Integer productCost, String productImageUrl, LocalDate startAt, LocalDate finishAt, User user, Boolean isSuccess) {
        super(id, productName, productCost, productImageUrl, startAt, finishAt);
        this.user = user;
        this.isSuccess = isSuccess;
    }
}
