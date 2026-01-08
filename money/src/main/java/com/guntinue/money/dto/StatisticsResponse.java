package com.guntinue.money.dto;

public class StatisticsResponse {
    private String timeLabel;
    private Long totalAmount;

    public StatisticsResponse() {
    }

    public StatisticsResponse(String timeLabel, Long totalAmount) {
        this.timeLabel = timeLabel;
        this.totalAmount = totalAmount;
    }

    public String getTimeLabel() {
        return timeLabel;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }
}
