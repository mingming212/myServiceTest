package com.whs.wework.contact;

import com.jayway.jsonpath.JsonPath;
import com.whs.wework.Wework;
import com.whs.wework.WeworkConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.responseSpecification;

public class Department extends Contact {
    public Response list(String id){
        reset();
        return requestSpecification
                .param("id", id)
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
        .then().extract().response();
    }

    //post请求
    public Response creat(String name,String parentid){
        reset();
        String body=JsonPath.parse(this.getClass().getResourceAsStream("/data/create.json"))
                .set("name",name)
                .set("parentid",parentid)
                .set("id",null)
                .jsonString();
        return requestSpecification
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
        .then().extract().response();
    }

    public Response delete(String id){
        reset();
        return requestSpecification
                .param("id",id)
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then()
                .extract().response();
    }

    public Response update(String id,String name){
        reset();
        String body=JsonPath.parse(this.getClass().getResourceAsStream("/data/update"))
                .set("id",id)
                .set("name",name)
                .jsonString();
        return requestSpecification
                .body(body)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
                .then()
                .extract().response();
    }
}
