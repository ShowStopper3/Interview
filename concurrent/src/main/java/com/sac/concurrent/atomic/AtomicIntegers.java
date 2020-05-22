package com.sac.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegers {
    // An not thread safe counter using a volatile int
    private final CounterNotThreadSafe counter = new CounterNotThreadSafe();

    // Change to Counter using AtomicInteger incrementAndGet method:
    //private final CounterUsingIncrement counter = new CounterUsingIncrement();

    // Change to Counter using AtomicInteger compareAndSet method:
    //private final CounterUsingCompareAndSet counter = new CounterUsingCompareAndSet();


    private void increment() {
        counter.increment();
    }

    public void testCounter() throws InterruptedException {
        Thread first = new Thread(this::increment);
        Thread second = new Thread(this::increment);
        first.start();
        second.start();
        first.join();
        second.join();
        System.out.println(counter.getCount() == 2);
    }
}


class CounterNotThreadSafe {
    private int count = 0;

    public void increment() {
        count++;
    }

    public int getCount() {
        return count;
    }
}

class CounterUsingCompareAndSet {
    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        int current = count.get();
        int newValue = current + 1;
        while (!count.compareAndSet(current, newValue)) {
            current = count.get();
            newValue = current + 1;
        }
    }

    public int getCount() {
        return count.get();
    }
}

class CounterUsingIncrement {
    private final AtomicInteger count = new AtomicInteger();

    public void increment() {
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }
}