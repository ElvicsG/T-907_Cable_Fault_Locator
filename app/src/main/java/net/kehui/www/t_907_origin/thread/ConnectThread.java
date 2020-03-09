package net.kehui.www.t_907_origin.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.kehui.www.t_907_origin.ConnectService;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.view.MainActivity;
import net.kehui.www.t_907_origin.view.ModeActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.security.auth.login.LoginException;

import static android.content.Context.SYSTEM_HEALTH_SERVICE;
import static net.kehui.www.t_907_origin.base.BaseActivity.COMMAND_MODE;
import static net.kehui.www.t_907_origin.base.BaseActivity.COMMAND_RANGE;
import static net.kehui.www.t_907_origin.base.BaseActivity.COMMAND_RECEIVE_WAVE;
import static net.kehui.www.t_907_origin.base.BaseActivity.ICM_DECAY;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_250;
import static net.kehui.www.t_907_origin.base.BaseActivity.RECEIVE_RIGHT;
import static net.kehui.www.t_907_origin.base.BaseActivity.TDR;
import static net.kehui.www.t_907_origin.base.BaseActivity.ICM;
import static net.kehui.www.t_907_origin.base.BaseActivity.DECAY;
import static net.kehui.www.t_907_origin.base.BaseActivity.SIM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_16_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_1_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_2_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_32_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_4_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_500;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_64_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_8_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.READ_ICM_DECAY;
import static net.kehui.www.t_907_origin.base.BaseActivity.READ_TDR_SIM;

/**
 * @author Gong
 * @date 2019/07/15
 */
public class ConnectThread extends Thread {

    private final Socket socket;
    private Handler handler;
    private String ip;
    private OutputStream outputStream;

    private int mode = TDR;
    private int range = 0;
    private int wifiStreamLen = 549;

    private int mimWifiStreamLen = 0;
    private int readCount = 0;
    private boolean isCommand = true;


    public Socket getSocket() {
        return socket;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public ConnectThread(Socket socket, Handler handler, String ip) {
        setName("ConnectThread");
        this.socket = socket;
        this.handler = handler;
        this.ip = ip;

    }

    private void SendCmdMessage(int[] msgData) {
        //Log.e("【设备-->APP】", " 指令：" + msgData[5] + " 传输数据：" + msgData[6] + " 全部数据：" + Arrays.toString(msgData));
        Log.e("【设备-->APP】", " 指令：" + msgData[5] + " 传输数据：" + msgData[6]);
        Message message = Message.obtain();
        message.what = ModeActivity.GET_COMMAND;
        Bundle bundle = new Bundle();
        bundle.putIntArray("CMD", msgData);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private void SendWaveMessage(int[] waveData) {
        Log.e("【波形数据处理】", "正常包");
        //Log.e("【波形数据处理】", "正常包：" + Arrays.toString(waveData));

        Message message = Message.obtain();
        message.what = ModeActivity.GET_WAVE;
        Bundle bundle = new Bundle();
        bundle.putIntArray("WAVE", waveData);
        message.setData(bundle);
        handler.sendMessage(message);
        //Log.e("【波形数据处理】", "正常包：" + Arrays.toString(waveData));
    }


    /**
     * 变更接收数据处理的方式
     * 不再使用是否是命令的判断
     * 循环接收数据，判断数据头，区分是命令还是波形。
     * 思路：
     * 收到的数据可能会混杂命令和数据，从数据头去判断，如果是命令，则处理完，未处理数据前移，依次处理。
     * 采用了生产者消费者模式，本线程只接收数据，处理数据放到单独的线程。
     */
    @Override
    public void run() {
        if (socket == null) {
            return;
        }
        //Constant.DeviceIP = ip;
        handler.sendEmptyMessage(ModeActivity.DEVICE_CONNECTED);
        try {
            //获取数据流
            InputStream inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            int bytesread = 0;
            int bytes = 0;

            byte[] buffer = new byte[65565 * 9];
            byte[] command = new byte[65565 * 9];
            byte[] wave = new byte[65565 * 9];
            byte[] tempBuffer = new byte[65565 * 9 + 18];
            int processedByte = 0;
            int remainByte = 0;
            boolean needAddData = false;

            boolean needProcessMIMData = false;

            int mimProcessedDataLen = 0;

            //是否需要异常处理
            boolean needOtherProcess = false;

            //TODO 20191220 重新写收设备数据的逻辑


            while (true) {
                Arrays.fill(buffer, (byte) 0);
                bytesread = inputStream.read(buffer);

                if (bytesread == -1) {
                    handler.sendEmptyMessage(ConnectService.DEVICE_RECONNECT);
                    break;
                }
                //printBuffer("【总接受数据】", buffer);
                Log.e("【接收数据总量】", "" + bytesread);
                if (bytesread > 0) {
                    bytes = bytesread;
                    while (true) {
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //将读到的数据放到缓存变量
                        //清空缓存数组
                        //如果剩余要处理的为0则重新赋值
                        if (remainByte == 0 & needAddData == false) {
                            Arrays.fill(tempBuffer, (byte) 0);
                            System.arraycopy(buffer, 0, tempBuffer, 0, bytes);
                        }
                        //开始检测是否有垃圾数据
                        //不是垃圾数据
                        if ((tempBuffer[0] & 0xff) != 235 && needAddData == false && needProcessMIMData == false) {
                            Log.e("【新数据处理】", "无效头数据");
                            //一个个处理太慢，先直接扔掉吧。
                            break;
                           /* //已经处理的数量+1
                            processedByte += 1;
                            //剩余待处理数量-1
                            remainByte = bytes - processedByte;
                            //后面的数据前移一个字节，继续下一个循环处理数据
                            byte[] convertBuffer = new byte[remainByte];
                            System.arraycopy(tempBuffer, 1, convertBuffer, 0, remainByte);
                            Arrays.fill(tempBuffer, (byte) 0);
                            System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte);
                            continue;*/
                        } else {
                            //普通命令，截8个
                            if ((tempBuffer[3] & 0xff) == 85 && (tempBuffer[4] & 0xff) == 3) {
                                Log.e("【新数据处理】", "普通命令");

                                byte[] cmdBytes = new byte[8];
                                System.arraycopy(tempBuffer, 0, cmdBytes, 0, 8);
                                //加入队列
                                ConnectService.bytesDataQueue.put(cmdBytes);
                                //已经处理过的字节数累加8
                                processedByte += 8;
                                //剩余字节数减8
                                remainByte = bytes - processedByte;
                                //如果剩余字节数为0，跳出循环，不需要继续处理,继续接收数据。
                                if (remainByte == 0) {
                                    processedByte = 0;
                                    break;
                                }
                                //没处理完，可能有命令或者波形数据需要处理，继续
                                else {
                                    //将未处理的字节数组前移
                                    byte[] convertBuffer = new byte[remainByte];
                                    System.arraycopy(tempBuffer, 8, convertBuffer, 0, remainByte);
                                    Arrays.fill(tempBuffer, (byte) 0);
                                    System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte);
                                    continue;
                                }
                            }
                            //电量，截9个
                            else if ((tempBuffer[3] & 0xff) == 85 && (tempBuffer[4] & 0xff) == 4) {
                                Log.e("【新数据处理】", "电池命令");

                                byte[] powerBytes = new byte[9];
                                System.arraycopy(tempBuffer, 0, powerBytes, 0, 9);
                                //加入队列
                                ConnectService.bytesDataQueue.put(powerBytes);
                                //已经处理过的字节数累加8
                                processedByte += 9;
                                //剩余字节数减8
                                remainByte = bytes - processedByte;
                                //如果剩余字节数为0，跳出循环，不需要继续处理,继续接收数据。
                                if (remainByte == 0) {
                                    processedByte = 0;
                                    break;
                                }
                                //没处理完，可能有命令或者波形数据需要处理，继续
                                else {
                                    //将未处理的字节数组前移
                                    byte[] convertBuffer = new byte[remainByte];
                                    System.arraycopy(tempBuffer, 9, convertBuffer, 0, remainByte);
                                    Arrays.fill(tempBuffer, (byte) 0);
                                    System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte);
                                    continue;
                                }
                            }
                            //波形数据，截需要的长度，不够要拼数据
                            else if ((tempBuffer[3] & 0xff) == 102 && needAddData == false) {
                                //Log.e("【时效测试】", "开始接收波形数据");


                                //如果不是从处理中数据过来的，初始化为当前接受的数据，也就是直接是波形数据的。
                                if (remainByte == 0)
                                    remainByte = bytes;
                                //如果剩余波形长度和需要的长度一致
                                if (remainByte == wifiStreamLen) {
                                    Log.e("【新数据处理】", "一次性长度一致，不用补数据");
                                    byte[] waveBytes = new byte[remainByte];
                                    System.arraycopy(tempBuffer, 0, waveBytes, 0, wifiStreamLen);
                                    ConnectService.bytesDataQueue.put(waveBytes);

                                    remainByte = 0;
                                    processedByte = 0;
                                    needAddData = false;
                                    break;
                                }
                                //不一致要继续接受数据
                                else {
                                    needAddData = true;
                                    Log.e("【新数据处理】", "数据不全，需要补全，需要：" + wifiStreamLen + ",当前：" + remainByte + ",补全数据ing....");
                                    break;
                                }
                            } else if ((tempBuffer[3] & 0xff) == 119 && needAddData == false && needProcessMIMData == false) {
                                //Log.e("【时效测试】", "开始接收波形数据");


                                if ((tempBuffer[3] & 0xff) == 119) {
                                    mimWifiStreamLen = wifiStreamLen / 9;
                                }
                                //如果不是从处理中数据过来的，初始化为当前接受的数据，也就是直接是波形数据的。
                                if (remainByte == 0)
                                    remainByte = bytes;

                                if (remainByte >= mimWifiStreamLen) {
                                    needProcessMIMData = true;
                                    continue;

                                } else {
                                    needAddData = true;
                                    break;
                                }

                            } else {
                                //如果是正常的命令或波形数据，则继续收取数据，如果不是，则走异常数据处理。
                                if ((tempBuffer[3] & 0xff) == 102) {
                                    //Log.e("【时效测试】", "接收波形数据");

                                    System.arraycopy(buffer, 0, tempBuffer, remainByte, bytes);
                                    remainByte += bytes;

                                    Log.e("【新数据处理】", "需要：" + wifiStreamLen + ",当前：" + remainByte + ",补全数据ing....");
                                    //补全数据后，如果长度相等，则不需要继续补全
                                    if (remainByte == wifiStreamLen) {

                                        byte[] waveBytes = new byte[wifiStreamLen];
                                        System.arraycopy(tempBuffer, 0, waveBytes, 0, wifiStreamLen);
                                        ConnectService.bytesDataQueue.put(waveBytes);
                                        Log.e("【新数据处理】", "补全结束");

                                        remainByte = 0;
                                        processedByte = 0;
                                        needAddData = false;
                                        break;

                                    } else if (remainByte > wifiStreamLen) {
                                        //Log.e("【时效测试】", "接收波形数据");

                                        Log.e("【新数据处理】", "数据超长，取正确包，截取继续处理，可能是波形后跟了电量数据");

                                        //获取补全的波形放入队列
                                        byte[] correctBytes = new byte[wifiStreamLen];
                                        System.arraycopy(tempBuffer, 0, correctBytes, 0, wifiStreamLen);
                                        ConnectService.bytesDataQueue.put(correctBytes);

                                        //超出的波形重新放入缓存，继续处理。
                                        byte[] convertBuffer = new byte[remainByte - wifiStreamLen];
                                        System.arraycopy(tempBuffer, wifiStreamLen, convertBuffer, 0, remainByte - wifiStreamLen);
                                        Arrays.fill(tempBuffer, (byte) 0);
                                        System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte - wifiStreamLen);

                                        //设置基础处理的几个参数
                                        bytes = remainByte - wifiStreamLen;
                                        remainByte = remainByte - wifiStreamLen;
                                        processedByte = 0;
                                        needAddData = false;
                                        continue;

                                    } else {
                                        int i = 0;
                                        break;
                                    }

                                } else if (((tempBuffer[3] & 0xff) == 119
                                        || (tempBuffer[3] & 0xff) == 136
                                        || (tempBuffer[3] & 0xff) == 153
                                        || (tempBuffer[3] & 0xff) == 170
                                        || (tempBuffer[3] & 0xff) == 187
                                        || (tempBuffer[3] & 0xff) == 204
                                        || (tempBuffer[3] & 0xff) == 221
                                        || (tempBuffer[3] & 0xff) == 238
                                        || (tempBuffer[3] & 0xff) == 255) & needProcessMIMData == true) {

                                    if (remainByte >= mimWifiStreamLen) {
                                        byte[] waveBytes = new byte[mimWifiStreamLen];
                                        System.arraycopy(tempBuffer, 0, waveBytes, 0, mimWifiStreamLen);
                                        ConnectService.bytesDataQueue.put(waveBytes);
                                        Log.e("【MIM】", "入库：" + (waveBytes[3] & 0xff));
                                        remainByte -= mimWifiStreamLen;
                                        mimProcessedDataLen += mimWifiStreamLen;

                                        if (remainByte == 0 && mimProcessedDataLen == wifiStreamLen) {
                                            remainByte = 0;
                                            processedByte = 0;
                                            mimProcessedDataLen = 0;

                                            needAddData = false;
                                            needProcessMIMData = false;
                                            break;
                                        } else if (remainByte > 0 && mimProcessedDataLen == wifiStreamLen) {

                                            remainByte = 0;
                                            processedByte = 0;
                                            mimProcessedDataLen = 0;

                                            needAddData = false;
                                            needProcessMIMData = false;
                                            break;
                                        } else {
                                            byte[] convertBytes = new byte[remainByte];
                                            System.arraycopy(tempBuffer, mimWifiStreamLen, convertBytes, 0, remainByte);
                                            Arrays.fill(tempBuffer, (byte) 0);
                                            System.arraycopy(convertBytes, 0, tempBuffer, 0, remainByte);

                                            needProcessMIMData = true;
                                            continue;
                                        }


                                    } else {
                                        needAddData = true;
                                        needProcessMIMData = false;
                                        break;
                                    }
                                    /*if (mimProcessedDataLen == wifiStreamLen) {
                                        remainByte = 0;
                                        processedByte = 0;
                                        mimProcessedDataLen = 0;

                                        needAddData = false;
                                        needProcessMIMData = false;
                                        break;
                                    } else if (mimProcessedDataLen > wifiStreamLen) {
                                        remainByte = 0;
                                        processedByte = 0;
                                        mimProcessedDataLen = 0;

                                        needAddData = false;
                                        needProcessMIMData = false;
                                        break;
                                    } else {
                                        needAddData = true;
                                        needProcessMIMData = false;
                                        break;
                                    }*/


                                } else if (((tempBuffer[3] & 0xff) == 119
                                        || (tempBuffer[3] & 0xff) == 136
                                        || (tempBuffer[3] & 0xff) == 153
                                        || (tempBuffer[3] & 0xff) == 170
                                        || (tempBuffer[3] & 0xff) == 187
                                        || (tempBuffer[3] & 0xff) == 204
                                        || (tempBuffer[3] & 0xff) == 221
                                        || (tempBuffer[3] & 0xff) == 238
                                        || (tempBuffer[3] & 0xff) == 255) & needAddData == true) {

                                    System.arraycopy(buffer, 0, tempBuffer, remainByte, bytes);
                                    remainByte += bytes;

                                    if (remainByte >= mimWifiStreamLen) {
                                        needProcessMIMData = true;
                                        needAddData = false;
                                        continue;

                                    } else {
                                        needAddData = true;
                                        needProcessMIMData = false;
                                        break;
                                    }


                                }
                                //数据头部不适正常数据的，如中断接受的波形数据，在这里处理
                                else {
                                    //此处不容易测试，可能会有bug，需要时间调试。
                                    Log.e("【容错处理】", "进入容错程序");
                                    remainByte = 0;
                                    processedByte = 0;
                                    mimProcessedDataLen = 0;
                                    needAddData = false;
                                    needProcessMIMData = false;
                                    break;

                                }
                            }
                        }

                    }
                }
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (
                IOException | InterruptedException e) {
            handler.sendEmptyMessage(ConnectService.DEVICE_RECONNECT);
            e.printStackTrace();
        }
    }

    /*
    //TODO 20191228 处理电量数据，处理包头异常数据，重写逻辑前的备份
    @Override
    public void run() {
        if (socket == null) {
            return;
        }
        Constant.DeviceIP = ip;
        handler.sendEmptyMessage(ModeActivity.DEVICE_CONNECTED);
        try {
            //获取数据流
            InputStream inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            int bytesread = 0;
            int bytes = 0;

            byte[] buffer = new byte[65565 * 9];
            byte[] command = new byte[65565 * 9];
            byte[] wave = new byte[65565 * 9];
            byte[] tempBuffer = new byte[65565 * 9];
            int processedByte = 0;
            int remainByte = 0;
            boolean needAddData = false;

            //是否需要异常处理
            boolean needOtherProcess = false;

            //TODO 20191220 重新写收设备数据的逻辑

            *//*    *
     * 变更接收数据处理的方式
     * 不再使用是否是命令的判断
     * 循环接收数据，判断数据头，区分是命令还是波形。
     * 思路：
     * 收到的数据可能会混杂命令和数据，从数据头去判断，如果是命令，则处理完，未处理数据前移，依次处理。
     **//*
            while (true) {
                bytesread = inputStream.read(buffer);

                if (bytesread == -1) {
                    handler.sendEmptyMessage(ConnectService.DEVICE_RECONNECT);
                    break;
                }
                //printBuffer("【总接受数据】", buffer);
                Log.e("【接收数据总量】", "" + bytesread);
                //未知模式上来的很长数据，丢弃
                if (bytesread > wifiStreamLen + 40) {
                    Log.e("【接收数据】", "发现异常数据长度，超过需要的长度，丢弃。");
                    continue;
                }
                if (bytesread > 0) {
                    bytes = bytesread;
                    //收到的字节大于0，开始处理数据
                    while (true) {
                        //如果没有要处理的，则把全部数据放到缓存
                        if (remainByte == 0 && needOtherProcess == false) {
                            //清空缓存字节数组
                            Arrays.fill(tempBuffer, (byte) 0);
                            System.arraycopy(buffer, 0, tempBuffer, 0, bytes);
                        }
                        //是命令
                        if ((tempBuffer[3] & 0xff) == 85) {
                            needOtherProcess = false;
                            //从数据中取8个，等待传递，剩余可能有数据，需要继续处理
                            System.arraycopy(tempBuffer, 0, command, 0, 8);
                            //System.arraycopy(tempBuffer, 0, command, 0, 8);
                            //需要传递的命令数据
                            int[] wifiCommand = new int[8];
                            for (int i = 0; i < 8; i++) {
                                //将字节数组转变为int数组
                                wifiCommand[i] = command[i] & 0xff;
                            }
                            SendCmdMessage(wifiCommand);

                            //已经处理过的字节数累加8
                            processedByte += 8;
                            //剩余字节数减8
                            remainByte = bytes - processedByte;
                            //如果剩余字节数为0，跳出循环，不需要继续处理,继续接收数据。
                            if (remainByte == 0) {
                                processedByte = 0;
                                break;
                            }
                            //没处理完，可能有命令或者波形数据需要处理，继续
                            else {
                                //将未处理的字节数组前移
                                byte[] convertBuffer = new byte[65565 * 9];
                                System.arraycopy(tempBuffer, 8, convertBuffer, 0, remainByte);
                                Arrays.fill(tempBuffer, (byte) 0);
                                System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte);
                                continue;
                                //printBuffer("前移数据后/已处理：" + processedByte + "/剩余：" + remainByte, tempBuffer);
                            }
                        }
                        //是波形
                        else if (((tempBuffer[3] & 0xff) == 102 || (tempBuffer[3] & 0xff) == 119) && needAddData == false) {
                            needOtherProcess = false;
                            //如果不是从处理中数据过来的，初始化为当前接受的数据，也就是直接是波形数据的。
                            if (remainByte == 0)
                                remainByte = bytes;
                            //如果剩余波形长度和需要的长度一致
                            if (remainByte == wifiStreamLen) {
                                Log.e("【状态】", "一次性长度一致");
                                System.arraycopy(tempBuffer, 0, wave, 0, wifiStreamLen);

                                int[] wifiWave = new int[wifiStreamLen];
                                for (int i = 0; i < wifiStreamLen; i++) {
                                    //将字节数组转变为int数组
                                    wifiWave[i] = wave[i] & 0xff;
                                }
                                SendWaveMessage(wifiWave);


                                remainByte = 0;
                                processedByte = 0;
                                needAddData = false;
                                break;
                            }
                            //不一致要继续接受数据
                            else {
                                needAddData = true;
                                Log.e("【状态】", "数据不全，需要补全，需要：" + wifiStreamLen + ",当前：" + remainByte + ",补全数据ing....");
                                break;
                            }

                        }
                        //是波形没收完，将收到的数据累加到未处理完的数据上，继续处理
                        else {
                            //如果是正常的命令或波形数据，则继续收取数据，如果不是，则走异常数据处理。
                            if ((tempBuffer[3] & 0xff) == 102 || (tempBuffer[3] & 0xff) == 85 || (tempBuffer[3] & 0xff) == 119) {
                                //System.arraycopy(buffer, 0, tempBuffer, remainByte, bytes);
                                if (remainByte == wifiStreamLen) {
                                    Log.e("【状态】", "一次性长度一致");
                                    System.arraycopy(tempBuffer, 0, wave, 0, wifiStreamLen);

                                    int[] wifiWave = new int[wifiStreamLen];
                                    for (int i = 0; i < wifiStreamLen; i++) {
                                        //将字节数组转变为int数组
                                        wifiWave[i] = wave[i] & 0xff;
                                    }
                                    SendWaveMessage(wifiWave);


                                    remainByte = 0;
                                    processedByte = 0;
                                    needAddData = false;
                                    break;
                                }
                                //不一致要继续接受数据
                                else {
                                    System.arraycopy(buffer, 0, tempBuffer, remainByte, bytes);
                                    remainByte += bytes;
                                    Log.e("【状态】", "需要：" + wifiStreamLen + ",当前：" + remainByte + ",补全数据ing....");
                                    //补全数据后，如果长度相等，则不需要继续补全
                                    if (remainByte == wifiStreamLen) {
                                        int[] wifiWave = new int[wifiStreamLen];
                                        for (int i = 0; i < wifiStreamLen; i++) {
                                            //将字节数组转变为int数组
                                            wifiWave[i] = tempBuffer[i] & 0xff;
                                        }
                                        SendWaveMessage(wifiWave);
                                        remainByte = 0;
                                        processedByte = 0;
                                        needAddData = false;
                                    }

                                    //printBuffer("拼接波形数据", tempBuffer);
                                    break;
                                }
                            }
                            //数据头部不适正常数据的，如中断接受的波形数据，在这里处理
                            else {
                                //此处不容易测试，可能会有bug，需要时间调试。
                                Log.e("【容错处理】", "进入容错程序");
                                break;

                            }
                        }
                    }
                }
            }
        } catch (
                IOException e) {
            handler.sendEmptyMessage(ConnectService.DEVICE_RECONNECT);
            e.printStackTrace();
        }
    }*/
    //******************************************************************************************************************



/*    @Override
    public void run() {
        if (socket == null) {
            return;
        }
        handler.sendEmptyMessage(ModeActivity.DEVICE_CONNECTED);
        try {
            //获取数据流
            InputStream inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();


            int bytes = 0;
            byte[] buffer = new byte[65565 * 9];
            byte[] command = new byte[65565 * 9];
            byte[] wave = new byte[65565 * 9];

            */

    /**
     * 变更接收数据处理的方式
     * 不再使用是否是命令的判断
     * 循环接收数据，判断接收的字节数，可能是8、16、24或者更大的数字，8、16、24一般为命令数据
     * 较大的数字一般为波形数据，但波形数据有可能加载着命令数据，需要处理粘包
     *//*
            while (true) {
                bytes = inputStream.read(buffer);
                printBuffer(buffer);
                Log.e("【接收数据长度】", "" + bytes);
                if (bytes == 8) {
                    System.arraycopy(buffer, 0, command, 0, bytes);
                    //需要传递的命令数据
                    int[] wifiCommand = new int[8];
                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);

                } else if (bytes == 16) {
                    System.arraycopy(buffer, 0, command, 0, bytes);
                    //需要传递的命令数据
                    int[] wifiCommand = new int[8];
                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);

                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i + 8] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);


                } else if (bytes == 24) {
                    System.arraycopy(buffer, 0, command, 0, bytes);
                    //需要传递的命令数据
                    int[] wifiCommand = new int[8];
                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);


                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i + 8] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);


                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i + 16] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);


                }
                else if (bytes == 32) {
                    System.arraycopy(buffer, 0, command, 0, bytes);
                    //需要传递的命令数据
                    int[] wifiCommand = new int[8];
                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);


                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i + 8] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);


                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i + 16] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);

                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i + 24] & 0xff;
                    }
                    SendCmdMessage(wifiCommand);



                } else if (bytes > 32) {
                    System.arraycopy(buffer, 0, wave, readCount, bytes);
                    readCount += bytes;
                    Log.e("【Read&Need】", "" + readCount + "//" + "" + wifiStreamLen);

                    if (readCount == wifiStreamLen) {
                        //需要传递的波形数据
                        int[] wifiWave = new int[wifiStreamLen];
                        for (int i = 0; i < wifiStreamLen; i++) {
                            //将字节数组转变为int数组
                            wifiWave[i] = wave[i] & 0xff;
                        }
                        SendWaveMessage(wifiWave);
                        //波形数据收取完成，准备收取command
                        readCount = 0;
                    } else if (readCount - wifiStreamLen == 8) {
                        int[] wifiCommand = new int[8];
                        for (int i = 0; i < 8; i++) {
                            //将字节数组转变为int数组
                            wifiCommand[i] = wave[i] & 0xff;
                        }
                        SendCmdMessage(wifiCommand);

                        int[] wifiWave = new int[wifiStreamLen];
                        for (int i = 0; i < wifiStreamLen; i++) {
                            //将字节数组转变为int数组
                            wifiWave[i] = wave[i + 8] & 0xff;
                        }
                        SendWaveMessage(wifiWave);
                        //波形数据收取完成，准备收取command
                        readCount = 0;
                    } else if (readCount - wifiStreamLen == 16) {
                        int[] wifiCommand = new int[8];
                        for (int i = 0; i < 8; i++) {
                            //将字节数组转变为int数组
                            wifiCommand[i] = wave[i] & 0xff;
                        }
                        SendCmdMessage(wifiCommand);

                        for (int i = 0; i < 8; i++) {
                            //将字节数组转变为int数组
                            wifiCommand[i] = wave[i + 8] & 0xff;
                        }
                        SendCmdMessage(wifiCommand);

                        int[] wifiWave = new int[wifiStreamLen];
                        for (int i = 0; i < wifiStreamLen; i++) {
                            //将字节数组转变为int数组
                            wifiWave[i] = wave[i + 16] & 0xff;
                        }
                        SendWaveMessage(wifiWave);
                        //波形数据收取完成，准备收取command
                        readCount = 0;
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }



*//*            while (true) {
                Log.e("【isCommand】", String.valueOf(isCommand));
                if (isCommand) {
                    while (true) {
                        bytes = inputStream.read(buffer);
                        Log.e("【命令数据长度】", String.valueOf(bytes));
                        System.arraycopy(buffer, 0, command, 0, bytes);
                        if (bytes > 8) {
                            //G?  先简化逻辑，处理脉冲电流命令波形数据掺杂在一起
                            isCommand = false;
                            System.arraycopy(command, 8, wave, 0, bytes - 8);
                            readCount += bytes - 8;
                            Log.e("【命令数据中发现波形数据】", "" + bytes);
                            break;
                        }
                        if (bytes == 8) {
                            break;
                        }

                    }

                    //需要传递的命令数据
                    int[] wifiCommand = new int[8];
                    for (int i = 0; i < 8; i++) {
                        //将字节数组转变为int数组
                        wifiCommand[i] = command[i] & 0xff;
                    }
                    //接收到“收取数据”命令后，准备接受波形数据
                    if ((wifiCommand[5] == COMMAND_RECEIVE_WAVE) && (wifiCommand[6] == RECEIVE_RIGHT)) {
                        isCommand = false;
                    }
                    Message message = Message.obtain();
                    message.what = ModeActivity.GET_COMMAND;
                    Bundle bundle = new Bundle();
                    bundle.putIntArray("CMD", wifiCommand);
                    message.setData(bundle);
                    handler.sendMessage(message);
                    Log.e("设备-->APP", " 指令：" + wifiCommand[5] + " 传输数据：" + wifiCommand[6]);

                } else {
                    //GC20190706 数据处理优化
                    Log.e("【开始处理时Read/Neadlen】", "" + readCount + "/" + wifiStreamLen);

                    while (true) {
                        //每次收取的wifi数据流长度
                        bytes = inputStream.read(buffer);
                        Log.e("【非命令数据长度】", String.valueOf(bytes));
                        //先缓存wifi数据流至data（避免数据过多时缓存不够大造成SIM64Km收数不全）
                        //TODO 波形数据中遇到命令信息的处理
                        if (bytes == 8) {
                            System.arraycopy(buffer, 0, command, 0, bytes);
                            //需要传递的命令数据
                            int[] wifiCommand = new int[8];
                            for (int i = 0; i < 8; i++) {
                                //将字节数组转变为int数组
                                wifiCommand[i] = command[i] & 0xff;
                            }
                            //接收到“收取数据”命令后，准备接受波形数据
                            if ((wifiCommand[5] == COMMAND_RECEIVE_WAVE) && (wifiCommand[6] == RECEIVE_RIGHT)) {
                                isCommand = false;
                            }
                            Message message = Message.obtain();
                            message.what = ModeActivity.GET_COMMAND;
                            Bundle bundle = new Bundle();
                            bundle.putIntArray("CMD", wifiCommand);
                            message.setData(bundle);
                            handler.sendMessage(message);
                            Log.e("（波形中发现命令信息）设备-->APP", " 指令：" + wifiCommand[5] + " 传输数据：" + wifiCommand[6]);
                        } else if (bytes > 8) {
                            Log.i("WAVE", " 实际收取:" + bytes);
                            System.arraycopy(buffer, 0, wave, readCount, bytes);
                            readCount += bytes;
                        }
                        Log.e("【循环读取后Read/Neadlen】", "" + readCount + "/" + wifiStreamLen);
                        if (readCount >= wifiStreamLen)
                            break;
                    }
                    *//**//*     } while (readCount != wifiStreamLen);*//**//*

                    //需要传递的波形数据
                    int[] wifiWave = new int[wifiStreamLen];
                    for (int i = 0; i < wifiStreamLen; i++) {
                        //将字节数组转变为int数组
                        wifiWave[i] = wave[i] & 0xff;
                    }
                    Message message = Message.obtain();
                    message.what = ModeActivity.GET_WAVE;
                    Bundle bundle = new Bundle();
                    bundle.putIntArray("WAVE", wifiWave);
                    message.setData(bundle);
                    handler.sendMessage(message);

                    //波形数据收取完成，准备收取command
                    readCount = 0;
                    isCommand = true;
                }
            }*//*

        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }*/
    private void printBuffer(String tag, byte[] buffer) {
        int[] bufferData = new int[buffer.length];
        for (int i = 0; i < wifiStreamLen; i++) {
            //将字节数组转变为int数组
            bufferData[i] = buffer[i] & 0xff;
        }
        //Log.e("【全部数据】【" + tag + "】", Arrays.toString(bufferData));
        Log.e("【全部数据】【" + tag + "】", "暂时隐藏");

    }

    /**
     * 命令发送处理
     */
    public void sendCommand(byte[] request) {
        if (outputStream != null) {
            try {
                outputStream.write(request);
                //读取方式范围
                if (request[5] == COMMAND_MODE) {
                    switch (request[6]) {
                        case (byte) TDR:
                            mode = TDR;
                            break;
                        case (byte) ICM:
                            mode = ICM;
                            break;
                        case (byte) SIM:
                            mode = SIM;
                            break;
                        case (byte) DECAY:
                            mode = DECAY;
                            break;
                        case (byte) ICM_DECAY:
                            mode = ICM_DECAY;
                            break;
                        default:
                            break;
                    }
                    //接收数据个数选择
                    selectWifiStreamLength();
                } else if (request[5] == COMMAND_RANGE) {
                    switch (request[6]) {
                        case (byte) RANGE_250:
                            range = 0;
                            break;
                        case (byte) RANGE_500:
                            range = 1;
                            break;
                        case (byte) RANGE_1_KM:
                            range = 2;
                            break;
                        case (byte) RANGE_2_KM:
                            range = 3;
                            break;
                        case (byte) RANGE_4_KM:
                            range = 4;
                            break;
                        case (byte) RANGE_8_KM:
                            range = 5;
                            break;
                        case (byte) RANGE_16_KM:
                            range = 6;
                            break;
                        case (byte) RANGE_32_KM:
                            range = 7;
                            break;
                        case (byte) RANGE_64_KM:
                            range = 8;
                            break;
                        default:
                            break;
                    }
                    //接收数据个数选择
                    selectWifiStreamLength();
                }
                Message message = Message.obtain();
                message.what = ModeActivity.SEND_SUCCESS;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                Message message = Message.obtain();
                message.what = ModeActivity.SEND_ERROR;
                handler.sendMessage(message);
            }
        }
    }

    /**
     * 根据方式、范围选取判断收取波形数据的点数
     */
    private void selectWifiStreamLength() {
        if (mode == TDR) {
            wifiStreamLen = READ_TDR_SIM[range] + 9;
        } else if ((mode == ICM) || (mode == ICM_DECAY) || (mode == DECAY)) {
            wifiStreamLen = READ_ICM_DECAY[range] + 9;
        } else if (mode == SIM) {
            wifiStreamLen = (READ_TDR_SIM[range] + 9) * 9;
        }
        Log.i("WAVE", " 需要绘制:" + wifiStreamLen);
    }

}
