package com.jimilab.uwclient.presenter;

import com.jimilab.uwclient.UwClientApplication;
import com.jimilab.uwclient.bean.BaseResult;
import com.jimilab.uwclient.bean.EmergencyTask;
import com.jimilab.uwclient.bean.ValuableBaseResult;
import com.jimilab.uwclient.model.IValuableModel;
import com.jimilab.uwclient.model.ValuableModel;
import com.jimilab.uwclient.view.IValuableView;

/**
 * @Author : LiangGuoChang
 * @Date : 2019-11-20
 * @描述 :
 */
public class ValuablePresenter<T extends IValuableView> extends BasePresenter<T> {
    private ValuableModel valuableModel;
    private UwClientApplication mApplication;

    public ValuablePresenter(UwClientApplication application) {
        valuableModel = new ValuableModel(application);
    }

    //加载盘点或者抽检任务列表
    public void fetchGetTaskList(IValuableView valuableView, String token) {
        tWeakReference.get().showLoading("正在加载...");
        valuableModel.getTaskList(valuableView, token, new IValuableModel.OnTaskLoadListener() {
            @Override
            public void onComplete(EmergencyTask baseResult) {
                //返回到UI
                tWeakReference.get().showTaskList(token, baseResult);
                tWeakReference.get().dismissLoading();
            }

            @Override
            public void onError(Throwable throwable) {
                tWeakReference.get().dismissLoading();
                tWeakReference.get().showToast("获取任务失败,请点击刷新");
            }
        });
    }

    //上传盘点或者抽检单个数据
    public void fetchUploadMaterial(IValuableView valuableView, String token, String mTaskId, String materialUnique, String count) {
        tWeakReference.get().showLoading("正在上传...");
        valuableModel.uploadMaterial(valuableView, token, mTaskId, materialUnique, count, new IValuableModel.OnUploadMaterialListener() {
            @Override
            public void onComplete(ValuableBaseResult baseResult) {
                tWeakReference.get().dismissLoading();
                if (baseResult.getResult().equalsIgnoreCase("200")) {
                    tWeakReference.get().showToast("上传成功!");
                    tWeakReference.get().setScanMaterialRequestFocus();
                    tWeakReference.get().onlodaSuccess();
                } else {
                    tWeakReference.get().showToast(baseResult.getData());
                    tWeakReference.get().scanNext();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                tWeakReference.get().dismissLoading();
                tWeakReference.get().showToast("上传数据异常,请重新扫描!");
                tWeakReference.get().scanNext();
            }
        });
    }

    //获取盘点的剩余数
    public void get_pandian_not(IValuableView valuableView, String taskId, String no, String supplierId) {
        valuableModel.get_pandian_not(valuableView,taskId, no,supplierId, new IValuableModel.LoadOkhttpListener() {
            @Override
            public void onComplete(String baseResult) {
                tWeakReference.get().set_has_not_saomiao_number(baseResult);
            }

            @Override
            public void onError(String throwable) {
                tWeakReference.get().show_error_form_okhttp(throwable);
            }
        });
    }
    //获取抽检的剩余数
    public void get_choujain_not(IValuableView valuableView, String taskId, String no, String supplierId) {
        valuableModel.get_choujain_not(valuableView,taskId, no, supplierId, new IValuableModel.LoadOkhttpListener() {
            @Override
            public void onComplete(String baseResult) {
                tWeakReference.get().set_has_not_saomiao_number(baseResult);
            }

            @Override
            public void onError(String throwable) {
                tWeakReference.get().show_error_form_okhttp(throwable);
            }
        });
    }

}
