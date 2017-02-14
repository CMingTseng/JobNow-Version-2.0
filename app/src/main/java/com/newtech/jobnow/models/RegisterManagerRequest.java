package com.newtech.jobnow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tungpm on 21/8/2016.
 */
public class RegisterManagerRequest extends BaseRequest implements Serializable {
    private static final String PATH_URL = "api/v1/users/postRegisterEmployee";

    private String Name;
    private String Email;
    private String Password;
    private int IndustryID;
    private int CompanySizeID;
    private String ContactNumber;
    private String ContactName;

    public RegisterManagerRequest(String fullName, String email, String password, int industryID, int companySizeID,String contactNumber,String contactName) {
        super(PATH_URL);
        this.Name = fullName;
        this.Email = email;
        this.Password = password;
        this.IndustryID = industryID;
        this.CompanySizeID = companySizeID;
        this.ContactNumber=contactNumber;
        this.ContactName= contactName;
    }
}
