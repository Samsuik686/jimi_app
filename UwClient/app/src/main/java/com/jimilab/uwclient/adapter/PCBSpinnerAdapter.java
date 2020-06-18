package com.jimilab.uwclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimilab.uwclient.R;
import com.jimilab.uwclient.bean.CheckTask;
import com.jimilab.uwclient.bean.pcb_bean.manufacturer_bean;
import com.jimilab.uwclient.bean.pcb_bean.mission_bean;
import com.jimilab.uwclient.bean.pcb_bean.supplier_bean;


import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;

public class PCBSpinnerAdapter<T> extends BaseAdapter {
    private Context ctx;
    private LayoutInflater li;
    private ArrayList<T> dataList;

    public PCBSpinnerAdapter(Context ctx,ArrayList<T> dataList) {
        this.ctx = ctx;
        this.li = LayoutInflater.from(ctx);
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.item_text, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();// get convertView's holder
        T bean = dataList.get(position);
        if (bean instanceof manufacturer_bean) {
            holder.name.setText(((manufacturer_bean)bean).getManufacturer_name());
        }else if (bean instanceof supplier_bean) {
            holder.name.setText(((supplier_bean)bean).getSupplier_name());
        }
        else if (bean instanceof mission_bean) {
            holder.name.setText(((mission_bean)bean).getMission_name());
        }
        return convertView;
    }

    class ViewHolder {
        TextView name;



        public ViewHolder(View convertView){
            name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(this);//set a viewholder
        }
    }


}