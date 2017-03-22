package com.jobnow.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jobnow.acitvity.MenuActivity;
import com.jobnow.acitvity.MyApplication;
import com.jobnow.acitvity.ProfileActivity;
import com.jobnow.acitvity.ProfileVer2Activity;
import com.jobnow.acitvity.SetInterviewDetailActivity;
import com.jobnow.common.APICommon;
import com.jobnow.controller.NotificationController;
import com.jobnow.fragment.AppliedJobListFragment;
import com.jobnow.fragment.InterviewJobSeekerFragment;
import com.jobnow.fragment.MainFragment;
import com.jobnow.fragment.SaveJobListFragment;
import com.jobnow.models.AnInterviewResponse;
import com.jobnow.models.DeleteNotificationRequest;
import com.jobnow.models.JobListRequest;
import com.jobnow.models.NotificationRequest;
import com.jobnow.models.NotificationUpdateRequest;
import com.jobnow.models.NotificationVersion2Object;
import com.newtech.jobnow.R;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by manhi on 1/6/2016.
 */

public class NotificationVer2Adapter extends BaseRecyclerAdapter<NotificationVersion2Object, NotificationVer2Adapter.ViewHolder> {

    public static final String TAG = NotificationVer2Adapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;
    private Activity activity;
    public NotificationVer2Adapter(Context context, List<NotificationVersion2Object> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
    }

    public NotificationVer2Adapter(Context context, Activity activity , List<NotificationVersion2Object> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
        this.activity=activity;
    }

    public NotificationVer2Adapter(Context context, List<NotificationVersion2Object> list) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_notification_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position), position);
    }

    @Override
    public NotificationVersion2Object getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitleNotification, txtEmail, txtTimeNotification, txtFullName;
        ImageView img_photo_company, img_delete_item;
        TableRow tb_add_category;

        public ViewHolder(View view) {
            super(view);
            txtTitleNotification = (TextView) view.findViewById(R.id.txtTitleNotification);
            txtEmail = (TextView) view.findViewById(R.id.txtEmail);
            txtFullName = (TextView) view.findViewById(R.id.txtFullName);
            txtTimeNotification = (TextView) view.findViewById(R.id.txtTimeNotification);
            img_photo_company = (ImageView) view.findViewById(R.id.img_photo_company);
            tb_add_category = (TableRow) view.findViewById(R.id.tb_add_category);
            img_delete_item = (ImageView) view.findViewById(R.id.img_delete_item);
        }

        public void bindData(final NotificationVersion2Object object, final int position) {
            txtTitleNotification.setText(object.Title);
            txtEmail.setText(object.Email);
            txtFullName.setText(object.Content);
            if (object.Status == 0) {
                txtTitleNotification.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                txtTitleNotification.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            String[] dateTime = object.created_at.substring(0, object.created_at.indexOf(" ")).split("-");
            String createTime = dateTime[2] + "/" + dateTime[1] + "/" + dateTime[0];
            txtTimeNotification.setText(createTime);
            try {
                Picasso.with(mContext).load(object.Avatar).error(R.mipmap.default_avatar).into(img_photo_company);
            } catch (Exception e) {
                Picasso.with(mContext).load(R.mipmap.default_avatar).into(img_photo_company);
            }

            img_delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setMessage("Are you sure you want to delete?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DeleteNotificationAsystask deleteNotificationAsystask = new DeleteNotificationAsystask(mContext, new DeleteNotificationRequest(object.id), position);
                                    deleteNotificationAsystask.execute();
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

            tb_add_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (object.Status == 0) {
                        UpdateNotificationAsystask updateNotificationAsystask = new UpdateNotificationAsystask(mContext, new NotificationUpdateRequest(object.id), object);
                        updateNotificationAsystask.execute();
                    }
                    if (type == 1) {
                        if (object.KeyScreen != 3) {
                            Intent intent = new Intent(mContext, ProfileVer2Activity.class);
                            intent.putExtra("idJobSeeker", object.JobSeekerID);
                            intent.putExtra("interviewID", object.InterviewID);
                            intent.putExtra("emailJobSeeker", object.Email);
                            mContext.startActivity(intent);
                        }
                    } else {
                          /*Intent intent = new Intent(mContext, ProfileActivity.class);
                        intent.putExtra("chooseTab", 3);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);*/

                        if (object.InterviewID != 0) {
                            final ProgressDialog progressDialog = ProgressDialog.show(mContext, "Loading...", "Please wait", true, false);
                            APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
                            Call<AnInterviewResponse> getJobList = service.getAnInterview(
                                    APICommon.getSign(APICommon.getApiKey(), JobListRequest.PATH_URL),
                                    APICommon.getAppId(),
                                    APICommon.getDeviceType(),
                                    object.InterviewID
                            );
                            getJobList.enqueue(new Callback<AnInterviewResponse>() {
                                @Override
                                public void onResponse(Response<AnInterviewResponse> response, Retrofit retrofit) {
                                    try {
                                        progressDialog.dismiss();
                                        if (response.body() != null && response.body().code == 200) {
                                            if (response.body().result != null) {
                                                Intent intent = new Intent(mContext, SetInterviewDetailActivity.class);
                                                intent.putExtra("interview_detail", response.body().result);
                                                mContext.startActivity(intent);
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
                        } else {
                            Intent intent = new Intent(mContext, ProfileActivity.class);
                            intent.putExtra("chooseTab", 3);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
                        }
                    }
                }
            });

        }

        class UpdateNotificationAsystask extends AsyncTask<Void, Void, String> {
            ProgressDialog dialog;
            String sessionId = "";
            NotificationUpdateRequest request;
            NotificationVersion2Object notificationVersion2Object;
            Context ct;
            int position;

            public UpdateNotificationAsystask(Context ct, NotificationUpdateRequest request, NotificationVersion2Object notificationVersion2Object) {
                this.ct = ct;
                this.request = request;
                this.notificationVersion2Object = notificationVersion2Object;
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    NotificationController controller = new NotificationController();
                    return controller.UpdateNotification(request);
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
                    dialog.dismiss();
                    if (!code.equals("")) {
                       /* Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();*/
                        if (type == 2) {
                            CountNotificationAsystask countNotificationAsystask = new CountNotificationAsystask(mContext, new NotificationRequest(notificationVersion2Object.JobSeekerID, 0), 1);
                            countNotificationAsystask.execute();
                        } else {
                            CountNotificationAsystask countNotificationAsystask = new CountNotificationAsystask(mContext, new NotificationRequest(0, notificationVersion2Object.CompanyID), 2);
                            countNotificationAsystask.execute();
                        }
                    }
                } catch (Exception e) {
                }

            }
        }

        class CountNotificationAsystask extends AsyncTask<Void, Void, Integer> {
            ProgressDialog dialog;
            String sessionId = "";
            NotificationRequest notificationRequest;
            Context ct;
            Dialog dialogs;
            int type;

            public CountNotificationAsystask(Context ct, NotificationRequest notificationRequest, int type) {
                this.ct = ct;
                this.notificationRequest = notificationRequest;
                this.type = type;
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
                    dialog.dismiss();
                    if (type == 1) {
                        if (code == 0) {
                            MenuActivity.txtCount.setVisibility(View.GONE);
                        } else {
                            MainFragment.txtCount.setText(code + "");
                            SaveJobListFragment.txtCount.setText(code + "");
                            AppliedJobListFragment.txtCount.setText(code + "");
                            InterviewJobSeekerFragment.txtCount.setText(code + "");
                        }

                    } else {
                        if (code == 0) {
                            MenuActivity.txtCount.setVisibility(View.GONE);
                        } else {
                            MenuActivity.txtCount.setVisibility(View.VISIBLE);
                            MenuActivity.txtCount.setText(code + "");
                        }
                    }

                } catch (Exception e) {
                }

            }
        }

        class DeleteNotificationAsystask extends AsyncTask<Void, Void, String> {
            ProgressDialog dialog;
            String sessionId = "";
            DeleteNotificationRequest request;
            Context ct;
            int position;

            public DeleteNotificationAsystask(Context ct, DeleteNotificationRequest request, int position) {
                this.ct = ct;
                this.request = request;
                this.position = position;
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    NotificationController controller = new NotificationController();
                    return controller.DeleteNotificationByID(request);
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
                       /* Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();*/
                        Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
                dialog.dismiss();
            }
        }

    }

}
