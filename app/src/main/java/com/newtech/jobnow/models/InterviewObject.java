package com.newtech.jobnow.models;

import java.io.Serializable;

public class InterviewObject implements Serializable {
    public int id;
    public int JobSeekerID;
    public int CompanyID;
    public String Title;
    public String Content;
    public String InterviewDate;
    public String ContactName;
    public String PhoneNumber;
    public String Status;
    public String created_at;
    public String updated_at;
    public String Start_time;
    public String End_time;
    public String Location;
    public String Avatar;
    public String FullName;
    public String CountryName;
    public String InterviewDate_int;

    public InterviewObject() {
    }

}
