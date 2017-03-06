package com.newtech.jobnow.models;

import com.newtech.jobnow.common.APICommon;

/**
 * Created by manhi on 7/9/2016.
 */
public class DeleteInterviewRequest extends BaseRequest {
    public static final String PATH_URL_ADD = "api/v1/interview/deleteInterview";
    public int InterviewID;
    public int IsCompany;

    public DeleteInterviewRequest(int interviewID, int isCompany) {
        super(PATH_URL_ADD);
        InterviewID = interviewID;
        IsCompany=isCompany;
    }

    public DeleteInterviewRequest(int interviewID) {
        super(PATH_URL_ADD);
        InterviewID = interviewID;
    }
}
