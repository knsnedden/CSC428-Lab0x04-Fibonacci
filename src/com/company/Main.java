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

    // standard recursion
    public static long fibRecur(long X){
        if (X == 1 || X == 0){
            return X; // reached bottom
        }
        else{
            return fibRecur(X-1) + fibRecur(X-2);
        }
    }

    // stores all previous fib numbers and accesses them when moving up the list
    // prevents performing same fib sequence multiple times
    // cuts time complexity to linear time
    public static long fibCache(long X){
        long[] cache = new long[(int)X + 2];

        // with recursion, same as if(x == 1 || x == 0) return X;
        cache[0] = 0;
        cache[1] = 1;

        for (int i = 2; i < X + 1; i++){
            cache[i] = cache[i-1] + cache[i-2]; // store fib numbers
        }

        return cache[(int)X]; // return highest fib # in sequence

    }

    // exact same as fibCache with better storing capabilities
    public static long fibLoop(long X){
        long x = 0, y = 1, sum = 0;

        for (int i = 1; i < X; ++i){
            sum = x + y;
            x = y;
            y = sum;
        }

        return sum;
    }

    /* fib sequence with matrix:
    [(1 1)   ^ n = [ (fib(n + 1) fib(n))
     (1 0)]          (fib(n)     fib(n-1)) ]
     the goal of this function is then to find the power matrix to the X - 1 power
     then the first element of the matrix is the fib sequence
     this has a time complexity of log(n)
     */
    public static long fibMatrix(long X){
        long[][] arr = new long[][]{{1,1},{1,0}};
        if (X == 0){
            return X;
        }
        matrixPower(arr,(int)X-1);

        return arr[0][0];

    }

    public static void matrixPower(long[][] arr, int n){
        if (n == 0 || n == 1){
            return;
        }

        long[][] place = new long[][]{{1,1},{1,0}};

        matrixPower(arr,n/2); // recursive: find the the smallest power and work up; n/2 because f(n+1), f(n), and f(n-1) are found

        // multiply the matrix by itself
        long a = arr[0][0] * arr[0][0] + arr[0][1] * arr[1][0];
        long b = arr[0][0] * arr[0][1] + arr[0][1] * arr[1][1];
        long c = arr[1][0] * arr[0][0] + arr[1][1] * arr[1][0];
        long d = arr[1][0] * arr[0][1] + arr[1][1] * arr[1][1];

        // move the results into the matrix
        arr[0][0] = a;
        arr[0][1] = b;
        arr[1][0] = c;
        arr[1][1] = d;

        // if n is odd, this gives correct answer
        if (n%2 != 0){
            long w = arr[0][0] * place[0][0] + arr[0][1] * place[1][0];
            long x = arr[0][0] * place[0][1] + arr[0][1] * place[1][1];
            long y = arr[1][0] * place[0][0] + arr[1][1] * place[1][0];
            long z = arr[1][0] * place[0][1] + arr[1][1] * place[1][1];

            arr[0][0] = w;
            arr[0][1] = x;
            arr[1][0] = y;
            arr[1][1] = z;
        }


    }

    public static void timeTrial(){
        boolean keepGoing = true;
        long maxTime = (long)Math.pow(2,30);
        long cacheTime = 0, recurTime = 0, loopTime = 0, matrixTime = 0;
        int N = 1, recurIndex = 0, cacheIndex = 0, loopIndex = 0, matrixIndex = 0;
        long x = 1, prev_x = 0, result = 0, max = (long)Math.pow(2,63)-1;
        ArrayList<Long> recurTimeResults = new ArrayList<Long>(), cacheTimeResults = new ArrayList<Long>(),
                        loopTimeResults = new ArrayList<Long>(), matrixTimeResults = new ArrayList<Long>();

        System.out.println("                                                fibRecur(X)                            fibCache(X)                        fibLoop(X)                         fibMatrix(X)");
        System.out.println("   N   |        X       |       fib(X)        |    Time     |    DR    |   Exp. DR   |    Time     |   DR   |  Exp. DR  |    Time     |   DR   |  Exp. DR  |    Time     |   DR   |  Exp. DR  ");

        while (keepGoing){
            System.out.printf("%6d |", N);
            System.out.printf("%15d |", x);
            result = fibMatrix(x);
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

            if (matrixTime <= maxTime){
                matrixTime = 0;
                for (int i = 0; i < 10; ++i){
                    long timeBefore = getCpuTime();
                    result = fibMatrix(x);
                    long timeAfter = getCpuTime();
                    matrixTime += timeAfter - timeBefore;
                }
                matrixTime = matrixTime/10;
                matrixTimeResults.add(matrixTime);
                System.out.printf("%12d |", matrixTime);
                if (prev_x == 0 || x%2 == 1){
                    System.out.printf("   --   |    --     |");
                }
                else{
                    float pdr = (float)Math.log(x)/(float)Math.log(x/2);
                    float dr = (float)Math.log(matrixTimeResults.get(matrixIndex))/(float)Math.log(matrixTimeResults.get(matrixIndex/2));
                    System.out.printf("%7.2f |%10.2f |", dr, pdr);
                }

                matrixIndex++;
            }
            else{
                System.out.print("      --     |    --    |      --     |");
            }



            if (recurTime >= maxTime && cacheTime >= maxTime && loopTime >= maxTime && matrixTime >= maxTime){
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
