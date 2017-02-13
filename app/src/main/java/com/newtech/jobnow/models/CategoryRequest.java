package com.newtech.jobnow.models;

import java.io.Serializable;
public class CategoryRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "api/v1/category/addCategory";
    String Name;
    int CompanyID;
    int CategoryID;
    public CategoryRequest(String name, int companyID,int categoryID) {
        super(PATH_URL);
        Name = name;
        CompanyID = companyID;
        CategoryID=categoryID;
    }
}
