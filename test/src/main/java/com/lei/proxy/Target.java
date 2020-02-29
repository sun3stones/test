package com.lei.proxy;

public class Target implements Subject{

    private String name;
    private Object target;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    @Override
    public void doSomething(String something) {
        System.out.println(something);
    }

    @Override
    public String toString() {
        return "DynamicProxy{" +
                "target=" + target +
                '}';
    }
}