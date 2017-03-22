package com.jobnow.acitvity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.models.LoginRequest;
import com.jobnow.models.LoginResponse;
import com.jobnow.models.RegisterFBReponse;
import com.jobnow.models.RegisterFBRequest;
import com.jobnow.service.DeleteTokenService;
import com.jobnow.utils.Utils;
import com.newtech.jobnow.R;

import org.json.JSONObject;

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
    String session_id;
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ver2);
        InitUI();
        InitEvent();
        initData();
        Intent intent1 = new Intent(this, DeleteTokenService.class);
        startService(intent1);
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
        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginJobSeekerActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
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
            try {
                Thread.sleep(3000);
                String session_id = FirebaseInstanceId.getInstance().getToken().toString();
                APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
                Call<LoginResponse> loginResponseCall =
                        service.loginUserV2(new LoginRequest(email, password,0,session_id));
                loginResponseCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                        try {
                            int code = response.body().code;
                            if (code == 200) {

                                SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Config.KEY_TOKEN, response.body().result.apiToken).commit();
                                editor.putInt(Config.KEY_ID, response.body().result.id).commit();
                                editor.putString(Config.KEY_EMAIL, response.body().result.email).commit();
                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), response.body().message, Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception err){
                            Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.d(TAG, "(on failed): " + t.toString());
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initData() {
        try {
            Thread.sleep(3000);
            session_id = FirebaseInstanceId.getInstance().getToken().toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(final JSONObject object,
                                                    GraphResponse response) {
                                if (object != null) {
                                    String email = object.optString("email")==null||object.optString("email").equals("")?object.optString("id"):object.optString("email");
                                    String name = object.optString("name");
                                    String fbid = object.optString("id");
                                    String avatar = Utils.addressAvatarFB(fbid);
                                    APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
                                    Call<RegisterFBReponse> registerFBReponseCall =
                                            service.registerFB(new RegisterFBRequest(name, email, avatar, fbid,session_id));
                                    registerFBReponseCall.enqueue(new Callback<RegisterFBReponse>() {
                                        @Override
                                        public void onResponse(final Response<RegisterFBReponse> response, Retrofit retrofit) {
                                            Log.d(TAG, "get login response: " + response.body().toString());
                                            int code = response.body().code;
                                            if (code == 200) {
                                                SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(Config.KEY_TOKEN, response.body().result.apiToken).commit();
                                                editor.putInt(Config.KEY_ID, response.body().result.id).commit();
                                                editor.putString(Config.KEY_EMAIL, response.body().result.email).commit();
                                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(getApplicationContext(),
                                                                response.body().message, Toast.LENGTH_SHORT)
                                                                .show();
                                                    }
                                                });

                                            }
                                        }

                                        @Override
                                        public void onFailure(Throwable t) {
                                            Log.d(TAG, "(on failed): " + t.toString());
                                        }
                                    });
                                } else {

                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,birthday,picture,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginJobSeekerActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onError(final FacebookException error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginJobSeekerActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
