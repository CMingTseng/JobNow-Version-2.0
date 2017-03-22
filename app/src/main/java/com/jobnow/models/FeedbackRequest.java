package com.jobnow.models;

import java.io.Serializable;

public class FeedbackRequest extends BaseRequest implements Serializable {
    public static final String PATH_URL = "api/v1/feedback/addFeedback";
    String Title;
    String Message;
    int User_id;

    public FeedbackRequest(String title, String message, int user_id) {
        super(PATH_URL);
        Title = title;
        Message = message;
        User_id = user_id;
    }
}
