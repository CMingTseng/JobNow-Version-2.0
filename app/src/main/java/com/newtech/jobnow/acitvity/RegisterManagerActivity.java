package com.newtech.jobnow.acitvity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.adapter.IndustryAdapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.UserController;
import com.newtech.jobnow.models.IndustryObject;
import com.newtech.jobnow.models.IndustryResponse;
import com.newtech.jobnow.models.LoginRequest;
import com.newtech.jobnow.models.LoginResponse;
import com.newtech.jobnow.models.RegisterManagerRequest;
import com.newtech.jobnow.models.RegisterRequest;
import com.newtech.jobnow.models.UserModel;
import com.newtech.jobnow.service.DeleteTokenService;
import com.newtech.jobnow.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 08/02/2017.
 */

public class RegisterManagerActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edtCompanyName,edtFullNameManager,edtEmailManager,edtPhoneNumberManager,edtPasswordManager;
    private int industryID=0,companySizeID=0;
    private TextView txtCondition;
    private Button btnRegister;
    private IndustryAdapter industryAdapter,industryCompanySize;
    private Spinner spIndustry,spCompanySize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_manager_v2);
        InitUI();
        getIndustry();
        getCompanySize();
        InitEvent();
        Intent intent1 = new Intent(this, DeleteTokenService.class);
        startService(intent1);
    }
    public void InitUI(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar_sign_up);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Search");
        toolbar.setTitleTextColor(Color.parseColor("#0c69d3"));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.login));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_back);

        /*click vào nut home tren toolbar*/
        View view = toolbar.getChildAt(0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txtCondition=(TextView) findViewById(R.id.txtCondition);
        String textCondition="Term of Service and Privacy Policy";
        Spannable wordtoSpan1 = new SpannableString("Term of Service and Privacy Policy");
        int index=textCondition.indexOf("and");
        wordtoSpan1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), index, index+3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtCondition.setText(wordtoSpan1);

        spIndustry=(Spinner) findViewById(R.id.spIndustry);
        spCompanySize=(Spinner) findViewById(R.id.spCompanySize);

        edtCompanyName=(EditText) findViewById(R.id.edtCompanyName);
        edtFullNameManager=(EditText) findViewById(R.id.edtFullNameManager);
        edtEmailManager=(EditText) findViewById(R.id.edtEmailManager);
        edtPhoneNumberManager=(EditText) findViewById(R.id.edtPhoneNumberManager);
        edtPasswordManager=(EditText) findViewById(R.id.edtPasswordManager);

        btnRegister=(Button) findViewById(R.id.btnRegister);
    }

    private void getIndustry() {
        final ProgressDialog progressDialog = ProgressDialog.show(RegisterManagerActivity.this,
                "Loading", "Please wait..", true, false);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        String url = APICommon.BASE_URL + "industry/getListIndustry/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/industry/getListIndustry")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType();
        Call<IndustryResponse> industryResponseCall = service.getIndustry(url);
        industryResponseCall.enqueue(new Callback<IndustryResponse>() {
            @Override
            public void onResponse(Response<IndustryResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {
                    List<IndustryObject> industryObjects = new ArrayList<>();
                    IndustryObject industryObject = new IndustryObject();
                    industryObject.id = 0;
                    industryObject.Name = getString(R.string.selectIndustry);
                    industryObjects.add(industryObject);
                    if (response.body().result != null && response.body().result.size() > 0)
                        industryObjects.addAll(response.body().result);
                    industryAdapter = new IndustryAdapter(RegisterManagerActivity.this, industryObjects);
                    spIndustry.setAdapter(industryAdapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterManagerActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCompanySize() {
        final ProgressDialog progressDialog = ProgressDialog.show(RegisterManagerActivity.this,
                "Loading", "Please wait..", true, false);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        String url = APICommon.BASE_URL + "companysize/getListCompanySize/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/industry/getListIndustry")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType();
        Call<IndustryResponse> industryResponseCall = service.getIndustry(url);
        industryResponseCall.enqueue(new Callback<IndustryResponse>() {
            @Override
            public void onResponse(Response<IndustryResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {
                    List<IndustryObject> industryObjects = new ArrayList<>();
                    IndustryObject industryObject = new IndustryObject();
                    industryObject.id = 0;
                    industryObject.Name = getString(R.string.selectCompanySize);
                    industryObjects.add(industryObject);
                    if (response.body().result != null && response.body().result.size() > 0)
                        industryObjects.addAll(response.body().result);
                    industryCompanySize = new IndustryAdapter(RegisterManagerActivity.this, industryObjects);
                    spCompanySize.setAdapter(industryCompanySize);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RegisterManagerActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void InitEvent(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String companyName=edtCompanyName.getText().toString();
                String fullname = " ";
                String phoneNumber = edtPhoneNumberManager.getText().toString();
                final String email = edtEmailManager.getText().toString();
                String password = edtPasswordManager.getText().toString();
                industryID= industryAdapter.getItem(spIndustry.getSelectedItemPosition()).id;
                companySizeID= industryCompanySize.getItem(spCompanySize.getSelectedItemPosition()).id;
                if (email.isEmpty()) {
                    Toast.makeText(RegisterManagerActivity.this, getString(R.string.pleaseInputEmail), Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(RegisterManagerActivity.this, getString(R.string.pleaseInputPassword), Toast.LENGTH_SHORT).show();
                } else if (!Utils.isEmailValid(email)) {
                    Toast.makeText(RegisterManagerActivity.this, getString(R.string.emailNotValid), Toast.LENGTH_SHORT).show();
                } /*else if (fullname.isEmpty()) {
                    Toast.makeText(RegisterManagerActivity.this, getString(R.string.pleaseInputFullName), Toast.LENGTH_SHORT).show();
                } */else if (phoneNumber.isEmpty()) {
                    Toast.makeText(RegisterManagerActivity.this, getString(R.string.pleaseInputPhoneNumber), Toast.LENGTH_SHORT).show();
                } else if (companyName.isEmpty()) {
                    Toast.makeText(RegisterManagerActivity.this, getString(R.string.pleaseInputPhoneNumber), Toast.LENGTH_SHORT).show();
                }else if(industryID==0){
                    Toast.makeText(RegisterManagerActivity.this, getString(R.string.selectIndustry), Toast.LENGTH_SHORT).show();
                }else if(companySizeID==0){
                    Toast.makeText(RegisterManagerActivity.this, getString(R.string.selectCompanySize), Toast.LENGTH_SHORT).show();
                }
                else {
                    RegisterManagerRequest registerManagerRequest= new RegisterManagerRequest(companyName,email,password,industryID,companySizeID,phoneNumber,fullname);
                    RegisterAsystask registerAsystask= new RegisterAsystask(RegisterManagerActivity.this,registerManagerRequest);
                    registerAsystask.execute();
                }
            }
        });
    }
    // AsynTask
    class RegisterAsystask extends AsyncTask<RegisterManagerRequest,Void,LoginResponse> {
        ProgressDialog dialog;
        String sessionId="";
        RegisterManagerRequest loginRequest;
        Context ct;
        public RegisterAsystask(Context ct,RegisterManagerRequest loginRequest){
            this.ct=ct;
            this.loginRequest=loginRequest;
        }

        @Override
        protected LoginResponse doInBackground(RegisterManagerRequest... params) {
            try {
                UserController controller= new UserController();
                return controller.RegisterManager(loginRequest);
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
        protected void onPostExecute(LoginResponse response) {
            try {
                Toast.makeText(RegisterManagerActivity.this, response.message, Toast.LENGTH_SHORT).show();
                if(response.code==200){
                    Gson gson= new Gson();
                    String profile=gson.toJson(response.result);
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Config.KEY_COMPANYID, response.result.getId()).commit();
                    editor.putString(Config.KEY_USER_PROFILE, profile).commit();

                    Intent intent= new Intent(RegisterManagerActivity.this,MenuActivity.class);
                    startActivity(intent);
                }else {

                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
}
