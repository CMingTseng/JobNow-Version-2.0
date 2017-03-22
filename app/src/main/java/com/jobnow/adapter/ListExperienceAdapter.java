package com.jobnow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jobnow.models.ExperienceObject;
import com.newtech.jobnow.R;

import java.util.List;

/**
 * Created by manhi on 26/8/2016.
 */
public class ListExperienceAdapter extends ArrayAdapter<ExperienceObject> {
    private Context context;
    private List<ExperienceObject> list;

    public ListExperienceAdapter(Context context, List<ExperienceObject> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    public String getWordbyPosition(int position) {
        return getItem(position).Name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).Name;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_industry, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(name);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).Name;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_industry, parent, false);
        }
        TextView tvWord = (TextView) convertView.findViewById(R.id.tvName);
        tvWord.setText(name);

        return convertView;
    }
}