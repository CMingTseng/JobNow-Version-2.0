package com.jobnow.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jobnow.acitvity.MyApplication;
import com.jobnow.acitvity.ShortlistManagerActivity;
import com.jobnow.adapter.CategoryAdapter;
import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.controller.CategoryController;
import com.jobnow.models.CategoryObject;
import com.jobnow.models.CategoryRequest;
import com.jobnow.models.CategoryResponse;
import com.jobnow.models.JobListRequest;
import com.jobnow.models.UserModel;
import com.jobnow.utils.Utils;
import com.jobnow.widget.CRecyclerView;
import com.jobnow.widget.RecyclerItemClickListener;
import com.newtech.jobnow.R;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Administrator on 10/02/2017.
 */

public class ShorListCategoryFragment extends Fragment {
    RelativeLayout btn_add_manager;

    EditText edtSearch;

    private LinearLayout lnErrorView;
    private CRecyclerView rvListCategory;
    private CategoryAdapter adapter;
    private SwipeRefreshLayout refresh;
    int page = 1;
    private boolean isCanNext = false;
    private boolean isProgessingLoadMore = false;
    private JobListRequest jobListRequest = null;
    private int type = 1;
    private int companyID = 0;
    UserModel userModel;
    List<CategoryObject> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shortlist, container, false);
        InitUI(rootView);
        InitEvent();
        bindData();
        return rootView;
    }

    public void InitUI(View rootView) {
        btn_add_manager = (RelativeLayout) rootView.findViewById(R.id.btn_add_manager);
        View view = getActivity().findViewById(R.id.layout_editSearch);
        edtSearch=(EditText) view.findViewById(R.id.editSearch);
        rvListCategory = (CRecyclerView) rootView.findViewById(R.id.rvListShortList);
        adapter = new CategoryAdapter(getActivity(), new ArrayList<CategoryObject>());
        lnErrorView = (LinearLayout) rootView.findViewById(R.id.lnErrorView);
        rvListCategory.setAdapter(adapter);
        refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        Utils.closeKeyboard(getActivity());
    }

    public void InitEvent() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtSearch.getText().toString().equals("")) {
                    adapter.clear();
                    adapter.addAll(list);
                } else {
                    List<CategoryObject> list_employee_tmp = new ArrayList<CategoryObject>();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getName().toLowerCase().contains(edtSearch.getText().toString().toLowerCase())) {
                            list_employee_tmp.add(list.get(i));
                        }
                    }
                    adapter.clear();
                    adapter.addAll(list_employee_tmp);
                }
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                adapter.clear();
                list.clear();
                page = 1;
                bindData();
            }
        });
        rvListCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Utils.isReadyForPullEnd(recyclerView) && isCanNext && !isProgessingLoadMore) {
                    bindData();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        rvListCategory.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvListCategory, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        try {
                            CategoryObject categoryObject = adapter.getList().get(position);
                            Intent intent = new Intent(getActivity(), ShortlistManagerActivity.class);
                            intent.putExtra("category_id", categoryObject.getId());
                            intent.putExtra("category_name", categoryObject.getName());
                            getActivity().startActivityForResult(intent, 1);
                        } catch (Exception exx) {

                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        btn_add_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_add_shortlist);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.28);
                dialog.getWindow().setLayout(width, height);
                dialog.show();

                final EditText edtCategoryName = (EditText) dialog.findViewById(R.id.edtCategoryName);
                Button btnAddCategoryShortlist = (Button) dialog.findViewById(R.id.btnAddCategoryShortlist);

                btnAddCategoryShortlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (edtCategoryName.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), getString(R.string.pleaseInputCategoryName), Toast.LENGTH_SHORT).show();
                        } else {
                            CategoryRequest categoryRequest = new CategoryRequest(edtCategoryName.getText().toString(), userModel.id, 0);
                            SetNewCategoryAsystask setNewCategoryAsystask = new SetNewCategoryAsystask(getActivity(), categoryRequest, dialog);
                            setNewCategoryAsystask.execute();
                        }
                    }
                });

            }
        });
    }

    private void bindData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.Pref, getActivity().MODE_PRIVATE);
        String profile = sharedPreferences.getString(Config.KEY_USER_PROFILE, "");
        Gson gson = new Gson();
        userModel = gson.fromJson(profile, UserModel.class);
        companyID = userModel.id;
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "Loading...", "Please wait", true, false);
        isProgessingLoadMore = true;

        Bundle bundle = getArguments();
        if (bundle != null) {
            //jobListRequest = (JobListRequest) bundle.getSerializable(KEY_JOB);
        }
        APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
        Call<CategoryResponse> getJobList = service.getListCategory(
                APICommon.getSign(APICommon.getApiKey(), JobListRequest.PATH_URL),
                APICommon.getAppId(),
                APICommon.getDeviceType(),
                userModel.id);
        getJobList.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Response<CategoryResponse> response, Retrofit retrofit) {
                isProgessingLoadMore = false;
                refresh.setRefreshing(false);
                try {
                    progressDialog.dismiss();
                    if (response.body() != null && response.body().code == 200) {
                        if (response.body().result != null && response.body().result != null) {
                            adapter.addAll(response.body().result);
                            list.addAll(response.body().result);
                            /*if (page < response.body().result.last_page) {
                                page++;
                                isCanNext = true;
                            } else {
                                isCanNext = false;
                            }*/
                        } else {
                            isCanNext = false;
                        }
                    }

                    if (adapter.getItemCount() == 0) {
                        lnErrorView.setVisibility(View.VISIBLE);
                        rvListCategory.setVisibility(View.GONE);
                    } else {
                        lnErrorView.setVisibility(View.GONE);
                        rvListCategory.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Throwable t) {
                refresh.setRefreshing(false);
                isProgessingLoadMore = false;
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getActivity().getString(R.string.error_connect), Toast.LENGTH_SHORT).show();
            }
        });
    }

    class SetNewCategoryAsystask extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;
        String sessionId = "";
        CategoryRequest categoryRequest;
        Context ct;
        Dialog dialogs;

        public SetNewCategoryAsystask(Context ct, CategoryRequest categoryRequest, Dialog dialogs) {
            this.ct = ct;
            this.categoryRequest = categoryRequest;
            this.dialogs = dialogs;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                CategoryController controller = new CategoryController();
                return controller.AddCatgory(categoryRequest);
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
                    Toast.makeText(getActivity(), "Category updated successfully", Toast.LENGTH_SHORT).show();
                    adapter.clear();
                    bindData();
                    dialogs.dismiss();
                    Utils.closeKeyboard(getActivity());
                }
            } catch (Exception e) {
            }
            dialog.dismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra("result", 0);
                adapter.clear();
                bindData();
            }
        }
    }
}
