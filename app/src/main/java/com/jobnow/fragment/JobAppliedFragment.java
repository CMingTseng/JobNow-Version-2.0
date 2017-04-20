package com.jobnow.fragment;


import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jobnow.acitvity.MyApplication;
import com.jobnow.acitvity.SearchResultActivity;
import com.jobnow.adapter.JobListV2Adapter;
import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.controller.NotificationController;
import com.jobnow.models.JobListRequest;
import com.jobnow.models.JobListV2Reponse;
import com.jobnow.models.JobV2Object;
import com.jobnow.models.NotificationRequest;
import com.jobnow.utils.Utils;
import com.jobnow.widget.CRecyclerView;
import com.jobnow.R;

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
public class JobAppliedFragment extends Fragment {
    private String TAG = JobAppliedFragment.class.getSimpleName();
    public static String KEY_SEACH = "hasSearch";
    public static String KEY_JOB = "key_job";
    private String ASC = "ASC";
    private String DESC = "DESC";
    private Spinner spnSortBy;
    private String sort = DESC;
    private int page = 1;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private JobListRequest jobListRequest = null;
    private LinearLayout lnErrorView;
    EditText edSearch;
    List<JobV2Object> list = new ArrayList<>();
    boolean flag = false;
    Bundle bundle;
    int iRandom=-1;
    public static TextView txtCount;
    public JobAppliedFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(boolean hasSearch) {
        return getInstance(hasSearch, null);
    }

    public static Fragment getInstance(boolean hasSearch, JobListRequest request) {
        Fragment fragment = new JobAppliedFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_SEACH, hasSearch);
        bundle.putSerializable(KEY_JOB, request);
        fragment.setArguments(bundle);
        return fragment;
    }

    //    private LinearLayout lnJob1, lnJob2, lnJob3;
    private RelativeLayout rlSearchBar;
    private CRecyclerView rvListJob;
    private JobListV2Adapter adapter;
    private TextView tvNumberJob;
    private SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_job_list2, container, false);
        initUI(rootView);
        bindData();
        event();
        return rootView;
    }

    private void event() {
        spnSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sort = DESC;
                    //sort = ASC;
                } else {
                    sort = DESC;
                }
                adapter.clear();
                list.clear();
                page = 1;
                bindData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //sort = ASC;
                sort = DESC;
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
                jobListRequest = null;
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

    public void changeSearch() {
        /*if (edSearch.getText().toString().equals("")) {
            adapter.clear();
            adapter.addAll(list);
            tvNumberJob.setText(getString(R.string.number_job, list.size()));
        } else {
            List<JobV2Object> list_employee_tmp = new ArrayList<JobV2Object>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Position.toLowerCase().contains(edSearch.getText().toString().toLowerCase()) ||
                        list.get(i).CompanyName.toLowerCase().contains(edSearch.getText().toString().toLowerCase()) ||
                        list.get(i).Title.toLowerCase().contains(edSearch.getText().toString().toLowerCase())) {
                    list_employee_tmp.add(list.get(i));
                }
            }
            adapter.clear();
            adapter.addAll(list_employee_tmp);
            tvNumberJob.setText(getString(R.string.number_job, list_employee_tmp.size()));
        }*/

        jobListRequest = new JobListRequest(page, sort, edSearch.getText().toString(), null, null, null,
                null, null, null,0);
        sort = DESC;
        adapter.clear();
        list.clear();
        page = 1;
        bindData();
    }

    private void bindData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        isProgessingLoadMore = true;
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();

        String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
        int userId = sharedPreferences.getInt(Config.KEY_ID, 0);

       CountNotificationAsystask countNotificationAsystask= new CountNotificationAsystask(getActivity(),new NotificationRequest(userId,0));
        countNotificationAsystask.execute();
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
                                tvNumberJob.setText("No Results Found. Your search didn't match any results");
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

    private void search(String title) {
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
        rlSearchBar = (RelativeLayout) view.findViewById(R.id.rlSearchBar);
        rlSearchBar.setVisibility(getArguments().getBoolean(KEY_SEACH) ? View.VISIBLE : View.GONE);
        rvListJob = (CRecyclerView) view.findViewById(R.id.rvListJob);
        rvListJob.setDivider();
        adapter = new JobListV2Adapter(getActivity(), new ArrayList<JobV2Object>(), 2);
        rvListJob.setAdapter(adapter);
        tvNumberJob = (TextView) view.findViewById(R.id.tvNumberJob);
        lnErrorView = (LinearLayout) view.findViewById(R.id.lnErrorView);
        edSearch = (EditText) getActivity().findViewById(R.id.edSearchSave);

        spnSortBy = (Spinner) view.findViewById(R.id.spnSortBy);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        Utils.closeKeyboard(getActivity());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bundle bundle1 = data.getExtras();
                    if (bundle1 != null) {
                        flag = true;
                        jobListRequest = (JobListRequest) bundle1.getSerializable(KEY_JOB);
                        if (iRandom != jobListRequest.IntRandom) {
                            iRandom = jobListRequest.IntRandom;
                            adapter.clear();
                            list.clear();
                            page = 1;
                            bindData();
                        }
                    }
                }catch (Exception err){
                    err.printStackTrace();
                }

            }
        }
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
                    JobMainFragment.txtCount.setVisibility(View.GONE);
                }else {
                    JobMainFragment.txtCount.setVisibility(View.VISIBLE);
                    JobMainFragment.txtCount.setText(code + "");
                }

            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }

}
