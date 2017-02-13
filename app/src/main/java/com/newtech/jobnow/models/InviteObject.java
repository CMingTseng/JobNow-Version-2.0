package com.newtech.jobnow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by manhi on 26/8/2016.
 */
public class InviteObject implements Serializable {
    int id;
    String CompanyName;
    String FirstName;
    String LastName;
    String Email;
    String created_at;
    String updated_at;
    int Status;
    int User_id;

    public InviteObject() {
    }

    public InviteObject(int id, String companyName, String firstName, String lastName, String email, String created_at, String updated_at, int status, int user_id) {
        this.id = id;
        CompanyName = companyName;
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        this.created_at = created_at;
        this.updated_at = updated_at;
        Status = status;
        User_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }
}
