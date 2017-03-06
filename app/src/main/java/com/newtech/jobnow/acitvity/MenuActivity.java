package com.newtech.jobnow.acitvity;

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
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.google.gson.Gson;
import com.newtech.jobnow.R;
import com.newtech.jobnow.common.APICommon;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.controller.NotificationController;
import com.newtech.jobnow.fragment.AppliedJobListFragment;
import com.newtech.jobnow.fragment.InterviewFragment;
import com.newtech.jobnow.fragment.JobHiringFragment;
import com.newtech.jobnow.fragment.MainFragment;
import com.newtech.jobnow.fragment.ProfileFragment;
import com.newtech.jobnow.fragment.ProfileManagerFragment;
import com.newtech.jobnow.fragment.SaveJobListFragment;
import com.newtech.jobnow.fragment.ShorListCategoryFragment;
import com.newtech.jobnow.models.BaseResponse;
import com.newtech.jobnow.models.CreditsNumberResponse;
import com.newtech.jobnow.models.JobListReponse;
import com.newtech.jobnow.models.JobListRequest;
import com.newtech.jobnow.models.NotificationRequest;
import com.newtech.jobnow.models.UserModel;
import com.newtech.jobnow.widget.TabEntity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MenuActivity extends AppCompatActivity {
    ImageView img_menu,img_notification;
    DrawerLayout drawer;


    private CommonTabLayout tabbottom;
    private int[] mIconUnselectIds = {
            R.mipmap.ic_job_inactive, R.mipmap.ic_profile_bottom,
            R.mipmap.ic_shortlist_inactive, R.mipmap.ic_interview_inactive};
    private int[] mIconSelectIds = {
            R.mipmap.ic_job, R.mipmap.ic_profile_bottom_selected,
            R.mipmap.ic_shortlist, R.mipmap.ic_interview};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();
    NavigationView navigationView;
    RelativeLayout header_layout;
    private ProgressDialog progressDialog;
    ImageView img_avatar;
    TextView txt_emailUser;
    public static  TextView txtCount;
    public static TextView txt_numberCredits;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        try{
            SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
            String profile=sharedPreferences.getString(Config.KEY_USER_PROFILE,"");
            Gson gson= new Gson();
            userModel=gson.fromJson(profile,UserModel.class);
        }catch (Exception err){

        }

        InitUI();
        bindData();
        InitEvent();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void InitUI(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        img_menu=(ImageView) findViewById(R.id.img_menu);
        img_notification=(ImageView) findViewById(R.id.img_notification);
        tabbottom = (CommonTabLayout) findViewById(R.id.tabbottomManager);
        txtCount=(TextView) findViewById(R.id.txtCount);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.nav_postJobs);
        Spannable wordtoSpan1 = new SpannableString(tools.getTitle());
        wordtoSpan1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, wordtoSpan1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tools.setTitle(wordtoSpan1);

        View headerview = navigationView.getHeaderView(0);
        img_avatar=(ImageView) headerview.findViewById(R.id.img_avatar);
        txt_emailUser=(TextView) headerview.findViewById(R.id.txt_emailUser);
        header_layout = (RelativeLayout) headerview.findViewById(R.id.header_layout);
        txt_numberCredits=(TextView) headerview.findViewById(R.id.txt_numberCredits) ;

        txt_numberCredits.setText(userModel.creditNumber+"");
        txt_emailUser.setText(userModel.email);
        try {
            Picasso.with(MenuActivity.this).load(userModel.avatar).placeholder(R.mipmap.default_avatar).error(R.mipmap.default_avatar).into(img_avatar);
        }catch (Exception e){
            Picasso.with(MenuActivity.this).load(R.mipmap.default_avatar).into(img_avatar);
        }
    }

    public void InitEvent(){
        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int drawerLockMode = drawer.getDrawerLockMode(GravityCompat.START);
                if (drawer.isDrawerVisible(GravityCompat.START)
                        && (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MenuActivity.this,NotificationManagerActivity.class);
                startActivityForResult(intent,1);

            }
        });
        header_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabbottom.setCurrentTab(1);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_appSettings) {
                    Intent intent= new Intent(MenuActivity.this, AppSettingActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_getJobCredit) {
                    Intent intent= new Intent(MenuActivity.this, GetMoreJobCreditsActivity.class);
                    startActivity(intent);
                }else if (id == R.id.nav_privacyPolicy) {
                    Intent intent= new Intent(MenuActivity.this, PolicyActivity.class);
                    startActivity(intent);
                }else if (id == R.id.nav_feedback) {
                    Intent intent= new Intent(MenuActivity.this, FeedbackActivity.class);
                    startActivityForResult(intent,1);
                }else if (id == R.id.nav_termOfUse) {
                    Intent intent= new Intent(MenuActivity.this, TermsofUseActivity.class);
                    startActivityForResult(intent,1);
                }else if (id == R.id.nav_logOut) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MenuActivity.this);
                    builder1.setMessage("Are you sure you want to Log Out?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    logout();
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

                }else if (id == R.id.nav_postJobs) {
                    if(userModel.creditNumber>=1) {
                        final ProgressDialog progressDialog = ProgressDialog.show(MenuActivity.this, "Loading...", "Please wait", true, false);
                        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
                        Call<CreditsNumberResponse> getJobList = service.getCredits(
                                APICommon.getSign(APICommon.getApiKey(), JobListRequest.PATH_URL),
                                APICommon.getAppId(),
                                APICommon.getDeviceType(),
                                userModel.id
                        );
                        getJobList.enqueue(new Callback<CreditsNumberResponse>() {
                            @Override
                            public void onResponse(Response<CreditsNumberResponse> response, Retrofit retrofit) {
                                try {
                                    progressDialog.dismiss();
                                    if (response.body() != null && response.body().code == 200) {
                                        Float result = Float.parseFloat(response.body().result+"");
                                        txt_numberCredits.setText(result+"");
                                        Gson gson= new Gson();
                                        userModel.creditNumber=response.body().result;
                                        String profile=gson.toJson(userModel);
                                        SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(Config.KEY_USER_PROFILE, profile).commit();
                                        if(response.body().result>=1){
                                            Intent intent = new Intent(MenuActivity.this, PostAJobsActivity.class);
                                            startActivityForResult(intent,1);
                                        }else {
                                            Toast.makeText(MenuActivity.this,"Sorry, you have insufficient credits. Please purchase in Menu ---> Get More Job Credit",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(Throwable t) {
                                progressDialog.dismiss();
                            }
                        });


                    }else {
                        Toast.makeText(MenuActivity.this,"Sorry, you have insufficient credits. Please purchase in Menu ---> Get More Job Credit",Toast.LENGTH_LONG).show();
                    }
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void bindData() {
        CountNotificationAsystask countNotificationAsystask= new CountNotificationAsystask(MenuActivity.this,new NotificationRequest(0,userModel.id));
        countNotificationAsystask.execute();

        String[] mTitles = {getString(R.string.job), getString(R.string.profile), getString(R.string.shortlist), getString(R.string.interview)};
        for (int i = 0; i < mTitles.length; i++) {
            switch (i) {
                case 0:
                    mFragments2.add(new JobHiringFragment());
                    break;
                case 1:
                    mFragments2.add(new ProfileManagerFragment());
                    break;
                case 2:
                    mFragments2.add(new ShorListCategoryFragment());
                    break;
                case 3:
                    mFragments2.add(new InterviewFragment());
                    break;
                default:
                    break;

            }
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        tabbottom.setTabData(mTabEntities, this, R.id.fl_change, mFragments2);
        tabbottom.setCurrentTab(0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            try {
                Float result = Float.parseFloat(data.getStringExtra("credits"));
                txt_numberCredits.setText(result+"");
            }catch (Exception err){

            }
        }

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void logout() {
        progressDialog = ProgressDialog.show(MenuActivity.this, "", getString(R.string.loading), true, true);
        final SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);

        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<BaseResponse> call = service.getLogout(APICommon.getSign(APICommon.getApiKey(), "api/v1/users/getLogout"), APICommon.getAppId(), APICommon.getDeviceType(), userModel.id, userModel.apiToken);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    if (response.body().code == 200) {
                        Toast.makeText(MenuActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(MenuActivity.this, SplashScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish(); // call this to f
                    } else if (response.body().code == 503) {
                        Toast.makeText(MenuActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MenuActivity.this, getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

       /* if (System.currentTimeMillis() - key_pressed < 2000) {
            super.onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Back again to exit", Toast.LENGTH_SHORT).show();
        }
        key_pressed = System.currentTimeMillis();*/
    }
    static long key_pressed;

    class CountNotificationAsystask extends AsyncTask<Void, Void, Integer> {
        ProgressDialog dialog;
        String sessionId = "";
        NotificationRequest notificationRequest;
        Context ct;
        Dialog dialogs;

        public CountNotificationAsystask(Context ct, NotificationRequest notificationRequest) {
            this.ct = ct;
            this.notificationRequest = notificationRequest;

        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                NotificationController controller = new NotificationController();
                return controller.CountNotification(notificationRequest);
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ct);
            dialog.setMessage("");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Integer code) {
            try {
                if(code==0){
                    txtCount.setVisibility(View.GONE);
                }else {
                    txtCount.setText(code + "");
                }

            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }
}