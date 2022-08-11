package com.swcns.dopoonserver.domain.finance.repository;

import com.swcns.dopoonserver.domain.finance.entity.CurrentGoal;
import com.swcns.dopoonserver.domain.finance.entity.Goal;
import org.springframework.data.repository.CrudRepository;

public interface CurrentGoalRepository extends CrudRepository<CurrentGoal, Long> {
}
