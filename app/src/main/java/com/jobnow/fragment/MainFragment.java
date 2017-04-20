package com.jobnow.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jobnow.acitvity.FilterActivity;
import com.jobnow.acitvity.NotificationActivity;
import com.jobnow.acitvity.SearchResultActivity;
import com.jobnow.config.Config;
import com.jobnow.controller.NotificationController;
import com.jobnow.models.JobListRequest;
import com.jobnow.models.NotificationRequest;
import com.jobnow.utils.Utils;
import com.jobnow.R;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewpager;
    private RelativeLayout imgFilter, imgBack;
    public static TextView txtCount;
    private EditText edtSearch;
    int userID;
    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        InitUI(view);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        userID = sharedPreferences.getInt(Config.KEY_ID, 0);
        CountNotificationAsystask countNotificationAsystask= new CountNotificationAsystask(getActivity(),new NotificationRequest(userID,0));
        countNotificationAsystask.execute();

        return view;
    }

    void search(String title) {
        Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
        if (title.equals(""))
            title = null;
        JobListRequest request = new JobListRequest(1, "DESC", title, null, null,
                null, null, null, null,0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(SearchResultActivity.KEY_JOB, request);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    private void InitUI(View view) {
        txtCount=(TextView) view.findViewById(R.id.txtCount);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        imgFilter = (RelativeLayout) view.findViewById(R.id.imgFilter);
        imgBack = (RelativeLayout) view.findViewById(R.id.btnRemove);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,1);
            }
        });
        imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FilterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent,1);

            }
        });
        setupViewPager(viewpager);
        tabLayout.setupWithViewPager(viewpager);
        Utils.closeKeyboard(getActivity());
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(JobListFragment.getInstance(false), getString(R.string.jobList));
        //viewPagerAdapter.addFragment(new MapListFragment(), getString(R.string.mapList));
        viewPagerAdapter.addFragment(JobListPartTimeFragment.getInstance(false), getString(R.string.mapList));
        viewPager.setAdapter(viewPagerAdapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grants) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grants);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            CountNotificationAsystask countNotificationAsystask = new CountNotificationAsystask(getActivity(), new NotificationRequest(userID, 0));
            countNotificationAsystask.execute();
            for (Fragment fragment : getChildFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }catch (Exception er){
            er.printStackTrace();
        }
    }


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
                    txtCount.setVisibility(View.VISIBLE);
                    txtCount.setText(code + "");
                }

            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }
}
