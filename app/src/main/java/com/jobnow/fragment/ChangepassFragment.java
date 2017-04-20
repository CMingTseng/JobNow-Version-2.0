package com.jobnow.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jobnow.config.Config;
import com.jobnow.controller.UserController;
import com.jobnow.models.ChangePassRequest;
import com.jobnow.utils.Utils;
import com.jobnow.R;

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
