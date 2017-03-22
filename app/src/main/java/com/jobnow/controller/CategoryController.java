package com.jobnow.controller;

import android.os.StrictMode;

import com.jobnow.acitvity.MyApplication;
import com.jobnow.common.APICommon;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.CategoryRequest;
import com.jobnow.models.CompanyIDRequest;
import com.jobnow.models.ShortlistDetailResponse;

/**
 * Created by Administrator on 13/02/2017.
 */

public class CategoryController {
    APICommon.JobNowService service;
    public CategoryController(){
        service = MyApplication.getInstance().getJobNowService();
    }
    public String AddCatgory(CategoryRequest categoryRequest){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.addCategory(categoryRequest);

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
    public String DeleteEmployeeCategory(CompanyIDRequest request){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.deleteCategory(request);

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
    public String UpCatgory(CategoryRequest categoryRequest){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.updateCategory(categoryRequest);

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

    public String DeleteCatgory(CategoryRequest categoryRequest){
        try {
            retrofit.Call<BaseResponse> inResponseCall = service.deleteCategory(categoryRequest);

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

    public void GetShortListDetail(int category_id){
        try {
            retrofit.Call<ShortlistDetailResponse> inResponseCall = service.getShortlistDetail(
                    APICommon.getSign(APICommon.getApiKey(), "api/v1/shortlist/getShortlist"),
                    APICommon.getAppId(),
                    APICommon.getDeviceType(),
                    category_id);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                ShortlistDetailResponse result = inResponseCall.execute().body();
                String ahioi="";
            } catch (Exception ex) {
                String sss= ex.getMessage();
            }
        }catch (Exception exx){
        }

    }
}
