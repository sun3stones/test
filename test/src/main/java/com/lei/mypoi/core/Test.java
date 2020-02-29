package com.lei.mypoi.core;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lei.mypoi.entity.Student;

import java.io.File;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        ImportParams params = new ImportParams();
        params.setSheetNum(2);
        params.setTitleRows(0);
        params.setHeadRows(1);
        List<Student> list = ExcelImportUtil.importExcel(
                new File("C:\\Users\\admin\\Desktop\\成绩表.xlsx"),
                Student.class, params);
        System.out.println(list.size());
    }

}
