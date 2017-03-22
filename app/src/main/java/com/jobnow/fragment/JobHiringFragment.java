package com.jobnow.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jobnow.acitvity.MyApplication;
import com.jobnow.adapter.JobManagerAdapter;
import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.models.JobListReponse;
import com.jobnow.models.JobListRequest;
import com.jobnow.models.JobObject;
import com.jobnow.models.UserModel;
import com.jobnow.utils.Utils;
import com.jobnow.widget.CRecyclerView;
import com.newtech.jobnow.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 10/02/2017.
 */

public class JobHiringFragment extends Fragment {
    RelativeLayout tab_hiringInProgress,tab_doneHiring;
    ImageView img_hiring_progress,img_done_hiring;
    TextView txt_tab_hiring_inprogress,txt_done_hiring;
    private LinearLayout lnErrorView;
    private CRecyclerView rvListJob;
    private JobManagerAdapter adapter;
    private SwipeRefreshLayout refresh;
    int page=1;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private JobListRequest jobListRequest = null;
    private int type=1;
    private int companyID=0;
    UserModel userModel;

    EditText edtSearch;
    List<JobObject> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_jobs, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        String profile=sharedPreferences.getString(Config.KEY_USER_PROFILE,"");
        Gson gson= new Gson();
        userModel=gson.fromJson(profile,UserModel.class);

        InitUI(rootView);
        InitEvent();
        bindData();
        ChangeStatusTab(1);
        return rootView;
    }

    public void InitUI(View rootView){
        View view = getActivity().findViewById(R.id.layout_editSearch);
        edtSearch=(EditText) view.findViewById(R.id.editSearch);
        tab_hiringInProgress=(RelativeLayout) rootView.findViewById(R.id.tab_hiringInProgress);
        tab_doneHiring=(RelativeLayout) rootView.findViewById(R.id.tab_doneHiring);
        img_hiring_progress=(ImageView) rootView.findViewById(R.id.img_hiring_progress);
        img_done_hiring=(ImageView) rootView.findViewById(R.id.img_done_hiring);
        txt_tab_hiring_inprogress=(TextView)rootView.findViewById(R.id.txt_tab_hiring_inprogress);
        txt_done_hiring=(TextView)rootView.findViewById(R.id.txt_done_hiring);
        rvListJob = (CRecyclerView) rootView.findViewById(R.id.rvListJobManager);
        adapter = new JobManagerAdapter(getActivity(), new ArrayList<JobObject>(),userModel);
        lnErrorView = (LinearLayout) rootView.findViewById(R.id.lnErrorView);
        rvListJob.setAdapter(adapter);
        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(getActivity());
        ChangeStatusTab(2);
    }
    public void InitEvent(){
        tab_hiringInProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeStatusTab(1);
                type=1;
                adapter.clear();
                list.clear();
                page = 1;
                bindData();
            }
        });
        tab_doneHiring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeStatusTab(2);
                type=0;
                adapter.clear();
                list.clear();
                page = 1;
                bindData();
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

        edtSearch.addTextChangedListener(new TextWatcher() {
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
    public void ChangeStatusTab(int key){
        if(key==1){
            img_hiring_progress.setImageResource(R.mipmap.ic_hiring_active);
            txt_tab_hiring_inprogress.setTextColor(getActivity().getResources().getColor(R.color.white));
            tab_hiringInProgress.setBackgroundColor(getActivity().getResources().getColor(R.color.colorBgApplyJob));

            img_done_hiring.setImageResource(R.mipmap.ic_done_inactive);
            txt_done_hiring.setTextColor(getActivity().getResources().getColor(R.color.colorText));
            tab_doneHiring.setBackgroundColor(getActivity().getResources().getColor(R.color.backgroudTab));
        }else {
            img_hiring_progress.setImageResource(R.mipmap.ic_hiring_inactive);
            txt_tab_hiring_inprogress.setTextColor(getActivity().getResources().getColor(R.color.colorText));
            tab_hiringInProgress.setBackgroundColor(getActivity().getResources().getColor(R.color.backgroudTab));

            img_done_hiring.setImageResource(R.mipmap.ic_done_active);
            txt_done_hiring.setTextColor(getActivity().getResources().getColor(R.color.white));
            tab_doneHiring.setBackgroundColor(getActivity().getResources().getColor(R.color.colorBgApplyJob));
        }
    }

    public void bindData() {

        companyID=userModel.id;
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait", true, false);
        isProgessingLoadMore = true;

        Bundle bundle = getArguments();
        if (bundle != null) {
            //jobListRequest = (JobListRequest) bundle.getSerializable(KEY_JOB);
        }
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<JobListReponse> getJobList = service.getJobListByParamV2(
                APICommon.getSign(APICommon.getApiKey(), JobListRequest.PATH_URL),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                page,
                "DESC",
                "",
                "",
                "",
                0,
                0, 0,
                0,
                1,
                type,
                companyID,-1);
        getJobList.enqueue(new Callback<JobListReponse>() {
            @Override
            public void onResponse(Response<JobListReponse> response, Retrofit retrofit) {
                isProgessingLoadMore = false;
                refresh.setRefreshing(false);
                try {
                    progressDialog.dismiss();
                    if (response.body() != null && response.body().code == 200) {
                        if (response.body().result != null && response.body().result.data != null
                                && response.body().result.data.size() > 0) {
                            adapter.setTypeJob(type);
                            adapter.addAll(response.body().result.data);
                            list.addAll(response.body().result.data);
                            changeSearch();
                            if (page < response.body().result.last_page) {
                                page++;
                                isCanNext = true;
                            } else {
                                isCanNext = false;
                            }
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
                Toast.makeText(getActivity(), getActivity().getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void changeSearch(){
        if (edtSearch.getText().toString().equals("")) {
            adapter.clear();
            adapter.addAll(list);
        } else {
            List<JobObject> list_employee_tmp = new ArrayList<JobObject>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Position.toLowerCase().contains(edtSearch.getText().toString().toLowerCase())||
                        list.get(i).CompanyName.toLowerCase().contains(edtSearch.getText().toString().toLowerCase())||
                        list.get(i).Title.toLowerCase().contains(edtSearch.getText().toString().toLowerCase())) {
                    list_employee_tmp.add(list.get(i));
                }
            }
            adapter.clear();
            adapter.addAll(list_employee_tmp);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
                ChangeStatusTab(1);
                type=1;
                adapter.clear();
                list.clear();
                page = 1;
                bindData();
                /*int result = data.getIntExtra("result", 0);
                if(result==150293){
                    ChangeStatusTab(1);
                    type=1;
                    adapter.clear();
                    list.clear();
                    page = 1;
                    bindData();
                }*/
        }
    }
}
