package com.java.vibecraft.mapper;

import com.java.vibecraft.dto.subscription.PlanResponse;
import com.java.vibecraft.dto.subscription.SubscriptionResponse;
import com.java.vibecraft.entity.Plan;
import com.java.vibecraft.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

}
