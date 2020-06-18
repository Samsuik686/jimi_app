package com.jimilab.uwclient.bean.pcb_bean;

public class type_bean {
    String type;
    String typeid;

    public type_bean(String type, String typeid){
        this.type = type;
        this.typeid = typeid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

}
