package com.lei.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler {

    private Object target;

    public DynamicProxy(Object target) {
        this.target = target;
    }

    public Subject getProxy(){
        return (Subject) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before do something");
        Object result = method.invoke(target,args);
        System.out.println("after do something");
        return result;
    }


    public static void main(String[] args) {
        Target target = new Target();
        DynamicProxy dp = new DynamicProxy(target);
        Subject subject = dp.getProxy();
        subject.doSomething("say hello");
    }

}
