package com.jimilab.uwclient.bean.pcb_bean;

public class mission_bean {
    String mission_name;
    String mission_id;

    public String getMission_name() {
        return mission_name;
    }

    public void setMission_name(String mission_name) {
        this.mission_name = mission_name;
    }

    public String getMission_id() {
        return mission_id;
    }

    public void setMission_id(String mission_id) {
        this.mission_id = mission_id;
    }

    public mission_bean(String mission_name, String mission_id) {
        this.mission_name = mission_name;
        this.mission_id = mission_id;
    }
}
