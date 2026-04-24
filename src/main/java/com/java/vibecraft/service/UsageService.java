package com.java.vibecraft.service;

import com.java.vibecraft.dto.subscription.PlanLimitsResponse;
import com.java.vibecraft.dto.subscription.UsageTodayResponse;

public interface UsageService {

    UsageTodayResponse getTodayUsageOfUser();

    PlanLimitsResponse getCurrentSubscriptionLimitsOfUser();
}
