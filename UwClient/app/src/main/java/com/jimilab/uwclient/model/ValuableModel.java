package com.jimilab.uwclient.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jimilab.uwclient.BuildConfig;
import com.jimilab.uwclient.UwClientApplication;
import com.jimilab.uwclient.bean.CheckTask;
import com.jimilab.uwclient.bean.ChipTask;
import com.jimilab.uwclient.bean.EmergencyTask;
import com.jimilab.uwclient.bean.ValuableBaseResult;
import com.jimilab.uwclient.utils.Log;
import com.jimilab.uwclient.utils.OkHttpUtils;
import com.jimilab.uwclient.view.IValuableView;
import com.jimilab.uwclient.view.impl.valuable.CheckFragment;
import com.jimilab.uwclient.view.impl.valuable.SamplingFragment;

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
 * @Date : 2019-11-20
 * @描述 :
 */
public class ValuableModel implements IValuableModel {
    private static final String TAG = ValuableModel.class.getSimpleName();
    private UwClientApplication mApplication;

    public ValuableModel(UwClientApplication application) {
        this.mApplication = application;
    }

    public void getTaskList(IValuableView valuableView, String token, OnTaskLoadListener loadListener) {
        if (valuableView instanceof CheckFragment) {
            getCheckTaskList(token, loadListener);
        } else if (valuableView instanceof SamplingFragment) {
            getChipTaskList(token, loadListener);
        }
    }

    @Override
    public void uploadMaterial(IValuableView valuableView, String token, String mTaskId, String materialUnique, String count, OnUploadMaterialListener uploadMaterialListener) {
        if (valuableView instanceof CheckFragment) {
            uploadCheckMaterial(token, mTaskId, materialUnique, count, uploadMaterialListener);
        } else if (valuableView instanceof SamplingFragment) {
            uploadChipMaterial(token, mTaskId, materialUnique, count, uploadMaterialListener);
        }
    }

    //上传单个抽检数据
    private void uploadChipMaterial(String token, String mTaskId, String materialUnique, String count, OnUploadMaterialListener uploadMaterialListener) {
        Map<String, String> checkParam = new HashMap<>();
        checkParam.put("#TOKEN#", token);
        checkParam.put("taskId", mTaskId);
        checkParam.put("materialId", materialUnique);
        checkParam.put("acturalNum", count);

        Log.d(TAG, "uploadChipMaterial: " + checkParam);
        mApplication.getNetApi()
                .uploadChipMaterial(checkParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ValuableBaseResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ValuableBaseResult baseResult) {

                        if (baseResult != null) {
                            Log.d(TAG, "onNext: " + baseResult.toString());
                            uploadMaterialListener.onComplete(baseResult);
                        } else {
                            uploadMaterialListener.onError(new NullPointerException("ValuableModel - onNext : uploadChipMaterial return null"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        uploadMaterialListener.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //上传单个盘点数据
    private void uploadCheckMaterial(String token, String mTaskId, String materialUnique, String count, OnUploadMaterialListener uploadMaterialListener) {
        Map<String, String> checkParam = new HashMap<>();
        checkParam.put("#TOKEN#", token);
        checkParam.put("taskId", mTaskId);
        checkParam.put("materialId", materialUnique);
        checkParam.put("acturalNum", count);

        Log.d(TAG, "uploadCheckMaterial: " + checkParam);
        mApplication.getNetApi()
                .uploadCheckMaterial(checkParam)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ValuableBaseResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ValuableBaseResult baseResult) {

                        if (baseResult != null) {
                            Log.d(TAG, "onNext: " + baseResult.toString());
                            uploadMaterialListener.onComplete(baseResult);
                        } else {
                            uploadMaterialListener.onError(new NullPointerException("ValuableModel - onNext : uploadCheckMaterial return null"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.toString());
                        uploadMaterialListener.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //获取盘点的任务
    private void getCheckTaskList(String token, OnTaskLoadListener loadListene) {
        mApplication.getNetApi()
                .getCheckTask(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EmergencyTask>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EmergencyTask emergencyTask) {
                        if (emergencyTask != null) {
                            loadListene.onComplete(emergencyTask);
                        } else {
                            loadListene.onError(new NullPointerException("get task list null"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadListene.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    //获取抽检的任务
    private void getChipTaskList(String token, OnTaskLoadListener loadListene) {
        mApplication.getNetApi()
                .getChipTask(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EmergencyTask>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(EmergencyTask emergencyTask) {
                        if (emergencyTask != null) {
                            loadListene.onComplete(emergencyTask);
                        } else {
                            loadListene.onError(new NullPointerException("get task list null"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadListene.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    public void get_pandian_not(IValuableView valuableView,String taskId, String no, String supplierId, LoadOkhttpListener LoadOkhttpListener) {
        get_pandianokhttp(taskId, no, supplierId, LoadOkhttpListener);


    }
    public void get_choujain_not(IValuableView valuableView,String taskId, String no, String supplierId, LoadOkhttpListener LoadOkhttpListener) {

        get_choujianokhttp(taskId, no, supplierId, LoadOkhttpListener);

    }
    public void get_choujianokhttp(String taskId, String no, String supplierId, LoadOkhttpListener LoadOkhttpListener){
        //Precious
        OkHttpUtils.sendOkHttpPostRequest(BuildConfig.BASE_URL + "pda/getUnScanSampleTaskMaterial", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        LoadOkhttpListener.onError(e.toString());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        android.util.Log.d("sxmmmm", "choujian_load_plan_fact_result=="+body);
                        try {
                            JSONObject jsonObject = JSONObject.parseObject(body);
                            int result = Integer.parseInt(jsonObject.getString("result"));
                            if (result==200){
                                LoadOkhttpListener.onComplete(jsonObject.getString("data"));
                            }else {
                                LoadOkhttpListener.onError(jsonObject.getString("data"));
                            }
                        }catch (Exception ex){
                            LoadOkhttpListener.onError(ex.toString());
                        }

                    }
                },"taskId",taskId,
                "no",no,
                "supplierId",supplierId,
                "#TOKEN#",mApplication.getTOKEN());
    }
    public void get_pandianokhttp(String taskId, String no, String supplierId, LoadOkhttpListener LoadOkhttpListener){
        Log.d("sxmmm","taskId=="+taskId+"  no=="+no+"  supplierId=="+supplierId+ "  #TOKEN#=="+mApplication.getTOKEN());
        //Precious
        OkHttpUtils.sendOkHttpPostRequest(BuildConfig.BASE_URL + "pda/getUnScanInventoryTaskMaterial", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("sxmmmm", "IOException=="+e.toString());
                        LoadOkhttpListener.onError(e.toString());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        Log.d("sxmmmm", "pandian_load_plan_fact_result=="+body);
                        try {
                            JSONObject jsonObject = JSONObject.parseObject(body);
                            int result = Integer.parseInt(jsonObject.getString("result"));
                            if (result==200){
                                LoadOkhttpListener.onComplete(jsonObject.getString("data"));
                            }else {
                                LoadOkhttpListener.onError(jsonObject.getString("data"));
                            }
                        }catch (Exception ex){
                            LoadOkhttpListener.onError(ex.toString());
                        }

                    }
                },"taskId",taskId,
                "no",no,
                "supplierId",supplierId,
                "#TOKEN#",mApplication.getTOKEN());
    }
}
