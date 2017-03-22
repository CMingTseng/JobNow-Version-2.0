package com.jobnow.controller;

import android.os.StrictMode;

import com.google.gson.Gson;
import com.jobnow.acitvity.MyApplication;
import com.jobnow.common.APICommon;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.ChangePassRequest;
import com.jobnow.models.ForgotRequest;
import com.jobnow.models.JobListRequest;
import com.jobnow.models.LoginRequest;
import com.jobnow.models.LoginResponse;
import com.jobnow.models.ProfileModel;
import com.jobnow.models.ProfileRequest;
import com.jobnow.models.ProfileResponse;
import com.jobnow.models.RegisterManagerRequest;
import com.jobnow.models.SendPricingRequest;
import com.jobnow.models.TokenRequest;
import com.jobnow.models.UploadFileResponse;

/**
 * Created by Administrator on 11/02/2017.
 */

public class UserController {
    APICommon.JobNowService service;

    public UserController() {
        service = MyApplication.getInstance().getJobNowService();
    }

    public LoginResponse CheckLogin(LoginRequest loginRequest) {
        try {
            retrofit.Call<LoginResponse> loginResponseCall = service.loginUserV2(loginRequest);

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                LoginResponse result = loginResponseCall.execute().body();
                return result;
            } catch (Exception ex) {
                String ss = ex.toString();
            }
        } catch (Exception exx) {
        }
        return null;
    }

    public LoginResponse RegisterManager(RegisterManagerRequest loginRequest) {
        try {
            retrofit.Call<LoginResponse> loginResponseCall = service.registerForManager(loginRequest);
            Gson gson = new Gson();
            String json = gson.toJson(loginRequest).toString();
            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                LoginResponse result = loginResponseCall.execute().body();
                return result;

            } catch (Exception ex) {
                String ss = ex.toString();

            }
        } catch (Exception exx) {
        }
        return null;
    }

    public String ForgotPass(ForgotRequest loginRequest) {
        try {
            retrofit.Call<BaseResponse> loginResponseCall = service.forgotPass(loginRequest);
            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                BaseResponse result = loginResponseCall.execute().body();
                if (result.code == 200) {
                    return "";
                } else {
                    return result.message;
                }
            } catch (Exception ex) {
                String ss = ex.toString();
                return ss;
            }
        } catch (Exception exx) {
        }
        return "";
    }


    public String GetToken(TokenRequest loginRequest) {
        try {
            retrofit.Call<LoginResponse> loginResponseCall = service.getToken(loginRequest);
            Gson gson = new Gson();
            String json = gson.toJson(loginRequest).toString();
            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
                LoginResponse results = loginResponseCall.execute().body();
                if (results.code == 200) {
                    return results.result.apiToken;
                } else {
                    return "";
                }
            } catch (Exception ex) {
                String ss = ex.toString();
                return ss;
            }
        } catch (Exception exx) {
        }
        return "";
    }

    public ProfileModel GetProfileCompany(int userID, String token, int companyID) {
        try {
            retrofit.Call<ProfileResponse> loginResponseCall = service.getCompanyProfile(
                    APICommon.getSign(APICommon.getApiKey(), JobListRequest.PATH_URL),
                    APICommon.getAppId(),
                    APICommon.getDeviceType(),
                    userID, token, companyID
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
        } catch (Exception exx) {
        }
        return null;
    }

    public String UpdateProfileCompany(ProfileRequest profileRequest) {
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
        } catch (Exception exx) {
        }
        return "";
    }

    public String ChangePassword(ChangePassRequest changePassRequest) {
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
        } catch (Exception exx) {
        }
        return "";
    }

    public String SendPricing(SendPricingRequest request) {
        try {
            retrofit.Call<BaseResponse> loginResponseCall = service.SendPricing(request);

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
