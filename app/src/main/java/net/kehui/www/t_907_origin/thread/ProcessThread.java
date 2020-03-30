package net.kehui.www.t_907_origin.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.kehui.www.t_907_origin.ConnectService;
import net.kehui.www.t_907_origin.view.ModeActivity;

/**
 * 消费者线程，开启后，循环从队列中取数据，按照数据类型，做不同处理
 * @author 34238
 */
public class ProcessThread extends Thread {

    private Handler handler;

    public ProcessThread(Handler handler) {
        setName("ConnectThread");
        this.handler = handler;

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
                    if (byteLength == 8) {
                        //普通命令
                        int[] normalCommand = new int[8];
                        for (int i = 0; i < 8; i++) {
                            //将字节数组转变为int数组
                            normalCommand[i] = bytesItem[i] & 0xff;
                        }
                        getCmdMessage(normalCommand);
                    } else if (byteLength == 9) {
                        //电量命令
                        int[] powerCommand = new int[9];
                        for (int i = 0; i < 9; i++) {
                            powerCommand[i] = bytesItem[i] & 0xff;
                        }
                        getCmdMessage(powerCommand);
                    } else {
                        //波形数据
                        //Log.e("【时效测试】", "接收完数据");
                        int[] waveData = new int[byteLength];
                        for (int i = 0; i < byteLength; i++) {
                            waveData[i] = bytesItem[i] & 0xff;
                        }
                        //Log.e("【时效测试】", "处理完数据");
                        getWaveMessage(waveData);
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

    /**
     * 获取硬件设备返回的命令数据
     */
    private void getCmdMessage(int[] msgData) {
        //Log.e("【设备-->APP】", " 指令：" + msgData[5] + " 传输数据：" + msgData[6] + " 全部数据：" + Arrays.toString(msgData));
        Log.e("【设备-->APP】", " 指令：" + msgData[5] + " 传输数据：" + msgData[6]);
        Message message = Message.obtain();
        message.what = ConnectService.GET_COMMAND;
        Bundle bundle = new Bundle();
        bundle.putIntArray("CMD", msgData);
        message.setData(bundle);
        handler.sendMessage(message);
        //GC20200317
        ConnectService.canAskPower = true;
    }

    /**
     * 获取硬件设备返回的波形数据
     */
    private void getWaveMessage(int[] waveData) {
        Log.e("【波形数据处理】", "正常包：" + waveData[3]);
        //e("【波形包数据】", Arrays.toString(waveData));
        Message message = Message.obtain();
        message.what = ConnectService.GET_WAVE;
        Bundle bundle = new Bundle();
        bundle.putIntArray("WAVE", waveData);
        message.setData(bundle);
        handler.sendMessage(message);
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

}
