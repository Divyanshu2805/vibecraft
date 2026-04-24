package com.java.vibecraft.service.impl;

import com.java.vibecraft.dto.subscription.PlanLimitsResponse;
import com.java.vibecraft.dto.subscription.UsageTodayResponse;
import com.java.vibecraft.service.UsageService;
import org.springframework.stereotype.Service;

@Service
public class UsageServiceImpl implements UsageService {
    @Override
    public UsageTodayResponse getTodayUsageOfUser() {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimitsOfUser() {
        return null;
    }
}
