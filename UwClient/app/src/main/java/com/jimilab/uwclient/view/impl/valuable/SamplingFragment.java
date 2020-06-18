package com.jimilab.uwclient.view.impl.valuable;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jimilab.uwclient.BuildConfig;
import com.jimilab.uwclient.R;
import com.jimilab.uwclient.adapter.TaskSpinnerAdapter;
import com.jimilab.uwclient.bean.BaseResult;
import com.jimilab.uwclient.bean.ChipTask;
import com.jimilab.uwclient.bean.EmergencyTask;
import com.jimilab.uwclient.model.IValuableModel;
import com.jimilab.uwclient.presenter.ValuablePresenter;
import com.jimilab.uwclient.utils.Log;
import com.jimilab.uwclient.utils.OkHttpUtils;
import com.jimilab.uwclient.utils.Tool;
import com.jimilab.uwclient.view.IValuableView;
import com.jimilab.uwclient.view.impl.BaseFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SamplingFragment extends BaseFragment<ValuablePresenter<IValuableView>, IValuableView> implements IValuableView {
    String number;
    String error_message;
    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 1:
                    Tool.showToast(mContext, error_message);
                    break;
                case 2:
                    not_saomiao_number_pandian.setText(number);
                    break;
                case 3:
                    spinnerAdapter.notifyDataSetChanged();
                    samplingSpinner.setSelection(0);
                    break;
            }
        }
    };

    private static final String TAG = SamplingFragment.class.getSimpleName();
    @BindView(R.id.sampling_spinner)
    Spinner samplingSpinner;
    @BindView(R.id.tv_scan)
    TextView tvScan;
    @BindView(R.id.tv_material_unique)
    TextView tvMaterialUnique;
    @BindView(R.id.tv_sampling_num)
    TextView tvSamplingNum;
    @BindView(R.id.tv_operate_id)
    TextView tvOperateId;
    @BindView(R.id.btn_fresh_sampling)
    Button btnFreshSampling;
    @BindView(R.id.et_sampling_material)
    EditText etSamplingMaterial;
    @BindView(R.id.clock_sampling)
    Button clock;
    @BindView(R.id.not_saomiao_number_pandian)
    TextView not_saomiao_number_pandian;


    private EmergencyTask.DataBean topBean;
    //    private List<CheckTask.DataBean.ListBean> beanList = new ArrayList<>();
    private List<EmergencyTask.DataBean> beanList = new ArrayList<>();
    private TaskSpinnerAdapter<EmergencyTask.DataBean> spinnerAdapter;
    private String mTaskId;
    private boolean hasLocked=false;
    int pos;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void init(Bundle bundle) {
        //必须调用
        ButterKnife.bind(this, view);
        initViewEvent();
        clock.setText("锁定");
        topBean = new EmergencyTask.DataBean();
        topBean.setSupplierName("");
        topBean.setFile_name("请选择抽检任务");
        beanList.add(0, topBean);
        spinnerAdapter = new TaskSpinnerAdapter<>(beanList, uwClientApplication);
        samplingSpinner.setAdapter(spinnerAdapter);
        samplingSpinner.setSelection(0);
        presenter.fetchGetTaskList(this, uwClientApplication.getTOKEN());
    }
    private void initViewEvent() {
        //任务选择监听
        samplingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + position);

                mTaskId = "";
                if (position > 0) {
                    pos = position;
                    mTaskId = String.valueOf(beanList.get(position).getId());
                    scanNext();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTaskId.isEmpty()){
                    if (hasLocked == false){
                        hasLocked = true;
                        clock.setText("解锁");
                        showToast("任务已经锁定，如需更换任务或刷新请解锁");
                        btnFreshSampling.setEnabled(false);
                        samplingSpinner.setEnabled(false);
                    }else {
                        hasLocked = false;
                        clock.setText("锁定");
                        showToast("已解锁，可刷新或切换任务");
                        btnFreshSampling.setEnabled(true);
                        samplingSpinner.setEnabled(true);
                    }
                }else {
                    showToast("请先选择任务");
                }


            }
        });

        //扫描输入监听
        etSamplingMaterial.setOnEditorActionListener(mEditorActionListener);
    }

    String[] materialValues;
    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                if (!mTaskId.isEmpty()) {
                    if(hasLocked == true){
                        if (event.getAction() == KeyEvent.ACTION_UP) {
                            String scanValue = String.valueOf(v.getText()).replace("\r", "");
                            Log.d(TAG, "scanValue - " + scanValue);

                            tvScan.setText(scanValue);

                            //料号@数量@时间戳(唯一码)@打印人员@供应商@站位@序号@生产日期@描述@
                            materialValues = Tool.parserMaterialNo(scanValue);
                            if (materialValues.length >= 9) {
                                tvSamplingNum.setText(materialValues[1]);
                                tvMaterialUnique.setText(materialValues[2]);
                                tvOperateId.setText(materialValues[0]);

                                //上传数据
                                presenter.fetchUploadMaterial(SamplingFragment.this, uwClientApplication.getTOKEN(), mTaskId,
                                        tvMaterialUnique.getText().toString().trim(), tvSamplingNum.getText().toString().trim());

                            } else {
                                showToast("料号不正确,请重新扫描!");
                                scanNext();
                            }
                        }
                        return true;
                    }else {
                        showToast("请锁定任务任务!");
                    }


                } else {
                    showToast("请选择抽检任务!");
                }

            }
            return false;
        }
    };

    @Override
    protected ValuablePresenter<IValuableView> createPresenter() {
        return new ValuablePresenter<>(uwClientApplication);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_sampling;
    }

    @Override
    protected void destroy() {

    }

    @Override
    public void showTaskList(String token, BaseResult taskList) {
        if (taskList != null) {
            List<EmergencyTask.DataBean> list = ((EmergencyTask) taskList).getData();
            beanList.clear();
            beanList.add(0, topBean);
            beanList.addAll(1, list);
            spinnerAdapter.notifyDataSetChanged();
            samplingSpinner.setSelection(0);
        }
    }

    @Override
    public void scanNext() {
        tvScan.setText("");
        tvMaterialUnique.setText("");
        tvSamplingNum.setText("");
        tvOperateId.setText("");
        setScanMaterialRequestFocus();
    }

    @Override
    public void setScanMaterialRequestFocus() {
        etSamplingMaterial.setText("");
        etSamplingMaterial.requestFocus();
    }

    @Override
    public void set_has_not_saomiao_number(String str) {
        Message message = new Message();
        message.what=2;
        handler.sendMessage(message);
        number = str;
    }

    @Override
    public void show_error_form_okhttp(String str) {
        error_message = str;
        Message message = new Message();
        message.what=1;
        handler.sendMessage(message);
    }

    @Override
    public void onlodaSuccess() {
        Log.d("sxmmm","执行获取没抽检任务数量方法");
        presenter.get_choujain_not(SamplingFragment.this, mTaskId,materialValues[0],String.valueOf(beanList.get(pos).getSupplier()));
    }

    @OnClick(R.id.btn_fresh_sampling)
    public void onViewClicked() {
//        getlist();
        presenter.fetchGetTaskList(this, uwClientApplication.getTOKEN());
    }
}
