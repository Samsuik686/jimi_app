package com.jimilab.uwclient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jimilab.uwclient.R;
import com.jimilab.uwclient.bean.pcb_bean.PcbPandianChoujianBean;

import java.util.ArrayList;

public class Pcb_Pandian_Choujian_adapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<PcbPandianChoujianBean> dataList;

    public Pcb_Pandian_Choujian_adapter(Context ctx,ArrayList<PcbPandianChoujianBean> dataList) {
        this.ctx = ctx;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(ctx, R.layout.task_spinner_item, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();// get convertView's holder
        holder.tv_supplier_name.setText(dataList.get(position).getTv_supplier_name());
        holder.tv_task_name.setText(dataList.get(position).getTv_task_name());
        return convertView;
    }

    class ViewHolder {
        TextView tv_supplier_name;
        TextView tv_task_name;


        public ViewHolder(View convertView){
            tv_supplier_name = (TextView) convertView.findViewById(R.id.tv_supplier_name);
            tv_task_name = (TextView) convertView.findViewById(R.id.tv_task_name);
            convertView.setTag(this);//set a viewholder
        }
    }


}