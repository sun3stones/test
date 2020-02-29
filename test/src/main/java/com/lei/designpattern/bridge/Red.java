package com.lei.designpattern.bridge;

public class Red implements ImplObject {
    @Override
    public void color() {
        System.out.println("red");
    }
}
