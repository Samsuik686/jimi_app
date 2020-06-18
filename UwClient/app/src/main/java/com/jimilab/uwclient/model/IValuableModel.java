package com.jimilab.uwclient.model;

import com.jimilab.uwclient.bean.BaseResult;
import com.jimilab.uwclient.bean.EmergencyTask;
import com.jimilab.uwclient.bean.ValuableBaseResult;
import com.jimilab.uwclient.view.IValuableView;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;

/**
 * @Author : LiangGuoChang
 * @Date : 2019-11-20
 * @描述 :
 */
public interface IValuableModel {

    void getTaskList(IValuableView valuableView, String token, OnTaskLoadListener loadListener);

    void uploadMaterial(IValuableView valuableView, String token, String mTaskId, String materialUnique, String count, OnUploadMaterialListener uploadMaterialListener);

    interface OnTaskLoadListener {
        void onComplete(EmergencyTask emergencyTask);

        void onError(Throwable throwable);
    }


     interface OnUploadMaterialListener {
        void onComplete(ValuableBaseResult baseResult);

        void onError(Throwable throwable);
    }

    interface LoadOkhttpListener {
        void onComplete(String string);

        void onError(String string);
    }


}
