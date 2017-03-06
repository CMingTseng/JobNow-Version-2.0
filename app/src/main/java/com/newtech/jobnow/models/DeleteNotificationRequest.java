package com.newtech.jobnow.models;

public class DeleteNotificationRequest extends BaseRequest {
    public static final String PATH_URL = "/api/v1/notification/deleteNotificationByID";
    public int NotificationID;

    public DeleteNotificationRequest(int notificationID) {
        super(PATH_URL);
        NotificationID = notificationID;
    }
}
