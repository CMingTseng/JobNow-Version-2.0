package com.newtech.jobnow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.newtech.jobnow.R;
import com.newtech.jobnow.models.EmploymentObject;
import com.newtech.jobnow.models.LocationObject;

import java.util.List;

public class EmploymentTypeAdapter extends ArrayAdapter<EmploymentObject> {
    private Context context;
    private List<EmploymentObject> list;

    public EmploymentTypeAdapter(Context context, List<EmploymentObject> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    public String getWordbyPosition(int position) {
        return getItem(position).NameType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).NameType;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_industry, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(name);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).NameType;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_industry, parent, false);
        }
        TextView tvWord = (TextView) convertView.findViewById(R.id.tvName);
        tvWord.setText(name);

        return convertView;
    }
}