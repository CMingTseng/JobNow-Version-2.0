package com.newtech.jobnow.acitvity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import java.util.Calendar;

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
import com.newtech.jobnow.controller.InviteController;
import com.newtech.jobnow.models.InviteRequest;
import com.newtech.jobnow.models.ProfileModel;
import com.newtech.jobnow.models.SetInterviewRequest;
import com.newtech.jobnow.models.ShortlistDetailObject;
import com.newtech.jobnow.models.UserModel;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 12/02/2017.
 */

public class SetInterviewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    RelativeLayout layout_invite;
    TextView txt_name_employee, txt_location;
    CustomTextViewHelveticaneuelight editDatetimeInterview;
    TextView btn_start_time, btn_end_time;
    EditText editInterviewer, editPhoneNumber, editLocation, editSubjects, editMessage;
    Button btnSendSetInterview;
    ShortlistDetailObject shortlistDetailObject;
    ImageView img_photo_company;

    private int mYear, mMonth, mDay, mHour, mMinute;

    String dateTimeInterview="";
    String startTime="";
    String endTime="";

    UserModel userModel;
    ProfileModel profileModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setinterview);

        try {
            shortlistDetailObject = (ShortlistDetailObject) getIntent().getSerializableExtra("jobseeker_v2");
        } catch (Exception err) {

        }
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref,MODE_PRIVATE);
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

        txt_name_employee = (TextView) findViewById(R.id.txt_name_employee);
        txt_location = (TextView) findViewById(R.id.txt_location);
        editDatetimeInterview = (CustomTextViewHelveticaneuelight) findViewById(R.id.editDatetimeInterview);
        btn_start_time = (TextView) findViewById(R.id.btn_start_time);
        btn_end_time = (TextView) findViewById(R.id.btn_end_time);

        editInterviewer = (EditText) findViewById(R.id.editInterviewer);
        editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        editLocation = (EditText) findViewById(R.id.editLocation);
        editSubjects = (EditText) findViewById(R.id.editSubjects);
        editMessage = (EditText) findViewById(R.id.editMessage);

        btnSendSetInterview = (Button) findViewById(R.id.btnSendSetInterview);
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



        editDatetimeInterview.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {

                switch (target) {
                    case LEFT:
                        //Do something here
                        break;
                    case RIGHT:
                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog datePickerDialog = new DatePickerDialog(SetInterviewActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        if(dayOfMonth<10){

                                            if(monthOfYear+1<10){
                                                editDatetimeInterview.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                                dateTimeInterview=year+ "-0" + (monthOfYear + 1) + "-" + "0"+dayOfMonth;
                                            }else {
                                                editDatetimeInterview.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                dateTimeInterview=year+ "-" + (monthOfYear + 1) + "-" + "0"+dayOfMonth;
                                            }
                                        }else {
                                            if(monthOfYear+1<10){
                                                editDatetimeInterview.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                                dateTimeInterview=year+ "-0" + (monthOfYear + 1) + "-"+dayOfMonth;
                                            }else {
                                                editDatetimeInterview.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                                dateTimeInterview=year+ "-" + (monthOfYear + 1) + "-"+dayOfMonth;
                                            }
                                        }


                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.show();

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
        btn_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog datePickerDialog = new TimePickerDialog(SetInterviewActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if(minute<10){
                                    if(hourOfDay<10){
                                        startTime="0"+hourOfDay+":0"+minute+" AM";
                                    }else if(hourOfDay<12){
                                        startTime="0"+hourOfDay+":0"+minute+" AM";
                                    }else {
                                        if(hourOfDay-12<10) {
                                            startTime = "0"+(hourOfDay-12) + ":0" + minute + " PM";
                                        }else {
                                            startTime = (hourOfDay-12) + ":0" + minute + " PM";
                                        }
                                    }
                                }else {
                                    if(hourOfDay<10){
                                        startTime="0"+hourOfDay+":"+minute+" AM";
                                    }else if(hourOfDay<12){
                                        startTime="0"+hourOfDay+":"+minute+" AM";
                                    }else {
                                        if(hourOfDay-12<10) {
                                            startTime = "0"+(hourOfDay-12) + ":" + minute + " PM";
                                        }else {
                                            startTime = (hourOfDay-12) + ":" + minute + " PM";
                                        }
                                    }
                                }

                                btn_start_time.setText(startTime);
                            }
                        }, mHour, mMinute,true);
                datePickerDialog.show();
            }
        });

        btn_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog datePickerDialog = new TimePickerDialog(SetInterviewActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if(minute<10){
                                    if(hourOfDay<10){
                                        endTime="0"+hourOfDay+":0"+minute+" AM";
                                    }else if(hourOfDay<12){
                                        endTime="0"+hourOfDay+":0"+minute+" AM";
                                    }else {
                                        if(hourOfDay-12<10) {
                                            endTime = "0"+(hourOfDay-12) + ":0" + minute + " PM";
                                        }else {
                                            endTime = (hourOfDay-12) + ":0" + minute + " PM";
                                        }
                                    }
                                }else {
                                    if(hourOfDay<10){
                                        endTime="0"+hourOfDay+":"+minute+" AM";
                                    }else if(hourOfDay<12){
                                        endTime="0"+hourOfDay+":"+minute+" AM";
                                    }else {
                                        if(hourOfDay-12<10) {
                                            endTime = "0"+(hourOfDay-12) + ":" + minute + " PM";
                                        }else {
                                            endTime = (hourOfDay-12) + ":" + minute + " PM";
                                        }
                                    }
                                }

                                btn_end_time.setText(endTime);
                            }
                        }, mHour, mMinute,true);
                datePickerDialog.show();
            }
        });

        btnSendSetInterview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SetInterviewActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_set_interview);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                TextView txt_setInterView=(TextView) dialog.findViewById(R.id.txt_setInterview);
                ImageView btn_confirm_send_interview=(ImageView) dialog.findViewById(R.id.btn_confirm_send_interview);

                btn_confirm_send_interview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = editSubjects.getText().toString();
                        String interview = editInterviewer.getText().toString();
                        String numberPhone = editPhoneNumber.getText().toString();
                        String location = editLocation.getText().toString();
                        String message = editMessage.getText().toString();

                        SetInterviewRequest request = new SetInterviewRequest(0, shortlistDetailObject.JobSeekerID, profileModel.CompanyID, title, message, dateTimeInterview, interview, numberPhone, 1, startTime, endTime, location);
                        SetNewInterviewAsystask setNewInterviewAsystask = new SetNewInterviewAsystask(SetInterviewActivity.this, request,dialog);
                        setNewInterviewAsystask.execute();
                    }
                });

            }
        });
    }

    public void BindData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String userProfile = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        String companyProfile = sharedPreferences.getString(Config.KEY_COMPANY_PROFILE, "");
        Gson gson = new Gson();

        try {
            Picasso.with(SetInterviewActivity.this).load(shortlistDetailObject.Avatar).placeholder(R.mipmap.img_logo_company).error(R.mipmap.default_avatar).into(img_photo_company);
        } catch (Exception e) {
            Picasso.with(SetInterviewActivity.this).load(R.mipmap.default_avatar).into(img_photo_company);
        }
        txt_name_employee.setText(shortlistDetailObject.FullName);
        txt_location.setText(shortlistDetailObject.CountryName);
    }

    class SetNewInterviewAsystask extends AsyncTask<Void,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        SetInterviewRequest profileRequest;
        Context ct;
        Dialog dialogs;
        public SetNewInterviewAsystask(Context ct,SetInterviewRequest profileRequest,Dialog dialogs){
            this.ct=ct;
            this.profileRequest=profileRequest;
            this.dialogs=dialogs;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                InterviewController controller= new InterviewController();
                return controller.SetInterviewTime(profileRequest);
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
                    Toast.makeText(SetInterviewActivity.this, code, Toast.LENGTH_SHORT).show();
                    dialogs.dismiss();
                    finish();
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
}
