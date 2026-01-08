package com.guntinue.money.repository;

import com.guntinue.money.domain.MoneyLog;
import com.guntinue.money.dto.StatisticsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MoneyLogRepository extends JpaRepository<MoneyLog, Long> {

        // Using a safe approach for PostgreSQL date functions or returning Object[] to
        // handle in service if needed.
        // However, trying a standard JPQL approach first.
        // Previous error was 'Type mismatch'. Ensuring constructor types match exactly.
        // Hour extraction in standard JPQL is not always consistent.
        // We will use a native query for reliability with PostgreSQL extract function
        // if standard JPQL fails,
        // but here is a standard JPQL attempt suitable for H2/MySQL/Postgres usually
        // (with creating DTO).

        @Query("SELECT new com.guntinue.money.dto.StatisticsResponse(" +
                        "   CONCAT(CAST(FUNCTION('HOUR', m.transactionTime) AS string), ':00'), " +
                        "   SUM(m.amount)) " +
                        "FROM MoneyLog m " +
                        "WHERE m.transactionTime BETWEEN :startTime AND :endTime " +
                        "GROUP BY FUNCTION('HOUR', m.transactionTime)")
        List<StatisticsResponse> findHourlyStatistics(@Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        @Query("SELECT SUM(m.amount) FROM MoneyLog m " +
                        "WHERE m.member.id = :memberId " +
                        "AND m.category = :category " +
                        "AND m.transactionTime BETWEEN :startTime AND :endTime")
        Long sumAmountByMemberIdAndCategory(@Param("memberId") Long memberId,
                        @Param("category") String category,
                        @Param("startTime") LocalDateTime startTime,
                        @Param("endTime") LocalDateTime endTime);

        List<MoneyLog> findByMemberIdAndTransactionTimeBetweenOrderByTransactionTimeDesc(Long memberId,
                        LocalDateTime start, LocalDateTime end);

        List<MoneyLog> findTop5ByMemberIdOrderByTransactionTimeDesc(Long memberId);
}
