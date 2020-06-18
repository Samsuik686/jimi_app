package com.jimilab.uwclient.view.impl.PCB;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jimilab.uwclient.R;
import com.jimilab.uwclient.adapter.PCBSpinnerAdapter;
import com.jimilab.uwclient.adapter.PcbImpAdapter;
import com.jimilab.uwclient.bean.pcb_bean.manufacturer_bean;
import com.jimilab.uwclient.bean.pcb_bean.mission_bean;
import com.jimilab.uwclient.bean.pcb_bean.mission_detail_bean;
import com.jimilab.uwclient.bean.pcb_bean.mission_detail_son_bean;
import com.jimilab.uwclient.bean.pcb_bean.supplier_bean;
import com.jimilab.uwclient.utils.Log;
import com.jimilab.uwclient.utils.Tool;
import com.jimilab.uwclient.view.impl.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PcbInActivity extends BaseActivity {
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.sp_supplier)
    Spinner sp_supplier;
    @BindView(R.id.sp_manufacturer)
    Spinner sp_manufacturer;
    @BindView(R.id.mission_name)
    Spinner mission_name;
    @BindView(R.id.myrecycle)
    RecyclerView myrecycle;
    @BindView(R.id.locan)
    TextView locan;
    @BindView(R.id.et_sampling_material)
    EditText etSamplingMaterial;
    ArrayList<manufacturer_bean> manufacturerlist = new ArrayList<>();
    PCBSpinnerAdapter manufactureradapter;
    ArrayList<supplier_bean> supplierlist = new ArrayList<>();
    PCBSpinnerAdapter supplieradapter;
    ArrayList<mission_bean> missionlist = new ArrayList<>();
    PCBSpinnerAdapter missionadapter;
    //选择的任务id
    String mTaskId;
    LinearLayoutManager linearLayoutManager;
    List<mission_detail_bean> mlist = new ArrayList<>();
    PcbImpAdapter Missionadapter;
    //扫描的二维码分解字段
    String[] materialValues;
    //最后扫描到的二维码
    String scanMaterial;
    //最后扫到的二维码转化的javabean
    mission_detail_son_bean new_bean;
    //是否进行了扫描动作
    boolean hasxzing;
    //扫描了的二维码的料号名字
    String name;
    //倘若物料对应，则对应哪个item
    int zxing_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pcbimp);
        ButterKnife.bind(this);
        initView();
        testSpinnerManufacturer();
        testSpinnerSupplier();
        testSpinnerMission();
        initSpinnerAction();
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
                    getMissionList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initView() {
        Intent intent = getIntent();
        String intentUsr = intent.getStringExtra("usrname");
        user_name.setText(intentUsr);
        manufactureradapter = new PCBSpinnerAdapter(PcbInActivity.this, manufacturerlist);
        supplieradapter = new PCBSpinnerAdapter(PcbInActivity.this, supplierlist);
        missionadapter = new PCBSpinnerAdapter(PcbInActivity.this, missionlist);
        etSamplingMaterial.setOnEditorActionListener(mEditorActionListener);
        linearLayoutManager = new LinearLayoutManager(this);
        myrecycle.setLayoutManager(linearLayoutManager);
        Missionadapter = new PcbImpAdapter(mlist, PcbInActivity.this);
        myrecycle.setAdapter(Missionadapter);
        myrecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int  visibleItemCount = linearLayoutManager.getChildCount(); //子数
                int totalItemCount = linearLayoutManager.getItemCount(); // item总数
                int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition(); //当前屏幕 首个 可见的 Item 的position
                if (pastVisiblesItems+visibleItemCount+1>=zxing_number&&zxing_number>=pastVisiblesItems-1) {
                    takeItem();
                }

            }
        });

    }

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
    private void getMissionList() {
        mlist.clear();
        mission_detail_son_bean mission_detail_son_bean11 = new mission_detail_son_bean("物料1", "物料1料盘1", "100");
        mission_detail_son_bean mission_detail_son_bean12 = new mission_detail_son_bean("物料1", "物料1料盘2", "100");
        mission_detail_son_bean mission_detail_son_bean13 = new mission_detail_son_bean("物料1", "物料1料盘3", "100");
        mission_detail_son_bean mission_detail_son_bean14 = new mission_detail_son_bean("物料1", "物料1料盘4", "100");
        mission_detail_son_bean mission_detail_son_bean15 = new mission_detail_son_bean("物料1", "物料1料盘5", "100");
        ArrayList<mission_detail_son_bean> list11 = new ArrayList<mission_detail_son_bean>();
        list11.add(mission_detail_son_bean11);
        list11.add(mission_detail_son_bean12);
        list11.add(mission_detail_son_bean13);
        list11.add(mission_detail_son_bean14);
        list11.add(mission_detail_son_bean15);
        mission_detail_bean mission_detail_bean1 = new mission_detail_bean("物料1", "规格1234321234", "", "10000",
                "500", false, list11);
        mission_detail_son_bean mission_detail_son_bean21 = new mission_detail_son_bean("物料2", "物料2料盘1", "100");
        mission_detail_son_bean mission_detail_son_bean22 = new mission_detail_son_bean("物料2", "物料2料盘2", "100");
        mission_detail_son_bean mission_detail_son_bean23 = new mission_detail_son_bean("物料2", "物料2料盘3", "100");
        mission_detail_son_bean mission_detail_son_bean24 = new mission_detail_son_bean("物料2", "物料2料盘4", "100");
        mission_detail_son_bean mission_detail_son_bean25 = new mission_detail_son_bean("物料2", "物料2料盘5", "100");
        ArrayList<mission_detail_son_bean> list12 = new ArrayList<mission_detail_son_bean>();
        list12.add(mission_detail_son_bean21);
        list12.add(mission_detail_son_bean22);
        list12.add(mission_detail_son_bean23);
        list12.add(mission_detail_son_bean24);
        list12.add(mission_detail_son_bean25);
        mission_detail_bean mission_detail_bean2 = new mission_detail_bean("物料3", "规格1xxxxxxxxxxxx", "", "10000",
                "500", false, list12);
        ArrayList<mission_detail_son_bean> list13 = new ArrayList<mission_detail_son_bean>();
        mission_detail_bean mission_detail_bean3 = new mission_detail_bean("物料4", "规格1xkkkkkkkkkkx", "", "10000",
                "0", false, list13);
        mlist.add(mission_detail_bean1);
        mlist.add(mission_detail_bean2);
        mlist.add(mission_detail_bean3);

        for (int i = 0; i < 15; i++) {
            ArrayList<mission_detail_son_bean> listcycle = new ArrayList<mission_detail_son_bean>();
            mission_detail_bean mission_detail_bean_cycle = new mission_detail_bean("循环cycle" + i, "规格1234321234", "", "10000",
                    "500", false, listcycle);
            mlist.add(mission_detail_bean_cycle);
        }
        Missionadapter.notifyDataSetChanged();

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
                                name = materialValues[0];
                                hasxzing = true;
                                boolean haspipei = false;
                                boolean hasclose = false;
                                for (int i = 0; i < mlist.size(); i++) {
                                    if (name.equals(mlist.get(i).getId())==false){
                                        if (mlist.get(i).isSpread()){
                                            try {
                                                View view = linearLayoutManager.findViewByPosition(i);
                                                LinearLayout layout = (LinearLayout) view; //获取布局中任意控件对象
                                                ImageView status = layout.findViewById(R.id.open_close);
                                                status.performClick();
                                            }catch (Exception e){
                                                mlist.get(i).setSpread(false);
                                                hasclose = true;
                                            }
                                        }
                                    }
                                }
                                if (hasclose){
                                    missionadapter.notifyDataSetChanged();
                                }

                                for (int i = 0; i < mlist.size(); i++) {
                                    if (name.equals(mlist.get(i).getId())) {
                                        if (materialValues[0].equals(mlist.get(i).getId()) == true) {
                                            zxing_number = i;
                                            myrecycle.scrollToPosition(i);
                                            linearLayoutManager = (LinearLayoutManager) myrecycle.getLayoutManager();
                                            linearLayoutManager.scrollToPositionWithOffset(i, 0);
                                            try {
                                                takeItem();
                                            }catch (Exception ex){
                                                hasxzing = true;
                                            }
                                            haspipei = true;

                                        }
                                    }
                                }
                                if (!haspipei) {
                                    hasxzing = false;
                                    showToast("此条码未找到适配物料");
                                }


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
    private void takeItem(){
        if (hasxzing){
            hasxzing = false;
            if (mlist.get(zxing_number).isSpread() == false) {
                View view = linearLayoutManager.findViewByPosition(zxing_number);
                LinearLayout layout = (LinearLayout) view; //获取布局中任意控件对象
                ImageView status = layout.findViewById(R.id.open_close);
                status.performClick();
            }
            new_bean = new mission_detail_son_bean(materialValues[0], materialValues[2], materialValues[1]);
            Missionadapter.addnewPcblmpDetail(new_bean, zxing_number);
            showChooseLocan();
        }

    }


    public void setScanMaterialRequestFocus() {
        etSamplingMaterial.setText("");
        etSamplingMaterial.requestFocus();
    }

    public void showChooseLocan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PcbInActivity.this);
        builder.setTitle("请选择摆放位置");
        final String[] items = {"1", "2", "3", "4"};// 创建一个存放选项的数组
        final boolean[] checkedItems = {true, false, false, false};// 存放选中状态，true为选中
        // ，false为未选中，和setSingleChoiceItems中第二个参数对应
        // 为对话框添加单选列表项
        // 第一个参数存放选项的数组，第二个参数存放默认被选中的项，第三个参数点击事件
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                }
                checkedItems[arg1] = true;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                arg0.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                String str = "";
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        str = items[i];
                    }
                }

            }
        });
        builder.create().show();


    }

}
