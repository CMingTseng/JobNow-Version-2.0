package com.newtech.jobnow.models;

import java.io.Serializable;

public class LocationObject implements Serializable {
    public int id;
    public String ZipCode;
    public String Name;
    public int CountryID;
    public int IsActive;
    public String  Description;
    public String created_at;
    public String updated_at;
    public boolean isChecked;
}
