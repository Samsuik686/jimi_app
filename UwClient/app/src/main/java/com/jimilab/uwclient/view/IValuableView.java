package com.jimilab.uwclient.view;

import com.jimilab.uwclient.bean.BaseResult;

/**
 * @Author : LiangGuoChang
 * @Date : 2019-11-20
 * @描述 :
 */
public interface IValuableView extends IBaseView {
    void showTaskList(String token, BaseResult taskList);

    void scanNext();

    void setScanMaterialRequestFocus();

    void set_has_not_saomiao_number(String str);

    void show_error_form_okhttp(String str);

    void onlodaSuccess();
}
