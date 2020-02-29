package com.lei.designpattern.bridge;

public abstract class AbstactObject {
    protected ImplObject impl;

    public AbstactObject(ImplObject impl) {
        this.impl = impl;
    }

    public abstract void color();
}
