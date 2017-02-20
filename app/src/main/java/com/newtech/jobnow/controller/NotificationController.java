package com.newtech.jobnow.controller;

import android.os.StrictMode;

import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.JobListReponse;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.JobRequest;
import com.newtech.jobnow.models.NotificationRequest;
import com.newtech.jobnow.models.PostJobRequest;
import com.newtech.jobnow.models.PostJobResponse;
import com.newtech.jobnow.models.SetNotificationResponse;

/**
 * Created by Administrator on 11/02/2017.
 */

public class NotificationController {
    APICommon.JobNowService service;

    public NotificationController() {
        service = MyApplication.getInstance().getJobNowService();
    }


    public String SetNotification(NotificationRequest request){
        try {
            retrofit.Call<SetNotificationResponse> inResponseCall = service.SetNotification(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                SetNotificationResponse result = inResponseCall.execute().body();
                return result.result;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        }catch (Exception exx){
        }
        return "";
    }
}
