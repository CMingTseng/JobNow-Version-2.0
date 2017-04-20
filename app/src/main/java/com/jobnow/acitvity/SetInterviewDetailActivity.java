package com.jobnow.acitvity;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jobnow.common.CustomTextViewHelveticaneuelight;
import com.jobnow.config.Config;
import com.jobnow.controller.NotificationController;
import com.jobnow.models.InterviewObject;
import com.jobnow.models.ProfileModel;
import com.jobnow.models.ShortlistDetailObject;
import com.jobnow.models.StatusInterviewRequest;
import com.jobnow.models.UserModel;
import com.jobnow.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 12/02/2017.
 */

public class SetInterviewDetailActivity extends AppCompatActivity {
    private Toolbar toolbar;
    RelativeLayout layout_invite;
    TextView txt_name_employee, txt_location;
    CustomTextViewHelveticaneuelight editDatetimeInterview;
    TextView btn_start_time, btn_end_time;
    TextView editInterviewer, editPhoneNumber, editLocation, editSubjects, editMessage;
    ShortlistDetailObject shortlistDetailObject;
    ImageView img_photo_company;
    Button btnAccept, btnReject;
    TableRow tb_status;
    private int mYear, mMonth, mDay, mHour, mMinute;

    String dateTimeInterview = "";
    String startTime = "";
    String endTime = "";

    UserModel userModel, profile;
    ProfileModel profileModel;
    InterviewObject interviewObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setinterview_detail);

        try {
            interviewObject = (InterviewObject) getIntent().getSerializableExtra("interview_detail");
        } catch (Exception err) {

        }
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String profileUser = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        String profileCompany = sharedPreferences.getString(Config.KEY_COMPANY_PROFILE, "");
        Gson gson = new Gson();
        userModel = gson.fromJson(profileUser, UserModel.class);
        profileModel = gson.fromJson(profileCompany, ProfileModel.class);

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
        actionBar.setTitle(getResources().getString(R.string.set_interview_time));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_back);

        img_photo_company = (ImageView) findViewById(R.id.img_photo_company);

        txt_name_employee = (TextView) findViewById(R.id.txtTitleNotification);
        txt_location = (TextView) findViewById(R.id.txt_location);

        editDatetimeInterview = (CustomTextViewHelveticaneuelight) findViewById(R.id.editDatetimeInterview);
        btn_start_time = (TextView) findViewById(R.id.btn_start_time);
        btn_end_time = (TextView) findViewById(R.id.btn_end_time);
        editInterviewer = (TextView) findViewById(R.id.editInterviewer);
        editPhoneNumber = (TextView) findViewById(R.id.editPhoneNumber);
        editLocation = (TextView) findViewById(R.id.editLocation);
        editSubjects = (TextView) findViewById(R.id.editSubjects);
        editMessage = (TextView) findViewById(R.id.editMessage);
        btnReject = (Button) findViewById(R.id.btnReject);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        tb_status = (TableRow) findViewById(R.id.tb_status);
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

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetStatusNotificationAsystask setStatusNotificationAsystask = new SetStatusNotificationAsystask(SetInterviewDetailActivity.this, new StatusInterviewRequest(interviewObject.id, 3));
                setStatusNotificationAsystask.execute();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetStatusNotificationAsystask setStatusNotificationAsystask = new SetStatusNotificationAsystask(SetInterviewDetailActivity.this, new StatusInterviewRequest(interviewObject.id, 2));
                setStatusNotificationAsystask.execute();
            }
        });
    }

    public void BindData() {
        try {
            Picasso.with(SetInterviewDetailActivity.this).load(interviewObject.CompanyAvatar).placeholder(R.mipmap.img_logo_company).error(R.mipmap.default_avatar).into(img_photo_company);
        } catch (Exception e) {
            Picasso.with(SetInterviewDetailActivity.this).load(R.mipmap.default_avatar).into(img_photo_company);
        }
        txt_name_employee.setText(interviewObject.CompanyName);
        txt_location.setText(interviewObject.CompanyEmail);

        String[] dateInterview = interviewObject.InterviewDate.substring(0, interviewObject.InterviewDate.indexOf(" ")).split("-");
        dateTimeInterview = interviewObject.InterviewDate.substring(0, interviewObject.InterviewDate.indexOf(" "));
        editDatetimeInterview.setText(dateInterview[2] + "-" + dateInterview[1] + "-" + dateInterview[0]);
        startTime = interviewObject.Start_time;
        endTime = interviewObject.End_time;
        btn_start_time.setText(interviewObject.Start_time);
        btn_end_time.setText(interviewObject.End_time);
        editInterviewer.setText(interviewObject.ContactName);
        editPhoneNumber.setText(interviewObject.PhoneNumber);
        editLocation.setText(interviewObject.Location);
        editSubjects.setText(interviewObject.Title);
        editMessage.setText(interviewObject.Content);
        if(!interviewObject.Status.equals("1")){
            tb_status.setVisibility(View.GONE);
        }

    }

    class SetStatusNotificationAsystask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;
        String sessionId = "";
        StatusInterviewRequest request;
        Context ct;

        public SetStatusNotificationAsystask(Context ct, StatusInterviewRequest request) {
            this.ct = ct;
            this.request = request;

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                NotificationController controller = new NotificationController();
                return controller.updateInterviewStatus(request);
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
                    Toast.makeText(ct, code, Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("reload",1);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }

}
