package com.hsbc;


/**
 * @Description : 数据读取线程
 * @Author  Jims
 * @Date   2020/3/27
 * @Param
 * @Return
 * @Exception
 */
public class ReadDataThread extends Thread{

    /* 共享数据
     */
    private final PaymentRecordData shareData;

    public ReadDataThread(PaymentRecordData shareData) {
        this.shareData = shareData;
    }

    @Override
    public void run() {
        try{
            while (true){
                String data = shareData.read();
                System.out.println(" Payment Record:  " + data); //输出当前数据
                sleep(10000);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

