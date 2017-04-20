package com.jobnow.acitvity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.jobnow.common.CustomEditextHelveticaneuelight;
import com.jobnow.common.DrawableClickListener;
import com.jobnow.config.Config;
import com.jobnow.controller.UserController;
import com.jobnow.models.ChangePassRequest;
import com.jobnow.models.ProfileModel;
import com.jobnow.models.ProfileRequest;
import com.jobnow.models.UserModel;
import com.jobnow.R;

/**
 * Created by Administrator on 12/02/2017.
 */

public class AppSettingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ProfileModel profileModel;
    private UserModel userModel;
    TextView txt_name_setting, txt_email_address;
    CustomEditextHelveticaneuelight edtPhone;
    Button btnChangePass;
    boolean flag=false;
    String numberPhone="";
    private  EditText edtOldPassword,edtNewPassword,edtReEnterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
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
        actionBar.setTitle(getResources().getString(R.string.app_settings));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_back);

        txt_name_setting = (TextView) findViewById(R.id.txt_name_setting);
        txt_email_address = (TextView) findViewById(R.id.txt_email_address);
        edtPhone = (CustomEditextHelveticaneuelight) findViewById(R.id.edtPhone);
        edtPhone.setEnabled(false);
        btnChangePass=(Button) findViewById(R.id.btnChangePass);
        edtOldPassword=(EditText) findViewById(R.id.edtOldPassword);
        edtNewPassword=(EditText) findViewById(R.id.edtNewPassword);
        edtReEnterPassword=(EditText) findViewById(R.id.edtReEnterPassword);
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

        edtPhone.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        //Do something here
                        break;
                    case RIGHT:
                        if(!flag) {
                            edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pencil, 0);
                            edtPhone.setSelection(edtPhone.getText().toString().trim().length());
                            edtPhone.setEnabled(true);
                            flag=true;
                        }else{
                            edtPhone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pencil_inactive, 0);
                            edtPhone.setEnabled(false);
                            if(!numberPhone.equals(edtPhone.getText().toString().trim())) {
                                ProfileRequest profileRequest = new ProfileRequest(userModel.id, userModel.apiToken, edtPhone.getText().toString().trim(), "","");
                                UpdateProfileAsystask updateProfileAsystask = new UpdateProfileAsystask(AppSettingActivity.this, profileRequest);
                                updateProfileAsystask.execute();
                            }
                            flag=false;
                        }
                        break;
                    case TOP:
                        //Do something here
                        break;
                    case BOTTOM:
                        //Do something here
                        break;
                    default:
                        break;
                }
            }

        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass= edtOldPassword.getText().toString();
                String newPass= edtNewPassword.getText().toString();
                String reEnterPass= edtReEnterPassword.getText().toString();
                if (oldPass.isEmpty()) {
                    Toast.makeText(AppSettingActivity.this, getString(R.string.pleaseInputOldPass), Toast.LENGTH_SHORT).show();
                } else if (newPass.isEmpty()) {
                    Toast.makeText(AppSettingActivity.this, getString(R.string.pleaseInputNewPass), Toast.LENGTH_SHORT).show();
                } else if (reEnterPass.isEmpty()) {
                    Toast.makeText(AppSettingActivity.this, getString(R.string.pleaseInputReEnter), Toast.LENGTH_SHORT).show();
                } else if (!newPass.equals(reEnterPass)) {
                    Toast.makeText(AppSettingActivity.this, getString(R.string.pleasediffrent), Toast.LENGTH_SHORT).show();
                }else {
                    ChangePassRequest changePassRequest = new ChangePassRequest(userModel.email, oldPass, newPass);
                    ChangePasswordAsystask changePasswordAsystask = new ChangePasswordAsystask(AppSettingActivity.this, changePassRequest);
                    changePasswordAsystask.execute();
                }
            }
        });
    }

    public void BindData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String userProfile = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        String companyProfile = sharedPreferences.getString(Config.KEY_COMPANY_PROFILE, "");
        Gson gson = new Gson();
        profileModel = gson.fromJson(companyProfile, ProfileModel.class);
        userModel = gson.fromJson(userProfile, UserModel.class);
        txt_name_setting.setText(profileModel.Name);
        txt_email_address.setText(userModel.email);
        edtPhone.setText(profileModel.ContactNumber);
        numberPhone=profileModel.ContactNumber;
    }

    class UpdateProfileAsystask extends AsyncTask<Void,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        ProfileRequest profileRequest;
        Context ct;
        public UpdateProfileAsystask(Context ct,ProfileRequest profileRequest){
            this.ct=ct;
            this.profileRequest=profileRequest;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                UserController controller= new UserController();
                return controller.UpdateProfileCompany(profileRequest);
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
                    numberPhone=edtPhone.getText().toString().trim();
                    Toast.makeText(AppSettingActivity.this, "Phone updated successfully!", Toast.LENGTH_SHORT).show();
                    profileModel.setContactNumber(edtPhone.getText().toString().trim());
                    Gson gson= new Gson();
                    String profile=gson.toJson(profileModel);
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.KEY_COMPANY_PROFILE, profile).commit();
                    flag=false;
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
    class ChangePasswordAsystask extends AsyncTask<Void,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        ChangePassRequest profileRequest;
        Context ct;
        public ChangePasswordAsystask(Context ct,ChangePassRequest profileRequest){
            this.ct=ct;
            this.profileRequest=profileRequest;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                UserController controller= new UserController();
                return controller.ChangePassword(profileRequest);
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
                    Toast.makeText(AppSettingActivity.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                    edtOldPassword.setText("");
                    edtNewPassword.setText("");
                    edtReEnterPassword.setText("");
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
}
