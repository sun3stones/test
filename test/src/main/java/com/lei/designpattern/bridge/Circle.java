package com.lei.designpattern.bridge;

public class Circle extends AbstactObject {
    private int radius;

    public Circle(ImplObject impl, int radius) {
        super(impl);
        this.radius = radius;
    }

    @Override
    public void color() {
        impl.color();
    }

    public static void main(String[] args) {
        Circle circle = new Circle(new Red(),10);
        circle.color();
    }

    /**
     * 桥接模式
     * 将抽象部分与实现部分分离，使它们都可以独立的变化
     * 实现接口提供方法给实现类不同实现
     * 抽象类使定义用接口方法
     * 子类继承抽象类，引用接口实现类调用方法
     * */
}
