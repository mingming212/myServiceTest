package com.whs.wework.contact;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;

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

    public Response creat(HashMap<String,Object> map){
        reset();
        DocumentContext documentContext=JsonPath.parse(this.getClass().getResourceAsStream("/data/create.json"));
        map.entrySet().forEach(entry -> documentContext.set(entry.getKey(),entry.getValue()));

        return requestSpecification
                .body(documentContext.jsonString())
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
        String body=JsonPath.parse(this.getClass().getResourceAsStream("/data/update.json"))
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

    public Response update(HashMap<String,Object> map){
        //伪代码，演示通过分析Har文件中的信息，传递参数去请求接口，简化代码
        return templateFromHar(
                "data/demo.har.json",
                "https://work.weixin.qq.com/wework_admin/contacts/getSingleMember",
                map);

    }

    public void deleteAll(){
        ArrayList<Integer> alist=list("").then().extract().response().path("department.id");
        System.out.println(alist.toString());
       /* for(int id:alist){
            department.delete(String.valueOf(id));
        }*/
        alist.forEach(id->delete(id.toString()));
    }
}
