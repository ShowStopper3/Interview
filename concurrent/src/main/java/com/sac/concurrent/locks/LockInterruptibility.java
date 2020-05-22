package com.sac.concurrent.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class LockInterruptibility {

    private final ReentrantLock lock = new ReentrantLock(true);

    private int counter = 0;

    void perform() {

        try {
            lock.lockInterruptibly();
            counter++;
            lock.lock();
        } catch (Exception exception) {
        } finally {
            lock.unlock();
        }
    }

    private void performTryLock() {

        try {
            boolean isLockAcquired = lock.tryLock(2, TimeUnit.SECONDS);
            if (isLockAcquired) {
                try {

                    Thread.sleep(1000);
                } finally {
                    lock.unlock();

                }
            }
        } catch (InterruptedException exception) {
        }
    }


    public static void main(String[] args) {

        final int threadCount = 5;
        final ExecutorService service = Executors.newFixedThreadPool(threadCount);
        final LockInterruptibility object = new LockInterruptibility();

        service.execute(object::perform);
        service.execute(object::perform);

        service.execute(object::perform);

        service.execute(object::performTryLock);

        service.shutdown();

    }

}