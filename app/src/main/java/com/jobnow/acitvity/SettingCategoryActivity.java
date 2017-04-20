package com.jobnow.acitvity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jobnow.common.CustomEditextHelveticaneuelight;
import com.jobnow.common.DrawableClickListener;
import com.jobnow.config.Config;
import com.jobnow.controller.CategoryController;
import com.jobnow.models.CategoryRequest;
import com.jobnow.models.UserModel;
import com.jobnow.utils.Utils;
import com.jobnow.R;

/**
 * Created by Administrator on 12/02/2017.
 */

public class SettingCategoryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    CustomEditextHelveticaneuelight editTitleCategory;
    Button btnChange,btnDelete;

    UserModel userModel;
    int category_id=0;
    String category_name="";
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_category);
        try {
            category_id=getIntent().getIntExtra("category_id",0);
            category_name=getIntent().getStringExtra("category_name");
        }catch (Exception err){

        }
        InitUI();
        InitEvent();
        BindData();
    }

    public void InitUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_sign_in);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Material Search");
        toolbar.setTitleTextColor(Color.parseColor("#0c69d3"));

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.get_more_job_credits));
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_back);

        Utils.closeKeyboard(SettingCategoryActivity.this);
        editTitleCategory= (CustomEditextHelveticaneuelight)findViewById(R.id.editTitleCategory);
        btnChange=(Button) findViewById(R.id.btnChange);
        btnDelete=(Button) findViewById(R.id.btnDelete);
    }

    public void InitEvent() {
        /*click vào nut home tren toolbar*/
        View view = toolbar.getChildAt(0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("results",category_name);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryRequest categoryRequest= new CategoryRequest(editTitleCategory.getText().toString().trim(),userModel.id,category_id);
                SetUpdateCategoryAsystask setUpdateCategoryAsystask= new SetUpdateCategoryAsystask(SettingCategoryActivity.this,categoryRequest,0);
                setUpdateCategoryAsystask.execute();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingCategoryActivity.this);
                builder1.setMessage("Confirm to delete this category");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                CategoryRequest categoryRequest= new CategoryRequest(editTitleCategory.getText().toString().trim(),userModel.id,category_id);
                                SetUpdateCategoryAsystask setUpdateCategoryAsystask= new SetUpdateCategoryAsystask(SettingCategoryActivity.this,categoryRequest,1);
                                setUpdateCategoryAsystask.execute();
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });

        editTitleCategory.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        //Do something here
                        break;
                    case RIGHT:
                        if(!flag) {
                            editTitleCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pencil, 0);
                            editTitleCategory.setSelection(editTitleCategory.getText().toString().trim().length());
                            editTitleCategory.setEnabled(true);
                            flag=true;
                        }else{
                            editTitleCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_pencil_inactive, 0);
                            editTitleCategory.setEnabled(false);
                            flag=false;
                        }
                        break;
                    case TOP:
                        //Do something here
                        break;
                    case BOTTOM:
                        //Do something here
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void BindData() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
        String userProfile = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        Gson gson = new Gson();
        userModel=gson.fromJson(userProfile,UserModel.class);

        editTitleCategory.setText(category_name);
        editTitleCategory.setEnabled(false);


    }

    class SetUpdateCategoryAsystask extends AsyncTask<Void,Void,String> {
        ProgressDialog dialog;
        String sessionId="";
        CategoryRequest categoryRequest;
        Context ct;
        Dialog dialogs;
        int type;
        public SetUpdateCategoryAsystask(Context ct, CategoryRequest categoryRequest, int type){
            this.ct=ct;
            this.categoryRequest=categoryRequest;
            this.type=type;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                CategoryController controller= new CategoryController();
                if(type==0) {
                    // update
                    return controller.UpCatgory(categoryRequest);
                }else{
                    return controller.DeleteCatgory(categoryRequest);
                }
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
                    Toast.makeText(SettingCategoryActivity.this, code, Toast.LENGTH_SHORT).show();
                    if(type!=0){
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",15);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }else {
                        category_name=editTitleCategory.getText().toString();
                    }
                }
            }catch (Exception e){
            }
            dialog.dismiss();
        }
    }

}
