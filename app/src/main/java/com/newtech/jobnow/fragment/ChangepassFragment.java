package com.newtech.jobnow.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.acitvity.AppSettingActivity;
import com.newtech.jobnow.acitvity.MyApplication;
import com.newtech.jobnow.acitvity.SearchResultActivity;
import com.newtech.jobnow.adapter.JobListV2Adapter;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.UserController;
import com.newtech.jobnow.models.ChangePassRequest;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.JobListV2Reponse;
import com.newtech.jobnow.models.JobV2Object;
import com.newtech.jobnow.models.ProfileModel;
import com.newtech.jobnow.models.UserModel;
import com.newtech.jobnow.utils.Utils;
import com.newtech.jobnow.widget.CRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangepassFragment extends Fragment {
    private  EditText edtOldPassword,edtNewPassword,edtReEnterPassword;
    Button btnChangePass;
    String email="";
    public ChangepassFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_change_pass, container, false);
        initUI(rootView);
        event();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        email=sharedPreferences.getString(Config.KEY_EMAIL,"");
        return rootView;
    }

    private void event() {
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass= edtOldPassword.getText().toString();
                String newPass= edtNewPassword.getText().toString();
                String reEnterPass= edtReEnterPassword.getText().toString();
                if (oldPass.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.pleaseInputOldPass), Toast.LENGTH_SHORT).show();
                } else if (newPass.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.pleaseInputNewPass), Toast.LENGTH_SHORT).show();
                } else if (reEnterPass.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.pleaseInputReEnter), Toast.LENGTH_SHORT).show();
                } else if (!newPass.equals(reEnterPass)) {
                    Toast.makeText(getActivity(), getString(R.string.pleasediffrent), Toast.LENGTH_SHORT).show();
                }else {
                    ChangePassRequest changePassRequest = new ChangePassRequest(email, oldPass, newPass);
                    ChangePasswordAsystask changePasswordAsystask = new ChangePasswordAsystask(getActivity(), changePassRequest);
                    changePasswordAsystask.execute();
                }
            }
        });
    }

    private void initUI(View view) {
        btnChangePass=(Button) view.findViewById(R.id.btnChangePass);
        edtOldPassword=(EditText) view.findViewById(R.id.edtOldPassword);
        edtNewPassword=(EditText) view.findViewById(R.id.edtNewPassword);
        edtReEnterPassword=(EditText) view.findViewById(R.id.edtReEnterPassword);
        Utils.closeKeyboard(getActivity());

    }
    class ChangePasswordAsystask extends AsyncTask<Void,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        ChangePassRequest profileRequest;
        Context ct;
        public ChangePasswordAsystask(Context ct,ChangePassRequest profileRequest){
            this.ct=ct;
            this.profileRequest=profileRequest;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                UserController controller= new UserController();
                return controller.ChangePassword(profileRequest);
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
                    Toast.makeText(getActivity(), "Password updated successfully!", Toast.LENGTH_SHORT).show();
                    edtOldPassword.setText("");
                    edtNewPassword.setText("");
                    edtReEnterPassword.setText("");
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }
}
