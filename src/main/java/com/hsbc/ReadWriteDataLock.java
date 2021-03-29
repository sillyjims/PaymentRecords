package com.hsbc;

/**
 * @author Jims
 * @Date: 2021/03/27
 * @Description: 读写锁控制
 */
public class ReadWriteDataLock {

    /**
     * @Description: 当前正在读取数据线程数量
     */
    private int readingThreads = 0;

    /**
     * @Description: 等待去读取数据的线程的数量， 但是读取不了，放到了wait队列中
     */
    private int waitReadThreads = 0;

    /**
     * @Description: 正在写入数据的线程数量 只有一个
     */
    private int writingThreads = 0;

    /**
     * @Description: 记录当前有多少个线程想要写入，但是写入不了，放到了wait队列中，在等待其他释放锁
     */
    private int waitWriteThreads = 0;

    /**
     * @Description: 是否写入优先级更高
     */
    private boolean preferWrite;

    public ReadWriteDataLock(boolean preferWrite) {
        this.preferWrite = preferWrite;
    }

    public ReadWriteDataLock() {
        // 默认 true
        this(true);
    }

    /**
     * @author: Jims
     * @Date: 2021/3/27
     * @Description: 加读锁
     */
    public synchronized void readLock() throws InterruptedException {
        // 获取锁的时候，有可能获取不到锁，进入到等待队列（waitSet）中, 所以waitReadThreads可能需要加1的
        this.waitReadThreads++;
        try {
            while (writingThreads > 0 || (preferWrite && this.waitWriteThreads > 0)) {
                // 当前有线程正在进行写入的时候，肯定是不能这个读取数据的线程肯定是不能进行读取数据的，进入等待
                // 如果更偏爱写入，并且有写入的线程数量大于0，也让读取的线程进入等待
                // this.waitReadThreads++; 这个放在这里不合适，需要立马进入等待，就放到外面，但是可能会记录的数量不对
                this.wait();
            }
            // 此时没有线程进行写入数据，就可以读取数据了, 正在读取数据的线程数加1
            this.readingThreads++;
        } finally {
            // 最终释放锁的时候，要减减waitingReaders
            this.waitReadThreads--;
        }
    }

    /**
     * @author: Jims
     * @Date: 2021/3/27
     * @Description: 释放读锁
     */
    public synchronized void readUnLock() throws InterruptedException {
        // 1 读取数据的线程的数量减1
        this.readingThreads--;
        // 2 释放
        this.notifyAll();
    }


    public synchronized void writeLock() throws InterruptedException {
        // 获取锁的时候，有可能获取不到锁，进入到等待队列（waitSet）中, 所以waitWriteThreads可能需要加1的
        this.waitWriteThreads++;
        try {
            while (readingThreads > 0 || writingThreads > 0) {
                // 当有其他线程在进行读取或者写入的时候，则当前线程不能进行写操作，只能等待
                this.wait();
            }
            // 没有其他线程在进行读取或者写入的时候，那就可以进行写入
            this.writingThreads++;
        } finally {
            // 最终释放锁的时候，要减减waitWriteThreads
            this.waitWriteThreads--;
        }
    }

    /**
     * @author: Jims
     * @Date: 2021/3/27
     * @Description: 释放写锁
     */
    public synchronized void unWriteLock() {
        this.writingThreads--;
        this.notifyAll();
    }


}


