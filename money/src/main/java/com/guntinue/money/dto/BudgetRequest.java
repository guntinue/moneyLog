package com.guntinue.money.dto;

public class BudgetRequest {
    private String category;
    private Long targetAmount;
    private String yearMonth; // YYYY-MM

    public BudgetRequest() {
    }

    public BudgetRequest(String category, Long targetAmount, String yearMonth) {
        this.category = category;
        this.targetAmount = targetAmount;
        this.yearMonth = yearMonth;
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
