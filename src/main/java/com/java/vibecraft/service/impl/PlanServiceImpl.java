package com.java.vibecraft.service.impl;

import com.java.vibecraft.dto.subscription.PlanResponse;
import com.java.vibecraft.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {
    @Override
    public List<PlanResponse> getAllActivePlans() {
        return List.of();
    }
}
