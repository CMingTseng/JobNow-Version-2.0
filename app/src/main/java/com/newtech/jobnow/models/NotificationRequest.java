package com.newtech.jobnow.models;

import com.newtech.jobnow.common.APICommon;

public class NotificationRequest extends BaseRequest {
    public static final String PATH_URL = "/api/v1/notification/setNotification";
    public int CompanyID;
    public int JobSeekerID;
    public int JobID;
    public String Title;
    public String Content;
    public int KeyScreen;
    public int Status;

    public NotificationRequest(int companyID, int jobSeekerID, int jobID, String title, String content, int keyScreen, int status) {
        super(PATH_URL);
        CompanyID = companyID;
        JobSeekerID = jobSeekerID;
        JobID = jobID;
        Title = title;
        Content = content;
        KeyScreen = keyScreen;
        Status = status;
    }
}
