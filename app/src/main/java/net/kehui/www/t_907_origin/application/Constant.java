package net.kehui.www.t_907_origin.application;

/**
 * @author li.md
 * @date 2019/7/8
 */
public class Constant {
    public static final String SSID = "T-9071";
    public static final String DEVICE_IP = "192.168.5.143";
    public static final String BASE_API = "http://cfl.kehui.cn/";
    public static final int MI_UNIT = 0;
    public static final int FT_UNIT = 1;
    public static int CurrentUnit = MI_UNIT;
    public static int CurrentSaveUnit = MI_UNIT;

    public static final String PARAM_INFO_KEY = "param_info_key";
    public static final String PULSE_WIDTH_INFO_KEY = "pulse_width_info_key";

    /**
     * 脉宽修改     //GC20200331
     */
    public static boolean hasPulseWidth;
    public static int ModeValue;
    public static int RangeValue;
    public static int Gain;
    public static int SaveToDBGain;
    public static double Velocity;
    public static int Density;
    public static int DensityMax;
    public static String Date;
    public static String Time;
    public static int Mode;//16进制指令
    public static int Range;
    public static String Line;
    public static int Phase;//代表相位编码
    public static int PositonV;//虚光标
    public static int PositionR;//实光标

    /**
     * 电量状态记录   //GC20200314
     */
    public static int batteryValue = -1;
    public static String Tester;
    public static double CurrentLocation;
    public static double SaveLocation;
    public static int[] Para;
    public static int[] WaveData;
    public static int[] SimData;
    public static int[] TempData1;
    public static int[] TempData2;
    public static int[] TempData3;
    public static int[] TempData4;
    public static int[] TempData5;
    public static int[] TempData6;
    public static int[] TempData7;
    public static int[] TempData8;

    //TODO 20191218 触发框出现后不点击取消测试，按机器返回键的处理逻辑
    public static boolean isCancelAim = false;

    //TODO 20191219 当前语言
    public static String currentLanguage = "";

}
