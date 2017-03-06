package com.newtech.jobnow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by manhi on 26/8/2016.
 */
public class JobListRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "api/v1/jobs/getListJob";
    public Integer page;
    public String Order;
    public String Title;
    public String Location;
    public String Skill;
    public Integer MinSalary;
    public Integer FromSalary;
    public Integer ToSalary;
    public Integer isEmployee;
    public Integer isHiring;
    public Integer CompanyID;
    public Integer IntRandom;
    @SerializedName("IndustryID")
    public Integer industryID;

    public JobListRequest(Integer page, String Order, String Title, String Location, String Skill,
                          Integer MinSalary, Integer FromSalary, Integer ToSalary) {
        super(PATH_URL);
        this.page = page;
        this.Order = Order;
        this.Title = Title;
        this.Location = Location;
        this.Skill = Skill;
        this.MinSalary = MinSalary;
        this.FromSalary = FromSalary;
        this.ToSalary = ToSalary;
    }

    public JobListRequest(Integer page, String Order, String Title, String Location, String Skill,
                          Integer MinSalary, Integer FromSalary, Integer ToSalary,
                          Integer industryID, Integer intRandom) {
        super(PATH_URL);
        this.page = page;
        this.Order = Order;
        this.Title = Title;
        this.Location = Location;
        this.Skill = Skill;
        this.MinSalary = MinSalary;
        this.FromSalary = FromSalary;
        this.ToSalary = ToSalary;
        this.industryID = industryID;
        this.IntRandom=intRandom;
    }

    public JobListRequest(String path_url, Integer page, String order, String title, String location, String skill, Integer minSalary, Integer fromSalary, Integer toSalary, Integer isEmployee, Integer isHiring, Integer companyID, Integer industryID) {
        super(PATH_URL);
        this.page = page;
        Order = order;
        Title = title;
        Location = location;
        Skill = skill;
        MinSalary = minSalary;
        FromSalary = fromSalary;
        ToSalary = toSalary;
        this.isEmployee = isEmployee;
        this.isHiring = isHiring;
        CompanyID = companyID;
        this.industryID = industryID;
    }
}
