package com.java.vibecraft.service;

import com.java.vibecraft.dto.subscription.CheckoutRequest;
import com.java.vibecraft.dto.subscription.CheckoutResponse;
import com.java.vibecraft.dto.subscription.PortalResponse;
import com.java.vibecraft.dto.subscription.SubscriptionResponse;
import com.java.vibecraft.enums.SubscriptionStatus;

import java.time.Instant;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription();

    void activateSubscription(Long userId, Long planId, String subscriptionId, String customerId);

    void updateSubscription(String gatewaySubscriptionId, SubscriptionStatus status, Instant periodStart, Instant periodEnd, Boolean cancelAtPeriodEnd, Long planId);

    void cancelSubscription(String gatewaySubscriptionId);

    void renewSubscriptionPeriod(String gatewaySubscriptionId, Instant periodStart, Instant periodEnd);

    void markSubscriptionPastDue(String gatewaySubscriptionId);

    boolean canCreateNewProject();
}
