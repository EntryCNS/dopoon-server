package com.swcns.dopoonserver.domain.card.entity;

import com.swcns.dopoonserver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn
    private User owner;

    @NotNull
    private String cardNumber;

    @OneToMany(mappedBy = "paymentCard")
    private List<Bill> billList;
}
