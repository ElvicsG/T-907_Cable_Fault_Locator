package net.kehui.www.t_907_origin.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import net.kehui.www.t_907_origin.ConnectService;
import net.kehui.www.t_907_origin.base.BaseActivity;
import net.kehui.www.t_907_origin.view.ModeActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import static net.kehui.www.t_907_origin.base.BaseActivity.TDR;
import static net.kehui.www.t_907_origin.base.BaseActivity.ICM;
import static net.kehui.www.t_907_origin.base.BaseActivity.ICM_DECAY;
import static net.kehui.www.t_907_origin.base.BaseActivity.SIM;
import static net.kehui.www.t_907_origin.base.BaseActivity.DECAY;
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
        handler.sendEmptyMessage(ConnectService.DEVICE_CONNECTED);
        try {
            //获取数据流
            InputStream inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            int bytesread;
            int bytes;

            byte[] buffer = new byte[65565 * 9];
            byte[] tempBuffer = new byte[65565 * 9 + 18];
            int remainByte = 0;
            int processedByte = 0;
            int mimProcessedDataLen = 0;
            boolean needAddData = false;
            boolean needProcessMIMData = false;

            //TODO 20191220 重新写收设备数据的逻辑
            while (true) {
                Arrays.fill(buffer, (byte) 0);
                bytesread = inputStream.read(buffer);
                /*if (bytesread == -1) {
                    handler.sendEmptyMessage(ConnectService.DEVICE_DO_CONNECT);
                    break;
                }*/ //G?? 没有用么
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
                        //将读到的数据放到缓存数组
                        if (remainByte == 0 && !needAddData) {
                            //如果剩余要处理的为0则重新赋值
                            Arrays.fill(tempBuffer, (byte) 0);
                            System.arraycopy(buffer, 0, tempBuffer, 0, bytes);
                        }
                        //处理缓存数组
                        if ((tempBuffer[0] & 0xff) != 235 && !needAddData && !needProcessMIMData) {
                            //判断为无效头数据时，直接丢掉
                            Log.e("【新数据处理】", "无效头数据");
                            break;
                        } else {
                            if ((tempBuffer[3] & 0xff) == 85 && (tempBuffer[4] & 0xff) == 3) {
                                //普通命令，截8个
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
                                } else {
                                    //将未处理的字节数组前移   //没处理完，可能有命令或者波形数据需要处理，继续
                                    byte[] convertBuffer = new byte[remainByte];
                                    System.arraycopy(tempBuffer, 8, convertBuffer, 0, remainByte);
                                    Arrays.fill(tempBuffer, (byte) 0);
                                    System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte);
                                    continue;   //G??   可以去掉不
                                }
                            } else if ((tempBuffer[3] & 0xff) == 85 && (tempBuffer[4] & 0xff) == 4) {
                                //电量命令，截9个
                                Log.e("【新数据处理】", "电池命令");
                                byte[] powerBytes = new byte[9];
                                System.arraycopy(tempBuffer, 0, powerBytes, 0, 9);
                                //加入队列
                                ConnectService.bytesDataQueue.put(powerBytes);
                                //已经处理过的字节数累加8
                                processedByte += 9;
                                //剩余字节数减9
                                remainByte = bytes - processedByte;
                                //如果剩余字节数为0，跳出循环，不需要继续处理,继续接收数据。
                                if (remainByte == 0) {
                                    processedByte = 0;
                                    break;
                                } else {
                                    //将未处理的字节数组前移  //没处理完，可能有命令或者波形数据需要处理，继续
                                    byte[] convertBuffer = new byte[remainByte];
                                    System.arraycopy(tempBuffer, 9, convertBuffer, 0, remainByte);
                                    Arrays.fill(tempBuffer, (byte) 0);
                                    System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte);
                                    continue;
                                }
                            } else if ((tempBuffer[3] & 0xff) == 102 && !needAddData) {
                                //波形数据，截需要的长度，不够要拼数据
                                //Log.e("【时效测试】", "开始接收波形数据");
                                //如果不是从处理中数据过来的，初始化为当前接受的数据，也就是直接是波形数据的。
                                if (remainByte == 0) {
                                    remainByte = bytes;
                                }
                                if (remainByte == wifiStreamLen) {
                                    //如果剩余波形长度和需要的长度一致
                                    Log.e("【新数据处理】", "一次性长度一致，不用补数据");
                                    byte[] waveBytes = new byte[remainByte];
                                    System.arraycopy(tempBuffer, 0, waveBytes, 0, wifiStreamLen);
                                    ConnectService.bytesDataQueue.put(waveBytes);
                                    remainByte = 0;
                                    processedByte = 0;
                                    needAddData = false;
                                    break;
                                } else if(remainByte > wifiStreamLen) {
                                    //大于需要个数的处理 //GC20200408
                                    Log.e("【新数据处理】", "混杂命令了！");
                                    byte[] waveBytes = new byte[wifiStreamLen];
                                    System.arraycopy(tempBuffer, 0, waveBytes, 0, wifiStreamLen);
                                    ConnectService.bytesDataQueue.put(waveBytes);

                                    remainByte = 0;
                                    processedByte = 0;
                                    needAddData = false;
                                } else {
                                    //不一致要继续接受数据
                                    needAddData = true;
                                    Log.e("【新数据处理】", "数据不全，需要补全，需要：" + wifiStreamLen + ",当前：" + remainByte + ",补全数据ing....");
                                    break;
                                }
                            } else if ((tempBuffer[3] & 0xff) == 119 && !needAddData && !needProcessMIMData) {
                                //Log.e("【时效测试】", "开始接收波形数据");
                                if ((tempBuffer[3] & 0xff) == 119) {
                                    mimWifiStreamLen = wifiStreamLen / 9;
                                }
                                //如果不是从处理中数据过来的，初始化为当前接受的数据，也就是直接是波形数据的。
                                if (remainByte == 0) {
                                    remainByte = bytes;
                                }
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
                                        || (tempBuffer[3] & 0xff) == 255) & needProcessMIMData) {

                                    if (remainByte >= mimWifiStreamLen) {
                                        byte[] waveBytes = new byte[mimWifiStreamLen];
                                        System.arraycopy(tempBuffer, 0, waveBytes, 0, mimWifiStreamLen);
                                        ConnectService.bytesDataQueue.put(waveBytes);
                                        Log.e("【MIM】", "入库：" + (waveBytes[3] & 0xff));
                                        remainByte -= mimWifiStreamLen;
                                        mimProcessedDataLen += mimWifiStreamLen;

                                        if (remainByte == 0 && mimProcessedDataLen == wifiStreamLen) {
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

                                } else if (((tempBuffer[3] & 0xff) == 119
                                        || (tempBuffer[3] & 0xff) == 136
                                        || (tempBuffer[3] & 0xff) == 153
                                        || (tempBuffer[3] & 0xff) == 170
                                        || (tempBuffer[3] & 0xff) == 187
                                        || (tempBuffer[3] & 0xff) == 204
                                        || (tempBuffer[3] & 0xff) == 221
                                        || (tempBuffer[3] & 0xff) == 238
                                        || (tempBuffer[3] & 0xff) == 255) & needAddData) {

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

                                } else {
                                    //数据头部不适正常数据的，如中断接受的波形数据，在这里处理
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
        } catch (IOException | InterruptedException e) {
            //EN20200324
            handler.sendEmptyMessage(ConnectService.DEVICE_DISCONNECTED);
            handler.sendEmptyMessage(ConnectService.DEVICE_DO_CONNECT);
            Log.e("【SOCKET连接】", "socket异常，重连。");
            e.printStackTrace();
        }
    }

    /**
     * APP下发命令
     */
    public void sendCommand(byte[] request) {
        if (outputStream != null) {
            try {
                outputStream.write(request);
                //读取方式范围
                if (request[5] == BaseActivity.COMMAND_MODE) {
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
                } else if (request[5] == BaseActivity.COMMAND_RANGE) {
                    switch (request[6]) {
                        case (byte) BaseActivity.RANGE_250:
                            range = 0;
                            break;
                        case (byte) BaseActivity.RANGE_500:
                            range = 1;
                            break;
                        case (byte) BaseActivity.RANGE_1_KM:
                            range = 2;
                            break;
                        case (byte) BaseActivity.RANGE_2_KM:
                            range = 3;
                            break;
                        case (byte) BaseActivity.RANGE_4_KM:
                            range = 4;
                            break;
                        case (byte) BaseActivity.RANGE_8_KM:
                            range = 5;
                            break;
                        case (byte) BaseActivity.RANGE_16_KM:
                            range = 6;
                            break;
                        case (byte) BaseActivity.RANGE_32_KM:
                            range = 7;
                            break;
                        case (byte) BaseActivity.RANGE_64_KM:
                            range = 8;
                            break;
                        default:
                            break;
                    }
                    //接收数据个数选择
                    selectWifiStreamLength();
                }
            } catch (IOException e) {
                e.printStackTrace();
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

    private void printBuffer(String tag, byte[] buffer) {
        int[] bufferData = new int[buffer.length];
        for (int i = 0; i < wifiStreamLen; i++) {
            //将字节数组转变为int数组
            bufferData[i] = buffer[i] & 0xff;
        }
        //Log.e("【全部数据】【" + tag + "】", Arrays.toString(bufferData));
        Log.e("【全部数据】【" + tag + "】", "暂时隐藏");

    }

}
