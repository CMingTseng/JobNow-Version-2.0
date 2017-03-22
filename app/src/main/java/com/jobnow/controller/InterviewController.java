package com.jobnow.controller;

import android.os.StrictMode;

import com.jobnow.acitvity.MyApplication;
import com.jobnow.common.APICommon;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.DeleteInterviewRequest;
import com.jobnow.models.SetInterviewRequest;

/**
 * Created by Administrator on 13/02/2017.
 */

public class InterviewController {
    APICommon.JobNowService service;
    public InterviewController(){
        service = MyApplication.getInstance().getJobNowService();
    }
    public String SetInterviewTime(SetInterviewRequest request){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.SetInterviewTime(request);

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

    public String DeleteInterview(DeleteInterviewRequest request) {
        try {
            retrofit.Call<BaseResponse> loginResponseCall = service.DeleteInterview(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                BaseResponse result = loginResponseCall.execute().body();
                return result.message;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        } catch (Exception exx) {
        }
        return "";
    }

    public String RejectInterview(DeleteInterviewRequest request) {
        try {
            retrofit.Call<BaseResponse> loginResponseCall = service.RejectInterview(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                BaseResponse result = loginResponseCall.execute().body();
                return result.message;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        } catch (Exception exx) {
        }
        return "";
    }


}
