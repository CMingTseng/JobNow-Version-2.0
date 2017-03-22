package com.jobnow.models;

import java.io.Serializable;

public class SetInterviewRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "api/v1/interview/setInterview";

    int id;
    int JobSeekerID;
    int CompanyID;
    String Title;
    String Content;
    String InterviewDate;
    String ContactName;
    String PhoneNumber;
    int Status;
    String Start_time;
    String End_time;
    String Location;

    public SetInterviewRequest(int id, int jobSeekerID, int companyID, String title, String content, String interviewDate, String contactName, String phoneNumber, int status, String start_time, String end_time, String location) {
        super(PATH_URL);
        this.id = id;
        JobSeekerID = jobSeekerID;
        CompanyID = companyID;
        Title = title;
        Content = content;
        InterviewDate = interviewDate;
        ContactName = contactName;
        PhoneNumber = phoneNumber;
        Status = status;
        Start_time = start_time;
        End_time = end_time;
        Location = location;
    }
}
