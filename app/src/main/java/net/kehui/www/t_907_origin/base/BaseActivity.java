package net.kehui.www.t_907_origin.base;

import net.kehui.www.t_907_origin.adpter.DataAdapter;
import net.kehui.www.t_907_origin.adpter.MyChartAdapterBase;
import net.kehui.www.t_907_origin.dao.DataDao;
import net.kehui.www.t_907_origin.global.BaseAppData;
import net.kehui.www.t_907_origin.thread.ConnectThread;
import net.kehui.www.t_907_origin.util.MultiLanguageUtil;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.WindowManager;

import java.io.BufferedReader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

/**
 * @author Gong
 * @date 2019/07/15
 */

public class BaseActivity extends AppCompatActivity {

    /**
     * sparkView图形绘制部分
     */
    public MyChartAdapterBase myChartAdapterMainWave;
    public MyChartAdapterBase myChartAdapterFullWave;

    /**
     * 波形参数
     */
    public int mode;
    public int modeBefore;
    public int range;
    public int rangeBefore;
    public int rangeState;
    public int gain;
    public double velocity;
    public int density;
    public int densityMax;
    public int balance;
    public int delay;
    public int inductor;
    public int selectSim;
    public boolean isCom;
    public boolean isMemory;
    public boolean isDatabase;

    /**
     * wdx 20191218 波宽度全局变量
     */
    public int pulseWidth;
    public boolean hasPulseWidth;

    /**
     * 波形原始数据数组
     */
    public int dataMax;
    public int dataLength;
    public int[] waveArray;
    public int[] simArray1;
    public int[] simArray2;
    public int[] simArray3;
    public int[] simArray4;
    public int[] simArray5;
    public int[] simArray6;
    public int[] simArray7;
    public int[] simArray8;

    /**
     * 波形绘制数据数组（抽点510个）
     */
    public int[] waveDraw;
    public int[] waveCompare;
    public int[] waveCompareDraw;
    public int[] waveDrawFull;
    public int[] waveCompareFull;
    public int[] simDraw1;
    public int[] simDraw2;
    public int[] simDraw3;
    public int[] simDraw4;
    public int[] simDraw5;
    public int[] simDraw6;
    public int[] simDraw7;
    public int[] simDraw8;
    public int[] simDraw1Full;
    public int[] simDraw2Full;
    public int[] simDraw3Full;
    public int[] simDraw4Full;
    public int[] simDraw5Full;
    public int[] simDraw6Full;
    public int[] simDraw7Full;
    public int[] simDraw8Full;

    /**
     * 不同范围和方式下，波形数据的点数、需要去掉的冗余点数、比例值
     */
    public final static int[] READ_TDR_SIM = {540, 540, 1052, 2076, 4124, 8220, 16412, 32796, 65556};
    public final static int[] READ_ICM_DECAY = {2068, 2068, 4116, 8212, 16404, 32788, 65556, 32788, 65556};
    public int[] removeTdrSim = {30, 30, 32, 36, 44, 60, 92, 156, 276};
    public int[] removeIcmDecay = {28, 28, 36, 52, 84, 148, 276, 148, 276};
    public int[] densityMaxTdrSim = {1, 1, 2, 4, 8, 16, 32, 64, 128};
    public int[] densityMaxIcmDecay = {4, 4, 8, 16, 32, 64, 128, 64, 128};

    /**
     * 光标参数
     */
    public int zero;
    public int pointDistance;
    public int simZero;
    public int positionReal;
    public int positionVirtual;
    public int cursorMoveValue;
    public boolean cursorState;
    public int simPosition;
    public int positionSim;

    /**
     * ICM自动测距参数
     */
    public int gainState;
    public int breakdownPosition;
    public int breakBk;
    public int faultResult;
    public double[] waveArrayFilter = new double[65560];
    public double[] waveArrayIntegral = new double[65560];
    public double[] s1 = new double[65560];
    public double[] s2 = new double[65560];

    /**
     * ICM自动测距参数    //GC20191231
     */
    public boolean breakDown;

    /**
     * 测试缆信息添加    //GC20200103
     */
    public double leadLength;
    public double leadVop;

    /**
     * WiFi连接部分
     */
    public ConnectThread connectThread;
    public BufferedReader br;
    public static final int PORT = 9000;
    public boolean isSuccessful;
    public boolean netBoolean;
    public boolean isFirst = true;
    public int[] wifiStream;

    /**
     * 接收波形
     * 数据头      数据长度    传输数据    校验和
     * eb90aaxx    aabbccdd       X         xx
     *
     * 发送命令(16进制显示)
     * 数据头   数据长度  指令  传输数据  校验和
     * eb90aa55     03      01      11       15
     * eb90aa55 03 01 11 15	    测试0x11
     * eb90aa55 03 01 22 26	    取消测试0x22
     * eb90aa55 03 02 11 16		TDR低压脉冲方式
     * eb90aa55 03 02 22 27		ICM脉冲电流方式
     * eb90aa55 03 02 33 38		SIM二次脉冲方式
     * eb90aa55 03 03 11 17		范围500m
     * eb90aa55 03 03 22 28
     * eb90aa55 03 03 33 39
     * eb90aa55 03 03 44 4a
     * eb90aa55 03 03 55 5b
     * eb90aa55 03 03 66 6c
     * eb90aa55 03 03 77 7d
     * eb90aa55 03 03 88 8e		范围64km
     * eb90aa55 03 04 11 18		增益+
     * eb90aa55 03 04 22 29		增益-
     * eb90aa55 03 05 11 19		延时+
     * eb90aa55 03 05 22 2a		延时-
     * eb90aa55 03 07 11 1b  	平衡+
     * eb90aa55 03 07 22 2c		平衡-
     * eb90aa55 03 08 11 1c		//G后续添加 接收到触发信号
     * eb90aa55 03 09 11 1d		//G后续添加 接收数据命令
     * eb90aa55 03 0a 11 1e		//G后续添加 关机重连
     */
    public int command;
    public final static int COMMAND_TEST = 0x01;
    public final static int COMMAND_MODE = 0x02;
    public final static int COMMAND_RANGE = 0x03;
    public final static int COMMAND_GAIN = 0x04;
    public final static int COMMAND_DELAY = 0x05;
    public final static int COMMAND_BALANCE = 0x07;
    public final static int COMMAND_RECEIVE_WAVE = 0x09;

    public int dataTransfer;
    public final static int TESTING = 0x11;
    public final static int CANCEL_TEST = 0x22;

    /**
     * wdx 20191218 波宽度发送指令代码
     */
    public final static int COMMAND_PULSE_WIDTH = 0x0a;

    public final static int TDR = 0x11;
    public final static int ICM = 0x22;
    public final static int ICM_DECAY = 0x55;
    public final static int SIM = 0x33;
    public final static int DECAY = 0x44;

    public final static int RANGE_250 = 0x99;
    public final static int RANGE_500 = 0x11;
    public final static int RANGE_1_KM = 0x22;
    public final static int RANGE_2_KM = 0x33;
    public final static int RANGE_4_KM = 0x44;
    public final static int RANGE_8_KM = 0x55;
    public final static int RANGE_16_KM = 0x66;
    public final static int RANGE_32_KM = 0x77;
    public final static int RANGE_64_KM = 0x88;

    /**
     * APP接收到的命令
     */
    public final static int COMMAND_TRIGGER = 0x08;
    public final static int TRIGGERED = 0x11;
    public final static int POWER_DISPLAY = 0x06;
    public final static int RECEIVE_RIGHT = 0x33;
    public final static int RECEIVE_WRONG = 0x44;

    /**
     * APP接收到的波形数据头
     * 数据头      数据长度    传输数据    校验和
     * eb90aaXX    aabbccdd     X……X        xx
     */
    public final static int WAVE_TDR_ICM_DECAY = 0x66;
    public final static int WAVE_SIM = 0x77;

    /**
     * 数据库存储波形部分
     */
    public DataAdapter adapter;
    public DataDao dao;
    public int selectedId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //G??   让程序变卡？
        initParameter();
    }

    /**
     * 参数初始化
     */
    private void initParameter() {
        mode = TDR;
        range = RANGE_500;
        rangeState = 0;
        gain = 13;
        velocity = 172;
        density = 1;
        densityMax = 1;
        balance = 5;
        delay = 0;
        inductor = 3;
        //二次脉冲多组数据选择
        selectSim = 1;

        dataMax = 540;
        //GC20191219
        dataLength = 510;
        waveArray = new int[540];
        waveDraw = new int[510];
        waveDrawFull = new int[510];
        waveCompare = new int[510];
        waveCompareDraw = new int[510];
        waveCompareFull = new int[510];
        simDraw1 = new int[510];
        simDraw2 = new int[510];
        simDraw3 = new int[510];
        simDraw4 = new int[510];
        simDraw5 = new int[510];
        simDraw6 = new int[510];
        simDraw7 = new int[510];
        simDraw8 = new int[510];
        simDraw1Full = new int[510];
        simDraw2Full = new int[510];
        simDraw3Full = new int[510];
        simDraw4Full = new int[510];
        simDraw5Full = new int[510];
        simDraw6Full = new int[510];
        simDraw7Full = new int[510];
        simDraw8Full = new int[510];

        zero = 0;
        pointDistance = 255;
        //光标在画布中移动的点数   //GC20191218
        cursorMoveValue = 0;
        //光标显示的位置（变化范围0-509）
        positionReal = 0;
        positionVirtual = 255;
        //默认控制虚光标
        cursorState = false;

        //增益大小状态
        gainState = 0;
        //故障击穿时刻对应的那一点
        breakdownPosition = 0;
        //击穿点
        breakBk = 0;

        BaseAppData db = Room.databaseBuilder(getApplicationContext(), BaseAppData.class, "database-wave").build();
        dao = db.dataDao();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

}

/*更改记录*/
//GT 工作信息测试
//GC20181223 实、虚光标切换绘制
//GC20181227 不同方式范围sparkView点数选择（旧有方式弃用）

//GC20190628 光标虚化和限制范围
//GC20190629 光标使用优化
//GC20190702 波形绘制参数准备工作
//GC20190703 记忆比较功能
//GC20190704 增益、平衡、延时命令调节
//GC20190705 fragment切换显示优化，SIM波形选择
//GC20190706 数据处理优化
//GC20190708 ICM自动测距                ***************
//GC20190709 距离计算，比例选择
//GC20191210 增加电感
//GC20190711 放大缩小
//GC20190712 光标零点设置
//GC20190713 数据库波形显示
//GC20190716 存储显示、放大缩小操作之后的bug修正

//GC20191218    光标位置修正
//GC20191219    根据虚光标的原始位置进行放大缩小
//GC20191223    250m范围取点
//GC20191226    250m范围距离
//GC20191231    自动测距修改
//GC20200103    测试缆信息添加
//GC20200106    光标定位修改
//GC20200109    DC方式下自动测距单独实现
//GC20200110    击穿点判断起始位置更改

//GC20200312    故障距离单位显示添加
//GC20200313    增益显示转为百分比
//GC20200314    切换到模式界面时电量图标更新过慢BUG修改
//GC20200319    “等待触发”对话框重连时不消掉BUG修改

//G??
//GC20200317    发送命令和获取电量修改
//EN20200324    增加条件限制，避免极端条件下会多次尝试连接
//GC20200325    连接条件修改

//GC20200327    帮助功能添加
//GC20200330    紫色标记光标添加
//GC20200331    脉宽修改