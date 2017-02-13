package com.newtech.jobnow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.newtech.jobnow.R;
import com.newtech.jobnow.models.InviteObject;
import com.newtech.jobnow.models.JobObject;
import com.newtech.jobnow.utils.Utils;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by manhi on 1/6/2016.
 */

public class InviteAdapter extends BaseRecyclerAdapter<InviteObject, InviteAdapter.ViewHolder> {

    public static final String TAG = InviteAdapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;

    public InviteAdapter(Context context, List<InviteObject> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
    }

    public InviteAdapter(Context context, List<InviteObject> list) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_invite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public InviteObject getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_invite;
        private TextView txt_title_company_invite, txt_email_company, txt_status_invite;

        public ViewHolder(View view) {
            super(view);
            layout_invite=(RelativeLayout) view.findViewById(R.id.layout_invite);
            txt_title_company_invite=(TextView) view.findViewById(R.id.txt_title_company_invite);
            txt_email_company=(TextView) view.findViewById(R.id.txt_email_company);
            txt_status_invite=(TextView) view.findViewById(R.id.txt_status_invite);

        }

        public void bindData(InviteObject inviteObject) {
            if(inviteObject.getStatus()==0){
                txt_status_invite.setText("Invited");
                txt_status_invite.setBackgroundResource(R.drawable.bg_btn_invite);
                layout_invite.setBackgroundResource(R.drawable.bg_invite);
            }else if(inviteObject.getStatus()==1){
                txt_status_invite.setText("Done");
                txt_status_invite.setBackgroundResource(R.drawable.bg_btn_invite_done);
                layout_invite.setBackgroundResource(R.drawable.bg_invite_done);
            }else if(inviteObject.getStatus()==2) {
                txt_status_invite.setText("Reject");
                txt_status_invite.setBackgroundResource(R.drawable.bg_btn_invite_reject);
                layout_invite.setBackgroundResource(R.drawable.bg_invite_reject);
            }
            txt_title_company_invite.setText(inviteObject.getCompanyName());
            txt_email_company.setText(inviteObject.getEmail());

        }

    }

}
