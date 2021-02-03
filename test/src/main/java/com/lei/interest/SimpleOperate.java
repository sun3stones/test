package com.lei.interest;

public class SimpleOperate {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(i+":"+test1(i));
        }
    }

    public static Integer test1(Integer num){
        return num & (8 - 1);
    }
}
