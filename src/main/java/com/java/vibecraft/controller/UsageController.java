package com.java.vibecraft.controller;

import com.java.vibecraft.dto.subscription.PlanLimitsResponse;
import com.java.vibecraft.dto.subscription.UsageTodayResponse;
import com.java.vibecraft.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageController {

    private final UsageService usageService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage() {
        return ResponseEntity.ok(usageService.getTodayUsageOfUser());
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitsResponse> getPlanLimits() {
        return ResponseEntity.ok(usageService.getCurrentSubscriptionLimitsOfUser());
    }

}
