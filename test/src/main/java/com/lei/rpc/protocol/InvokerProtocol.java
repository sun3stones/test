package com.lei.rpc.protocol;


public class InvokerProtocol {

    private boolean isConsumer;
    private String className;
    private String methodName;
    private Object[] params;

    /**
     * 生产者
     * @param className
     */
    public InvokerProtocol(String className) {
        this.isConsumer = false;
        this.className = className;
    }

    /**
     * 消费者
     * @param className
     * @param methodName
     * @param params
     */
    public InvokerProtocol(String className, String methodName, Object[] params) {
        this.isConsumer = true;
        this.className = className;
        this.methodName = methodName;
        this.params = params;
    }

    public InvokerProtocol() {
    }

    public boolean isConsumer() {
        return isConsumer;
    }

    public void setConsumer(boolean consumer) {
        isConsumer = consumer;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
