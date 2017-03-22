package com.jobnow.models;

import java.io.Serializable;

/**
 * Created by manhi on 26/8/2016.
 */
public class InviteRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "api/v1/invite/setInvite";

    String CompanyName;
    String FirstName;
    String LastName;
    String Email;
    int Status;
    int User_id;

    public InviteRequest(String companyName, String firstName, String lastName, String email, int status, int user_id) {
        super(PATH_URL);
        CompanyName = companyName;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        Status = status;
        User_id = user_id;
    }
}
