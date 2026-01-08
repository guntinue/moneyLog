package com.guntinue.money.dto;

import java.time.LocalDateTime;

public class MoneyLogRequest {
    private String title;
    private Long amount;
    private String description;
    private String category;
    private LocalDateTime transactionTime;

    public MoneyLogRequest() {
    }

    public MoneyLogRequest(String title, Long amount, String description, String category,
            LocalDateTime transactionTime) {
        this.title = title;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.transactionTime = transactionTime;
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
