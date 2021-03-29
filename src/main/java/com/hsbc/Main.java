package com.hsbc;

/**
 * @author Jims
 * @date 2021/3/28
 * @description: Console
 **/
public class Main {

    public static void main(String[] args) {

        final PaymentRecordData shareData= new PaymentRecordData();
        new WriteDataThread(shareData).start();
        new ReadDataThread(shareData).start();

    }
}
