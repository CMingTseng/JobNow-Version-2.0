package com.jobnow.models;

public class NotificationRequest extends BaseRequest {
    public static final String PATH_URL = "/api/v1/notification/setNotification";
    public static final String PATH_URL_DELETE = "/api/v1/notification/deleteNotification";
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

    public NotificationRequest(int jobSeekerID, int companyID) {
        super(PATH_URL_DELETE);
        JobSeekerID = jobSeekerID;
        CompanyID = companyID;
    }
}
