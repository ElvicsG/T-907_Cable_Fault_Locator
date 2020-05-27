package net.kehui.www.t_907_origin;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.kehui.www.t_907_origin.base.BaseActivity;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.thread.ConnectThread;
import net.kehui.www.t_907_origin.thread.ProcessThread;
import net.kehui.www.t_907_origin.util.WifiUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static net.kehui.www.t_907_origin.view.ModeActivity.BUNDLE_MODE_KEY;
import static net.kehui.www.t_907_origin.view.ModeActivity.BUNDLE_COMMAND_KEY;
import static net.kehui.www.t_907_origin.view.ModeActivity.BUNDLE_DATA_TRANSFER_KEY;
import static net.kehui.www.t_907_origin.view.ModeActivity.BUNDLE_PARAM_KEY;

/**
 * @author 34238
 */
public class ConnectService extends Service {

    public int mode;
    public int command;
    public int dataTransfer;
    public int[] wifiStream;
    public static final int PORT = 9000;
    public static int[] mExtra;
    /**
     *是否已经与T-907建立连接
     */
    public static boolean isConnected;
    /**
     * 是否需要连接   //GC? 优化逻辑去掉了
     */
    public static boolean needConnect = true;
    /**
     *是否正在连接中    //20200523
     */
    private boolean isConnecting;
    /**
     * 发送命令
     */
    public static boolean canAskPower = true;

    private BufferedReader br;
    private ConnectThread connectThread;
    private ProcessThread processThread;

    /**
     * 阻塞列队 //数据生产者队列，生产的数据放入队列
     */
    public static ArrayBlockingQueue bytesDataQueue;
    public static Boolean isWifiConnect = false;

    static Socket socket;

    /**
     * 全局的handler对象用来执行UI更新
     */
    public static final int DEVICE_CONNECTED = 1;
    public static final int DEVICE_DISCONNECTED = 2;
    public static final int DEVICE_DO_CONNECT = 3;
    public static final int GET_COMMAND = 4;
    public static final int GET_WAVE = 5;

    public final static String BROADCAST_ACTION_DEVICE_CONNECTED = "device_connected";
    public final static String BROADCAST_ACTION_DEVICE_CONNECT_FAILURE = "device_failure";
    public final static String BROADCAST_ACTION_DOWIFI_COMMAND = "broadcast_action_dowifi_command";
    public final static String BROADCAST_ACTION_DOWIFI_WAVE = "broadcast_action_dowifi_wave";
    public final static String INTENT_KEY_COMMAND = "CMD";
    public final static String INTENT_KEY_WAVE = "WAVE";

    @Override
    public void onCreate() {
        WifiUtil wifiUtil = new WifiUtil(getApplicationContext());
        //WIFI网卡可用
        if (wifiUtil.checkState() == 3) {
            if (wifiUtil.getSSID().contains(Constant.SSID)) {
                //更换语言重启时，连着设备无线
                isWifiConnect = true;
                handler.sendEmptyMessage(DEVICE_DO_CONNECT);
            } else {
                handler.sendEmptyMessage(DEVICE_DO_CONNECT);
            }
        } else {
            handler.sendEmptyMessage(DEVICE_DO_CONNECT);
        }
        //EN20200324
        Log.e("【SOCKET连接】", "服务启动");

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
        this.bytesDataQueue = new ArrayBlockingQueue(100);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //如果连接正常并且允许收取电量。
                if (isConnected && canAskPower) {
                    //EN20200324    //G??   有必要么
                    canAskPower = false;
                    command = 0x06;
                    dataTransfer = 0x13;
                    sendCommand();
                }
                handler.postDelayed(this, 30000);
            }
        };
        handler.postDelayed(runnable, 30000);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        //GN 服务启动相关
        return null;
    }

    public Handler handler = new Handler(msg -> {
        switch (msg.what) {
            case DEVICE_CONNECTED:
                Toast.makeText(this, getResources().getString(R.string.connect_success), Toast.LENGTH_SHORT).show();
                sendBroadcast(BROADCAST_ACTION_DEVICE_CONNECTED, null, null);
                break;
            case DEVICE_DISCONNECTED:
//                Toast.makeText(ConnectService.this, getResources().getString(R.string.disconnect), Toast.LENGTH_LONG).show();
                //连接断开时，重置变量 //EN20200324
                socket = null;
                connectThread = null;
                processThread = null;
                break;
            case DEVICE_DO_CONNECT:
                //Toast.makeText(ConnectService.this, getResources().getString(R.string.communication_failed), Toast.LENGTH_LONG).show();
                //连接条件修改    //GC20200325
                /*if (needReconnect && !isConnecting) {
                    connectWifi();
                    connectDevice();
                }*/
                if (!isConnecting) {
                    connectWifi();
                    connectDevice();
                }
                break;
            case GET_COMMAND:
                wifiStream = msg.getData().getIntArray("CMD");
                assert wifiStream != null;
                sendBroadcast(BROADCAST_ACTION_DOWIFI_COMMAND, INTENT_KEY_COMMAND, wifiStream);
                break;
            case GET_WAVE:
                wifiStream = msg.getData().getIntArray("WAVE");
                assert wifiStream != null;
                mExtra = wifiStream;
                sendBroadcast(BROADCAST_ACTION_DOWIFI_WAVE, INTENT_KEY_WAVE, wifiStream);
                break;
            default:
                break;
        }
        return false;
    });

    /**
     * 监听网络广播
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            assert action != null;
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                assert connectivityManager != null;
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                /*if (info != null && isWifiConnect == false) {*/
                if (info != null) {
                    if ((info.isConnected() && info.getExtraInfo().contains(Constant.SSID)) && !isWifiConnect) {
                        //EN20200324
                        Log.e("【SOCKET连接】", "网络连接状态变化，重连");
                        handler.sendEmptyMessage(DEVICE_DO_CONNECT);
                    }
                } else {
                    isWifiConnect = false;
                    //todo 断开链接的通知
                    //GT1 无连接第一次走这里
                    handler.sendEmptyMessage(DEVICE_DISCONNECTED);
                    try {
                        connectThread.getOutputStream().flush();
                        connectThread.getOutputStream().close();
                        connectThread.getSocket().close();
                        /*socket = null;
                        connectThread = null;
                        processThread = null;*/ //EN20200324    //G?? 可以去掉吧
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    sendBroadcast(BROADCAST_ACTION_DEVICE_CONNECT_FAILURE, null, null);
                }
            }
        }
    };

    private void connectWifi() {
        WifiUtil wifiUtil = new WifiUtil(this);
        if (wifiUtil.checkState() != 3) {
            //WIFI网卡不可用
            wifiUtil.openWifi();
        }
        try {
            if (!wifiUtil.getSSID().contains(Constant.SSID)) {
                wifiUtil.addNetwork(wifiUtil.createWifiInfo(Constant.SSID, "123456789", 3));
            }
        } catch (Exception l_Ex) {
        }
    }

    private void connectDevice() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("connect-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(3, 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), threadFactory,
                new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(() -> {
            try {
                if (!isConnected) {
                    isConnecting = true;
                    Log.e("【SOCKET连接】", "开始连接");
                    if (socket == null) {
                        Log.e("【SOCKET连接】", "尝试建立连接");
                        socket = new Socket(Constant.DEVICE_IP, PORT);
                        socket.setKeepAlive(true);
                    }
                    if (connectThread == null) {
                        Log.e("【SOCKET连接】", "启动接收数据线程");
                        connectThread = new ConnectThread(socket, handler, Constant.DEVICE_IP);
                        connectThread.start();
                    }
                    if (processThread == null) {
                        Log.e("【SOCKET连接】", "启动接收数据线程");
                        processThread = new ProcessThread(handler);
                        processThread.start();
                    }
                    Log.e("【SOCKET连接】", "连接成功结束");
                    isConnecting = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    connectThread.getOutputStream().flush();
                    connectThread.getOutputStream().close();
                    connectThread.getSocket().close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                sendBroadcast(BROADCAST_ACTION_DEVICE_CONNECT_FAILURE, null, null);
                //GT2
                handler.sendEmptyMessage(DEVICE_DISCONNECTED);
                Log.e("【SOCKET连接】", "连接失败重连");
                isConnecting = false;
                handler.sendEmptyMessageDelayed(DEVICE_DO_CONNECT, 2000);
            }
        });
        Log.e("DIA", "WIFI连接：" + "隐藏");
        singleThreadPool.shutdown();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        assert intent != null;
        Bundle bundle = intent.getBundleExtra(BUNDLE_PARAM_KEY);
        //接收到发送指令的信息
        if (bundle != null && bundle.getInt(BUNDLE_COMMAND_KEY) != 0) {
            mode = bundle.getInt(BUNDLE_MODE_KEY);
            command = bundle.getInt(BUNDLE_COMMAND_KEY);
            dataTransfer = bundle.getInt(BUNDLE_DATA_TRANSFER_KEY);
            sendCommand();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * APP下发命令
     */
    public void sendCommand() {
        //发命令时禁止请求电量    //EN20200324
        canAskPower = false;

        byte[] request = new byte[8];
        //数据头部分
        request[0] = (byte) 0xeb;
        request[1] = (byte) 0x90;
        request[2] = (byte) 0xaa;
        request[3] = (byte) 0x55;
        //数据长度
        request[4] = (byte) 0x03;
        request[5] = (byte) command;
        if (command == BaseActivity.COMMAND_RANGE && dataTransfer == BaseActivity.RANGE_250) {
            //需要发送250m范围命令时改为500m范围命令
            dataTransfer = BaseActivity.RANGE_500;
        }
        if (command == BaseActivity.COMMAND_MODE && dataTransfer == BaseActivity.ICM_DECAY) {
            //需要发送ICM-DECAY方式命令时改为ICM命令
            dataTransfer = BaseActivity.ICM;
        }
        request[6] = (byte) dataTransfer;
        int sum = request[4] + request[5] + request[6];
        request[7] = (byte) sum;
        //TODO 20200407 发送数据是判断连接是否正常，否则不发送
        if (connectThread != null && ConnectService.isConnected) {
            connectThread.sendCommand(request);
            Log.e("#【APP-->设备】", "指令：" + command + " 传输数据：" + sendDataTransfer(command, dataTransfer) + " ——发命令时禁止请求电量");
        }
    }

    /**
     * @param cmdStr    指令
     * @param dataStr   传输数据
     * @return  发送内容
     */
    private String sendDataTransfer(int cmdStr, int dataStr) {
        String returnStr = "";
        if (cmdStr == 1) {
            switch (dataStr) {
                case 17:
                    returnStr = "0x11 / 测试";
                    break;
                case 34:
                    returnStr = "0x22 / 取消测试";
                    break;
                default:
                    break;
            }
        }
        if (cmdStr == 2) {
            switch (dataStr) {
                case 17:
                    returnStr = "0x11 / TDR";
                    break;
                case 34:
                    returnStr = "0x22 / ICM";
                    break;
                case 85:
                    returnStr = "0x55 / ICM_DECAY";
                    break;
                case 51:
                    returnStr = "0x33 / SIM";
                    break;
                case 68:
                    returnStr = "0x44 / DECAY";
                    break;
                default:
                    break;
            }
        }
        if (cmdStr == 3) {
            switch (dataStr) {
                case 153:
                    returnStr = "0x99 / 250m";
                    break;
                case 17:
                    returnStr = "0x11 / 500m";
                    break;
                case 34:
                    returnStr = "0x22 / 1km";
                    break;
                case 51:
                    returnStr = "0x33 / 2km";
                    break;
                case 68:
                    returnStr = "0x44 / 4km";
                    break;
                case 85:
                    returnStr = "0x55 / 8km";
                    break;
                case 102:
                    returnStr = "0x66 / 16km";
                    break;
                case 119:
                    returnStr = "0x77 / 32km";
                    break;
                case 136:
                    returnStr = "0x88 / 64km";
                    break;
                default:
                    break;
            }
        }
        if (cmdStr == 4) {
            returnStr = dataStr + "   / 增益";
        }
        if (cmdStr == 5) {
            returnStr = dataStr + "   / 延迟";
        }
        if (cmdStr == 6) {
            returnStr = " / 电量";
        }
        if (cmdStr == 7) {
            returnStr = dataStr + "   / 平衡";
        }
        if (cmdStr == 9) {
            returnStr = "开始接收数据 " + String.valueOf(dataStr);
        }
        if (cmdStr == 10) {
            returnStr = dataStr + " / 脉宽";
        }
        return returnStr;
    }

    /**
     *发送广播
     */
    public void sendBroadcast(String action, String extraKey, int[] extra) {
        try {
            Intent intent = new Intent();
            intent.setAction(action);
            if (extraKey != null) {
                intent.putExtra(extraKey, extra);
            }
            sendBroadcast(intent);
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(action);
            mExtra = extra;
            sendBroadcast(intent);
            e.printStackTrace();
        }

    }

    /**
     * @return 获取ip
     */
    private ArrayList<String> getConnectedIP() {
        ArrayList<String> connectedIP = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(" +");
                if (splitted != null && splitted.length >= 4) {
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connectedIP;
    }

    @Override
    public void onDestroy() {
        isConnected = false;
        handler.removeCallbacksAndMessages(null);
        unregisterReceiver(receiver);

        try {
            connectThread.getOutputStream().flush();
            connectThread.getOutputStream().close();
            connectThread.getSocket().close();

/*            WifiUtil wifiUtil = new WifiUtil(this);
            wifiUtil.closeWifi();*/
            android.os.Process.killProcess(android.os.Process.myPid());

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

}
