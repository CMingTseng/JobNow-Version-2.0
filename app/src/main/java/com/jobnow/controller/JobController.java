package com.jobnow.controller;

import android.os.StrictMode;

import com.jobnow.acitvity.MyApplication;
import com.jobnow.common.APICommon;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.JobListReponse;
import com.jobnow.models.JobListRequest;
import com.jobnow.models.JobRequest;
import com.jobnow.models.PostJobRequest;
import com.jobnow.models.PostJobResponse;
import com.jobnow.models.RepostJobRequest;

/**
 * Created by Administrator on 11/02/2017.
 */

public class JobController {
    APICommon.JobNowService service;

    public JobController() {
        service = MyApplication.getInstance().getJobNowService();
    }

    public JobListReponse.JobListResult GetListJob(int page, int type, int companyID) {
        try {
            retrofit.Call<JobListReponse> jobListReponseCall = service.getJobListByParamV2(
                    APICommon.getSign(APICommon.getApiKey(), JobListRequest.PATH_URL),
                    APICommon.getAppId(),
                    APICommon.getDeviceType(),
                    page,
                    "ASC",
                    "",
                    "",
                    "",
                    0,
                    0, 0,
                    0,
                    1,
                    type,
                    companyID,-1
                    );

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                JobListReponse result = jobListReponseCall.execute().body();
                if (result.code == 200) {
                    return result.result;
                }
            } catch (Exception ex) {
                String ss = ex.toString();
            }
        } catch (Exception exx) {
        }
        return null;
    }

    public String DeleteJob(JobRequest request){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.deleteJob(request);

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

    public PostJobResponse PostAJobs(PostJobRequest request){
        try {
            retrofit.Call<PostJobResponse> inResponseCall = service.PostAJobs(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                PostJobResponse result = inResponseCall.execute().body();
                return result;

            } catch (Exception ex) {
                String sss=ex.getMessage();
            }
        }catch (Exception exx){
        }
        return null;
    }

    public PostJobResponse extendJob(RepostJobRequest request){
        try {
            retrofit.Call<PostJobResponse> inResponseCall = service.extendJob(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                PostJobResponse result = inResponseCall.execute().body();
                return result;

            } catch (Exception ex) {
                String sss=ex.getMessage();
            }
        }catch (Exception exx){
        }
        return null;
    }
    public String EditPostAJobs(PostJobRequest request){
        try {
            retrofit.Call<PostJobResponse> inResponseCall = service.EditPostAJobs(request);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                PostJobResponse result = inResponseCall.execute().body();
                return result.result;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        }catch (Exception exx){
        }
        return "";
    }
}
