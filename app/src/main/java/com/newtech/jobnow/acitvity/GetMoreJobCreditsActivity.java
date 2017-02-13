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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.common.CustomEditextHelveticaneuelight;
import com.newtech.jobnow.common.DrawableClickListener;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.UserController;
import com.newtech.jobnow.models.ChangePassRequest;
import com.newtech.jobnow.models.InviteObject;
import com.newtech.jobnow.models.ProfileModel;
import com.newtech.jobnow.models.ProfileRequest;
import com.newtech.jobnow.models.UserModel;

/**
 * Created by Administrator on 12/02/2017.
 */

public class GetMoreJobCreditsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    RelativeLayout layout_invite;
    EditText edtEmailInviteSend;
    Button btnRecivePricing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_more_job_credits);
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

        layout_invite=(RelativeLayout) findViewById(R.id.layout_invite);
        edtEmailInviteSend=(EditText) findViewById(R.id.edtEmailInviteSend);
        btnRecivePricing=(Button) findViewById(R.id.btnRecivePricing);
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

        layout_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(GetMoreJobCreditsActivity.this, InviteActivity.class);
                startActivity(intent);
            }
        });

        btnRecivePricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void BindData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String userProfile = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        String companyProfile = sharedPreferences.getString(Config.KEY_COMPANY_PROFILE, "");
        Gson gson = new Gson();
    }


}
