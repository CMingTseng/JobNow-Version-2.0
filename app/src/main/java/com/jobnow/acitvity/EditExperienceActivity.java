package com.jobnow.acitvity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.ExperienceRequest;
import com.jobnow.models.ExperienceResponse;
import com.jobnow.models.LoginResponse;
import com.jobnow.models.TokenRequest;
import com.jobnow.R;

import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class EditExperienceActivity extends AppCompatActivity implements View.OnClickListener {

    private ExperienceResponse.Experience experience;
    private Button btnSave, btnCancel, btnRemove;
    private TextView btn_start_time,btn_end_time;
    private EditText edCompanyName, edJobOrPosition, edDescription,editSalary;
    private String companyName, location, description;

    public String fromDate,toDate;
    public Double salary;
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String TAG = EditExperienceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_experience);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Edit Experience");

        Intent intent = getIntent();
        experience = (ExperienceResponse.Experience) intent.getSerializableExtra("experience");
        initUI();
        bindData();
        event();
    }

    public void getApiToken() {
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
        String email = sharedPreferences.getString(Config.KEY_EMAIL, "");
        Call<LoginResponse> call = service.getToken(new TokenRequest(userId, email));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                Log.d(TAG, "get token: " + response.body());
                if(response.body() != null && response.body().code == 200) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Config.KEY_TOKEN, response.body().result.apiToken);
                    editor.commit();
                } else
                    Toast.makeText(getApplicationContext(), response.body().message,
                            Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.error_connect, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void event() {
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        btn_start_time.setOnClickListener(this);
        btn_end_time.setOnClickListener(this);
    }

    private void bindData() {
        edCompanyName.setText(experience.CompanyName);
        edJobOrPosition.setText(experience.PositionName);
        edDescription.setText(experience.Description);
        editSalary.setText(experience.Salary+"");

        salary=experience.Salary;

        String []startTime=experience.FromDate.substring(0,experience.FromDate.indexOf(" ")).split("-");
        fromDate=experience.FromDate.substring(0,experience.FromDate.indexOf(" "));
        btn_start_time.setText(startTime[2]+"-"+startTime[1]+"-"+startTime[0]);

        String []endTime=experience.ToDate.substring(0,experience.ToDate.indexOf(" ")).split("-");
        toDate=experience.ToDate.substring(0,experience.ToDate.indexOf(" "));
        btn_end_time.setText(endTime[2]+"-"+endTime[1]+"-"+endTime[0]);
    }

    private void initUI() {
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        edCompanyName = (EditText) findViewById(R.id.edCompanyName);
        edJobOrPosition = (EditText) findViewById(R.id.edJobOrPosition);
        edDescription = (EditText) findViewById(R.id.edDescription);
        editSalary=(EditText) findViewById(R.id.editSalary);
        btn_start_time=(TextView) findViewById(R.id.btn_start_time);
        btn_end_time=(TextView) findViewById(R.id.btn_end_time);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                companyName = edCompanyName.getText().toString();
                location = edJobOrPosition.getText().toString();
                description = edDescription.getText().toString();
                salary=Double.parseDouble(editSalary.getText().toString());
                if (companyName == null || companyName.isEmpty()) {
                    Toast.makeText(this, getString(R.string.pleaseInputCompanyName), Toast.LENGTH_SHORT).show();
                } else if (location == null || location.isEmpty()) {
                    Toast.makeText(this, getString(R.string.pleaseInputJobOrPosition), Toast.LENGTH_SHORT).show();
                } else if (description == null || description.isEmpty()) {
                    Toast.makeText(this, getString(R.string.pleaseInputDescription), Toast.LENGTH_SHORT).show();
                } else {
                    save();
                }
                break;
            case R.id.btn_end_time:
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditExperienceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (dayOfMonth < 10) {

                                    if (monthOfYear + 1 < 10) {
                                        btn_end_time.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        toDate = year + "-0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                    } else {
                                        btn_end_time.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        toDate = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                    }
                                } else {
                                    if (monthOfYear + 1 < 10) {
                                        btn_end_time.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        toDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    } else {
                                        btn_end_time.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        toDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    }
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.btn_start_time:
                final Calendar cs = Calendar.getInstance();
                mYear = cs.get(Calendar.YEAR);
                mMonth = cs.get(Calendar.MONTH);
                mDay = cs.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialogs = new DatePickerDialog(EditExperienceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (dayOfMonth < 10) {

                                    if (monthOfYear + 1 < 10) {
                                        btn_start_time.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        fromDate = year + "-0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                    } else {
                                        btn_start_time.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        fromDate = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                    }
                                } else {
                                    if (monthOfYear + 1 < 10) {
                                        btn_start_time.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        fromDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    } else {
                                        btn_start_time.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        fromDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    }
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialogs.show();
                break;
            case R.id.btnRemove:
                companyName = edCompanyName.getText().toString();
                location = edJobOrPosition.getText().toString();
                description = edDescription.getText().toString();
                delete();
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
    }

    private void delete() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
        int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<BaseResponse> call = service.postDeleteJobSeekerExperience(new ExperienceRequest(experience.id, userId, token, userId));
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    if (response.body().code == 200) {
                        setResult(RESULT_OK);
                        finish();
                    } else if(response.body().code == 503) {
                        getApiToken();
                        Toast.makeText(EditExperienceActivity.this, response.body().message,
                                Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(EditExperienceActivity.this, response.body().message,
                                Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(EditExperienceActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void save() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
        int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<BaseResponse> call = service.postUpdateJobSeekerExperience(new ExperienceRequest(userId, companyName, location, description, token, userId, experience.id, ExperienceRequest.UPDATE,fromDate,toDate,salary));
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {
                Toast.makeText(EditExperienceActivity.this, response.body().message,
                        Toast.LENGTH_SHORT).show();
                if (response.body() != null) {
                    if (response.body().code == 200) {
                        setResult(RESULT_OK);
                        finish();
                    } else if(response.body().code == 503) {
                        getApiToken();
                        Toast.makeText(EditExperienceActivity.this, response.body().message,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditExperienceActivity.this, response.body().message,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(EditExperienceActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
