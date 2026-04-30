package com.java.vibecraft.service;

import com.java.vibecraft.dto.subscription.CheckoutRequest;
import com.java.vibecraft.dto.subscription.CheckoutResponse;
import com.java.vibecraft.dto.subscription.PortalResponse;
import com.stripe.model.StripeObject;

import java.util.Map;

public interface PaymentProcessor {

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal();

    void handleWebhookEvent(String type, StripeObject stripeObject, Map<String, String> metadata);
}
