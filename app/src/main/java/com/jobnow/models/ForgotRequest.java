package com.jobnow.models;

import java.io.Serializable;

public class ForgotRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "/api/v1/users/postForgot";
    String Email;

    public ForgotRequest(String email) {
        super(PATH_URL);
        Email = email;
    }
}
