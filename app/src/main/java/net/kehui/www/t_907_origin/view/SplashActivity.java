package net.kehui.www.t_907_origin.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.kehui.www.t_907_origin.ConnectService;
import net.kehui.www.t_907_origin.R;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.entity.ParamInfo;
import net.kehui.www.t_907_origin.util.MultiLanguageUtil;
import net.kehui.www.t_907_origin.util.PermissionUtil;
import net.kehui.www.t_907_origin.util.StateUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

import static java.lang.Thread.sleep;

/**
 * @author Gong
 * @date 2020/07/16
 */
public class SplashActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {

    @BindView(R.id.tv_version)
    TextView tvVersion;

    //TODO 20191218申请动态权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    //要申请的权限
    private String[] mPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int CODE = 0x001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissions = PermissionUtil.getDeniedPermissions(this, mPermissions);
        if (mPermissions != null) {
            if (mPermissions.length > 0) {
                EasyPermissions.requestPermissions(this, PermissionUtil.permissionText(mPermissions), CODE, mPermissions);
            }
        }

        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
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
                    //使程序休眠1秒
                    sleep(2000);
                    //startService();
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, MainActivity.class);
                    //20200521  启动界面添加
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    //启动MainActivity
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        singleThreadPool.shutdown();

        initView();

    }

    /**
     * 添加版本号显示  //GC20200716
     */
    private void initView() {
        //设置字体
        Typeface type = Typeface.createFromAsset(tvVersion.getContext().getAssets(), "founderBlack.ttf");
        tvVersion.setTypeface(type);
        tvVersion.setText("V" + getAppVersion());
    }

    private String getAppVersion() {
        //获取PackageManager的实例
        PackageManager packageManager = getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionName;

    }

    //所有的权限申请成功的回调
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //do something
    }

    //权限获取失败的回调
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //存在被永久拒绝(拒绝&不再询问)的权限
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            mPermissions = PermissionUtil.getDeniedPermissions(this, mPermissions);
            PermissionUtil.PermissionDialog(this, PermissionUtil.permissionText(mPermissions) + "请在应用权限管理进行设置！");
        } else {
            EasyPermissions.requestPermissions(this, PermissionUtil.permissionText(mPermissions), CODE, mPermissions);
        }
    }

    //权限被拒绝后的显示提示对话框，点击确认的回调
    @Override
    public void onRationaleAccepted(int requestCode) {
        //会自动再次获取没有申请成功的权限
        //do something
    }

    //权限被拒绝后的显示提示对话框，点击取消的回调
    @Override
    public void onRationaleDenied(int requestCode) {
        //什么都不会做
        //do something
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将结果传入EasyPermissions中
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void InitPulseWidthInfo() {
        ParamInfo paramInfo = (ParamInfo) StateUtils.getObject(SplashActivity.this, Constant.PULSE_WIDTH_INFO_KEY);
        if (paramInfo != null) {
            paramInfo.setPulseWidth(0);
        } else {
            paramInfo = new ParamInfo();
            paramInfo.setPulseWidth(0);
        }

        StateUtils.setObject(SplashActivity.this, paramInfo, Constant.PULSE_WIDTH_INFO_KEY);


    }

    //启动后台服务，用于数据通讯。
    public void startService() {
        Intent intent = new Intent(SplashActivity.this, ConnectService.class);
        startService(intent);
    }

    //动态申请权限
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase));
    }

}
