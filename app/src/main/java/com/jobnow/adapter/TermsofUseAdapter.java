package com.jobnow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jobnow.models.TermObject;
import com.newtech.jobnow.R;
import com.ocpsoft.pretty.time.PrettyTime;

import java.util.List;

/**
 * Created by manhi on 1/6/2016.
 */

public class TermsofUseAdapter extends BaseRecyclerAdapter<TermObject, TermsofUseAdapter.ViewHolder> {

    public static final String TAG = TermsofUseAdapter.class.getSimpleName();
    public static final int NORMAL_TYPE = 0;
    public static final int SAVE_TYPE = 1;
    public static final int APPLY_TYPE = 2;
    private PrettyTime p;
    private int type=0;

    public TermsofUseAdapter(Context context, List<TermObject> list, int type) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = type;
    }

    public TermsofUseAdapter(Context context, List<TermObject> list) {
        super(context, list);
        this.p = new PrettyTime();
        this.type = 0;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.term_use, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public TermObject getItembyPostion(int position) {
        return super.getItembyPostion(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout_invite;
        private TextView txt_title_termuse, txt_content;

        public ViewHolder(View view) {
            super(view);
            txt_title_termuse=(TextView) view.findViewById(R.id.txt_title_termuse);
            txt_content=(TextView) view.findViewById(R.id.txt_content);

        }

        public void bindData(TermObject object) {
            if(type==1){
                txt_title_termuse.setText(Html.fromHtml(object.Title));
                txt_content.setText(Html.fromHtml(object.Description));
            }else {
                txt_title_termuse.setText(object.Title);
                txt_content.setText(object.Description);
            }

        }

    }

}
