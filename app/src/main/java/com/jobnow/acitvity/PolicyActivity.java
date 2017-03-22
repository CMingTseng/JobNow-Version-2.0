package com.jobnow.acitvity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jobnow.adapter.TermsofUseAdapter;
import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.controller.InviteController;
import com.jobnow.models.InviteRequest;
import com.jobnow.models.TermObject;
import com.jobnow.models.TermResponse;
import com.jobnow.models.UserModel;
import com.jobnow.utils.Utils;
import com.jobnow.widget.CRecyclerView;
import com.newtech.jobnow.R;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 12/02/2017.
 */

public class PolicyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SwipeRefreshLayout refresh;
    int page=1;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private LinearLayout lnErrorView;
    private CRecyclerView rvListJob;
    private TermsofUseAdapter adapter;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        InitUI();
        InitEvent();
        BindData();
    }

    public void InitUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_sign_in);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Search");
        toolbar.setTitleTextColor(Color.parseColor("#0c69d3"));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Privacy Policy");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_back);

        rvListJob = (CRecyclerView) findViewById(R.id.rvListTermUseList);
        adapter = new TermsofUseAdapter(PolicyActivity.this, new ArrayList<TermObject>(),1);
        lnErrorView = (LinearLayout) findViewById(R.id.lnErrorView);
        rvListJob.setAdapter(adapter);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(PolicyActivity.this);

    }

    public void InitEvent() {
        /*click vào nut home tren toolbar*/
        View view = toolbar.getChildAt(0);
        view.setOnClickListener(new View.OnClickListener() {
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

    public void BindData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String userProfile = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        Gson gson = new Gson();
        userModel=gson.fromJson(userProfile,UserModel.class);

        final ProgressDialog progressDialog = ProgressDialog.show(PolicyActivity.this, "Loading...", "Please wait", true, false);
        isProgessingLoadMore = true;

        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        String url = APICommon.BASE_URL + "users/getAllPrivacy/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/users/getListTerm")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType();
        Call<TermResponse> getJobList = service.getTermsofUse(url);
        getJobList.enqueue(new Callback<TermResponse>() {
            @Override
            public void onResponse(Response<TermResponse> response, Retrofit retrofit) {
                isProgessingLoadMore = false;
                refresh.setRefreshing(false);
                try {
                    progressDialog.dismiss();
                    if (response.body() != null && response.body().code == 200) {
                        if (response.body().result != null && response.body().result.size() > 0) {
                            adapter.addAll(response.body().result);
                           /* if (page < response.body().result.last_page) {
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
                Toast.makeText(PolicyActivity.this, PolicyActivity.this.getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class SetNewInviteAsystask extends AsyncTask<Void,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        InviteRequest profileRequest;
        Context ct;
        Dialog dialogs;
        public SetNewInviteAsystask(Context ct, InviteRequest profileRequest, Dialog dialogs){
            this.ct=ct;
            this.profileRequest=profileRequest;
            this.dialogs=dialogs;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                InviteController controller= new InviteController();
                return controller.SetNewInvite(profileRequest);
            }catch (Exception ex){
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ct);
            dialog.setMessage("" );
            dialog.show();
        }

        @Override
        protected void onPostExecute(String code) {
            try {
                if(!code.equals("")){
                    Toast.makeText(PolicyActivity.this, code, Toast.LENGTH_SHORT).show();
                    adapter.clear();
                    BindData();
                    dialogs.dismiss();
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }

}
