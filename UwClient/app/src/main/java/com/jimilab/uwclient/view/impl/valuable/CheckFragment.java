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
import com.jimilab.uwclient.bean.CheckTask;
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

public class CheckFragment extends BaseFragment<ValuablePresenter<IValuableView>, IValuableView> implements IValuableView {
    String error_message;
    String number;
    int pos;
    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 1:
                    Tool.showToast(mContext, error_message);
                    break;
                case 2:
                    not_saomiao_number_check.setText(number);
                    break;
                case 3:
                    spinnerAdapter.notifyDataSetChanged();
                    checkSpinner.setSelection(0);
                    break;
            }
        }
    };




    private static final String TAG = CheckFragment.class.getSimpleName();
    @BindView(R.id.check_spinner)
    Spinner checkSpinner;
    @BindView(R.id.tv_scan_check)
    TextView tvScanCheck;
    @BindView(R.id.tv_check_material_unique)
    TextView tvCheckMaterialUnique;
    @BindView(R.id.tv_check_num)
    TextView tvCheckNum;
    @BindView(R.id.tv_check_id)
    TextView tvCheckId;
    @BindView(R.id.btn_fresh_check)
    Button btnFreshCheck;
    @BindView(R.id.et_check_material)
    EditText etCheckMaterial;
    @BindView(R.id.clock)
    Button clock;
    @BindView(R.id.not_saomiao_number_check)
    TextView not_saomiao_number_check;


    private EmergencyTask.DataBean topBean;
//    private List<CheckTask.DataBean.ListBean> beanList = new ArrayList<>();
    private List<EmergencyTask.DataBean> beanList = new ArrayList<>();
    private TaskSpinnerAdapter<EmergencyTask.DataBean> spinnerAdapter;
    private String mTaskId;
    private boolean hasLocked=false;

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
        hasLocked=false;
        topBean = new EmergencyTask.DataBean();
        topBean.setSupplierName("");
        topBean.setFile_name("请选择盘点任务");
        beanList.add(0, topBean);
        spinnerAdapter = new TaskSpinnerAdapter<>(beanList, uwClientApplication);
        checkSpinner.setAdapter(spinnerAdapter);
        checkSpinner.setSelection(0);
        presenter.fetchGetTaskList(this, uwClientApplication.getTOKEN());
    }
    private void initViewEvent() {
        //任务选择监听
        checkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        btnFreshCheck.setEnabled(false);
                        checkSpinner.setEnabled(false);
                    }else {
                        hasLocked = false;
                        clock.setText("锁定");
                        showToast("已解锁，可刷新或切换任务");
                        btnFreshCheck.setEnabled(true);
                        checkSpinner.setEnabled(true);
                    }
                }else {
                    showToast("请先选择任务");
                }

            }
        });

        //扫描输入监听
        etCheckMaterial.setOnEditorActionListener(mEditorActionListener);
    }
    String[] materialValues;
    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                if (!mTaskId.isEmpty()) {
                    if (hasLocked == true){
                        if (event.getAction() == KeyEvent.ACTION_UP) {
                            String scanValue = String.valueOf(v.getText()).replace("\r", "");
                            Log.d(TAG, "scanValue - " + scanValue);

                            tvScanCheck.setText(scanValue);

                            //料号@数量@时间戳(唯一码)@打印人员@供应商@站位@序号@生产日期@描述@
                            materialValues = Tool.parserMaterialNo(scanValue);
                            if (materialValues.length >= 9) {
                                tvCheckNum.setText(materialValues[1]);
                                tvCheckMaterialUnique.setText(materialValues[2]);
                                tvCheckId.setText(materialValues[0]);

                                //上传数据
                                presenter.fetchUploadMaterial(CheckFragment.this, uwClientApplication.getTOKEN(), mTaskId,
                                        tvCheckMaterialUnique.getText().toString().trim(), tvCheckNum.getText().toString().trim());

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
                    showToast("请选择盘点任务!");
                }

            }
            return false;
        }
    };

    @Override
    public void scanNext() {
        tvScanCheck.setText("");
        tvCheckMaterialUnique.setText("");
        tvCheckNum.setText("");
        tvCheckId.setText("");
        setScanMaterialRequestFocus();
    }

    @Override
    protected ValuablePresenter<IValuableView> createPresenter() {
        return new ValuablePresenter<>(uwClientApplication);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_check;
    }

    @Override
    protected void destroy() {

    }

    @Override
    public void showTaskList(String token, BaseResult taskList) {
        //显示盘点任务列表
        if (taskList != null) {
            Log.d("sxmmm","string=="+taskList.toString());
            List<EmergencyTask.DataBean> list = ((EmergencyTask) taskList).getData();
            beanList.clear();
            beanList.add(0, topBean);
            beanList.addAll(1, list);
            spinnerAdapter.notifyDataSetChanged();
            checkSpinner.setSelection(0);
        }
    }

    @Override
    public void setScanMaterialRequestFocus() {
        etCheckMaterial.setText("");
        etCheckMaterial.requestFocus();
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
        Log.d("sxmmm","执行获取没盘点任务数量方法");
        presenter.get_pandian_not(CheckFragment.this,mTaskId,materialValues[0],String.valueOf(beanList.get(pos).getSupplier()));

    }
    @OnClick(R.id.btn_fresh_check)
    void onViewClicked(View view) {
        presenter.fetchGetTaskList(this, uwClientApplication.getTOKEN());
    }
}
