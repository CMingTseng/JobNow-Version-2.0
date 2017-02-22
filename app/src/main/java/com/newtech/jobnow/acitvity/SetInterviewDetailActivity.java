package com.newtech.jobnow.acitvity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.common.CustomTextViewHelveticaneuelight;
import com.newtech.jobnow.common.DrawableClickListener;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.InterviewController;
import com.newtech.jobnow.models.InterviewObject;
import com.newtech.jobnow.models.ProfileModel;
import com.newtech.jobnow.models.SetInterviewRequest;
import com.newtech.jobnow.models.ShortlistDetailObject;
import com.newtech.jobnow.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

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

    private int mYear, mMonth, mDay, mHour, mMinute;

    String dateTimeInterview = "";
    String startTime = "";
    String endTime = "";

    UserModel userModel,profile;
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
    }

    public void BindData() {
            try {
                Picasso.with(SetInterviewDetailActivity.this).load(interviewObject.Avatar).placeholder(R.mipmap.img_logo_company).error(R.mipmap.default_avatar).into(img_photo_company);
            } catch (Exception e) {
                Picasso.with(SetInterviewDetailActivity.this).load(R.mipmap.default_avatar).into(img_photo_company);
            }
            txt_name_employee.setText(interviewObject.FullName);
            txt_location.setText(interviewObject.CountryName);

            String[] dateInterview = interviewObject.InterviewDate.substring(0, interviewObject.InterviewDate.indexOf(" ")).split("-");
            dateTimeInterview = interviewObject.InterviewDate.substring(0, interviewObject.InterviewDate.indexOf(" "));
            editDatetimeInterview.setText(dateInterview[2] + "-" + dateInterview[1] + "-" + dateInterview[0]);
            startTime=interviewObject.Start_time;
            endTime=interviewObject.End_time;
            btn_start_time.setText(interviewObject.Start_time);
            btn_end_time.setText(interviewObject.End_time);
            editInterviewer.setText(interviewObject.ContactName);
            editPhoneNumber.setText(interviewObject.PhoneNumber);
            editLocation.setText(interviewObject.Location);
            editSubjects.setText(interviewObject.Title);
            editMessage.setText(interviewObject.Content);

    }

}
