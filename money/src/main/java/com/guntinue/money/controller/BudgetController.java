package com.guntinue.money.controller;

import com.guntinue.common.ApiResponse;
import com.guntinue.money.dto.BudgetRequest;
import com.guntinue.money.dto.BudgetStatusResponse;
import com.guntinue.money.service.BudgetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // Creating/Updating Budget
    // Normally memberId would come from Security Context, but using param/hardcoded
    // for now as per history
    @PostMapping
    public ApiResponse<Long> setBudget(@RequestParam Long memberId, @RequestBody BudgetRequest request) {
        return ApiResponse.success(budgetService.setBudget(memberId, request));
    }

    // Get Status
    @GetMapping("/status")
    public ApiResponse<List<BudgetStatusResponse>> getBudgetStatus(
            @RequestParam Long memberId,
            @RequestParam String yearMonth) {
        return ApiResponse.success(budgetService.getBudgetStatus(memberId, yearMonth));
    }
}
