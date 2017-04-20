package com.jobnow.acitvity;

import android.app.Dialog;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.jobnow.config.Config;
import com.jobnow.controller.FeedbackController;
import com.jobnow.models.FeedbackRequest;
import com.jobnow.models.UserModel;
import com.jobnow.utils.Utils;
import com.jobnow.R;

/**
 * Created by Administrator on 12/02/2017.
 */

public class FeedbackActivity extends AppCompatActivity {
    private Toolbar toolbar;
    EditText edtSubTitle, editDescription;
    Button btnSendFeedback;

    UserModel userModel;
    int category_id = 0;
    String category_name = "";
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        InitUI();
        InitEvent();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String userProfile = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        String companyProfile = sharedPreferences.getString(Config.KEY_COMPANY_PROFILE, "");
        Gson gson = new Gson();
        userModel = gson.fromJson(userProfile, UserModel.class);
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

        Utils.closeKeyboard(FeedbackActivity.this);
        edtSubTitle = (EditText) findViewById(R.id.edtSubTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        btnSendFeedback = (Button) findViewById(R.id.btnSendFeedback);

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
        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSubTitle.getText().toString().trim().isEmpty()){
                    Toast.makeText(FeedbackActivity.this, getString(R.string.pleaseInputsubject), Toast.LENGTH_SHORT).show();
                }else if(editDescription.getText().toString().trim().isEmpty()){
                    Toast.makeText(FeedbackActivity.this, getString(R.string.pleaseInputdesFeedback), Toast.LENGTH_SHORT).show();
                }else {
                    FeedbackRequest request = new FeedbackRequest(edtSubTitle.getText().toString().trim(), editDescription.getText().toString(), userModel.id);
                    SetNewFeedbackAsystask setUpdateCategoryAsystask = new SetNewFeedbackAsystask(FeedbackActivity.this, request);
                    setUpdateCategoryAsystask.execute();
                }
            }
        });


    }


    class SetNewFeedbackAsystask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;
        String sessionId = "";
        FeedbackRequest feedbackRequest;
        Context ct;
        Dialog dialogs;
        int type;

        public SetNewFeedbackAsystask(Context ct, FeedbackRequest feedbackRequest) {
            this.ct = ct;
            this.feedbackRequest = feedbackRequest;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                FeedbackController controller = new FeedbackController();
                return controller.AddNewFeedback(feedbackRequest);
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ct);
            dialog.setMessage("");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String code) {
            try {
                if (!code.equals("")) {
                    Toast.makeText(FeedbackActivity.this, "Thank you for your valuable feedback", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }

}
