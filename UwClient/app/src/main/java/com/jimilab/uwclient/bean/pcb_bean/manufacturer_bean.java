package com.jimilab.uwclient.bean.pcb_bean;

public class manufacturer_bean {
    String manufacturer_name;
    String manufacturer_id;

    public String getManufacturer_name() {
        return manufacturer_name;
    }

    public void setManufacturer_name(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }

    public String getManufacturer_id() {
        return manufacturer_id;
    }

    public void setManufacturer_id(String manufacturer_id) {
        this.manufacturer_id = manufacturer_id;
    }

    public manufacturer_bean(String manufacturer_name, String manufacturer_id) {
        this.manufacturer_name = manufacturer_name;
        this.manufacturer_id = manufacturer_id;
    }
}
