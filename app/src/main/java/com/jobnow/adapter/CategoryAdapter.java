package com.jobnow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jobnow.models.CategoryObject;
import com.jobnow.R;
import com.ocpsoft.pretty.time.PrettyTime;

import java.util.List;

/**
 * Created by manhi on 1/6/2016.
 */

public class CategoryAdapter extends BaseRecyclerAdapter<CategoryObject, CategoryAdapter.ViewHolder> {

    public static final String TAG = CategoryAdapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type;

    public CategoryAdapter(Context context, List<CategoryObject> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
    }

    public CategoryAdapter(Context context, List<CategoryObject> list) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public CategoryObject getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_category;
        private TextView txt_title_category, txt_count_category;

        public ViewHolder(View view) {
            super(view);
            layout_category=(RelativeLayout) view.findViewById(R.id.layout_category);
            txt_title_category=(TextView) view.findViewById(R.id.txt_title_category);
            txt_count_category=(TextView) view.findViewById(R.id.txt_count_category);
        }

        public void bindData(CategoryObject inviteObject) {
            txt_title_category.setText(inviteObject.getName());
            txt_count_category.setText("("+inviteObject.getNumber_shortlist()+")");

        }

    }

}
