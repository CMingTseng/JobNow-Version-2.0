package com.newtech.jobnow.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.acitvity.NotificationActivity;
import com.newtech.jobnow.adapter.InterviewAdapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.NotificationController;
import com.newtech.jobnow.models.InterviewObject;
import com.newtech.jobnow.models.InterviewResponse;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.JobObject;
import com.newtech.jobnow.models.NotificationRequest;
import com.newtech.jobnow.models.ProfileModel;
import com.newtech.jobnow.models.UserModel;
import com.newtech.jobnow.utils.Utils;
import com.newtech.jobnow.widget.CRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 10/02/2017.
 */

public class InterviewJobSeekerFragment extends Fragment {

    private LinearLayout lnErrorView;
    private CRecyclerView rvListJob;
    private InterviewAdapter adapter;
    private SwipeRefreshLayout refresh;
    int page=1;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private JobListRequest jobListRequest = null;
    private int type=1;
    private int companyID=0;
    ImageView img_back;
    EditText edSearchInterview;
    public static TextView txtCount;
    List<InterviewObject> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_interview_list_jobseeker, container, false);
        InitUI(rootView);
        InitEvent();
        bindData();
        return rootView;
    }

    public void InitUI(View rootView){
        txtCount=(TextView) rootView.findViewById(R.id.txtCount);
        img_back=(ImageView) rootView.findViewById(R.id.img_notification);

        rvListJob = (CRecyclerView) rootView.findViewById(R.id.rvListInterview);
        adapter = new InterviewAdapter(getActivity(),getActivity() ,new ArrayList<InterviewObject>(),2);
        lnErrorView = (LinearLayout) rootView.findViewById(R.id.lnErrorView);
        rvListJob.setAdapter(adapter);
        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(getActivity());

        edSearchInterview=(EditText) rootView.findViewById(R.id.edSearchInterview);

    }

    public void InitEvent(){
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                adapter.clear();
                list.clear();
                page = 1;
                bindData();
            }
        });
        rvListJob.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Utils.isReadyForPullEnd(recyclerView) && isCanNext && !isProgessingLoadMore) {
                    bindData();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        edSearchInterview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeSearch();
            }
        });
    }
    public void changeSearch(){
        if (edSearchInterview.getText().toString().equals("")) {
            adapter.clear();
            adapter.addAll(list);
        } else {
            List<InterviewObject> list_employee_tmp = new ArrayList<InterviewObject>();

            for (int i = 0; i < list.size(); i++) {
                if ( list.get(i).CompanyName.toLowerCase().contains(edSearchInterview.getText().toString().toLowerCase())||
                        list.get(i).CompanyEmail.toLowerCase().contains(edSearchInterview.getText().toString().toLowerCase())) {
                    list_employee_tmp.add(list.get(i));
                }
            }
            adapter.clear();
            adapter.addAll(list_employee_tmp);
        }
    }

    private void bindData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        int userID = sharedPreferences.getInt(Config.KEY_ID, 0);
        CountNotificationAsystask countNotificationAsystask= new CountNotificationAsystask(getActivity(),new NotificationRequest(userID,0));
        countNotificationAsystask.execute();

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait", true, false);
        isProgessingLoadMore = true;

        Bundle bundle = getArguments();
        if (bundle != null) {
            //jobListRequest = (JobListRequest) bundle.getSerializable(KEY_JOB);
        }
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<InterviewResponse> getJobList = service.getInterview(
                APICommon.getSign(APICommon.getApiKey(), JobListRequest.PATH_URL),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                0,
                userID
              );
        getJobList.enqueue(new Callback<InterviewResponse>() {
            @Override
            public void onResponse(Response<InterviewResponse> response, Retrofit retrofit) {
                isProgessingLoadMore = false;
                refresh.setRefreshing(false);
                try {
                    progressDialog.dismiss();
                    if (response.body() != null && response.body().code == 200) {
                        if (response.body().result != null ) {
                            adapter.addAll(response.body().result);
                            list.addAll(response.body().result);
                            /*if (page < response.body().result) {
                                page++;
                                isCanNext = true;
                            } else {
                                isCanNext = false;
                            }*/
                        } else {
                            isCanNext = false;
                        }
                    }

                    if(adapter.getItemCount() == 0) {
                        lnErrorView.setVisibility(View.VISIBLE);
                        rvListJob.setVisibility(View.GONE);
                    } else {
                        lnErrorView.setVisibility(View.GONE);
                        rvListJob.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                refresh.setRefreshing(false);
                isProgessingLoadMore = false;
                progressDialog.dismiss();
                //Toast.makeText(getActivity(), getActivity().getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }
    class CountNotificationAsystask extends AsyncTask<Void, Void, Integer> {
        ProgressDialog dialog;
        String sessionId = "";
        NotificationRequest notificationRequest;
        Context ct;
        Dialog dialogs;

        public CountNotificationAsystask(Context ct, NotificationRequest notificationRequest) {
            this.ct = ct;
            this.notificationRequest = notificationRequest;

        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                NotificationController controller = new NotificationController();
                return controller.CountNotification(notificationRequest);
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ct);
            dialog.setMessage("");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Integer code) {
            try {
                if(code==0){
                    txtCount.setVisibility(View.GONE);
                }else {
                    txtCount.setText(code + "");
                }

            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(resultCode == Activity.RESULT_OK) {
                try {
                    int result = data.getIntExtra("reload", 0);
                    if (result == 1) {
                        adapter.clear();
                        bindData();
                    }
                }catch (Exception err){
                    err.printStackTrace();
                }
            }
        }
    }
}
