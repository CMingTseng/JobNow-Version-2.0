package com.newtech.jobnow.acitvity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.adapter.InviteAdapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.InviteController;
import com.newtech.jobnow.models.InviteObject;
import com.newtech.jobnow.models.InviteRequest;
import com.newtech.jobnow.models.InviteResponse;
import com.newtech.jobnow.models.JobListRequest;
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

public class InviteActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SwipeRefreshLayout refresh;
    int page=1;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private LinearLayout lnErrorView;
    private CRecyclerView rvListJob;
    private InviteAdapter adapter;
    private RelativeLayout btn_add_invite;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
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
        actionBar.setTitle(getResources().getString(R.string.get_more_job_credits));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_back);

        rvListJob = (CRecyclerView) findViewById(R.id.rvListInvite);
        adapter = new InviteAdapter(InviteActivity.this, new ArrayList<InviteObject>());
        lnErrorView = (LinearLayout) findViewById(R.id.lnErrorView);
        rvListJob.setAdapter(adapter);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(InviteActivity.this);

        btn_add_invite=(RelativeLayout) findViewById(R.id.btn_add_manager);
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

        btn_add_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(InviteActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_invitation);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int width = (int)(getResources().getDisplayMetrics().widthPixels*0.80);
                int height = (int)(getResources().getDisplayMetrics().heightPixels*0.53);
                dialog.getWindow().setLayout(width, height);
                dialog.show();

                final EditText edtCompanyNameInvite,edtFirstnameInvite,edtLasttnameInvite,edtEmailInvite;
                edtCompanyNameInvite=(EditText)dialog.findViewById(R.id.edtCompanyNameInvite);
                edtFirstnameInvite=(EditText)dialog.findViewById(R.id.edtFirstnameInvite);
                edtLasttnameInvite=(EditText)dialog.findViewById(R.id.edtLasttnameInvite);
                edtEmailInvite=(EditText)dialog.findViewById(R.id.edtEmailInvite);
                Button btnSendInvite=(Button) dialog.findViewById(R.id.btnSendInvite);



                btnSendInvite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String companyName=edtCompanyNameInvite.getText().toString();
                        String firstName=edtFirstnameInvite.getText().toString();
                        String lastName=edtLasttnameInvite.getText().toString();
                        String emailCompany=edtEmailInvite.getText().toString();
                        if (edtCompanyNameInvite.getText().toString().isEmpty()) {
                            Toast.makeText(InviteActivity.this, getString(R.string.pleaseInputCompanyName), Toast.LENGTH_SHORT).show();
                        } else if (edtFirstnameInvite.getText().toString().isEmpty()) {
                            Toast.makeText(InviteActivity.this, getString(R.string.pleaseInputFirstName), Toast.LENGTH_SHORT).show();
                        } else if (!Utils.isEmailValid(edtEmailInvite.getText().toString())) {
                            Toast.makeText(InviteActivity.this, getString(R.string.emailNotValid), Toast.LENGTH_SHORT).show();
                        } else if (edtLasttnameInvite.getText().toString().isEmpty()) {
                            Toast.makeText(InviteActivity.this, getString(R.string.pleaseInputLastName), Toast.LENGTH_SHORT).show();
                        } else {
                            InviteRequest inviteRequest= new InviteRequest(companyName,firstName,lastName,emailCompany,0,userModel.id);
                            SetNewInviteAsystask setNewInviteAsystask= new SetNewInviteAsystask(InviteActivity.this,inviteRequest,dialog);
                            setNewInviteAsystask.execute();
                        }
                    }
                });


            }
        });

    }

    public void BindData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String userProfile = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        Gson gson = new Gson();
        userModel=gson.fromJson(userProfile,UserModel.class);

        final ProgressDialog progressDialog = ProgressDialog.show(InviteActivity.this, "Loading...", "Please wait", true, false);
        isProgessingLoadMore = true;

        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<InviteResponse> getJobList = service.getListInvitation(
                APICommon.getSign(APICommon.getApiKey(), JobListRequest.PATH_URL),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                userModel.id
                );
        getJobList.enqueue(new Callback<InviteResponse>() {
            @Override
            public void onResponse(Response<InviteResponse> response, Retrofit retrofit) {
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
                Toast.makeText(InviteActivity.this, InviteActivity.this.getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class SetNewInviteAsystask extends AsyncTask<Void,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        InviteRequest profileRequest;
        Context ct;
        Dialog dialogs;
        public SetNewInviteAsystask(Context ct,InviteRequest profileRequest,Dialog dialogs){
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
                    Toast.makeText(InviteActivity.this, code, Toast.LENGTH_SHORT).show();
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
