package com.hsbc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jims
 * @Date : 2021/3/28
 * ** @description: Console
 * *
 *
 * *
 */
public class PaymentRecordData {

    // 定义一个 ConcurrentHashMap， 从里面读取数据和写入数据
    private final static String SEP_CHAR = System.getProperty("line.separator");
    // 定义一个 ConcurrentHashMap， 从里面读取数据和写入数据，简单起见，数值只考虑了Integer，实际情况需要取实数
    private Map<String, Integer> concurrentHashMap ;
    // 定义一个锁
    private final ReadWriteDataLock lock = new ReadWriteDataLock();

    public PaymentRecordData() {
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }

    public String read() throws InterruptedException {
        try {
            // 1 加读锁
            lock.readLock();
            // 2 读取数据
            return doRead();
        }finally {
            // 释放锁
            lock.readUnLock();
        }
    }

    private String doRead() {
        StringBuffer sb = new StringBuffer();
        for(String key :concurrentHashMap.keySet()){
            //System.out.println(key.concat(" ").concat((String) concurrentHashMap.get(key)));
            if(0 == concurrentHashMap.get(key)) {
                continue;
            }
            sb.append(key);
            sb.append(" ");
            sb.append(concurrentHashMap.get(key));
            sb.append(SEP_CHAR);
        }
        return sb.toString();
    }

    private void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean write(String content) throws InterruptedException {
        try {
            // 1 加写锁
            lock.writeLock();
            // 2 写数据
            return doWrite(content);
        }finally {
            // 释放锁
            lock.unWriteLock();
        }
    }

    private synchronized boolean doWrite(String content) {
        //拆分输入内容
        String[] array = content.split("\\s+");
        int amount = 0;
        //输入异常判断
        if(array.length!=2){
            return false;
        }
        //字符串转数值判断
        try{
            amount = Integer.valueOf(array[1]);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        if(concurrentHashMap.containsKey(array[0])){
            amount+=concurrentHashMap.get(array[0]);
            concurrentHashMap.replace(array[0],amount);
        }else{
            concurrentHashMap.put(array[0],amount);
        }
        return true;
    }
}
