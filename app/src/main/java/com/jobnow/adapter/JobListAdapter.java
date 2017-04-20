package com.jobnow.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jobnow.acitvity.DetailJobsActivity;
import com.jobnow.eventbus.DeleteJobEvent;
import com.jobnow.models.JobObject;
import com.jobnow.R;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by manhi on 1/6/2016.
 */

public class JobListAdapter extends BaseRecyclerAdapter<JobObject, JobListAdapter.ViewHolder> {

    public static final String TAG = JobListAdapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;

    public JobListAdapter(Context context, List<JobObject> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
    }

    public JobListAdapter(Context context, List<JobObject> list) {
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
        holder.bindData(list.get(position));
    }

    @Override
    public JobObject getItembyPostion(int position) {
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailJobsActivity.class);
                    int job_id = (type == NORMAL_TYPE ? list.get(getAdapterPosition()).id :
                            list.get(getAdapterPosition()).JobID);
                    intent.putExtra("jobId", job_id);
                    intent.putExtra("jobObject", list.get(getAdapterPosition()));
                    mContext.startActivity(intent);
                }
            });

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new DeleteJobEvent(getAdapterPosition(), type));
                }
            });
        }

        public void bindData(JobObject jobObject) {
            tvName.setText(jobObject.Title);
            tvLocation.setText(jobObject.LocationName);
            if(jobObject.IsDisplaySalary==1) {
                tvPrice.setText(new DecimalFormat("#,###.#").format(Double.parseDouble(jobObject.FromSalary.equals("")?"0":jobObject.FromSalary)) + " - " + new DecimalFormat("#,###.#").format(Double.parseDouble(jobObject.ToSalary.equals("")?"0":jobObject.ToSalary)) + "(SGD)");
            }else {
                tvPrice.setVisibility(View.GONE);
            }
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00"));
                Date oldDate = dateFormat.parse(jobObject.updated_at);
                Date cDate = new Date();
                Long timeDiff = cDate.getTime() - oldDate.getTime();
                int day = (int) TimeUnit.MILLISECONDS.toDays(timeDiff);
                int hour = (int) (TimeUnit.MILLISECONDS.toHours(timeDiff) - TimeUnit.DAYS.toHours(day));
                int mm = (int) (TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));


                if (day > 0) {
                    if (day > 1)
                        tvTime.setText(mContext.getString(R.string.posted)+" " + day + " days ago");
                    else
                        tvTime.setText(mContext.getString(R.string.posted)+" " + day + " day ago");
                } else {
                    if (hour < 1) {
                        tvTime.setText(mContext.getString(R.string.posted)+" " + mm + " min ago");
                    } else {
                        tvTime.setText(mContext.getString(R.string.posted)+" " + hour + " hour ago");

                    }
                }
            } catch (Exception exx) {

            }


            //tvTime.setText(mContext.getString(R.string.posted)+" "+p.format(new Date(Utils.getLongTime(jobObject.created_at))));
            tvCompanyName.setText(jobObject.CompanyName);
            try {
                Picasso.with(mContext).load(jobObject.CompanyLogo).placeholder(R.mipmap.img_logo_company).error(R.mipmap.img_logo_company).into(imgLogo);
            }catch (Exception e){
                Picasso.with(mContext).load(R.mipmap.img_logo_company).into(imgLogo);
            }
        }

    }

}
