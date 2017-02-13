package com.newtech.jobnow.acitvity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.newtech.jobnow.R;
import com.newtech.jobnow.config.Config;
import com.newtech.jobnow.fragment.AppliedJobListFragment;
import com.newtech.jobnow.fragment.InterviewFragment;
import com.newtech.jobnow.fragment.JobHiringFragment;
import com.newtech.jobnow.fragment.MainFragment;
import com.newtech.jobnow.fragment.ProfileFragment;
import com.newtech.jobnow.fragment.ProfileManagerFragment;
import com.newtech.jobnow.fragment.SaveJobListFragment;
import com.newtech.jobnow.fragment.ShorListCategoryFragment;
import com.newtech.jobnow.widget.TabEntity;

import java.util.ArrayList;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem tools= menu.findItem(R.id.nav_appSettings);
        Spannable wordtoSpan1 = new SpannableString(tools.getTitle());
        wordtoSpan1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, wordtoSpan1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tools.setTitle(wordtoSpan1);

        View headerview = navigationView.getHeaderView(0);
        header_layout = (RelativeLayout) headerview.findViewById(R.id.header_layout);
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
                }else if (id == R.id.nav_feedback) {
                    Intent intent= new Intent(MenuActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                }else if (id == R.id.nav_logOut) {
                    finish();
                    Intent intent= new Intent(MenuActivity.this,SplashScreen.class);
                    startActivity(intent);
                    SharedPreferences sharedPreferences = getSharedPreferences(Config.Pref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Config.KEY_COMPANYID,0).commit();
                    editor.putString(Config.KEY_USER_PROFILE, "").commit();
                    editor.putString(Config.KEY_COMPANY_PROFILE, "").commit();
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void bindData() {

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

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
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
}