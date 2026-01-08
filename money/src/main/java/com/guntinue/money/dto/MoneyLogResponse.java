package com.guntinue.money.dto;

import com.guntinue.money.domain.MoneyLog;
import java.time.LocalDateTime;

public class MoneyLogResponse {
    private Long id;
    private String title;
    private Long amount;
    private String description;
    private String category;
    private LocalDateTime transactionTime;

    public MoneyLogResponse(MoneyLog log) {
        this.id = log.getId();
        this.title = log.getTitle();
        this.amount = log.getAmount();
        this.description = log.getDescription();
        this.category = log.getCategory();
        this.transactionTime = log.getTransactionTime();
    }

    // Getters needed for Jackson serialization
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
}
