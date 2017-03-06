package com.newtech.jobnow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by SANG on 8/21/2016.
 */
public class ProfileRequest extends BaseRequest implements Serializable {

    private static final String PATH_URL = "/api/v1/companyprofile/postUpdateCompany";

    int UserID;
    String ApiToken;
    String Phone;
    String Description;
    String Name;

    public ProfileRequest(int userID, String apiToken, String phone, String description,String name) {
        super(PATH_URL);
        UserID = userID;
        ApiToken = apiToken;
        Phone = phone;
        Description = description;
        Name=name;
    }
}
