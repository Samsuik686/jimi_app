package com.jimilab.uwclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jimilab.uwclient.R;
import com.jimilab.uwclient.bean.pcb_bean.mission_detail_bean;
import com.jimilab.uwclient.bean.pcb_bean.mission_detail_son_bean;
import com.jimilab.uwclient.utils.Log;

import java.util.List;

public class PcbImpAdapter extends RecyclerView.Adapter<PcbImpAdapter.ViewHolder> {

    List<mission_detail_bean> mlist;
    Context mcontext;

    public PcbImpAdapter(List<mission_detail_bean> mlist,Context mcontext){
        this.mcontext = mcontext;
        this.mlist = mlist;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView number;
        TextView id;
        TextView specifications;
        TextView locan;
        TextView planNumber;
        TextView tureNumber;
        ImageView open_close;
        RecyclerView pcb_son_recycleview;
        LinearLayout son_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            id = itemView.findViewById(R.id.id);
            specifications = itemView.findViewById(R.id.specifications);
            locan = itemView.findViewById(R.id.locan);
            planNumber = itemView.findViewById(R.id.planNumber);
            tureNumber = itemView.findViewById(R.id.tureNumber);
            open_close = itemView.findViewById(R.id.open_close);
            pcb_son_recycleview = itemView.findViewById(R.id.pcb_son_recycleview);
            son_title = itemView.findViewById(R.id.son_title);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bcp,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("sxmmm","position=="+position);
        mission_detail_bean bean = mlist.get(position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        holder.pcb_son_recycleview.setLayoutManager(linearLayoutManager);
        PcblmpDetaileAdapter adapter = new PcblmpDetaileAdapter(bean.getList(), mcontext);
        bean.setAdapter(adapter);
        holder.pcb_son_recycleview.setAdapter(adapter);
        holder.number.setText(String.valueOf(position+1));
        holder.id.setText(bean.getId());
        holder.specifications.setText(bean.getSpecifications());
        holder.planNumber.setText(bean.getPlanNumber());
        holder.tureNumber.setText(bean.getTureNumber());
        if (bean.isSpread()){
            holder.pcb_son_recycleview.setVisibility(View.VISIBLE);
            holder.son_title.setVisibility(View.VISIBLE);
            holder.open_close.setImageResource(R.drawable.close_pcb_detail);
        }else {
            holder.pcb_son_recycleview.setVisibility(View.GONE);
            holder.son_title.setVisibility(View.GONE);
            holder.open_close.setImageResource(R.drawable.open_down);
        }
        holder.open_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlist.get(position).isSpread()==false){
                    holder.pcb_son_recycleview.setVisibility(View.VISIBLE);
                    holder.son_title.setVisibility(View.VISIBLE);
                    holder.open_close.setImageResource(R.drawable.close_pcb_detail);
                    bean.setSpread(true);
                }else if (mlist.get(position).isSpread()==true){
                    holder.pcb_son_recycleview.setVisibility(View.GONE);
                    holder.son_title.setVisibility(View.GONE);
                    holder.open_close.setImageResource(R.drawable.open_down);
                    bean.setSpread(false);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public void addnewPcblmpDetail(mission_detail_son_bean bean, int i){
        mlist.get(i).getList().add(bean);
        mlist.get(i).getAdapter().notifyDataSetChanged();
    }

}
