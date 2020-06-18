package com.jimilab.uwclient.model;

import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jimilab.uwclient.BuildConfig;
import com.jimilab.uwclient.UwClientApplication;
import com.jimilab.uwclient.bean.pcb_bean.PcbPandianChoujianBean;
import com.jimilab.uwclient.utils.OkHttpUtils;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PcbModel implements inPcbModel {

    private UwClientApplication mApplication;

    public PcbModel(UwClientApplication application) {
        this.mApplication = application;
    }

    public void getData(String url, onStringLoadListener loadListener) {
        OkHttpUtils.sendOkHttpPostRequest(BuildConfig.BASE_URL + url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadListener.onError(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.d("sxmmmm", url + " result==" + body);
//                try {
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    int result = Integer.parseInt(jsonObject.getString("result"));
                    if (result == 200) {
                        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
                        ArrayList<PcbPandianChoujianBean> dataList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            PcbPandianChoujianBean missionBean = JSON.parseObject(jsonArray.get(i).toString(), PcbPandianChoujianBean.class);
                            dataList.add(missionBean);
                        }
                        loadListener.onComplete(dataList);

                    } else {
                        loadListener.onError(jsonObject.getString("data"));
                    }
//                } catch (Exception e) {
//                    loadListener.onError(e.toString());
//                }
            }
        }, "#TOKEN#", mApplication.getTOKEN());
    }

    public void SetMessageData(String url, String materialId, String taskId, String acturalNum, SendMessageListener loadListener) {
        OkHttpUtils.sendOkHttpPostRequest(BuildConfig.BASE_URL + url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("sxmmmm", e.toString());
                        loadListener.onError(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        Log.d("sxmmmm", "send_message_result==" + body);
                        loadListener.onComplete();
                    }
                }, "#TOKEN#", mApplication.getTOKEN()
                , "materialId", materialId
                , "taskId", taskId
                , "acturalNum", acturalNum);
    }

    public void getHasNot(String url, String taskId, String no, String supplierId, getHasNotListener loadListener) {

        OkHttpUtils.sendOkHttpPostRequest(BuildConfig.BASE_URL + url, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        com.jimilab.uwclient.utils.Log.d("sxmmmm", "IOException==" + e.toString());
                        loadListener.onError(e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        loadListener.onComplete(body);

                    }
                }, "taskId", taskId,
                "no", no,
                "supplierId", supplierId,
                "#TOKEN#", mApplication.getTOKEN());
    }
}
