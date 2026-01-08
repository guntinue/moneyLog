package com.guntinue.money.controller;

import com.guntinue.common.ApiResponse;
import com.guntinue.money.dto.StatisticsResponse;
import com.guntinue.money.service.StatisticsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/hourly")
    public ApiResponse<List<StatisticsResponse>> getHourlyStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return ApiResponse.success(statisticsService.getHourlyStatistics(start, end));
    }
}
