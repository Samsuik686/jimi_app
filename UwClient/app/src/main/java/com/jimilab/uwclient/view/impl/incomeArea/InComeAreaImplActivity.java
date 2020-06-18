package com.jimilab.uwclient.view.impl.incomeArea;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jimilab.uwclient.R;
import com.jimilab.uwclient.adapter.TaskSpinnerAdapter;
import com.jimilab.uwclient.bean.BaseResult;
import com.jimilab.uwclient.bean.EmergencyTask;
import com.jimilab.uwclient.presenter.ComeAreaPresenter;
import com.jimilab.uwclient.utils.Log;
import com.jimilab.uwclient.utils.Tool;
import com.jimilab.uwclient.view.InComeAreaView;
import com.jimilab.uwclient.view.custom.MyEditTextDel;
import com.jimilab.uwclient.view.impl.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InComeAreaImplActivity extends BaseActivity implements InComeAreaView {

    String error_message;
    String planQuantity;
    String actualQuantity;
    private Handler handler = new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case 1:
                    Tool.showToast(mContext, error_message);
                    break;
                case 2:
                    plan_for_chucang.setText(planQuantity);
                    fact_for_chucang.setText(actualQuantity);
                    break;
            }
        }
    };


    private static final String TAG = InComeAreaImplActivity.class.getSimpleName();
    //@BindView(R.id.iv_menu)
    //ImageView ivMenu;
    @BindView(R.id.tv_operator)
    TextView tvOperator;
    @BindView(R.id.sp_orders)
    Spinner spOrders;
    @BindView(R.id.tv_material_no)
    TextView tvMaterialNo;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.et_material_income)
    MyEditTextDel etMaterialNo;
    @BindView(R.id.plan_for_chucang)
    TextView plan_for_chucang;
    @BindView(R.id.fact_for_chucang)
    TextView fact_for_chucang;
    @BindView(R.id.tv_material_count)
    TextView tvMaterialCount;
    @BindView(R.id.btn_lock_task)
    Button btnLockTask;
    @BindView(R.id.btn_fresh_task)
    Button btnFreshTask;
    @BindView(R.id.tv_scan_material)
    TextView tvScanMaterial;
    @BindView(R.id.tv_upload_res)
    TextView tvUploadRes;
    private ComeAreaPresenter comeAreaPresenter;
    private EmergencyTask.DataBean topBean;
    private List<EmergencyTask.DataBean> dataBeanList = new ArrayList<>();
    private TaskSpinnerAdapter<EmergencyTask.DataBean> spinnerAdapter;
    private int selectPos = -1;
    private boolean orderChanged = false;
    private boolean taskLocked = false;
    private InputMethodManager inputMethodManager;
    private List<EmergencyTask.DataBean> list;
    String[] getmaterial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_come_area_impl);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String intentUsr = intent.getStringExtra("usrname");
        tvOperator.setText(intentUsr);
        comeAreaPresenter = new ComeAreaPresenter(mApplication);
        comeAreaPresenter.attachView(this);

        inputMethodManager = (InputMethodManager) mApplication.getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();

        topBean = new EmergencyTask.DataBean();
        topBean.setId(-1);
        topBean.setFile_name("请选择任务单");
        dataBeanList.add(0, topBean);

        spinnerAdapter = new TaskSpinnerAdapter<>(dataBeanList, mApplication);
        spOrders.setAdapter(spinnerAdapter);
        //获取任务
        comeAreaPresenter.fetchGetTasks(mApplication.getTOKEN());
    }

    private void initView() {
        spOrders.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected - position: " + position + " selectPos: " + selectPos);
                if (selectPos != position) {
                    selectPos = position;
                    orderChanged = true;
                } else {
                    orderChanged = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: ");
            }
        });


        etMaterialNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    inputMethodManager.hideSoftInputFromWindow(etMaterialNo.getWindowToken(), 1);
                }
            }
        });

        etMaterialNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (selectPos > 0 && taskLocked) {
                        if (event.getAction() == KeyEvent.ACTION_UP) {
                            clearResult();
                            String scanValue = String.valueOf(v.getText()).replace("\r", "");
                            Log.d(TAG, "scanValue - " + scanValue);
                            inputMethodManager.hideSoftInputFromWindow(etMaterialNo.getWindowToken(), 1);
                            if (scanValue.contains("@")) {
                                tvScanMaterial.setText(scanValue.substring(0, scanValue.indexOf("@")));
                                if (v.getId() == R.id.et_material_income) {
                                    comeAreaPresenter.doScanMaterial(dataBeanList.get(selectPos).getId(), scanValue);
                                }
                            } else {
                                showToast("二维码格式不正确");
                                setScanMaterialRequestFocus();
                            }

                            return true;
                        }
                        return true;
                    } else {
                        showToast("未锁定任务单");
                        setScanMaterialRequestFocus();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void showTaskList(String token, BaseResult taskList) {
        if (taskList != null) {
            Log.d("sxmmm","string=="+taskList.toString());
            List<EmergencyTask.DataBean> list = ((EmergencyTask) taskList).getData();
            dataBeanList.clear();
            dataBeanList.add(0, topBean);
            dataBeanList.addAll(1, list);
            spinnerAdapter.notifyDataSetChanged();
            spOrders.setSelection(0);
            spOrders.setEnabled(true);
            btnLockTask.setText("锁定");
            taskLocked = false;
        }
    }

    @Override
    public void showMaterial(String[] material) {
        //料号@数量@时间戳(唯一码)@打印人员@供应商@位置@物料类型（标准/非标准）@生产日期@型号@产商@周期@打印日期@
        tvMaterialNo.setText(material[0]);
        tvMaterialCount.setText(material[1]);
        getmaterial = material;
    }

    @Override
    public void clearMaterial() {
        tvMaterialNo.setText("");
        tvMaterialCount.setText("");

        setScanMaterialRequestFocus();
    }

    @Override
    public void setScanMaterialRequestFocus() {
        etMaterialNo.setText("");
        etMaterialNo.requestFocus();
    }

    @Override
    public void showResult(int resCode, String res) {
        if (resCode == 200) {
            tvUploadRes.setTextColor(getResources().getColor(R.color.myGreen));
            //料号@数量@时间戳(唯一码)@打印人员@供应商@位置@物料类型（标准/非标准）@生产日期@型号@产商@周期@打印日期@
            //String cycle, String manufacturer, Date printTim
            // String taskId, String no, String materialId, Integer quantity, Date productionTime,
            // String supplierName, String cycle, String manufacturer, Date printTime
            comeAreaPresenter.GetPlanAndFact(String.valueOf(dataBeanList.get(selectPos).getId()),getmaterial[0]);
        } else {
            tvUploadRes.setTextColor(Color.RED);
        }
        tvUploadRes.setText(res);
    }

    @Override
    public void clearResult() {
        tvScanMaterial.setText("");
        tvUploadRes.setText("");
    }

    @Override
    public void showOkhttpResult( String planQuantity,String actualQuantity) {
        Message message = new Message();
        message.what=2;
        handler.sendMessage(message);
        this.planQuantity = planQuantity;
        this.actualQuantity = actualQuantity;
    }

    @Override
    public void showOkhttpErrer( String res) {
        error_message = res;
        Message message = new Message();
        message.what=1;
        handler.sendMessage(message);

    }

    @OnClick({R.id.iv_back, R.id.btn_lock_task, R.id.btn_fresh_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                exit();
                break;
            case R.id.btn_lock_task:
                lockSelectTask();
                break;
            case R.id.btn_fresh_task:
                comeAreaPresenter.fetchGetTasks(mApplication.getTOKEN());
                break;
        }
    }

    private void lockSelectTask() {
        if (!taskLocked) {//锁定
            if (selectPos <= 0) {
                showToast("请选择任务单");
            } else {
                spOrders.setEnabled(false);
                btnLockTask.setText("解锁");
                taskLocked = true;
                showToast("已锁定");
                if (orderChanged) {
                    clearResult();
                }
                btnFreshTask.setEnabled(false);
                setScanMaterialRequestFocus();
            }
        } else {//解锁
            btnFreshTask.setEnabled(true);
            spOrders.setEnabled(true);
            btnLockTask.setText("锁定");
            taskLocked = false;
            showToast("已解锁");
        }
    }



    @Override
    public void showToast(String msg) {
        Tool.showToast(mContext, msg);
    }

    @Override
    public void showMsgDialog(String title, String msg) {

    }

    @Override
    public void showLoading(String msg) {
        Tool.showLoading(this, msg);
    }

    @Override
    public void dismissLoading() {
        Tool.dismissLoading();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        comeAreaPresenter.detachView();
    }

}
