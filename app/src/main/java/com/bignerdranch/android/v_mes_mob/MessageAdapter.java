package com.bignerdranch.android.v_mes_mob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Message> objects;

    MessageAdapter(Context context, ArrayList<Message> products) {
        ctx = context;
        objects = products;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_message, parent, false);
        }

        Message p = getMessage(position);

        ((TextView) view.findViewById(R.id.item_tv_name)).setText(p.getName());
        ((TextView) view.findViewById(R.id.item_tv_mes)).setText(p.getMessage());
        return view;
    }

    Message getMessage(int position) {
        return ((Message) getItem(position));
    }
}