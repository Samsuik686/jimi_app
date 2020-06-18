package com.jimilab.uwclient.bean.pcb_bean;

public class mission_detail_son_bean {

    String fatherid;
    String number;
    String materialId;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getFatherid() {
        return fatherid;
    }

    public void setFatherid(String fatherid) {
        this.fatherid = fatherid;
    }

    public mission_detail_son_bean(String fatherid, String materialId, String number) {
        this.fatherid = fatherid;
        this.number = number;
        this.materialId = materialId;
    }
}
