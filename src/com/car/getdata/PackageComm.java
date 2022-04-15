package com.car.getdata;

import com.car.dao.EnvironmentInfoDao;
import com.car.dao.imp.EnvironmentInfoDaoImp;
import com.car.pojo.EnvironmentInfo;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.TooManyListenersException;

/**
 * @author jhyang
 * @create 2021-05-29 23:42
 */
public class PackageComm implements SerialPortEventListener,Runnable {

    private static final byte start_1 = 0x0A;

    private static final byte start_2 = 0x0A;

    private static final byte end_1 = (byte)0xAA;

    private static final byte end_2 = (byte)0xAB;

    private static final byte end_3 = (byte)0xAC;

    private CommPortIdentifier portIdentifier;

    private int delayRead = 100;

    // private int numBytes;

    private static byte[] readBuffer = new byte[1024]; // 4k��buffer�ռ�,���洮�ڶ��������

    private InputStream inputStream;

    private OutputStream outputStream;

    private SerialPort serialPort;

    private HashMap serialParams;

    private static Boolean isOpen = false;

    public static final String PARAMS_DELAY = "delay read"; // ��ʱ�ȴ��˿�����׼����ʱ��

    public static final String PARAMS_TIMEOUT = "timeout"; // ��ʱʱ��

    public static final String PARAMS_PORT = "port name"; // �˿�����

    public static final String PARAMS_DATABITS = "data bits"; // ����λ

    public static final String PARAMS_STOPBITS = "stop bits"; // ֹͣλ

    public static final String PARAMS_PARITY = "parity"; // ��żУ��

    public static final String PARAMS_RATE = "rate"; // ������

    private byte[] length = new byte[2];

    private byte[] busId = new byte[4];

    private byte busStatus;

    private byte busType;

    private byte[] busTemperate = new byte[2];

    private byte[] personaltemperate = new byte[2];
    private byte[] smoke = new byte[2];

    private byte[] longitude  = new byte[4];

    private byte[] latitude = new byte[4];

    private byte[] lightStrength = new byte[2];

    private byte light_one;

    private byte light_two;

    private byte light_three;

    private byte light_four;

    private byte light_one_status;

    private byte light_two_status;

    private byte light_three_status;

    private byte light_four_status;

    private byte[] rssi = new byte[2];
    private byte[] crc = new byte[4];
    private String sensor_id_inner;

    private byte[] envirtemp = new byte[2];
    private byte[] ultra_distance = new byte[2];

    public boolean validateflag = false;

//    public CarHouse carHouse;

    public EnvironmentInfo environmentInfo;

    public PackageComm(){
        if(isOpen){
            close();
        }
        try {
            portIdentifier = CommPortIdentifier.getPortIdentifier("COM3");
            serialPort = (SerialPort)portIdentifier.open("send", 5000);
            serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);

        } catch (NoSuchPortException e) {
            // TODO Auto-generated catch block
            System.out.println("No such PortException");
        } catch (PortInUseException e) {
            // TODO Auto-generated catch block
            System.out.println("This Port is used");
        } catch (UnsupportedCommOperationException e) {
            // TODO Auto-generated catch block
            System.out.println("This Control Is Not Support");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("This Port Open Wrong");
        } catch (TooManyListenersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public PackageComm(HashMap hashMap){
        if(isOpen){
            this.close();
        }
        serialParams = hashMap;
        int timeout = Integer.parseInt(serialParams.get(PARAMS_TIMEOUT).toString());
        int rate = Integer.parseInt(serialParams.get(PARAMS_RATE).toString());
        int dataBits = Integer.parseInt(serialParams.get(PARAMS_DATABITS).toString());
        int stopBits = Integer.parseInt(serialParams.get(PARAMS_STOPBITS).toString());
        int parity = Integer.parseInt(serialParams.get(PARAMS_PARITY).toString());
        String port = serialParams.get(PARAMS_PORT).toString();

        try {
            portIdentifier = CommPortIdentifier.getPortIdentifier(port);
            serialPort = (SerialPort)portIdentifier.open("portComm1", timeout);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            serialPort.setSerialPortParams(rate, dataBits, stopBits, parity);
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            isOpen = true;
        } catch (NoSuchPortException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (PortInUseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TooManyListenersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedCommOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void close(){
        if(isOpen){
            serialPort.notifyOnDataAvailable(false);
            serialPort.removeEventListener();
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.out.print("CommPort Close Error");
            }
            isOpen = false;
        }
    }

    public void start(){
        Thread readThread = new Thread(this);
        readThread.start();
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        // TODO Auto-generated method stub
        try{
            Thread.sleep(delayRead);
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        switch (event.getEventType()){
            case SerialPortEvent.BI:
                System.out.println("通讯中断");
                break;
            case SerialPortEvent.OE:
                System.out.println("溢位错误");
                break;
            case SerialPortEvent.FE:
                System.out.println("帧错误");
                break;
            case SerialPortEvent.PE:
                System.out.println("奇偶检验错误");
                break;
            case SerialPortEvent.CD:
                System.out.println("载波侦听");
                break;
            case SerialPortEvent.CTS:
                System.out.println("清除发送");
                break;
            case SerialPortEvent.DSR:
                System.out.println("数据设备准备好");
                break;
            case SerialPortEvent.RI:
                System.out.println("响铃侦测 ");
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:

                try{
                    /*�Ӵ��ڽ���*/
                    while(inputStream.available()>0){
                        byte[] str = new byte[41];
                        inputStream.read(str);
                        if (str[0] == 0x0A) {

                            environmentInfo_bytesToData(str);

                        }else {
                        }

                        if (environmentInfo != null ) {//�õ���������Ч

                            EnvironmentInfoDao environmentInfoDao = new EnvironmentInfoDaoImp();

                            environmentInfoDao.updateEnvironmentInfo(environmentInfo);

                        }
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
                break;
        }
    }

    public void sendToComm(boolean lightone,boolean lighttwo,boolean lightthree){
        byte[] sendInfo = new byte[21];
        sendInfo[0] = start_1;
        sendInfo[1] = start_2;
        sendInfo[2] = 0x00;
        sendInfo[3] = 0x00;
        sendInfo[4]=0x01;
        sendInfo[5]=0x00;
        sendInfo[6]=0x00;
        sendInfo[7]=0x00;

        if (lightone) {
            sendInfo[8] = 0x01;
            sendInfo[9] = 0x01;
        }else{
            sendInfo[8] = 0x01;
            sendInfo[9] = 0x00;
        }
        if (lighttwo) {
            sendInfo[10] = 0x02;
            sendInfo[11] = 0x01;
        }else{
            sendInfo[10] = 0x02;
            sendInfo[11] = 0x00;
        }
        if (lightthree) {
            sendInfo[12] = 0x03;
            sendInfo[13] = 0x01;
        }else{
            sendInfo[12] = 0x03;
            sendInfo[13] = 0x00;
        }
        sendInfo[14] = 0x04;
        sendInfo[15] = 0x01;

        sendInfo[16] = 0x00;
        sendInfo[17] = 0x00;
        sendInfo[18] = end_1;
        sendInfo[19] = end_2;
        sendInfo[20] = end_3;
        run(sendInfo);
    }

    private EnvironmentInfo environmentInfo_bytesToData(byte[] data) {
        environmentInfo = null;
        validateflag = false;

        if(data[0]==start_1 && data[1] == start_2 && data[38] ==end_1&& data[39] ==end_2&& data[40] ==end_3)
        {
            validateflag=true;
            length[0] = data[2];
            length[1] = data[3];
            int length_i = bytesToShort(length);
            if(length_i == 32){
                environmentInfo =new EnvironmentInfo();

                double etemp = 0,humidity = 0;
                int distance = 0;

                envirtemp[0] = data[10];
                envirtemp[1] = data[11];
                etemp = (double)bytesToShort(envirtemp)/100;
                BigDecimal bg = new BigDecimal(etemp);
                etemp = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                System.out.println("环境温度" + etemp);

                humidity = (double)(oneByteToInt(data[29]));
                System.out.println("湿度" + humidity);

                ultra_distance[0] = data[30];
                ultra_distance[1] = data[31];
                distance = (int) bytesToInt(ultra_distance);
                System.out.println("超声波间距" + distance);

                environmentInfo.setTemp(etemp);
                environmentInfo.setHumidity(humidity);
                environmentInfo.setUltra_distance(distance);

            }else{
                System.out.println("Can't receive for wrong length!");
                for(int i = 0 ; i < 41 ;i++){
                    System.out.println(byteToHex(data[2]));
                }
            }
        }else{
            System.out.println("Wrong Start Or Wrong End!");
        }

        return environmentInfo;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Waiting For Read");
    }

    public void run(byte[] data){
        try{
            byte[] sendData = new byte[1];
            for(int i = 0 ; i < data.length ; i++){
                sendData[0] = data[i];
                outputStream.write(sendData);
                System.out.print(byteToHex(sendData[0])+" ");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String byteToHex(byte b) {
        int i = b & 0xFF;//& 0xFF作用：&0xff可以将高的24位置为0，低8位保持原样。这样做的目的就是为了保证二进制数据的一致性。
        return Integer.toHexString(i);
    }

    public int oneByteToInt(byte b){
        byte[] one_int =new byte[4];
        one_int[0] = b;
        one_int[1] = 0x00;
        one_int[2] = 0x00;
        one_int[3] = 0x00;
        return bytesToInt(one_int);
    }

    public short bytesToShort(byte[] bytes) {
        return (short) (((bytes[1] << 8) | bytes[0] & 0xff));
    }

    public static int bytesToInt(byte[] bytes) {
        return (int) ((((bytes[3] & 0xff) << 24)
                | ((bytes[2] & 0xff) << 16)//左移运算符<<使指定值的所有位都左移规定的次数，丢弃最高位，0补最低位
                | ((bytes[1] & 0xff) << 8) | ((bytes[0] & 0xff) << 0)));
    }

    public static void main(String[] args) {
        PackageComm packageComm = new PackageComm();
//        packageComm.start();
        packageComm.sendToComm(false,true,true);
    }

}
