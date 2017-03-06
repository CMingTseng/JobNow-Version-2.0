package com.newtech.jobnow.acitvity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.adapter.EmployeeAdapter;
import com.newtech.jobnow.adapter.ShortlistManagerAdapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.CategoryController;
import com.newtech.jobnow.controller.FeedbackController;
import com.newtech.jobnow.controller.ShortListController;
import com.newtech.jobnow.models.EmployeeAddRequest;
import com.newtech.jobnow.models.EmployeeObject;
import com.newtech.jobnow.models.EmployeeResponse;
import com.newtech.jobnow.models.FeedbackRequest;
import com.newtech.jobnow.models.ProfileModel;
import com.newtech.jobnow.models.ShortlistDetailObject;
import com.newtech.jobnow.models.ShortlistDetailResponse;
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
 * Created by Administrator on 12/02/2017.
 */

public class AddShortlistManagerActivity extends AppCompatActivity {
    ImageView ing_back;
    TextView btn_done;
    EditText editTextSearch;
    private SwipeRefreshLayout refresh;
    int page = 1;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private LinearLayout lnErrorView;
    private CRecyclerView rvListJob;
    private EmployeeAdapter adapter;
    private RelativeLayout btn_add_manager;
    ProfileModel profileModel;
    int category_id = 0;
    String category_name = "";
    List<EmployeeObject> list = new ArrayList<>();
    int inSet=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee_shortlist);
        try {
            category_id = getIntent().getIntExtra("category_id", 0);
            category_name = getIntent().getStringExtra("category_name");
        } catch (Exception err) {

        }
        InitUI();
        InitEvent();
        BindData();
    }

    public void InitUI() {
        rvListJob = (CRecyclerView) findViewById(R.id.rvListEmployee);
        adapter = new EmployeeAdapter(AddShortlistManagerActivity.this, new ArrayList<EmployeeObject>());
        lnErrorView = (LinearLayout) findViewById(R.id.lnErrorView);
        rvListJob.setAdapter(adapter);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(AddShortlistManagerActivity.this);
        ing_back = (ImageView) findViewById(R.id.ing_back);
        btn_done = (TextView) findViewById(R.id.btn_done);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);

    }

    public void InitEvent() {
        ing_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<EmployeeObject> list = adapter.getList();
                    List<EmployeeObject> list_tmp = new ArrayList<EmployeeObject>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isChoose) {
                            list_tmp.add(list.get(i));
                        }
                    }
                    if (list_tmp.size() > 0) {
                        ProgressDialog dialog;
                        dialog = new ProgressDialog(AddShortlistManagerActivity.this);
                        dialog.setMessage("Waiting...");
                        dialog.show();
                        for (int i = 0; i < list_tmp.size(); i++) {
                            EmployeeAddRequest employeeAddRequest = new EmployeeAddRequest(category_id, list_tmp.get(i).user_id);
                            SetAddShortlistAsystask setAddShortlistAsystask = new SetAddShortlistAsystask(AddShortlistManagerActivity.this, employeeAddRequest);
                            setAddShortlistAsystask.execute();
                        }
                        Toast.makeText(AddShortlistManagerActivity.this, list_tmp.size()+" Candidate(s) added successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",1502);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                } catch (Exception e) {

                }
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                adapter.clear();
                page = 1;
                BindData();
            }
        });

        rvListJob.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Utils.isReadyForPullEnd(recyclerView) && isCanNext && !isProgessingLoadMore) {
                    BindData();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editTextSearch.getText().toString().equals("")) {
                    adapter.clear();
                    adapter.addAll(list);
                } else {
                    List<EmployeeObject> list_employee_tmp = new ArrayList<EmployeeObject>();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).FullName.toLowerCase().contains(editTextSearch.getText().toString().toLowerCase())
                                || list.get(i).Name.toLowerCase().contains(editTextSearch.getText().toString().toLowerCase())) {
                            list_employee_tmp.add(list.get(i));
                        }
                    }
                    adapter.clear();
                    adapter.addAll(list_employee_tmp);
                }
            }
        });
    }

    private void BindData() {
        final ProgressDialog progressDialog = ProgressDialog.show(AddShortlistManagerActivity.this, "Loading...", "Please wait", true, false);
        isProgessingLoadMore = true;

        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref,MODE_PRIVATE);
        String profile=sharedPreferences.getString(Config.KEY_USER_PROFILE,"");
        Gson gson= new Gson();
        UserModel userModel=gson.fromJson(profile,UserModel.class);

        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<EmployeeResponse> getJobList = service.getEmployee(
                APICommon.getSign(APICommon.getApiKey(), "api/v1/users/getListEmployee"),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                "",
                category_id,
                userModel.id
        );
        getJobList.enqueue(new Callback<EmployeeResponse>() {
            @Override
            public void onResponse(Response<EmployeeResponse> response, Retrofit retrofit) {
                isProgessingLoadMore = false;
                refresh.setRefreshing(false);
                try {
                    progressDialog.dismiss();
                    if (response.body() != null && response.body().code == 200) {
                        if (response.body().result != null && response.body().result != null) {
                            adapter.addAll(response.body().result);
                            list = response.body().result;
                            /*if (page < response.body().result.last_page) {
                                page++;
                                isCanNext = true;
                            } else {
                                isCanNext = false;
                            }*/
                        } else {
                            isCanNext = false;
                        }
                    }

                    if (adapter.getItemCount() == 0) {
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
                lnErrorView.setVisibility(View.VISIBLE);
                rvListJob.setVisibility(View.GONE);
                //Toast.makeText(AddShortlistManagerActivity.this, ShortlistManagerActivity.this.getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class SetAddShortlistAsystask extends AsyncTask<Void, Void, String> {

        String sessionId = "";
        EmployeeAddRequest request;
        Context ct;
        Dialog dialogs;
        int type;

        public SetAddShortlistAsystask(Context ct, EmployeeAddRequest request) {
            this.ct = ct;
            this.request = request;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                ShortListController controller = new ShortListController();
                return controller.AddShortlist(request);
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String code) {
            try {
                if (!code.equals("")) {
                   inSet++;
                }
            } catch (Exception e) {
            }

        }
    }
}
