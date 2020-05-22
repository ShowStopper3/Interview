package com.sac.concurrent.atomic;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicLongArrayExample {
    public static void main(String args[]) {
        // Initializing an array
        int a[] = new int[5];
        ExecutorService es = Executors.newFixedThreadPool(5);

        // Initializing an AtomicLongArray with array a
        AtomicIntegerArray arr = new AtomicIntegerArray(a);

        // Displaying the AtomicLongArray
        System.out.println("The array : " + arr);

        // Index where update is to be made
        int idx = 4;

        // Value to make operation with value at idx
        int x = 5;

        for (int base = 2; base < arr.length() + 2; base++) {
            for (int exponent = 1; exponent <= 3; exponent++) {
                es.execute(new Task2(a, base, exponent));

            }

        }
    }
}

class Task implements Runnable {

    AtomicIntegerArray ar;
    int base;
    int exponent;

    public Task(AtomicIntegerArray ar, int base, int exponent) {
        this.ar = ar;
        this.base = base;
        this.exponent = exponent;
    }

    @Override
    public void run() {
        System.out.println("");
        ar.accumulateAndGet(base - 2, (int) Math.pow(base, exponent), (d1, d2) -> d1 + d2);

        // Displaying the AtomicLongArray
        System.out.println("The array after update : " + ar);

    }

}

class Task2 implements Runnable {

    int[] ar;
    int base;
    int exponent;

    public Task2(int[] ar, int base, int exponent) {
        this.ar = ar;
        this.base = base;
        this.exponent = exponent;
    }

    @Override
    public void run() {
        ar[base - 2] = (int) (ar[base - 2] + Math.pow(base, exponent));
        System.out.println(Arrays.toString(ar));

    }

}