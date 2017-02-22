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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.adapter.NotificationAdapter;
import com.newtech.jobnow.adapter.NotificationVer2Adapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.CategoryController;
import com.newtech.jobnow.models.LoginResponse;
import com.newtech.jobnow.models.NotificationObject;
import com.newtech.jobnow.models.NotificationResponse;
import com.newtech.jobnow.models.NotificationVersion2Object;
import com.newtech.jobnow.models.NotificationVersion2Response;
import com.newtech.jobnow.models.ProfileModel;
import com.newtech.jobnow.models.TokenRequest;
import com.newtech.jobnow.utils.Utils;
import com.newtech.jobnow.widget.CRecyclerView;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NotificationActivity extends AppCompatActivity {

    /*public static final String TAG = NotificationActivity.class.getSimpleName();
    private CRecyclerView rvNotification;
    private NotificationAdapter adapter;
    private RelativeLayout btnBack, btnRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initUI();
        bindData();
    }

    public void getApiToken() {
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
        String email = sharedPreferences.getString(Config.KEY_EMAIL, "");
        Call<LoginResponse> call = service.getToken(new TokenRequest(userId, email));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                Log.d(TAG, "get token: " + response.body());
                if(response.body() != null && response.body().code == 200) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.KEY_TOKEN, response.body().result.apiToken);
                    editor.commit();
                    bindData();
                } else
                    Toast.makeText(getApplicationContext(), response.body().message,
                            Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_connect, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void bindData() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Please wait...", true, false);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
        int userId = sharedPreferences.getInt(Config.KEY_ID, 0);

        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<NotificationResponse> call = service.getListNotification(
                APICommon.getSign(APICommon.getApiKey(), "api/v1/notification/getListNotification"),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                userId,
                token);
        call.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Response<NotificationResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    NotificationResponse notificationResponse = response.body();
                    if (notificationResponse.code == 200) {
                        if (notificationResponse.result.size() > 0)
                            adapter.addAll(notificationResponse.result);
                    } else if(notificationResponse.code == 503){
                        getApiToken();
                        Toast.makeText(getApplicationContext(), notificationResponse.message,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), notificationResponse.message,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), R.string.error_connect, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void initUI() {
        rvNotification = (CRecyclerView) findViewById(R.id.rvNotification);
        rvNotification.setDivider();
        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        btnRemove = (RelativeLayout) findViewById(R.id.btnRemove);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rvNotification.setHasFixedSize(true);
        rvNotification.setItemAnimator(new DefaultItemAnimator());

        adapter = new NotificationAdapter(NotificationActivity.this,
                new ArrayList<NotificationObject>());
        rvNotification.setAdapter(adapter);
    }*/


    private RelativeLayout btnBack;
    TextView edTitleCategory;
    private SwipeRefreshLayout refresh;
    int page=1;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private LinearLayout lnErrorView;
    private CRecyclerView rvListJob;
    private NotificationVer2Adapter adapter;
    private RelativeLayout btn_add_manager;
    ProfileModel profileModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref,MODE_PRIVATE);
        String profile=sharedPreferences.getString(Config.KEY_COMPANY_PROFILE,"");
        Gson gson= new Gson();
        profileModel=gson.fromJson(profile,ProfileModel.class);

        InitUI();
        InitEvent();
        BindData();
    }

    public void InitUI() {
        rvListJob = (CRecyclerView) findViewById(R.id.rvNotification);
        adapter = new NotificationVer2Adapter(NotificationActivity.this, new ArrayList<NotificationVersion2Object>(),2);
        lnErrorView = (LinearLayout) findViewById(R.id.lnErrorView);
        rvListJob.setAdapter(adapter);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(NotificationActivity.this);
        btnBack=(RelativeLayout) findViewById(R.id.btnBack);

    }

    public void InitEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    }

    private void BindData() {
        final SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
        int userID = sharedPreferences.getInt(Config.KEY_ID, 0);
        final ProgressDialog progressDialog = ProgressDialog.show(NotificationActivity.this, "Loading...", "Please wait", true, false);
        isProgessingLoadMore = true;
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<NotificationVersion2Response> getJobList = service.getListNotification(
                APICommon.getSign(APICommon.getApiKey(), "api/v1/notification/getListNotification"),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                0,
                userID,page
        );
        getJobList.enqueue(new Callback<NotificationVersion2Response>() {
            @Override
            public void onResponse(Response<NotificationVersion2Response> response, Retrofit retrofit) {
                isProgessingLoadMore = false;
                refresh.setRefreshing(false);
                try {
                    progressDialog.dismiss();
                    if (response.body() != null && response.body().code == 200) {
                        if (response.body().result != null && response.body().result != null) {
                            adapter.addAll(response.body().result.data);
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
                lnErrorView.setVisibility(View.VISIBLE);
                rvListJob.setVisibility(View.GONE);
                //Toast.makeText(ShortlistManagerActivity.this, ShortlistManagerActivity.this.getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int result=data.getIntExtra("result",0);
                String results=data.getStringExtra("results");
                if (result==15){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",result);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                edTitleCategory.setText(results);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

        }
    }
    class GetShortListDetailAsystask extends AsyncTask<Void,Void,Void> {
        ProgressDialog dialog;
        String sessionId="";
        int category_id;
        Context ct;
        Dialog dialogs;
        public GetShortListDetailAsystask(Context ct,int category_id){
            this.ct=ct;
            this.category_id=category_id;

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                CategoryController controller= new CategoryController();
                controller.GetShortListDetail(category_id);
            }catch (Exception ex){
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ct);
            dialog.setMessage("" );
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void code) {
            try {

            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
}
