package net.kehui.www.t_907_origin.thread;

import android.os.Handler;
import android.util.Log;

import net.kehui.www.t_907_origin.ConnectService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import static net.kehui.www.t_907_origin.application.Constant.needAddData;
import static net.kehui.www.t_907_origin.application.Constant.waveLen;
import static net.kehui.www.t_907_origin.application.Constant.waveSimLen;

/**
 * @author Gong
 * @date 2019/07/15
 */
public class ConnectThread extends Thread {

    private final Socket socket;
    private Handler handler;
    private String ip;
    private OutputStream outputStream;

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
     * 循环接收数据，判断数据头，区分是命令还是波形。
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
            int bytesRead;
            int bytes;

            byte[] buffer = new byte[65565 * 9];
            byte[] tempBuffer = new byte[65565 * 9 + 18];
            int remainByte = 0;
            int processedByte = 0;
            int mimProcessedDataLen = 0;
//            boolean needAddData = false;  //GC20200428
            boolean needProcessSimData = false;
            Log.e("【DEBUG】", "初始化" + needAddData);

            while (true) {
                //接收设备数据
                Arrays.fill(buffer, (byte) 0);
                bytesRead = inputStream.read(buffer);
                if (bytesRead == -1) {
                    handler.sendEmptyMessage(ConnectService.DEVICE_DO_CONNECT);
                    break;
                }
                Log.e("【新数据处理】", "接收设备数据，总量：" + bytesRead);
                if (bytesRead > 0) {
                    bytes = bytesRead;
                    while (true) {
                        try {
                            Thread.sleep(10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //读取数据放入缓存数组
                        if (remainByte == 0 && !needAddData) {
                            //判断数据处理完毕且不需要补齐数据
                            Arrays.fill(tempBuffer, (byte) 0);
                            System.arraycopy(buffer, 0, tempBuffer, 0, bytes);
                            Log.e("【新数据处理】", "读取数据放入缓存,长度：" + bytes);
                        }
                        //处理数据
                        if ((tempBuffer[0] & 0xff) != 235 && !needAddData && !needProcessSimData) {
                            //判断不是数据头：0xeb（235）；不需要补齐数据；不是处理SIM数据——认为无效
                            Log.e("【新数据处理】", "无效头数据，跳出");
                            break;
                        } else {
                            //判断数据头0xaa（170）——加深判断
                            //0x55（85），0x03——普通命令，截8个
                            if ((tempBuffer[2] & 0xff) == 170 && (tempBuffer[3] & 0xff) == 85 && (tempBuffer[4] & 0xff) == 3) {
                                byte[] cmdBytes = new byte[8];
                                System.arraycopy(tempBuffer, 0, cmdBytes, 0, 8);
                                //加入队列
                                ConnectService.bytesDataQueue.put(cmdBytes);
                                Log.e("【新数据处理】", "提取普通命令消息，加入处理队列");
                                //已经处理过的字节数累加8
                                processedByte += 8;
                                //剩余字节数减8
                                remainByte = bytes - processedByte;
                                if (remainByte == 0) {
                                    //剩余字节数为0，跳出循环，继续接收设备数据
                                    processedByte = 0;
                                    Log.e("【新数据处理】", "中断，无待处理数据，跳出循环继续接收数据");
                                    break;
                                } else {
                                    //没处理完，可能有命令或者波形数据需要处理，将未处理的字节数组前移
                                    byte[] convertBuffer = new byte[remainByte];
                                    System.arraycopy(tempBuffer, 8, convertBuffer, 0, remainByte);
                                    Arrays.fill(tempBuffer, (byte) 0);
                                    System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte);
                                    Log.e("【新数据处理】", "继续，有待处理消息，继续下次循环，长度：" + remainByte);
//                                    continue;     //GC?
                                }
                            }
                            //0x55（85），0x04——普通命令，截9个
                            else if ((tempBuffer[2] & 0xff) == 170 && (tempBuffer[3] & 0xff) == 85 && (tempBuffer[4] & 0xff) == 4) {
                                byte[] powerBytes = new byte[9];
                                System.arraycopy(tempBuffer, 0, powerBytes, 0, 9);
                                //加入队列
                                ConnectService.bytesDataQueue.put(powerBytes);
                                Log.e("【新数据处理】", "提取电量，加入处理队列");
                                //已经处理过的字节数累加8
                                processedByte += 9;
                                //剩余字节数减9
                                remainByte = bytes - processedByte;
                                if (remainByte == 0) {
                                    //剩余字节数为0，跳出循环，继续接收设备数据
                                    processedByte = 0;
                                    Log.e("【新数据处理】", "中断，无待处理数据，跳出循环继续接收数据");
                                    break;
                                } else {
                                    //没处理完，可能有命令或者波形数据需要处理，将未处理的字节数组前移
                                    byte[] convertBuffer = new byte[remainByte];
                                    System.arraycopy(tempBuffer, 9, convertBuffer, 0, remainByte);
                                    Arrays.fill(tempBuffer, (byte) 0);
                                    System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte);
                                    Log.e("【新数据处理】", "继续，有待处理消息，继续下次循环，长度：" + remainByte);
//                                    continue;     //GC?
                                }
                            }
                            //0x66（102），不需要补齐——非SIM波形数据；截需要的长度，不够要补齐数据
                            else if ((tempBuffer[2] & 0xff) == 170 && (tempBuffer[3] & 0xff) == 102 && !needAddData) {
                                if (remainByte == 0) {  //GC? 有必要有这个判断？
                                    //非处理中数据，初始化为当前接收的数据长度，也就是直接是波形数据的。
                                    remainByte = bytes;
                                }
                                //GC20200428
                                if (remainByte == waveLen) {
                                    //长度和需要的波形长度一致
                                    Log.e("【新数据处理】", "一次性长度一致，不用补齐数据");
                                    byte[] waveBytes = new byte[remainByte];
                                    System.arraycopy(tempBuffer, 0, waveBytes, 0, waveLen);
                                    ConnectService.bytesDataQueue.put(waveBytes);
                                    Log.e("【新数据处理】", "提取波形数据，加入处理队列");
                                    remainByte = 0;
                                    processedByte = 0;
                                    needAddData = false;
                                    Log.e("【新数据处理】", "中断，无待处理数据，跳出。");
                                    break;
                                } else {
                                    //不一致要继续接收设备数据
                                    needAddData = true;
                                    Log.e("【新数据处理】", "中断，数据不全，需要补全，需要：" + waveLen + ",当前：" + remainByte + ",补全数据ing....跳出");
                                    break;
                                }
                            }
                            //0x77（119），不需要补齐数据，不是处理SIM数据——SIM波形数据，截需要的长度，不够要补齐数据
                            else if ((tempBuffer[2] & 0xff) == 170 && (tempBuffer[3] & 0xff) == 119 && !needAddData && !needProcessSimData) {
                                if ((tempBuffer[3] & 0xff) == 119) {
                                    waveSimLen = waveLen / 9;
                                }
                                if (remainByte == 0) {//GC? 有必要有这个判断？
                                    //非处理中数据，初始化为当前接收的数据长度，也就是直接是波形数据的。
                                    remainByte = bytes;
                                }
                                if (remainByte >= waveSimLen) {
                                    //待处理字节数大于每段的长度，需要找出一段SIM数据，进行处理。
                                    needProcessSimData = true;
                                    Log.e("【新数据处理】", "继续，SIM有待处理数据继续循环，剩余：" + remainByte + "/SIM每段长度：" + waveSimLen);
//                                    continue;     //GC?
                                } else {
                                    needAddData = true;
                                    Log.e("【新数据处理】", "中断，SIM需要补全数据跳出循环，剩余：" + remainByte + "/SIM每段长度：" + waveSimLen);
                                    break;
                                }

                            }
                            //补齐数据
                            else {
                                //非SIM波形数据
                                if ((tempBuffer[3] & 0xff) == 102) {
                                    System.arraycopy(buffer, 0, tempBuffer, remainByte, bytes);
                                    remainByte += bytes;
                                    Log.e("【新数据处理】", "需要：" + waveLen + ",当前：" + remainByte + ",补齐数据ing....");
                                    //补全数据后，如果长度相等，则不需要继续补全
                                    if (remainByte == waveLen) {
                                        byte[] waveBytes = new byte[waveLen];
                                        System.arraycopy(tempBuffer, 0, waveBytes, 0, waveLen);
                                        ConnectService.bytesDataQueue.put(waveBytes);
                                        Log.e("【新数据处理】", "补齐结束，提取波形数据，加入处理队列");
                                        remainByte = 0;
                                        processedByte = 0;
                                        needAddData = false;
                                        Log.e("【新数据处理】", "中断，补全结束，跳出");
                                        break;

                                    } else if (remainByte > waveLen) {
                                        Log.e("【新数据处理】", "数据超长，取正确包，截取继续处理，可能是波形后跟了电量数据");
                                        //获取补全的波形放入队列
                                        byte[] correctBytes = new byte[waveLen];
                                        System.arraycopy(tempBuffer, 0, correctBytes, 0, waveLen);
                                        ConnectService.bytesDataQueue.put(correctBytes);
                                        Log.e("【新数据处理】", "数据超长，取正确包，放入队列。");

                                        //超出的波形重新放入缓存，继续处理。
                                        byte[] convertBuffer = new byte[remainByte - waveLen];
                                        System.arraycopy(tempBuffer, waveLen, convertBuffer, 0, remainByte - waveLen);
                                        Arrays.fill(tempBuffer, (byte) 0);
                                        System.arraycopy(convertBuffer, 0, tempBuffer, 0, remainByte - waveLen);

                                        //设置基础处理的几个参数
                                        bytes = remainByte - waveLen;
                                        remainByte = remainByte - waveLen;
                                        processedByte = 0;
                                        needAddData = false;Log.e("【新数据处理】", "继续，数据超长，继续，剩余：" + remainByte + "/需要：" + waveLen);
//                                        continue;     //GC?

                                    } else {
                                        int i = 0;Log.e("【新数据处理】", "中断，长度不够，继续补全，跳出，剩余：" + remainByte + "/需要：" + waveLen);
                                        break;
                                    }

                                }
                                //SIM波形数据（0x77-0xFF 共9段）
                                else if ((( tempBuffer[3] & 0xff) == 119 || (tempBuffer[3] & 0xff) == 136 || (tempBuffer[3] & 0xff) == 153
                                        || (tempBuffer[3] & 0xff) == 170 || (tempBuffer[3] & 0xff) == 187 || (tempBuffer[3] & 0xff) == 204
                                        || (tempBuffer[3] & 0xff) == 221 || (tempBuffer[3] & 0xff) == 238 || (tempBuffer[3] & 0xff) == 255)
                                        && needProcessSimData) {
                                    //剩余字节数为0切已经处理的字节长度等于需要处理的长度
                                    if (remainByte >= waveSimLen) {
                                        byte[] waveBytes = new byte[waveSimLen];
                                        System.arraycopy(tempBuffer, 0, waveBytes, 0, waveSimLen);
                                        ConnectService.bytesDataQueue.put(waveBytes);
                                        Log.e("【MIM】", "入库：" + (waveBytes[3] & 0xff));
                                        remainByte -= waveSimLen;
                                        mimProcessedDataLen += waveSimLen;

                                        //剩余字节数为0切已经处理的字节长度等于需要处理的长度
                                        if (remainByte == 0 && mimProcessedDataLen == waveLen) {
                                            processedByte = 0;
                                            mimProcessedDataLen = 0;
                                            needAddData = false;
                                            needProcessSimData = false;
                                            Log.e("【新数据处理】", "中断，剩余字节数为0，已经处理的字节长度等于需要处理的长度");
                                            break;
                                        } else if (remainByte > 0 && mimProcessedDataLen == waveLen) {
                                            //剩余字节长度大于0，已处理长度和需要处理长度一致，有多余数据
                                            remainByte = 0;
                                            processedByte = 0;
                                            mimProcessedDataLen = 0;
                                            needAddData = false;
                                            needProcessSimData = false;
                                            Log.e("【新数据处理】", "中断，剩余字节长度大于0，已处理长度和需要处理长度一致，有多余数据");
                                            break;
                                        } else if (remainByte == 0 && mimProcessedDataLen < waveLen) {
                                            //待处理为0，但没收完。
                                            needAddData = true;
                                            needProcessSimData = false;
                                            Log.e("【新数据处理】", "跳出，长度不够，继续接受SIM数据，剩余：" + remainByte + "/需要：" + waveLen + "/已处理：" + mimProcessedDataLen);
                                            break;

                                        } else {
                                            byte[] convertBytes = new byte[remainByte];
                                            System.arraycopy(tempBuffer, waveSimLen, convertBytes, 0, remainByte);
                                            Arrays.fill(tempBuffer, (byte) 0);
                                            System.arraycopy(convertBytes, 0, tempBuffer, 0, remainByte);
                                            needProcessSimData = true;
                                            Log.e("【新数据处理】", "继续解析，剩余：" + remainByte + "/需要：" + waveSimLen + "/已处理：" + mimProcessedDataLen);
//                                            continue;     //GC?
                                        }

                                    } else {
                                        needAddData = true;
                                        needProcessSimData = false;
                                        Log.e("【新数据处理】", "跳出，长度不够，继续接受mim数据，剩余：" + remainByte + "/需要：" + waveSimLen + "/已处理：" + mimProcessedDataLen);
                                        break;
                                    }

                                }
                                else if ((( tempBuffer[3] & 0xff) == 119 || (tempBuffer[3] & 0xff) == 136 || (tempBuffer[3] & 0xff) == 153
                                        || (tempBuffer[3] & 0xff) == 170 || (tempBuffer[3] & 0xff) == 187 || (tempBuffer[3] & 0xff) == 204
                                        || (tempBuffer[3] & 0xff) == 221 || (tempBuffer[3] & 0xff) == 238 || (tempBuffer[3] & 0xff) == 255)
                                        && needAddData) {  //GC? 有必要有？

                                    System.arraycopy(buffer, 0, tempBuffer, remainByte, bytes);
                                    remainByte += bytes;

                                    if (remainByte >= waveSimLen) {
                                        needProcessSimData = true;
                                        needAddData = false;
                                        Log.e("【新数据处理】", "继续，SIM有待处理数据，继续，剩余：" + remainByte + "/需要：" + waveSimLen + "/已处理：" + mimProcessedDataLen);
//                                        continue;     //GC?

                                    } else {
                                        needAddData = true;
                                        needProcessSimData = false;
                                        Log.e("【新数据处理】", "中断，SIM需要补全，跳出，剩余：" + remainByte + "/需要：" + waveSimLen + "/已处理：" + mimProcessedDataLen);
                                        break;
                                    }

                                }
                                else {
                                    //数据头部不是正常数据的，如中断接受的波形数据，在这里处理
                                    //此处不容易测试，可能会有bug，需要时间调试。
                                    Log.e("【容错处理】", "进入容错程序");
                                    remainByte = 0;
                                    processedByte = 0;
                                    mimProcessedDataLen = 0;
                                    needAddData = false;
                                    needProcessSimData = false;
                                    Log.e("【新数据处理】", "中断，容错跳出");
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
