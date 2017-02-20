package com.newtech.jobnow.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.newtech.jobnow.R;
import com.newtech.jobnow.acitvity.PostAJobsActivity;
import com.newtech.jobnow.controller.JobController;
import com.newtech.jobnow.models.JobObject;
import com.newtech.jobnow.models.JobRequest;
import com.newtech.jobnow.models.UserModel;
import com.newtech.jobnow.utils.Utils;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

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
            txt_price_item.setText(jobObject.FromSalary + " - " + jobObject.ToSalary + " (USD)");
            txt_time_post.setText(mContext.getString(R.string.posted)+" "+p.format(new Date(Utils.getLongTime(jobObject.created_at))));
            txt_name_company.setText(jobObject.CompanyName);
            try {
                Picasso.with(mContext).load(jobObject.CompanyLogo).placeholder(R.mipmap.img_logo_company).error(R.mipmap.default_avatar).into(img_photo_company);
            }catch (Exception e){
                Picasso.with(mContext).load(R.mipmap.default_avatar).into(img_photo_company);
            }

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JobRequest jobRequest= new JobRequest(userModel.apiToken,jobObject.id,userModel.id);
                    DeleteJobAsystask deleteJobAsystask= new DeleteJobAsystask(mContext,jobRequest,position);
                    deleteJobAsystask.execute();
                }
            });

            layout_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(mContext, PostAJobsActivity.class);
                    intent.putExtra("job_detail",jobObject);
                    mContext.startActivity(intent);
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
            public DeleteJobAsystask(Context ct, JobRequest request,int position) {
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
