package com.jobnow.models;

import java.io.Serializable;

public class JobRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "api/v1/feedback/addFeedback";
    String ApiToken;
    int JobID;
    int UserID;

    public JobRequest(String apiToken, int jobID, int userID) {
        super(PATH_URL);
        ApiToken = apiToken;
        JobID = jobID;
        UserID = userID;
    }
}
