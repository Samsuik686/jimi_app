package com.jimilab.uwclient.presenter;

import com.jimilab.uwclient.UwClientApplication;
import com.jimilab.uwclient.bean.pcb_bean.PcbPandianChoujianBean;
import com.jimilab.uwclient.model.PcbCallBack;
import com.jimilab.uwclient.model.PcbModel;
import com.jimilab.uwclient.model.inPcbModel;
import java.util.ArrayList;

public class PcbPresent {
    private PcbCallBack CallBack;
    private PcbModel model;
    public PcbPresent(PcbCallBack callBack,UwClientApplication application) {
        this.CallBack = callBack;
        model=new PcbModel(application);
    }

    public void getListData(String url){
        model.getData(url,new inPcbModel.onStringLoadListener() {

            @Override
            public void onComplete(ArrayList<PcbPandianChoujianBean> dataList) {
                CallBack.showMessage(dataList);
            }

            @Override
            public void onError(String throwable) {
                CallBack.error(throwable);
            }
        });
    }

    public void SendMessage(String url,String materialId,String taskId,String acturalNum){
        model.SetMessageData( url, materialId, taskId, acturalNum,new inPcbModel.SendMessageListener() {
            @Override
            public void onComplete() {
                CallBack.Success("");
            }

            @Override
            public void onError(String error_message) {
                CallBack.error(error_message);
            }
        });
    }
    public void getHasNot(String url,String taskId, String no, String supplierId){
        model.getHasNot( url, taskId,  no,  supplierId,new inPcbModel.getHasNotListener() {
            @Override
            public void onComplete(String string) {
                CallBack.getHasNot(string);
            }

            @Override
            public void onError(String string) {
                CallBack.error(string);
            }
        });
    }

}
