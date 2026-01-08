package com.guntinue.money.repository;

import com.guntinue.money.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByMemberIdAndYearMonthAndCategory(Long memberId, String yearMonth, String category);

    List<Budget> findByMemberIdAndYearMonth(Long memberId, String yearMonth);
}
