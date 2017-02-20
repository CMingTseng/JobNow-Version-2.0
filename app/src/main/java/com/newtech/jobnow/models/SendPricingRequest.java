package com.newtech.jobnow.models;

import com.newtech.jobnow.common.APICommon;

/**
 * Created by manhi on 7/9/2016.
 */
public class SendPricingRequest extends BaseRequest {

    public static final String PATH_URL = "/api/v1/users/sendPricing";
    String Email;

    public SendPricingRequest(String email) {
        super(PATH_URL);
        Email = email;
    }
}
