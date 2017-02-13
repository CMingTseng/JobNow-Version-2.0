package com.newtech.jobnow.controller;

import android.os.StrictMode;

import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.ChangePassRequest;
import com.newtech.jobnow.models.InviteRequest;

/**
 * Created by Administrator on 13/02/2017.
 */

public class InviteController {
    APICommon.JobNowService service;
    public InviteController(){
        service = MyApplication.getInstance().getJobNowService();
    }
    public String SetNewInvite(InviteRequest inviteRequest){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.setNewInvite(inviteRequest);

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
