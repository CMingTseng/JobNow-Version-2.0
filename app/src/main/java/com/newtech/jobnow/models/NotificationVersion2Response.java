package com.newtech.jobnow.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sang on 9/28/2016.
 */
public class NotificationVersion2Response extends BaseResponse implements Serializable {
    public NotificationVersion2Result result;

    public static class NotificationVersion2Result {
        public Integer total;
        public Integer per_page;
        public Integer current_page;
        public Integer last_page;
        public String next_page_url;
        public String prev_page_url;
        public String from;
        public String to;
        public List<NotificationVersion2Object> data;
    }
}
