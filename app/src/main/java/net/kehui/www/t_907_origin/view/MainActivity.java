package net.kehui.www.t_907_origin.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;

import net.kehui.www.t_907_origin.base.BaseActivity;
import net.kehui.www.t_907_origin.ConnectService;
import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.application.AppConfig;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.application.MyApplication;
import net.kehui.www.t_907_origin.entity.ParamInfo;
import net.kehui.www.t_907_origin.ui.AppUpdateDialog;
import net.kehui.www.t_907_origin.ui.HelpHomeDialog;
import net.kehui.www.t_907_origin.ui.LanguageChangeDialog;
import net.kehui.www.t_907_origin.ui.HelpCenterDialog;
import net.kehui.www.t_907_origin.ui.ShowRecordsDialog;
import net.kehui.www.t_907_origin.util.AppUtils;
import net.kehui.www.t_907_origin.util.StateUtils;
import net.kehui.www.t_907_origin.util.UnitUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.kehui.www.t_907_origin.application.Constant.CurrentUnit;
import static net.kehui.www.t_907_origin.application.Constant.FT_UNIT;
import static net.kehui.www.t_907_origin.application.Constant.MI_UNIT;

/**
 * @author JWJ
 * @date 2019/12/1
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.et_cable_vop)
    EditText etCableVop;
    @BindView(R.id.et_cable_length)
    EditText etCableLength;
    @BindView(R.id.et_cable_id)
    EditText etCableId;
    @BindView(R.id.et_length)
    EditText etLength;
    @BindView(R.id.et_vop)
    EditText etVop;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.cb_test_lead)
    CheckBox cbTestLead;
    @BindView(R.id.rb_mi)
    RadioButton rbMi;
    @BindView(R.id.rb_ft)
    RadioButton rbFt;
    @BindView(R.id.rg_unit)
    RadioGroup rgUnit;
    @BindView(R.id.tv_cable_vop_unit)
    TextView tvCableVopUnit;
    @BindView(R.id.tv_cable_length_unit)
    TextView tvCableLengthUnit;
    @BindView(R.id.tv_length_unit)
    TextView tvLengthUnit;
    @BindView(R.id.tv_vop_unit)
    TextView tvVopUnit;
    @BindView(R.id.iv_wifi_status)
    ImageView ivWifiStatus;
    @BindView(R.id.iv_battery_status)
    ImageView ivBatteryStatus;

    private int unit;
    private String version = "1";
    private String url = "baidu.com";

    private ProgressBar progressBar;
    private TextView tvProgress;
    private LanguageChangeDialog languageChangeDialog;
    private AppUpdateDialog mAppUpdateDialog;
    private TDialog tDialog;

    /**
     * 定义bundle的key-value（bundle就是个容器）
     */
    public static final String BUNDLE_COMMAND_KEY = "command";
    public static final String BUNDLE_MODE_KEY = "mode";
    public static final String BUNDLE_DATA_TRANSFER_KEY = "dataTransfer";
    public static final String BUNDLE_PARAM_KEY = "bundle_param_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //启动服务
        startMainService();
        initUnit();
        initParamInfo();
        //初始化广播接收器
        initBroadcastReceiver();
    }

    public void startMainService() {
        Intent intent = new Intent(MainActivity.this, ConnectService.class);
        startService(intent);
    }

    /**
     * 计量单位初始化 (m ft)
     */
    int currentCheckedId = 0;
    private void initUnit() {
        tvMsg.setText(String.format(getString(R.string.main_notice), getString(R.string.mi)));
        unit = StateUtils.getInt(MainActivity.this, AppConfig.CURRENT_UNIT, MI_UNIT);
        if (unit == MI_UNIT) {
            rbMi.setChecked(true);
        } else {
            rbFt.setChecked(true);
        }
        changeUnitView(unit);
        rgUnit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                currentCheckedId = checkedId;
                if (checkedId == rbFt.getId()) {
                    StateUtils.setInt(MainActivity.this, AppConfig.CURRENT_UNIT, FT_UNIT);
                    changeUnitView(FT_UNIT);

                } else if (checkedId == rbMi.getId()) {
                    StateUtils.setInt(MainActivity.this, AppConfig.CURRENT_UNIT, MI_UNIT);
                    changeUnitView(MI_UNIT);
                }
            }
        });
    }

    private void changeUnitView(int currentUnit) {
        CurrentUnit = currentUnit;
        if (currentUnit == MI_UNIT) {
            tvCableVopUnit.setText(R.string.mius);
            tvCableLengthUnit.setText(R.string.mi);
            tvLengthUnit.setText(R.string.mi);
            tvVopUnit.setText(R.string.mius);
            tvMsg.setText(String.format(getString(R.string.main_notice), "500" + getString(R.string.mi)));
            //改变数值
            initParamInfo();

        } else {
            tvCableVopUnit.setText(R.string.ftus);
            tvCableLengthUnit.setText(R.string.ft);
            tvLengthUnit.setText(R.string.ft);
            tvVopUnit.setText(R.string.ftus);
            tvMsg.setText(String.format(getString(R.string.main_notice), "1640" + getString(R.string.ft)));
            initParamInfo();
        }

    }

    /**
     * 参数配置初始化
     */
    private void initParamInfo() {
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(MainActivity.this, Constant.PARAM_INFO_KEY);
        unit = StateUtils.getInt(MainActivity.this, AppConfig.CURRENT_UNIT, MI_UNIT);
        //获取当前存储的单位
        int saveUnit = StateUtils.getInt(MainActivity.this, AppConfig.CURRENT_SAVE_UNIT, MI_UNIT);
        CurrentUnit = unit;
        if (paramInfo != null) {
            //如果单位是米
            if (unit == MI_UNIT) {
                if (saveUnit == MI_UNIT) {
                    //改变数值
                    if (!TextUtils.isEmpty(paramInfo.getCableVop())) {
                        if (paramInfo.getCableVop().equals("0") || paramInfo.getCableVop().equals("0.0")) {
                            etCableVop.setText("");
                        } else {
                            etCableVop.setText(String.valueOf(paramInfo.getCableVop()));
                        }
                    }
                    if (!TextUtils.isEmpty(paramInfo.getCableLength())) {
                        if (paramInfo.getCableLength().equals("0") || paramInfo.getCableLength().equals("0.0")) {
                            etCableLength.setText("");
                        } else {
                            etCableLength.setText(String.valueOf(paramInfo.getCableLength()));
                        }
                    }

                    etCableId.setText(paramInfo.getCableId());
                    if (paramInfo.getTestLead()) {
                        cbTestLead.setChecked(paramInfo.getTestLead());
                        if (!TextUtils.isEmpty(paramInfo.getLength())) {
                            etLength.setText(String.valueOf(paramInfo.getLength()));
                        }
                        if(paramInfo.getLength().equals("0")) {
                            etLength.setText("");
                        }
                        if (!TextUtils.isEmpty(paramInfo.getVop())) {
                            etVop.setText(String.valueOf(paramInfo.getVop()));
                        }
                        if(paramInfo.getVop().equals("0")) {
                            etVop.setText("");
                        }
                    }
                } else {
                    //改变数值
                    if (!TextUtils.isEmpty(paramInfo.getCableVop())) {
                        if (paramInfo.getCableVop().equals("0") || paramInfo.getCableVop().equals("0.0")) {
                            etCableVop.setText("");
                        } else {
                            etCableVop.setText(String.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getCableVop()))));
                        }
                    }
                    if (!TextUtils.isEmpty(paramInfo.getCableLength())) {
                        if (paramInfo.getCableLength().equals("0") || paramInfo.getCableLength().equals("0.0")) {
                            etCableLength.setText("");
                        } else {
                            etCableLength.setText(String.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getCableLength()))));
                        }
                    }

                    etCableId.setText(paramInfo.getCableId());
                    if (paramInfo.getTestLead()) {
                        cbTestLead.setChecked(paramInfo.getTestLead());
                        if (!TextUtils.isEmpty(paramInfo.getLength())) {
                            etLength.setText(String.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getLength()))));
                        }
                        if(paramInfo.getLength().equals("0")) {
                            etLength.setText("");
                        }
                        if (!TextUtils.isEmpty(paramInfo.getVop())) {
                            etVop.setText(String.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getVop()))));
                        }
                        if(paramInfo.getVop().equals("0")) {
                            etVop.setText("");
                        }
                    }
                }
            } else {
                if (saveUnit == FT_UNIT) {
                    //改变数值
                    if (!TextUtils.isEmpty(paramInfo.getCableVop())) {
                        if (paramInfo.getCableVop().equals("0") || paramInfo.getCableVop().equals("0.0")) {
                            etCableVop.setText("");
                        } else {
                            etCableVop.setText(String.valueOf(paramInfo.getCableVop()));
                        }
                    }
                    if (!TextUtils.isEmpty(paramInfo.getCableLength())) {
                        if (paramInfo.getCableLength().equals("0") || paramInfo.getCableLength().equals("0.0")) {
                            etCableLength.setText("");
                        } else {
                            etCableLength.setText(String.valueOf(paramInfo.getCableLength()));
                        }
                    }

                    etCableId.setText(paramInfo.getCableId());
                    if (paramInfo.getTestLead()) {
                        cbTestLead.setChecked(paramInfo.getTestLead());
                        if (!TextUtils.isEmpty(paramInfo.getLength())) {
                            etLength.setText(String.valueOf(paramInfo.getLength()));
                        }
                        if(paramInfo.getLength().equals("0")) {
                            etLength.setText("");
                        }
                        if (!TextUtils.isEmpty(paramInfo.getVop())) {
                            etVop.setText(String.valueOf(paramInfo.getVop()));
                        }
                        if(paramInfo.getVop().equals("0")) {
                            etVop.setText("");
                        }
                    }
                } else {
                    //改变数值
                    if (!TextUtils.isEmpty(paramInfo.getCableVop())) {
                        if (paramInfo.getCableVop().equals("0") || paramInfo.getCableVop().equals("0.0")) {
                            etCableVop.setText("");
                        } else {
                            etCableVop.setText(String.valueOf(UnitUtils.miToFt(Double.valueOf(paramInfo.getCableVop()))));
                        }
                    }
                    if (!TextUtils.isEmpty(paramInfo.getCableLength())) {
                        if (paramInfo.getCableLength().equals("0") || paramInfo.getCableLength().equals("0.0")) {
                            etCableLength.setText("");
                        } else {
                            etCableLength.setText(String.valueOf(UnitUtils.miToFt(Double.valueOf(paramInfo.getCableLength()))));
                        }
                    }

                    etCableId.setText(paramInfo.getCableId());
                    if (paramInfo.getTestLead()) {
                        cbTestLead.setChecked(paramInfo.getTestLead());
                        if (!TextUtils.isEmpty(paramInfo.getLength())) {
                            etLength.setText(String.valueOf(UnitUtils.miToFt(Double.valueOf(paramInfo.getLength()))));
                        }
                        if(paramInfo.getLength().equals("0")) {
                            etLength.setText("");
                        }
                        if (!TextUtils.isEmpty(paramInfo.getVop())) {
                            etVop.setText(String.valueOf(UnitUtils.miToFt(Double.valueOf(paramInfo.getVop()))));
                        }
                        if(paramInfo.getVop().equals("0")) {
                            etVop.setText("");
                        }
                    }
                }
            }
        }

        if (cbTestLead.isChecked()) {
            etLength.setEnabled(true);
            etVop.setEnabled(true);
        } else {
            etLength.setEnabled(false);
            etVop.setEnabled(false);
        }
        cbTestLead.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etLength.setEnabled(true);
                etVop.setEnabled(true);
            } else {
                etLength.setEnabled(false);
                etVop.setEnabled(false);
            }
        });
        etCableVop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etVop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    double vop = Double.parseDouble(s.toString());
                    double maxVop;
                    if (CurrentUnit == MI_UNIT) {
                        maxVop = 300;
                    } else {
                        maxVop = Double.valueOf(UnitUtils.miToFt(300));
                    }
                    if (vop > maxVop) {
                        etVop.setText(maxVop + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 广播接收器初始化
     */
    private void initBroadcastReceiver() {
        IntentFilter ifDisplay = new IntentFilter();
        ifDisplay.addAction(ConnectService.BROADCAST_ACTION_DEVICE_CONNECTED);
        ifDisplay.addAction(ConnectService.BROADCAST_ACTION_DEVICE_CONNECT_FAILURE);
        ifDisplay.addAction(ConnectService.BROADCAST_ACTION_DOWIFI_COMMAND);
        registerReceiver(receiver, ifDisplay);
        //判断服务里连接状态，如果已经连接，则发送广播，改变连接状态
        if (ConnectService.isConnected) {
            Intent intent = new Intent(ConnectService.BROADCAST_ACTION_DEVICE_CONNECTED);
            sendBroadcast(intent);
        }
    }

    /**
     * 监听广播并做相应处理
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            switch (action) {
                case ConnectService.BROADCAST_ACTION_DEVICE_CONNECTED:
                    //网络连接，更换网络图标
                    ConnectService.isConnected = true;
                    ivWifiStatus.setImageResource(R.drawable.ic_wifi_connected);
                    //发送获取电量命令  //EN20200324    //G??   有必要么
                    if (ConnectService.canAskPower) {
                        handler.postDelayed(() -> {
                            ConnectService.canAskPower = false;
                            //电量
                            command = 0x06;
                            dataTransfer = 0x08;
                            startService();
                        }, 100);
                    }
                    break;
                case ConnectService.BROADCAST_ACTION_DEVICE_CONNECT_FAILURE:
                    //网络断开，更换网络图标、电量图标
                    ConnectService.isConnected = false;
                    ivWifiStatus.setImageResource(R.drawable.ic_no_wifi_connect);
                    ivBatteryStatus.setImageResource(R.drawable.ic_battery_no);
                    break;
                case ConnectService.BROADCAST_ACTION_DOWIFI_COMMAND:
                    //处理获取到的电量数据
                    wifiStream = intent.getIntArrayExtra(ConnectService.INTENT_KEY_COMMAND);
                    assert wifiStream != null;
                    doWifiCommand(wifiStream);
                    break;
                default:
                    break;
            }
        }

    };

    /**
     *通过服务发送获取电量指令
     */
    public void startService() {
        Intent intent = new Intent(MainActivity.this, ConnectService.class);
        //发送指令
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_COMMAND_KEY, command);
        bundle.putInt(BUNDLE_MODE_KEY, mode);
        bundle.putInt(BUNDLE_DATA_TRANSFER_KEY, dataTransfer);
        intent.putExtra(BUNDLE_PARAM_KEY, bundle);
        startService(intent);
    }

    /**
     * @param wifiArray 处理获取到的电量数据
     */
    private void doWifiCommand(int[] wifiArray) {
        if (wifiArray[5] == POWER_DISPLAY) {
            //判断获取到的数据为电量数据
            int batteryValue = wifiArray[6] * 256 + wifiArray[7];
            if (batteryValue <= 2600) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_zero);
                //主界面电量状态记录   //GC20200314
                Constant.batteryValue = 0;
            } else if (batteryValue <= 2818) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_one);
                Constant.batteryValue = 1;
            } else if (batteryValue <= 3018) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_two);
                Constant.batteryValue = 2;
            } else if (batteryValue <= 3120) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_three);
                Constant.batteryValue = 3;
            } else {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_four);
                Constant.batteryValue = 4;
            }
        }

    }

    @OnClick({R.id.tv_vop_save, R.id.iv_tdr_mode, R.id.iv_icms_mode, R.id.iv_icmd_mode, R.id.iv_mim_mode, R.id.iv_decay_mode, R.id.btn_language, R.id.btn_update, R.id.btn_help, R.id.btn_records, R.id.btn_remote})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_vop_save:
                //保存首页设置
                saveParamInfo();
                break;
            case R.id.iv_tdr_mode:
                showProgressMode(TDR);
                break;
            case R.id.iv_icms_mode:
                showProgressMode(ICM);
                break;
            case R.id.iv_icmd_mode:
                showProgressMode(ICM_DECAY);
                break;
            case R.id.iv_mim_mode:
                showProgressMode(SIM);
                break;
            case R.id.iv_decay_mode:
                showProgressMode(DECAY);
                break;
            case R.id.btn_language:
                showLanguageChangeDialog();
                break;
            case R.id.btn_update:
                //TODO 20191218 增加版本更新
                Toast.makeText(MainActivity.this, R.string.check_update, Toast.LENGTH_SHORT).show();
                downloadFile();
                break;
            case R.id.btn_help:
//                 showHelpCenterDialog();
                //GC20200327
                showHelpHomeDialog();
                break;
            case R.id.btn_records:
                showRecordsDialog();
                break;
            case R.id.btn_remote:
                break;
            default:
                break;
        }
    }

    /**
     * 参数设置
     */
    private void saveParamInfo() {
        //保存数据存储的单位状态
        int currentunit = StateUtils.getInt(MainActivity.this, AppConfig.CURRENT_UNIT, MI_UNIT);
        if (currentunit == FT_UNIT) {
            StateUtils.setInt(MainActivity.this, AppConfig.CURRENT_SAVE_UNIT, FT_UNIT);
        } else if (currentunit == MI_UNIT) {
            StateUtils.setInt(MainActivity.this, AppConfig.CURRENT_SAVE_UNIT, MI_UNIT);
        }

        //待测电缆长度、波速度、ID
        String cableLength = etCableLength.getText().toString();
        String cableVop = etCableVop.getText().toString();
        String cableId = etCableId.getText().toString();

        //最大最小波速度限制
        if (!TextUtils.isEmpty(etCableVop.getText().toString())) {
            double vop = Double.parseDouble(etCableVop.getText().toString());
            double maxVop;
            double minVop;
            if (CurrentUnit == MI_UNIT) {
                maxVop = 300;
                minVop = 90;
            } else {
                maxVop = Double.valueOf(UnitUtils.miToFt(300));
                minVop = Double.valueOf(UnitUtils.miToFt(90));
            }
            if (vop > maxVop) {
                etCableVop.setText(maxVop + "");
            }
            if (vop < minVop) {
                etCableVop.setText(minVop + "");
            }
        }

        //是否有测试缆信息
        boolean testLead;
        testLead = cbTestLead.isChecked();

        //测试线缆长度、波速度
        String length = etLength.getText().toString();
        String vop1 = etVop.getText().toString();
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(MainActivity.this, Constant.PARAM_INFO_KEY);
        if (paramInfo == null) {
            paramInfo = new ParamInfo();
        }
        paramInfo.setCableId(cableId);
        paramInfo.setTestLead(testLead);

        //处理拿到不输入的情况
        if (TextUtils.isEmpty(cableLength)) {
            cableLength = "0";
            paramInfo.setCableLength(cableLength);
        } else {
            paramInfo.setCableLength(cableLength);
        }
        if (TextUtils.isEmpty(cableVop)) {
            cableVop = "0";
            paramInfo.setCableVop(cableVop);
        } else {
            paramInfo.setCableVop(cableVop);
        }
        if (TextUtils.isEmpty(length)) {
            length = "0";
            paramInfo.setLength(length);

        } else {
            paramInfo.setLength(length);
        }
        if (TextUtils.isEmpty(vop1)) {
            vop1 = "0";
            paramInfo.setVop(vop1);

        } else {
            paramInfo.setVop(vop1);
        }
        StateUtils.setObject(MainActivity.this, paramInfo, Constant.PARAM_INFO_KEY);
        Toast.makeText(MainActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
    }

    private void showProgressMode(int mode) {
        showProgress();
        //开启线程
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("splash-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), threadFactory,
                new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //使程序休眠5秒
                    Intent intent = new Intent(getApplicationContext(), ModeActivity.class);
                    intent.setClass(MainActivity.this, ModeActivity.class);
                    intent.putExtra(BUNDLE_MODE_KEY, mode);
                    startActivity(intent);
                    if (tDialog != null) {
                        tDialog.dismiss();
                    }
                    //启动MainActivity
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        singleThreadPool.shutdown();
    }

    /**
     * 等待进度progress创建并显示    //G?? 作用？
     */
    private void showProgress() {
        //进度条
        tDialog = new TDialog.Builder(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_loading)
                .setScreenWidthAspect(this, 0.8f)
                .setCancelableOutside(false)
                .setGravity(Gravity.BOTTOM)
                .setOnBindViewListener(new OnBindViewListener() {
                    @Override
                    public void bindView(BindViewHolder viewHolder) {
                        progressBar = viewHolder.getView(R.id.progressBar);
                        tvProgress = viewHolder.getView(R.id.hardwareConnection);
                    }
                })
                .create()
                .show();
    }

    private void showLanguageChangeDialog() {
        languageChangeDialog = new LanguageChangeDialog(this);
        if (!languageChangeDialog.isShowing()) {
            languageChangeDialog.show();
        }
        languageChangeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!languageChangeDialog.getCloseStatus()) {
                    Intent intent = new Intent(MainActivity.this, ConnectService.class);
                    stopService(intent);
                    Intent intentSplash = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intentSplash);

                }
            }
        });
    }

    /**
     * 开始下载文件
     */
    public void downloadFile() {
        String xmlurl = Constant.BASE_API + "app/version.xml";
        Request request = new Request.Builder().url(xmlurl).addHeader("Accept-Encoding", "identity").build();
        //Request request = new Request.Builder().url(xmlurl).build();
        OkHttpClient httpClient = new OkHttpClient();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //下载失败
                Log.i("下载失败：", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = null;
                byte[] buff = new byte[2048];
                int len = 0;
                try {
                    inputStream = response.body().byteStream();
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(isr);
                    // 内存流 写入读取的数据
                    StringWriter sw = new StringWriter();
                    String str = null;
                    while ((str = br.readLine()) != null) {
                        sw.write(str);
                    }
                    br.close();
                    sw.close();
                    str = sw.toString();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new StringReader(str));
                    int eventType = parser.getEventType();
                    version = "";
                    url = "";
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String nodeName = parser.getName();
                        switch (eventType) {
                            // 开始解析某个结点
                            case XmlPullParser.START_TAG: {
                                if ("version".equals(nodeName)) {
                                    version = parser.nextText();
                                } else if ("url".equals(nodeName)) {
                                    url = parser.nextText();
                                }
                                break;
                            }
                            // 完成解析某个结点
                            case XmlPullParser.END_TAG: {
                                if ("update".equals(nodeName)) {
                                    if (Integer.parseInt(version) > AppUtils.getAppVersionCode()) {
                                        //获取文案
                                        if (!AppUpdateDialog.isShowUpdating) {
                                            handler.sendEmptyMessage(0);
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, R.string.has_new_version, Toast.LENGTH_SHORT).show();
                                    }
                                }
                                break;
                            }
                            default:
                                break;
                        }
                        eventType = parser.next();
                    }

                } catch (Exception e) {
//                    Logs.i("下载出错：" + e.getMessage());
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            showAppUpdate(version, url);
            return false;
        }
    });

    private void showAppUpdate(String version, String url) {
        if (mAppUpdateDialog == null) {
            mAppUpdateDialog = new AppUpdateDialog(this);
        }
        if (!mAppUpdateDialog.isShowing()) {
            mAppUpdateDialog.show();
        }
        mAppUpdateDialog.setVersionEntity(url);
    }

    private void showHelpCenterDialog() {
        HelpCenterDialog helpCenterDialog = new HelpCenterDialog(this);
        if (!helpCenterDialog.isShowing()) {
            helpCenterDialog.show();
        }
    }

    /**
     * 主页帮助对话框    //GC20200327
     */
    private void showHelpHomeDialog() {
        HelpHomeDialog helpHomeDialog = new HelpHomeDialog(this);
        if (!helpHomeDialog.isShowing()) {
            helpHomeDialog.show();
        }
    }

    private void showRecordsDialog() {
        ShowRecordsDialog customDialog = new ShowRecordsDialog(this);
        customDialog.setFromMain(true);
        if (!customDialog.isShowing()) {
            customDialog.show();
        }
    }

    /**
     *隐藏虚拟按键，暂时用不到。
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

    @Override
    protected void onResume() {
        initParamInfo();
        String languageType = StateUtils.getString(MyApplication.getInstances(), AppConfig.CURRENT_LANGUAGE, "ch");
        Constant.currentLanguage = languageType;
        super.onResume();
    }

    @Override
    protected void onPause() {
        /*if (receiver != null) {
            unregisterReceiver(receiver);
        }*/
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        Intent intent = new Intent(MainActivity.this, ConnectService.class);
        stopService(intent);
        super.onDestroy();
    }

}
