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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.UserController;
import com.newtech.jobnow.models.LoginRequest;
import com.newtech.jobnow.models.UserModel;

/**
 * Created by Administrator on 07/02/2017.
 */

public class LoginManagerActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edtPasswordManager;
    private EditText edtEmailManager;
    private Button btnLoginManager,btnRegisterManager;
    private TextView txtForgot;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_manager_v2);
        InitUI();
        InitEvent();
    }
    public void InitUI(){
        toolbar = (Toolbar)findViewById(R.id.toolbar_sign_in);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Search");
        toolbar.setTitleTextColor(Color.parseColor("#0c69d3"));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.login));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_back);

        edtEmailManager=(EditText) findViewById(R.id.edtEmailManager);
        edtPasswordManager=(EditText) findViewById(R.id.edtPasswordManager);
        btnLoginManager=(Button) findViewById(R.id.btnLoginManager);
        btnRegisterManager=(Button) findViewById(R.id.btnRegisterManager);
        txtForgot=(TextView) findViewById(R.id.txtForgot);
    }

    public void InitEvent(){
        /*click vào nut home tren toolbar*/
        View view = toolbar.getChildAt(0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLoginManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAsystask loginAsystask= new LoginAsystask(LoginManagerActivity.this, new LoginRequest(edtEmailManager.getText().toString(),edtPasswordManager.getText().toString(),1));
                loginAsystask.execute();
            }
        });

        btnRegisterManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginManagerActivity.this, RegisterManagerActivity.class);
                startActivity(intent);
            }
        });
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginManagerActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
    // AsynTask
    class LoginAsystask extends AsyncTask<LoginRequest,Void,UserModel> {
        ProgressDialog dialog;
        String sessionId="";
        LoginRequest loginRequest;
        Context ct;
        public LoginAsystask(Context ct,LoginRequest loginRequest){
            this.ct=ct;
            this.loginRequest=loginRequest;
        }

        @Override
        protected UserModel doInBackground(LoginRequest... params) {
            try {
                UserController controller= new UserController();
                return controller.CheckLogin(loginRequest);
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
        protected void onPostExecute(UserModel code) {
            try {
                if(code!=null){
                    Intent intent= new Intent(LoginManagerActivity.this,MenuActivity.class);

                    startActivity(intent);
                    Gson gson= new Gson();
                    String profile=gson.toJson(code);
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Config.KEY_COMPANYID, code.getId()).commit();
                    editor.putString(Config.KEY_USER_PROFILE, profile).commit();
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }


}
