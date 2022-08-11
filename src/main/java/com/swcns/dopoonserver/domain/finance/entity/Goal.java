package com.swcns.dopoonserver.domain.finance.entity;

import com.swcns.dopoonserver.domain.user.entity.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor @Getter
@MappedSuperclass
public abstract class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String productName;

    protected Integer productCost;

    protected String productImageUrl;

    @CreatedDate
    protected LocalDate startAt;

    protected LocalDate finishAt;
}
