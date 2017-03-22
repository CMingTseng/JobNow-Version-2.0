package com.jobnow.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobnow.acitvity.DetailJobsActivity;
import com.jobnow.acitvity.MyApplication;
import com.jobnow.common.APICommon;
import com.jobnow.config.Config;
import com.jobnow.models.BaseResponse;
import com.jobnow.models.DeleteJobRequest;
import com.jobnow.models.JobV2Object;
import com.newtech.jobnow.R;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by manhi on 1/6/2016.
 */

public class JobListV2Adapter extends BaseRecyclerAdapter<JobV2Object, JobListV2Adapter.ViewHolder> {

    public static final String TAG = JobListV2Adapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;

    public JobListV2Adapter(Context context, List<JobV2Object> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
    }

    public JobListV2Adapter(Context context, List<JobV2Object> list) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_job, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);
        holder.bindData(list.get(position),position);
    }

    @Override
    public JobV2Object getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnFeautured, lnJob;
        private TextView tvName, tvLocation, tvPrice, tvTime, tvCompanyName;
        private ImageView imgLogo;
        private ImageButton btnRemove;

        public ViewHolder(View view) {
            super(view);
            lnFeautured = (LinearLayout) view.findViewById(R.id.lnFeautured);
            lnJob = (LinearLayout) view.findViewById(R.id.lnJob);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvCompanyName = (TextView) view.findViewById(R.id.tvCompanyName);
            imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
            btnRemove = (ImageButton) view.findViewById(R.id.btnRemove);
            btnRemove.setVisibility(type == 0 ? View.INVISIBLE : View.VISIBLE);

        }

        public void bindData(final JobV2Object jobObject, final int position) {
            tvName.setText(jobObject.Title);
            tvLocation.setText(jobObject.LocationName);
            if (jobObject.IsDisplaySalary == 1) {
                tvPrice.setText(new DecimalFormat("#,###.#").format(Double.parseDouble(jobObject.FromSalary)) + " - " + new DecimalFormat("#,###.#").format(Double.parseDouble(jobObject.ToSalary)) + "(SGD)");
            }else {
                tvPrice.setVisibility(View.GONE);
            }
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00"));
                Date oldDate = dateFormat.parse(jobObject.CreateDate.date);
                Date cDate = new Date();
                Long timeDiff = cDate.getTime() - oldDate.getTime();
                int day = (int) TimeUnit.MILLISECONDS.toDays(timeDiff);
                int hour = (int) (TimeUnit.MILLISECONDS.toHours(timeDiff) - TimeUnit.DAYS.toHours(day));
                int mm = (int) (TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));


                if (day > 0) {
                    if (day > 1)
                        tvTime.setText(mContext.getString(R.string.posted) + " " + day + " days ago");
                    else
                        tvTime.setText(mContext.getString(R.string.posted) + " " + day + " day ago");
                } else {
                    if (hour < 1) {
                        tvTime.setText(mContext.getString(R.string.posted) + " " + mm + " min ago");
                    } else {
                        tvTime.setText(mContext.getString(R.string.posted) + " " + hour + " hour ago");

                    }
                }
            } catch (Exception exx) {

            }
            tvCompanyName.setText(jobObject.CompanyName);
            try {
                Picasso.with(mContext).load(jobObject.CompanyLogo).placeholder(R.mipmap.img_logo_company).error(R.mipmap.img_logo_company).into(imgLogo);
            } catch (Exception e) {
                Picasso.with(mContext).load(R.mipmap.img_logo_company).into(imgLogo);
            }

            lnJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailJobsActivity.class);
                    intent.putExtra("jobId", jobObject.JobID);
                    intent.putExtra("jobObject", jobObject);
                    mContext.startActivity(intent);
                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setMessage("Are you sure to delete");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    handleUnappliedJob(jobObject,position);
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
        }

        private void handleUnappliedJob(final JobV2Object jobObject, final int position) {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Config.Pref, Context.MODE_PRIVATE);
            String token = sharedPreferences.getString(Config.KEY_TOKEN, "");
            int userId = sharedPreferences.getInt(Config.KEY_ID, 0);
            APICommon.JobNowService service = MyApplication.getInstance().getJobNowService();
            Call<BaseResponse> call;
            if(type== JobListV2Adapter.APPLY_TYPE) {
                call = service.deleteAppliedJob(new DeleteJobRequest(userId, jobObject.JobID, token, userId,
                        JobListV2Adapter.APPLY_TYPE));
            }else {
                call = service.deleteSaveJob(new DeleteJobRequest(userId, jobObject.JobID, token, userId,
                        JobListAdapter.SAVE_TYPE));
            }
            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Response<BaseResponse> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        BaseResponse baseResponse = response.body();

                        if (baseResponse.code == 200) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, baseResponse.message,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

    }

}
