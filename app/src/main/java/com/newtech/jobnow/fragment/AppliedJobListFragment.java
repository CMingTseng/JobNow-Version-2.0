package com.newtech.jobnow.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newtech.jobnow.R;
import com.newtech.jobnow.acitvity.FilterActivity;
import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.acitvity.NotificationActivity;
import com.newtech.jobnow.acitvity.SearchResultActivity;
import com.newtech.jobnow.adapter.JobListAdapter;
import com.newtech.jobnow.adapter.JobListV2Adapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.NotificationController;
import com.newtech.jobnow.eventbus.ApplyJobEvent;
import com.newtech.jobnow.eventbus.DeleteJobEvent;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.DeleteJobRequest;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.JobListV2Reponse;
import com.newtech.jobnow.models.JobV2Object;
import com.newtech.jobnow.models.NotificationRequest;
import com.newtech.jobnow.utils.Utils;
import com.newtech.jobnow.widget.CRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppliedJobListFragment extends Fragment {
    public static final String TAG = AppliedJobListFragment.class.getSimpleName();
    //    private LinearLayout lnJob1, lnJob2, lnJob3;
    private CRecyclerView rvListJob;
    private JobListV2Adapter adapter;
    private TextView tvNumberJob;
    private LinearLayout lnErrorView;
    private RelativeLayout imgFilter, imgBack;
    private SwipeRefreshLayout refresh;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private int page = 1;
    EditText edSearch;
    public static TextView txtCount;
    List<JobV2Object> list = new ArrayList<>();
    public AppliedJobListFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        return new AppliedJobListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_job_list, container, false);
        initUI(rootView);
        bindData();
        event();
        return rootView;
    }
    public void event(){
        edSearch.addTextChangedListener(new TextWatcher() {
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
    private void bindData() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        int userID = sharedPreferences.getInt(Config.KEY_ID, 0);
        CountNotificationAsystask countNotificationAsystask= new CountNotificationAsystask(getActivity(),new NotificationRequest(userID,0));
        countNotificationAsystask.execute();

        isProgessingLoadMore = true;
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();

        String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
        int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
        Call<JobListV2Reponse> request = service.getAppliedListJob(
                APICommon.getSign(APICommon.getApiKey(), "/api/v1/jobs/getAppliedJob"),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                userId,
                token, page);
        request.enqueue(new Callback<JobListV2Reponse>() {
            @Override
            public void onResponse(Response<JobListV2Reponse> response, Retrofit retrofit) {
                refresh.setRefreshing(false);
                isProgessingLoadMore = false;
                JobListV2Reponse jobList = response.body();
                if (jobList != null) {
                    if (jobList.code == 200) {
                        JobListV2Reponse.JobListV2Result result = jobList.result;
                        if (result != null) {
                            tvNumberJob.setText(result.total + " applied job");
                            adapter.addAll(result.data);
                            list.addAll(result.data);
                            if(result.total == 0) {
                                lnErrorView.setVisibility(View.VISIBLE);
                                rvListJob.setVisibility(View.GONE);
                            } else {
                                lnErrorView.setVisibility(View.GONE);
                                rvListJob.setVisibility(View.VISIBLE);
                            }

                            if (page < result.last_page) {
                                page++;
                                isCanNext = true;
                            } else {
                                isCanNext = false;
                            }

                            if(result.data.size() == 0)
                                isCanNext = false;
                        }
                    } else if(jobList.code == 503) {
                        MyApplication.getInstance().getApiToken(new MyApplication.TokenCallback() {
                            @Override
                            public void onTokenSuccess() {
                                page = 1;
                                bindData();
                            }
                        });
                        Toast.makeText(getActivity(), jobList.message, Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(getActivity(), jobList.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                refresh.setRefreshing(false);
                isProgessingLoadMore = false;
                Toast.makeText(getActivity(), getString(R.string.error_connect),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void changeSearch(){
        if (edSearch.getText().toString().equals("")) {
            adapter.clear();
            adapter.addAll(list);
            tvNumberJob.setText(getString(R.string.number_job, list.size()));
        } else {
            List<JobV2Object> list_employee_tmp = new ArrayList<JobV2Object>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Position.toLowerCase().contains(edSearch.getText().toString().toLowerCase())||
                        list.get(i).CompanyName.toLowerCase().contains(edSearch.getText().toString().toLowerCase())||
                        list.get(i).Title.toLowerCase().contains(edSearch.getText().toString().toLowerCase())) {
                    list_employee_tmp.add(list.get(i));
                }
            }
            adapter.clear();
            adapter.addAll(list_employee_tmp);
            tvNumberJob.setText(getString(R.string.number_job, list_employee_tmp.size()));
        }
    }
    void search(String title) {
        Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
        if (title.equals(""))
            title = null;
        JobListRequest request = new JobListRequest(1, "ASC", title, null, null,
                null, null, null, null,0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SearchResultActivity.KEY_JOB, request);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    private void initUI(View view) {
        txtCount=(TextView) view.findViewById(R.id.txtCount);
        rvListJob = (CRecyclerView) view.findViewById(R.id.rvListJob);
        lnErrorView = (LinearLayout) view.findViewById(R.id.lnErrorView);
        rvListJob.setDivider();
        adapter = new JobListV2Adapter(getActivity(), new ArrayList<JobV2Object>(),
                JobListAdapter.APPLY_TYPE);
        rvListJob.setAdapter(adapter);
        tvNumberJob = (TextView) view.findViewById(R.id.tvNumberJob);
        imgFilter = (RelativeLayout) view.findViewById(R.id.imgFilter);
        imgBack = (RelativeLayout) view.findViewById(R.id.btnRemove);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        edSearch=(EditText) view.findViewById(R.id.edSearch);
        rvListJob.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Utils.isReadyForPullEnd(recyclerView) && isCanNext && !isProgessingLoadMore) {
                    bindData();
                }
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


       /* EditText edtSearch = (EditText) view.findViewById(R.id.edSearchs);
        edtSearch.requestFocus();
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(v.getText().toString());
                    return true;
                }
                return false;
            }
        });*/
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Subscribe
    public void onEvent(ApplyJobEvent event) {
        adapter.clear();
        bindData();
    }

    @Subscribe
    public void onEvent(DeleteJobEvent event) {
        int type = event.type;
        if (type == JobListAdapter.APPLY_TYPE) {
            int job_id = adapter.getItembyPostion(event.position).JobID;
            final int position = event.position;
            APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                    Config.Pref, Context.MODE_PRIVATE);
            String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
            int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
            Call<BaseResponse> call = service.deleteAppliedJob(new DeleteJobRequest(userId, job_id,
                    token, userId, type));
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {
                    if (response.body() != null && response.body().code == 200) {
                        adapter.remove(position);
                        tvNumberJob.setText(adapter.getItemCount() +  " applied job");

                        if(adapter.getItemCount() == 0) {
                            lnErrorView.setVisibility(View.VISIBLE);
                            rvListJob.setVisibility(View.GONE);
                        } else {
                            lnErrorView.setVisibility(View.GONE);
                            rvListJob.setVisibility(View.VISIBLE);
                        }
                    } else if(response.body().code == 503){
                        MyApplication.getInstance().getApiToken();
                    } else {
                        Toast.makeText(getActivity(), response.body().message, Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }


    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
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
}