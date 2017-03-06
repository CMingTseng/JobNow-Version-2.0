package com.newtech.jobnow.models;

/**
 * Created by manhi on 7/9/2016.
 */
public class StatusInterviewRequest extends BaseRequest {
    public static final String PATH_URL_ADD = "api/v1/interview/updateInterviewStatus";
    public int InterviewID;
    public int Status;

    public StatusInterviewRequest(int interviewID, int status) {
        super(PATH_URL_ADD);
        InterviewID = interviewID;
        Status = status;
    }
}
