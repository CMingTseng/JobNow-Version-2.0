package com.newtech.jobnow.controller;

import android.os.StrictMode;

import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.CategoryRequest;
import com.newtech.jobnow.models.FeedbackRequest;
import com.newtech.jobnow.models.ShortlistDetailResponse;

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
