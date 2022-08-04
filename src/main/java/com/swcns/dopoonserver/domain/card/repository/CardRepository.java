package com.swcns.dopoonserver.domain.card.repository;

import com.swcns.dopoonserver.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

}
