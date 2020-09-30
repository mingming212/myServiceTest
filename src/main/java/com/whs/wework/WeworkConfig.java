package com.whs.wework;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.HashMap;

public class WeworkConfig {
    public String corpid="ww89219670ad1e9a4e";
    public String corpsecret="4flRSUlS1HteEQ9yQaqcKHmN3_Q10TSkPn7E5sJh0YM";
    public String contactSecret="_YyJN7ys2GW58pQ8Z9ML4OBeBAO6PSxaqX6bJK2aHtI";

    public String current="test";
    public HashMap<String, HashMap<String,String>> env;

    private WeworkConfig(){}
    private static WeworkConfig weworkConfig;
    public static WeworkConfig getInstance(){
        if(weworkConfig==null){
            weworkConfig=load("/conf/WeworkConfig.yaml");//从配置文件中读取信息（替代在类中设置的变量，更方便管理）
//            System.out.println(weworkConfig.corpid);
        }
        return weworkConfig;

    }

    public static WeworkConfig load(String path){
        //fixed: read from yaml or json
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            //writeValueAsString()方法：将java类序列化为String，即：将类的信息（如变量）转化成yaml格式
//            System.out.println(mapper.writeValueAsString(WeworkConfig.getInstance()));

            //readValue()方法：读取yaml文件，返回类的实例
            return mapper.readValue(WeworkConfig.class.getResourceAsStream(path),WeworkConfig.class);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
