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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.UserController;
import com.newtech.jobnow.models.LoginRequest;
import com.newtech.jobnow.models.LoginResponse;
import com.newtech.jobnow.models.UserModel;
import com.newtech.jobnow.utils.Utils;

import java.util.Arrays;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 07/02/2017.
 */

public class LoginJobSeekerActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edtEmail_ver2;
    private EditText edtPassword_v2;
    private Button btnLogin_v2,btnRegister_v2,fbLogin;
    private TextView txtForgotPass;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ver2);
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

        edtEmail_ver2=(EditText) findViewById(R.id.edtEmail_ver2);
        edtPassword_v2=(EditText) findViewById(R.id.edtPassword_v2);
        btnLogin_v2=(Button) findViewById(R.id.btnLogin_v2);
        btnRegister_v2=(Button) findViewById(R.id.btnRegister_v2);
        fbLogin=(Button) findViewById(R.id.fbLogin);
        txtForgotPass=(TextView) findViewById(R.id.txtForgotPass);
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

        btnLogin_v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogin();
            }
        });

        btnRegister_v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFacebook();
            }
        });
    }
    private void getLogin() {
        String email = edtEmail_ver2.getText().toString();
        String password = edtPassword_v2.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(LoginJobSeekerActivity.this, getString(R.string.pleaseInputEmail), Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(LoginJobSeekerActivity.this, getString(R.string.pleaseInputPassword), Toast.LENGTH_SHORT).show();
        } else if (!Utils.isEmailValid(email)) {
            Toast.makeText(LoginJobSeekerActivity.this, getString(R.string.emailNotValid), Toast.LENGTH_SHORT).show();
        } else {
            APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
            Call<LoginResponse> loginResponseCall =
                    service.loginUser(new LoginRequest(email, password));
            loginResponseCall.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                    Log.d(TAG, "get login response: " + response.body().toString());
                    int code = response.body().code;
                    if (code == 200) {
                        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Config.KEY_TOKEN, response.body().result.apiToken).commit();
                        editor.putInt(Config.KEY_ID, response.body().result.id).commit();
                        editor.putString(Config.KEY_EMAIL, response.body().result.email).commit();
                        Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d(TAG, "(on failed): " + t.toString());
                }
            });
        }


    }

    private void loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
    }
}
