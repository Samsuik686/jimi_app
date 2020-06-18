package com.jimilab.uwclient.model;

import com.jimilab.uwclient.bean.pcb_bean.PcbPandianChoujianBean;

import java.util.ArrayList;

public interface PcbCallBack {
    void showMessage(ArrayList<PcbPandianChoujianBean> dataList);
    void Success(String string);
    void getHasNot(String string);
    void error(String string);
}
