package com.newtech.jobnow.models;

import java.io.Serializable;

/**
 * Created by manhi on 26/8/2016.
 */
public class CategoryObject implements Serializable {
    int id;
    String CompanyID;
    String Name;
    String created_at;
    String updated_at;
    int number_shortlist;

    public CategoryObject() {
    }

    public CategoryObject(int id, String companyID, String name, String created_at, String updated_at, int number_shortlist) {
        this.id = id;
        CompanyID = companyID;
        Name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.number_shortlist = number_shortlist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public int getNumber_shortlist() {
        return number_shortlist;
    }

    public void setNumber_shortlist(int number_shortlist) {
        this.number_shortlist = number_shortlist;
    }
}
