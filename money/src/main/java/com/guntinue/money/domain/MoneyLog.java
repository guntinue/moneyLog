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
    private Long amount;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime transactionTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    protected MoneyLog() {
    }

    public MoneyLog(Long amount, String description, String category, LocalDateTime transactionTime, Member member) {
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.transactionTime = transactionTime;
        this.member = member;
    }

    public Long getId() {
        return id;
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
