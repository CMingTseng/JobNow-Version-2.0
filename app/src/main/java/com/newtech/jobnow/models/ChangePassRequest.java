package com.newtech.jobnow.models;

import java.io.Serializable;

/**
 * Created by SANG on 8/21/2016.
 */
public class ChangePassRequest extends BaseRequest implements Serializable {

    private static final String PATH_URL = "api/v1/users/changePassword";

    String Email;
    String OldPassword;
    String NewPassword;

    public ChangePassRequest(String email, String oldPassword, String newPassword) {
        super(PATH_URL);
        Email = email;
        OldPassword = oldPassword;
        NewPassword = newPassword;
    }
}
