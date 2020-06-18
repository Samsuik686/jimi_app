package com.jimilab.uwclient.view.impl.PCB;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jimilab.uwclient.BuildConfig;
import com.jimilab.uwclient.R;
import com.jimilab.uwclient.UwClientApplication;
import com.jimilab.uwclient.bean.Constant;
import com.jimilab.uwclient.utils.CheckPermissionUtils;
import com.jimilab.uwclient.view.custom.ItemView;
import com.jimilab.uwclient.view.impl.BaseActivity;
import com.jimilab.uwclient.view.impl.incomeArea.InComeAreaLoginActivity;
import com.jimilab.uwclient.view.impl.urgent.UrgentLoginActivity;
import com.jimilab.uwclient.view.impl.valuable.ValuableLoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PcbcheckActivity extends BaseActivity {
    private static final String TAG = "PcbcheckActivity";

    @BindView(R.id.rl_out)
    ItemView rl_out;
    @BindView(R.id.rl_in)
    ItemView rl_in;
    @BindView(R.id.rl_pandian)
    ItemView rl_pandian;
    @BindView(R.id.rl_choujian)
    ItemView rl_choujian;

    String intentUsr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getActionBar() != null) {
            getActionBar().hide();
        }
        setContentView(R.layout.activity_pcb_check);
        //使屏幕常亮
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //申请权限
        CheckPermissionUtils.initPermission(this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        intentUsr = intent.getStringExtra("usrname");
        initView();
    }

    private void initView() {
        rl_in.setOnClickListener(mClickListener);
        rl_out.setOnClickListener(mClickListener);
        rl_pandian.setOnClickListener(mClickListener);
        rl_choujian.setOnClickListener(mClickListener);

    }
    Intent intent;
    private void resetItem(View view) {
        switch (view.getId()) {
            case R.id.rl_out:
                rl_pandian.reset();
                rl_choujian.reset();
                rl_in.reset();
                ((UwClientApplication) getApplication()).setIp(Constant.IP);

                intent = new Intent(this, PcbOutActivity.class);
                intent.putExtra("usrname", intentUsr);
                startActivity(intent);

                break;
            case R.id.rl_in:
                rl_out.reset();
                rl_pandian.reset();
                rl_choujian.reset();
                ((UwClientApplication) getApplication()).setIp(Constant.IP);

                intent = new Intent(this, PcbInActivity.class);
                intent.putExtra("usrname", intentUsr);
                startActivity(intent);

                break;
            case R.id.rl_pandian:
                rl_out.reset();
                rl_in.reset();
                rl_choujian.reset();
                ((UwClientApplication) getApplication()).setIp(Constant.IP);

                intent = new Intent(this, PcbPandianActivity.class);
                intent.putExtra("usrname", intentUsr);
                startActivity(intent);

                break;
            case R.id.rl_choujian:
                rl_out.reset();
                rl_in.reset();
                rl_pandian.reset();
                ((UwClientApplication) getApplication()).setIp(Constant.IP);
                intent = new Intent(this, PcbChoujianActivity.class);
                intent.putExtra("usrname", intentUsr);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            resetItem(view);
        }
    };
}
