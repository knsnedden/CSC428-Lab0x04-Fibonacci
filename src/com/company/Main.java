package com.company;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.Math;
import java.util.Arrays;
import java.util.ArrayList;

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

    public static long fibCache(long X){
        long[] cache = new long[(int)X + 2];

        cache[0] = 0;
        cache[1] = 1;

        for (int i = 2; i < X + 1; i++){
            cache[i] = cache[i-1] + cache[i-2];
        }

        return cache[(int)X];

    }

    public static long fibLoop(long X){
        long x = 0, y = 1, sum = 0;

        for (int i = 2; i < X; ++i){
            sum = x + y;
            x = y;
            y = sum;
        }

        return sum;
    }

    public static void timeTrial(){
        boolean keepGoing = true;
        long maxTime = (long)Math.pow(2,30);
        long cacheTime = 0, recurTime = 0, loopTime = 0;
        int N = 1, recurIndex = 0, cacheIndex = 0, loopIndex = 0;
        long x = 1, prev_x = 0, result = 0, max = (long)Math.pow(2,63)-1;
        ArrayList<Long> recurTimeResults = new ArrayList<Long>(), cacheTimeResults = new ArrayList<Long>(),
                        loopTimeResults = new ArrayList<Long>();

        System.out.println("                                                fibRecur(X)                            fibCache(X)                        fibLoop(X)");
        System.out.println("   N   |        X       |       fib(X)        |    Time     |    DR    |   Exp. DR   |    Time     |   DR   |  Exp. DR  |    Time     |   DR   |  Exp. DR  |");

        while (keepGoing){
            System.out.printf("%6d |", N);
            System.out.printf("%15d |", x);
            result = fibCache(x);
            System.out.printf("%20d |", result);

            if (recurTime <= maxTime){
                recurTime = 0;
                for (int i = 0; i < 10; ++i){
                    long timeBefore = getCpuTime();
                    result = fibRecur(x);
                    long timeAfter = getCpuTime();
                    recurTime += timeAfter - timeBefore;
                }
                recurTime = recurTime/10;
                recurTimeResults.add(recurTime);
                System.out.printf("%12d |", recurTime);
                if (prev_x == 0 || x%2 == 1){
                    System.out.printf("    --    |      --     |");
                }
                else{
                    float pdr = (float)Math.pow(1.6180,x)/(float)Math.pow(1.6180,x/2);
                    float dr = (float)recurTimeResults.get(recurIndex)/(float)recurTimeResults.get(recurIndex/2);
                    System.out.printf("%9.2f |%12.2f |",dr, pdr);
                }

                recurIndex++;
            }
            else{
                System.out.print("      --     |    --    |      --     |");
            }

            if (cacheTime <= maxTime){
                cacheTime = 0;
                for (int i = 0; i < 10; ++i){
                    long timeBefore = getCpuTime();
                    result = fibCache(x);
                    long timeAfter = getCpuTime();
                    cacheTime += timeAfter - timeBefore;
                }
                cacheTime = cacheTime/10;
                cacheTimeResults.add(cacheTime);
                System.out.printf("%12d |", cacheTime);
                if (prev_x == 0 || x%2 == 1){
                    System.out.printf("   --   |    --     |");
                }
                else{
                    float pdr = (float)x/(float)(x/2);
                    float dr = (float)cacheTimeResults.get(cacheIndex)/(float)cacheTimeResults.get(cacheIndex/2);
                    System.out.printf("%7.2f |%10.2f |",dr, pdr);
                }

                cacheIndex++;
            }
            else{
                System.out.print("      --     |    --    |      --     |");
            }

            if (loopTime <= maxTime){
                loopTime = 0;
                for (int i = 0; i < 10; ++i){
                    long timeBefore = getCpuTime();
                    result = fibLoop(x);
                    long timeAfter = getCpuTime();
                    loopTime += timeAfter - timeBefore;
                }
                loopTime = loopTime/10;
                loopTimeResults.add(loopTime);
                System.out.printf("%12d |", loopTime);
                if (prev_x == 0 || x%2 == 1){
                    System.out.printf("   --   |    --     |");
                }
                else{
                    float pdr = (float)x/(float)(x/2);
                    float dr = (float)loopTimeResults.get(loopIndex)/(float)loopTimeResults.get(loopIndex/2);
                    System.out.printf("%7.2f |%10.2f |",dr, pdr);
                }

                loopIndex++;
            }
            else{
                System.out.print("      --     |    --    |      --     |");
            }


            if (recurTime >= maxTime && cacheTime >= maxTime && loopTime >= maxTime){
                keepGoing = false;
            }
            prev_x = x;
            x++;
            if (result < 0){ // means fib(X) exceeded long size
                keepGoing = false;
            }
            N = (int)Math.ceil(Math.log(x+1)/Math.log(2));
            System.out.println();
        }
    }

    public static long getCpuTime(){
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported() ?
                bean.getCurrentThreadCpuTime() : 0L;
    }
}
