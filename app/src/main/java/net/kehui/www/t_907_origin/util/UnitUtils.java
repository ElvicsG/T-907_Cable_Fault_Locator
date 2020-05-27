package net.kehui.www.t_907_origin.util;

import java.text.DecimalFormat;

public class UnitUtils {
    public static final double yingliToKmScale = 1.61;
    public static final double kmToYingliScale = 0.62;
    public static final double ftToMiScale = 0.3048;
    //单位转化精度    //20200522
    public static final double miToFtScale = 3.2808399;


    //英里转公里
    public static String yingliToKm(double yingli) {
        return new DecimalFormat("0.0").format(yingli * yingliToKmScale);
    }

    //英尺转米
    public static String ftToMi(double ft) {
        return new DecimalFormat("0.0").format(ft * ftToMiScale);
    }

    //公里转英里
    public static String kmToYingli(double km) {
        return new DecimalFormat("0.0").format(km * kmToYingliScale) ;
    }

    //英尺转米
    public static String miToFt(double mi) {
        return new DecimalFormat("0.0").format((mi * miToFtScale));
    }

}
