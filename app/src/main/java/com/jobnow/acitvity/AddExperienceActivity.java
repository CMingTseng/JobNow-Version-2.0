package com.jobnow.acitvity;

import android.app.DatePickerDialog;
import android.content.Context;
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
import com.jobnow.models.LoginResponse;
import com.jobnow.models.TokenRequest;
import com.newtech.jobnow.R;

import java.util.Calendar;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddExperienceActivity extends AppCompatActivity {
    private static final String TAG = AddExperienceActivity.class.getSimpleName();
    private EditText edCompanyName, edJobOrPosition, edDescription,editSalary;
    private TextView btn_start_time,btn_end_time;
    private Button btnCancel, btnSave;
    public String companyName, location, description;
    public String fromDate,toDate;
    public Double salary;
    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Add Experience");
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);

        edCompanyName = (EditText) findViewById(R.id.edCompanyName);
        edJobOrPosition = (EditText) findViewById(R.id.edJobOrPosition);
        edDescription = (EditText) findViewById(R.id.edDescription);
        editSalary=(EditText) findViewById(R.id.editSalary);
        btn_start_time=(TextView) findViewById(R.id.btn_start_time);
        btn_end_time=(TextView) findViewById(R.id.btn_end_time);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddExperienceActivity.this,
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
                datePickerDialog.show();
            }
        });

        btn_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddExperienceActivity.this,
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
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                companyName = edCompanyName.getText().toString();
                location = edJobOrPosition.getText().toString();
                description = edDescription.getText().toString();
                salary=Double.parseDouble(editSalary.getText().toString());
                if (companyName == null || companyName.isEmpty()) {
                    Toast.makeText(AddExperienceActivity.this, getString(R.string.pleaseInputCompanyName), Toast.LENGTH_SHORT).show();
                } else if (location == null || location.isEmpty()) {
                    Toast.makeText(AddExperienceActivity.this, getString(R.string.pleaseInputJobOrPosition), Toast.LENGTH_SHORT).show();
                } else if (fromDate == null || fromDate.isEmpty()) {
                    Toast.makeText(AddExperienceActivity.this, getString(R.string.pleaseInputFromDate), Toast.LENGTH_SHORT).show();
                } else if (toDate == null || toDate.isEmpty()) {
                    Toast.makeText(AddExperienceActivity.this, getString(R.string.pleaseInputEndDate), Toast.LENGTH_SHORT).show();
                } else if (salary == null || salary==0) {
                    Toast.makeText(AddExperienceActivity.this, getString(R.string.pleaseInputSalary), Toast.LENGTH_SHORT).show();
                }else if (description == null || description.isEmpty()) {
                    Toast.makeText(AddExperienceActivity.this, getString(R.string.pleaseInputDescription), Toast.LENGTH_SHORT).show();
                } else {
                    addExepience();
                }

            }
        });
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
                    addExepience();
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

    private void addExepience() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
        int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<BaseResponse> call = service.postAddJobSeekerExperience(new ExperienceRequest(userId, companyName, location, description, token, userId,fromDate,toDate,salary));
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {
                if (response.body() != null) {
                    Toast.makeText(AddExperienceActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    if (response.body().code == 200) {
                        setResult(RESULT_OK);
                        finish();
                    } else if(response.body().code == 503) {
                        getApiToken();
                    }
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(AddExperienceActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
