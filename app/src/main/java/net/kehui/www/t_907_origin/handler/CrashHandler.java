package net.kehui.www.t_907_origin.handler;

import android.content.Context;
import android.content.Intent;

import net.kehui.www.t_907_origin.ConnectService;
import net.kehui.www.t_907_origin.application.MyApplication;
import net.kehui.www.t_907_origin.view.MainActivity;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context mcontext;

    private static CrashHandler INSTANCE = new CrashHandler();

    private CrashHandler() {

    }



    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * 当有未捕获异常发生，就会调用该函数，
     * 可以在该函数中对异常信息捕获并上传
     *
     * @param t 发生异常的线程
     * @param e 异常
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 处理异常,可以自定义弹框，可以上传异常信息
        /*Intent intent = new Intent(mcontext, ConnectService.class);
        mcontext.stopService(intent);*/
        // 干掉当前的程序
        //android.os.Process.killProcess(android.os.Process.myPid());
    }
}
