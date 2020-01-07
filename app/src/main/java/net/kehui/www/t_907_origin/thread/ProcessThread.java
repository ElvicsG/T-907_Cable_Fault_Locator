package net.kehui.www.t_907_origin.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.kehui.www.t_907_origin.ConnectService;
import net.kehui.www.t_907_origin.application.Constant;
import net.kehui.www.t_907_origin.view.ModeActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import static net.kehui.www.t_907_origin.base.BaseActivity.COMMAND_MODE;
import static net.kehui.www.t_907_origin.base.BaseActivity.COMMAND_RANGE;
import static net.kehui.www.t_907_origin.base.BaseActivity.DECAY;
import static net.kehui.www.t_907_origin.base.BaseActivity.ICM;
import static net.kehui.www.t_907_origin.base.BaseActivity.ICM_DECAY;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_16_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_1_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_250;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_2_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_32_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_4_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_500;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_64_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.RANGE_8_KM;
import static net.kehui.www.t_907_origin.base.BaseActivity.READ_ICM_DECAY;
import static net.kehui.www.t_907_origin.base.BaseActivity.READ_TDR_SIM;
import static net.kehui.www.t_907_origin.base.BaseActivity.SIM;
import static net.kehui.www.t_907_origin.base.BaseActivity.TDR;

/**
 * 消费者线程，开启后，循环从队列中取数据，按照数据类型，做不同处理
 */
public class ProcessThread extends Thread {

    private Handler handler;


    public ProcessThread(Handler handler) {
        setName("ConnectThread");
        this.handler = handler;

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

        Log.e("【波形数据处理】", "正常包：" + waveData[3]);
        //e("【波形包数据】", Arrays.toString(waveData));

        Message message = Message.obtain();
        message.what = ModeActivity.GET_WAVE;
        Bundle bundle = new Bundle();
        bundle.putIntArray("WAVE", waveData);
        message.setData(bundle);
        handler.sendMessage(message);
        //Log.e("【波形数据处理】", "正常包：" + Arrays.toString(waveData));
    }

    public static void e(String tag, String msg) {  //信息太长,分段打印
        //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
        //  把4*1024的MAX字节打印长度改为2001字符数
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (msg.length() > max_str_length) {
            Log.i(tag, msg.substring(0, max_str_length));
            msg = msg.substring(max_str_length);
        }
        //剩余部分
        Log.i(tag, msg);
    }

    /**
     * 处理队列数据，循环执行。
     */
    @Override
    public void run() {

        while (true) {
            if (ConnectService.bytesDataQueue.size() > 0) {
                try {
                    int byteLength = 0;
                    byte[] bytesItem = (byte[]) ConnectService.bytesDataQueue.take();
                    byteLength = bytesItem.length;
                    //普通命令
                    if (byteLength == 8) {
                        int[] normalCommand = new int[8];
                        for (int i = 0; i < 8; i++) {
                            //将字节数组转变为int数组
                            normalCommand[i] = bytesItem[i] & 0xff;
                        }
                        SendCmdMessage(normalCommand);
                    }
                    //电量命令
                    else if (byteLength == 9) {
                        int[] powerCommand = new int[9];
                        for (int i = 0; i < 9; i++) {
                            //将字节数组转变为int数组
                            powerCommand[i] = bytesItem[i] & 0xff;
                        }
                        SendCmdMessage(powerCommand);
                    }
                    //波形数据
                    else {
                        //Log.e("【时效测试】", "接收完数据");

                        int[] waveData = new int[byteLength];

                        for (int i = 0; i < byteLength; i++) {
                            //将字节数组转变为int数组
                            waveData[i] = bytesItem[i] & 0xff;
                        }
                        //Log.e("【时效测试】", "处理完数据");

                        SendWaveMessage(waveData);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //10毫秒休息，降低CPU使用率
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
