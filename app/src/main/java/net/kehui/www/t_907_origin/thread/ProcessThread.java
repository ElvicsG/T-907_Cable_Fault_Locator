package net.kehui.www.t_907_origin.thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.kehui.www.t_907_origin.ConnectService;

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
                        int[] waveData = new int[byteLength];
                        for (int i = 0; i < byteLength; i++) {
                            waveData[i] = bytesItem[i] & 0xff;
                        }
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
    private void getCmdMessage(int[] cmdData) {
        Log.e("#【设备-->APP】", "指令：" + getCommandStr(cmdData[5]) + " 已应答");
        Message message = Message.obtain();
        message.what = ConnectService.GET_COMMAND;
        Bundle bundle = new Bundle();
        bundle.putIntArray("CMD", cmdData);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    /**
     * @param cmdStr    指令
     * @return  指令内容
     */
    private String getCommandStr(int cmdStr) {
        String returnStr = "";
        switch (cmdStr) {
            case 1:
                returnStr = "1 测试";
                break;
            case 2:
                returnStr = "2 模式";
                break;
            case 3:
                returnStr = "3 范围";
                break;
            case 4:
                returnStr = "4 增益";
                break;
            case 5:
                returnStr = "5 延时";
                break;
            case 6:
                returnStr = "6 电量";
                break;
            case 7:
                returnStr = "7 平衡";
                break;
            case 9:
                returnStr = "9 接受数据";
                break;
            case 10:
                returnStr = "10 脉宽";
                break;
            default:
                break;
        }
        return returnStr;
    }

    /**
     * 获取硬件设备返回的波形数据
     */
    private void getWaveMessage(int[] waveData) {
        Log.e("【波形数据处理】", "正常包：" + waveData[3] + " 波形数据头/处理结束啦！");
        Message message = Message.obtain();
        message.what = ConnectService.GET_WAVE;
        Bundle bundle = new Bundle();
        bundle.putIntArray("WAVE", waveData);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public static void e(String tag, String msg) {
        //信息太长,分段打印
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
