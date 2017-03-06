package com.newtech.jobnow.adapter;

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

import com.newtech.jobnow.R;
import com.newtech.jobnow.acitvity.SetInterviewActivity;
import com.newtech.jobnow.acitvity.SetInterviewDetailActivity;
import com.newtech.jobnow.controller.InterviewController;
import com.newtech.jobnow.models.CompanyIDRequest;
import com.newtech.jobnow.models.DeleteInterviewRequest;
import com.newtech.jobnow.models.InterviewObject;
import com.newtech.jobnow.models.SetInterviewRequest;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class InterviewAdapter extends BaseRecyclerAdapter<InterviewObject, InterviewAdapter.ViewHolder> {

    public static final String TAG = InterviewAdapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;
    private Activity activity;
    public InterviewAdapter(Context context, List<InterviewObject> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
        this.activity=activity;
    }
    public InterviewAdapter(Context context, Activity activity, List<InterviewObject> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
        this.activity=activity;
    }
    public InterviewAdapter(Context context, List<InterviewObject> list) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_interview_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position), position);
    }

    @Override
    public InterviewObject getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_name_employee, txt_location, txt_datetime_interview, txt_start_time, txt_end_time;
        ImageView img_photo_company, img_delete,imageView5;
        TableRow tb_add_category;

        public ViewHolder(View view) {
            super(view);
            txt_name_employee = (TextView) view.findViewById(R.id.txtTitleNotification);
            txt_location = (TextView) view.findViewById(R.id.txt_location);
            txt_datetime_interview = (TextView) view.findViewById(R.id.txt_datetime_interview);
            txt_start_time = (TextView) view.findViewById(R.id.txt_start_time);
            txt_end_time = (TextView) view.findViewById(R.id.txt_end_time);
            img_photo_company = (ImageView) view.findViewById(R.id.img_photo_company);
            tb_add_category = (TableRow) view.findViewById(R.id.tb_add_category);
            img_delete = (ImageView) view.findViewById(R.id.img_delete);
            imageView5=(ImageView) view.findViewById(R.id.imageView5);
        }

        public void bindData(final InterviewObject object, final int position) {
            if (type == 1) {
                txt_name_employee.setText(object.FullName);
                txt_location.setText(object.Location);

            /*long millis = new Date().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String millisInString  = dateFormat.format(new Date());
            long time= System.currentTimeMillis();
            if(millis>object.InterviewDate_int){
                txt_datetime_interview.setTextColor(mContext.getResources().getColor(R.color.red));
                txt_datetime_interview.setCompoundDrawablesWithIntrinsicBounds( R.mipmap.ic_datetime_red, 0,0, 0);
            }*/
                try {
                    Picasso.with(mContext).load(object.Avatar).error(R.mipmap.default_avatar).into(img_photo_company);
                } catch (Exception e) {
                    Picasso.with(mContext).load(R.mipmap.default_avatar).into(img_photo_company);
                }
            }else {
                imageView5.setVisibility(View.GONE);
                txt_name_employee.setText(object.CompanyName);
                txt_location.setText(object.CompanyEmail);
                try {
                    Picasso.with(mContext).load(object.CompanyAvatar).error(R.mipmap.default_avatar).into(img_photo_company);
                } catch (Exception e) {
                    Picasso.with(mContext).load(R.mipmap.ic_company).into(img_photo_company);
                }
            }
            txt_start_time.setText("Start time: " + object.Start_time);
            txt_end_time.setText("End time: " + object.End_time);
            String date_time_interview = "";
            String[] list = object.InterviewDate.substring(0, object.InterviewDate.indexOf("")).split("-");
            try {
                if (list.length == 3) {
                    date_time_interview = list[2] + "-" + list[1] + "-" + list[0];
                    txt_datetime_interview.setText(date_time_interview);
                }
            } catch (Exception err) {

            }
            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setMessage("Are you sure to delete");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (type == 1) {
                                        DeleteInterviewAsystask deleteInterviewAsystask = new DeleteInterviewAsystask(mContext, new DeleteInterviewRequest(object.id, 1), position);
                                        deleteInterviewAsystask.execute();
                                    }else {
                                        DeleteInterviewAsystask deleteInterviewAsystask = new DeleteInterviewAsystask(mContext, new DeleteInterviewRequest(object.id, 2), position);
                                        deleteInterviewAsystask.execute();
                                    }
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
                    if (type == 1) {
                        Intent intent = new Intent(mContext, SetInterviewActivity.class);
                        intent.putExtra("interview_detail", object);
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, SetInterviewDetailActivity.class);
                        intent.putExtra("interview_detail", object);
                        activity.startActivityForResult(intent,1);
                    }


                }
            });

        }

        class DeleteInterviewAsystask extends AsyncTask<Void, Void, String> {
            ProgressDialog dialog;
            String sessionId = "";
            DeleteInterviewRequest request;
            Context ct;
            int position;

            public DeleteInterviewAsystask(Context ct, DeleteInterviewRequest request, int position) {
                this.ct = ct;
                this.request = request;
                this.position = position;
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    InterviewController controller = new InterviewController();
                    return controller.DeleteInterview(request);
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
