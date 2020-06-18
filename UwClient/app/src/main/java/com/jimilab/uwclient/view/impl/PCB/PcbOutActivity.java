package com.jimilab.uwclient.view.impl.PCB;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jimilab.uwclient.R;
import com.jimilab.uwclient.adapter.PCBSpinnerAdapter;
import com.jimilab.uwclient.adapter.PcbImpAdapter;
import com.jimilab.uwclient.adapter.Pcb_out_adapter;
import com.jimilab.uwclient.adapter.PcblmpDetaileAdapter;
import com.jimilab.uwclient.bean.pcb_bean.manufacturer_bean;
import com.jimilab.uwclient.bean.pcb_bean.mission_bean;
import com.jimilab.uwclient.bean.pcb_bean.mission_detail_son_bean;
import com.jimilab.uwclient.bean.pcb_bean.supplier_bean;
import com.jimilab.uwclient.view.impl.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PcbOutActivity extends BaseActivity {
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.id)
    TextView id;
    @BindView(R.id.specifications)
    TextView specifications;
    @BindView(R.id.locan)
    TextView locan;
    @BindView(R.id.planNumber)
    TextView planNumber;
    @BindView(R.id.tureNumber)
    TextView tureNumber;
    @BindView(R.id.sp_supplier)
    Spinner sp_supplier;
    @BindView(R.id.sp_manufacturer)
    Spinner sp_manufacturer;
    @BindView(R.id.mission_name)
    Spinner mission_name;
    @BindView(R.id.myrecycle)
    RecyclerView myrecycle;
    @BindView(R.id.et_sampling_material)
    EditText etSamplingMaterial;
    ArrayList<manufacturer_bean> manufacturerlist = new ArrayList<>();
    PCBSpinnerAdapter manufactureradapter;
    ArrayList<supplier_bean> supplierlist = new ArrayList<>();
    PCBSpinnerAdapter supplieradapter;
    ArrayList<mission_bean> missionlist = new ArrayList<>();
    PCBSpinnerAdapter missionadapter;
    LinearLayoutManager linearLayoutManager;
    String mTaskId;
    //扫描的二维码分解字段
    String[] materialValues;
    //最后扫描到的二维码
    String scanMaterial;
    List<mission_detail_son_bean> mlist = new ArrayList<>();
    Pcb_out_adapter outMissionadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcbout);
        ButterKnife.bind(this);
        initView();
        testSpinnerManufacturer();
        testSpinnerSupplier();
        testSpinnerMission();
        initSpinnerAction();
    }

    private void initView() {
        Intent intent = getIntent();
        String intentUsr = intent.getStringExtra("usrname");
        user_name.setText(intentUsr);
        manufactureradapter = new PCBSpinnerAdapter(PcbOutActivity.this, manufacturerlist);
        supplieradapter = new PCBSpinnerAdapter(PcbOutActivity.this, supplierlist);
        missionadapter = new PCBSpinnerAdapter(PcbOutActivity.this, missionlist);
        etSamplingMaterial.setOnEditorActionListener(mEditorActionListener);
        linearLayoutManager = new LinearLayoutManager(this);
        myrecycle.setLayoutManager(linearLayoutManager);
        outMissionadapter = new Pcb_out_adapter(mlist, PcbOutActivity.this);
        myrecycle.setAdapter(outMissionadapter);

    }
    private TextView.OnEditorActionListener mEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                switch (event.getAction()) {
                    //按下
                    case KeyEvent.ACTION_UP:
                        if (!mTaskId.isEmpty()) {
                            if (!TextUtils.isEmpty(v.getText().toString().trim())) {
                                //扫描的内容
                                // 料号@数量@时间戳(唯一码)@打印人员@供应商@位置@物料类型（标准/非标准）@生产日期@型号@产商@周期@打印日期@
                                scanMaterial = String.valueOf(((EditText) v).getText());
                                scanMaterial = scanMaterial.replaceAll("\r", "");
                                materialValues = scanMaterial.split("@");
                                mlist.add(new mission_detail_son_bean("111","111","111"));
                                outMissionadapter.notifyDataSetChanged();

                            }
                            setScanMaterialRequestFocus();
                        } else {
                            setScanMaterialRequestFocus();
                            showToast("请选择任务单！");
                        }

                        return true;
                    default:
                        return true;
                }
            }
            return false;
        }
    };

    private void testSpinnerManufacturer() {
        manufacturerlist.clear();
        manufacturerlist.add(new manufacturer_bean("不限", ""));
        manufacturerlist.add(new manufacturer_bean("黑石科技", ""));
        manufacturerlist.add(new manufacturer_bean("几米物联", ""));
    }
    private void testSpinnerMission() {
        supplierlist.clear();
        supplierlist.add(new supplier_bean("不限", ""));
        supplierlist.add(new supplier_bean("康凯斯", ""));
        supplierlist.add(new supplier_bean("几米制造", ""));
        supplierlist.add(new supplier_bean("葫芦娃", ""));
        supplierlist.add(new supplier_bean("乐行天下", ""));

    }
    private void testSpinnerSupplier() {
        missionlist.clear();
        missionlist.add(new mission_bean("请选择任务单", ""));
        missionlist.add(new mission_bean("任务编号00000000", "123454321"));
    }
    private void initSpinnerAction() {
        sp_manufacturer.setAdapter(manufactureradapter);
        sp_manufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mTaskId = "";
                testSpinnerMission();
                missionadapter.notifyDataSetChanged();
                mission_name.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        sp_supplier.setAdapter(supplieradapter);
        sp_supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                testSpinnerMission();
                missionadapter.notifyDataSetChanged();
                mission_name.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mission_name.setAdapter(missionadapter);
        mission_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                mTaskId = "";
                if (pos != 0) {
                    mTaskId = missionlist.get(pos).getMission_id();
                    getOutMission();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setScanMaterialRequestFocus() {
        etSamplingMaterial.setText("");
        etSamplingMaterial.requestFocus();
    }


    private void getOutMission() {
        id.setText("物料号112345");
        specifications.setText("物料描述23456432");
        locan.setText("位置1");
        planNumber.setText("1000");
        tureNumber.setText("100");
    }
}
