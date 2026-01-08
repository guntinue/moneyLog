package com.guntinue.money.service;

import com.guntinue.money.domain.Budget;
import com.guntinue.money.dto.BudgetRequest;
import com.guntinue.money.dto.BudgetStatusResponse;
import com.guntinue.money.repository.BudgetRepository;
import com.guntinue.money.repository.MoneyLogRepository;
import com.guntinue.user.domain.Member;
import com.guntinue.user.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final MoneyLogRepository moneyLogRepository;
    private final MemberRepository memberRepository;

    public BudgetService(BudgetRepository budgetRepository, MoneyLogRepository moneyLogRepository,
            MemberRepository memberRepository) {
        this.budgetRepository = budgetRepository;
        this.moneyLogRepository = moneyLogRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Long setBudget(Long memberId, BudgetRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Budget budget = budgetRepository
                .findByMemberIdAndYearMonthAndCategory(memberId, request.getYearMonth(), request.getCategory())
                .orElse(new Budget(member, request.getCategory(), request.getTargetAmount(), request.getYearMonth()));

        budget.updateAmount(request.getTargetAmount());
        budgetRepository.save(budget);
        return budget.getId();
    }

    public List<BudgetStatusResponse> getBudgetStatus(Long memberId, String yearMonth) {
        List<Budget> budgets = budgetRepository.findByMemberIdAndYearMonth(memberId, yearMonth);

        YearMonth ym = YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

        return budgets.stream().map(budget -> {
            Long currentAmount = moneyLogRepository.sumAmountByMemberIdAndCategory(
                    memberId, budget.getCategory(), start, end);
            if (currentAmount == null)
                currentAmount = 0L;
            return new BudgetStatusResponse(budget.getId(), budget.getCategory(), budget.getTargetAmount(),
                    currentAmount);
        }).collect(Collectors.toList());
    }
}
