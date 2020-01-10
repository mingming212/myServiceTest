package com.whs.wework.contact;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Restful {

    HashMap<String,Object> query =new HashMap<String, Object>();
    public RequestSpecification requestSpecification=given();
    public Response send(){  //send()方法其实用不到，只是举个例子
        requestSpecification=given().log().all();
        query.entrySet().forEach(
                entry -> requestSpecification.queryParam(entry.getKey(),entry.getValue())
        );

        return requestSpecification.when().request("get","baidu.com");
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
        DocumentContext documentContext= JsonPath.parse(Restful.class.getResourceAsStream(path));
        map.entrySet().forEach(entry ->
                documentContext.set(entry.getKey(),entry.getValue()));

        String method=documentContext.read("method");       //读取har中的信息,伪代码
        String url=documentContext.read("url");       //读取har中的信息,伪代码
        return requestSpecification.when().request(method,url);
    }

    public Response templateFromSwagger(String path,String pattern,HashMap<String,Object> map){
        //todo：支持从swagger文件读取接口定义并发送
        //从swagger中读取请求，进行更新
        DocumentContext documentContext= JsonPath.parse(Restful.class.getResourceAsStream(path));
        map.entrySet().forEach(entry ->
                documentContext.set(entry.getKey(),entry.getValue()));

        String method=documentContext.read("method");       //读取har中的信息,伪代码
        String url=documentContext.read("url");       //读取har中的信息,伪代码
        return requestSpecification.when().request(method,url);
    }

}
