package com.newtech.jobnow.controller;

import android.os.StrictMode;

import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.CategoryRequest;
import com.newtech.jobnow.models.DeleteInterviewRequest;
import com.newtech.jobnow.models.SendPricingRequest;
import com.newtech.jobnow.models.SetInterviewRequest;
import com.newtech.jobnow.models.ShortlistDetailResponse;

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
