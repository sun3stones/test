package com.lei.mypoi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

@ExcelTarget("scores")
public class Scores {
    private String number;
    @Excel(name = "语文")
    private float chinese;
    @Excel(name = "数学")
    private float math;
    @Excel(name = "英语")
    private float english;

    public float getChinese() {
        return chinese;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
