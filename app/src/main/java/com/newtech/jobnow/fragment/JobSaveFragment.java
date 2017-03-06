package com.newtech.jobnow.fragment;


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
import android.util.Log;
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

import com.newtech.jobnow.R;
import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.acitvity.SearchResultActivity;
import com.newtech.jobnow.adapter.JobListAdapter;
import com.newtech.jobnow.adapter.JobListV2Adapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.eventbus.SaveJobListEvent;
import com.newtech.jobnow.models.JobListReponse;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.JobListV2Reponse;
import com.newtech.jobnow.models.JobObject;
import com.newtech.jobnow.models.JobV2Object;
import com.newtech.jobnow.models.NotificationRequest;
import com.newtech.jobnow.utils.Utils;
import com.newtech.jobnow.widget.CRecyclerView;

import org.greenrobot.eventbus.EventBus;

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
public class JobSaveFragment extends Fragment {
    private String TAG = JobSaveFragment.class.getSimpleName();
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
    EditText edSearchSave;
    List<JobV2Object> list = new ArrayList<>();
    boolean flag = false;
    Bundle bundle;
    int iRandom=-1;
    public JobSaveFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(boolean hasSearch) {
        return getInstance(hasSearch, null);
    }

    public static Fragment getInstance(boolean hasSearch, JobListRequest request) {
        Fragment fragment = new JobSaveFragment();
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
        edSearchSave.addTextChangedListener(new TextWatcher() {
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
        if (edSearchSave.getText().toString().equals("")) {
            adapter.clear();
            adapter.addAll(list);
            tvNumberJob.setText(getString(R.string.number_job, list.size()));
        } else {
            List<JobV2Object> list_employee_tmp = new ArrayList<JobV2Object>();

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Position.toLowerCase().contains(edSearchSave.getText().toString().toLowerCase()) ||
                        list.get(i).CompanyName.toLowerCase().contains(edSearchSave.getText().toString().toLowerCase()) ||
                        list.get(i).Title.toLowerCase().contains(edSearchSave.getText().toString().toLowerCase())) {
                    list_employee_tmp.add(list.get(i));
                }
            }
            adapter.clear();
            adapter.addAll(list_employee_tmp);
            tvNumberJob.setText(getString(R.string.number_job, list_employee_tmp.size()));
        }
    }

    private void bindData() {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
            isProgessingLoadMore = true;
            APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
            String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
            int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
            Call<JobListV2Reponse> request = service.getSaveListJobV2(
                    APICommon.getSign(APICommon.getApiKey(), "/api/v1/jobs/getSaveJob"),
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
                    if(jobList != null) {
                        if(jobList.code == 200) {
                            JobListV2Reponse.JobListV2Result result = jobList.result;
                            if(result != null) {
                                tvNumberJob.setText(result.total + " saved job");
                                adapter.addAll(result.data);
                                list.addAll(result.data);
                                Log.d(TAG, "save job list total:" + result.total);
                                if(page < result.last_page) {
                                    page++;
                                    isCanNext = true;
                                } else
                                    isCanNext = false;
                                if(result.data.size() == 0)
                                    isCanNext = false;

                                if(result.total == 0) {
                                    lnErrorView.setVisibility(View.VISIBLE);
                                    rvListJob.setVisibility(View.GONE);
                                } else {
                                    lnErrorView.setVisibility(View.GONE);
                                    rvListJob.setVisibility(View.VISIBLE);
                                }
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
                        }

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
        rlSearchBar = (RelativeLayout) view.findViewById(R.id.rlSearchBar);
        rlSearchBar.setVisibility(getArguments().getBoolean(KEY_SEACH) ? View.VISIBLE : View.GONE);
        rvListJob = (CRecyclerView) view.findViewById(R.id.rvListJob);
        rvListJob.setDivider();
        adapter = new JobListV2Adapter(getActivity(), new ArrayList<JobV2Object>());
        rvListJob.setAdapter(adapter);
        tvNumberJob = (TextView) view.findViewById(R.id.tvNumberJob);
        lnErrorView = (LinearLayout) view.findViewById(R.id.lnErrorView);
        edSearchSave = (EditText) getActivity().findViewById(R.id.edSearchSave);

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

}
