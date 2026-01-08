package com.guntinue.money.domain;

import com.guntinue.user.domain.Member;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "money_log")
public class MoneyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long amount; // 거래 금액 (양수)

    @Column(nullable = false)
    private String title; // [NEW] 거래 제목 (간략한 설명)

    @Column(nullable = false)
    private String description; // 거래 내용/메모 (상세)

    @Column(nullable = false)
    private String category; // 카테고리 (e.g., "식비", "교통비")

    @Column(nullable = false)
    private LocalDateTime transactionTime; // 거래 발생 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 해당 내역의 소유자

    protected MoneyLog() {
    }

    public MoneyLog(String title, Long amount, String description, String category, LocalDateTime transactionTime,
            Member member) {
        this.title = title;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.transactionTime = transactionTime;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Long getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public Member getMember() {
        return member;
    }
}
