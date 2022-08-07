package com.swcns.dopoonserver.domain.finance.service;

import com.swcns.dopoonserver.domain.card.entity.Bill;
import com.swcns.dopoonserver.domain.card.repository.BillRepository;
import com.swcns.dopoonserver.domain.card.type.BillCategory;
import com.swcns.dopoonserver.domain.finance.entity.Goal;
import com.swcns.dopoonserver.domain.finance.entity.MonthlyPlan;
import com.swcns.dopoonserver.domain.finance.entity.MonthlyPlanId;
import com.swcns.dopoonserver.domain.finance.exception.GoalCreationImpossibleException;
import com.swcns.dopoonserver.domain.finance.presentation.dto.request.GoalCreationRequest;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.ExpenditureListResponse;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.ExpenditureResponse;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.GoalResponse;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.UsableMoneyResponse;
import com.swcns.dopoonserver.domain.finance.repository.GoalRepository;
import com.swcns.dopoonserver.domain.finance.repository.MonthlyPlanRepository;
import com.swcns.dopoonserver.domain.user.entity.User;
import com.swcns.dopoonserver.domain.user.exception.UserNotFoundException;
import com.swcns.dopoonserver.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GoalServiceImpl implements GoalService {

    private final UserFacade userFacade;
    private final GoalRepository goalRepository;
    private final MonthlyPlanRepository monthlyPlanRepository;
    private final BillRepository billRepository;

    private int getSavedMoney(Map<BillCategory, Integer> usages, Map<BillCategory, Double> savePercent, int month) {
        int saved = 0;
        for(BillCategory key: usages.keySet()) {
            saved += (usages.get(key) * (savePercent.get(key) / 100));
        }
        return saved * month;
    }

    @Override
    @Transactional
    public void createNewGoal(GoalCreationRequest goalCreationRequest) {
        final double REDUCE_UNIT = 0.001;

        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UserNotFoundException::new);

        List<Bill> bills = new ArrayList<>();
        user.getCards().stream()
                .forEach(it -> bills.addAll(it.getBillList()));

        Map<BillCategory, Integer> currentUsages = new HashMap<>();
        bills.forEach(bill -> currentUsages.put(bill.getBillCategory(), currentUsages.getOrDefault(bill.getBillCategory(), 0) + bill.getPrice()));

        BillCategory categories[] = BillCategory.values();
        Map<BillCategory, Double> reducePercents = new HashMap<>();
        int goalCost = goalCreationRequest.getCost();
        Map<BillCategory, Double> maximumPercents = new HashMap<>();

        for(BillCategory category: categories) {
            reducePercents.put(category, 0.0);
            maximumPercents.put(category, category.getMaximumReducePercent());
        }

        if(getSavedMoney(currentUsages, maximumPercents, goalCreationRequest.getMonth()) < goalCost)
            throw new GoalCreationImpossibleException();

        while(getSavedMoney(currentUsages, reducePercents, goalCreationRequest.getMonth()) < goalCost) {
            for(BillCategory key: categories) {
                if(reducePercents.get(key) + REDUCE_UNIT <= key.getMaximumReducePercent()) {
                    reducePercents.put(key, reducePercents.get(key) + REDUCE_UNIT);
                }

                if(getSavedMoney(currentUsages, reducePercents, goalCreationRequest.getMonth()) >= goalCost)
                    break;
            }
        }

        LocalDate finishDate = LocalDate.now();
        finishDate.plusMonths(goalCreationRequest.getMonth());

        Goal newGoal = Goal.builder()
                .user(user)
                .productName(goalCreationRequest.getProductName())
                .productCost(goalCreationRequest.getCost())
                .isDecided(false)
                .productImageUrl("TESTING")
                .finishAt(finishDate)
                .monthlyPlanList(new ArrayList<>())
                .build();
        newGoal = goalRepository.save(newGoal);
        user.setGoal(newGoal);

        LocalDate today = LocalDate.now();
        for (BillCategory category: categories) {
            MonthlyPlanId planId = MonthlyPlanId.builder()
                    .goal(newGoal)
                    .yearAndMonth(today)
                    .category(category)
                    .build();

            MonthlyPlan plan = MonthlyPlan.builder()
                    .id(planId)
                    .usableMoney((int)(currentUsages.get(category) - currentUsages.get(category) * (reducePercents.get(category) / 100)))
                    .build();
            newGoal.addMonthlyPlan(monthlyPlanRepository.save(plan));
        }

        goalRepository.save(newGoal);
    }

    @Override
    @Transactional(readOnly = true)
    public GoalResponse getMyGoal() {
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UserNotFoundException::new);

        Goal goal = user.getGoal();
        List<MonthlyPlan> plans = goal.getMonthlyPlanList();
        List<UsableMoneyResponse> details = plans.stream().map(it -> {
            return UsableMoneyResponse.builder()
                    .category(it.getId().getCategory().name())
                    .usableMoney(it.getUsableMoney())
                    .build();
        }).collect(Collectors.toList());

        return GoalResponse.builder()
                .productName(goal.getProductName())
                .cost(goal.getProductCost())
                .imageUrl(goal.getProductImageUrl())
                .startAt(goal.getStartAt().format(dateFormatter))
                .finishAt(goal.getFinishAt().format(dateFormatter))
                .isDecided(goal.isDecided())
                .details(details)
                .build();
    }

    @Override
    @Transactional
    public void decideGoal() {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UserNotFoundException::new);
        user.getGoal().decideGoal();
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenditureListResponse getExpenditures(int year, int month, int page) {
        final DateTimeFormatter sqlDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

        User user = userFacade.queryCurrentUser()
                .orElseThrow(UserNotFoundException::new);

        Pageable pageRequest = PageRequest.of(page, 10);
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, 31);

        Page<Bill> bills = billRepository.findAllByUser(user.getId(), startDate.format(sqlDateFormatter), endDate.format(sqlDateFormatter), pageRequest);

        List<ExpenditureResponse> expenditures = bills.stream().map(bill -> {
            return ExpenditureResponse.builder()
                    .store(bill.getStoreName())
                    .bill(bill.getPrice())
                    .cardCompany(bill.getPaymentCard().getCompanyName())
                    .category(bill.getBillCategory().name())
                    .purchasedAt(dateTimeFormatter.format(bill.getBilledAt()))
                    .build();
        }).collect(Collectors.toList());

        return ExpenditureListResponse.builder()
                .page(page)
                .pageCount(bills.getTotalPages())
                .result(expenditures)
                .build();
    }
}
