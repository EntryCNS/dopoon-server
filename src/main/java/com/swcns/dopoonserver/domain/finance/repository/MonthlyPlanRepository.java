package com.swcns.dopoonserver.domain.finance.repository;

import com.swcns.dopoonserver.domain.finance.entity.CurrentGoal;
import com.swcns.dopoonserver.domain.finance.entity.MonthlyPlan;
import com.swcns.dopoonserver.domain.finance.entity.MonthlyPlanId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface MonthlyPlanRepository extends CrudRepository<MonthlyPlan, MonthlyPlanId> {
    @Query("SELECT p FROM MonthlyPlan p WHERE p.id.goal=:goal AND p.id.yearAndMonth=:yearAndMonth")
    List<MonthlyPlan> getPlansByMonth(CurrentGoal goal, LocalDate yearAndMonth);
}
