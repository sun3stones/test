package com.lei.mypoi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class Student {
    @Excel(name = "学号")
    private String sno;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "班级")
    private String clazz;
    @Excel(name = "语文")
    private float chinese;
    @Excel(name = "数学")
    private float math;
    @Excel(name = "英语")
    private float english;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public float getChinese() {
        return chinese;
    }

    public void setChinese(float chinese) {
        this.chinese = chinese;
    }

    public float getMath() {
        return math;
    }

    public void setMath(float math) {
        this.math = math;
    }

    public float getEnglish() {
        return english;
    }

    public void setEnglish(float english) {
        this.english = english;
    }
}
