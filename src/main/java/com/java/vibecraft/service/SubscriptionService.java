package com.java.vibecraft.service;

import com.java.vibecraft.dto.subscription.CheckoutRequest;
import com.java.vibecraft.dto.subscription.CheckoutResponse;
import com.java.vibecraft.dto.subscription.PortalResponse;
import com.java.vibecraft.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription();

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal();
}
