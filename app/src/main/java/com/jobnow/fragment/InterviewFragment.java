package com.jobnow.fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jobnow.acitvity.MyApplication;
import com.jobnow.adapter.InterviewAdapter;
import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.models.InterviewObject;
import com.jobnow.models.InterviewResponse;
import com.jobnow.models.JobListRequest;
import com.jobnow.models.ProfileModel;
import com.jobnow.models.UserModel;
import com.jobnow.utils.Utils;
import com.jobnow.widget.CRecyclerView;
import com.jobnow.R;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 10/02/2017.
 */

public class InterviewFragment extends Fragment {

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_interview_list, container, false);
        InitUI(rootView);
        InitEvent();
        bindData();
        return rootView;
    }

    public void InitUI(View rootView){

        rvListJob = (CRecyclerView) rootView.findViewById(R.id.rvListInterview);
        adapter = new InterviewAdapter(getActivity(), new ArrayList<InterviewObject>(),1);
        lnErrorView = (LinearLayout) rootView.findViewById(R.id.lnErrorView);
        rvListJob.setAdapter(adapter);
        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(getActivity());

    }
    public void InitEvent(){

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                adapter.clear();
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
    }


    private void bindData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        String profile=sharedPreferences.getString(Config.KEY_USER_PROFILE,"");
        String profileCompany=sharedPreferences.getString(Config.KEY_COMPANY_PROFILE,"");
        Gson gson= new Gson();
        UserModel userModel=gson.fromJson(profile,UserModel.class);
        ProfileModel profileModel= gson.fromJson(profileCompany,ProfileModel.class);
        companyID=userModel.id;
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
                companyID,
                0
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

}
