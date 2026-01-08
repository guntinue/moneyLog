package com.guntinue.money.domain;

import com.guntinue.user.domain.Member;
import jakarta.persistence.*;
import java.time.YearMonth;

/**
 * 예산(목표) 엔티티.
 * 특정 연월의 카테고리별 목표 금액을 저장합니다.
 */
@Entity
@Table(name = "budget", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "member_id", "year_month", "category" })
})
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String category; // e.g., "FOOD", "TRANSPORT"

    @Column(name = "target_amount", nullable = false)
    private Long targetAmount;

    @Column(name = "year_month", nullable = false)
    private String yearMonth; // Format: "YYYY-MM"

    protected Budget() {
    }

    public Budget(Member member, String category, Long targetAmount, String yearMonth) {
        this.member = member;
        this.category = category;
        this.targetAmount = targetAmount;
        this.yearMonth = yearMonth;
    }

    public void updateAmount(Long targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getCategory() {
        return category;
    }

    public Long getTargetAmount() {
        return targetAmount;
    }

    public String getYearMonth() {
        return yearMonth;
    }
}
