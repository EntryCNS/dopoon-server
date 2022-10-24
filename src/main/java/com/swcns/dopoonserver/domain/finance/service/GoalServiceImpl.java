package com.swcns.dopoonserver.domain.finance.service;

import com.swcns.dopoonserver.domain.card.entity.Bill;
import com.swcns.dopoonserver.domain.card.repository.BillRepository;
import com.swcns.dopoonserver.domain.card.type.BillCategory;
import com.swcns.dopoonserver.domain.finance.entity.CurrentGoal;
import com.swcns.dopoonserver.domain.finance.entity.GoalHistory;
import com.swcns.dopoonserver.domain.finance.entity.MonthlyPlan;
import com.swcns.dopoonserver.domain.finance.entity.MonthlyPlanId;
import com.swcns.dopoonserver.domain.finance.exception.GoalCreationImpossibleException;
import com.swcns.dopoonserver.domain.finance.presentation.dto.request.GoalCreationRequest;
import com.swcns.dopoonserver.domain.finance.presentation.dto.response.*;
import com.swcns.dopoonserver.domain.finance.repository.CurrentGoalRepository;
import com.swcns.dopoonserver.domain.finance.repository.GoalHistoryRepository;
import com.swcns.dopoonserver.domain.finance.repository.MonthlyPlanRepository;
import com.swcns.dopoonserver.domain.finance.type.GoalFinishType;
import com.swcns.dopoonserver.domain.user.entity.User;
import com.swcns.dopoonserver.domain.user.exception.UserNotFoundException;
import com.swcns.dopoonserver.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class GoalServiceImpl implements GoalService {

    private final UserFacade userFacade;
    private final CurrentGoalRepository currentGoalRepository;
    private final GoalHistoryRepository goalHistoryRepository;
    private final MonthlyPlanRepository monthlyPlanRepository;
    private final BillRepository billRepository;

    private int getSavedMoney(Map<BillCategory, Integer> usages, Map<BillCategory, Double> savePercent, int month) {
        int saved = 0;
        for(BillCategory key: usages.keySet()) {
            saved += (usages.get(key) * (savePercent.get(key) / 100));
        }
        return saved * month;
    }

    private List<MonthlyPlan> generatePlan(GoalCreationRequest goalCreationRequest, CurrentGoal goal) {
        final double REDUCE_UNIT = 0.001;

        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UserNotFoundException::new);

        List<Bill> bills = new ArrayList<>();
        LocalDate billStartDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
        LocalDate billEndDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 32);
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        bills.addAll(billRepository.findAllByUser(user.getId(), dateFormatter.format(billStartDate), dateFormatter.format(billEndDate)));

        Map<BillCategory, Integer> currentUsages = new HashMap<>();
        bills.forEach(bill -> currentUsages.put(bill.getBillCategory(), currentUsages.getOrDefault(bill.getBillCategory(), 0) + bill.getPrice()));

        BillCategory[] categories = BillCategory.values();
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

        List<MonthlyPlan> result = new ArrayList<>();
        LocalDate today = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        for (BillCategory category: categories) {
            MonthlyPlanId planId = MonthlyPlanId.builder()
                    .goal(goal)
                    .yearAndMonth(today)
                    .category(category)
                    .build();

            MonthlyPlan plan = MonthlyPlan.builder()
                    .id(planId)
                    .usableMoney((int)(currentUsages.get(category) - currentUsages.get(category) * (reducePercents.get(category) / 100)))
                    .build();
            result.add(plan);
        }

        return result;
    }

    @Override
    @Transactional
    public void createNewGoal(GoalCreationRequest goalCreationRequest) {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UserNotFoundException::new);

        LocalDate finishDate = LocalDate.now();
        finishDate.plusMonths(goalCreationRequest.getMonth());

        CurrentGoal newGoal = CurrentGoal.builder()
                .user(user)
                .productName(goalCreationRequest.getProductName())
                .productCost(goalCreationRequest.getCost())
                .isDecided(false)
                .productImageUrl("TESTING")
                .finishAt(finishDate)
                .monthlyPlanList(new ArrayList<>())
                .build();
        newGoal = currentGoalRepository.save(newGoal);
        user.setGoal(newGoal);
        List<MonthlyPlan> plans = generatePlan(goalCreationRequest, newGoal);
        for(MonthlyPlan plan : plans) {
            newGoal.addMonthlyPlan(monthlyPlanRepository.save(plan));
        }

        currentGoalRepository.save(newGoal);
    }

    @Override
    @Transactional(readOnly = true)
    public GoalResponse getMyGoal() {
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UserNotFoundException::new);

        CurrentGoal goal = user.getGoal();
        LocalDate currentMonth = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        List<MonthlyPlan> plans = monthlyPlanRepository.getPlansByMonth(goal, currentMonth);
        if(plans.size() == 0) {
            log.info("이번달 계획이 존재하지 않으므로 계획을 생성합니다");
            LocalDate periodDate = goal.getFinishAt().minusYears(goal.getStartAt().getYear()).minusMonths(goal.getStartAt().getMonthValue());
            LocalDate leftDate = goal.getFinishAt().minusYears(LocalDate.now().getYear()).minusMonths(LocalDate.now().getMonthValue());

            int leftMonths = leftDate.getYear() * 12 + leftDate.getMonthValue();
            int periodMonths = periodDate.getYear() * 12 + periodDate.getMonthValue();

            GoalCreationRequest goalDetailForMonth = GoalCreationRequest.builder()
                    .month(leftMonths)
                    .cost((goal.getProductCost() / periodMonths) * leftMonths)
                    .build();

            List<MonthlyPlan> plansForThisMonth = generatePlan(goalDetailForMonth, goal);
            for(MonthlyPlan plan : plansForThisMonth) {
                goal.addMonthlyPlan(monthlyPlanRepository.save(plan));
            }
            plans.addAll(plansForThisMonth);
        }

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
    @Transactional
    public void finishGoal(GoalFinishType finishType) {
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UserNotFoundException::new);

        GoalHistory history = GoalHistory.getInstanceByParent(user.getGoal().getParentInstance(),
                user, GoalFinishType.SUCCESS.equals(finishType));
        user.addGoalHistory(history);

        user.setGoal(null);
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

        List<ExpenditureGroupResponse> groups = expenditures.stream()
                .collect(Collectors.groupingByConcurrent(it -> it.getPurchasedAt().split(" ")[0]))
                .entrySet().stream()
                .map(entry -> ExpenditureGroupResponse.builder()
                        .date(entry.getKey())
                        .list(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return ExpenditureListResponse.builder()
                .page(page)
                .pageCount(bills.getTotalPages())
                .result(groups)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public GoalHistoriesResponse getMyGoalHistories() {
        final SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd");
        User user = userFacade.queryCurrentUser(true)
                .orElseThrow(UserNotFoundException::new);

        return new GoalHistoriesResponse(
                user.getGoalHistories().stream().map(history -> {
                    return GoalHistoryResponse.builder()
                            .productName(history.getProductName())
                            .cost(history.getProductCost())
                            .isSuccess(history.getIsSuccess())
                            .imageUrl(history.getProductImageUrl())
                            .startAt(timeFormatter.format(history.getStartAt()))
                            .finishAt(timeFormatter.format(history.getFinishAt()))
                            .build();
                }).collect(Collectors.toList())
        );
    }
}
