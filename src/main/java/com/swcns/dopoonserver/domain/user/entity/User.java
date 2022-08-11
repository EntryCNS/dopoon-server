package com.swcns.dopoonserver.domain.user.entity;

import com.swcns.dopoonserver.domain.card.entity.Card;
import com.swcns.dopoonserver.domain.finance.entity.CurrentGoal;
import com.swcns.dopoonserver.domain.finance.entity.Goal;
import com.swcns.dopoonserver.domain.finance.entity.GoalHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor @NoArgsConstructor
@Getter
@Entity
@DynamicInsert @DynamicUpdate
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String userName;

    @NotNull
    private LocalDate birthday;

    @NotNull
    @Column(unique = true)
    private String loginId;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @OneToOne(mappedBy = "user")
    private FintechToken fintechToken;
    public void setFintechToken(FintechToken fintechToken) {
        this.fintechToken = fintechToken;
    }
    public void clearFintechToken() {
        this.fintechToken = null;
    }

    @OneToMany(mappedBy = "owner")
    private List<Card> cards;
    public void addCard(Card card) {
        this.cards.add(card);
    }
    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CurrentGoal goal;
    public void setGoal(CurrentGoal newGoal) {
        this.goal = newGoal;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<GoalHistory> goalHistories;
    public void addGoalHistory(GoalHistory oldGoal) {
        this.goalHistories.add(oldGoal);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
