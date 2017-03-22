package com.jobnow.acitvity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobnow.fragment.ExperienceFragment;
import com.jobnow.fragment.MyProfileFragment;
import com.jobnow.fragment.ProfileFragment;
import com.jobnow.fragment.SkillsFragment;
import com.jobnow.fragment.UserExperienceFragment;
import com.jobnow.fragment.UserProfileFragment;
import com.jobnow.fragment.UserSkillsFragment;
import com.newtech.jobnow.R;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Administrator on 12/02/2017.
 */

public class ProfileVer2Activity extends AppCompatActivity {
    private String TAG = ProfileFragment.class.getSimpleName();
    private ViewPager viewPager;

    public int[] tabs() {
        return new int[]{
                R.string.myProfile,
                R.string.experience,
                R.string.skills
        };
    }

    private LinearLayout tab1, tab2, tab3;
    private ImageView ic_tab1, ic_tab2, ic_tab3;
    private TextView custom_text1, custom_text2, custom_text3;
    private int tabSelected = 0;
    public static CircleImageView img_avatar;
    public static TextView tvName, tvLocation;
    public static String emailJobSeeker;
    public static int idJobSeeker,interviewID;
    ImageView img_back;
    private ProgressDialog progressDialog;
    public static Button btnSetInterviewTime,btnNotSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_jobseeker);

        try{
            emailJobSeeker=getIntent().getStringExtra("emailJobSeeker");
            idJobSeeker=getIntent().getIntExtra("idJobSeeker",0);
            interviewID=getIntent().getIntExtra("interviewID",0);
        }catch (Exception err){

        }

        initUI();
        bindData();
        initEvent();
    }


    private void initEvent() {

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabSelected = 0;
                setPager();
            }
        });
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabSelected = 1;
                setPager();
            }
        });
        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabSelected = 2;
                setPager();
            }
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabSelected = position;
                setTabSelected();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPager() {
        viewPager.setCurrentItem(tabSelected);
        setTabSelected();
    }

    private void setTabSelected() {
        switch (tabSelected) {
            case 0:
                tab1.setBackgroundColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.colorPrimary));
                tab2.setBackgroundResource(R.drawable.bg_tab_profile);
                tab3.setBackgroundResource(R.drawable.bg_tab_profile);

                ic_tab1.setImageResource(R.mipmap.ic_profile_tab_selected);
                custom_text1.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.white));
                ic_tab2.setImageResource(R.mipmap.ic_exprience);
                custom_text2.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.black));
                ic_tab3.setImageResource(R.mipmap.ic_skill);
                custom_text3.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.black));
                new MyProfileFragment();
                break;
            case 1:
                tab2.setBackgroundColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.colorPrimary));
                tab1.setBackgroundResource(R.drawable.bg_tab_profile);
                tab3.setBackgroundResource(R.drawable.bg_tab_profile);

                ic_tab1.setImageResource(R.mipmap.ic_profile_tab);
                custom_text1.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.black));
                ic_tab2.setImageResource(R.mipmap.ic_exprience_selected);
                custom_text2.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.white));
                ic_tab3.setImageResource(R.mipmap.ic_skill);
                custom_text3.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.black));
                break;
            case 2:
                tab3.setBackgroundColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.colorPrimary));
                tab1.setBackgroundResource(R.drawable.bg_tab_profile);
                tab2.setBackgroundResource(R.drawable.bg_tab_profile);

                ic_tab1.setImageResource(R.mipmap.ic_profile_tab);
                custom_text1.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.black));
                ic_tab2.setImageResource(R.mipmap.ic_exprience);
                custom_text2.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.black));
                ic_tab3.setImageResource(R.mipmap.ic_skill_selected);
                custom_text3.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.white));
                new ExperienceFragment();
                break;
            default:
                tab1.setBackgroundColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.colorPrimary));
                tab2.setBackgroundResource(R.drawable.bg_tab_profile);
                tab3.setBackgroundResource(R.drawable.bg_tab_profile);

                ic_tab1.setImageResource(R.mipmap.ic_profile_tab_selected);
                custom_text1.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.white));
                ic_tab2.setImageResource(R.mipmap.ic_exprience);
                custom_text2.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.black));
                ic_tab3.setImageResource(R.mipmap.ic_skill);
                custom_text3.setTextColor(ContextCompat.getColor(ProfileVer2Activity.this, R.color.black));
                new SkillsFragment();
                break;
        }
    }

    private void bindData() {
        new MyProfileFragment();
        MyPagerAdapter adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setOffscreenPageLimit(3);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new UserProfileFragment();
                case 1:
                    return new UserExperienceFragment();
                case 2:
                    return new UserSkillsFragment();
                default:
                    return new UserProfileFragment();
            }
        }


        public CharSequence getPageTitle(int position) {
            return getString(tabs()[position]);
        }

    }


    private void initUI() {
        img_back = (ImageView) findViewById(R.id.img_back);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tab1 = (LinearLayout) findViewById(R.id.tab1);
        tab2 = (LinearLayout) findViewById(R.id.tab2);
        tab3 = (LinearLayout) findViewById(R.id.tab3);

        ic_tab1 = (ImageView) findViewById(R.id.ic_tab1);
        ic_tab2 = (ImageView) findViewById(R.id.ic_tab2);
        ic_tab3 = (ImageView) findViewById(R.id.ic_tab3);


        custom_text1 = (TextView) findViewById(R.id.custom_text1);
        custom_text2 = (TextView) findViewById(R.id.custom_text2);
        custom_text3 = (TextView) findViewById(R.id.custom_text3);

        tvName = (TextView) findViewById(R.id.tvName);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        img_avatar = (CircleImageView) findViewById(R.id.img_avatar);

        btnSetInterviewTime=(Button) findViewById(R.id.btnSetInterviewTime);
        btnNotSelected=(Button) findViewById(R.id.btnNotSelected);
    }
}

