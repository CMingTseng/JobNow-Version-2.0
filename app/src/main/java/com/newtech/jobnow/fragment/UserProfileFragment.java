package com.newtech.jobnow.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.newtech.jobnow.R;
import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.acitvity.ProfileVer2Activity;
import com.newtech.jobnow.acitvity.SetInterviewActivity;
import com.newtech.jobnow.adapter.InterviewAdapter;
import com.newtech.jobnow.adapter.LocationSpinnerAdapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.InterviewController;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.DeleteInterviewRequest;
import com.newtech.jobnow.models.JobLocationResponse;
import com.newtech.jobnow.models.LoginResponse;
import com.newtech.jobnow.models.TokenRequest;
import com.newtech.jobnow.models.UpdateProfileRequest;
import com.newtech.jobnow.models.UserDetailResponse;
import com.newtech.jobnow.models.UserModel;
import com.newtech.jobnow.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class UserProfileFragment extends Fragment {
    private TextView tvEmail, tvPhoneNumber, tvGender, tvBirthday, tvCountry, tvPostalCode, tvDescription;
    private ProgressDialog progressDialog;
    private ImageView imgEditPhoneNumber, imgEditGender, imgEditBirthday, imgEditCountry, imgEditPostalCode, imgEditDescription;
    private UserModel userModel;
    private int gender = 0;

    /*public MyProfileFragment() {
        // Required empty public constructor
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initUI(view);
        bindData();
        event();
        return view;
    }

    private void event() {

    }

    private void bindData() {
        loadUserDetail();
    }

    private void loadUserDetail() {
        progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading), true, true);
        int user_id = ProfileVer2Activity.idJobSeeker;
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<UserDetailResponse> call =
                service.getUserProfile(APICommon.getSign(APICommon.getApiKey(),
                                "api/v1/users/getUserProfile"),
                        APICommon.getAppId(),
                        APICommon.getDeviceType(), user_id);
        call.enqueue(new Callback<UserDetailResponse>() {
            @Override
            public void onResponse(final Response<UserDetailResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {
                    userModel = response.body().result;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setProfileToUI(true);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProfileToUI(boolean isUpdateAvatar) {
        if (isUpdateAvatar)
            if (userModel.avatar != null) {
                if(userModel.avatar.indexOf("http://graph.facebook.com")>5){
                    Picasso.with(getActivity()).load(userModel.avatar.substring(userModel.avatar.indexOf("http://graph.facebook.com"),userModel.avatar.length())).
                            placeholder(R.mipmap.default_avatar).
                            error(R.mipmap.default_avatar).
                            into(ProfileVer2Activity.img_avatar);
                }else {
                    Picasso.with(getActivity()).load(userModel.avatar).
                            placeholder(R.mipmap.default_avatar).
                            error(R.mipmap.default_avatar).
                            into(ProfileVer2Activity.img_avatar);
                }
            }
        ProfileVer2Activity.tvName.setText(
                userModel.fullName == null || userModel.fullName.isEmpty() ? userModel.email : userModel.fullName);
        ProfileVer2Activity.tvLocation.setText(userModel.countryName);
        tvEmail.setText(userModel.email);
        tvPhoneNumber.setText(userModel.phoneNumber);
        gender = userModel.gender;
        if (userModel.gender == 1) {
            tvGender.setText(getString(R.string.male));
        } else if (userModel.gender == 2) {
            tvGender.setText(getString(R.string.female));
        }
        tvBirthday.setText(userModel.birthDay);
        tvCountry.setText(userModel.countryName);
        tvPostalCode.setText(userModel.postalCode);
        tvDescription.setText(userModel.description);

        ProfileVer2Activity.btnSetInterviewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),SetInterviewActivity.class);
                intent.putExtra("profile",userModel);
                getActivity().startActivity(intent);
            }
        });
        ProfileVer2Activity.btnNotSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder1.setMessage("Are you sure to reject the candidate");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                               try {
                                   RejectInterviewAsystask rejectInterviewAsystask= new RejectInterviewAsystask(getActivity(),new DeleteInterviewRequest(ProfileVer2Activity.interviewID));
                                   rejectInterviewAsystask.execute();
                               }catch (Exception err){
                                   err.printStackTrace();
                               }
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                android.support.v7.app.AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
    }

    private void initUI(View view) {
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
        tvGender = (TextView) view.findViewById(R.id.tvGender);
        tvBirthday = (TextView) view.findViewById(R.id.tvBirthday);
        tvCountry = (TextView) view.findViewById(R.id.tvCountry);
        tvPostalCode = (TextView) view.findViewById(R.id.tvPostalCode);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);

    }

    class RejectInterviewAsystask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;
        String sessionId = "";
        DeleteInterviewRequest request;
        Context ct;
        int position;

        public RejectInterviewAsystask(Context ct, DeleteInterviewRequest request) {
            this.ct = ct;
            this.request = request;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                InterviewController controller = new InterviewController();
                return controller.RejectInterview(request);
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ct);
            dialog.setMessage("");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String code) {
            try {
                if (!code.equals("")) {
                    Toast.makeText(getActivity(), code, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }
}
