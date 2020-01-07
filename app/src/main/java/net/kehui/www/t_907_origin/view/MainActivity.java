package net.kehui.www.t_907_origin.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.percentlayout.widget.PercentRelativeLayout;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnBindViewListener;

import net.kehui.www.t_907_origin.ConnectService;
import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.application.AppConfig;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.application.MyApplication;
import net.kehui.www.t_907_origin.base.BaseActivity;
import net.kehui.www.t_907_origin.entity.ParamInfo;
import net.kehui.www.t_907_origin.ui.AppUpdateDialog;
import net.kehui.www.t_907_origin.ui.HelpCenterDialog;
import net.kehui.www.t_907_origin.ui.LanguageChangeDialog;
import net.kehui.www.t_907_origin.ui.ShowRecordsDialog;
import net.kehui.www.t_907_origin.util.AppUtils;
import net.kehui.www.t_907_origin.util.MultiLanguageUtil;
import net.kehui.www.t_907_origin.util.StateUtils;
import net.kehui.www.t_907_origin.util.UnitUtils;
import net.kehui.www.t_907_origin.util.WifiUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Locale;
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

import static net.kehui.www.t_907_origin.ConnectService.BROADCAST_ACTION_DEVICE_CONNECTED;
import static net.kehui.www.t_907_origin.ConnectService.BROADCAST_ACTION_DEVICE_CONNECT_FAILURE;
import static net.kehui.www.t_907_origin.ConnectService.BROADCAST_ACTION_DOWIFI_COMMAND;
import static net.kehui.www.t_907_origin.ConnectService.INTENT_KEY_COMMAND;
import static net.kehui.www.t_907_origin.application.Constant.CurrentUnit;
import static net.kehui.www.t_907_origin.application.Constant.FtUnit;
import static net.kehui.www.t_907_origin.application.Constant.LastUnit;
import static net.kehui.www.t_907_origin.application.Constant.MiUnit;
import static net.kehui.www.t_907_origin.view.ModeActivity.BUNDLE_COMMAND_KEY;
import static net.kehui.www.t_907_origin.view.ModeActivity.MODE_KEY;

/**
 * @author JWJ
 * @date 2019/12/1
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.tv_cable_vop_text)
    TextView tvCableVopText;
    @BindView(R.id.et_cable_vop)
    EditText etCableVop;
    @BindView(R.id.tv_cable_length_text)
    TextView tvCableLengthText;
    @BindView(R.id.et_cable_length)
    EditText etCableLength;
    @BindView(R.id.tv_cable_id_text)
    TextView tvCableIdText;
    @BindView(R.id.et_cable_id)
    EditText etCableId;
    @BindView(R.id.tv_length_text)
    TextView tvLengthText;
    @BindView(R.id.et_length)
    EditText etLength;
    @BindView(R.id.ll_test)
    LinearLayout llTest;
    @BindView(R.id.tv_vop_text)
    TextView tvVopText;
    @BindView(R.id.et_vop)
    EditText etVop;
    @BindView(R.id.tv_unit_text)
    TextView tvUnitText;

    @BindView(R.id.ll_length)
    LinearLayout llLength;
    @BindView(R.id.tv_vop_save)
    TextView tvSave;
    @BindView(R.id.ll_setting)
    PercentRelativeLayout llSetting;
    @BindView(R.id.iv_tdr_mode)
    ImageView ivTdrMode;
    @BindView(R.id.iv_icms_mode)
    ImageView ivIcmsMode;
    @BindView(R.id.iv_icmd_mode)
    ImageView ivIcmdMode;
    @BindView(R.id.iv_mim_mode)
    ImageView ivMimMode;
    @BindView(R.id.iv_decay_mode)
    ImageView ivDecayMode;
    @BindView(R.id.ll_mode)
    LinearLayout llMode;
    @BindView(R.id.btn_language)
    ImageView btnLanguage;
    @BindView(R.id.btn_update)
    ImageView btnUpdate;
    @BindView(R.id.btn_help)
    ImageView btnHelp;
    @BindView(R.id.btn_records)
    ImageView btnRecords;
    @BindView(R.id.btn_remote)
    ImageView btnRemote;
    @BindView(R.id.ll_featured)
    LinearLayout llFeatured;
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
    @BindView(R.id.tv_test_lead_text)
    TextView tvTestLeadText;
    @BindView(R.id.tv_length_unit)
    TextView tvLengthUnit;
    @BindView(R.id.tv_vop_unit)
    TextView tvVopUnit;
    @BindView(R.id.rl_info)
    LinearLayout rlInfo;
    @BindView(R.id.iv_wifi_status)
    ImageView ivWifiStatus;
    @BindView(R.id.iv_battery_status)
    ImageView ivBatteryStatus;

    private String cableVop;
    private String cableLength;
    private String cableId;
    /**
     * 测试缆信息
     */
    private Boolean testLead;
    private String length;
    private String vop;

    private ProgressBar progressBar;
    private TextView tvProgress;

    private ShowRecordsDialog customDialog;
    private LanguageChangeDialog languageChangeDialog;
    private TDialog tDialog;
    private HelpCenterDialog helpCenterDialog;

    private int unit;
    private int saveUnit;
    private AppUpdateDialog mAppUpdateDialog;
    private String version = "1";
    private String url = "baidu.com";

    public static final String MODE_KEY = "mode_key";
    public static final String DATA_TRANSFER_KEY = "dataTransfer";
    //定义intent bundle的key
    public static final String BUNDLE_PARAM_KEY = "bundle_param_key";
    //定义bundle 里参数command 的key
    public static final String BUNDLE_COMMAND_KEY = "bundle_command_key";

    private boolean needStopServce = true;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            //网络连接，更换网络图标
            if (action.equals(BROADCAST_ACTION_DEVICE_CONNECTED)) {
                //连接设备成功
                //如果成功，在跳转ModeActivity时需要主动下发给设备指令
                ConnectService.isConnected = true;
                ivWifiStatus.setImageResource(R.drawable.ic_wifi_connected);
                handler.postDelayed(() -> {
                    //电量
                    command = 0x06;
                    dataTransfer = 0x08;
                    startService();
                }, 20);
            }
            //网络断开，更换网络图标
            else if (action.equals(BROADCAST_ACTION_DEVICE_CONNECT_FAILURE)) {
                ConnectService.isConnected = false;
                ivWifiStatus.setImageResource(R.drawable.ic_no_wifi_connect);
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_no);
            } else if (action.equals(BROADCAST_ACTION_DOWIFI_COMMAND)) {
                wifiStream = intent.getIntArrayExtra(INTENT_KEY_COMMAND);
                assert wifiStream != null;
                doWifiCommand(wifiStream);

            }

        }

    };

    //去服务发送指令
    public void startService() {
        Intent intent = new Intent(MainActivity.this, ConnectService.class);
        //发送指令
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_COMMAND_KEY, command);
        bundle.putInt(MODE_KEY, mode);
        bundle.putInt(DATA_TRANSFER_KEY, dataTransfer);
        intent.putExtra(BUNDLE_PARAM_KEY, bundle);
        startService(intent);
    }

    private void doWifiCommand(int[] wifiArray) {
        //仪器触发时：APP发送接收数据命令
        if (wifiArray[5] == POWERDISPLAY) {
            int batteryValue = wifiArray[6] * 256 + wifiArray[7];
            if (batteryValue <= 2600) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_zero);

            } else if (batteryValue > 2600 && batteryValue <= 2818) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_one);

            } else if (batteryValue > 2818 && batteryValue <= 3018) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_two);

            } else if (batteryValue > 3018 && batteryValue <= 3220) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_three);

            } else if (batteryValue > 3220) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_four);

            }

        }

    }

    //隐藏虚拟按键，暂时用不到。
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ConnectService.needReconnect = true;
        //初始化单位相关
        initUnit();
        //读取配置默认值
        initParamInfo();

        initBroadcastReceiver();
    }

    /**
     * 注册广播接受服务发送的通知
     */
    private void initBroadcastReceiver() {
        //初始化画数据库监听广播   //GC20190713
        IntentFilter ifDisplay = new IntentFilter();
        ifDisplay.addAction(BROADCAST_ACTION_DEVICE_CONNECTED);
        ifDisplay.addAction(BROADCAST_ACTION_DEVICE_CONNECT_FAILURE);
        ifDisplay.addAction(BROADCAST_ACTION_DOWIFI_COMMAND);

        registerReceiver(receiver, ifDisplay);

        //判断服务里连接状态，如果已经连接，则重发广播，改变连接状态
        if (ConnectService.isConnected) {
            Intent intent = new Intent(BROADCAST_ACTION_DEVICE_CONNECTED);
            sendBroadcast(intent);
        }
    }

    int currentCheckedId = 0;

    private void initUnit() {
        tvMsg.setText(String.format(getString(R.string.main_notice), getString(R.string.mi)));
        unit = StateUtils.getInt(MainActivity.this, AppConfig.CURRENT_UNIT, MiUnit);
        if (unit == MiUnit) {
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
                    StateUtils.setInt(MainActivity.this, AppConfig.CURRENT_UNIT, FtUnit);
                    changeUnitView(FtUnit);

                } else if (checkedId == rbMi.getId()) {
                    StateUtils.setInt(MainActivity.this, AppConfig.CURRENT_UNIT, MiUnit);
                    changeUnitView(MiUnit);
                }
            }
        });
    }

    private void changeUnitView(int currentUnit) {
        CurrentUnit = currentUnit;
        if (currentUnit == MiUnit) {
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


    @OnClick({R.id.tv_vop_save, R.id.iv_tdr_mode, R.id.iv_icms_mode, R.id.iv_icmd_mode, R.id.iv_mim_mode, R.id.iv_decay_mode, R.id.btn_language, R.id.btn_update, R.id.btn_help, R.id.btn_records, R.id.btn_remote})
    public void onClick(View view) {
        Intent intent = new Intent();
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
                showHelpCenterDialog();
                break;
            case R.id.btn_records:
                showRecordsDialog();
                break;
            case R.id.btn_remote:
                break;
        }
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


    //开始下载文件
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

    private void showHelpCenterDialog() {
        helpCenterDialog = new HelpCenterDialog(this);
        if (!helpCenterDialog.isShowing()) {
            helpCenterDialog.show();
        }
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
                    intent.putExtra(MODE_KEY, mode);
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
     * 等待进度progress创建并显示
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

    private void showRecordsDialog() {

        customDialog = new ShowRecordsDialog(this);
        customDialog.setFromMain(true);
        if (!customDialog.isShowing()) {
            customDialog.show();
        }

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
                    //不需要关闭服务
                    needStopServce = true;
                    ConnectService.needReconnect = false;
                    Intent intent = new Intent(MainActivity.this, ConnectService.class);
                    stopService(intent);
                    //finish();
                    Intent intentSplash = new Intent(MainActivity.this, SplashActivity.class);
                    startActivity(intentSplash);

                }
            }
        });
    }

    private void saveParamInfo() {
        if (!TextUtils.isEmpty(etCableVop.getText().toString())) {
            double vop = Double.parseDouble(etCableVop.getText().toString());
            double maxVop;
            double minVop;
            if (CurrentUnit == MiUnit) {
                maxVop = 300;
                minVop = 90;
            } else {
                maxVop = Double.valueOf(UnitUtils.miToFt(300));
                minVop = Double.valueOf(UnitUtils.miToFt(90));
            }
            if (vop > maxVop) {
                etCableVop.setText(maxVop + "");
            }
            if (vop < minVop)
                etCableVop.setText(minVop + "");

        }

        //保存数据存储的单位状态
        int currentunit = StateUtils.getInt(MainActivity.this, AppConfig.CURRENT_UNIT, MiUnit);

        //保存数据库存储的数据单位状态
        if (currentunit == FtUnit) {
            StateUtils.setInt(MainActivity.this, AppConfig.CURRENT_SAVE_UNIT, FtUnit);

        } else if (currentunit == MiUnit) {
            StateUtils.setInt(MainActivity.this, AppConfig.CURRENT_SAVE_UNIT, MiUnit);
        }

        cableLength = etCableLength.getText().toString();
        cableVop = etCableVop.getText().toString();
        cableId = etCableId.getText().toString();
        if (cbTestLead.isChecked()) {
            testLead = true;
        } else {
            testLead = false;
        }

        length = etLength.getText().toString();
        vop = etVop.getText().toString();
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(MainActivity.this, Constant.PARAM_INFO_KEY);
        if (paramInfo == null) {
            paramInfo = new ParamInfo();
        }
        paramInfo.setCableId(cableId);
        paramInfo.setTestLead(testLead);
        //处理拿到不输入的情况
        /*if (CurrentUnit == MiUnit) {*/
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
        } else {
            paramInfo.setLength(length);
        }
        if (TextUtils.isEmpty(vop)) {
            vop = "0";
        } else {
            paramInfo.setVop(vop);
        }

        /*else {
            if (TextUtils.isEmpty(cableLength)) {
                cableLength = "0";
            } else {
                paramInfo.setCableLength(UnitUtils.ftToMi(Double.parseDouble(cableLength)));
            }
            if (TextUtils.isEmpty(cableVop)) {
                cableVop = "0";
            } else {
                paramInfo.setCableVop(UnitUtils.ftToMi(Double.parseDouble(cableVop)));
            }
            if (TextUtils.isEmpty(length)) {
                length = "0";
            } else {
                paramInfo.setLength(UnitUtils.ftToMi(Double.parseDouble(length)));
            }
            if (TextUtils.isEmpty(vop)) {
                vop = "0";
            } else {
                paramInfo.setVop(UnitUtils.ftToMi(Double.parseDouble(vop)));
            }
        }*/

        StateUtils.setObject(MainActivity.this, paramInfo, Constant.PARAM_INFO_KEY);
        Toast.makeText(MainActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
    }


    private void initParamInfo() {
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(MainActivity.this, Constant.PARAM_INFO_KEY);
        unit = StateUtils.getInt(MainActivity.this, AppConfig.CURRENT_UNIT, MiUnit);
        saveUnit = StateUtils.getInt(MainActivity.this, AppConfig.CURRENT_SAVE_UNIT, MiUnit);
        CurrentUnit = unit;
        if (paramInfo != null) {
            //如果单位是米
            if (unit == MiUnit) {
                if (saveUnit == MiUnit) {
                    //改变数值
                    if (!TextUtils.isEmpty(paramInfo.getCableVop())) {
                        if (paramInfo.getCableVop().equals("0") || paramInfo.getCableVop().equals("0.0"))
                            etCableVop.setText("");
                        else
                            etCableVop.setText(String.valueOf(paramInfo.getCableVop()));
                    }
                    if (!TextUtils.isEmpty(paramInfo.getCableLength())) {
                        if (paramInfo.getCableLength().equals("0") || paramInfo.getCableLength().equals("0.0"))
                            etCableLength.setText("");
                        else
                            etCableLength.setText(String.valueOf(paramInfo.getCableLength()));
                    }

                    etCableId.setText(paramInfo.getCableId());
                    if (paramInfo.getTestLead()) {
                        cbTestLead.setChecked(paramInfo.getTestLead());
                        if (!TextUtils.isEmpty(paramInfo.getLength())) {
                            etLength.setText(String.valueOf(paramInfo.getLength()));
                        }
                        if (!TextUtils.isEmpty(paramInfo.getVop())) {
                            etVop.setText(String.valueOf(paramInfo.getVop()));
                        }
                    }
                } else {
                    //改变数值
                    if (!TextUtils.isEmpty(paramInfo.getCableVop())) {
                        if (paramInfo.getCableVop().equals("0") || paramInfo.getCableVop().equals("0.0"))
                            etCableVop.setText("");
                        else
                            etCableVop.setText(String.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getCableVop()))));
                    }
                    if (!TextUtils.isEmpty(paramInfo.getCableLength())) {
                        if (paramInfo.getCableLength().equals("0") || paramInfo.getCableLength().equals("0.0"))
                            etCableLength.setText("");
                        else
                            etCableLength.setText(String.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getCableLength()))));
                    }

                    etCableId.setText(paramInfo.getCableId());
                    if (paramInfo.getTestLead()) {
                        cbTestLead.setChecked(paramInfo.getTestLead());
                        if (!TextUtils.isEmpty(paramInfo.getLength())) {
                            etLength.setText(String.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getLength()))));
                        }
                        if (!TextUtils.isEmpty(paramInfo.getVop())) {
                            etVop.setText(String.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getVop()))));
                        }
                    }
                }
            } else {
                if (saveUnit == FtUnit) {
                    //改变数值
                    if (!TextUtils.isEmpty(paramInfo.getCableVop())) {
                        if (paramInfo.getCableVop().equals("0") || paramInfo.getCableVop().equals("0.0"))
                            etCableVop.setText("");
                        else
                            etCableVop.setText(String.valueOf(paramInfo.getCableVop()));
                    }
                    if (!TextUtils.isEmpty(paramInfo.getCableLength())) {
                        if (paramInfo.getCableLength().equals("0") || paramInfo.getCableLength().equals("0.0"))
                            etCableLength.setText("");
                        else
                            etCableLength.setText(String.valueOf(paramInfo.getCableLength()));
                    }

                    etCableId.setText(paramInfo.getCableId());
                    if (paramInfo.getTestLead()) {
                        cbTestLead.setChecked(paramInfo.getTestLead());
                        if (!TextUtils.isEmpty(paramInfo.getLength())) {
                            etLength.setText(String.valueOf(paramInfo.getLength()));
                        }
                        if (!TextUtils.isEmpty(paramInfo.getVop())) {
                            etVop.setText(String.valueOf(paramInfo.getVop()));
                        }
                    }
                } else {
                    //改变数值
                    if (!TextUtils.isEmpty(paramInfo.getCableVop())) {
                        if (paramInfo.getCableVop().equals("0") || paramInfo.getCableVop().equals("0.0"))
                            etCableVop.setText("");
                        else
                            etCableVop.setText(String.valueOf(UnitUtils.miToFt(Double.valueOf(paramInfo.getCableVop()))));
                    }
                    if (!TextUtils.isEmpty(paramInfo.getCableLength())) {
                        if (paramInfo.getCableLength().equals("0") || paramInfo.getCableLength().equals("0.0"))
                            etCableLength.setText("");
                        else
                            etCableLength.setText(String.valueOf(UnitUtils.miToFt(Double.valueOf(paramInfo.getCableLength()))));
                    }

                    etCableId.setText(paramInfo.getCableId());
                    if (paramInfo.getTestLead()) {
                        cbTestLead.setChecked(paramInfo.getTestLead());
                        if (!TextUtils.isEmpty(paramInfo.getLength())) {
                            etLength.setText(String.valueOf(UnitUtils.miToFt(Double.valueOf(paramInfo.getLength()))));
                        }
                        if (!TextUtils.isEmpty(paramInfo.getVop())) {
                            etVop.setText(String.valueOf(UnitUtils.miToFt(Double.valueOf(paramInfo.getVop()))));
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
                    if (CurrentUnit == MiUnit) {
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
        //切换语言时不要关闭服务
        if (needStopServce == true) {
            ConnectService.needReconnect = false;
            Intent intent = new Intent(MainActivity.this, ConnectService.class);
            stopService(intent);
            //android.os.Process.killProcess(android.os.Process.myPid());

        }
        super.onDestroy();
    }


}