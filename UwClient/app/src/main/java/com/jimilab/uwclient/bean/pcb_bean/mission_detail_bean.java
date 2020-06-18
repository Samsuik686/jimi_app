package com.jimilab.uwclient.bean.pcb_bean;

import com.jimilab.uwclient.adapter.PcblmpDetaileAdapter;

import java.util.ArrayList;

public class mission_detail_bean {
    String id;
    String specifications;
    String place;
    String planNumber;
    String tureNumber;
    boolean spread = false;

    PcblmpDetaileAdapter adapter;

    public PcblmpDetaileAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(PcblmpDetaileAdapter adapter) {
        this.adapter = adapter;
    }


    public boolean isSpread() {
        return spread;
    }

    public void setSpread(boolean spread) {
        this.spread = spread;
    }

    ArrayList<mission_detail_son_bean> list = new ArrayList<mission_detail_son_bean>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPlanNumber() {
        return planNumber;
    }

    public void setPlanNumber(String planNumber) {
        this.planNumber = planNumber;
    }

    public String getTureNumber() {
        return tureNumber;
    }

    public void setTureNumber(String tureNumber) {
        this.tureNumber = tureNumber;
    }

    public ArrayList<mission_detail_son_bean> getList() {
        return list;
    }

    public void setList(ArrayList<mission_detail_son_bean> list) {
        this.list = list;
    }

    public mission_detail_bean(String id, String specifications, String place, String planNumber, String tureNumber, boolean spread, ArrayList<mission_detail_son_bean> list) {
        this.id = id;
        this.specifications = specifications;
        this.place = place;
        this.planNumber = planNumber;
        this.tureNumber = tureNumber;
        this.spread = spread;
        this.list = list;
    }
}
