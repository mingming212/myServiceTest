package com.whs.wework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Api {
    HashMap<String,Object> query =new HashMap<String, Object>();

    public RequestSpecification getDefaultRequestSpecification(){
        return given().log().all();
    }

    public static String template(String path,HashMap<String,Object> map){
        DocumentContext documentContext= JsonPath.parse(Restful.class.getResourceAsStream(path));
        map.entrySet().forEach(entry ->
                documentContext.set(entry.getKey(),entry.getValue()));
        return documentContext.jsonString();
    }

    public Response templateFromHar(String path,String pattern,HashMap<String,Object> map){
        //todo：支持从har文件读取接口定义并发送
        //从har中读取请求，进行更新
        //2020.3月份更新，尝试读取har文件，并发送，此为半成品
        //调用方法：DepartmentTest类的listFromHar方法。
        DocumentContext documentContext= JsonPath.parse(Restful.class.getResourceAsStream(path));
        /*
         * map.entrySet().forEach(entry ->
         * documentContext.set(entry.getKey(),entry.getValue()));
         */
        String method=documentContext.read("$.log.entries[0].request.method");       //读取har中的信息
//        System.out.println("~~~  "+method);
        String url=documentContext.read("$.log.entries[0].request.url");
//        System.out.println("~~~  "+url);
        String cookie=documentContext.read("$.log.entries[0].request.headers[-1].value");
//        String cookie=documentContext.read("$.log.entries[0].request.headers[?(@.name == 'cookie')].value");
        System.out.println("~~~  "+cookie);
        return getDefaultRequestSpecification().given().cookie("cookie", cookie).when().request(method,url);
    }

    public Response templateFromSwagger(String path,String pattern,HashMap<String,Object> map){
        //todo：支持从swagger文件读取接口定义并发送
        //从swagger中读取请求，进行更新
        DocumentContext documentContext= JsonPath.parse(Restful.class.getResourceAsStream(path));
        map.entrySet().forEach(entry ->
                documentContext.set(entry.getKey(),entry.getValue()));

        String method=documentContext.read("method");       //读取har中的信息,伪代码
        String url=documentContext.read("url");       //读取har中的信息,伪代码
        return getDefaultRequestSpecification().when().request(method,url);
    }

    public Response templateFromYaml(String path, HashMap<String,Object> map){
        //fixed：根据yaml生成接口定义并发送
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            //writeValueAsString()方法：将java类序列化为String，即：将类的信息（如变量）转化成yaml格式
//            System.out.println(mapper.writeValueAsString(WeworkConfig.getInstance()));

            //readValue()方法：读取yaml文件，返回类的实例
            Restful restful=mapper.readValue(Api.class.getResourceAsStream(path),Restful.class);
//            System.out.println("-----------"+restful.method);
            if(restful.method.toLowerCase().contains("get")){
                //用map中的数据，填充restful中的query
                map.entrySet().forEach(entry->{
                    restful.query.replace(entry.getKey(),entry.getValue().toString());

                });
            }

            RequestSpecification requestSpecification=getDefaultRequestSpecification();
            //循环设置请求中的query数据，即given().queryParam()
            restful.query.entrySet().forEach(entry->{
                requestSpecification.queryParam(entry.getKey(),entry.getValue());
//                System.out.println("==========="+entry.getValue());
            });

            return requestSpecification
                    .log().all()
                    .request(restful.method,restful.url)
                    .then().log().all()
                    .extract().response();


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
