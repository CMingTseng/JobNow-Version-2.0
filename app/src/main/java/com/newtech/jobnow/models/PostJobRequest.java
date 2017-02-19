package com.newtech.jobnow.models;

import java.io.Serializable;

public class PostJobRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "api/v1/jobs/postCreateJob";
    int JobID;
    String Title;
    String Position;
    int Level;
    String YearOfExperience;
    int LocationID;
    int IndustryID;
    float FromSalary;
    float ToSalary;
    int CurrencyID;
    int IsDisplaySalary;
    int JobLevelID;
    float Latitude;
    float Longitude;
    String Description;
    String Requirement;
    String Start_date;
    String End_date;
    int IsActive;
    String Location;
    String SkillList;
    String ExperienceID;

    int UserID;
    String ApiToken;

    public PostJobRequest(String title, String position, int level, String yearOfExperience, int locationID, int industryID, float fromSalary, float toSalary, int currencyID, int isDisplaySalary, int jobLevelID, float latitude, float longitude, String description, String requirement, String start_date, String end_date, int isActive, String location, String skillList, String experienceID, int userID, String apiToken) {
        super(PATH_URL);
        Title = title;
        Position = position;
        Level = level;
        YearOfExperience = yearOfExperience;
        LocationID = locationID;
        IndustryID = industryID;
        FromSalary = fromSalary;
        ToSalary = toSalary;
        CurrencyID = currencyID;
        IsDisplaySalary = isDisplaySalary;
        JobLevelID = jobLevelID;
        Latitude = latitude;
        Longitude = longitude;
        Description = description;
        Requirement = requirement;
        Start_date = start_date;
        End_date = end_date;
        IsActive = isActive;
        Location = location;
        SkillList = skillList;
        ExperienceID = experienceID;
        UserID = userID;
        ApiToken = apiToken;
    }

    public PostJobRequest(int jobID,String title, String position, int level, String yearOfExperience, int locationID, int industryID, float fromSalary, float toSalary, int currencyID, int isDisplaySalary, int jobLevelID, float latitude, float longitude, String description, String requirement, String start_date, String end_date, int isActive, String location, String skillList, String experienceID, int userID, String apiToken) {
        super(PATH_URL);
        JobID=jobID;
        Title = title;
        Position = position;
        Level = level;
        YearOfExperience = yearOfExperience;
        LocationID = locationID;
        IndustryID = industryID;
        FromSalary = fromSalary;
        ToSalary = toSalary;
        CurrencyID = currencyID;
        IsDisplaySalary = isDisplaySalary;
        JobLevelID = jobLevelID;
        Latitude = latitude;
        Longitude = longitude;
        Description = description;
        Requirement = requirement;
        Start_date = start_date;
        End_date = end_date;
        IsActive = isActive;
        Location = location;
        SkillList = skillList;
        ExperienceID = experienceID;
        UserID = userID;
        ApiToken = apiToken;
    }
}
