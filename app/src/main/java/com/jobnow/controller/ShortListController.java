package com.jobnow.controller;

import android.os.StrictMode;

import com.jobnow.acitvity.MyApplication;
import com.jobnow.common.APICommon;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.EmployeeAddRequest;

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
