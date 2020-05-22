package com.sac.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferences {
    public static void main(String[] args) throws InterruptedException {
        final UpdateStateWithCompareAndSet object = new UpdateStateWithCompareAndSet();
        Thread first = new Thread(object::update);
        Thread second = new Thread(object::update);
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(object.getState());

    }


}


class UpdateStateWithCompareAndSet {
    private final AtomicReference<ThreadState> state = new AtomicReference<>(new ThreadState());

    public void update() {
        ThreadState current = state.get();
        ThreadState newValue = current.update();
        while (!state.compareAndSet(current, newValue)) {
            current = state.get();
            newValue = current.update();
        }
    }

    public ThreadState getState() {
        return state.get();
    }
}

class UpdateStateNotThreadSafe {
    private ThreadState state = new ThreadState();

    public void update() {
        state = state.update();
    }

    public ThreadState getState() {
        return state;
    }
}

class ThreadState {
    private final Thread thread;
    private final boolean accessedByMultipleThreads;

    public ThreadState(Thread thread, boolean accessedByMultipleThreads) {
        super();
        this.thread = thread;
        this.accessedByMultipleThreads = accessedByMultipleThreads;
    }

    public ThreadState() {
        super();
        this.thread = null;
        this.accessedByMultipleThreads = false;
    }

    public ThreadState update() {
        if (accessedByMultipleThreads) {
            return this;
        }
        if (thread == null) {
            return new ThreadState(Thread.currentThread(), accessedByMultipleThreads);
        }
        if (thread != Thread.currentThread()) {
            return new ThreadState(null, true);
        }
        return this;
    }

    public boolean isAccessedByMultipleThreads() {
        return accessedByMultipleThreads;
    }
}