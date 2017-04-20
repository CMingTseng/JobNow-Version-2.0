package com.jobnow.adapter;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jobnow.acitvity.MenuActivity;
import com.jobnow.acitvity.PostAJobsActivity;
import com.jobnow.controller.JobController;
import com.jobnow.models.JobObject;
import com.jobnow.models.JobRequest;
import com.jobnow.models.UserModel;
import com.jobnow.R;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by manhi on 1/6/2016.
 */

public class JobManagerAdapter extends BaseRecyclerAdapter<JobObject, JobManagerAdapter.ViewHolder> {

    public static final String TAG = JobManagerAdapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;
    private int typeJob;
    private UserModel userModel;
    public JobManagerAdapter(Context context, List<JobObject> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
    }

    public JobManagerAdapter(Context context, List<JobObject> list, UserModel userModel) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = 0;
        this.userModel=userModel;
    }

    public int getTypeJob() {
        return typeJob;
    }

    public void setTypeJob(int typeJob) {
        this.typeJob = typeJob;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_job_manager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position),position);
    }

    @Override
    public JobObject getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnFeautured, lnJob;
        private TextView txt_title_job_item, txt_location_item, txt_price_item, txt_time_post, txt_name_company;
        private ImageView img_photo_company;
        private TextView btn_delete;
        private LinearLayout layout_parent;

        public ViewHolder(View view) {
            super(view);
            txt_title_job_item=(TextView) view.findViewById(R.id.txt_title_job_item);
            txt_location_item=(TextView) view.findViewById(R.id.txt_location_item);
            txt_price_item=(TextView) view.findViewById(R.id.txt_price_item);
            txt_time_post=(TextView) view.findViewById(R.id.txt_time_post);
            txt_name_company=(TextView) view.findViewById(R.id.txtTitleNotification);
            btn_delete=(TextView) view.findViewById(R.id.btn_delete);
            img_photo_company=(ImageView) view.findViewById(R.id.img_photo_company);
            layout_parent=(LinearLayout) view.findViewById(R.id.layout_parent);
        }

        public void bindData(final JobObject jobObject, final int position) {
            txt_title_job_item.setText(jobObject.Title);
            txt_location_item.setText(jobObject.LocationName);
            txt_price_item.setText(new DecimalFormat("#,###.#").format(Double.parseDouble(jobObject.FromSalary)) + " - " +new DecimalFormat("#,###.#").format(Double.parseDouble(jobObject.ToSalary )) + " (SGD)");

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
                        txt_time_post.setText(mContext.getString(R.string.posted)+" " + day + " days ago");
                    else
                        txt_time_post.setText(mContext.getString(R.string.posted)+" " + day + " day ago");
                } else {
                    if (hour < 1) {
                        txt_time_post.setText(mContext.getString(R.string.posted)+" " + mm + " min ago");
                    } else {
                        txt_time_post.setText(mContext.getString(R.string.posted)+" " + hour + " hour ago");

                    }
                }
            } catch (Exception exx) {

            }

            //txt_time_post.setText(mContext.getString(R.string.posted)+" "+p.format(new Date(Utils.getLongTime(jobObject.created_at))));
            txt_name_company.setText(jobObject.CompanyName);
            try {
                Picasso.with(mContext).load(jobObject.CompanyLogo).placeholder(R.mipmap.img_logo_company).error(R.mipmap.default_avatar).into(img_photo_company);
            }catch (Exception e){
                Picasso.with(mContext).load(R.mipmap.default_avatar).into(img_photo_company);
            }

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setMessage("Are you sure to delete");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    JobRequest jobRequest= new JobRequest(userModel.apiToken,jobObject.id,userModel.id);
                                    DeleteJobAsystask deleteJobAsystask= new DeleteJobAsystask(mContext,jobRequest,position);
                                    deleteJobAsystask.execute();
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

            layout_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getTypeJob()==0){
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date1 = sdf.parse(jobObject.DateExprire);
                            Date date2 =sdf.parse(sdf.format(new Date()));
                            int sss=date1.compareTo(date2);
                            if(date1.compareTo(date2)<0) {
                                if (Double.parseDouble(MenuActivity.txt_numberCredits.getText().toString()) >= 0.5) {
                                    Toast.makeText(mContext, "Please choose Re-post in the Job status if you want to repost", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(mContext, PostAJobsActivity.class);
                                    intent.putExtra("re_post",2);
                                    intent.putExtra("job_detail",jobObject);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, "Sorry, you have insufficient credits. Please purchase in Menu ---> Get More Job Credit", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Intent intent= new Intent(mContext, PostAJobsActivity.class);
                                intent.putExtra("job_detail",jobObject);
                                mContext.startActivity(intent);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
        }

        class DeleteJobAsystask extends AsyncTask<Void, Void, String> {
            ProgressDialog dialog;
            String sessionId = "";
            JobRequest request;
            Context ct;
            Dialog dialogs;
            int type;
            int position;
            public DeleteJobAsystask(Context ct, JobRequest request, int position) {
                this.ct = ct;
                this.request = request;
                this.position= position;
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    JobController controller = new JobController();
                    return controller.DeleteJob(request);
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
