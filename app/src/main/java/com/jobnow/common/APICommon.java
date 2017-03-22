package com.jobnow.common;

import android.util.Log;

import com.jobnow.models.AnInterviewResponse;
import com.jobnow.models.ApplyJobRequest;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.CategoryIndustryResponse;
import com.jobnow.models.CategoryRequest;
import com.jobnow.models.CategoryResponse;
import com.jobnow.models.ChangePassRequest;
import com.jobnow.models.CompanyIDRequest;
import com.jobnow.models.CompanyProfileResponse;
import com.jobnow.models.CountJobResponse;
import com.jobnow.models.CountNotificationResponse;
import com.jobnow.models.CreditsNumberResponse;
import com.jobnow.models.DeleteInterviewRequest;
import com.jobnow.models.DeleteJobRequest;
import com.jobnow.models.DeleteNotificationRequest;
import com.jobnow.models.DetailJobResponse;
import com.jobnow.models.EmployeeAddRequest;
import com.jobnow.models.EmployeeResponse;
import com.jobnow.models.ExperienceRequest;
import com.jobnow.models.ExperienceResponse;
import com.jobnow.models.FeedbackRequest;
import com.jobnow.models.ForgotRequest;
import com.jobnow.models.IndustryResponse;
import com.jobnow.models.InterviewResponse;
import com.jobnow.models.InviteRequest;
import com.jobnow.models.InviteResponse;
import com.jobnow.models.JobListReponse;
import com.jobnow.models.JobListV2Reponse;
import com.jobnow.models.JobLocationResponse;
import com.jobnow.models.JobRequest;
import com.jobnow.models.LevelResponse;
import com.jobnow.models.ListEmploymentResponse;
import com.jobnow.models.ListExperienceResponse;
import com.jobnow.models.LocationResponse;
import com.jobnow.models.LoginRequest;
import com.jobnow.models.LoginResponse;
import com.jobnow.models.MapJobListReponse;
import com.jobnow.models.NotificationRequest;
import com.jobnow.models.NotificationResponse;
import com.jobnow.models.NotificationUpdateRequest;
import com.jobnow.models.NotificationVersion2Response;
import com.jobnow.models.PostJobRequest;
import com.jobnow.models.PostJobResponse;
import com.jobnow.models.ProfileRequest;
import com.jobnow.models.ProfileResponse;
import com.jobnow.models.RegisterFBReponse;
import com.jobnow.models.RegisterFBRequest;
import com.jobnow.models.RegisterManagerRequest;
import com.jobnow.models.RegisterRequest;
import com.jobnow.models.RegisterResponse;
import com.jobnow.models.RepostJobRequest;
import com.jobnow.models.SaveJobRequest;
import com.jobnow.models.SendPricingRequest;
import com.jobnow.models.SetInterviewRequest;
import com.jobnow.models.SetNotificationResponse;
import com.jobnow.models.ShortlistDetailResponse;
import com.jobnow.models.SkillRequest;
import com.jobnow.models.SkillResponse;
import com.jobnow.models.StatusInterviewRequest;
import com.jobnow.models.TermResponse;
import com.jobnow.models.TokenRequest;
import com.jobnow.models.UpdateProfileRequest;
import com.jobnow.models.UploadFileResponse;
import com.jobnow.models.UserDetailResponse;
import com.squareup.okhttp.RequestBody;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Url;

/**
 * Created by SANG on 8/21/2016.
 */
public class APICommon {
    public static final String BASE_URL = "http://jobnow.com.sg/api/v1/";
    public static final int ANDROID = 4;
    public static final int IOS = 3;
    public static final int WEB = 2;
    private static final String TAG = APICommon.class.getSimpleName();

    public static String getApiKey() {
        return "tD0EudnC92D198TR";
    }

    public static String getAppId() {
        return "com.jobnow.demo";
    }

    public static int getDeviceType() {
        return ANDROID;
    }

    public static String getSign(String api_key, String path_url) {
        try {
            long time = System.currentTimeMillis();
            StringBuilder builder = new StringBuilder();
            builder.append(time);
            builder.append(".");
            String md5_hash = FunctionCommon.hashString(path_url + ":" + time + ":" + api_key);
            builder.append(md5_hash);
            return builder.toString();
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
            return null;
        }
    }

    public interface JobNowService {

        @POST("users/getToken")
        Call<LoginResponse> getToken(@Body TokenRequest request);

        @GET("notification/getListNotification/{sign}/{app_id}/{device_type}/{user_id}/{api_token}")
        Call<NotificationResponse> getListNotification(@Path("sign") String sign,
                                                       @Path("app_id") String app_id,
                                                       @Path("device_type") int device_type,
                                                       @Path("user_id") int user_id,
                                                       @Path("api_token") String api_token);

        @GET("jobs/getCountJob/{sign}/{app_id}/{device_type}/{location_id}")
        Call<CountJobResponse> getCountJob(@Path("sign") String sign,
                                           @Path("app_id") String app_id,
                                           @Path("device_type") int device_type,
                                           @Path("location_id") int location_id);

        @GET("jobs/getListJobInLocation/{sign}/{app_id}/{device_type}/{lat}/{lng}")
        Call<MapJobListReponse> getListJobInLocation(@Path("sign") String sign,
                                                     @Path("app_id") String app_id,
                                                     @Path("device_type") int device_type,
                                                     @Path("lat") Double lat,
                                                     @Path("lng") Double lng);

        @POST("jobs/postDeleteSaveJob")
        Call<BaseResponse> deleteSaveJob(@Body DeleteJobRequest request);

        @POST("jobs/postDeleteAppliedJob")
        Call<BaseResponse> deleteAppliedJob(@Body DeleteJobRequest request);

        @POST("jobs/postSaveJob")
        Call<BaseResponse> saveJob(@Body SaveJobRequest request);

        @POST("jobs/postAppliedJob")
        Call<BaseResponse> applyJob(@Body ApplyJobRequest request);

        @GET("jobs/getAppliedJob/{sign}/{app_id}/{device_type}/{user_id}/{ApiToken}")
        Call<JobListV2Reponse> getAppliedListJob(@Path("sign") String sign,
                                                 @Path("app_id") String app_id,
                                                 @Path("device_type") int device_type,
                                                 @Path("user_id") int user_id,
                                                 @Path("ApiToken") String apiToken,
                                                 @Query("page") Integer page);

        @GET("jobs/getSaveJob/{sign}/{app_id}/{device_type}/{user_id}/{ApiToken}")
        Call<JobListReponse> getSaveListJob(@Path("sign") String sign,
                                            @Path("app_id") String app_id,
                                            @Path("device_type") int device_type,
                                            @Path("user_id") int user_id,
                                            @Path("ApiToken") String apiToken,
                                            @Query("page") Integer page);

        @GET("jobs/getSaveJob/{sign}/{app_id}/{device_type}/{user_id}/{ApiToken}")
        Call<JobListV2Reponse> getSaveListJobV2(@Path("sign") String sign,
                                                @Path("app_id") String app_id,
                                                @Path("device_type") int device_type,
                                                @Path("user_id") int user_id,
                                                @Path("ApiToken") String apiToken,
                                                @Query("page") Integer page);

        @POST("users/postLogin")
        Call<LoginResponse> loginUser(@Body LoginRequest request);

        @POST("users/postRegister")
        Call<RegisterResponse> registerUser(@Body RegisterRequest request);

        @POST("users/postRegisterSocialite")
        Call<RegisterFBReponse> registerFB(@Body RegisterFBRequest request);

        @GET("jobs/getListJob/{sign}/{app_id}/{device_type}/")
        Call<JobListReponse> getJobListByParam(@Path("sign") String sign,
                                               @Path("app_id") String app_id,
                                               @Path("device_type") int device_type,
                                               @Query("page") Integer page,
                                               @Query("Order") String Order,
                                               @Query("Title") String Title,
                                               @Query("Location") String Location,
                                               @Query("Skill") String Skill,
                                               @Query("MinSalary") Integer MinSalary,
                                               @Query("FromSalary") Integer FromSalary,
                                               @Query("ToSalary") Integer ToSalary,
                                               @Query("IndustryID") Integer industryID,
                                               @Query("IsParttime") Integer isParttime);


        @GET()
        Call<JobListReponse> getJobList(@Url String url);

        @GET()
        Call<IndustryResponse> getIndustry(@Url String url);

        @GET
        Call<JobLocationResponse> getJobLocation(@Url String url);

        @GET
        Call<SkillResponse> getSkill(@Url String url);

        @Multipart
        @POST("users/postAvatarUploadFile")
        Call<UploadFileResponse> postuploadAvatar(@Part("sign") RequestBody sign,
                                                  @Part("app_id") RequestBody app_id,
                                                  @Part("device_type") RequestBody device_type,
                                                  @Part("ApiToken") RequestBody ApiToken,
                                                  @Part("UserID") RequestBody userid,
                                                  @Part("Files\"; filename=\"avatar.jpg\"") RequestBody file);

        @GET("users/getUserDetail/{sign}/{app_id}/{device_type}/{user_id}/{token}")
        Call<UserDetailResponse> getUserDetail(@Path("sign") String sign,
                                               @Path("app_id") String app_id,
                                               @Path("device_type") int device_type,
                                               @Path("user_id") int user_id,
                                               @Path("token") String token,
                                               @Query("user_id") int user_id1);

        @POST("users/postUpdateJobSeeker")
        Call<BaseResponse> postUpdateDetail(@Body UpdateProfileRequest updateProfileRequest);

        @GET("jobseekerexperience/getAllJobSeekerExperience/{sign}/{app_id}/{device_type}/{user_id}/{token}")
        Call<ExperienceResponse> getExperience(@Path("sign") String sign,
                                               @Path("app_id") String app_id,
                                               @Path("device_type") int device_type,
                                               @Path("user_id") int user_id,
                                               @Path("token") String token,
                                               @Query("user_id") int user_id1);

        @POST("jobseekerexperience/postAddJobSeekerExperience")
        Call<BaseResponse> postAddJobSeekerExperience(@Body ExperienceRequest addExperienceRequest);

        @POST("jobseekerexperience/postDeleteJobSeekerExperience")
        Call<BaseResponse> postDeleteJobSeekerExperience(@Body ExperienceRequest addExperienceRequest);

        @POST("jobseekerexperience/postUpdateJobSeekerExperience")
        Call<BaseResponse> postUpdateJobSeekerExperience(@Body ExperienceRequest addExperienceRequest);

        @GET("skill/getListSkill/{sign}/{app_id}/{device_type}/{user_id}")
        Call<SkillResponse> getSkill(@Path("sign") String sign,
                                     @Path("app_id") String app_id,
                                     @Path("device_type") int device_type,
                                     @Path("user_id") int user_id);

        @POST("skill/postEditSkill")
        Call<BaseResponse> postEditSkill(@Body SkillRequest skillRequest);

        @GET("jobs/getJobDetail/{sign}/{app_id}/{device_type}/{user_id}/{job_id}")
        Call<DetailJobResponse> getDetailJob(@Path("sign") String sign,
                                             @Path("app_id") String app_id,
                                             @Path("device_type") int device_type,
                                             @Path("user_id") int user_id,
                                             @Path("job_id") int job_id);

        @GET("users/getLogout/{sign}/{app_id}/{device_type}/{user_id}/{ApiToken}")
        Call<BaseResponse> getLogout(@Path("sign") String sign,
                                     @Path("app_id") String app_id,
                                     @Path("device_type") int device_type,
                                     @Path("user_id") int user_id,
                                     @Path("ApiToken") String token);

        // API Version 2 make by Redix Team
        @POST("users/postLogin")
        Call<LoginResponse> loginUserV2(@Body LoginRequest request);

        @POST("users/postRegisterEmployee")
        Call<LoginResponse> registerForManager(@Body RegisterManagerRequest request);

        @GET("jobs/getListJob/{sign}/{app_id}/{device_type}/")
        Call<JobListReponse> getJobListByParamV2(@Path("sign") String sign,
                                                 @Path("app_id") String app_id,
                                                 @Path("device_type") int device_type,
                                                 @Query("page") Integer page,
                                                 @Query("Order") String Order,
                                                 @Query("Title") String Title,
                                                 @Query("Location") String Location,
                                                 @Query("Skill") String Skill,
                                                 @Query("MinSalary") Integer MinSalary,
                                                 @Query("FromSalary") Integer FromSalary,
                                                 @Query("ToSalary") Integer ToSalary,
                                                 @Query("IndustryID") Integer industryID,
                                                 @Query("isEmployee") Integer isEmployee,
                                                 @Query("isHiring") Integer isHiring,
                                                 @Query("CompanyID") Integer CompanyID,
                                                 @Query("IsParttime") Integer isParttime

        );

        @GET("companyprofile/getCompanyProfile/{sign}/{app_id}/{device_type}/{user_id}/{token}/")
        Call<ProfileResponse> getCompanyProfile(@Path("sign") String sign,
                                                @Path("app_id") String app_id,
                                                @Path("device_type") int device_type,
                                                @Path("user_id") int user_id,
                                                @Path("token") String token,
                                                @Query("CompanyID") Integer CompanyID
        );

        @Multipart
        @POST("companyprofile/postCompanyUploadFile")
        Call<UploadFileResponse> uploadImageProfile(@Part("sign") RequestBody sign,
                                                    @Part("app_id") RequestBody app_id,
                                                    @Part("device_type") RequestBody device_type,
                                                    @Part("ApiToken") RequestBody ApiToken,
                                                    @Part("UserID") RequestBody userid,
                                                    @Part("Files\"; filename=\"avatar.jpg\"") RequestBody file);

        @POST("companyprofile/postUpdateCompany")
        Call<UploadFileResponse> updateProfile(@Body ProfileRequest request);

        @POST("users/changePassword")
        Call<BaseResponse> changePassword(@Body ChangePassRequest request);

        @GET("invite/getListInvitation/{sign}/{app_id}/{device_type}/")
        Call<InviteResponse> getListInvitation(@Path("sign") String sign,
                                               @Path("app_id") String app_id,
                                               @Path("device_type") int device_type,
                                               @Query("User_id") int user_id

        );

        @POST("invite/setInvite")
        Call<BaseResponse> setNewInvite(@Body InviteRequest request);

        @GET("category/getListCategory/{sign}/{app_id}/{device_type}/")
        Call<CategoryResponse> getListCategory(@Path("sign") String sign,
                                               @Path("app_id") String app_id,
                                               @Path("device_type") int device_type,
                                               @Query("CompanyID") int companyID

        );

        @POST("category/addCategory")
        Call<BaseResponse> addCategory(@Body CategoryRequest request);

        @GET("shortlist/getShortlist/{sign}/{app_id}/{device_type}/")
        Call<ShortlistDetailResponse> getShortlistDetail(@Path("sign") String sign,
                                                         @Path("app_id") String app_id,
                                                         @Path("device_type") int device_type,
                                                         @Query("CategoryID") int user_id

        );

        @GET("users/getListEmployee/{sign}/{app_id}/{device_type}/")
        Call<EmployeeResponse> getEmployee(@Path("sign") String sign,
                                           @Path("app_id") String app_id,
                                           @Path("device_type") int device_type,
                                           @Query("Name") String name,
                                           @Query("CategoryID") int category_id,
                                           @Query("CompanyID") int company_id

        );

        @POST("category/updateCategory")
        Call<BaseResponse> updateCategory(@Body CategoryRequest request);

        @POST("category/deleteCategory")
        Call<BaseResponse> deleteCategory(@Body CategoryRequest request);

        @POST("feedback/addFeedback")
        Call<BaseResponse> addNewFeedback(@Body FeedbackRequest request);

        @POST("jobs/postDeleteJob")
        Call<BaseResponse> deleteJob(@Body JobRequest request);

        @POST("shortlist/deleteShortlist")
        Call<BaseResponse> deleteCategory(@Body CompanyIDRequest request);

        @POST("interview/setInterview")
        Call<BaseResponse> SetInterviewTime(@Body SetInterviewRequest request);

        @GET("interview/getListInterView/{sign}/{app_id}/{device_type}/")
        Call<InterviewResponse> getInterview(@Path("sign") String sign,
                                             @Path("app_id") String app_id,
                                             @Path("device_type") int device_type,
                                             @Query("CompanyID") int company_id,
                                             @Query("JobSeekerID") int jobSeekerID

        );

        @POST("shortlist/addShortlist")
        Call<BaseResponse> addShortlist(@Body EmployeeAddRequest request);

        @POST("users/postForgot")
        Call<BaseResponse> forgotPass(@Body ForgotRequest request);

        @GET()
        Call<LevelResponse> getListLevel(@Url String url);

        @GET()
        Call<CategoryIndustryResponse> getListCategoryIndustry(@Url String url);

        @POST("jobs/postCreateJob")
        Call<PostJobResponse> PostAJobs(@Body PostJobRequest request);

        @POST("jobs/postEditJob")
        Call<PostJobResponse> EditPostAJobs(@Body PostJobRequest request);

        @GET()
        Call<ListExperienceResponse> getListExperience(@Url String url);

        @GET()
        Call<LocationResponse> getListLocation(@Url String url);

        @POST("notification/setNotification")
        Call<SetNotificationResponse> SetNotification(@Body NotificationRequest request);

        @GET("notification/getListNotification/{sign}/{app_id}/{device_type}/")
        Call<NotificationVersion2Response> getListNotification(@Path("sign") String sign,
                                                               @Path("app_id") String app_id,
                                                               @Path("device_type") int device_type,
                                                               @Query("CompanyID") int company_id,
                                                               @Query("JobSeekerID") int jobSeekerId,
                                                               @Query("page") int page
        );

        @POST("users/sendPricing")
        Call<BaseResponse> SendPricing(@Body SendPricingRequest request);

        @GET()
        Call<TermResponse> getTermsofUse(@Url String url);

        @GET("users/getUserProfile/{sign}/{app_id}/{device_type}/")
        Call<UserDetailResponse> getUserProfile(@Path("sign") String sign,
                                                @Path("app_id") String app_id,
                                                @Path("device_type") int device_type,
                                                @Query("user_id") int user_id
        );

        @GET("users/getCompanyProfile/{sign}/{app_id}/{device_type}/")
        Call<CompanyProfileResponse> getCompanyProfile(@Path("sign") String sign,
                                                       @Path("app_id") String app_id,
                                                       @Path("device_type") int device_type,
                                                       @Query("CompanyID") int companyID
        );


        @GET("jobseekerexperience/getAllUserExperience/{sign}/{app_id}/{device_type}/")
        Call<ExperienceResponse> getUserExperience(@Path("sign") String sign,
                                                   @Path("app_id") String app_id,
                                                   @Path("device_type") int device_type,
                                                   @Query("user_id") int user_id
        );

        @GET()
        Call<ListEmploymentResponse> getListEmployment(@Url String url);

        @POST("interview/deleteInterview")
        Call<BaseResponse> DeleteInterview(@Body DeleteInterviewRequest request);

        @POST("interview/rejectInterview")
        Call<BaseResponse> RejectInterview(@Body DeleteInterviewRequest request);



        @POST("notification/updateNotificationStatus")
        Call<BaseResponse> UpdateNotification(@Body NotificationUpdateRequest request);


        @GET("interview/getAnInterviewDetail/{sign}/{app_id}/{device_type}/")
        Call<AnInterviewResponse> getAnInterview(@Path("sign") String sign,
                                                 @Path("app_id") String app_id,
                                                 @Path("device_type") int device_type,
                                                 @Query("InterviewID") int interviewID

        );

        @GET("users/getCreditNumber/{sign}/{app_id}/{device_type}/")
        Call<CreditsNumberResponse> getCredits(@Path("sign") String sign,
                                               @Path("app_id") String app_id,
                                               @Path("device_type") int device_type,
                                               @Query("CompanyID") int companyID
        );

        @POST("notification/deleteNotification")
        Call<BaseResponse> deleteNotification(@Body NotificationRequest request);

        @POST("notification/countAllNotification")
        Call<CountNotificationResponse> countNotification(@Body NotificationRequest request);


        @POST("notification/deleteNotificationByID")
        Call<BaseResponse> deleteNotificationByID(@Body DeleteNotificationRequest request);

        @POST("interview/updateInterviewStatus")
        Call<BaseResponse> updateInterviewStatus(@Body StatusInterviewRequest request);

        @POST("jobs/extendJob")
        Call<PostJobResponse> extendJob(@Body RepostJobRequest request);


    }
}
