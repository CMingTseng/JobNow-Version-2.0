package com.newtech.jobnow.models;

import java.io.Serializable;
import java.util.List;


public class CategoryIndustryObject implements Serializable {
    public int id;
    public String Name;
    public int IsActive;
    public String Description;
    public String created_at;
    public String updated_at;

    public List<SkillObject> data;
}
