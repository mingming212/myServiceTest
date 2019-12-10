package com.whs.wework.contact;

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


}
