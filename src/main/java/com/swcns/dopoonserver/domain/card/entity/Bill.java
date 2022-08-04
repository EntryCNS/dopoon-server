package com.swcns.dopoonserver.domain.card.entity;

import com.swcns.dopoonserver.domain.card.type.BillCategory;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter @ToString
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Card paymentCard;
    public void setCard(Card card) {
        this.paymentCard = card;
    }

    private String storeName;

    @Enumerated(value = EnumType.STRING)
    private BillCategory billCategory;

    private Integer price;

    private LocalDateTime billedAt;
}
