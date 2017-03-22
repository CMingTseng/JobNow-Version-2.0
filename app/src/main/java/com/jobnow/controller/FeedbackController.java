package com.jobnow.controller;

import android.os.StrictMode;

import com.jobnow.acitvity.MyApplication;
import com.jobnow.common.APICommon;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.FeedbackRequest;

/**
 * Created by Administrator on 13/02/2017.
 */

public class FeedbackController {
    APICommon.JobNowService service;
    public FeedbackController(){
        service = MyApplication.getInstance().getJobNowService();
    }
    public String AddNewFeedback(FeedbackRequest feedbackRequest){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.addNewFeedback(feedbackRequest);

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
}
