package com.swcns.dopoonserver.domain.finance.repository;

import com.swcns.dopoonserver.domain.finance.entity.MonthlyPlan;
import com.swcns.dopoonserver.domain.finance.entity.MonthlyPlanId;
import org.springframework.data.repository.CrudRepository;

public interface MonthlyPlanRepository extends CrudRepository<MonthlyPlan, MonthlyPlanId> {

}
