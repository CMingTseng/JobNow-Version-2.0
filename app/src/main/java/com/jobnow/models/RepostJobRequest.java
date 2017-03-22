package com.jobnow.models;

import java.io.Serializable;

/**
 * Created by SANG on 8/21/2016.
 */
public class RepostJobRequest extends BaseRequest implements Serializable {

    private static final String PATH_URL = "/api/v1/jobs/extendJob";

    int JobID;
    int UserID;
    String ApiToken;

    public RepostJobRequest(int jobID, int userID, String apiToken) {
        super(PATH_URL);
        JobID = jobID;
        UserID = userID;
        ApiToken = apiToken;
    }
}
