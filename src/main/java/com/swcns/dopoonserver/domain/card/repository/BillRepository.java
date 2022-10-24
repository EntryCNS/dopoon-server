package com.swcns.dopoonserver.domain.card.repository;

import com.swcns.dopoonserver.domain.card.entity.Bill;
import com.swcns.dopoonserver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BillRepository extends JpaRepository<Bill, Long> {
    @Query(value = "SELECT b.* FROM bill as b\n" +
            "    JOIN (SELECT c.id, u.id AS user_id FROM card as c JOIN user AS u ON u.id = c.owner_id WHERE c.owner_id=?1) as c\n" +
            "    ON b.payment_card_id = c.id\n" +
            "    WHERE b.billed_at BETWEEN ?2 AND ?3\n" +
            "    ORDER BY b.billed_at DESC",
            countQuery = "SELECT COUNT(*) FROM bill as b\n" +
                    "    JOIN (SELECT c.id, u.id AS user_id FROM card as c JOIN user AS u ON u.id = c.owner_id WHERE c.owner_id=?1) as c\n" +
                    "    ON b.payment_card_id = c.id\n" +
                    "    WHERE b.billed_at BETWEEN ?2 AND ?3\n" +
                    "    ORDER BY b.billed_at DESC",
            nativeQuery = true)
    Page<Bill> findAllByUser(long userId, String startDate, String endDate, Pageable pageable);

    @Query(value = "SELECT b.* FROM bill as b\n" +
            "    JOIN (SELECT c.id, u.id AS user_id FROM card as c JOIN user AS u ON u.id = c.owner_id WHERE c.owner_id=?1) as c\n" +
            "    ON b.payment_card_id = c.id\n" +
            "    WHERE billed_at BETWEEN ?2 AND ?3\n",
            nativeQuery = true)
    List<Bill> findAllByUser(long userId, String startDate, String endDate);
}
