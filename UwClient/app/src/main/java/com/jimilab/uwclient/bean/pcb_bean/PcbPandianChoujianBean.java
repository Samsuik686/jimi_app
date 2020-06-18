package com.jimilab.uwclient.bean.pcb_bean;

public class PcbPandianChoujianBean {
    String tv_supplier_name;
    String tv_task_name;

    public String getTv_supplier_name() {
        return tv_supplier_name;
    }

    public void setTv_supplier_name(String tv_supplier_name) {
        this.tv_supplier_name = tv_supplier_name;
    }

    public String getTv_task_name() {
        return tv_task_name;
    }

    public void setTv_task_name(String tv_task_name) {
        this.tv_task_name = tv_task_name;
    }

    public PcbPandianChoujianBean(String tv_supplier_name, String tv_task_name) {
        this.tv_supplier_name = tv_supplier_name;
        this.tv_task_name = tv_task_name;
    }

}
