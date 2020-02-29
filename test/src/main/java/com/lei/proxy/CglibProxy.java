package com.lei.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxy implements MethodInterceptor {

    private Target target;

    public CglibProxy(Target target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before cglib proxy");
        Object result = method.invoke(target,objects);
        System.out.println("after cglib proxy");
        return result;
    }

    public Subject getCglibProxy(){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        Object object = enhancer.create();
        return (Subject) object;
    }

    public static void main(String[] args) {
        Target target = new Target();
        CglibProxy cglibProxy = new CglibProxy(target);
        Subject subject = cglibProxy.getCglibProxy();
        subject.doSomething("say hello");
    }
}
