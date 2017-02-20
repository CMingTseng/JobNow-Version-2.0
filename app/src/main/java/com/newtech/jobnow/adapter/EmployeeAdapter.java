package com.newtech.jobnow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newtech.jobnow.R;
import com.newtech.jobnow.common.CustomTextViewHelveticaneuelight;
import com.newtech.jobnow.models.EmployeeObject;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by manhi on 1/6/2016.
 */

public class EmployeeAdapter extends BaseRecyclerAdapter<EmployeeObject, EmployeeAdapter.ViewHolder> {

    public static final String TAG = EmployeeAdapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;

    public EmployeeAdapter(Context context, List<EmployeeObject> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
    }

    public EmployeeAdapter(Context context, List<EmployeeObject> list) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_add_employee_shortlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position),position);
    }

    @Override
    public EmployeeObject getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnFeautured, lnJob;
        private TextView txt_name_employee, txt_location, txt_description;
        private ImageView img_photo_company,btn_addEmployee;
        private CustomTextViewHelveticaneuelight btn_delete,btn_set_interview;

        public ViewHolder(View view) {
            super(view);
            txt_name_employee=(TextView) view.findViewById(R.id.txtTitleNotification);
            txt_location=(TextView) view.findViewById(R.id.txt_location);
            img_photo_company=(ImageView) view.findViewById(R.id.img_photo_company);
            btn_addEmployee=(ImageView) view.findViewById(R.id.btn_addEmployee);
        }

        public void bindData(final EmployeeObject shortlistDetailObject, final int position) {
            txt_name_employee.setText(shortlistDetailObject.FullName);
            txt_location.setText(shortlistDetailObject.Name);
            if(shortlistDetailObject.is_added!=0){
                Picasso.with(mContext).load(R.mipmap.ic_tick_active).error(R.mipmap.ic_tick_inactive).into(btn_addEmployee);
            }else {
                Picasso.with(mContext).load(R.mipmap.ic_tick_inactive).into(btn_addEmployee);
            }
            try {
                Picasso.with(mContext).load(shortlistDetailObject.Avatar).placeholder(R.mipmap.img_logo_company).error(R.mipmap.default_avatar).into(img_photo_company);
            }catch (Exception e){
                Picasso.with(mContext).load(R.mipmap.default_avatar).into(img_photo_company);
            }

            btn_addEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(shortlistDetailObject.is_added==0) {
                        if (!list.get(position).isChoose) {
                            list.get(position).isChoose = true;
                            Picasso.with(mContext).load(R.mipmap.ic_tick_active).into(btn_addEmployee);
                        } else {
                            list.get(position).isChoose = false;
                            Picasso.with(mContext).load(R.mipmap.ic_tick_inactive).into(btn_addEmployee);
                        }
                    }
                }
            });

        }



    }

}
