package com.jimilab.uwclient.model;

import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jimilab.uwclient.BuildConfig;
import com.jimilab.uwclient.UwClientApplication;
import com.jimilab.uwclient.bean.BaseResult;
import com.jimilab.uwclient.bean.EmergencyTask;
import com.jimilab.uwclient.utils.OkHttpUtils;
import com.jimilab.uwclient.view.impl.incomeArea.InComeAreaImplActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @Author : LiangGuoChang
 * @Date : 2019-11-26
 * @描述 :
 */
public class ComeAreaModel  implements InComeAreaModel  {

    private String TAG = "ComeAreaModel";
    private UwClientApplication mApplication;

    public ComeAreaModel(UwClientApplication application) {
        this.mApplication = application;
    }


    @Override
    public void loadTaskList(String token, onLoadListener loadListener) {
        mApplication.getNetApi()
                .getEmergencyRegularTasks(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EmergencyTask>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EmergencyTask emergencyTask) {
                        Log.d("sxmmm","comearea=="+emergencyTask.toString());
                        if (emergencyTask != null) {
                            loadListener.onComplete(emergencyTask);
                        } else {
                            loadListener.onError(new NullPointerException("get task list null"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadListener.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传出库料信息
     *
     * @param taskID           任务ID
     * @param material         解析料二维码的信息
     * @param itemLoadListener 结果回调接口
     */
    @Override
    public void uploadMaterialItem(String taskID, String[] material, onItemLoadListener itemLoadListener) {
        /* 上传来料区出库料号
         * #TOKEN#
         * taskId 任务ID
         * no 料号
         * materialId 时间戳
         * quantity 数量
         * productionTime 生产日期
         * supplierName 供应商名
         * cycle 周期
         * manufacturer 产商
         * printTime 打印时间
         */
        //料号@数量@时间戳(唯一码)@打印人员@供应商@位置@物料类型（标准/非标准）@生产日期@型号@产商@周期@打印日期@
        Map<String, String> params = new HashMap<>();
        params.put("#TOKEN#", mApplication.getTOKEN());
        params.put("taskId", taskID);
        params.put("no", material[0]);
        params.put("materialId", material[2]);
        params.put("quantity", material[1]);
        params.put("productionTime", material[7]);
        params.put("supplierName", material[4]);
        params.put("cycle", material[10]);
        params.put("manufacturer", material[9]);
        params.put("printTime", material[11]);

        Log.d(TAG, "uploadMaterialItem: " + params);

        mApplication.getNetApi()
                .uploadMaterial(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResult<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResult<String> stringBaseResult) {
                        if (stringBaseResult != null) {
                            int code = stringBaseResult.getResultCode();
                            if (code == 200) {
                                itemLoadListener.onComplete(material);
                            } else {
                                itemLoadListener.onError(new Exception(stringBaseResult.getData()));
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        itemLoadListener.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void load_plan_fact(String taskId, String no, onStringLoadListener onLoadListener) {
        Log.d("sxmmm","id=="+taskId+"  no=="+no+"  talk=="+mApplication.getTOKEN());
        OkHttpUtils.sendOkHttpPostRequest(BuildConfig.BASE_URL + "pda/getEmergencyRegularTaskInfo", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onLoadListener.onError(e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("sxmmmm", "load_plan_fact_result=="+body);
                JSONObject jsonObject = JSONObject.parseObject(body);
                int result = Integer.parseInt(jsonObject.getString("result"));
                if (result==200){
                    try {
                        JSONArray jsonArray= JSONArray.parseArray(jsonObject.getString("data"));
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        onLoadListener.onComplete(jsonObject1.getString("planQuantity"),jsonObject1.getString("actualQuantity"));

                    }catch (Exception e){
                        onLoadListener.onError(jsonObject.getString(e.toString()));
                    }
                    }else {
                    onLoadListener.onError(jsonObject.getString("data"));
                }
            }
        },"taskId",taskId,
                "no",no,
                "#TOKEN#",mApplication.getTOKEN());

    }

}
