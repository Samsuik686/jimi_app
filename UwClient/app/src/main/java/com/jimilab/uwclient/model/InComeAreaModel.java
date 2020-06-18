package com.jimilab.uwclient.model;

import com.jimilab.uwclient.bean.EmergencyTask;

/**
 * @Author : LiangGuoChang
 * @Date : 2019-11-26
 * @描述 :
 */
public interface InComeAreaModel {


    void loadTaskList(String token, onLoadListener loadListener);

    void uploadMaterialItem(String taskID, String[] material, onItemLoadListener itemLoadListener);

    void load_plan_fact(String taskId, String no, onStringLoadListener onLoadListener);

    interface onItemLoadListener {
        void onComplete(String[] item);

        void onError(Throwable throwable);
    }

    interface onLoadListener {
        void onComplete(EmergencyTask emergencyTask);

        void onError(Throwable throwable);
    }

    interface onStringLoadListener {
        void onComplete(String data1,String data2);

        void onError(String throwable);
    }
}
