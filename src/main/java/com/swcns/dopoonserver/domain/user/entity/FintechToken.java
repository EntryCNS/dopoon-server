package com.swcns.dopoonserver.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor @NoArgsConstructor
@Builder @Getter
@Entity
public class FintechToken {
    @Id
    private Long id;

    @MapsId
    @OneToOne @JoinColumn
    private User user;

    @Column(nullable = false)
    private String userNumber;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;
}
