package com.jobnow.acitvity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jobnow.adapter.IndustryAdapter;
import com.jobnow.adapter.JobLocationAdapter;
import com.jobnow.adapter.JobLocationV2Adapter;
import com.jobnow.common.APICommon;
import com.jobnow.models.IndustryObject;
import com.jobnow.models.IndustryResponse;
import com.jobnow.models.JobListRequest;
import com.jobnow.models.LocationObject;
import com.jobnow.models.LocationResponse;
import com.jobnow.utils.Utils;
import com.jobnow.widget.DisableScrollRecyclerView;
import com.jobnow.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class FilterActivity extends AppCompatActivity {
    private String TAG = FilterActivity.class.getSimpleName();
    private IndustryAdapter industryAdapter;
    private Spinner spnIndustry;
    private DisableScrollRecyclerView rvJobLocation;
    //rvSkill;
    private JobLocationAdapter jobLocationAdapter;
    private JobLocationV2Adapter jobLocationV2Adapter;
//    private SkillAdapter skillAdapter;
    private Button btnFilter;
    private TextView tvReset;
    private EditText edtTitle, edtMinSalary;

    private void initUI() {

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }
        spnIndustry = (Spinner) findViewById(R.id.spnIndustry);
        rvJobLocation = (DisableScrollRecyclerView) findViewById(R.id.rvJobLocation);
        btnFilter = (Button) findViewById(R.id.btnFilter);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtMinSalary = (EditText) findViewById(R.id.edtMinimumSalary);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        rvJobLocation.setLayoutManager(layoutManager);
        rvJobLocation.setHasFixedSize(true);
        rvJobLocation.setItemAnimator(new DefaultItemAnimator());
        //jobLocationAdapter = new JobLocationAdapter(FilterActivity.this, new ArrayList<JobLocationResponse.JobLocation>());
        jobLocationV2Adapter = new JobLocationV2Adapter(FilterActivity.this, new ArrayList<LocationObject>());
        rvJobLocation.setAdapter(jobLocationV2Adapter);

        tvReset = (TextView) findViewById(R.id.tvReset);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initUI();
        bindData();
        initevent();

        Utils.closeKeyboard(FilterActivity.this);
    }

    private String getLocationParseFromLocations() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jobLocationV2Adapter.getItemCount(); i++) {
            LocationObject location = jobLocationV2Adapter.getItembyPostion(i);
            if (location != null) {
                if (location.isChecked) {
                    if (sb.toString().equals("")) {
                        sb.append(location.id);
                    } else {
                        sb.append(',');
                        sb.append(location.id);
                    }
                }
            }
        }
        Log.d(TAG, "location : " + sb.toString());
        return sb.toString();
    }
    private Integer convertFromString(String number) {
        Integer result;
        try {
            result = Integer.parseInt(number);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        }
    }

    private void clearSelectData() {
        //skill
        spnIndustry.setSelection(0);
        for(int i = 0; i < jobLocationV2Adapter.getItemCount(); i++) {
            LocationObject jobLocation = jobLocationV2Adapter.getItembyPostion(i);
            if(jobLocation.isChecked) {
                jobLocation.isChecked = false;
                jobLocationV2Adapter.setData(i, jobLocation);
            }
        }
        edtMinSalary.setText("");
        edtTitle.setText("");
    }

    private void initevent() {
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString();
                if (title.equals(""))
                    title = null;

                Integer minSalary = convertFromString(edtMinSalary.getText().toString());

                IndustryObject industry = industryAdapter.getItem(
                        spnIndustry.getSelectedItemPosition());

                Integer industryId = industry.id;

//                String skill = getSkillParseFromSkills();
                String location = getLocationParseFromLocations();
                Random randomno = new Random();
                JobListRequest request = new JobListRequest(1, "DESC", title, location, "",
                        minSalary, null, null, industryId,randomno.nextInt(10000));

                Intent returnIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(SearchResultActivity.KEY_JOB, request);
                returnIntent.putExtras(bundle);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelectData();
            }
        });
    }

    private void bindData() {
        getIndustry();
        getJobLocation();
    }
    private void getJobLocation() {
        final ProgressDialog progressDialog = ProgressDialog.show(FilterActivity.this,
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
                        jobLocationV2Adapter.addAll(response.body().result);

                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(FilterActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getIndustry() {
        final ProgressDialog progressDialog = ProgressDialog.show(FilterActivity.this,
                "Loading", "Please wait..", true, false);
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        String url = APICommon.BASE_URL + "industry/getListIndustry/" + APICommon.getSign(APICommon.getApiKey(), "api/v1/industry/getListIndustry")
                + "/" + APICommon.getAppId() + "/" + APICommon.getDeviceType();
        Call<IndustryResponse> industryResponseCall = service.getIndustry(url);
        industryResponseCall.enqueue(new Callback<IndustryResponse>() {
            @Override
            public void onResponse(Response<IndustryResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {
                    List<IndustryObject> industryObjects = new ArrayList<>();
                    IndustryObject industryObject = new IndustryObject();
                    industryObject.id = 0;
                    industryObject.Name = getString(R.string.AllSpecializations);
                    industryObjects.add(industryObject);
                    if (response.body().result != null && response.body().result.size() > 0)
                        industryObjects.addAll(response.body().result);
                    industryAdapter = new IndustryAdapter(FilterActivity.this, industryObjects);
                    spnIndustry.setAdapter(industryAdapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(FilterActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
