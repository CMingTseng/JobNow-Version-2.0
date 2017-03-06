package com.newtech.jobnow.fragment;


import android.app.Dialog;
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

import com.newtech.jobnow.R;
import com.newtech.jobnow.acitvity.FeedbackActivity;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.FeedbackController;
import com.newtech.jobnow.models.FeedbackRequest;
import com.newtech.jobnow.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackFragment extends Fragment {
    EditText edtSubTitle, editDescription;
    Button btnSendFeedback;
    int user_id=0;
    public FeedbackFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        initUI(rootView);
        event();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        user_id=sharedPreferences.getInt(Config.KEY_ID,0);
        return rootView;
    }

    private void event() {
        btnSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtSubTitle.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), getString(R.string.pleaseInputsubject), Toast.LENGTH_SHORT).show();
                }else if(editDescription.getText().toString().trim().isEmpty()){
                    Toast.makeText(getActivity(), getString(R.string.pleaseInputdesFeedback), Toast.LENGTH_SHORT).show();
                }else {
                    FeedbackRequest request = new FeedbackRequest(edtSubTitle.getText().toString().trim(), editDescription.getText().toString(), user_id);
                    SetNewFeedbackAsystask setUpdateCategoryAsystask = new SetNewFeedbackAsystask(getActivity(), request);
                    setUpdateCategoryAsystask.execute();
                }
            }
        });
    }

    private void initUI(View view) {
        edtSubTitle = (EditText) view.findViewById(R.id.edtSubTitle);
        editDescription = (EditText) view.findViewById(R.id.editDescription);
        btnSendFeedback = (Button) view.findViewById(R.id.btnSendFeedback);
        Utils.closeKeyboard(getActivity());

    }
    class SetNewFeedbackAsystask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;
        String sessionId = "";
        FeedbackRequest feedbackRequest;
        Context ct;
        Dialog dialogs;
        int type;

        public SetNewFeedbackAsystask(Context ct, FeedbackRequest feedbackRequest) {
            this.ct = ct;
            this.feedbackRequest = feedbackRequest;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                FeedbackController controller = new FeedbackController();
                return controller.AddNewFeedback(feedbackRequest);
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
                if (!code.equals("")) {
                    Toast.makeText(getActivity(), "Thank you for your valuable feedback", Toast.LENGTH_SHORT).show();
                    edtSubTitle.setText("");
                    editDescription.setText("");
                }
            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }
}
