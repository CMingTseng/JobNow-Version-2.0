package com.jobnow.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jobnow.acitvity.EditExperienceActivity;
import com.jobnow.acitvity.MyApplication;
import com.jobnow.acitvity.ProfileVer2Activity;
import com.jobnow.adapter.ExperienceAdapter;
import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.eventbus.EditExperienceEvent;
import com.jobnow.models.ExperienceResponse;
import com.jobnow.widget.CRecyclerView;
import com.jobnow.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserExperienceFragment extends Fragment{

    private static final String TAG = UserExperienceFragment.class.getSimpleName();
    private CRecyclerView rvExperience;
    private ExperienceAdapter adapter;
    private ProgressDialog progressDialog;

    public UserExperienceFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_experience, container, false);
        initUI(rootView);
        bindData();
        return rootView;
    }

    private void bindData() {
        progressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading), true, true);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, MODE_PRIVATE);
        int userId = ProfileVer2Activity.idJobSeeker;
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<ExperienceResponse> call = service.getUserExperience(
                APICommon.getSign(APICommon.getApiKey(), "api/v1/jobseekerexperience/getAllUserExperience"),
                APICommon.getAppId(), APICommon.getDeviceType(), userId);
        call.enqueue(new Callback<ExperienceResponse>() {
            @Override
            public void onResponse(Response<ExperienceResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null && response.body().code == 200) {
                    adapter.clear();
                    adapter.addAll(response.body().result);
                } else if(response.body().code == 503) {
                    MyApplication.getInstance().getApiToken(new MyApplication.TokenCallback() {
                        @Override
                        public void onTokenSuccess() {
                            bindData();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initUI(View rootView) {
        rvExperience = (CRecyclerView) rootView.findViewById(R.id.rvExperience);
        rvExperience.setDivider();
        adapter = new ExperienceAdapter(getActivity(), new ArrayList<ExperienceResponse.Experience>(),1);
        rvExperience.setAdapter(adapter);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                bindData();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDetach();
    }

    @Subscribe
    public void onEvent(EditExperienceEvent event) {
        Intent intent = new Intent(getActivity(), EditExperienceActivity.class);
        intent.putExtra("experience", event.experience);
        startActivityForResult(intent, 1);
    }
}
