package com.company;



public class Main {

    public static void main(String[] args) {

    }

    public static long fibRecur(long X){
        if (X == 1 || X == 0){
            return X;
        }
        else{
            return fibRecur(X-1) + fibRecur(X-2);
        }
    }
}
