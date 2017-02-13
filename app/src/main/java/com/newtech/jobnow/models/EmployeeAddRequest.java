package com.newtech.jobnow.models;

import java.io.Serializable;

public class EmployeeAddRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "api/v1/shortlist/addShortlist";

    int CategoryID;
    int UserID;

    public EmployeeAddRequest(int categoryID, int userID) {
        super(PATH_URL);
        CategoryID = categoryID;
        UserID = userID;
    }
}
