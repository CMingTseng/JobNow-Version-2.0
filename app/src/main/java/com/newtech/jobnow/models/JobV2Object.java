package com.newtech.jobnow.models;

import java.io.Serializable;

/**
 * Created by manhi on 26/8/2016.
 */
public class JobV2Object implements Serializable {
    public Integer id;
    public int JobSeekerID;
    public Integer JobID;
    public Integer CompanyID;
    public String Title;
    public String Position;
    public Integer Level;
    public String YearOfExperience;
    public Integer LocationID;
    public Integer IndustryID;
    public String FromSalary;
    public String ToSalary;
    public Integer CurrencyID;
    public Integer IsDisplaySalary;
    public String Description;
    public String Requirement;
    public Integer IsActive;
    public String created_at;
    public String updated_at;
    public String Start_date;
    public String End_date;
    public int JobLevelID;
    public String Location;
    public String SkillList;
    public int ExperienceID;
    public String LocationName;
    public String CompanyName;
    public String CompanyLogo;
    public String IndustryName;
    public String CurrencyName;
    public long CreateDate_int;
    public Integer CountUserApplyJob;
    public boolean IsApplyJob;
    public boolean IsSaveJob;
    public String ShareUrl;
    public Double Latitude;
    public Double Longitude;
    public long created_at_int;
    public long updated_at_int;
    public long Start_date_int;
    public long End_date_int;

    public CreateDateObject CreateDate;
}
