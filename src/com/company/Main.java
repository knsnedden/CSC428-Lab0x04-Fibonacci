package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        //timeTrial();
    }

    public static long fibRecur(long X){
        if (X == 1 || X == 0){
            return X;
        }
        else{
            return fibRecur(X-1) + fibRecur(X-2);
        }
    }

    public static long fibCache(long X){
        long[] cache = new long[(int)X + 2];

        cache[0] = 0;
        cache[1] = 1;

        for (int i = 2; i < X + 1; i++){
            cache[i] = cache[i-1] + cache[i-2];
        }

        return cache[(int)X];

    }

    public static void timeTrial(){
        boolean keepGoing = true;
        long recurTime = 0, maxTime = (long)Math.pow(2,30), prevRecurTime = 0;
        int N = 1, recurIndex = 0;
        long x = 1, prev_x = 0, result = 0;
        ArrayList<Long> recurTimeResults = new ArrayList<Long>();

        System.out.println("                             fibRecur(X)");
        System.out.println("   N   |         X         |    fib(X)     |    Time     | Doubling Ratio | Exp. Doubling Ratio |");

        while (keepGoing){
            System.out.printf("%6d |", N);

            if (recurTime <= maxTime){
                System.out.printf("%18d |", x);
                recurTime = 0;
                for (int i = 0; i < 10; ++i){
                    long timeBefore = getCpuTime();
                    result = fibRecur(x);
                    long timeAfter = getCpuTime();
                    recurTime += timeAfter - timeBefore;
                }
                recurTime = recurTime/10;
                recurTimeResults.add(recurTime);
                System.out.printf("%14d |%12d |", result, recurTime);
                if (prev_x == 0 || x%2 == 1){
                    System.out.printf("       --       |         --          |");
                }
                else{
                    float pdr = (float)Math.pow(1.6180,x)/(float)Math.pow(1.6180,x/2);
                    float dr = (float)recurTimeResults.get(recurIndex)/(float)recurTimeResults.get(recurIndex/2);
                    System.out.printf("%15.2f |%20.2f |",dr, pdr);
                }

                recurIndex++;
                prev_x = x;
                x++;
                N = (int)Math.ceil(Math.log(x+1)/Math.log(2));
            }
            else{
                System.out.print("        --        |      --      |     --     |      --       |         --         |");
            }


            if (recurTime >= maxTime){
                keepGoing = false;
            }
            System.out.println();
        }
    }

    public static long getCpuTime(){
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }
}
