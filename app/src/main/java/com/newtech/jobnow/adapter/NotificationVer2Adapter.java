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
import com.newtech.jobnow.acitvity.MenuActivity;
import com.newtech.jobnow.acitvity.NotificationManagerActivity;
import com.newtech.jobnow.acitvity.ProfileVer2Activity;
import com.newtech.jobnow.models.InterviewObject;
import com.newtech.jobnow.models.NotificationVersion2Object;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.util.List;

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

    public NotificationVer2Adapter(Context context, List<NotificationVersion2Object> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
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
        holder.bindData(list.get(position));
    }

    @Override
    public NotificationVersion2Object getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitleNotification, txtEmail, txtTimeNotification;
        ImageView img_photo_company;
        TableRow tb_add_category;

        public ViewHolder(View view) {
            super(view);
            txtTitleNotification=(TextView) view.findViewById(R.id.txtTitleNotification);
            txtEmail=(TextView) view.findViewById(R.id.txtEmail);
            txtTimeNotification=(TextView) view.findViewById(R.id.txtTimeNotification);
            img_photo_company=(ImageView) view.findViewById(R.id.img_photo_company);
            tb_add_category=(TableRow) view.findViewById(R.id.tb_add_category);
        }

        public void bindData(final NotificationVersion2Object object) {
            txtTitleNotification.setText(object.Title);
            txtEmail.setText(object.Email);

            String []dateTime=object.created_at.substring(0,object.created_at.indexOf(" ")).split("-");
            String createTime=dateTime[2]+"/"+dateTime[1]+"/"+dateTime[0];
            txtTimeNotification.setText(createTime);
            try {
                Picasso.with(mContext).load(object.Avatar).error(R.mipmap.default_avatar).into(img_photo_company);
            }catch (Exception e){
                Picasso.with(mContext).load(R.mipmap.default_avatar).into(img_photo_company);
            }

            tb_add_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(mContext,ProfileVer2Activity.class);
                    intent.putExtra("idJobSeeker",object.JobSeekerID);
                    intent.putExtra("emailJobSeeker",object.Email);
                    mContext.startActivity(intent);

                }
            });

        }

    }

}
