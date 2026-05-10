package com.java.vibecraft.service.impl;

import com.java.vibecraft.dto.subscription.PlanLimitsResponse;
import com.java.vibecraft.dto.subscription.UsageTodayResponse;
import com.java.vibecraft.entity.UsageLog;
import com.java.vibecraft.repository.UsageLogRepository;
import com.java.vibecraft.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UsageServiceImpl implements UsageService {

    private final UsageLogRepository usageLogRepository;

    @Override
    public void recordTokenUsage(Long userId, int actualTokens) {
        LocalDate today = LocalDate.now();

        UsageLog todayLog = usageLogRepository.findByUserIdAndDate(userId, today).
                orElseGet(() -> createNewDailyLog(userId, today));

        todayLog.setTokensUsed(todayLog.getTokensUsed() + actualTokens);
        usageLogRepository.save(todayLog);
    }
    @Override
    public UsageTodayResponse getTodayUsageOfUser() {
        return null;
    }

    @Override
    public PlanLimitsResponse getCurrentSubscriptionLimitsOfUser() {
        return null;
    }

    private UsageLog createNewDailyLog(Long userId, LocalDate date) {
        UsageLog newLog = UsageLog.builder()
                .userId(userId)
                .date(date)
                .tokensUsed(0)
                .build();
        return usageLogRepository.save(newLog);
    }
}
