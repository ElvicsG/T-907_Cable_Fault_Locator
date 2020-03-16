package net.kehui.www.t_907_origin.application;

import android.app.Application;

import android.content.Context;
import android.content.res.Configuration;

import androidx.multidex.MultiDex;

import net.kehui.www.t_907_origin.util.MultiLanguageUtil;
import net.kehui.www.t_907_origin.util.StateUtils;


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
