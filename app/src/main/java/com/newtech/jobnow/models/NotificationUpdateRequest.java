package com.newtech.jobnow.models;

/**
 * Created by manhi on 7/9/2016.
 */
public class NotificationUpdateRequest extends BaseRequest {
    public static final String PATH_URL_ADD = "api/v1/notification/updateNotificationStatus";
    public int NotifiID;

    public NotificationUpdateRequest(int notifiID) {
        super(PATH_URL_ADD);
        NotifiID = notifiID;
    }
}
