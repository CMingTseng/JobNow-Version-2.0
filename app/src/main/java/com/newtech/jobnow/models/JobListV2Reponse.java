package com.newtech.jobnow.models;


import java.util.List;

public class JobListV2Reponse extends BaseResponse {
    public JobListV2Result result;

    public static class JobListV2Result {
        public Integer total;
        public Integer per_page;
        public Integer current_page;
        public Integer last_page;
        public String next_page_url;
        public String prev_page_url;
        public String from;
        public String to;
        public List<JobV2Object> data;
    }
}
