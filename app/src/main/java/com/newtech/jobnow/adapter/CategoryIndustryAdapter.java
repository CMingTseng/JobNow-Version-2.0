package com.newtech.jobnow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.newtech.jobnow.R;
import com.newtech.jobnow.models.CategoryIndustryObject;
import com.newtech.jobnow.models.LevelObject;

import java.util.List;

/**
 * Created by manhi on 26/8/2016.
 */
public class CategoryIndustryAdapter extends ArrayAdapter<CategoryIndustryObject> {
    private Context context;
    private List<CategoryIndustryObject> list;

    public CategoryIndustryAdapter(Context context, List<CategoryIndustryObject> list) {
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