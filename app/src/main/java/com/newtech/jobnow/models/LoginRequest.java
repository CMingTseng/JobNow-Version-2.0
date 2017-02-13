package com.newtech.jobnow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by SANG on 8/21/2016.
 */
public class LoginRequest extends BaseRequest implements Serializable {

    private static final String PATH_URL = "/api/v1/users/postLogin";

    @SerializedName("Email")
    private String email;

    @SerializedName("Password")
    private String password;

    @SerializedName("isEmployee")
    private int isEmployee;

    public LoginRequest(String email, String password) {
        super(PATH_URL);
        this.email = email;
        this.password = password;
    }

    public LoginRequest(String email, String password, int isEmployee) {
        super(PATH_URL);
        this.email = email;
        this.password = password;
        this.isEmployee= isEmployee;
    }

    public static String getPathUrl() {
        return PATH_URL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(int isEmployee) {
        this.isEmployee = isEmployee;
    }
}
