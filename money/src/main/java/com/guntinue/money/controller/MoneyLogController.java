package com.guntinue.money.controller;

import com.guntinue.common.ApiResponse;
import com.guntinue.money.dto.MoneyLogRequest;
import com.guntinue.money.dto.MoneyLogResponse;
import com.guntinue.money.service.MoneyLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/money-log")
public class MoneyLogController {

    private final MoneyLogService moneyLogService;

    public MoneyLogController(MoneyLogService moneyLogService) {
        this.moneyLogService = moneyLogService;
    }

    @PostMapping
    public ApiResponse<Long> createLog(@RequestParam Long memberId, @RequestBody MoneyLogRequest request) {
        return ApiResponse.success(moneyLogService.createLog(memberId, request));
    }

    @GetMapping("/monthly")
    public ApiResponse<List<MoneyLogResponse>> getMonthlyLogs(
            @RequestParam Long memberId,
            @RequestParam String yearMonth) {
        return ApiResponse.success(moneyLogService.getMonthlyLogs(memberId, yearMonth));
    }

    @GetMapping("/recent")
    public ApiResponse<List<MoneyLogResponse>> getRecentLogs(@RequestParam Long memberId) {
        return ApiResponse.success(moneyLogService.getRecentLogs(memberId));
    }
}
