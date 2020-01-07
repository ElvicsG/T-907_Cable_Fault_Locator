package net.kehui.www.t_907_origin.application;

import android.app.Activity;
import android.app.Application;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.multidex.MultiDex;

import net.kehui.www.t_907_origin.handler.CrashHandler;
import net.kehui.www.t_907_origin.util.MultiLanguageUtil;
import net.kehui.www.t_907_origin.util.StateUtils;
import net.kehui.www.t_907_origin.view.SplashActivity;

import java.util.Locale;


/**
 * @author IF
 */
public class MyApplication extends Application {

    public static MyApplication instances;


    @Override
    public void onCreate() {
        super.onCreate();
/*        CrashHandler handler = CrashHandler.getInstance();
        Thread.setDefaultUncaughtExceptionHandler(handler);*/

        instances = this;
        MultiLanguageUtil.init(getApplicationContext());
        String languageType = StateUtils.getString(MyApplication.getInstances(), AppConfig.CURRENT_LANGUAGE, "ch");
        MultiLanguageUtil.getInstance().updateLanguage(languageType);
        Constant.currentLanguage = languageType;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        String languageType = StateUtils.getString(MyApplication.getInstances(), AppConfig.CURRENT_LANGUAGE, "ch");
        MultiLanguageUtil.getInstance().updateLanguage(languageType);
        Constant.currentLanguage = languageType;

    }

    public static MyApplication getInstances() {
        return instances;

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //android.support.multidex.MultiDex.install(this);
        MultiDex.install(this);

    }

}
