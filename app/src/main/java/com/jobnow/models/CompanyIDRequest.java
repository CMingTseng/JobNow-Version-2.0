package com.jobnow.models;

import java.io.Serializable;

public class CompanyIDRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "api/v1/shortlist/deleteShortlist";

    int CategoryID;
    int UserID;

    public CompanyIDRequest(int categoryID, int userID) {
        super(PATH_URL);
        CategoryID = categoryID;
        UserID = userID;
    }
}
