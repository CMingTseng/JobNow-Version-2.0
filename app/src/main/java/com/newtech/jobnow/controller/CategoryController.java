package com.newtech.jobnow.controller;

import android.os.StrictMode;

import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.CategoryRequest;
import com.newtech.jobnow.models.CompanyIDRequest;
import com.newtech.jobnow.models.InviteRequest;
import com.newtech.jobnow.models.ShortlistDetailObject;
import com.newtech.jobnow.models.ShortlistDetailResponse;

import java.util.List;

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
