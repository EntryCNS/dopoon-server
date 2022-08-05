package com.swcns.dopoonserver.domain.user.repository;

import com.swcns.dopoonserver.domain.user.entity.FintechToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FintechTokenRepository extends CrudRepository<FintechToken, Long> {

}
