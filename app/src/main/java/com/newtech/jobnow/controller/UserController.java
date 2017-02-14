package com.newtech.jobnow.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.google.gson.Gson;
import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.ChangePassRequest;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.LoginRequest;
import com.newtech.jobnow.models.LoginResponse;
import com.newtech.jobnow.models.ProfileModel;
import com.newtech.jobnow.models.ProfileRequest;
import com.newtech.jobnow.models.ProfileResponse;
import com.newtech.jobnow.models.RegisterManagerRequest;
import com.newtech.jobnow.models.RegisterResponse;
import com.newtech.jobnow.models.TokenRequest;
import com.newtech.jobnow.models.UploadFileResponse;
import com.newtech.jobnow.models.UserModel;

import java.util.Hashtable;

import retrofit2.Call;

/**
 * Created by Administrator on 11/02/2017.
 */

public class UserController {
    APICommon.JobNowService service;
    public UserController(){
        service = MyApplication.getInstance().getJobNowService();
    }

    public UserModel CheckLogin(LoginRequest loginRequest){
        try {
            retrofit.Call<LoginResponse> loginResponseCall = service.loginUserV2(loginRequest);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                LoginResponse result = loginResponseCall.execute().body();
                if (result.code == 200) {
                    return result.result;
                }
            } catch (Exception ex) {
                String ss = ex.toString();
            }
        }catch (Exception exx){
        }
        return null;
    }
    public String RegisterManager(RegisterManagerRequest loginRequest){
        try {
            retrofit.Call<RegisterResponse> loginResponseCall = service.registerForManager(loginRequest);
            Gson gson= new Gson();
            String json=gson.toJson(loginRequest).toString();
            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                RegisterResponse result = loginResponseCall.execute().body();
                if (result.code == 200) {
                    return "";
                }else{
                    return result.message;
                }
            } catch (Exception ex) {
                String ss = ex.toString();
                return ss;
            }
        }catch (Exception exx){
        }
        return "";
    }

    public String GetToken(TokenRequest loginRequest){
        try {
            retrofit.Call<LoginResponse> loginResponseCall = service.getToken(loginRequest);
            Gson gson= new Gson();
            String json=gson.toJson(loginRequest).toString();
            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                LoginResponse results = loginResponseCall.execute().body();
                if (results.code == 200) {
                    return results.result.apiToken;
                }else{
                    return "";
                }
            } catch (Exception ex) {
                String ss = ex.toString();
                return ss;
            }
        }catch (Exception exx){
        }
        return "";
    }

    public ProfileModel GetProfileCompany(int userID,String token,int companyID){
        try {
            retrofit.Call<ProfileResponse> loginResponseCall = service.getCompanyProfile(
                    APICommon.getSign(APICommon.getApiKey(), JobListRequest.PATH_URL),
                    APICommon.getAppId(),
                    APICommon.getDeviceType(),
                    userID,token,companyID
            );

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                ProfileResponse result = loginResponseCall.execute().body();
                if (result.code == 200) {
                    return result.result;
                }
            } catch (Exception ex) {
                String ss = ex.toString();
            }
        }catch (Exception exx){
        }
        return null;
    }

    public String UpdateProfileCompany(ProfileRequest profileRequest){
        try {
            retrofit.Call<UploadFileResponse> loginResponseCall = service.updateProfile(profileRequest);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                UploadFileResponse result = loginResponseCall.execute().body();
                return result.message;

            } catch (Exception ex) {
                return ex.getMessage();
            }
        }catch (Exception exx){
        }
        return "";
    }

    public String ChangePassword(ChangePassRequest changePassRequest){
        try {
            retrofit.Call<BaseResponse> loginResponseCall = service.changePassword(changePassRequest);

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
        }catch (Exception exx){
        }
        return "";
    }
}
