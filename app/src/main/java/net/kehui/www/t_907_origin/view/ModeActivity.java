package net.kehui.www.t_907_origin.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;

import androidx.percentlayout.widget.PercentRelativeLayout;

import com.timmy.tdialog.TDialog;
import com.timmy.tdialog.base.BindViewHolder;
import com.timmy.tdialog.listener.OnViewClickListener;

import net.kehui.www.t_907_origin.ConnectService;
import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.adpter.MyChartAdapterBase;
import net.kehui.www.t_907_origin.application.AppConfig;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.base.BaseActivity;
import net.kehui.www.t_907_origin.entity.ParamInfo;
import net.kehui.www.t_907_origin.ui.MoveView;
import net.kehui.www.t_907_origin.ui.MoveWaveView;
import net.kehui.www.t_907_origin.ui.SaveRecordsDialog;
import net.kehui.www.t_907_origin.ui.ShowRecordsDialog;
import net.kehui.www.t_907_origin.ui.HelpModeDialog;
import net.kehui.www.t_907_origin.ui.SparkView.SparkView;
import net.kehui.www.t_907_origin.util.StateUtils;
import net.kehui.www.t_907_origin.util.UnitUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static net.kehui.www.t_907_origin.ConnectService.BROADCAST_ACTION_DEVICE_CONNECTED;
import static net.kehui.www.t_907_origin.ConnectService.BROADCAST_ACTION_DEVICE_CONNECT_FAILURE;
import static net.kehui.www.t_907_origin.ConnectService.BROADCAST_ACTION_DOWIFI_COMMAND;
import static net.kehui.www.t_907_origin.ConnectService.BROADCAST_ACTION_DOWIFI_WAVE;
import static net.kehui.www.t_907_origin.ConnectService.INTENT_KEY_COMMAND;
import static net.kehui.www.t_907_origin.ConnectService.INTENT_KEY_WAVE;
import static net.kehui.www.t_907_origin.application.Constant.FT_UNIT;
import static net.kehui.www.t_907_origin.application.Constant.MI_UNIT;
import static net.kehui.www.t_907_origin.application.Constant.SaveToDBGain;
import static net.kehui.www.t_907_origin.application.Constant.batteryValue;
import static net.kehui.www.t_907_origin.view.ListActivity.DISPLAY_ACTION;

public class ModeActivity extends BaseActivity {

    @BindView(R.id.tv_gain_add)
    ImageView tvGainAdd;
    @BindView(R.id.tv_gain_min)
    ImageView tvGainMin;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    /**
     * 自动测距结果 //GC20190708
     */
    @BindView(R.id.tv_information)
    TextView  tvInformation;
    @BindView(R.id.tv_pulse_width)
    ImageView tvPulseWidth;
    @BindView(R.id.tv_compare)
    ImageView tvCompare;
    @BindView(R.id.tv_cal)
    ImageView tvCal;
    @BindView(R.id.tv_range)
    ImageView tvRange;
    @BindView(R.id.tv_file)
    ImageView tvFile;
    @BindView(R.id.ll_adjust)
    LinearLayout llAdjust;
    @BindView(R.id.tv_home)
    ImageView tvHome;
    @BindView(R.id.tv_zero)
    ImageView tvZero;
    @BindView(R.id.tv_cursor_plus)
    ImageView tvCursorPlus;
    @BindView(R.id.tv_cursor_min)
    ImageView tvCursorMin;
    @BindView(R.id.tv_zoom_plus)
    ImageView tvZoomPlus;
    @BindView(R.id.tv_zoom_min)
    ImageView tvZoomMin;
    @BindView(R.id.tv_test)
    ImageView tvTest;
    @BindView(R.id.tv_help)
    ImageView tvHelp;
    @BindView(R.id.rl_feature)
    LinearLayout rlFeature;
    @BindView(R.id.fullWave)
    SparkView fullWave;
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.tv_range_value)
    TextView tvRangeValue;
    @BindView(R.id.tv_gain_value)
    TextView tvGainValue;
    @BindView(R.id.tv_vop_value)
    TextView tvVopValue;
    @BindView(R.id.tv_balance_value)
    TextView tvBalanceValue;
    @BindView(R.id.tv_zoom_value)
    TextView tvZoomValue;
    @BindView(R.id.ll_info)
    PercentRelativeLayout llInfo;
    @BindView(R.id.tv_balance_plus)
    ImageView tvBalancePlus;
    @BindView(R.id.tv_balance_min)
    ImageView tvBalanceMin;
    @BindView(R.id.layout_tv_memory)
    ImageView layoutTvMemory;
    @BindView(R.id.layout_tv_both)
    ImageView layoutTvBoth;
    @BindView(R.id.ll_compare)
    LinearLayout llCompare;
    @BindView(R.id.tv_vop_plus)
    ImageView tvVopPlus;
    @BindView(R.id.tv_vop_min)
    ImageView tvVopMin;
    @BindView(R.id.tv_vop_save)
    ImageView tvSave;
    @BindView(R.id.ll_cal)
    LinearLayout llCal;
    @BindView(R.id.tv_250m)
    TextView tv250m;
    @BindView(R.id.tv_500m)
    TextView tv500m;
    @BindView(R.id.tv_1km)
    TextView tv1km;
    @BindView(R.id.tv_2km)
    TextView tv2km;
    @BindView(R.id.tv_4km)
    TextView tv4km;
    @BindView(R.id.tv_8km)
    TextView tv8km;
    @BindView(R.id.tv_16km)
    TextView tv16km;
    @BindView(R.id.tv_32km)
    TextView tv32km;
    @BindView(R.id.tv_64km)
    TextView tv64km;
    @BindView(R.id.ll_range)
    LinearLayout llRange;
    @BindView(R.id.tv_file_records)
    ImageView tvFileRecords;
    @BindView(R.id.ll_records)
    LinearLayout llRecords;
    @BindView(R.id.iv_compare_close)
    ImageView ivCompareClose;
    @BindView(R.id.iv_cal_close)
    ImageView ivGainClose;
    @BindView(R.id.iv_range_close)
    ImageView ivRangeClose;
    @BindView(R.id.iv_records_close)
    ImageView ivRecordsClose;
    @BindView(R.id.tv_decay_value)
    TextView tvDelayValue;
    @BindView(R.id.tv_delay_text)
    TextView tvDelayText;
    @BindView(R.id.tv_origin)
    ImageView tvOrigin;
    @BindView(R.id.tv_trigger_delay)
    ImageView tvTriggerDelay;
    @BindView(R.id.tv_records_save)
    ImageView tvRecordsSave;
    @BindView(R.id.tv_delay_plus)
    ImageView tvDelayPlus;
    @BindView(R.id.tv_delay_min)
    ImageView tvDelayMin;
    @BindView(R.id.ll_trigger_delay)
    LinearLayout llTriggerDelay;
    /**
     * wdx 20191218 波宽度添加保存信息
     */
    @BindView(R.id.ll_pulse_width)
    LinearLayout llPulseWidth;
    @BindView(R.id.iv_pulse_width_close)
    ImageView ivPulseWidthClose;
    @BindView(R.id.et_pulse_width_id)
    EditText etPulseWidth;
    @BindView(R.id.iv_wifi_status)
    ImageView ivWifiStatus;
    @BindView(R.id.iv_battery_status)
    ImageView ivBatteryStatus;
    @BindView(R.id.tv_balance_text)
    TextView tvBalanceText;
    @BindView(R.id.tv_balance_space)
    TextView tvBalanceSpace;
    @BindView(R.id.iv_close_trigger_delay)
    ImageView ivCloseTriggerDelay;
    @BindView(R.id.tv_delay_space)
    TextView tvDelaySpace;
    @BindView(R.id.tv_wave_text)
    TextView tvWaveText;
    @BindView(R.id.tv_wave_value)
    TextView tvWaveValue;
    @BindView(R.id.tv_wave_space)
    TextView tvWaveSpace;
    @BindView(R.id.view_move_vertical_wave)
    MoveWaveView viewMoveVerticalWave;
    @BindView(R.id.tv_wave_pre)
    ImageView tvWavePre;
    @BindView(R.id.tv_wave_next)
    ImageView tvWaveNext;
    @BindView(R.id.tv_icm)
    TextView tvIcm;
    @BindView(R.id.tv_distance_unit)
    TextView tvDistanceUnit;
    @BindView(R.id.rl_wave)
    RelativeLayout rlWave;
    @BindView(R.id.tv_vop_unit)
    TextView tvVopUnit;
    @BindView(R.id.mv_wave)
    MoveView mvWave;
    @BindView(R.id.tv_cable_vop_unit)
    TextView tvCableVopUnit;
    @BindView(R.id.tv_pulse_width_save)
    ImageView tvPulseWidthSave;
    @BindView(R.id.ll_horizontal_view)
    LinearLayout llHorizontalView;

    private int index;
    //计算滑动时的基数
    private int fenzi1;
    //初始化滑块位置
    private int fenzi2;
    private int CurrentActionDownValue = 0;

    private boolean alreadyDisplayWave;
    //设置是否需要进入页面接收数据，此处是为了适配从主页面展示波形时重复接收数据
    private boolean isReceiveData = true;

    private TDialog tDialog;

    /**
     * 定义bundle的key-value
     */
    public static final String BUNDLE_COMMAND_KEY = "command";
    public static final String BUNDLE_MODE_KEY = "mode";
    public static final String BUNDLE_DATA_TRANSFER_KEY = "dataTransfer";
    public static final String BUNDLE_PARAM_KEY = "bundle_param_key";

    /**
     * 全局的handler对象用来执行UI更新
     */
    public static final int DO_WAVE = 1;
    public static final int ORGANIZE_WAVE = 2;
    public static final int VIEW_REFRESH = 3;
    public static final int DISPLAY_DATABASE = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
        ButterKnife.bind(this);

        alreadyDisplayWave = false;
        mode = getIntent().getIntExtra(BUNDLE_MODE_KEY, 0);
        isReceiveData = getIntent().getBooleanExtra("isReceiveData", true);
        initUnit();
        initSparkView();
        initViewMoveWave();
        initBtnRangeView();
        //读取脉宽信息    //GC20200331    位置提前
        initPulseWidth();
        //20191217将获取设置中范围的函数调整到上方，用于读取设置中的位置
        initRange();
        initFrame();
        setChartListener();
        initMode();
        initBroadcastReceiver();
        //发送广播——处理从主页中显示的本地数据
        if (getIntent().getIntExtra("display_action", 0) == DISPLAY_DATABASE) {
            isReceiveData = false;
            Intent intent = new Intent(DISPLAY_ACTION);
            intent.putExtra("display_action", ModeActivity.DISPLAY_DATABASE);
            sendBroadcast(intent);
        }

    }

    /**
     * 计量单位初始化 (m ft)
     */
    private void initUnit() {
        Constant.CurrentUnit = StateUtils.getInt(ModeActivity.this, AppConfig.CURRENT_UNIT, Constant.MI_UNIT);
        Constant.CurrentSaveUnit = StateUtils.getInt(ModeActivity.this, AppConfig.CURRENT_SAVE_UNIT, Constant.MI_UNIT);
        if (Constant.CurrentUnit == Constant.MI_UNIT) {
            tvDistanceUnit.setText(R.string.mi);
            tvVopUnit.setText(R.string.mius);
        } else {
            tvDistanceUnit.setText(R.string.ft);
            tvVopUnit.setText(R.string.ftus);
        }
    }

    /**
     * 初始化sparkView
     */
    public void initSparkView() {
        for (int i = 0; i < 510; i++) {
            waveArray[i] = 128;
            //Constant.WaveData[i] = 128;
        }
        myChartAdapterMainWave = new MyChartAdapterBase(waveArray, null,
                false, 0, false);
        myChartAdapterFullWave = new MyChartAdapterBase(waveArray, null,
                false, 0, false);
        fullWave.setAdapter(myChartAdapterMainWave);
        setMoveView();
        Log.i("Draw", "初始化绘制结束");
    }

    /**
     * 初始化波形移动监听事件
     */
    int moverMoveValue = 0;
    int currentMoverPosition510 = 0;    //当前滑块左侧在510个点的数值
    private void initViewMoveWave() {

        viewMoveVerticalWave.setViewMoveWaveListener(new MoveWaveView.ViewMoveWaveListener() {
            @Override
            public void onMoved(float x, float y) {
                //上下移动波形
                fullWave.setSparkViewMove(y);
                Log.e("ModeActivity", y + "");
            }
            @Override
            public void onMoveEnded() {

            }
        });
        fenzi1 = (dataLength / (densityMax / density)) - 510;

        mvWave.setViewMoveWaveListener(new MoveView.ViewMoveWaveListener() {

            @Override
            public void onMoved(float x) {
                Log.e("【滑块】", "当前X：" + x + "/长度：" + mvWave.getParentWidth() + "/510Value:" + get510Value(x, mvWave.getParentWidth()));
                //滑块X在510个点中的位置
                int currentMoverX = get510Value(x, mvWave.getParentWidth());
                //计算滑块移动的数值，在波形框宽度510中的对应数值
                moverMoveValue = currentMoverX - currentMoverPosition510;
                //因当前波形是缩放时从某个点开始取的，因此滑块左侧的位置就是重新抽点的起始点，按照比例可以算出来
                organizeMoveWaveData(currentMoverX * dataLength / 510);
                //比对波形
                try {
                    organizeCompareWaveData(currentMoverX * dataLength / 510);
                } catch (Exception l_Ex) {
                }
                Log.e("【滑块】", "currentMoverX:" + currentMoverX + "moverMoveValue：" + moverMoveValue);
                //重新绘制波形
                displayWave();

                //滑块移动时变换虚光标位置
                positionVirtual = positionVirtual - moverMoveValue * (densityMax / density);
                //滑块移动时变换实光标位置
                positionReal = positionReal - moverMoveValue * (densityMax / density);
                //滑块移动时变换紫色实光标位置    //GC20200330
                positionSim = positionSim - moverMoveValue * (densityMax / density);

                //重新设置当前的滑块左侧位置数值
                //重新定位实光标
                if (positionReal > 0 && positionReal <= 510) {
                    fullWave.setScrubLineReal(positionReal);
                } else {
                    fullWave.setScrubLineRealDisappear();
                }

                //重新定位紫色实光标    //GC20200330
                if (mode == SIM) {
                    if (positionSim > 0 && positionSim <= 510) {
                        fullWave.setScrubLineSim(positionSim);
                    } else {
                        fullWave.setScrubLineSimDisappear();
                    }
                }

                //重新定位虚光标
                if (positionVirtual >= 0 && positionVirtual <= 510) {
                    fullWave.setScrubLineVirtual(positionVirtual);
                } else {
                    fullWave.setScrubLineVirtualDisappear();
                }

                //如果零点为0，而且当前绘制波形的起点也是0，说明波形在最左侧，需要在0位置显示实光标。
                if (zero == 0 & currentMoverPosition510 == 0) {
                    fullWave.setScrubLineReal(0);
                }

                //重新赋值当前波形的绘制起始位置
                currentMoverPosition510 = currentMoverX;
                if (zero == 0 & currentMoverPosition510 == 0) {
                    fullWave.setScrubLineReal(0);
                }
                Log.e("【光标参数】", "positionReal：" + positionReal + "/zero:" + zero + "/positionVirtual:" + positionVirtual + "/pointDistance:" + pointDistance + "/MoverPosition:" + (currentMoverX * dataLength / 510));

            }
        });

    }

    /**
     * 初始化英尺状态下范围按钮文字
     */
    private void initBtnRangeView() {
        if (Constant.CurrentUnit == Constant.FT_UNIT) {
            tv250m.setText(getResources().getString(R.string.btn_250m_to_ft));
            tv500m.setText(getResources().getString(R.string.btn_500m_to_ft));
            tv1km.setText(getResources().getString(R.string.btn_1km_to_yingli));
            tv2km.setText(getResources().getString(R.string.btn_2km_to_yingli));
            tv4km.setText(getResources().getString(R.string.btn_4km_to_yingli));
            tv8km.setText(getResources().getString(R.string.btn_8km_to_yingli));
            tv16km.setText(getResources().getString(R.string.btn_16km_to_yingli));
            tv32km.setText(getResources().getString(R.string.btn_32km_to_yingli));
            tv64km.setText(getResources().getString(R.string.btn_64km_to_yingli));
        }
    }

    /**
     * 读取主页设置中存储的范围数据，并初始化
     */
    private void initRange() {
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(ModeActivity.this, Constant.PARAM_INFO_KEY);
        double localRange;
        if (paramInfo != null && paramInfo.getCableLength() != null && !TextUtils.isEmpty(paramInfo.getCableLength())) {
            localRange = Double.valueOf(paramInfo.getCableLength());
            //如果数据库里存的是英尺值则转换为米
            if (Constant.CurrentSaveUnit == FT_UNIT) {
                localRange = Double.valueOf(UnitUtils.ftToMi(localRange));
            }

            if (localRange == 0.0 || localRange == 0) {
                //距离硬性输入0，默认脉宽
                range = (0x11);
                gain = 13;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_500m_to_ft));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_500m));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 40;
                    etPulseWidth.setText(String.valueOf(40));
                }
            } else if (localRange <= 250) {
                range = (0x99);
                gain = 13;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_250m_to_ft));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_250m));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 40;
                    etPulseWidth.setText(String.valueOf(40));
                }
            } else if (localRange > 250 && localRange <= 500) {
                range = (0x11);
                gain = 13;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_500m_to_ft));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_500m));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 40;
                    etPulseWidth.setText(String.valueOf(40));
                }
            } else if (localRange > 500 && localRange <= 1000) {
                range = (0x22);
                gain = 13;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_1km_to_yingli));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_1km));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 80;
                    etPulseWidth.setText(String.valueOf(80));
                }
            } else if (localRange > 1000 && localRange <= 2000) {
                range = (0x33);
                gain = 10;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_2km_to_yingli));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_2km));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 160;
                    etPulseWidth.setText(String.valueOf(160));
                }
            } else if (localRange > 2000 && localRange <= 4000) {
                range = (0x44);
                gain = 10;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_4km_to_yingli));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_4km));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 320;
                    etPulseWidth.setText(String.valueOf(320));
                }
            } else if (localRange > 4000 && localRange <= 8000) {
                range = (0x55);
                gain = 10;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_8km_to_yingli));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_8km));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 640;
                    etPulseWidth.setText(String.valueOf(640));
                }
            } else if (localRange > 8000 && localRange <= 16000) {
                range = (0x66);
                gain = 9;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_16km_to_yingli));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_16km));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 1280;
                    etPulseWidth.setText(String.valueOf(1280));
                }
            } else if (localRange > 16000 && localRange <= 32000) {
                range = (0x77);
                gain = 9;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_32km_to_yingli));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_32km));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 2560;
                    etPulseWidth.setText(String.valueOf(2560));
                }
            } else if (localRange > 32000) {
                range = (0x88);
                gain = 9;
                if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_64km_to_yingli));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_64km));
                }
                //脉宽未设置时根据范围初始化显示 //GC20200331
                if (!Constant.hasPulseWidth) {
                    pulseWidth = 5120;
                    etPulseWidth.setText(String.valueOf(5120));
                }
            }
        } else {
            //没有设置的距离
            range = (0x11);
            if (Constant.CurrentUnit == Constant.FT_UNIT) {
                tvRangeValue.setText(getResources().getString(R.string.btn_500m_to_ft));
            } else {
                tvRangeValue.setText(getResources().getString(R.string.btn_500m));
            }
            //脉宽未设置时根据范围初始化显示 //GC20200331
            if (!Constant.hasPulseWidth) {
                pulseWidth = 40;
                etPulseWidth.setText(String.valueOf(40));
            }
        }
        Constant.RangeValue = range;

    }

    /**
     * 初始化界面框架
     */
    public void initFrame() {
        Constant.ModeValue = TDR;
        tvGainValue.setText(String.valueOf(gain));
        Constant.Gain = gain;
        velocity = getLocalValue();
        setVelocity(velocity);
        Constant.Velocity = velocity;
        tvBalanceValue.setText(String.valueOf(balance));
        tvZoomValue.setText("1 : " + density);
        tvDelayValue.setText(delay + "μs");
        //初始化距离显示
        calculateDistance(Math.abs(pointDistance - zero));
        //自动测距显示    //GC20190708
        tvInformation.setVisibility(View.GONE);
        //SIM光标位置初始化    //GC20190712
        simZero = StateUtils.getInt(ModeActivity.this, AppConfig.CURRENT_CURSOR_POSITION, 12);
        //GC20200330
        simPosition = 12;
        //测试缆信息添加    //GC20200103
        leadLength = getLocalLength();
        leadVop = getLocalVop();
        //模式及面电量图标初始化 //GC20200314
        if (batteryValue == 0) {
            ivBatteryStatus.setImageResource(R.drawable.ic_battery_zero);
        } else if (batteryValue == 1) {
            ivBatteryStatus.setImageResource(R.drawable.ic_battery_one);
        } else if (batteryValue == 2) {
            ivBatteryStatus.setImageResource(R.drawable.ic_battery_two);
        } else if (batteryValue == 3) {
            ivBatteryStatus.setImageResource(R.drawable.ic_battery_three);
        } else if (batteryValue == 4) {
            ivBatteryStatus.setImageResource(R.drawable.ic_battery_four);
        } else if (batteryValue == -1) {
            //GC20200423
            ivBatteryStatus.setImageResource(R.drawable.ic_battery_no);
        }

    }

    /**
     * 监听虚光标位置变化
     */
    private void setChartListener() {
        //波形下发区域拿出，做波形的左右滑动
        fullWave.setScrubListener(new SparkView.OnScrubListener() {
            @Override
            public void onActionDown(Object x, float y) {
                Log.e("【ActionDown】", "ActionDown:" + x + "/Y:" + y);
                //手指按下记录位置
                CurrentActionDownValue = (Integer) x;
            }

            @Override
            public void onScrubbed(Object value, float y) {
                //手触摸区域判断，值越小，波形滑动区域越大
                if (y < 400) {
                    if ((Integer) value <= 510) {
                        //画虚光标
                        fullWave.setScrubLineVirtual((Integer) value);
                        //虚光标移动的点数  //GC20191218
                        cursorMoveValue = (int) value - positionVirtual;
                        Log.e("【虚光标】", "滑动开始：value:" + value + "/positionVirtual:" + positionVirtual + "/cursorMoveValue:" + cursorMoveValue + "/pointDistance" + pointDistance + "/zero:" + zero); //GN 数值变化0-509
                        //在原始数据中的位置
                        pointDistance = pointDistance + cursorMoveValue * density;
                        Log.e("【虚光标】", "滑动中：value:" + value + "/positionVirtual:" + positionVirtual + "/cursorMoveValue:" + cursorMoveValue + "/pointDistance" + pointDistance + "/zero:" + zero); //GN 数值变化0-509
                        //记忆移动后虚光标在画布中的位置
                        positionVirtual = (int) value;
                        int WaveDataStart = currentMoverPosition510 * dataLength / 510;
                        Log.e("【虚光标】", "滑动结束：value:" + value + "/positionVirtual:" + positionVirtual + "/cursorMoveValue:" + cursorMoveValue + "/pointDistance" + pointDistance + "/zero:" + zero + "/WaveDataStart:" + WaveDataStart); //GN 数值变化0-509

                        if (positionVirtual == 0 && zero == 0 && WaveDataStart < 510) {
                            pointDistance = 0;
                        }
                        //距离显示
                        calculateDistance(Math.abs(pointDistance - zero));
                    }
                } else {
                    Log.e("【新滑块】", "value：" + value);
                    Log.e("【新滑块】", "CurrentActionDownValue：" + CurrentActionDownValue);
                    //计算手指滑动的距离
                    moverMoveValue = (Integer) value - CurrentActionDownValue;
                    Log.e("【新滑块】", "moverMoveValue：" + moverMoveValue);
                    Log.e("【新滑块】", "前currentMoverPosition510：" + currentMoverPosition510);

                    //滑块X位置
                    currentMoverPosition510 = currentMoverPosition510 + moverMoveValue;
                    //滑块x位置大于等于0且滑块X位置+滑块宽度不超过波形区域，则允许波形滑动。
                    if (currentMoverPosition510 >= 0 && (currentMoverPosition510 + (mvWave.getWidth() * 510 / mvWave.getParentWidth())) <= 510 && density != densityMax) {
                        Log.e("【新滑块】", "后currentMoverPosition510：" + currentMoverPosition510);

                        //因当前波形是缩放时从某个点开始取的，因此滑块左侧的位置就是重新抽点的起始点，按照比例可以算出来
                        organizeMoveWaveData(currentMoverPosition510 * dataLength / 510);
                        //比对波形
                        try {
                            organizeCompareWaveData(currentMoverPosition510 * dataLength / 510);
                        } catch (Exception l_Ex) {
                        }
                        Log.e("【滑块】", "currentMoverX:" + currentMoverPosition510 + "moverMoveValue：" + moverMoveValue);
                        //重新绘制波形
                        displayWave();
                        //滑块移动时变换虚光标位置
                        positionVirtual = positionVirtual - moverMoveValue * (densityMax / density);
                        //滑块移动时变换实光标位置
                        positionReal = positionReal - moverMoveValue * (densityMax / density);
                        //滑块移动时变换紫色实光标位置    //GC20200330
                        positionSim = positionSim - moverMoveValue * (densityMax / density);

                        //重新设置当前的滑块左侧位置数值
                        //重新定位实光标
                        if (positionReal > 0 && positionReal <= 510) {
                            fullWave.setScrubLineReal(positionReal);
                        } else {
                            fullWave.setScrubLineRealDisappear();
                        }

                        //重新定位紫色实光标    //GC20200330
                        if (mode == SIM) {
                            if (positionSim > 0 && positionSim <= 510) {
                                fullWave.setScrubLineSim(positionSim);
                            } else {
                                fullWave.setScrubLineSimDisappear();
                            }
                        }

                        //重新定位虚光标
                        if (positionVirtual >= 0 && positionVirtual <= 510) {
                            fullWave.setScrubLineVirtual(positionVirtual);
                        } else {
                            fullWave.setScrubLineVirtualDisappear();
                        }

                        if (zero == 0 & currentMoverPosition510 == 0) {
                            fullWave.setScrubLineReal(0);
                        }

                        //手指拖动时关联移动滑块到指定位置
                        int moverPositon;
                        moverPositon = mvWave.getParentWidth() * currentMoverPosition510 / 510;
                        setMoverPostion(moverPositon);
                    } else {
                        currentMoverPosition510 = currentMoverPosition510 - moverMoveValue;
                    }
                    CurrentActionDownValue = (Integer) value;
                }

            }
        });
    }

    //初始化模式
    private void initMode() {
        switch (mode) {
            case TDR:
                initTDRView();
                break;
            case ICM:
                initICMSURGEView();
                break;
            case ICM_DECAY:
                initICMDECAYView();
                break;
            case SIM:
                initSIMView();
                break;
            case DECAY:
                initDecayView();
                break;
            default:
                break;
        }
    }

    /**
     * 初始化广播接收器
     */
    private void initBroadcastReceiver() {
        IntentFilter ifDisplay = new IntentFilter();
        ifDisplay.addAction(BROADCAST_ACTION_DEVICE_CONNECTED);
        ifDisplay.addAction(BROADCAST_ACTION_DEVICE_CONNECT_FAILURE);
        ifDisplay.addAction(BROADCAST_ACTION_DOWIFI_COMMAND);
        ifDisplay.addAction(BROADCAST_ACTION_DOWIFI_WAVE);
        ifDisplay.addAction(DISPLAY_ACTION);
        registerReceiver(receiver, ifDisplay);
        //判断服务里连接状态，如果已经连接，则发送广播，改变连接状态
        if (ConnectService.isConnected) {
            Intent intent = new Intent(BROADCAST_ACTION_DEVICE_CONNECTED);
            sendBroadcast(intent);
        }
    }

    /**
     * 监听广播并做相应处理
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //G?? 作用是啥
            handler.sendEmptyMessage(intent.getIntExtra("display_action", 0));

            String action = intent.getAction();
            assert action != null;
            switch (action) {
                case BROADCAST_ACTION_DEVICE_CONNECTED:
                    Log.e("ModeActivity", "连接成功");
                    //网络连接，更换网络图标   //GC20200325
                    ConnectService.isConnected = true;
                    ivWifiStatus.setImageResource(R.drawable.ic_wifi_connected);
                    //重连有对话框消对话框    //GC20200319
                    if (tDialog != null) {
                        tDialog.dismiss();
                        Log.e("DIA", "重连先取消对话框");
                    }
                    //如果网络连接后于读取本地波形数据，则再网络连接时设置读出的几个参数。
                    if (!isReceiveData || isDatabase) {
                        setModeNoCmd(Constant.Para[0]);
                        setRangeNoCmd(Constant.Para[1]);
                        setGain(Constant.Para[2]);
                        setVelocityNoCmd(Constant.Para[3]);
                    } else {
                        //第一次连接设备初始化
                        //方式
                        setMode(mode);
                        handler.postDelayed(() -> {
                            //范围
                            setRange(range);
                        }, 20);
                        handler.postDelayed(() -> {
                            //增益
                            setGain(gain);
                        }, 20);
                        //不同模式下命令初始化调整  //GC20200424
                        if (mode == TDR) {
                            handler.postDelayed(() -> {
                                //脉宽  //GC20200331
                                setPulseWidth(pulseWidth);
                            }, 20);
                        } else if (mode == ICM || mode == SIM) {
                            handler.postDelayed(() -> {
                                //延时0
                                setDelay(0);
                            }, 20);
                        }
                        //延时100毫秒发送测试命令，100毫秒是等待设备回复命令信息，如果不延时，有可能设备执行不完命令。
                        handler.postDelayed(ModeActivity.this::clickTest, 100);
                    }
                    break;
                case BROADCAST_ACTION_DEVICE_CONNECT_FAILURE:
                    //网络断开，更换网络图标、电量图标
                    ConnectService.isConnected = false;
                    ivWifiStatus.setImageResource(R.drawable.ic_no_wifi_connect);
                    ivBatteryStatus.setImageResource(R.drawable.ic_battery_no);
                    break;
                case BROADCAST_ACTION_DOWIFI_COMMAND:
                    //处理获取到的命令数据
                    wifiStream = intent.getIntArrayExtra(INTENT_KEY_COMMAND);
                    assert wifiStream != null;
                    doWifiCommand(wifiStream);
                    break;
                case BROADCAST_ACTION_DOWIFI_WAVE:
                    //64公里波形数据过大，正常的广播无法传递，改成全局变量。
                    //int[] wifiStreamNew = ConnectService.mExtra;
                    //wifiStream = ConnectService.mExtra;
                    //assert wifiStreamNew != null;
                    int[] wifiStreamNew = intent.getIntArrayExtra(INTENT_KEY_WAVE);
                    if (wifiStreamNew[3] == WAVE_SIM || wifiStreamNew[3] == WAVE_TDR_ICM_DECAY) {
                        setWaveParameter();
                    }
                    Message message = Message.obtain();
                    message.what = ModeActivity.DO_WAVE;
                    Bundle bundle = new Bundle();
                    bundle.putIntArray("WAVE", wifiStreamNew);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    break;
                default:
                    break;
            }

        }

    };

    public Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case DO_WAVE:
                int[] wifiStreamNew = msg.getData().getIntArray("WAVE");
                assert wifiStreamNew != null;
                doWifiWave(wifiStreamNew);
                break;
            case ORGANIZE_WAVE:
                if (density < densityMax) {
                    organizeNormalWaveData();
                } else {
                    organizeWaveData();
                }
                break;
            //显示波形
            case VIEW_REFRESH:
                resetWhatNeed();
                //Log.e("【时效测试】", "开始组织波形数据");
                if (density < densityMax) {
                    //setDensity(density);
                    organizeNormalWaveData();
                    // Log.e("【时效测试】", "组织波形数据完毕");
                    displayWave();
                } else {
                    organizeWaveData();
                    // Log.e("【时效测试】", "组织波形数据完毕");
                    displayWave();
                }
                //Log.e("【时效测试】", "绘制完毕");
                break;
            //显示本地存储的数据
            case DISPLAY_DATABASE:
                //数据库波形显示   //GC20190713
                setDateBaseParameter();
                try {
                    organizeWaveData();
                    displayWave();
                } catch (Exception l_ex) {
                }
                break;
            default:
                break;
        }
        return false;
    });

    /**
     * wdx 20191218 波宽度属性值初始化
     */
    private void initPulseWidth() {
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(ModeActivity.this, Constant.PULSE_WIDTH_INFO_KEY);
        if (paramInfo != null) {
            if (Constant.hasPulseWidth) {
                //有过保存操作，读取本地存储     //GC20200331
                pulseWidth = paramInfo.getPulseWidth();
                etPulseWidth.setText(String.valueOf(pulseWidth));
            }
        }

        etPulseWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    int pulseWidth = Integer.parseInt(s.toString());
                    int maxPulseWidth = 7000;
                    if (pulseWidth > maxPulseWidth) {
                        Toast.makeText(ModeActivity.this, getResources().getString(R.string
                                .maxpulsewidth), Toast.LENGTH_SHORT).show();
                        etPulseWidth.setText(maxPulseWidth + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //读取本地存储的波速度信息，默认为172，计算距离时要求是米单位，所以如果数据库里存的是英尺数，应该转换为米数。
    private double getLocalValue() {
        double vopValue = 172;
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(ModeActivity.this, Constant.PARAM_INFO_KEY);
        if (paramInfo != null) {
            if (paramInfo.getCableVop() != null && !TextUtils.isEmpty(paramInfo.getCableVop())) {
                if (Constant.CurrentSaveUnit == MI_UNIT) {
                    vopValue = Double.valueOf(paramInfo.getCableVop());
                } else {
                    vopValue = Double.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getCableVop())));
                }
            }
        }
        if (vopValue == 0 || vopValue == 0.0) {
            vopValue = 172;
        }
        return vopValue;
    }

    /**
     * 读取本地存储的测试缆状态
     */
    private double getLocalLength() {
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(ModeActivity.this, Constant.PARAM_INFO_KEY);
        if (paramInfo != null) {
            if (paramInfo.getLength() != null && !TextUtils.isEmpty(paramInfo.getLength())) {
                if (Constant.CurrentSaveUnit == MI_UNIT) {
                    leadLength = Double.valueOf(paramInfo.getLength());
                } else {
                    leadLength = Double.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getLength())));
                }
            }
        }
        return leadLength;
    }

    /**
     * 读取本地存储的测试缆状态
     */
    private double getLocalVop() {
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(ModeActivity.this, Constant.PARAM_INFO_KEY);
        if (paramInfo != null) {
            if (paramInfo.getVop() != null && !TextUtils.isEmpty(paramInfo.getVop())) {
                if (Constant.CurrentSaveUnit == MI_UNIT) {
                    leadVop = Double.valueOf(paramInfo.getVop());
                } else {
                    leadVop = Double.valueOf(UnitUtils.ftToMi(Double.valueOf(paramInfo.getVop())));
                }
            }
        }
        if (leadVop == 0 || leadVop == 0.0) {
            leadVop = 172;
        }
        return leadVop;
    }

    /**
     * 相同范围下点测试获取新波形数据
     */
    private void organizeNormalWaveData() {
        for (int i = currentMoverPosition510 * dataLength / 510, j = 0; j < 510; i = i + density, j++) {
            //组织TDR、ICM、ICM_DECAY、DECAY和SIM的第一条波形的数据
            waveDraw[j] = Constant.WaveData[i + index];
            //组织SIM的第二条波形的数据
            if (mode == SIM) {
                waveCompare[j] = Constant.SimData[i];
                if (!isDatabase) {
                    simDraw1[j] = simArray1[i];
                    simDraw2[j] = simArray2[i];
                    simDraw3[j] = simArray3[i];
                    simDraw4[j] = simArray4[i];
                    simDraw5[j] = simArray5[i];
                    simDraw6[j] = simArray6[i];
                    simDraw7[j] = simArray7[i];
                    simDraw8[j] = simArray8[i];
                }
            }
        }
    }

    /**
     * 处理APP接收的命令
     */
    private void doWifiCommand(int[] wifiArray) {
        //仪器触发时：APP发送接收数据命令
        if ((wifiArray[5] == COMMAND_TRIGGER) && (wifiArray[6] == TRIGGERED)) {
            command = COMMAND_RECEIVE_WAVE;
            dataTransfer = mode;
            //发送指令
            startService();
            // Log.e("【时效测试】", "发送接收波形数据命令");
            ConnectService.canAskPower = false;
            if (tDialog != null) {
                //重新进入时不需要再发取消测试命令
                Constant.isCancelAim = true;
                tDialog.dismiss();
            }
            tDialog = new TDialog.Builder(getSupportFragmentManager())
                    .setLayoutRes(R.layout.receiving_data)
                    .setScreenWidthAspect(this, 0.25f)
                    .setCancelableOutside(false)
                    .create()
                    .show();
            Log.e("DIA", " 正在接受数据显示" + " ICM/SIM/DECAY");
        } else if (wifiArray[5] == POWER_DISPLAY) {
            int batteryValue = wifiArray[6] * 256 + wifiArray[7];
            if (batteryValue <= 2600) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_zero);
            } else if (batteryValue > 2600 && batteryValue <= 2818) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_one);
            } else if (batteryValue > 2818 && batteryValue <= 3018) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_two);
            } else if (batteryValue > 3018 && batteryValue <= 3120) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_three);
            } else if (batteryValue > 3120) {
                ivBatteryStatus.setImageResource(R.drawable.ic_battery_four);
            }
        }

    }

    //缩放时重新组织记忆的波形数据
    private void organizeCompareWaveData(int start) {
        //按比例抽出510个点
        for (int i = start, j = 0; j < 510; i = i + density, j++) {
            //组织TDR、ICM、ICM_DECAY、DECAY和SIM的第一条波形的数据
            waveCompareDraw[j] = waveCompare[i];
        }
        int[] waveDraw250 = new int[255];
        //需要操作的数据长度
        if ((mode == TDR) || (mode == SIM)) {
            //GC20191223
            if (range == RANGE_250) {
                //取出前255个点数
                for (int i = 0, j = 0; i < 255; i++, j++) {
                    waveDraw250[j] = waveCompareDraw[i];
                }
                //算出差值的255个点
                for (int i = 0, j = 1; i < 254; i++, j += 2) {
                    waveCompareDraw[j] = waveDraw250[i] + (waveDraw250[i + 1] - waveDraw250[i]) / 2;
                }
                //将原始255个点分散
                for (int i = 0, j = 0; j < 255; i++, j++) {
                    waveCompareDraw[2 * j] = waveDraw250[i];
                }
            }
        }

    }

    //滑块移动过程中，重新组织需要绘制的波形数据
    private void organizeMoveWaveData(int start) {
        //按比例抽出510个点
        for (int i = start, j = 0; j < 510; i = i + density, j++) {
            //组织TDR、ICM、ICM_DECAY、DECAY和SIM的第一条波形的数据
            if (i < Constant.WaveData.length) {
                waveDraw[j] = Constant.WaveData[i];
            }
            //组织SIM的第二条波形的数据
            if (mode == SIM) {
                waveCompare[j] = Constant.SimData[i];
                if (!isDatabase) {
                    simDraw1[j] = simArray1[i];
                    simDraw2[j] = simArray2[i];
                    simDraw3[j] = simArray3[i];
                    simDraw4[j] = simArray4[i];
                    simDraw5[j] = simArray5[i];
                    simDraw6[j] = simArray6[i];
                    simDraw7[j] = simArray7[i];
                    simDraw8[j] = simArray8[i];
                }
            }
        }
    }

    /**
     * 根据滑块位置获取在波形框中的位置
     *
     * @param x     滑块左侧在滑条的位置，单位为像素
     * @param right 滑条长度
     * @return 转换好的值
     */
    private int get510Value(float x, float right) {
        return (int) (510 * x / right);
    }

    private void initDecayView() {
        tvMode.setText(getResources().getText(R.string.btn_decay));
        viewMoveVerticalWave.setVisibility(View.GONE);
        tvWaveNext.setVisibility(View.GONE);
        tvWavePre.setVisibility(View.GONE);
        tvBalanceValue.setVisibility(View.GONE);
        tvBalanceText.setVisibility(View.GONE);
        tvBalanceSpace.setVisibility(View.GONE);
        tvDelayValue.setVisibility(View.GONE);
        tvDelayText.setVisibility(View.GONE);
        tvDelaySpace.setVisibility(View.GONE);
        tvBalancePlus.setVisibility(View.GONE);
        tvBalanceMin.setVisibility(View.GONE);
        tvBalanceText.setVisibility(View.GONE);
        tvBalanceValue.setVisibility(View.GONE);
        tvPulseWidth.setVisibility(View.GONE);
        tvOrigin.setVisibility(View.GONE);
        tvPulseWidth.setVisibility(View.GONE);
        tvTriggerDelay.setVisibility(View.GONE);
        tvWaveValue.setVisibility(View.GONE);
        tvWaveText.setVisibility(View.GONE);
        tvWaveSpace.setVisibility(View.GONE);
    }

    //即MIM
    private void initSIMView() {
        tvMode.setText(getResources().getText(R.string.btn_sim));
        viewMoveVerticalWave.setVisibility(View.VISIBLE);
        tvWaveNext.setVisibility(View.VISIBLE);
        tvWavePre.setVisibility(View.VISIBLE);

        tvBalanceMin.setVisibility(View.GONE);
        tvBalancePlus.setVisibility(View.GONE);
        tvCompare.setVisibility(View.GONE);
        tvBalanceValue.setVisibility(View.GONE);
        tvBalanceText.setVisibility(View.GONE);
        tvBalanceSpace.setVisibility(View.GONE);
        tvDelayValue.setVisibility(View.VISIBLE);
        tvDelayText.setVisibility(View.VISIBLE);
        tvPulseWidth.setVisibility(View.GONE);
    }

    private void initICMDECAYView() {
        viewMoveVerticalWave.setVisibility(View.GONE);
        tvWaveNext.setVisibility(View.GONE);
        tvWavePre.setVisibility(View.GONE);
        tvBalancePlus.setVisibility(View.GONE);
        tvBalanceMin.setVisibility(View.GONE);
        tvBalanceText.setVisibility(View.GONE);
        tvBalanceValue.setVisibility(View.GONE);
        tvDelayValue.setVisibility(View.GONE);
        tvDelayText.setVisibility(View.GONE);
        tvDelaySpace.setVisibility(View.GONE);
        tvTriggerDelay.setVisibility(View.GONE);

        tvPulseWidth.setVisibility(View.GONE);
        tvOrigin.setVisibility(View.GONE);
        tvMode.setText(getResources().getText(R.string.btn_icm_decay));
        tvWaveValue.setVisibility(View.GONE);
        tvWaveText.setVisibility(View.GONE);
        tvWaveSpace.setVisibility(View.GONE);
        //TODO 20191226 为什么又改成了iCM?二话不说先屏蔽再说
        //mode = ICM;
    }

    private void initICMSURGEView() {
        viewMoveVerticalWave.setVisibility(View.GONE);
        tvWaveNext.setVisibility(View.GONE);
        tvWavePre.setVisibility(View.GONE);
        tvBalancePlus.setVisibility(View.GONE);
        tvBalanceMin.setVisibility(View.GONE);
        tvBalanceSpace.setVisibility(View.GONE);
        tvBalanceText.setVisibility(View.GONE);
        tvBalanceValue.setVisibility(View.GONE);
        tvPulseWidth.setVisibility(View.GONE);
        tvOrigin.setVisibility(View.GONE);
        tvMode.setText(getResources().getText(R.string.btn_icm));
        tvWaveValue.setVisibility(View.GONE);
        tvWaveText.setVisibility(View.GONE);
        tvWaveSpace.setVisibility(View.GONE);
    }

    private void initTDRView() {
        viewMoveVerticalWave.setVisibility(View.GONE);
        tvMode.setText(getResources().getText(R.string.btn_tdr));
        tvWaveNext.setVisibility(View.GONE);
        tvWavePre.setVisibility(View.GONE);
        tvDelayValue.setVisibility(View.GONE);
        tvDelayText.setVisibility(View.GONE);
        tvDelaySpace.setVisibility(View.GONE);
        tvOrigin.setVisibility(View.GONE);
        tvTriggerDelay.setVisibility(View.GONE);
        tvWaveValue.setVisibility(View.GONE);
        tvWaveText.setVisibility(View.GONE);
        tvWaveSpace.setVisibility(View.GONE);
    }

    /**
     * 计算距离  //GC20190709   //GC20191231 程序结构优化
     */
    private void calculateDistance(int cursorDistance) {
        double distance;
        int k = 1;
        int l;
        int lFault;

        //脉冲电流方式下range=6(32km)和range=7(64km)实时25M采样率，其余方式和范围实时100M采样率，此时相对其它方式采样周期扩大4倍
        if (((mode == DECAY) || mode == ICM || mode == ICM_DECAY) && (rangeState > 6)) {
            k = 4;
        }

        distance = (((double) cursorDistance * velocity) * k) / 2 * 0.01;
        if (mode == DECAY) {
            //DECAY方式距离/2
            distance = (((double) cursorDistance * velocity / 2) * k) / 2 * 0.01;
        } else if ((mode == TDR) || (mode == SIM)) {
            //GC20191226    250m范围/2
            if (range == RANGE_250) {
                distance = (((double) cursorDistance * velocity / 2) * k) / 2 * 0.01;
            }
        } else if ((mode == ICM) || (mode == ICM_DECAY)) {
            //有测试线缆     //GC20200103
            if (leadLength > 0) {
                //实际点数
                l = (int) (leadLength * 2000 / leadVop / 10);
                lFault = cursorDistance - l;
                distance = (((double) lFault * velocity) * k) / 2 * 0.01 + leadLength;
            }

        }

        //TODO 2019-1223-0947 显示的距离也存下来，保存波形的时候使用
        Constant.CurrentLocation = distance;
        //距离界面显示
        if (Constant.CurrentUnit == Constant.MI_UNIT) {
            tvDistance.setText(new DecimalFormat("0.00").format(distance));
        } else {
            tvDistance.setText(UnitUtils.miToFt(distance));
        }

    }

    public double getVelocity() {
        return velocity;
    }

    /**
     * @param delay 需要发送的延时控制命令值 / 响应信息栏延时变化
     */
    public void setDelay(int delay) {
        this.delay = delay;
        command = COMMAND_DELAY;
        dataTransfer = delay;
        tvDelayValue.setText(delay + "μs");
        //发送指令
        startService();
    }

    public int getDelay() {
        return delay;
    }

    /**
     * @param density 响应状态栏波速度变化
     */
    public void setDensity(int density) {
        this.density = density;
        tvZoomValue.setText("1 : " + density);
        Log.e("GT", "density：" + density);
        //GC20191219
        organizeZoomWaveData();
        displayWave();
    }

    public int getDensity() {
        return density;
    }

    /**
     * 组织需要绘制的放大缩小波形数组（抽点510个）  //GC20191219
     * 缩放时重新组织绘制的波形数据，重新绘制虚实光标。
     */
    public int currentStart = 0;
    private void organizeZoomWaveData() {
        //抽点的起始位置和虚光标位置
        currentStart = 0;
        //Log.e("【滑块相关】-开始处理缩放数据前", "densityMax:" + densityMax + "/density:" + density + "/pointDistance:" + pointDistance + "/positionVirtual" + positionVirtual + "/positionReal:" + positionReal + "/zero:" + zero + "/datalength:" + dataLength + "/start:" + currentStart);
        if (pointDistance < 255 * density) {
            //波形靠最右侧
            positionVirtual = pointDistance / density;
            Log.e("GT", "1：start：" + currentStart);
            //根据起始点判断是否画实光标
            if (zero > 510 * density) {
                //不画实光标
                positionReal = zero / density;
                fullWave.setScrubLineRealDisappear();
            } else {
                //画实光标
                positionReal = zero / density;
                Log.e("GT", "1：positionReal：" + positionReal);
                fullWave.setScrubLineReal(positionReal);
            }
            //GC20200330
            if (mode ==SIM) {
                if (simPosition > 510 * density) {
                    //不画紫色实光标
                    positionSim = simPosition / density;
                    fullWave.setScrubLineSimDisappear();
                } else {
                    //画紫色实光标
                    positionSim = simPosition / density;
                    fullWave.setScrubLineSim(positionSim);
                }
            }

        } else if ((pointDistance >= 255 * density) && (pointDistance < dataLength - 255 * density)) {
            //波形靠以虚光标原始位置为中心
            Log.e("GT", "2：dataLength：" + dataLength);
            Log.e("GT", "2：pointDistance：" + pointDistance);
            currentStart = pointDistance - 255 * density;
            positionVirtual = 255;
            Log.e("GT", "2：start：" + currentStart);
            //根据虚光标位置判断是否画实光标
            if ((pointDistance - zero) > 255 * density) {
                positionReal = positionVirtual - (pointDistance - zero) / density;
                //不画实光标
                fullWave.setScrubLineRealDisappear();
            } else if ((zero - pointDistance) >= 254 * density) {
                positionReal = positionVirtual - (pointDistance - zero) / density;
                //不画实光标
                fullWave.setScrubLineRealDisappear();
            } else {
                //画实光标
                positionReal = positionVirtual - (pointDistance - zero) / density;
                Log.e("GT", "2：positionReal：" + positionReal);
                fullWave.setScrubLineReal(positionReal);
            }
            //GC20200330
            if (mode == SIM) {
                if ((pointDistance - simPosition) > 255 * density) {
                    fullWave.setScrubLineSimDisappear();
                } else if ((simPosition - pointDistance) >= 254 * density) {
                    //不画紫色实光标
                    fullWave.setScrubLineSimDisappear();
                } else {
                    //画紫色实光标
                    positionSim = positionVirtual - (pointDistance - simPosition) / density;
                    fullWave.setScrubLineSim(positionSim);
                }
            }

        } else if (pointDistance >= dataLength - 255 * density) {
            //波形靠最左侧
            currentStart = dataLength - 510 * density;
            positionVirtual = 510 - (dataLength - pointDistance) / density;
            Log.e("GT", "3：positionVirtual：" + positionVirtual);
            Log.e("GT", "3：start：" + currentStart);
            //根据起始点判断是否画实光标
            if (zero > dataLength - 510 * density) {
                //画实光标
                positionReal = 510 - (dataLength - zero) / density;
                Log.e("GT", "3：positionReal：" + positionReal);
                fullWave.setScrubLineReal(positionReal);
            } else {
                //不画实光标
                positionReal = 510 - (dataLength - zero) / density;
                fullWave.setScrubLineRealDisappear();
            }
            //GC20200330
            if (mode == SIM) {
                if (simPosition > dataLength - 510 * density) {
                    //画紫色实光标
                    positionSim = 510 - (dataLength - simPosition) / density;
                    fullWave.setScrubLineReal(positionReal);
                } else {
                    //不画紫色实光标
                    positionSim = 510 - (dataLength - simPosition) / density;
                    fullWave.setScrubLineSimDisappear();
                }
            }
        }
        //还原状态
        if (density == densityMax) {
            currentStart = 0;
            positionVirtual = pointDistance / densityMax;
            positionReal = zero / densityMax;
            //画实光标
            fullWave.setScrubLineReal(positionReal);

            //画紫色实光标    //GC20200330
            if (mode == SIM) {
                positionSim = simPosition / densityMax;
                fullWave.setScrubLineSim(positionSim);
            }
            Log.e("GT", "4：还原");
        }
        //画虚光标
        fullWave.setScrubLineVirtual(positionVirtual);

        //Log.e("【滑块相关】-开始处理缩放数据后", "densityMax:" + densityMax + "/density:" + density + "/pointDistance:" + pointDistance + "/positionVirtual" + positionVirtual + "/positionReal:" + positionReal + "/zero:" + zero + "/datalength:" + dataLength + "/start:" + currentStart);
        //按比例抽出510个点
        for (int i = currentStart, j = 0; j < 510; i = i + density, j++) {
            //组织TDR、ICM、ICM_DECAY、DECAY和SIM的第一条波形的数据
            waveDraw[j] = Constant.WaveData[i + index];
            //组织SIM的第二条波形的数据
            if (mode == SIM) {
                waveCompare[j] = Constant.SimData[i];
                if (!isDatabase) {
                    simDraw1[j] = simArray1[i];
                    simDraw2[j] = simArray2[i];
                    simDraw3[j] = simArray3[i];
                    simDraw4[j] = simArray4[i];
                    simDraw5[j] = simArray5[i];
                    simDraw6[j] = simArray6[i];
                    simDraw7[j] = simArray7[i];
                    simDraw8[j] = simArray8[i];
                }
            }
        }
        //组织比对波形数据
        try {
            organizeCompareWaveData(currentStart);
        } catch (Exception l_ex) {
        }
        //设置水平滑块左侧位置
        int moverXValue = 0;
        //转换为在波形框中的对应数值
        moverXValue = currentStart * 510 / dataLength;
        currentMoverPosition510 = moverXValue;
        //Log.e("【滑块】", "" + moverXValue);

        //转换为屏幕坐标
        moverXValue = mvWave.getParentWidth() * moverXValue / 510;
        //重新设置滑块大小
        setHorizontalMoveView();
        //移动滑块到指定位置
        setMoverPostion(moverXValue);
        // Log.e("【光标参数】", "positionReal：" + positionReal + "/zero:" + zero + "/positionVirtual:" + positionVirtual + "/pointDistance:" + pointDistance);

    }

    /**
     * 在sparkView界面显示波形
     */
    private void displayWave() {
        //画波形
        myChartAdapterMainWave.setmTempArray(waveDraw);
        myChartAdapterFullWave.setmTempArray(waveDrawFull);
        myChartAdapterMainWave.setShowCompareLine(isCom);
        myChartAdapterFullWave.setShowCompareLine(isCom);
        if (mode == SIM) {
            if (isCom) {
                myChartAdapterMainWave.setmCompareArray(waveCompare);
                myChartAdapterFullWave.setmCompareArray(waveCompareFull);

            }
        }
        myChartAdapterMainWave.notifyDataSetChanged();
        myChartAdapterFullWave.notifyDataSetChanged();
        //有对话框消对话框
        if (tDialog != null) {
            tDialog.dismiss();
            Log.e("DIA", "正在接受数据隐藏" + " 波形绘制完成");
        }

        tvZoomMin.setEnabled(true);
        tvZoomPlus.setEnabled(true);
        alreadyDisplayWave = true;
    }

    //设置滑块参数
    private void setMoveView() {
        RectF sparkViewRectf = myChartAdapterMainWave.getDataBounds();
        viewMoveVerticalWave.setSparkViewRect(sparkViewRectf);
        //水平滑动view传递父view
        mvWave.setParentView(llHorizontalView);

    }

    /**
     * 脉冲电流故障自动计算过程  //GC20190708
     */
    private void icmAutoTest() {
        //1.判断增益是否合适
        gainJudgment();
        switch (gainState) {
            case 0:
                tvInformation.setText("");
                break;
            case 1:
                gainState = 0;
                //组织数据画波形
                handler.sendEmptyMessage(VIEW_REFRESH);
                //显示增益过大
                tvInformation.setVisibility(View.VISIBLE);
                tvInformation.setText(getResources().getString(R.string.gain_too_high));
                return;
            case 2:
                gainState = 0;
                //组织数据画波形
                handler.sendEmptyMessage(VIEW_REFRESH);
                //显示增益过小
                tvInformation.setVisibility(View.VISIBLE);
                tvInformation.setText(getResources().getString(R.string.gain_too_low));
                return;
            default:
                break;
        }
        //软件滤波
        softwareFilter();
        //积分
        integral();
        //2.击穿放电判断
        breakdownJudgment();
        //显示不击穿 //GC20191231
        if (!breakDown) {
            //组织数据画波形
            handler.sendEmptyMessage(VIEW_REFRESH);
            //显示不击穿    //GC20190710
            tvInformation.setVisibility(View.VISIBLE);
            tvInformation.setText(getResources().getString(R.string.not_break_down));
            return;
        }
        //计算方向脉冲
        calculatePulse();
        //计算故障距离并在界面显示
        correlationSimple();
        //放电脉冲位置确定——确定实光标
        breakPointCalculate();
        //光标自动定位
        icmAutoCursor();
        //组织数据画波形
        handler.sendEmptyMessage(VIEW_REFRESH);
    }

    /**
     * 脉冲电流直闪故障自动计算过程  //GC20200109
     */
    private void icmAutoTestDC() {
        //1.判断增益是否合适
        gainJudgment();
        switch (gainState) {
            case 0:
                tvInformation.setText("");
                break;
            case 1:
                gainState = 0;
                //组织数据画波形
                handler.sendEmptyMessage(VIEW_REFRESH);
                //显示增益过大
                tvInformation.setVisibility(View.VISIBLE);
                tvInformation.setText(getResources().getString(R.string.gain_too_high));
                return;
            case 2:
                gainState = 0;
                //组织数据画波形
                handler.sendEmptyMessage(VIEW_REFRESH);
                //显示增益过小
                tvInformation.setVisibility(View.VISIBLE);
                tvInformation.setText(getResources().getString(R.string.gain_too_low));
                return;
            default:
                break;
        }
        //软件滤波
        softwareFilter();
        //计算方向脉冲
        calculatePulse();
        //计算故障距离并在界面显示
        correlationSimpleDC();
        //光标自动定位
        icmAutoCursor();
        //组织数据画波形
        handler.sendEmptyMessage(VIEW_REFRESH);
    }

    /**
     * 脉冲电流方式增益自动判断
     */
    private void gainJudgment() {
        int i;
        int max = 0;
        int sub;

        //计算波形有效数据的极值
        for (i = 0; i < dataMax - removeIcmDecay[rangeState]; i++) {
            sub = waveArray[i] - 128;
            if (Math.abs(sub) > max) {
                max = Math.abs(sub);
            }
        }
        if (max <= 42) {
            //判断增益过小——如果最大值小于 15% 38
            gainState = 2;
            return;
        }
        for (i = 0; i < dataMax - removeIcmDecay[rangeState]; i++) {
            if ((waveArray[i] > 242) || (waveArray[i] < 13)) {
                //判断增益过大
                gainState = 1;
                return;
            }
        }
    }

    /**
     * 脉冲电流方式软件滤波   方向脉冲法自动计算-软件滤波，一阶滞后滤波，低通截止频率约750kHz（两个采样频率都是这个截止频率）
     */
    private void softwareFilter() {
        int i;
        for (i = 1; i < dataMax - removeIcmDecay[rangeState]; i++) {
            if (rangeState > 6) {
                //25M采样——32km、64km范围
                waveArrayFilter[i] = 0.6232 * waveArrayFilter[i - 1] + 0.3768 * (double) (waveArray[i] - 122);//1.5M
            } else {
                waveArrayFilter[i] = 0.9058 * waveArrayFilter[i - 1] + 0.0942 * (double) (waveArray[i] - 122);//1.5M
            }
        }
    }

    /**
     * 脉冲电流方式数字积分   方向脉冲法自动计算-数字积分,反演电流
     */
    private void integral() {
        for (int i = 1; i < dataMax - removeIcmDecay[rangeState]; i++) {
            if (rangeState > 6) {
                //25M采样——32km、64km范围
                waveArrayIntegral[i] = waveArrayIntegral[i - 1] + waveArrayFilter[i] * 4e-8;
            } else {
                waveArrayIntegral[i] = waveArrayIntegral[i - 1] + waveArrayFilter[i] * 1e-8;
            }
        }
    }

    /**
     * 判断是否击穿放电
     */
    private void breakdownJudgment() {
        int i;
        //从触发开始计算初始值(去除波形前面的直线部分)
        int start = 140;    //GC20200110 提前一部分 start = 92;
        double sum = 0;

        //GC20191231    //方法三  刷新击穿判断
        breakDown = false;
        //计算均值做基准
        for (i = start + 64; i < start + 72; i++) {
            sum = sum + waveArrayIntegral[i];
        }
        sum = sum / (double)8;
        //积分电流
        for (i = start + 64; i < dataMax - 50; i++) {
            if (waveArrayIntegral[i] < 0) {
                if(waveArrayIntegral[i] < sum * (double)2) {
                    breakDown = true;
                    break;
                }
            }
        }
    }

    /**
     * 脉冲电流方式  计算方向脉冲   方向脉冲法自动计算-使用滤波后电流的微分求VL=L * di/dt，滤波后电流*波阻抗 //使用滤波后电流进行微分
     */
    double L = 10e-6;
    double z = 25;
    private void calculatePulse() {
        double[] V = new double[65560];
        int i;
        double min = 0;
        //测距起点
        int breakPoint = 0;
        int start;

        //有测试缆  //GC20200103
        if (leadLength > 0) {
            z = (double)50;
        } else {
            z = (double)25;
        }
        //GC20191231
        for (i = 0; i < dataMax - removeIcmDecay[rangeState] - 1; i++) {
            if (rangeState > 6) {
                //25M采样——32km、64km范围
                V[i] = (waveArrayFilter[i + 1] - waveArrayFilter[i]) * 4.0e8;
            } else {
                V[i] = (waveArrayFilter[i + 1] - waveArrayFilter[i]) * 1.0e8;
            }
        }
        //方法三  确定测距起点
        start = 140;    //GC20200110 提前一部分 start = 120
        //测距起点使用一次导数的最小值，从触发后64点开始取，躲过电容放电脉冲
        for (i = start + 64; i < dataMax - 50; i++) {
            if((V[i] < min) && (V[i] < 0)) {
                min = V[i];
                breakPoint = i;
            }
        }
        breakBk = breakPoint;

        //计算VL
        for(i = 0; i < dataMax - removeIcmDecay[rangeState]; i++) {
            V[i] = V[i] * L * -1.0;
        }
        //计算方向行波
        for(i = 0; i < dataMax - removeIcmDecay[rangeState]; i++) {
            s1[i] = V[i] + waveArrayFilter[i] * z;
            s2[i] = V[i] - waveArrayFilter[i] * z;
        }

    }

    /**
     * 脉冲电流直闪方式  计算故障距离(抽点做数据相关)  方向脉冲法自动计算-使用相关计算故障距离
     */
    private void correlationSimpleDC()  {
        int i;
        int j = 0;
        int k;
        float p;
        float[] P = new float[510];
        int w1;
        int w2;
        int w3;
        float[] s1Simple = new float[510];
        float[] s2Simple = new float[510];
        int maxBak ;

        //GC20200109 DC方式下处理
        breakBk = 140;
        if(range >= 6) {//25M采样
            if(breakBk > (50/4)) {//需要修改，32km和64km采样频率变了，需要调整参数
                w1 = breakBk - (50/4);      //相关窗左侧
            } else {
                w1 = breakBk;
            }
            w2 = breakBk + (350/4);     //相关窗右侧
        } else {
            if(breakBk > 50) {//需要修改，32km和64km采样频率变了，需要调整参数
                w1 = breakBk - 50;      //相关窗左侧
            } else {
                w1 = breakBk;
            }
            w2 = breakBk + 350;     //相关窗右侧
        }
        for(i = 0;i < 510;i++) { //抽点
            s1Simple[i] = (float)s1[j];
            s2Simple[i] = (float)s2[j];
            j = j + densityMaxIcmDecay[rangeState];
        }
        w1 = w1 / densityMaxIcmDecay[rangeState];
        w2 = w2 / densityMaxIcmDecay[rangeState];
        w3 = 510 - w2;

        float[] S1 = new float[65556];
        float[] S2 = new float[65556];

        for(i = w1;i < w2;i++) {
            S1[i - w1] = s1Simple[i];
        }
        for(i = 0;i < w3;i++) {
            for(k = w1;k < w2;k++) {
                S2[k - w1] = s2Simple[k + i];
            }
            p = (float)0.0;                    //清零
            for(j = 0;j < (w2 - w1);j++) { //进行相关运算
                p += S1[j] * S2[j] * -1.0;
            }
            P[i] = p;                //将整条波形的相关运算值存入P数组中
        }
        //计算P数组中的最大值，并确定位置
        float max = P[0];
        int maxIndex = 0;
        for (i = 0; i < w3; i++) {
            if (P[i] > max) {
                max = P[i];
                maxIndex = i;
            }
        }

        //换算为整条波形数据中的点数
        maxIndex = (w1 + maxIndex) * densityMaxIcmDecay[rangeState];
        //GC20191231
        maxBak = maxIndex;

        w1 = w1 * densityMaxIcmDecay[rangeState];
        w2 = w2 * densityMaxIcmDecay[rangeState];

        for (i = w1; i < w2; i++) {
            S1[i - w1] = (float)s1[i];
        }

        for (i = (maxIndex - densityMaxIcmDecay[rangeState]); i < (maxIndex + densityMaxIcmDecay[rangeState]); i++) {
            for (k = 0; k < w2 - w1; k++) {
                S2[k] = (float)s2[k + i];
            }
            //清零
            p = (float)0.0;
            //进行相关运算S
            for (j = 0; j < (w2 - w1); j++) {
                p += S1[j] * S2[j] * -1.0;
            }
            //将整条波形的相关运算值存入P数组中
            P[i - (maxIndex - densityMaxIcmDecay[rangeState])] = p;
        }
        max = P[0];
        int maxIndex1 = 0;
        for (i = 0; i < densityMaxIcmDecay[rangeState] * 2; i++) {
            if (P[i] > max) {
                max = P[i];
                maxIndex1 = i;
            }
        }
        maxIndex = maxIndex - densityMaxIcmDecay[rangeState] + maxIndex1 - w1;

        //GC20191231
        if(maxIndex <= 0) {
            maxIndex = maxBak;
        }
        faultResult = maxIndex;
        //GN 可以没有，定光标位置即可出现距离
        calculateDistanceAuto(maxIndex);
    }

    /**
     * 脉冲电流方式  计算故障距离(抽点做数据相关)  方向脉冲法自动计算-使用相关计算故障距离        //GC20191231
     */
    private void correlationSimple() {
        int i;
        int j = 0;
        int k;
        double p;
        double[] P = new double[510];
        int w1;
        int w2;
        int w3;
        double[] s1Simple = new double[510];
        double[] s2Simple = new double[510];
        int maxBak ;
        double distance;

        if (rangeState > 6) {
            //25M采样——32km、64km范围
            if (breakBk > (50 / 4)) {
                //相关窗左侧
                w1 = breakBk - (50 / 4);
            } else {
                w1 = breakBk;
            }
            //相关窗右侧
            w2 = breakBk + (350 / 4);
        } else {
            if (breakBk > 50) {
                //相关窗左侧
                w1 = breakBk - 50;
            } else {
                w1 = breakBk;
            }
            //相关窗右侧
            w2 = breakBk + 350;
        }

        //抽点
        for (i = 0; i < 510; i++) {
            s1Simple[i] = s1[j];
            s2Simple[i] = s2[j];
            j = j + densityMaxIcmDecay[rangeState];
        }
        w1 = w1 / densityMaxIcmDecay[rangeState];
        w2 = w2 / densityMaxIcmDecay[rangeState];
        w3 = 510 - w2;

        double[] S1 = new double[65556];
        double[] S2 = new double[65556];

        for (i = w1; i < w2; i++) {
            S1[i - w1] = s1Simple[i];
        }
        for (i = 0; i < w3; i++) {
            for (k = w1; k < w2; k++) {
                S2[k - w1] = s2Simple[k + i];
            }
            p = 0.0;
            //进行相关运算
            for (j = 0; j < (w2 - w1); j++) {
                p += S1[j] * S2[j] * -1.0;
            }
            //将整条波形的相关运算值存入P数组中
            P[i] = p;
        }

        //计算P数组中的最大值，并确定位置
        double max = P[0];
        int maxIndex = 0;
        for (i = 0; i < w3; i++) {
            if (P[i] > max) {
                max = P[i];
                maxIndex = i;
            }
        }

        //换算为整条波形数据中的点数
        maxIndex = (w1 + maxIndex) * densityMaxIcmDecay[rangeState];
        //GC20191231
        maxBak = maxIndex;

        //增加计算距离为0时的情况判断    找出所有的极值点
        if(maxIndex == 0) {
            double[] maxData = new double[65560];
            double[] maxData1 = new double[65560];
            int[] maxDataPos  = new int[65560];
            //补偿系数
            double a = 0.05;
            i = 3;
            j = 1;
            maxDataPos[0] = 0;
            maxData[0] = P[0];
            while ((j < 255) && (i <w3)) {
                if ((P[i] > P[i - 1]) && (P[i] >= P[i + 1])) {
                    if((i >= 3) && (P[i - 1] > P[i - 2])) {
                        if(P[i - 2] > P[i - 3]) {
                            maxDataPos[j] = i;
                            maxData[j] = P[i];
                            j++;
                        }
                    }
                }
                i++;
            }
            k = 0;
            for(i = 0;i < j;i++) {
                //找到幅值>0.3最大值的极值点
                if(maxData[i] > 0.3 * max) {
                    //max_data[i]是否换成max_data_pos[i]
                    distance = pointToDistance((int) maxData[i]);
                    //a待定 20190821
                    maxData1[k] = maxData[i] / ((double)1-((double)2 * a * (distance/(double)1000)));
                    maxDataPos[k] = maxDataPos[i];
                    k++;
                }
            }
            //排序算法
            sort(maxData1,maxDataPos,k);
            if (pointToDistance(maxDataPos[0]) >= 10) {
                //故障距离在10米外
                maxIndex = maxDataPos[0];
            } else if ((pointToDistance(maxDataPos[1]) < 10) && (maxData1[1] < maxData1[0] * 0.4))  {
                maxIndex = maxDataPos[0];
            } else if ((pointToDistance(maxDataPos[1]) >= 80) && (maxData1[1] >= maxData1[0] * 0.4)) {
                maxIndex = maxDataPos[1];
            } else if ((pointToDistance(maxDataPos[1]) >= 10) && (pointToDistance(maxDataPos[1]) >= 80)) {
                if (maxData1[2] >= maxData1[0] * 0.4) {
                    if(Math.abs(pointToDistance(maxDataPos[2]) - (double)2 * (pointToDistance(maxDataPos[1]) + (double)10)) < (double)10) {
                        maxIndex = maxDataPos[1];
                    }
                }
            } else if (pointToDistance(maxDataPos[2]) >= 80) {
                maxIndex = maxDataPos[2];
            } else if (((pointToDistance(maxDataPos[2]) >= 10) && (pointToDistance(maxDataPos[2]) >= 80))) {
                if(maxData1[2] >= maxData1[0] * 0.4) {
                    if(Math.abs(pointToDistance(maxDataPos[3]) - (double)2 * (pointToDistance(maxDataPos[2]) + (double)10)) < (double)10) {
                        maxIndex = maxDataPos[2];
                    }
                } else {
                    maxIndex = maxDataPos[0];
                }
            }
        }

        w1 = w1 * densityMaxIcmDecay[rangeState];
        w2 = w2 * densityMaxIcmDecay[rangeState];

        for (i = w1; i < w2; i++) {
            S1[i - w1] = s1[i];
        }

        for (i = (maxIndex - densityMaxIcmDecay[rangeState]); i < (maxIndex + densityMaxIcmDecay[rangeState]); i++) {
            for (k = 0; k < w2 - w1; k++) {
                S2[k] = s2[k + i];
            }
            //清零
            p = 0.0;
            //进行相关运算S
            for (j = 0; j < (w2 - w1); j++) {
                p += S1[j] * S2[j] * -1.0;
            }
            //将整条波形的相关运算值存入P数组中
            P[i - (maxIndex - densityMaxIcmDecay[rangeState])] = p;
        }
        max = P[0];
        int maxIndex1 = 0;
        for (i = 0; i < densityMaxIcmDecay[rangeState] * 2; i++) {
            if (P[i] > max) {
                max = P[i];
                maxIndex1 = i;
            }
        }
        maxIndex = maxIndex - densityMaxIcmDecay[rangeState] + maxIndex1 - w1;

        //GC20191231
        if(maxIndex <= 0) {
            maxIndex = maxBak;
        }
        faultResult = maxIndex;
        //GN 可以没有，定光标位置即可出现距离
        calculateDistanceAuto(maxIndex);
    }

    /**
     * @param samplingPoints
     */
    int pointToDistance(int samplingPoints) {
        int Fx1;
        int k = 1;
        //脉冲电流方式下range=6(32km)和range=7(64km)实时25M采样率，其余方式和范围实时100M采样率，此时相对其它方式采样周期扩大4倍
        if (rangeState > 6){
            k = 4;
        }
        //算出距离的整数部分
        Fx1 = (int) (((samplingPoints * velocity) * k) / 200);
        return(Fx1);

    }

    /**
     * @param samplingPoints 方向脉冲法自动计算-显示故障距离   //GC20191231 自动定光标已给出距离，可忽略
     */
    private void calculateDistanceAuto(int samplingPoints) {
        int k = 1;
        int l;
        int lFault;
        double distance;

        //脉冲电流方式下range=6(32km)和range=7(64km)实时25M采样率，其余方式和范围实时100M采样率，此时相对其它方式采样周期扩大4倍
        if (rangeState > 6){
            k = 4;
        }
        //有测试缆  //GC20200103
        if(leadLength > 0) {
            //实际点数
            l = (int) (leadLength * 2000 / leadVop / 10);
            lFault = samplingPoints - l;
            distance = (((double) lFault * velocity) * k) / 2 * 0.01 + leadLength;
        } else {
            distance = (((double) samplingPoints * velocity) * k) / 2 * 0.01;
        }
        //自动距离界面显示
//        tvAutoDistance.setText(new DecimalFormat("0.00").format(distance) + "m");
        //距离界面显示
//        tvDistance.setText(new DecimalFormat("0.00").format(distance) + "m");

    }

    /**
     * 排序——数组和数组长度  //GC20191231
     */
    private void sort(double[] a,int[] b,int l) {
        int i, j;
        double v;
        int k;
        //排序主体
        for(i = 0; i < l - 1; i ++) {
            for(j = i+1; j < l; j ++) {
                //如前面的比后面的小，则交换。
                if(a[i] < a[j]) {
                    v = a[i];
                    a[i] = a[j];
                    a[j] = v;
                    k = b[i];
                    b[i] = b[j];
                    a[j] = k;
                }
            }
        }
    }

    /**
     * 击穿点位置判断,确定光标起始位置
     */
    private void breakPointCalculate() {
        int i;
        int j;
        int k;
        int start;
        double min = 0;
        double[] Diff = new double[65560];
        int pos = 0;

        double p;
        double[] P = new double[65560];
        int w1;
        int w2;
        int w3;

        double[] D1 = new double[65560];
        double[] D2 = new double[65560];
        double[] maxData = new double[65560];
        int[] maxDataPos = new int[65560];

        start = 140;    //GC20200110  start = 0;
        for (i = 0; i < (dataMax - 50); i++) {
            Diff[i] =  (waveArrayFilter[i + 1] - waveArrayFilter[i]);
        }

        for (i = start + faultResult; i < (dataMax - 50); i++) {
            if ((Diff[i] < min) && (Diff[i] < 0)) {
                min = Diff[i];
                //pos位置减去了起始位置+故障距离
                pos = i - (start + faultResult);
            }
        }
        pos = pos + (start + faultResult);

        w1 = pos - 30;
        w2 = pos + 70;
        w3 = pos - (start + faultResult);
        for(i = w1; i < w2; i++) {
            D1[i - w1] = waveArrayFilter[i];
        }


        for (i = (start + faultResult); i < pos; i++) {
            for(k = i; k < (i + 100); k++) {
                D2[k - i] = waveArrayFilter[k];
            }
            p = 0.0;
            for(j = 0;j < (w2 - w1);j++) {
                //进行相关运算
                p += D1[j] * D2[j];
            }
            //将整条波形的相关运算值存入P数组中
            P[i - (start + faultResult)] = p;
        }

        //计算P数组中的最大值，并确定位置
        double max = P[0];
        int maxIndex = 0;
        for (i = 0;i < w3;i++) {
            if(P[i] > max) {
                max = P[i];
                maxIndex = i;
            }
        }
        breakBk = maxIndex;
        //找出所有的极值点，并找到>0.7倍最大值的极值点作为有效极值点
        i = 3;
        j = 0;
        while ((j < 255) && (i < w3)) {
            if((P[i] > P[i - 1]) && (P[i] >= P[i + 1])) {
                if((i >= 3) && (P[i - 1] > P[i - 2])) {
                    if(P[i - 2] > P[i - 3]) {
                        maxDataPos[j] = i;
                        maxData[j] = P[i];
                        j++;
                    }
                }
            }
            i++;
        }
        for (k = 0; k < j; k++) {
            if (maxData[k] > 0.7 * Math.abs(max)) {
                //有效极值点
                breakBk = maxDataPos[k];
                break;
            }
        }
        //实光标位置
        breakBk = breakBk + start + faultResult + 10;
    }

    /**
     * 脉冲电流方式光标自动定位 //GC20190708
     */
    private void icmAutoCursor() {
        //GC20200106
        zero = breakBk;
        pointDistance = breakBk + faultResult;
        //positionReal = zero / densityMax;
        //positionVirtual = pointDistance / densityMax;
        // sc 20200109   光标定位
        density = getDensity();
        Log.e("cursor", "位置" + density);
        if(density == densityMax){
            positionReal = zero / densityMax;
            positionVirtual = pointDistance / densityMax;
        }
        //重新定位实光标
        if (zero >= (currentMoverPosition510 * dataLength / 510) && zero <= ((currentMoverPosition510 * dataLength / 510) + (510*density))) {
            positionReal = (zero - (currentMoverPosition510 * dataLength / 510)) / density;
            fullWave.setScrubLineReal(positionReal);
        } else {
            fullWave.setScrubLineRealDisappear();
        }
        //重新定位虚光标
        if (pointDistance >= (currentMoverPosition510 * dataLength / 510) && pointDistance <= ((currentMoverPosition510 * dataLength / 510) + (510*density))) {
            positionVirtual = (pointDistance - (currentMoverPosition510 * dataLength / 510)) / density;
            fullWave.setScrubLineVirtual(positionVirtual);
        } else {
            fullWave.setScrubLineVirtualDisappear();
        }

        //光标定位
        //fullWave.setScrubLineReal(positionReal);
        //fullWave.setScrubLineVirtual(positionVirtual);
        //距离显示
        calculateDistance(Math.abs(pointDistance - zero));
    }

    /**
     * @param balance 需要发送的平衡控制命令值 / 响应信息栏平衡变化
     */
    public void setBalance(int balance) {
        this.balance = balance;
        command = COMMAND_BALANCE;
        dataTransfer = balance;
        tvBalanceValue.setText(String.valueOf(balance));
        //发送指令
        startService();
    }

    public int getBalance() {
        return balance;
    }


    //TODO 20101219输出命令和数据
    private String getCommandStr(int cmdStr) {
        String returnStr = "波速度";
        switch (cmdStr) {
            case 1:
                returnStr = "1 测试";
                break;
            case 2:
                returnStr = "2 模式";
                break;
            case 3:
                returnStr = "3 范围";
                break;
            case 4:
                returnStr = "4 增益";
                break;
            case 5:
                returnStr = "5 延时";
                break;
            case 7:
                returnStr = "7 平衡";
                break;
            case 9:
                returnStr = "9 接受数据";
                break;
            default:
                break;
        }
        return returnStr;
    }

    private String getDataTransfer(int cmdStr, int dataStr) {
        String returnStr = "0";
        if (cmdStr == 1) {
            switch (dataStr) {
                case 17:
                    returnStr = "测试 11";
                    break;
                case 34:
                    returnStr = "取消测试 22";
                    break;
            }
        }
        if (cmdStr == 2) {
            switch (dataStr) {
                case 17:
                    returnStr = "TDR 11";
                    break;
                case 34:
                    returnStr = "ICM 22";
                    break;
                case 85:
                    returnStr = "ICM_DECAY 55";
                    break;
                case 51:
                    returnStr = "SIM 33";
                    break;
                case 68:
                    returnStr = "DECAY 44";
                    break;
            }
        }
        if (cmdStr == 3) {
            switch (dataStr) {
                case 153:
                    returnStr = "范围 250";
                    break;
                case 17:
                    returnStr = "范围 500";
                    break;
                case 34:
                    returnStr = "范围 1KM";
                    break;
                case 51:
                    returnStr = "范围 2KM";
                    break;
                case 68:
                    returnStr = "范围 4KM";
                    break;
                case 85:
                    returnStr = "范围 8KM";
                    break;
                case 102:
                    returnStr = "范围 16KM";
                    break;
                case 119:
                    returnStr = "范围 32KM";
                    break;
                case 136:
                    returnStr = "范围 64KM";
                    break;
            }
        }

        if (cmdStr == 4) {
            returnStr = "增益 " + String.valueOf(dataStr);

        }
        if (cmdStr == 5) {
            returnStr = "延迟 " + String.valueOf(dataStr);

        }
        if (cmdStr == 7) {
            returnStr = "平衡 " + String.valueOf(dataStr);

        }
        if (cmdStr == 9) {
            returnStr = "开始接收数据 " + String.valueOf(dataStr);

        }
        return returnStr;
    }

    /**
     * 组织需要绘制的波形数组（抽点510个）——最终得到waveDraw和waveCompare    //GC20191219
     */
    private void organizeWaveData() {
        /*if ((mode == TDR) || (mode == SIM)) {
            dataLength = dataMax - removeTdrSim[rangeState];
        } else if ((mode == ICM) || (mode == DECAY) || (mode == ICM_DECAY)) {
            dataLength = dataMax - removeIcmDecay[rangeState];
        }
        //按最大比例抽出510个点
        for (int i = 0, j = 0; j < 510; i = i + densityMax, j++) {
            //组织TDR、ICM、ICM_DECAY、DECAY和SIM的第一条波形的数据
            waveDraw[j] = Constant.WaveData[i];
            //组织SIM的第二条波形的数据
            if (mode == SIM) {
                waveCompare[j] = Constant.SimData[i];
                if (!isDatabase) {
                    simDraw1[j] = simArray1[i];
                    simDraw2[j] = simArray2[i];
                    simDraw3[j] = simArray3[i];
                    simDraw4[j] = simArray4[i];
                    simDraw5[j] = simArray5[i];
                    simDraw6[j] = simArray6[i];
                    simDraw7[j] = simArray7[i];
                    simDraw8[j] = simArray8[i];
                }
            }
        }
        //移动滑块到指定位置
        setHorizontalMoveView();*/
        //按最大比例抽出510个点
        for (int i = 0, j = 0; j < 510; i = i + densityMax, j++) {
            //组织TDR、ICM、ICM_DECAY、DECAY和SIM的第一条波形的数据
            waveDraw[j] = Constant.WaveData[i];
            //组织SIM的第二条波形的数据
            if (mode == SIM) {
                waveCompare[j] = Constant.SimData[i];
                if (!isDatabase) {
                    simDraw1[j] = simArray1[i];
                    simDraw2[j] = simArray2[i];
                    simDraw3[j] = simArray3[i];
                    simDraw4[j] = simArray4[i];
                    simDraw5[j] = simArray5[i];
                    simDraw6[j] = simArray6[i];
                    simDraw7[j] = simArray7[i];
                    simDraw8[j] = simArray8[i];
                }
            }
        }

        int[] waveDraw250 = new int[255];
        int[] waveCompare250 = new int[255];
        int[] simDraw2501 = new int[255];
        int[] simDraw2502 = new int[255];
        int[] simDraw2503 = new int[255];
        int[] simDraw2504 = new int[255];
        int[] simDraw2505 = new int[255];
        int[] simDraw2506 = new int[255];
        int[] simDraw2507 = new int[255];
        int[] simDraw2508 = new int[255];

        //需要操作的数据长度
        if ((mode == TDR) || (mode == SIM)) {
            dataLength = dataMax - removeTdrSim[rangeState];
            //GC20191223
            if (range == RANGE_250) {
                //取出前255个点数
                for (int i = 0, j = 0; i < 255; i++, j++) {
                    waveDraw250[j] = waveDraw[i];
                    //组织SIM的第二条波形的数据
                    if (mode == SIM) {
                        waveCompare250[j] = waveCompare[i];
                        if (!isDatabase) {
                            simDraw2501[j] = simDraw1[i];
                            simDraw2502[j] = simDraw2[i];
                            simDraw2503[j] = simDraw3[i];
                            simDraw2504[j] = simDraw4[i];
                            simDraw2505[j] = simDraw5[i];
                            simDraw2506[j] = simDraw6[i];
                            simDraw2507[j] = simDraw7[i];
                            simDraw2508[j] = simDraw8[i];
                        }
                    }
                }
                //算出差值的255个点
                for (int i = 0, j = 1; i < 254; i++, j += 2) {
                    waveDraw[j] = waveDraw250[i] + (waveDraw250[i + 1] - waveDraw250[i]) / 2;
                    //组织SIM的第二条波形的数据
                    if (mode == SIM) {
                        waveCompare[j] = waveCompare250[i] + (waveCompare250[i + 1] - waveCompare250[i]) / 2;
                        if (!isDatabase) {
                            simDraw1[j] = simDraw2501[i] + (simDraw2501[i + 1] - simDraw2501[i]) / 2;
                            simDraw2[j] = simDraw2502[i] + (simDraw2502[i + 1] - simDraw2502[i]) / 2;
                            simDraw3[j] = simDraw2503[i] + (simDraw2503[i + 1] - simDraw2503[i]) / 2;
                            simDraw4[j] = simDraw2504[i] + (simDraw2504[i + 1] - simDraw2504[i]) / 2;
                            simDraw5[j] = simDraw2505[i] + (simDraw2505[i + 1] - simDraw2505[i]) / 2;
                            simDraw6[j] = simDraw2506[i] + (simDraw2506[i + 1] - simDraw2506[i]) / 2;
                            simDraw7[j] = simDraw2507[i] + (simDraw2507[i + 1] - simDraw2507[i]) / 2;
                            simDraw8[j] = simDraw2508[i] + (simDraw2508[i + 1] - simDraw2508[i]) / 2;
                        }
                    }
                }
                //将原始255个点分散
                for (int i = 0, j = 0; j < 255; i++, j++) {
                    waveDraw[2 * j] = waveDraw250[i];
                    //组织SIM的第二条波形的数据
                    if (mode == SIM) {
                        waveCompare[2 * j] = waveCompare250[i];
                        if (!isDatabase) {
                            simDraw1[2 * j] = simDraw2501[i];
                            simDraw2[2 * j] = simDraw2502[i];
                            simDraw3[2 * j] = simDraw2503[i];
                            simDraw4[2 * j] = simDraw2504[i];
                            simDraw5[2 * j] = simDraw2505[i];
                            simDraw6[2 * j] = simDraw2506[i];
                            simDraw7[2 * j] = simDraw2507[i];
                            simDraw8[2 * j] = simDraw2508[i];
                        }
                    }
                }
            }

        } else if ((mode == ICM) || (mode == DECAY) || (mode == ICM_DECAY)) {

            dataLength = dataMax - removeIcmDecay[rangeState];
            if (range == RANGE_250) {
                dataLength = dataLength / 2;
            }
        }

        //移动滑块到指定位置
        setHorizontalMoveView();
    }

    /**
     * 设置波形绘制参数
     */
    private void setWaveParameter() {
        //记录当前显示波形的参数   //GC20190716 避免切换范围、方式之后存储的波形参数与波形数据不对应
        Constant.ModeValue = mode;
        Constant.RangeValue = range;
        Constant.Gain = gain;
        Constant.Velocity = velocity;
        //GC20190716 规避放大缩小操作的bug
        Constant.DensityMax = densityMax;
        if (density > densityMax) {
            density = densityMax;
            tvZoomValue.setText("1 : " + density);
        }
        //非显示数据库波形状态
        isDatabase = false;
        //擦除比较波形
        isCom = false;
        if (mode == TDR) {
            //需要绘制的波形原始数组初始化
            dataMax = READ_TDR_SIM[rangeState];
            waveArray = new int[dataMax];
            Constant.WaveData = new int[dataMax];
        } else if ((mode == ICM) || (mode == ICM_DECAY) || (mode == DECAY)) {
            dataMax = READ_ICM_DECAY[rangeState];
            waveArray = new int[dataMax];
            Constant.WaveData = new int[dataMax];
        } else if (mode == SIM) {
            dataMax = READ_TDR_SIM[rangeState];
            waveArray = new int[dataMax];
            Constant.WaveData = new int[dataMax];
            Constant.SimData = new int[dataMax];
            //SIM第二条波形初始化
            simArray1 = new int[dataMax];
            simArray2 = new int[dataMax];
            simArray3 = new int[dataMax];
            simArray4 = new int[dataMax];
            simArray5 = new int[dataMax];
            simArray6 = new int[dataMax];
            simArray7 = new int[dataMax];
            simArray8 = new int[dataMax];
            //利用比较功能绘制SIM的第二条波形数据
            isCom = true;
        }

        //TODO 201912241202 MIM模式下不要重置零点，因为positionReal整除会丢失精度
        if (density == densityMax) {
            if (mode != SIM) {
                zero = positionReal * densityMax;
            }
            pointDistance = positionVirtual * densityMax;
        }

    }

    /**
     * 处理APP接收的波形数据
     */
    private void doWifiWave(int[] wifiArray) {
        if (wifiArray[3] == WAVE_TDR_ICM_DECAY) {
            System.arraycopy(wifiArray, 8, waveArray, 0, dataMax);
            ConnectService.canAskPower = true;
            Constant.WaveData = waveArray;
            //GC20190708
//            handler.sendEmptyMessage(VIEW_REFRESH);
            //GC20191231
            if ((mode == ICM) ){
                icmAutoTest();
            } else if((mode == ICM_DECAY)){
                icmAutoTestDC();    //GC20200109 增加DC方式下的自动测距
            } else{
                handler.sendEmptyMessage(VIEW_REFRESH);
            }
        } else if (wifiArray[3] == WAVE_SIM || wifiArray[3] == 0x88
                || wifiArray[3] == 0x99
                || wifiArray[3] == 0xAA
                || wifiArray[3] == 0xBB
                || wifiArray[3] == 0xCC
                || wifiArray[3] == 0xDD
                || wifiArray[3] == 0xEE
                || wifiArray[3] == 0xFF) {
            if (wifiArray[3] == 0x77) {
                System.arraycopy(wifiArray, 8, waveArray, 0, dataMax);
                Constant.WaveData = waveArray;
                Log.e("【MIM】", "第1条");
            }
            if (wifiArray[3] == 0x88) {
                System.arraycopy(wifiArray, 8, simArray1, 0, dataMax);
                Constant.TempData1 = simArray1;
                Constant.SimData = Constant.TempData1;
                Log.e("【MIM】", "第2条");
            }
            if (wifiArray[3] == 0x99) {
                System.arraycopy(wifiArray, 8, simArray2, 0, dataMax);
                Constant.TempData2 = simArray2;
                handler.sendEmptyMessage(ORGANIZE_WAVE);
                Log.e("【MIM】", "第3条");
            }
            if (wifiArray[3] == 0xAA) {
                System.arraycopy(wifiArray, 8, simArray3, 0, dataMax);
                Constant.TempData3 = simArray3;
                handler.sendEmptyMessage(ORGANIZE_WAVE);
                Log.e("【MIM】", "第4条");
            }
            if (wifiArray[3] == 0xBB) {
                System.arraycopy(wifiArray, 8, simArray4, 0, dataMax);
                Constant.TempData4 = simArray4;
                handler.sendEmptyMessage(ORGANIZE_WAVE);
                Log.e("【MIM】", "第5条");
            }
            if (wifiArray[3] == 0xCC) {
                System.arraycopy(wifiArray, 8, simArray5, 0, dataMax);
                Constant.TempData5 = simArray5;
                handler.sendEmptyMessage(ORGANIZE_WAVE);
                Log.e("【MIM】", "第6条");
            }
            if (wifiArray[3] == 0xDD) {
                System.arraycopy(wifiArray, 8, simArray6, 0, dataMax);
                Constant.TempData6 = simArray6;
                handler.sendEmptyMessage(ORGANIZE_WAVE);
                Log.e("【MIM】", "第7条");
            }
            if (wifiArray[3] == 0xEE) {
                System.arraycopy(wifiArray, 8, simArray7, 0, dataMax);
                Constant.TempData7 = simArray7;
                handler.sendEmptyMessage(ORGANIZE_WAVE);
                Log.e("【MIM】", "第8条");
            }
            if (wifiArray[3] == 0xFF) {
                System.arraycopy(wifiArray, 8, simArray8, 0, dataMax);
                Constant.TempData8 = simArray8;
                handler.sendEmptyMessage(ORGANIZE_WAVE);
                ConnectService.canAskPower = true;
                Log.e("【MIM】", "第9条");
            }

            if (wifiArray[3] == 0x88) {
                handler.sendEmptyMessage(VIEW_REFRESH);
            }
        }
        //记录当前显示波形的数据
    }

    /**
     * 根据参数重置显示效果
     */
    public void resetWhatNeed() {
        //二次脉冲显示组数重置
        if (mode == SIM) {
            selectSim = 1;
            setSelectSim(selectSim);
            //TODO 20200325 二次脉冲如果是从数据库显示，则禁用上翻下翻按键，测试后恢复。
            if(!isDatabase) {
                tvWaveNext.setEnabled(true);
            }
            else{
                tvWaveNext.setEnabled(false);
                tvWavePre.setEnabled(false);
            }
        }
    }

    /**
     * 设置数据库波形绘制参数  //GC20190713
     */
    public void setDateBaseParameter() {
        //显示数据库波形状态
        isDatabase = true;
        //读取并设置数据库的参数
        ///20191217屏蔽设置，在网络连接后设置
        //TODO 20191223-1129 模式页面网络正常，打开记录应下发
        setModeNoCmd(Constant.Para[0]);
        setRangeNoCmd(Constant.Para[1]);
        setGain(Constant.Para[2]);
        setVelocityNoCmd(Constant.Para[3]);
        Constant.ModeValue = Constant.Para[0];
        Constant.RangeValue = Constant.Para[1];
        Constant.Gain = Constant.Para[2];
        Constant.Velocity = Constant.Para[3];

        //TODO 2019-1222-2256 初始化虚光标、实光标、零点、比例等参数，否则缩放失效
        //读取零点和虚光标在原始数据中的位置，重新绘制光标
        zero = Constant.PositionR;
        pointDistance = Constant.PositonV;
        positionVirtual = pointDistance / densityMax;
        positionReal = zero / densityMax;
        fullWave.setScrubLineVirtual(positionVirtual);
        fullWave.setScrubLineReal(positionReal);

        //zero = positionReal * densityMax;
        Constant.DensityMax = densityMax;
        //pointDistance = positionVirtual * densityMax;

        //显示数据库中存储的位置
        if (Constant.CurrentUnit == Constant.MI_UNIT) {
            if (Constant.CurrentSaveUnit == Constant.MI_UNIT) {
                tvDistance.setText(new DecimalFormat("0.00").format(Constant.SaveLocation));
            } else {
                tvDistance.setText(UnitUtils.ftToMi(Constant.SaveLocation));
            }
        } else {
            if (Constant.CurrentSaveUnit == Constant.FT_UNIT) {
                tvDistance.setText(new DecimalFormat("0.00").format(Constant.SaveLocation));
            } else {
                tvDistance.setText(UnitUtils.miToFt(Constant.SaveLocation));
            }
        }

        //擦除比较波形
        isCom = false;
        //需要绘制的波形原始数组初始化
        if (mode == TDR) {
            dataMax = READ_TDR_SIM[rangeState];
        } else if ((mode == ICM) || (mode == ICM_DECAY) || (mode == DECAY)) {
            dataMax = READ_ICM_DECAY[rangeState];
        } else if (mode == SIM) {
            dataMax = READ_TDR_SIM[rangeState];
            //利用比较功能绘制SIM的第二条波形数据
            isCom = true;
        }
        initMode();
    }

    /**
     * @param mode 需要发送的方式控制命令值 / 响应信息栏方式变化
     */
    public void setMode(int mode) {
        this.mode = mode;
        command = COMMAND_MODE;
        dataTransfer = mode;
        switch (mode) {
            case TDR:
                tvMode.setText(getResources().getString(R.string.btn_tdr));
                //GC20190709
                switchDensity();
                initCursor();
                break;
            case ICM:
                tvMode.setText(getResources().getString(R.string.btn_icm));
                switchDensity();
                initCursor();
                break;
            case ICM_DECAY:
                tvMode.setText(getResources().getString(R.string.btn_icm_decay));
                //GC20190709
                switchDensity();
                initCursor();
                break;
            case SIM:
                tvMode.setText(getResources().getString(R.string.btn_sim));
                switchDensity();
                initCursor();
                break;
            case DECAY:
                tvMode.setText(getResources().getString(R.string.btn_decay));
                switchDensity();
                initCursor();
                break;
            default:
                break;
        }
        startService();
    }

    public void setModeNoCmd(int mode) {
        this.mode = mode;
        command = COMMAND_MODE;
        dataTransfer = mode;
        startService();
        switch (mode) {
            case TDR:
                tvMode.setText(getResources().getString(R.string.btn_tdr));
                switchDensity();
                //GC20190709
                break;
            case ICM:
                switchDensity();
                tvMode.setText(getResources().getString(R.string.btn_icm));

                break;
            case ICM_DECAY:
                switchDensity();
                tvMode.setText(getResources().getString(R.string.btn_icm_decay));
                break;
            case SIM:
                switchDensity();
                tvMode.setText(getResources().getString(R.string.btn_sim));
                break;
            case DECAY:
                switchDensity();
                tvMode.setText(getResources().getString(R.string.btn_decay));
                break;
            default:
                break;
        }
    }

    /**
     * 比例选择 //GC20190709
     */
    private void switchDensity() {
        if ((mode == TDR) || (mode == SIM)) {
            densityMax = densityMaxTdrSim[rangeState];
        } else if ((mode == ICM) || (mode == ICM_DECAY) || (mode == DECAY)) {
            densityMax = densityMaxIcmDecay[rangeState];
            //GC20191223
            if (range == RANGE_250) {
                densityMax = densityMax / 2;
            }
        }
        density = densityMax;
        tvZoomValue.setText("1 : " + density);
        density = getDensity();

        //TODO 默认显示滚动条 wdx 20191218
        tvZoomMin.setEnabled(true);
        llHorizontalView.setVisibility(View.VISIBLE);

        tvZoomPlus.setEnabled(true);
        tvZoomMin.setEnabled(true);
        //设置滑动块的宽度
        setHorizontalMoveViewOnlyHeight();
    }

    /**
     * 设置滑动块的宽度
     */
    private void setHorizontalMoveView() {
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(rlWave.getWidth() / density, 30);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mvWave.getParentWidth() * 510 * density / dataLength, getResources().getDimensionPixelSize(R.dimen.dp_20));
        mvWave.setLayoutParams(layoutParams);
    }

    private void setHorizontalMoveViewOnlyHeight() {
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(rlWave.getWidth() / density, 30);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mvWave.getWidth(), getResources().getDimensionPixelSize(R.dimen.dp_20));
        mvWave.setLayoutParams(layoutParams);
    }

    /**
     * 设置滑动块的宽度
     */
    private void setHorizontalMoveViewPosition(int position) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mvWave.getLayoutParams();
        layoutParams.leftMargin = fenzi2 - mvWave.getWidth() / 2;
        mvWave.setLayoutParams(layoutParams);
    }

    private void setMoverPostion(int position) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mvWave.getLayoutParams();
        layoutParams.leftMargin = position;
        mvWave.setLayoutParams(layoutParams);
    }

    /**
     * 光标位置和距离显示初始化 //GC20190709
     */
    private void initCursor() {
        int zero2 = 0;
        //光标距离
        if (mode == SIM) {
            //GC20190712
            zero = simZero;
            if (range == RANGE_250) {
                zero = zero * 2;
            }
            //GC20200330
            zero2 = simPosition;
            if (range == RANGE_250) {
                zero2 = simPosition * 2;
            }
        } else {
            zero = 0;
        }
        pointDistance = 255 * densityMax;
        //计算并在界面显示距离
        calculateDistance(Math.abs(pointDistance - zero));
        //界面定位
        positionReal = zero / densityMax;
        positionVirtual = pointDistance / densityMax;

        if (positionReal >= 0) {
            //G?? 条件是不是没用
            fullWave.setScrubLineReal(positionReal);
        }
        fullWave.setScrubLineVirtual(positionVirtual);
        //GC20200330
        if (mode == SIM) {
            positionSim = zero2 / densityMax;
            fullWave.setScrubLineSim(positionSim);
        } else {
            fullWave.setScrubLineSimDisappear();
        }
    }

    /**
     * @param range 需要发送的范围控制命令值 / 响应信息栏范围变化
     */
    public void setRange(int range) {
        alreadyDisplayWave = false;
        this.range = range;

        switch (range) {
            case RANGE_250:
                range = 0x99;
                rangeState = 0;
                //GC20190709
                switchDensity();
                initCursor();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_250m));
                } else if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_250m_to_ft));
                }
                gain = 13;
                //增益转为百分比   //GC20200313
                tvGainValue.setText("41");
                break;
            case RANGE_500:
                rangeState = 1;
                //GC20190709
                switchDensity();
                initCursor();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_500m));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_500m_to_ft));
                }
                gain = 13;
                tvGainValue.setText("41");
                break;
            case RANGE_1_KM:
                rangeState = 2;
                switchDensity();
                initCursor();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_1km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_1km_to_yingli));
                }
                gain = 13;
                tvGainValue.setText("41");
                break;
            case RANGE_2_KM:
                rangeState = 3;
                switchDensity();
                initCursor();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_2km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_2km_to_yingli));
                }
                gain = 10;
                tvGainValue.setText("32");
                break;
            case RANGE_4_KM:
                rangeState = 4;
                switchDensity();
                initCursor();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_4km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_4km_to_yingli));
                }
                gain = 10;
                tvGainValue.setText("32");
                break;
            case RANGE_8_KM:
                rangeState = 5;
                switchDensity();
                initCursor();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_8km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_8km_to_yingli));
                }
                gain = 10;
                tvGainValue.setText("32");
                break;
            case RANGE_16_KM:
                rangeState = 6;
                switchDensity();
                initCursor();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_16km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_16km_to_yingli));
                }
                gain = 9;
                tvGainValue.setText("29");
                break;
            case RANGE_32_KM:
                rangeState = 7;
                switchDensity();
                initCursor();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_32km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_32km_to_yingli));
                }
                gain = 9;
                tvGainValue.setText("29");
                break;
            case RANGE_64_KM:
                rangeState = 8;
                switchDensity();
                initCursor();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_64km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_64km_to_yingli));
                }
                gain = 9;
                tvGainValue.setText("29");
                break;
            default:
                break;
        }

        //发送指令
        command = COMMAND_RANGE;
        dataTransfer = range;
        startService();
    }

    public void setRangeNoCmd(int range) {
        this.range = range;
        switch (range) {
            case RANGE_250:
                rangeState = 0;
                switchDensity();
                //GC20190709
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_250m));
                } else if (Constant.CurrentUnit == Constant.FT_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_250m_to_ft));
                }
                break;
            case RANGE_500:
                rangeState = 1;
                switchDensity();
                //GC20190709
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_500m));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_500m_to_ft));
                }
                break;
            case RANGE_1_KM:
                rangeState = 2;
                switchDensity();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_1km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_1km_to_yingli));
                }
                break;
            case RANGE_2_KM:
                rangeState = 3;
                switchDensity();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_2km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_2km_to_yingli));
                }
                break;
            case RANGE_4_KM:
                rangeState = 4;
                switchDensity();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_4km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_4km_to_yingli));
                }
                break;
            case RANGE_8_KM:
                rangeState = 5;
                switchDensity();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_8km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_8km_to_yingli));
                }
                break;
            case RANGE_16_KM:
                rangeState = 6;
                switchDensity();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_16km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_16km_to_yingli));
                }
                break;
            case RANGE_32_KM:
                rangeState = 7;
                switchDensity();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_32km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_32km_to_yingli));
                }
                break;
            case RANGE_64_KM:
                rangeState = 8;
                switchDensity();
                if (Constant.CurrentUnit == Constant.MI_UNIT) {
                    tvRangeValue.setText(getResources().getString(R.string.btn_64km));
                } else {
                    tvRangeValue.setText(getResources().getString(R.string.btn_64km_to_yingli));
                }
                break;
            default:
                break;
        }
        command = COMMAND_RANGE;
        //TODO 选择250，发送500命令，显示距离250 wdx 20191218
        if (range == RANGE_250) {
            dataTransfer = 0x11;
        } else {
            dataTransfer = range;
        }
        startService();

    }

    /**
     * @param gain 需要发送的增益控制命令值 / 响应信息栏增益变化
     */
    public void setGain(int gain) {
        this.gain = gain;
        Constant.Gain = gain;
        command = COMMAND_GAIN;
        dataTransfer = gain;
        //增益转为百分比   //GC20200313
        int temp = s2b(gain);
        tvGainValue.setText(String.valueOf(temp));
        startService();
    }

    //增益转为百分比31转100    //GC20200313
    public int s2b(int s) {
        int b;
        float v = (float) s / 31.0f;
        float v1 = v * 100;
        b = (int) v1;
        return b;
    }

    public void setGainNoCmd(int gain) {
        this.gain = gain;
        command = COMMAND_GAIN;
        dataTransfer = gain;
        tvGainValue.setText(String.valueOf(gain));
    }


    public int getGain() {
        return gain;
    }

    /**
     * @param velocity 响应状态栏波速度变化
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
        if (Constant.CurrentUnit == Constant.MI_UNIT) {
            tvVopValue.setText(String.valueOf(velocity));
        } else {
            tvVopValue.setText(UnitUtils.miToFt(velocity));
        }

        //GC20190709    //G?
        if (!isReceiveData || isDatabase == true) {
        } else {
            calculateDistance(Math.abs(pointDistance - zero));
        }
    }

    public void setVelocityNoCmd(int velocity) {
        if (Constant.CurrentUnit == Constant.MI_UNIT) {
            tvVopValue.setText(String.valueOf(velocity));
        } else {
            tvVopValue.setText(UnitUtils.miToFt(velocity));
        }

    }


    @OnClick({R.id.tv_500m, R.id.tv_250m, R.id.tv_1km, R.id.tv_2km, R.id.tv_4km, R.id.tv_8km, R.id.tv_16km, R.id.tv_32km, R.id.tv_64km,
            R.id.tv_gain_add, R.id.tv_gain_min, R.id.layout_tv_both, R.id.layout_tv_memory, R.id.tv_cursor_plus, R.id.tv_balance_plus, R.id.tv_balance_min, R.id.tv_pulse_width, R.id.tv_cal, R.id.tv_range,
            R.id.tv_file, R.id.tv_home, R.id.tv_zero, R.id.tv_cursor_min, R.id.tv_zoom_plus, R.id.tv_zoom_min, R.id.tv_test, R.id.tv_help,
            R.id.tv_compare,
            R.id.ll_adjust,
            R.id.iv_pulse_width_close, R.id.tv_pulse_width_save,
            R.id.tv_vop_save, R.id.iv_compare_close, R.id.iv_cal_close, R.id.iv_range_close, R.id.iv_records_close,
            R.id.tv_vop_min, R.id.tv_vop_plus,
            R.id.tv_records_save, R.id.tv_file_records,
            R.id.tv_origin, R.id.tv_trigger_delay, R.id.tv_delay_plus, R.id.tv_delay_min, R.id.ll_trigger_delay, R.id.iv_close_trigger_delay,
            R.id.tv_wave_next, R.id.tv_wave_pre})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_adjust:
                break;
            case R.id.tv_wave_next:
                selectSim = getSelectSim();
                if (selectSim < 8) {
                    selectSim++;
                    setSelectSim(selectSim);
                    tvWavePre.setEnabled(true);
                }
                //到第8组波形，上翻按钮点击无效
                if (selectSim == 8) {
                    tvWaveNext.setEnabled(false);
                }
                break;
            case R.id.tv_wave_pre:
                //SIM共8组，从1-8
                selectSim = getSelectSim();
                if (selectSim > 1) {
                    selectSim--;
                    setSelectSim(selectSim);
                    tvWaveNext.setEnabled(true);
                }
                //到第1组波形，上翻按钮点击无效
                if (selectSim == 1) {
                    tvWavePre.setEnabled(false);
                    //20191217波形8时禁用下翻按钮，重新测试时重新生效。
                    tvWaveNext.setEnabled(true);

                }
                break;
            case R.id.iv_close_trigger_delay:
                closeTriggerDelayView();
                break;
            case R.id.tv_origin:
                //20191217 zero修改为实光标位置
                //int simZero = zero;
                int simZero = zero;
                if (mode == SIM && range == RANGE_250) {
                    simZero = simZero / 2;
                }
                setSimZero(simZero);
                break;
            case R.id.tv_trigger_delay:
                showTriggerDelayView();
                break;
            case R.id.tv_delay_plus:
                int delay = getDelay();
                if (delay < 1250) {
                    delay = delay + 5;
                    //GC20190704 延时发送命令修改   (延时从0到1250，点击一次增加5，共250阶)
                    setDelay(delay);
                    tvDelayMin.setEnabled(true);
                }
                if (delay == 1250) {
                    tvDelayPlus.setEnabled(false);
                }
                break;
            case R.id.tv_delay_min:
                delay = getDelay();
                if (delay > 0) {
                    delay = delay - 5;
                    setDelay(delay);
                    tvDelayPlus.setEnabled(true);
                }
                if (delay == 0) {
                    tvDelayMin.setEnabled(false);
                }
                break;
            case R.id.iv_compare_close:
                closeCompareView();
                break;
            case R.id.iv_cal_close:
                closeCalView();
                break;
            case R.id.iv_pulse_width_close:
                closePulseWidthView();
                break;
            case R.id.iv_range_close:
                closeRangeView();
                break;
            case R.id.iv_records_close:
                closeFileView();
                break;
            case R.id.tv_vop_save:
                saveVop();
                llCal.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_pulse_width_save:
                savePulseWidth();
                break;
            case R.id.tv_file_records:
                showRecordsDialog();
                break;
            case R.id.tv_records_save:
                showSaveDialog();
                break;
            case R.id.tv_250m:
                setRange(0x99);
                setGain(gain);
                //GC20200331
                if (!Constant.hasPulseWidth) {
                    handler.postDelayed(() -> {
                        setPulseWidth(40);
                    }, 20);
                    etPulseWidth.setText(String.valueOf(40));
                }
                handler.postDelayed(this::clickTest, 50);
                tvGainAdd.setEnabled(true);
                tvGainMin.setEnabled(true);
                break;
            case R.id.tv_500m:
                setRange(0x11);
                setGain(gain);
                //GC20200331
                if (!Constant.hasPulseWidth) {
                    handler.postDelayed(() -> {
                        setPulseWidth(40);
                    }, 20);
                    etPulseWidth.setText(String.valueOf(40));
                }
                handler.postDelayed(this::clickTest, 50);
                tvGainAdd.setEnabled(true);
                tvGainMin.setEnabled(true);
                break;
            case R.id.tv_1km:
                setRange(0x22);
                setGain(gain);
                //GC20200331
                if (!Constant.hasPulseWidth) {
                    handler.postDelayed(() -> {
                        setPulseWidth(80);
                    }, 20);
                    etPulseWidth.setText(String.valueOf(80));
                }
                handler.postDelayed(this::clickTest, 50);
                tvGainAdd.setEnabled(true);
                tvGainMin.setEnabled(true);
                break;
            case R.id.tv_2km:
                setRange(0x33);
                setGain(gain);
                //GC20200331
                if (!Constant.hasPulseWidth) {
                    handler.postDelayed(() -> {
                        setPulseWidth(160);
                    }, 20);
                    etPulseWidth.setText(String.valueOf(160));
                }
                handler.postDelayed(this::clickTest, 50);
                tvGainAdd.setEnabled(true);
                tvGainMin.setEnabled(true);
                break;
            case R.id.tv_4km:
                setRange(0x44);
                setGain(gain);
                //GC20200331
                if (!Constant.hasPulseWidth) {
                    handler.postDelayed(() -> {
                        setPulseWidth(320);
                    }, 20);
                    etPulseWidth.setText(String.valueOf(320));
                }
                handler.postDelayed(this::clickTest, 50);
                tvGainAdd.setEnabled(true);
                tvGainMin.setEnabled(true);
                break;
            case R.id.tv_8km:
                setRange(0x55);
                setGain(gain);
                //GC20200331
                if (!Constant.hasPulseWidth) {
                    handler.postDelayed(() -> {
                        setPulseWidth(640);
                    }, 20);
                    etPulseWidth.setText(String.valueOf(640));
                }
                handler.postDelayed(this::clickTest, 50);
                tvGainAdd.setEnabled(true);
                tvGainMin.setEnabled(true);
                break;
            case R.id.tv_16km:
                setRange(0x66);
                setGain(gain);
                //GC20200331
                if (!Constant.hasPulseWidth) {
                    handler.postDelayed(() -> {
                        setPulseWidth(1280);
                    }, 20);
                    etPulseWidth.setText(String.valueOf(1280));
                }
                handler.postDelayed(this::clickTest, 50);
                tvGainAdd.setEnabled(true);
                tvGainMin.setEnabled(true);
                break;
            case R.id.tv_32km:
                setRange(0x77);
                setGain(gain);
                //GC20200331
                if (!Constant.hasPulseWidth) {
                    handler.postDelayed(() -> {
                        setPulseWidth(2560);
                    }, 20);
                    etPulseWidth.setText(String.valueOf(2560));
                }
                handler.postDelayed(this::clickTest, 50);
                tvGainAdd.setEnabled(true);
                tvGainMin.setEnabled(true);
                break;
            case R.id.tv_64km:
                setRange(0x88);
                setGain(gain);
                //GC20200331
                if (!Constant.hasPulseWidth) {
                    handler.postDelayed(() -> {
                        setPulseWidth(5120);
                    }, 20);
                    etPulseWidth.setText(String.valueOf(5120));
                }
                handler.postDelayed(this::clickTest, 50);
                tvGainAdd.setEnabled(true);
                tvGainMin.setEnabled(true);

                break;
            case R.id.tv_gain_add:
                int gain = getGain();
                if (gain < 31) {
                    gain++;
                    //GC20190704 增益发送命令修改   (命令范围0-31阶)
                    setGain(gain);
                    tvGainMin.setEnabled(true);
                }
                //增益命令到最大，按钮点击无效
                if (gain == 31) {
                    tvGainAdd.setEnabled(false);
                }
                closeAllView();
                break;
            case R.id.tv_gain_min:
                gain = getGain();
                if (gain > 0) {
                    gain--;
                    setGain(gain);
                    tvGainAdd.setEnabled(true);
                }
                if (gain == 0) {
                    tvGainMin.setEnabled(false);
                }
                closeAllView();
                break;
            case R.id.tv_balance_plus:
                int balance = getBalance();
                if (balance < 15) {
                    balance++;
                    //GC20190704 平衡发送命令修改   (命令范围0-15阶)
                    setBalance(balance);
                    tvBalanceMin.setEnabled(true);
                }

                if (balance == 15) {
                    tvBalancePlus.setEnabled(false);
                }
                closeAllView();
                break;
            case R.id.tv_balance_min:
                balance = getBalance();
                if (balance > 0) {
                    balance--;
                    setBalance(balance);
                    tvBalancePlus.setEnabled(true);
                }

                if (balance == 0) {
                    tvBalanceMin.setEnabled(false);

                }
                closeAllView();
                break;
            case R.id.tv_vop_min:
                velocity = getVelocity();
                if (velocity > 90) {
                    velocity--;
                    setVelocity(velocity);
                    saveVop();
                }
                break;
            case R.id.tv_vop_plus:
                int velocity = (int) getVelocity();
                if (velocity < 300) {
                    velocity++;
                    setVelocity(velocity);
                    saveVop();
                }
                break;
            case R.id.tv_pulse_width:
                showPulseWidth();
                break;
            case R.id.layout_tv_memory:
                clickMemory();
                break;
            case R.id.layout_tv_both:
                clickCompare();
                break;
            case R.id.tv_compare:
                showCompareView();
                break;
            case R.id.tv_cal:
                showCalView();
                break;
            case R.id.tv_range:
                showRangeView();
                break;
            case R.id.tv_file:
                showFileView();
                break;
            case R.id.tv_home:
                finish();
                break;
            case R.id.tv_zero:
                closeAllView();
                //20191217重新计算距离
                fullWave.setScrubLineReal(positionVirtual);
                positionReal = positionVirtual;
                //在原始数据中的位置 //GC20191218
                zero = pointDistance;
                calculateDistance(0);
                break;
            case R.id.tv_cursor_min:
                closeAllView();
                if (positionVirtual > 0) {
                    int positionVirtualtemp = positionVirtual;
                    positionVirtualtemp -= 1;
                    fullWave.setScrubLineVirtual(positionVirtualtemp);
                    pointDistance = pointDistance + ((int) positionVirtualtemp - positionVirtual) * density;
                    positionVirtual = positionVirtualtemp;
                    Log.e("【按钮调光标】", "positionVirtual" + positionVirtual);
                    if (positionVirtual == 0) {
                        pointDistance = 0;
                    }
                    calculateDistance(Math.abs(pointDistance - zero));
                }
                break;
            case R.id.tv_cursor_plus:
                closeAllView();
                if (positionVirtual < 509) {
                    int positionVirtualtemp = positionVirtual;
                    positionVirtualtemp += 1;
                    fullWave.setScrubLineVirtual(positionVirtualtemp);
                    pointDistance = pointDistance + ((int) positionVirtualtemp - positionVirtual) * density;
                    positionVirtual = positionVirtualtemp;
                    calculateDistance(Math.abs(pointDistance - zero));

                }
                break;
            case R.id.tv_zoom_plus:
                closeAllView();
                //GC20190711
                int density = getDensity();
                if (density > 1) {
                    density = density / 2;
                    setDensity(density);
                    tvZoomMin.setEnabled(true);
                }
                //无法放大
                if (density == 1) {
                    tvZoomPlus.setEnabled(false);
                }
                break;
            case R.id.tv_zoom_min:
                closeAllView();
                density = getDensity();
                if (density < Constant.DensityMax) {
                    density = density * 2;
                    setDensity(density);
                    tvZoomPlus.setEnabled(true);
                }
                if (density == Constant.DensityMax) {
                    tvZoomMin.setEnabled(false);
                }
                break;
            case R.id.tv_test:
                isReceiveData = true;
                clickTest();
                break;
            case R.id.tv_help:
                closeAllView();
                //GC20200327
                showHelpModeDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 测试界面帮助对话框    //GC20200327
     */
    private void showHelpModeDialog() {
        HelpModeDialog helpModeDialog = new HelpModeDialog(this);
        Constant.ModeValue = mode;
        if (!helpModeDialog.isShowing()) {
            helpModeDialog.show();
        }
    }

    public int getSelectSim() {
        return selectSim;
    }

    /**
     * @param selectSim SIM显示波形的组数
     */
    public void setSelectSim(int selectSim) {
        tvWaveText.setVisibility(View.VISIBLE);
        this.selectSim = selectSim;
        switch (selectSim) {
            case 1:
                System.arraycopy(simDraw1, 0, waveCompare, 0, 510);
                System.arraycopy(simDraw1Full, 0, waveCompareFull, 0, 510);
                Constant.SimData = Constant.TempData1;
                tvWaveValue.setText(R.string.wave_one);
                break;
            case 2:
                System.arraycopy(simDraw2, 0, waveCompare, 0, 510);
                System.arraycopy(simDraw2Full, 0, waveCompareFull, 0, 510);
                Constant.SimData = Constant.TempData2;
                tvWaveValue.setText(R.string.wave_two);
                break;
            case 3:
                System.arraycopy(simDraw3, 0, waveCompare, 0, 510);
                System.arraycopy(simDraw3Full, 0, waveCompareFull, 0, 510);
                Constant.SimData = Constant.TempData3;
                tvWaveValue.setText(R.string.wave_three);
                break;
            case 4:
                System.arraycopy(simDraw4, 0, waveCompare, 0, 510);
                System.arraycopy(simDraw4Full, 0, waveCompareFull, 0, 510);
                Constant.SimData = Constant.TempData4;
                tvWaveValue.setText(R.string.wave_four);
                break;
            case 5:
                System.arraycopy(simDraw5, 0, waveCompare, 0, 510);
                System.arraycopy(simDraw5Full, 0, waveCompareFull, 0, 510);
                Constant.SimData = Constant.TempData5;
                tvWaveValue.setText(R.string.wave_five);
                break;
            case 6:
                System.arraycopy(simDraw6, 0, waveCompare, 0, 510);
                System.arraycopy(simDraw6Full, 0, waveCompareFull, 0, 510);
                Constant.SimData = Constant.TempData6;
                tvWaveValue.setText(R.string.wave_six);
                break;
            case 7:
                System.arraycopy(simDraw7, 0, waveCompare, 0, 510);
                System.arraycopy(simDraw7Full, 0, waveCompareFull, 0, 510);
                Constant.SimData = Constant.TempData7;
                tvWaveValue.setText(R.string.wave_seven);
                break;
            case 8:
                System.arraycopy(simDraw8, 0, waveCompare, 0, 510);
                System.arraycopy(simDraw8Full, 0, waveCompareFull, 0, 510);
                Constant.SimData = Constant.TempData8;
                tvWaveValue.setText(R.string.wave_eight);
                break;
            default:
                break;
        }
        displayWave();
    }

    /**
     * @param simZero 光标零点设置    //GC20190712
     */
    public void setSimZero(int simZero) {
        this.simZero = simZero;
        StateUtils.setInt(ModeActivity.this, AppConfig.CURRENT_CURSOR_POSITION, simZero);
        Toast.makeText(this, getResources().getString(R.string
                .cursor_zero_set_success), Toast.LENGTH_SHORT).show();

    }

    private void saveVop() {
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(ModeActivity.this, Constant.PARAM_INFO_KEY);
        if (paramInfo != null) {
            if (Constant.CurrentSaveUnit == MI_UNIT) {
                paramInfo.setCableVop(String.valueOf(velocity));
            } else {
                paramInfo.setCableVop(UnitUtils.miToFt(Double.valueOf(String.valueOf(velocity))));
            }
        } else {
            paramInfo = new ParamInfo();
            if (Constant.CurrentSaveUnit == MI_UNIT) {
                paramInfo.setCableVop(String.valueOf(velocity));
            } else {
                paramInfo.setCableVop(UnitUtils.miToFt(Double.valueOf(String.valueOf(velocity))));
            }
        }
        Constant.Velocity = velocity;
        StateUtils.setObject(ModeActivity.this, paramInfo, Constant.PARAM_INFO_KEY);

    }

    /**
     * 该方法检测一个点击事件是否落入在一个View内，换句话说，检测这个点击事件是否发生在该View上。
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean touchEventInView(View view, float x, float y) {
        if (view == null) {
            return false;
        }

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int left = location[0];
        int top = location[1];

        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();

        if (y >= top && y <= bottom && x >= left && x <= right) {
            return true;
        }

        return false;
    }


    private void showFileView() {
        llRecords.setVisibility(View.VISIBLE);
        llRecords.setOnClickListener(null);
        closeCompareView();
        closeRangeView();
        closeCalView();
        closeTriggerDelayView();
        closePulseWidthView();
    }

    private void closeFileView() {
        llAdjust.setVisibility(View.VISIBLE);
        llRecords.setVisibility(View.GONE);
    }

    private void showRangeView() {
        llRange.setVisibility(View.VISIBLE);
        llRange.setOnClickListener(null);
        closeFileView();
        closeCompareView();
        closeCalView();
        closeTriggerDelayView();
        closePulseWidthView();
    }

    private void closeRangeView() {
        llAdjust.setVisibility(View.VISIBLE);
        llRange.setVisibility(View.GONE);
    }

    private void showTriggerDelayView() {
        llTriggerDelay.setVisibility(View.VISIBLE);
        closeFileView();
        closeRangeView();
        closeCalView();
        closeCompareView();
        closePulseWidthView();
    }

    private void closeTriggerDelayView() {
        llAdjust.setVisibility(View.VISIBLE);
        llTriggerDelay.setVisibility(View.GONE);
    }

    private void showCalView() {
        llCal.setVisibility(View.VISIBLE);
        llCal.setOnClickListener(null);
        closeFileView();
        closeRangeView();
        closeCompareView();
        closeTriggerDelayView();
        closePulseWidthView();
    }

    private void closeCalView() {
        llAdjust.setVisibility(View.VISIBLE);
        llCal.setVisibility(View.GONE);
    }


    private void closePulseWidthView() {
        llAdjust.setVisibility(View.VISIBLE);
        llPulseWidth.setVisibility(View.GONE);
    }


    private void closeCompareView() {
        llAdjust.setVisibility(View.VISIBLE);
        llCompare.setVisibility(View.GONE);
    }

    private void showCompareView() {
        llCompare.setVisibility(View.VISIBLE);
        llCompare.setOnClickListener(null);
        closeFileView();
        closeRangeView();
        closeCalView();
        closeTriggerDelayView();
        closePulseWidthView();
    }

    private void closeAllView() {
        closeCalView();
        closeCompareView();
        closeRangeView();
        closeTriggerDelayView();
        closeFileView();
        closePulseWidthView();
    }

    /**
     * 设置波宽度，存储本地保存，wdx 20191218
     */
    private void showPulseWidth() {
        llPulseWidth.setOnClickListener(null);
        llPulseWidth.setVisibility(View.VISIBLE);
        closeFileView();
        closeRangeView();
        closeCompareView();
        closeCalView();
        closeTriggerDelayView();
    }

    /**
     * 界面操作存储本地保存，wdx 20191218
     */
    private void savePulseWidth() {
        // 01 初始化操作值
        if (etPulseWidth.getText().toString().isEmpty()) {
            pulseWidth = 0;
            //GC20200331
            Constant.hasPulseWidth = false;
        } else {
            pulseWidth = Integer.valueOf(etPulseWidth.getText().toString());
            //GC20200331
            Constant.hasPulseWidth = true;
        }
        // 02 本地保存波宽度信息
        savePulseWidthInfo();
        // 03 指令下达
        setPulseWidth(pulseWidth);
        closePulseWidthView();
    }

    /**
     * 存储本地保存，wdx 20191218
     */
    private void savePulseWidthInfo() {
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(ModeActivity.this, Constant.PULSE_WIDTH_INFO_KEY);
        if (paramInfo != null) {
            paramInfo.setPulseWidth(pulseWidth);
        } else {
            paramInfo = new ParamInfo();
            paramInfo.setPulseWidth(pulseWidth);
        }
        StateUtils.setObject(ModeActivity.this, paramInfo, Constant.PULSE_WIDTH_INFO_KEY);

    }

    /**
     * 波宽度指令下发，wdx 20191218
     */
    private void setPulseWidth(int pulseWidth) {
        command = COMMAND_PULSE_WIDTH;
        dataTransfer = calPulseWidth(pulseWidth);
        startService();
    }

    /**
     * 计算波宽度数值，计算公式为255-X/40;X为输入值
     */
    private int calPulseWidth(int pulseWidth) {

        if (pulseWidth < 0 || pulseWidth > 7000) {
            return 0;
        }
        pulseWidth = 255 - pulseWidth / 40;
        return pulseWidth;
    }

    /**
     * 弹出波形存储对话框
     */
    private void showSaveDialog() {
        closeFileView();
        SaveRecordsDialog saveRecordsDialog = new SaveRecordsDialog(this);
        Constant.ModeValue = mode;
        //TODO 20191226 存储zero和pointDistance
        saveRecordsDialog.setPositionReal(zero);
        saveRecordsDialog.setPositionVirtual(pointDistance);
        if (!saveRecordsDialog.isShowing()) {
            saveRecordsDialog.show();
        }
    }

    /**
     * 弹出波形记录对话框
     */
    private void showRecordsDialog() {
        closeFileView();
        ShowRecordsDialog showRecordsDialog = new ShowRecordsDialog(this);
        showRecordsDialog.setMode(mode);
        if (!showRecordsDialog.isShowing()) {
            showRecordsDialog.show();
        }

    }

    /**
     * 测试按钮
     */
    private void clickTest() {
        Constant.SaveToDBGain = Constant.Gain;
        closeAllView();
        llRange.setVisibility(View.INVISIBLE);
        //初始化距离
        if (mode == TDR) {
            tDialog = new TDialog.Builder(getSupportFragmentManager())
                    .setLayoutRes(R.layout.receiving_data)
                    .setScreenWidthAspect(this, 0.25f)
                    .setCancelableOutside(false)
                    .create()
                    .show();
            Log.e("DIA", " 正在接受数据显示" + " TDR");
            command = COMMAND_TEST;
            dataTransfer = TESTING;
            startService();
            //Log.e("【时效测试】", "命令发送测试命令");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    command = COMMAND_RECEIVE_WAVE;
                    dataTransfer = 0x11;
                    startService();
                    //Log.e("【时效测试】", "发送接收波形数据命令");

                }

            }, 20);

        } else if ((mode == ICM) || (mode == ICM_DECAY) || (mode == SIM) || (mode == DECAY)) {
            tDialog = new TDialog.Builder(getSupportFragmentManager())
                    .setLayoutRes(R.layout.wait_trigger)
                    .setScreenWidthAspect(this, 0.3f)
                    .setCancelableOutside(false)
                    .addOnClickListener(R.id.tv_cancel)
                    .setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {

                            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                                Toast.makeText(ModeActivity.this, R.string.ask_cancel, Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    })
                    .setOnViewClickListener(new OnViewClickListener() {
                        @Override
                        public void onViewClick(BindViewHolder viewHolder, View view,
                                                TDialog tDialog) {

                            if (!alreadyDisplayWave) {
                                tvZoomMin.setEnabled(false);
                                tvZoomPlus.setEnabled(false);
                                tvWaveNext.setEnabled(false);
                                tvWavePre.setEnabled(false);
                            }
                            //设置取消测试标志位true
                            Constant.isCancelAim = true;
                            tDialog.dismiss();
                            command = COMMAND_TEST;
                            dataTransfer = CANCEL_TEST;
                            ConnectService.canAskPower = true;
                            startService();
                        }
                    })
                    .create()
                    .show();
            Log.e("DIA", " 等待触发显示");
            command = COMMAND_TEST;
            dataTransfer = TESTING;
            startService();
            ConnectService.canAskPower = false;  //GC20200317
            //Log.e("【时效测试】", "命令发送测试命令");
            Constant.isCancelAim = false;
        }
    }

    /**
     * 点击比较按钮执行的方法  //GC20190703
     */
    public void clickCompare() {
        closeCompareView();
        if (isMemory) {
            //GC20190703再优化
            if ((modeBefore == mode) && (rangeBefore == range)) {
                isCom = !isCom;
                myChartAdapterMainWave.setmTempArray(waveDraw);
                myChartAdapterMainWave.setShowCompareLine(isCom);
                //myChartAdapterMainWave.setmCompareArray(waveCompare);
                organizeCompareWaveData(currentStart);
                myChartAdapterMainWave.setmCompareArray(waveCompareDraw);
                myChartAdapterMainWave.notifyDataSetChanged();
            } else {
                Toast.makeText(this, getResources().getString(R.string
                        .You_can_not_compare), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string
                    .You_have_no_memory_data_can_not_compare), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 点击记忆按钮执行的方法  //GC20190703
     */
    public void clickMemory() {
        closeCompareView();
        isMemory = true;
        //TODO 20191224 修改记忆波形数据结构
        //System.arraycopy(waveDraw, 0, waveCompare, 0, 510);
        waveCompare = new int[Constant.WaveData.length];
        System.arraycopy(Constant.WaveData, 0, waveCompare, 0, Constant.WaveData.length);
        //记录记忆数据的方式范围   //GC20190703再优化
        modeBefore = mode;
        rangeBefore = range;
    }

    /**
     *去服务发送指令
     */
    public void startService() {
        Intent intent = new Intent(ModeActivity.this, ConnectService.class);
        //发送指令
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_COMMAND_KEY, command);
        bundle.putInt(BUNDLE_MODE_KEY, mode);
        bundle.putInt(BUNDLE_DATA_TRANSFER_KEY, dataTransfer);
        intent.putExtra(BUNDLE_PARAM_KEY, bundle);
        startService(intent);
    }

    public boolean getReceiveData() {
        return isReceiveData;
    }

    public void setReceiveData(boolean receiveData) {
        isReceiveData = receiveData;
    }

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
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }

}
