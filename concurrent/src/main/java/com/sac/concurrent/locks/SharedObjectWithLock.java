package com.sac.concurrent.locks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class SharedObjectWithLock {

	private ReentrantLock lock = new ReentrantLock(true);

	private int counter = 0;

	void performSync() {

		synchronized (this) {
			try {
				counter++;
				synchronized (this) {
					System.out.println();

				}
			} catch (Exception exception) {
			} finally {
				System.out.println("");
			}
		}

	}

	void perform() {

		lock.lock();
		try {
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

	public ReentrantLock getLock() {
		return lock;
	}

	boolean isLocked() {
		return lock.isLocked();
	}

	boolean hasQueuedThreads() {
		return lock.hasQueuedThreads();
	}

	int getCounter() {
		return counter;
	}

	public static void main(String[] args) {

		final int threadCount = 5;
		final ExecutorService service = Executors.newFixedThreadPool(threadCount);
		final SharedObjectWithLock object = new SharedObjectWithLock();

		service.execute(object::performSync);
		service.execute(object::performSync);

		service.execute(object::performSync);

		service.execute(object::performTryLock);

		service.shutdown();

	}

}