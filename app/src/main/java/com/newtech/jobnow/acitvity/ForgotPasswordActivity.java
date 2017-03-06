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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.UserController;
import com.newtech.jobnow.models.ForgotRequest;
import com.newtech.jobnow.models.LoginRequest;
import com.newtech.jobnow.models.UserModel;

/**
 * Created by Administrator on 07/02/2017.
 */

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText edtEmail;
    private Button btnForgot;
    private TextView txt_des_forgot_pass,txt_status;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
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
        actionBar.setTitle("Forgot Password");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_back);

        edtEmail=(EditText) findViewById(R.id.edtEmail);
        btnForgot=(Button) findViewById(R.id.btnForgot);

        txt_des_forgot_pass=(TextView) findViewById(R.id.txt_des_forgot_pass);
        txt_status=(TextView) findViewById(R.id.txt_status);
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

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotAsystask forgotAsystask= new ForgotAsystask(ForgotPasswordActivity.this, new ForgotRequest(edtEmail.getText().toString()));
                forgotAsystask.execute();
            }
        });

    }
    // AsynTask
    class ForgotAsystask extends AsyncTask<ForgotRequest,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        ForgotRequest request;
        Context ct;
        public ForgotAsystask(Context ct,ForgotRequest request){
            this.ct=ct;
            this.request=request;
        }

        @Override
        protected String doInBackground(ForgotRequest... params) {
            try {
                UserController controller= new UserController();
                return controller.ForgotPass(request);
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
                Toast.makeText(ForgotPasswordActivity.this, "Please check your email to reset password", Toast.LENGTH_SHORT).show();
                if(code.equals("")) {
                    txt_status.setVisibility(View.VISIBLE);
                    txt_des_forgot_pass.setVisibility(View.VISIBLE);
                    String textCondition= "A email has been send to "+edtEmail.getText().toString().trim()+", \nplease check email to reset password. Thank you!!!";
                    Spannable wordtoSpan1 = new SpannableString(textCondition);
                    int index=textCondition.indexOf(edtEmail.getText().toString().trim());
                    wordtoSpan1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), index, index+edtEmail.getText().toString().trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    txt_des_forgot_pass.setText(wordtoSpan1);
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }


}
