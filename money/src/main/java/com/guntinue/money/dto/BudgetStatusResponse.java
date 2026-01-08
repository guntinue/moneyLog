package com.guntinue.money.dto;

public class BudgetStatusResponse {
    private Long budgetId;
    private String category;
    private Long targetAmount;
    private Long currentAmount;
    private Double ratio; // Percentage (e.g., 50.5)

    public BudgetStatusResponse(Long budgetId, String category, Long targetAmount, Long currentAmount) {
        this.budgetId = budgetId;
        this.category = category;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.ratio = targetAmount == 0 ? 0.0 : ((double) currentAmount / targetAmount) * 100.0;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public String getCategory() {
        return category;
    }

    public Long getTargetAmount() {
        return targetAmount;
    }

    public Long getCurrentAmount() {
        return currentAmount;
    }

    public Double getRatio() {
        return ratio;
    }
}
