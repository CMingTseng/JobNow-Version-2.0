package com.jobnow.acitvity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.jobnow.adapter.ShortlistManagerAdapter;
import com.jobnow.common.APICommon;
import com.jobnow.controller.CategoryController;
import com.jobnow.models.ProfileModel;
import com.jobnow.models.ShortlistDetailObject;
import com.jobnow.models.ShortlistDetailResponse;
import com.jobnow.utils.Utils;
import com.jobnow.widget.CRecyclerView;
import com.jobnow.R;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 12/02/2017.
 */

public class ShortlistManagerActivity extends AppCompatActivity {
    ImageView img_back,img_setting;
    TextView edTitleCategory;
    private SwipeRefreshLayout refresh;
    int page=1;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private LinearLayout lnErrorView;
    private CRecyclerView rvListJob;
    private ShortlistManagerAdapter adapter;
    private RelativeLayout btn_add_manager;
    ProfileModel profileModel;
    int category_id=0;
    String category_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortlist_detail);
        try {
            category_id=getIntent().getIntExtra("category_id",0);
            category_name=getIntent().getStringExtra("category_name");
        }catch (Exception err){

        }
        InitUI();
        InitEvent();
        BindData();
    }

    public void InitUI() {
        rvListJob = (CRecyclerView) findViewById(R.id.rvListShortListDetail);
        adapter = new ShortlistManagerAdapter(ShortlistManagerActivity.this, new ArrayList<ShortlistDetailObject>(),0,category_id);
        lnErrorView = (LinearLayout) findViewById(R.id.lnErrorView);
        rvListJob.setAdapter(adapter);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(ShortlistManagerActivity.this);
        btn_add_manager=(RelativeLayout) findViewById(R.id.btn_add_manager);

        img_back=(ImageView) findViewById(R.id.img_back);
        img_setting=(ImageView) findViewById(R.id.img_setting);
        edTitleCategory=(TextView) findViewById(R.id.edTitleCategory);
        edTitleCategory.setText(category_name);
    }

    public void InitEvent() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result",1);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ShortlistManagerActivity.this,SettingCategoryActivity.class);
                intent.putExtra("category_id",category_id);
                intent.putExtra("category_name",category_name);
                startActivityForResult(intent,1);
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

        btn_add_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ShortlistManagerActivity.this,AddShortlistManagerActivity.class);
                intent.putExtra("category_id",category_id);
                intent.putExtra("category_name",category_name);
                startActivityForResult(intent,1);
            }
        });

    }

    private void BindData() {
        final ProgressDialog progressDialog = ProgressDialog.show(ShortlistManagerActivity.this, "Loading...", "Please wait", true, false);
        isProgessingLoadMore = true;

        /*GetShortListDetailAsystask getShortListDetailAsystask= new GetShortListDetailAsystask(ShortlistManagerActivity.this,category_id);
        getShortListDetailAsystask.execute();*/

        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<ShortlistDetailResponse> getJobList = service.getShortlistDetail(
                APICommon.getSign(APICommon.getApiKey(), "api/v1/shortlist/getShortlist"),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                category_id
        );
        getJobList.enqueue(new Callback<ShortlistDetailResponse>() {
            @Override
            public void onResponse(Response<ShortlistDetailResponse> response, Retrofit retrofit) {
                isProgessingLoadMore = false;
                refresh.setRefreshing(false);
                try {
                    progressDialog.dismiss();
                    if (response.body() != null && response.body().code == 200) {
                        if (response.body().result != null && response.body().result != null) {
                            adapter.addAll(response.body().result);
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
                String results=data.getStringExtra("results")==null?"":data.getStringExtra("results");
                if (result==15){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",result);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
                else if (result==1502){
                    adapter.clear();
                    BindData();
                }
                if(!results.equals("")) {
                    edTitleCategory.setText(results);
                }
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
