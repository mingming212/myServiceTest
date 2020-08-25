package com.whs.wework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeworkConfigTest {

    @Test
    void load() {
        WeworkConfig.load("");

//        WeworkConfig weworkConfig=WeworkConfig.load("/conf/WeworkConfig.yaml");
//        System.out.println("~~~"+weworkConfig.corpid);
    }

    @Test
    void getInstance() {
        WeworkConfig.getInstance();//读到的是yaml文件中的值，而不是类中写的变量的值
    }
}