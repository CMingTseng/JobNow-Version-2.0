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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.adapter.NotificationVer2Adapter;
import com.newtech.jobnow.adapter.ShortlistManagerAdapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.CategoryController;
import com.newtech.jobnow.models.NotificationVersion2Object;
import com.newtech.jobnow.models.NotificationVersion2Response;
import com.newtech.jobnow.models.ProfileModel;
import com.newtech.jobnow.models.ShortlistDetailObject;
import com.newtech.jobnow.models.ShortlistDetailResponse;
import com.newtech.jobnow.models.UserModel;
import com.newtech.jobnow.utils.Utils;
import com.newtech.jobnow.widget.CRecyclerView;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 12/02/2017.
 */

public class NotificationManagerActivity extends AppCompatActivity {
    ImageView img_back;
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
        setContentView(R.layout.activity_notification_manager);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref,MODE_PRIVATE);
        String profile=sharedPreferences.getString(Config.KEY_COMPANY_PROFILE,"");
        Gson gson= new Gson();
        profileModel=gson.fromJson(profile,ProfileModel.class);

        InitUI();
        InitEvent();
        BindData();
    }

    public void InitUI() {
        rvListJob = (CRecyclerView) findViewById(R.id.rvListNotification);
        adapter = new NotificationVer2Adapter(NotificationManagerActivity.this, new ArrayList<NotificationVersion2Object>());
        lnErrorView = (LinearLayout) findViewById(R.id.lnErrorView);
        rvListJob.setAdapter(adapter);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(NotificationManagerActivity.this);
        img_back=(ImageView) findViewById(R.id.img_back);

    }

    public void InitEvent() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent returnIntent = new Intent();
                returnIntent.putExtra("result",1);
                setResult(Activity.RESULT_OK,returnIntent);*/
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
        final ProgressDialog progressDialog = ProgressDialog.show(NotificationManagerActivity.this, "Loading...", "Please wait", true, false);
        isProgessingLoadMore = true;
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<NotificationVersion2Response> getJobList = service.getListNotification(
                APICommon.getSign(APICommon.getApiKey(), "api/v1/notification/getListNotification"),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                profileModel.CompanyID,
                0
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
