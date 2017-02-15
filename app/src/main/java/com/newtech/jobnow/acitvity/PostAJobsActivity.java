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
import com.newtech.jobnow.adapter.IndustryAdapter;
import com.newtech.jobnow.adapter.LevelAdapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.common.MultiSpinner;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.JobController;
import com.newtech.jobnow.controller.UserController;
import com.newtech.jobnow.models.CategoryIndustryObject;
import com.newtech.jobnow.models.CategoryIndustryResponse;
import com.newtech.jobnow.models.ForgotRequest;
import com.newtech.jobnow.models.IndustryObject;
import com.newtech.jobnow.models.IndustryResponse;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.LevelObject;
import com.newtech.jobnow.models.LevelResponse;
import com.newtech.jobnow.models.PostJobRequest;
import com.newtech.jobnow.models.SkillObject;
import com.newtech.jobnow.models.UserModel;

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
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText editJobTitle,edtPosition,edtSalaryFrom,edtSalaryTo,editResponsibilities,editRequiment;
    private CheckBox ckShowSalary;
    private Toolbar toolbar;
    private Button btnSavePostJobs;
    MultiSpinner spSkills;
    private LevelAdapter spJobLevelAdapter;
    private CategoryIndustryAdapter spJobCategoryAdapter;
    Spinner spJobLevel,spJobCategory;
    List<CategoryIndustryObject> listCategoryIndustry = new ArrayList<>();
    List<LevelObject> listLevelObjects = new ArrayList<>();
    List<SkillObject> listSkill= new ArrayList<>();
    TextView btn_startdate,btn_enddate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String startDateTimeInterview="",endDateTimeInterview="";
    SupportMapFragment mapFragment;
    double latitude, longitude;
    TextView txt_titleLocation;
    String addressName="";

    CategoryIndustryObject categoryIndustryObject;
    LevelObject levelObject;
    SkillObject skillObject;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String profile=sharedPreferences.getString(Config.KEY_USER_PROFILE,"");
        Gson gson= new Gson();
        userModel=gson.fromJson(profile,UserModel.class);

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
        actionBar.setTitle("Post A Jobs");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_back);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map_postjob);
        mapFragment.getMapAsync(this);

        btn_enddate=(TextView) findViewById(R.id.btn_enddate);
        btn_startdate=(TextView) findViewById(R.id.btn_startdate);

        spJobLevel=(Spinner) findViewById(R.id.spJobLevel);
        spJobCategory=(Spinner) findViewById(R.id.spJobCategory);
        txt_titleLocation=(TextView) findViewById(R.id.txt_titleLocation);
        getLevel();
        getJobCategory();

        editJobTitle=(EditText) findViewById(R.id.editJobTitle);
        edtPosition=(EditText) findViewById(R.id.edtPosition);
        edtSalaryFrom=(EditText) findViewById(R.id.edtSalaryFrom);
        edtSalaryTo=(EditText) findViewById(R.id.edtSalaryTo);
        editResponsibilities=(EditText) findViewById(R.id.editResponsibilities);
        editRequiment=(EditText) findViewById(R.id.editRequiment);

        ckShowSalary=(CheckBox) findViewById(R.id.ckShowSalary);
        btnSavePostJobs=(Button) findViewById(R.id.btnSavePostJobs);
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

        spJobLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                levelObject=listLevelObjects.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spJobCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryIndustryObject= listCategoryIndustry.get(position);
                listSkill=listCategoryIndustry.get(position).data;
                List<String> items= new ArrayList<>();
                for (int i=0; i<listSkill.size();i++){
                    items.add(listSkill.get(i).Name);
                }
                spSkills = (MultiSpinner) findViewById(R.id.spSkills);
                spSkills.setItems(items, "Select All Skill", new MultiSpinner.MultiSpinnerListener() {
                    @Override
                    public void onItemsSelected(boolean[] selected) {

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
                                if(dayOfMonth<10){

                                    if(monthOfYear+1<10){
                                        btn_startdate.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        startDateTimeInterview=year+ "-0" + (monthOfYear + 1) + "-" + "0"+dayOfMonth;
                                    }else {
                                        btn_startdate.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        startDateTimeInterview=year+ "-" + (monthOfYear + 1) + "-" + "0"+dayOfMonth;
                                    }
                                }else {
                                    if(monthOfYear+1<10){
                                        btn_startdate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        startDateTimeInterview=year+ "-0" + (monthOfYear + 1) + "-"+dayOfMonth;
                                    }else {
                                        btn_startdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        startDateTimeInterview=year+ "-" + (monthOfYear + 1) + "-"+dayOfMonth;
                                    }
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btn_enddate.setOnClickListener(new View.OnClickListener() {
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
                                if(dayOfMonth<10){

                                    if(monthOfYear+1<10){
                                        btn_enddate.setText("0"+dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        endDateTimeInterview=year+ "-0" + (monthOfYear + 1) + "-" + "0"+dayOfMonth;
                                    }else {
                                        btn_enddate.setText("0"+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        endDateTimeInterview=year+ "-" + (monthOfYear + 1) + "-" + "0"+dayOfMonth;
                                    }
                                }else {
                                    if(monthOfYear+1<10){
                                        btn_enddate.setText(dayOfMonth + "-0" + (monthOfYear + 1) + "-" + year);
                                        endDateTimeInterview=year+ "-0" + (monthOfYear + 1) + "-"+dayOfMonth;
                                    }else {
                                        btn_enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                        endDateTimeInterview=year+ "-" + (monthOfYear + 1) + "-"+dayOfMonth;
                                    }
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        btnSavePostJobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CheckOK()){
                    String jobTitle=editJobTitle.getText().toString();
                    String jobPosition=edtPosition.getText().toString();
                    String jobSalaryFrom=edtSalaryFrom.getText().toString();
                    String jobSalaryTo=edtSalaryTo.getText().toString();
                    String jobResponsibilities=editResponsibilities.getText().toString();
                    String jobRequiment=editRequiment.getText().toString();
                    String jobStartDate=btn_startdate.getText().toString();
                    String jobEndDate=btn_enddate.getText().toString();

                    PostJobRequest request= new PostJobRequest(jobTitle,jobPosition,1,"",1,categoryIndustryObject.id,Float.parseFloat(jobSalaryFrom),Float.parseFloat(jobSalaryTo),2,ckShowSalary.isChecked()==false?0:1,levelObject.id,Float.parseFloat(latitude+""),Float.parseFloat(longitude+""),jobResponsibilities,jobRequiment,startDateTimeInterview,endDateTimeInterview,1,userModel.id,userModel.apiToken);
                    PostAJobAsyntask postAJobAsyntask= new PostAJobAsyntask(PostAJobsActivity.this,request);
                    postAJobAsyntask.execute();
                }
            }
        });
    }
    /*
    * Check OK: Check dữ liệu trên form
    * */
    public boolean CheckOK(){

        String jobTitle=editJobTitle.getText().toString();
        String jobPosition=edtPosition.getText().toString();
        String jobSalaryFrom=edtSalaryFrom.getText().toString();
        String jobSalaryTo=edtSalaryTo.getText().toString();
        String jobResponsibilities=editResponsibilities.getText().toString();
        String jobRequiment=editRequiment.getText().toString();
        String jobStartDate=btn_startdate.getText().toString();
        String jobEndDate=btn_enddate.getText().toString();
        if(jobTitle.isEmpty()){
            Toast.makeText(PostAJobsActivity.this, "Please Input A Job Title", Toast.LENGTH_SHORT).show();
            return false;
        }else if(jobPosition.isEmpty()){
            Toast.makeText(PostAJobsActivity.this,"Please Input A Position", Toast.LENGTH_SHORT).show();
            return false;
        }else if(startDateTimeInterview.equals("")){
            Toast.makeText(PostAJobsActivity.this,"Please Input Start Date", Toast.LENGTH_SHORT).show();
            return false;
        }else if(endDateTimeInterview.equals("")){
            Toast.makeText(PostAJobsActivity.this,"Please Input End Date", Toast.LENGTH_SHORT).show();
            return false;
        } else if(jobSalaryFrom.isEmpty()|| Float.parseFloat(jobSalaryFrom)<0){
            Toast.makeText(PostAJobsActivity.this,"Please Input Salary From", Toast.LENGTH_SHORT).show();
            return false;
        }else if(jobSalaryTo.isEmpty()|| Float.parseFloat(jobSalaryTo)<0){
            Toast.makeText(PostAJobsActivity.this,"Please Input Salary From", Toast.LENGTH_SHORT).show();
            return false;
        }else if(Float.parseFloat(jobSalaryTo)<Float.parseFloat(jobSalaryFrom)){
            Toast.makeText(PostAJobsActivity.this,"Salary From Is Less Than ", Toast.LENGTH_SHORT).show();
            return false;
        }else if(jobResponsibilities.isEmpty()){
            Toast.makeText(PostAJobsActivity.this,"Please Input A Responsibilities", Toast.LENGTH_SHORT).show();
            return false;
        }else if(jobRequiment.isEmpty()){
            Toast.makeText(PostAJobsActivity.this,"Please Input A Requirements", Toast.LENGTH_SHORT).show();
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
            Location location = new Location("you");
            location.setLatitude(latLng.latitude);
            location.setLongitude(latLng.longitude);
            addressName=GetAddress(latLng.latitude,latLng.longitude);
            txt_titleLocation.setText(addressName);
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
        }catch (Exception exx){

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
                addMarkerChoice(new LatLng(latitude, longitude));
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

                addMarkerChoice(new LatLng(latitude, longitude));
            } else {
                addMarkerChoice(new LatLng(0, 0));
            }
        }
    }
    public String GetAddress(double lati, double longti){
        try {
            Geocoder geo = new Geocoder(PostAJobsActivity.this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(lati, longti, 1);
            if (addresses.isEmpty()) {
                return "Waiting for Location";
            }
            else {
                if (addresses.size() > 0) {
                    return (addresses.get(0).getFeatureName() +", "+ addresses.get(0).getAdminArea() +", "/* + ", " + addresses.get(0).getLocality() */+ addresses.get(0).getCountryName());
                    //Toast.makeText(getApplicationContext(), "Address:- " + addresses.get(0).getFeatureName() + addresses.get(0).getAdminArea() + addresses.get(0).getLocality(), Toast.LENGTH_LONG).show();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace(); // getFromLocation() may sometimes fail
        }
        return "";
    }

    // AsynTask
    class PostAJobAsyntask extends AsyncTask<PostJobRequest,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        PostJobRequest request;
        Context ct;
        public PostAJobAsyntask(Context ct,PostJobRequest request){
            this.ct=ct;
            this.request=request;
        }

        @Override
        protected String doInBackground(PostJobRequest... params) {
            try {
                JobController controller= new JobController();
                return controller.PostAJobs(request);
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
                Toast.makeText(PostAJobsActivity.this, code, Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("results",150293);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }


}
