package com.newtech.jobnow.controller;

import android.os.StrictMode;

import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.CountNotificationResponse;
import com.newtech.jobnow.models.DeleteNotificationRequest;
import com.newtech.jobnow.models.JobListReponse;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.JobRequest;
import com.newtech.jobnow.models.NotificationRequest;
import com.newtech.jobnow.models.NotificationUpdateRequest;
import com.newtech.jobnow.models.PostJobRequest;
import com.newtech.jobnow.models.PostJobResponse;
import com.newtech.jobnow.models.SetNotificationResponse;
import com.newtech.jobnow.models.StatusInterviewRequest;

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

    public String UpdateNotification(NotificationUpdateRequest request){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.UpdateNotification(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                BaseResponse result = inResponseCall.execute().body();
                return result.message;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        }catch (Exception exx){
        }
        return "";
    }

    public String DeleteNotification(NotificationRequest request){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.deleteNotification(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                BaseResponse result = inResponseCall.execute().body();
                return result.message;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        }catch (Exception exx){
        }
        return "";
    }

    public String DeleteNotificationByID(DeleteNotificationRequest request){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.deleteNotificationByID(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                BaseResponse result = inResponseCall.execute().body();
                return result.message;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        }catch (Exception exx){
        }
        return "";
    }


    public String updateInterviewStatus(StatusInterviewRequest request){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.updateInterviewStatus(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                BaseResponse result = inResponseCall.execute().body();
                return result.message;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        }catch (Exception exx){
        }
        return "";
    }

    public int CountNotification(NotificationRequest request){
        try {
            retrofit.Call<CountNotificationResponse> inResponseCall = service.countNotification(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                CountNotificationResponse result = inResponseCall.execute().body();
                return result.result;

            } catch (Exception ex) {
                return 0;
            }
        }catch (Exception exx){
        }
        return 0;
    }
}
