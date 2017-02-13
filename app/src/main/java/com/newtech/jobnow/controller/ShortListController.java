package com.newtech.jobnow.controller;

import android.os.StrictMode;

import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.EmployeeAddRequest;
import com.newtech.jobnow.models.FeedbackRequest;

/**
 * Created by Administrator on 13/02/2017.
 */

public class ShortListController {
    APICommon.JobNowService service;
    public ShortListController(){
        service = MyApplication.getInstance().getJobNowService();
    }
    public String AddShortlist(EmployeeAddRequest request){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.addShortlist(request);

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
