package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.Math;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        timeTrial();

    }

    public static long fibRecur(long X){
        if (X == 1 || X == 0){
            return X;
        }
        else{
            return fibRecur(X-1) + fibRecur(X-2);
        }
    }

    public static void timeTrial(){
        boolean keepGoing = true;
        long recurTime = 0, maxTime = (long)Math.pow(2,40), prevRecurTime = 0;
        int N = 1, prev_N = 0;
        long x = 0;

        System.out.println("                             fibRecur(X)");
        System.out.println("   N   |         X         |    Time     | Doubling Ratio | Exp. Doubling Ratio |");

        while (keepGoing || N > 64){
            System.out.printf("%6d |", N);
            long prev_x = x;
            x = (long) Math.pow(10, N - 1) + 1;
            System.out.printf("%18d |", x);

            if (x < 1000) {
                long timeBefore = getCpuTime();
                fibRecur(x);
                long timeAfter = getCpuTime();
                recurTime = (timeAfter - timeBefore);
                System.out.printf("%12d |", recurTime);
                if (prevRecurTime == 0) {
                    System.out.printf("       na       |          na         |");
                } else {
                    float pdr = (float) Math.pow(2, x) / (float) Math.pow(2, x);
                    float dr = (float) recurTime / (float) prevRecurTime;
                    System.out.printf("%15.2f |%20.2f |", dr, pdr);

                }
                prevRecurTime = recurTime;
            }else{
                recurTime = maxTime;
                System.out.printf("             |                |                     |");
            }


            if (recurTime >= maxTime){
                keepGoing = false;
            }

            System.out.println();
            prev_N = N;
            N = N*2;
        }
    }

    public static long getCpuTime(){
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }
}
