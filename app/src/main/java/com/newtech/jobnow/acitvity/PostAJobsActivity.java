package com.newtech.jobnow.acitvity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.adapter.CategoryIndustryAdapter;
import com.newtech.jobnow.adapter.EmploymentTypeAdapter;
import com.newtech.jobnow.adapter.IndustryAdapter;
import com.newtech.jobnow.adapter.LevelAdapter;
import com.newtech.jobnow.adapter.ListExperienceAdapter;
import com.newtech.jobnow.adapter.LocationAdapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.common.MultiSpinner;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.JobController;
import com.newtech.jobnow.controller.UserController;
import com.newtech.jobnow.models.CategoryIndustryObject;
import com.newtech.jobnow.models.CategoryIndustryResponse;
import com.newtech.jobnow.models.EmployeeResponse;
import com.newtech.jobnow.models.EmploymentObject;
import com.newtech.jobnow.models.ExperienceObject;
import com.newtech.jobnow.models.ExperienceResponse;
import com.newtech.jobnow.models.ForgotRequest;
import com.newtech.jobnow.models.IndustryObject;
import com.newtech.jobnow.models.IndustryResponse;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.JobObject;
import com.newtech.jobnow.models.LevelObject;
import com.newtech.jobnow.models.LevelResponse;
import com.newtech.jobnow.models.ListEmploymentResponse;
import com.newtech.jobnow.models.ListExperienceResponse;
import com.newtech.jobnow.models.LocationObject;
import com.newtech.jobnow.models.LocationResponse;
import com.newtech.jobnow.models.PostJobRequest;
import com.newtech.jobnow.models.PostJobResponse;
import com.newtech.jobnow.models.SkillObject;
import com.newtech.jobnow.models.UserModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 07/02/2017.
 */

public class PostAJobsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    ImageView img_back;
    TextView btnSetDone;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText editJobTitle, edtSalaryFrom, edtSalaryTo, editResponsibilities, editRequiment,edtWorkinghour;
    private CheckBox ckShowSalary;
    private Toolbar toolbar;
    private Button btnSavePostJobs;
    MultiSpinner spSkills;
    private LevelAdapter spJobLevelAdapter;
    private ListExperienceAdapter spJobExperienceAdapter;
    private CategoryIndustryAdapter spJobCategoryAdapter;
    private LocationAdapter spJobLocationAdapter;
    private EmploymentTypeAdapter spEmploymentAdapter;
    Spinner spJobLevel, spJobCategory, spExperience, spJobLocation, spEmploymentType;
    List<CategoryIndustryObject> listCategoryIndustry = new ArrayList<>();
    List<LevelObject> listLevelObjects = new ArrayList<>();
    List<ExperienceObject> listExperienceObjects = new ArrayList<>();
    List<SkillObject> listSkill = new ArrayList<>();
    List<EmploymentObject> listEmployment = new ArrayList<>();
    List<LocationObject> locationObjectList = new ArrayList<>();
    TextView btn_startdate, btn_enddate;
    RadioButton active, deactive;

    private int mYear, mMonth, mDay, mHour, mMinute;
    String startDateTimeInterview = "", endDateTimeInterview = "";
    SupportMapFragment mapFragment;
    double latitude, longitude;
    TextView txt_titleLocation;
    String addressName = "";

    EmploymentObject employmentObject;
    LocationObject locationObject;
    CategoryIndustryObject categoryIndustryObject;
    LevelObject levelObject;
    SkillObject skillObject;
    ExperienceObject experienceObject;
    UserModel userModel;
    String skillList = "";
    JobObject jobObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String profile = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        Gson gson = new Gson();
        userModel = gson.fromJson(profile, UserModel.class);
        InitUI();
        InitEvent();
        try {
            jobObject = (JobObject) getIntent().getSerializableExtra("job_detail");
            SetData(jobObject);

        } catch (Exception err) {

        }

    }

    public void InitUI() {
        img_back = (ImageView) findViewById(R.id.img_back);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map_postjob);
        mapFragment.getMapAsync(this);

        btnSetDone = (TextView) findViewById(R.id.btnSetDone);
        btn_enddate = (TextView) findViewById(R.id.btn_enddate);
        btn_startdate = (TextView) findViewById(R.id.btn_startdate);

        spJobLevel = (Spinner) findViewById(R.id.spJobLevel);
        spJobCategory = (Spinner) findViewById(R.id.spJobCategory);
        spExperience = (Spinner) findViewById(R.id.spExperience);
        spJobLocation = (Spinner) findViewById(R.id.spJobLocation);
        spEmploymentType = (Spinner) findViewById(R.id.spEmploymentType);
        getLevel();
        getJobCategory();
        getJobExperience();
        getJobLocation();
        getEmploymentType();
        editJobTitle = (EditText) findViewById(R.id.editJobTitle);
        edtSalaryFrom = (EditText) findViewById(R.id.edtSalaryFrom);
        edtSalaryTo = (EditText) findViewById(R.id.edtSalaryTo);
        editResponsibilities = (EditText) findViewById(R.id.editResponsibilities);
        editRequiment = (EditText) findViewById(R.id.editRequiment);
        edtWorkinghour=(EditText) findViewById(R.id.edtWorkinghour);

        ckShowSalary = (CheckBox) findViewById(R.id.ckShowSalary);
        active = (RadioButton) findViewById(R.id.active);
        deactive = (RadioButton) findViewById(R.id.deactive);

    }

    public void InitEvent() {
        /*click vào nut home tren toolbar*/
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        spJobLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                levelObject = listLevelObjects.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spExperience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                experienceObject = listExperienceObjects.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spJobLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locationObject = locationObjectList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spEmploymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employmentObject = listEmployment.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spJobCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                skillList = "";
                List<Integer> listChoose = null;

                categoryIndustryObject = listCategoryIndustry.get(position);
                listSkill = listCategoryIndustry.get(position).data;
                String skillChoose = "Select All Skill";
                if (jobObject != null) {
                    try {
                        skillList = "";
                        listChoose = new ArrayList<Integer>();
                        String[] listSkillChoose = jobObject.SkillList.split(",");
                        for (int i = 0; i < listSkill.size(); i++) {
                            for (int j = 0; j < listSkillChoose.length; j++) {
                                if (listSkillChoose[j].equals(listSkill.get(i).id + "")) {
                                    listChoose.add(i);
                                    skillList = skillList + listSkill.get(i).Name + ",";
                                    break;
                                }
                            }
                        }
                        skillList = skillList.substring(0, skillList.length() - 1);
                    } catch (Exception err) {

                    }
                }

                final List<String> items = new ArrayList<>();
                for (int i = 0; i < listSkill.size(); i++) {
                    items.add(listSkill.get(i).Name);
                }
                spSkills = (MultiSpinner) findViewById(R.id.spSkills);

                spSkills.setItems(items, skillChoose, listChoose, new MultiSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {
                        skillList="";
                        for (int i = 0; i < selected.length; i++) {
                            if (selected[i]) {
                                skillList = skillList + listSkill.get(i).id + ",";
                            }
                        }
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PostAJobsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (dayOfMonth < 10) {

                                    if (monthOfYear + 1 < 10) {
                                        btn_startdate.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        startDateTimeInterview = year + "-0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                    } else {
                                        btn_startdate.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        startDateTimeInterview = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                    }
                                } else {
                                    if (monthOfYear + 1 < 10) {
                                        btn_startdate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        startDateTimeInterview = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    } else {
                                        btn_startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        startDateTimeInterview = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    }
                                }
                                btn_enddate.setText(AddDate(startDateTimeInterview));
                                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                                Calendar c = Calendar.getInstance();
                                try {
                                    c.setTime(sdf.parse(AddDate(startDateTimeInterview)));
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                                    endDateTimeInterview= sdf1.format(c.getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });



        btn_enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PostAJobsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (dayOfMonth < 10) {

                                    if (monthOfYear + 1 < 10) {
                                        btn_enddate.setText("0" + dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        endDateTimeInterview = year + "-0" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                    } else {
                                        btn_enddate.setText("0" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        endDateTimeInterview = year + "-" + (monthOfYear + 1) + "-" + "0" + dayOfMonth;
                                    }
                                } else {
                                    if (monthOfYear + 1 < 10) {
                                        btn_enddate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        endDateTimeInterview = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    } else {
                                        btn_enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        endDateTimeInterview = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                    }
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();*/
            }
        });
        btnSetDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CheckOK()) {
                    String jobTitle = editJobTitle.getText().toString();

                    String jobSalaryFrom = edtSalaryFrom.getText().toString();
                    String jobSalaryTo = edtSalaryTo.getText().toString();
                    String jobResponsibilities = editResponsibilities.getText().toString();
                    String jobRequiment = editRequiment.getText().toString();
                    String jobStartDate = btn_startdate.getText().toString();
                    String jobEndDate = btn_enddate.getText().toString();

                    if (skillList.length() > 1) {
                        skillList = skillList.substring(0, skillList.length() - 1);
                    }
                    if (jobObject != null) {
                        PostJobRequest request = new PostJobRequest(jobObject.id, jobTitle, "jobnow", 1, "", locationObject.id, categoryIndustryObject.id, Float.parseFloat(jobSalaryFrom), Float.parseFloat(jobSalaryTo), 2, ckShowSalary.isChecked() == false ? 0 : 1, levelObject.id, Float.parseFloat(latitude + ""), Float.parseFloat(longitude + ""), jobResponsibilities, jobRequiment, startDateTimeInterview, endDateTimeInterview, active.isChecked() == true ? 1 : 0, "", skillList, experienceObject.id + "", userModel.id, userModel.apiToken,employmentObject.id,Integer.parseInt(edtWorkinghour.getText().toString()));
                        EditPostAJobAsyntask postAJobAsyntask = new EditPostAJobAsyntask(PostAJobsActivity.this, request);
                        postAJobAsyntask.execute();
                    } else {
                        PostJobRequest request = new PostJobRequest(jobTitle, "jobnow", 1, "", locationObject.id, categoryIndustryObject.id, Float.parseFloat(jobSalaryFrom), Float.parseFloat(jobSalaryTo), 2, ckShowSalary.isChecked() == false ? 0 : 1, levelObject.id, Float.parseFloat(latitude + ""), Float.parseFloat(longitude + ""), jobResponsibilities, jobRequiment, startDateTimeInterview, endDateTimeInterview, active.isChecked() == true ? 1 : 0, "", skillList, experienceObject.id + "", userModel.id, userModel.apiToken,employmentObject.id,Integer.parseInt(edtWorkinghour.getText().toString()));
                        PostAJobAsyntask postAJobAsyntask = new PostAJobAsyntask(PostAJobsActivity.this, request);
                        postAJobAsyntask.execute();
                    }

                }
            }
        });

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active.setTextColor(getResources().getColor(R.color.white));
                deactive.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        deactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active.setTextColor(getResources().getColor(R.color.colorPrimary));
                deactive.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }

    public String AddDate(String startDate){
        String dt = startDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 30);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        return sdf1.format(c.getTime());
    }

    public void SetData(JobObject jobObject) {
        editJobTitle.setText(jobObject.Title);
        edtSalaryFrom.setText(jobObject.FromSalary);
        edtSalaryTo.setText(jobObject.ToSalary);
        editResponsibilities.setText(jobObject.Description);
        editRequiment.setText(jobObject.Requirement);
        ckShowSalary.setChecked(jobObject.IsDisplaySalary == 0 ? false : true);

        String[] startDate = jobObject.Start_date.substring(0, jobObject.Start_date.indexOf(" ")).split("-");
        startDateTimeInterview = startDate[0] + "-" + startDate[1] + "-" + startDate[2];
        btn_startdate.setText(startDate[2] + "-" + startDate[1] + "-" + startDate[0]);

        String[] endDate = jobObject.End_date.substring(0, jobObject.End_date.indexOf(" ")).split("-");
        endDateTimeInterview = endDate[0] + "-" + endDate[1] + "-" + endDate[2];
        btn_enddate.setText(endDate[2] + "-" + endDate[1] + "-" + endDate[0]);
        edtWorkinghour.setText(jobObject.WorkingHours+"");
        if (jobObject.IsActive == 0) {
            active.setTextColor(getResources().getColor(R.color.colorPrimary));
            deactive.setTextColor(getResources().getColor(R.color.white));
            deactive.setChecked(true);
        } else {
            active.setChecked(true);
        }


    }

    /*
    * Check OK: Check dữ liệu trên form
    * */
    public boolean CheckOK() {

        String jobTitle = editJobTitle.getText().toString();
        String jobSalaryFrom = edtSalaryFrom.getText().toString();
        String jobSalaryTo = edtSalaryTo.getText().toString();
        String jobResponsibilities = editResponsibilities.getText().toString();
        String jobRequiment = editRequiment.getText().toString();
        String jobStartDate = btn_startdate.getText().toString();
        String edtWorking = edtWorkinghour.getText().toString();
        if (jobTitle.isEmpty()) {
            Toast.makeText(PostAJobsActivity.this, "Please Input A Job Title", Toast.LENGTH_SHORT).show();
            return false;
        } else if (startDateTimeInterview.equals("")) {
            Toast.makeText(PostAJobsActivity.this, "Please Input Start Date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (endDateTimeInterview.equals("")) {
            Toast.makeText(PostAJobsActivity.this, "Please Input End Date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (jobSalaryFrom.isEmpty() || Float.parseFloat(jobSalaryFrom) < 0) {
            Toast.makeText(PostAJobsActivity.this, "Please Input Salary From", Toast.LENGTH_SHORT).show();
            return false;
        } else if (jobSalaryTo.isEmpty() || Float.parseFloat(jobSalaryTo) < 0) {
            Toast.makeText(PostAJobsActivity.this, "Please Input Salary From", Toast.LENGTH_SHORT).show();
            return false;
        } else if (Float.parseFloat(jobSalaryTo) < Float.parseFloat(jobSalaryFrom)) {
            Toast.makeText(PostAJobsActivity.this, "Salary From Is Less Than ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (jobResponsibilities.isEmpty()) {
            Toast.makeText(PostAJobsActivity.this, "Please Input A Responsibilities", Toast.LENGTH_SHORT).show();
            return false;
        } else if (jobRequiment.isEmpty()) {
            Toast.makeText(PostAJobsActivity.this, "Please Input A Requirements", Toast.LENGTH_SHORT).show();
            return false;
        } else if (skillList.equals("")) {
            Toast.makeText(PostAJobsActivity.this, "Please Select A Skill", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtWorking.equals("")) {
            Toast.makeText(PostAJobsActivity.this, "Please Input Working hour number", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    /*-------------------------End Function--------------------------*/

    /*
    get level
    */
    private void getLevel() {
        final ProgressDialog progressDialog = ProgressDialog.show(PostAJobsActivity.this,
                "Loading", "Please wait..", true, false);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();

        String url = APICommon.BASE_URL + "jobs/getListLevel/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/jobs/getListLevel")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType();

        Call<LevelResponse> industryResponseCall = service.getListLevel(url);
        industryResponseCall.enqueue(new Callback<LevelResponse>() {
            @Override
            public void onResponse(Response<LevelResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {

                    if (response.body().result != null && response.body().result.size() > 0)
                        listLevelObjects.addAll(response.body().result);
                    spJobLevelAdapter = new LevelAdapter(PostAJobsActivity.this, listLevelObjects);
                    spJobLevel.setAdapter(spJobLevelAdapter);

                    if (jobObject != null) {
                        for (int i = 0; i < listLevelObjects.size(); i++) {
                            if (listLevelObjects.get(i).id == jobObject.JobLevelID) {
                                spJobLevel.setSelection(i);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PostAJobsActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getJobCategory() {
        final ProgressDialog progressDialog = ProgressDialog.show(PostAJobsActivity.this,
                "Loading", "Please wait..", true, false);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();

        String url = APICommon.BASE_URL + "jobs/getIndustry/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/jobs/getIndustry")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType();

        Call<CategoryIndustryResponse> industryResponseCall = service.getListCategoryIndustry(url);
        industryResponseCall.enqueue(new Callback<CategoryIndustryResponse>() {
            @Override
            public void onResponse(Response<CategoryIndustryResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {

                    if (response.body().result != null && response.body().result.size() > 0)
                        listCategoryIndustry.addAll(response.body().result);
                    spJobCategoryAdapter = new CategoryIndustryAdapter(PostAJobsActivity.this, listCategoryIndustry);
                    spJobCategory.setAdapter(spJobCategoryAdapter);
                    if (jobObject != null) {
                        for (int i = 0; i < listCategoryIndustry.size(); i++) {
                            if (listCategoryIndustry.get(i).id == jobObject.IndustryID) {
                                spJobCategory.setSelection(i);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PostAJobsActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getJobExperience() {
        final ProgressDialog progressDialog = ProgressDialog.show(PostAJobsActivity.this,
                "Loading", "Please wait..", true, false);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();

        String url = APICommon.BASE_URL + "users/getListExperience/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/jobs/getIndustry")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType();

        Call<ListExperienceResponse> experienceResponseCall = service.getListExperience(url);
        experienceResponseCall.enqueue(new Callback<ListExperienceResponse>() {
            @Override
            public void onResponse(Response<ListExperienceResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {

                    if (response.body().result != null && response.body().result.size() > 0)
                        listExperienceObjects.addAll(response.body().result);
                    spJobExperienceAdapter = new ListExperienceAdapter(PostAJobsActivity.this, listExperienceObjects);
                    spExperience.setAdapter(spJobExperienceAdapter);

                    if (jobObject != null) {
                        for (int i = 0; i < listExperienceObjects.size(); i++) {
                            if (listExperienceObjects.get(i).id == jobObject.ExperienceID) {
                                spExperience.setSelection(i);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PostAJobsActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getJobLocation() {
        final ProgressDialog progressDialog = ProgressDialog.show(PostAJobsActivity.this,
                "Loading", "Please wait..", true, false);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();

        String url = APICommon.BASE_URL + "location/getAllLocationOnCountry/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/location/getAllLocationOnCountry")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType() + "/0";

        Call<LocationResponse> experienceResponseCall = service.getListLocation(url);
        experienceResponseCall.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Response<LocationResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {

                    if (response.body().result != null && response.body().result.size() > 0)
                        locationObjectList.addAll(response.body().result);
                    spJobLocationAdapter = new LocationAdapter(PostAJobsActivity.this, locationObjectList);
                    spJobLocation.setAdapter(spJobLocationAdapter);

                    if (jobObject != null) {
                        for (int i = 0; i < locationObjectList.size(); i++) {
                            if (locationObjectList.get(i).id == jobObject.LocationID) {
                                spJobLocation.setSelection(i);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PostAJobsActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getEmploymentType() {
        final ProgressDialog progressDialog = ProgressDialog.show(PostAJobsActivity.this,
                "Loading", "Please wait..", true, false);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();

        String url = APICommon.BASE_URL + "employment/getListEmployment/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/employment/getListEmployment")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType();

        Call<ListEmploymentResponse> experienceResponseCall = service.getListEmployment(url);
        experienceResponseCall.enqueue(new Callback<ListEmploymentResponse>() {
            @Override
            public void onResponse(Response<ListEmploymentResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {

                    if (response.body().result != null && response.body().result.size() > 0)
                        listEmployment.addAll(response.body().result);
                    spEmploymentAdapter = new EmploymentTypeAdapter(PostAJobsActivity.this, listEmployment);
                    spEmploymentType.setAdapter(spEmploymentAdapter);

                    if (jobObject != null) {
                        for (int i = 0; i < listEmployment.size(); i++) {
                            if (listEmployment.get(i).id == jobObject.EmploymentID) {
                                spEmploymentType.setSelection(i);
                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PostAJobsActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addMarkerChoice(LatLng latLng) {
        try {
            latitude = latLng.latitude;
            longitude = latLng.longitude;
            Location location = new Location("Location set job");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            addressName = GetAddress(latLng.latitude, latLng.longitude);
            addMaker(location);
        } catch (Exception e) {
        }
    }

    public void addMaker(Location location) {
        try {
            mMap.clear();
            // create marker
            MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");
            // Changing marker icon
            marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker));
            // adding marker
            mMap.addMarker(marker);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setTrafficEnabled(true);
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Location location;
        Boolean isGPSEnabled = false;
        Boolean isNetworkEnabled = false;
        mMap = googleMap;

        try {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    //latLng_new = latLng;
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                    addMarkerChoice(latLng);

                }
            });
        } catch (Exception exx) {

        }
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(PostAJobsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(PostAJobsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        // getting GPS status
        isGPSEnabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = service
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            location = service
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                if (jobObject != null) {
                    addMarkerChoice(new LatLng(jobObject.Latitude, jobObject.Longitude));
                } else {
                    addMarkerChoice(new LatLng(latitude, longitude));
                }
            } else {
                addMarkerChoice(new LatLng(0, 0));
            }
        }
        if (isNetworkEnabled) {
            location = service
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }

                if (jobObject != null) {
                    addMarkerChoice(new LatLng(jobObject.Latitude, jobObject.Longitude));
                } else {
                    addMarkerChoice(new LatLng(latitude, longitude));
                }
            } else {
                addMarkerChoice(new LatLng(0, 0));
            }
        }
    }

    public String GetAddress(double lati, double longti) {
        try {
            Geocoder geo = new Geocoder(PostAJobsActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(lati, longti, 1);
            if (addresses.isEmpty()) {
                return "Waiting for Location";
            } else {
                if (addresses.size() > 0) {
                    return (addresses.get(0).getFeatureName() + ", " + addresses.get(0).getAdminArea() + ", "/* + ", " + addresses.get(0).getLocality() */ + addresses.get(0).getCountryName());
                    //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        return "";
    }

    // AsynTask
    class PostAJobAsyntask extends AsyncTask<PostJobRequest, Void, PostJobResponse> {
        ProgressDialog dialog;
        String sessionId = "";
        PostJobRequest request;
        Context ct;

        public PostAJobAsyntask(Context ct, PostJobRequest request) {
            this.ct = ct;
            this.request = request;
        }

        @Override
        protected PostJobResponse doInBackground(PostJobRequest... params) {
            try {
                JobController controller = new JobController();
                return controller.PostAJobs(request);
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
        protected void onPostExecute(PostJobResponse code) {
            try {
                Toast.makeText(PostAJobsActivity.this, "Congratulations! Job Posting is created successfully", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                Gson gson = new Gson();
                userModel.creditNumber = Float.parseFloat(code.message + "");
                String profile = gson.toJson(userModel);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.KEY_USER_PROFILE, profile).commit();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("results", 150293);
                returnIntent.putExtra("credits", code.message);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }

    class EditPostAJobAsyntask extends AsyncTask<PostJobRequest, Void, String> {
        ProgressDialog dialog;
        String sessionId = "";
        PostJobRequest request;
        Context ct;

        public EditPostAJobAsyntask(Context ct, PostJobRequest request) {
            this.ct = ct;
            this.request = request;
        }

        @Override
        protected String doInBackground(PostJobRequest... params) {
            try {
                JobController controller = new JobController();
                return controller.EditPostAJobs(request);
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
                Toast.makeText(PostAJobsActivity.this, "Congratulations! Job Posting is created successfully", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("results", 150293);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }

}
