package com.jobnow.acitvity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jobnow.common.APICommon;
import com.jobnow.models.CompanyProfile;
import com.jobnow.models.CompanyProfileResponse;
import com.newtech.jobnow.R;
import com.squareup.picasso.Picasso;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CompanyProfileActivity extends AppCompatActivity {

    ImageView img_avatar,img_back;
    TextView tvName,txt_industry,txt_company_size,txt_email,txt_phone,txt_dis;
    private ProgressDialog progressDialog;
    int companyID=0;
    CompanyProfile companyProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_company);
        try{
            companyID=getIntent().getIntExtra("companyID",0);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        initUI();
        bindData();
    }

    public void initUI(){
        img_back=(ImageView) findViewById(R.id.img_back);
        img_avatar=(ImageView) findViewById(R.id.img_avatar);
        tvName=(TextView) findViewById(R.id.tvName);
        txt_industry=(TextView) findViewById(R.id.txt_industry);
        txt_company_size=(TextView) findViewById(R.id.txt_company_size);
        txt_email=(TextView) findViewById(R.id.txt_email);
        txt_phone=(TextView) findViewById(R.id.txt_phone);
        txt_dis=(TextView) findViewById(R.id.txt_dis);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void bindData(){
        loadCompanyProfile();
    }

    private void loadCompanyProfile() {
        progressDialog = ProgressDialog.show(CompanyProfileActivity.this, "", getString(R.string.loading), true, true);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<CompanyProfileResponse> call =
                service.getCompanyProfile(APICommon.getSign(APICommon.getApiKey(),
                        "api/v1/users/getUserProfile"),
                        APICommon.getAppId(),
                        APICommon.getDeviceType(), companyID);
        call.enqueue(new Callback<CompanyProfileResponse>() {
            @Override
            public void onResponse(final Response<CompanyProfileResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {
                    companyProfile = response.body().result;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setData();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CompanyProfileActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setData(){
        try {
            Picasso.with(CompanyProfileActivity.this).load(companyProfile.Logo).placeholder(R.mipmap.default_avatar).error(R.mipmap.default_avatar).into(img_avatar);
            tvName.setText(companyProfile.Name);
            txt_company_size.setText("From "+companyProfile.CompanySizeName+" members");
            txt_industry.setText(companyProfile.IndustryName);
            txt_email.setText(companyProfile.Email);
            txt_phone.setText(companyProfile.ContactNumber);
            txt_dis.setText(companyProfile.Overview);

        }catch (Exception err){
            err.printStackTrace();
        }

    }
}
