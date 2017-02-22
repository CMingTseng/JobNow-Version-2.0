package com.newtech.jobnow.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.newtech.jobnow.R;
import com.newtech.jobnow.acitvity.SetInterviewActivity;
import com.newtech.jobnow.acitvity.SetInterviewDetailActivity;
import com.newtech.jobnow.models.InterviewObject;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by manhi on 1/6/2016.
 */

public class InterviewAdapter extends BaseRecyclerAdapter<InterviewObject, InterviewAdapter.ViewHolder> {

    public static final String TAG = InterviewAdapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;

    public InterviewAdapter(Context context, List<InterviewObject> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
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
        holder.bindData(list.get(position));
    }

    @Override
    public InterviewObject getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_name_employee, txt_location, txt_datetime_interview,txt_start_time,txt_end_time;
        ImageView img_photo_company;
        TableRow tb_add_category;
        public ViewHolder(View view) {
            super(view);
            txt_name_employee=(TextView) view.findViewById(R.id.txtTitleNotification);
            txt_location=(TextView) view.findViewById(R.id.txt_location);
            txt_datetime_interview=(TextView) view.findViewById(R.id.txt_datetime_interview);
            txt_start_time=(TextView) view.findViewById(R.id.txt_start_time);
            txt_end_time=(TextView) view.findViewById(R.id.txt_end_time);
            img_photo_company=(ImageView) view.findViewById(R.id.img_photo_company);
            tb_add_category=(TableRow) view.findViewById(R.id.tb_add_category);
        }

        public void bindData(final InterviewObject object) {
            txt_name_employee.setText(object.FullName);
            txt_location.setText(object.Location);
            txt_start_time.setText("Start time: "+object.Start_time);
            txt_end_time.setText("End time: "+object.End_time);

            try {
                Picasso.with(mContext).load(object.Avatar).error(R.mipmap.default_avatar).into(img_photo_company);
            }catch (Exception e){
                Picasso.with(mContext).load(R.mipmap.default_avatar).into(img_photo_company);
            }
            String date_time_interview="";
            String []list=object.InterviewDate.substring(0,object.InterviewDate.indexOf("")).split("-");
            try {
                if(list.length==3){
                    date_time_interview=list[2]+"-"+list[1]+"-"+list[0];
                    txt_datetime_interview.setText(date_time_interview);
                }
            }catch (Exception err){

            }

            tb_add_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type==1) {
                        Intent intent = new Intent(mContext, SetInterviewActivity.class);
                        intent.putExtra("interview_detail", object);
                        mContext.startActivity(intent);
                    }else {
                        Intent intent = new Intent(mContext, SetInterviewDetailActivity.class);
                        intent.putExtra("interview_detail", object);
                        mContext.startActivity(intent);
                    }


                }
            });

        }

    }

}
