package com.hsbc;

import java.util.Scanner;


/**
 * @Description : 数据写入线程
 * @Author  Jims
 * @Date   2021/3/27
 * @Param
 * @Return
 * @Exception
 */


public class WriteDataThread extends Thread{

    /* 共享数据
     */
    private final PaymentRecordData shareData;

    public WriteDataThread(PaymentRecordData shareData) {
        this.shareData = shareData;
    }

    @Override
    public void run() {
        // 在这里，如果中断，就会进入到catch，不用自己break，
        // 因为run方法已经结束了
        Scanner scanner = new Scanner(System.in);
        boolean result;
        try{
            while (true){
                String text = scanner.nextLine().trim();
                if("quit".equals(text)){
                    break;
                }
                result = shareData.write(text);
                if(!result){
                    System.out.println("InValid Input");
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}

