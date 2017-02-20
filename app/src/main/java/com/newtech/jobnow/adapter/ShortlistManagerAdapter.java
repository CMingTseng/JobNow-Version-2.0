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
import com.newtech.jobnow.acitvity.SetInterviewActivity;
import com.newtech.jobnow.common.CustomTextViewHelveticaneuelight;
import com.newtech.jobnow.common.DrawableClickListener;
import com.newtech.jobnow.controller.CategoryController;
import com.newtech.jobnow.models.CompanyIDRequest;
import com.newtech.jobnow.models.ShortlistDetailObject;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by manhi on 1/6/2016.
 */

public class ShortlistManagerAdapter extends BaseRecyclerAdapter<ShortlistDetailObject, ShortlistManagerAdapter.ViewHolder> {

    public static final String TAG = ShortlistManagerAdapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;
    int category_id;
    public ShortlistManagerAdapter(Context context, List<ShortlistDetailObject> list, int type,int category_id) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
        this.category_id=category_id;
    }

    public ShortlistManagerAdapter(Context context, List<ShortlistDetailObject> list) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custome_shortlist_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position),position);
    }

    @Override
    public ShortlistDetailObject getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout lnFeautured, lnJob;
        private TextView txt_name_employee, txt_location, txt_description;
        private ImageView img_photo_company;
        private CustomTextViewHelveticaneuelight btn_delete,btn_set_interview;

        public ViewHolder(View view) {
            super(view);
            txt_name_employee=(TextView) view.findViewById(R.id.txtTitleNotification);
            txt_location=(TextView) view.findViewById(R.id.txt_location);
            txt_description=(TextView) view.findViewById(R.id.txt_datetime_interview);
            btn_set_interview=(CustomTextViewHelveticaneuelight) view.findViewById(R.id.txt_set_interview);
            btn_delete=(CustomTextViewHelveticaneuelight) view.findViewById(R.id.btn_delete_shortlist_detail);
            img_photo_company=(ImageView) view.findViewById(R.id.img_photo_company);
        }

        public void bindData(final ShortlistDetailObject shortlistDetailObject, final int position) {
            txt_name_employee.setText(shortlistDetailObject.FullName);
            txt_location.setText(shortlistDetailObject.CountryName);
            txt_description.setText(shortlistDetailObject.Description);
            if(shortlistDetailObject.Description.equals("")||shortlistDetailObject.Description.isEmpty()){
                txt_description.setVisibility(View.GONE);
            }
            try {
                Picasso.with(mContext).load(shortlistDetailObject.Avatar).placeholder(R.mipmap.img_logo_company).error(R.mipmap.default_avatar).into(img_photo_company);
            }catch (Exception e){
                Picasso.with(mContext).load(R.mipmap.default_avatar).into(img_photo_company);
            }

            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CompanyIDRequest request= new CompanyIDRequest(category_id,shortlistDetailObject.UserID);
                    DeleteCategoryAsystask deleteJobAsystask= new DeleteCategoryAsystask(mContext,request,position);
                    deleteJobAsystask.execute();
                }
            });
            btn_set_interview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(mContext,SetInterviewActivity.class);
                    intent.putExtra("jobseeker_v2",shortlistDetailObject);
                    mContext.startActivity(intent);
                }
            });

            btn_set_interview.setDrawableClickListener(new DrawableClickListener() {


                public void onClick(DrawablePosition target) {
                    switch (target) {
                        case LEFT:
                            //Do something here
                            Intent intent= new Intent(mContext,SetInterviewActivity.class);
                            intent.putExtra("jobseeker_v2",shortlistDetailObject);
                            mContext.startActivity(intent);
                            break;
                        case RIGHT:
                            break;
                        case TOP:
                            //Do something here
                            break;
                        case BOTTOM:
                            //Do something here
                            break;
                        default:
                            break;
                    }
                }

            });

            btn_delete.setDrawableClickListener(new DrawableClickListener() {


                public void onClick(DrawablePosition target) {
                    switch (target) {
                        case LEFT:
                            //Do something here
                            CompanyIDRequest request= new CompanyIDRequest(category_id,shortlistDetailObject.UserID);
                            DeleteCategoryAsystask deleteJobAsystask= new DeleteCategoryAsystask(mContext,request,position);
                            deleteJobAsystask.execute();
                            break;
                        case RIGHT:
                            break;
                        case TOP:
                            //Do something here
                            break;
                        case BOTTOM:
                            //Do something here
                            break;
                        default:
                            break;
                    }
                }

            });

        }
        class DeleteCategoryAsystask extends AsyncTask<Void, Void, String> {
            ProgressDialog dialog;
            String sessionId = "";
            CompanyIDRequest request;
            Context ct;
            Dialog dialogs;
            int type;
            int position;
            public DeleteCategoryAsystask(Context ct, CompanyIDRequest request, int position) {
                this.ct = ct;
                this.request = request;
                this.position= position;
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    CategoryController controller = new CategoryController();
                    return controller.DeleteEmployeeCategory(request);
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
