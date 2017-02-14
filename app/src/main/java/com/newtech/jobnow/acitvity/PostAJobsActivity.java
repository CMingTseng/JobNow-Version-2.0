package com.newtech.jobnow.acitvity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.newtech.jobnow.R;
import com.newtech.jobnow.adapter.CategoryIndustryAdapter;
import com.newtech.jobnow.adapter.IndustryAdapter;
import com.newtech.jobnow.adapter.LevelAdapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.common.MultiSpinner;
import com.newtech.jobnow.controller.UserController;
import com.newtech.jobnow.models.CategoryIndustryObject;
import com.newtech.jobnow.models.CategoryIndustryResponse;
import com.newtech.jobnow.models.ForgotRequest;
import com.newtech.jobnow.models.IndustryObject;
import com.newtech.jobnow.models.IndustryResponse;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.LevelObject;
import com.newtech.jobnow.models.LevelResponse;
import com.newtech.jobnow.models.SkillObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private EditText edtEmail;
    private Button btnForgot;
    private TextView txt_des_forgot_pass,txt_status;
    private Toolbar toolbar;
    MultiSpinner spSkills;
    private LevelAdapter spJobLevelAdapter;
    private CategoryIndustryAdapter spJobCategoryAdapter;
    Spinner spJobLevel,spJobCategory;
    List<CategoryIndustryObject> listCategoryIndustry = new ArrayList<>();
    TextView btn_startdate,btn_enddate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    String startDateTimeInterview="",endDateTimeInterview="";
    SupportMapFragment mapFragment;
    double latitude, longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);
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
        getLevel();
        getJobCategory();
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

        spJobCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<SkillObject> listSkill=listCategoryIndustry.get(position).data;
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
    }

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
                    List<LevelObject> list = new ArrayList<>();
                    if (response.body().result != null && response.body().result.size() > 0)
                        list.addAll(response.body().result);
                    spJobLevelAdapter = new LevelAdapter(PostAJobsActivity.this, list);
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

    // AsynTask
    class ForgotAsystask extends AsyncTask<ForgotRequest,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        ForgotRequest request;
        Context ct;
        public ForgotAsystask(Context ct,ForgotRequest request){
            this.ct=ct;
            this.request=request;
        }

        @Override
        protected String doInBackground(ForgotRequest... params) {
            try {
                UserController controller= new UserController();
                return controller.ForgotPass(request);
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
                if(code.equals("")) {
                    txt_status.setVisibility(View.VISIBLE);
                    txt_des_forgot_pass.setVisibility(View.VISIBLE);
                    String textCondition= "A email has been send to "+edtEmail.getText().toString().trim()+", \nplease check email to reset password. Thank you!!!";
                    Spannable wordtoSpan1 = new SpannableString(textCondition);
                    int index=textCondition.indexOf(edtEmail.getText().toString().trim());
                    wordtoSpan1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), index, index+edtEmail.getText().toString().trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    txt_des_forgot_pass.setText(wordtoSpan1);
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }


}
