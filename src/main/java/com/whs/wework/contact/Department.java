package com.whs.wework.contact;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.HashMap;

public class Department extends Contact {
    public Response list(String id) {
 /*这种写法是基本写法，也可以用下面的从yaml文件读取接口定义来代替
        reset();
        return requestSpecification
                .param("id", id)
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/list")
        .then().extract().response();
*/

        //从yaml读取接口定义
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return getResponseFromYaml("/api/list.yaml", map);

    }

    //post请求
    public Response creat(String name, String parentid) {
/*这种写法是基本写法，也可以用下面的从yaml文件读取接口定义来代替
        String body=JsonPath.parse(this.getClass().getResourceAsStream("/data/create.json"))
                .set("name",name)
                .set("parentid",parentid)
                .set("id",null)
                .jsonString();
        return getDefaultRequestSpecification()
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
        .then().extract().response();
*/

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("_file", "/data/create.json");
        map.put("name", name);
        map.put("parentid", parentid);
        map.put("id", null);
        return getResponseFromYaml("/api/create.yaml", map);
    }

    public Response creat(HashMap<String, Object> map) {
/*这种写法是基本写法，也可以用下面的从yaml文件读取接口定义来代替
        DocumentContext documentContext = JsonPath.parse(this.getClass().getResourceAsStream("/data/create.json"));
        map.entrySet().forEach(entry -> documentContext.set(entry.getKey(), entry.getValue()));

        return getDefaultRequestSpecification()
                .body(documentContext.jsonString())
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/department/create")
                .then().extract().response();
*/

        map.put("_file","/data/create.json");
        return getResponseFromYaml("/api/create.yaml",map);

    }

    public Response delete(String id) {
/*这种写法是基本写法，也可以用下面的从yaml文件读取接口定义来代替
        return requestSpecification
                .param("id",id)
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/department/delete")
                .then()
                .extract().response();
*/

        HashMap map = new HashMap();
        map.put("id", id);
        return getResponseFromYaml("/api/delete.yaml", map);
    }

    public Response update(String id, String name) {
/*
        String body = JsonPath.parse(this.getClass().getResourceAsStream("/data/update.json"))
                .set("id", id)
                .set("name", name)
                .jsonString();
        return getDefaultRequestSpecification()
                .body(body)
                .when()
                .post("https://qyapi.weixin.qq.com/cgi-bin/department/update")
                .then()
                .extract().response();
*/

        HashMap<String, Object> map=new HashMap<String, Object>();
        map.put("_file","/data/update.json");
        map.put("id", id);
        map.put("name", name);
        return getResponseFromYaml("/api/update.yaml",map);

    }

    public void deleteAll() {
        ArrayList<Integer> alist = list("").then().extract().response().path("department.id");
        System.out.println(alist.toString());
       /* for(int id:alist){
            department.delete(String.valueOf(id));
        }*/
        alist.forEach(id -> delete(id.toString()));
    }


    public Response list(HashMap<String, Object> map) {
        //伪代码，演示通过分析Har文件中的信息，传递参数去请求接口，简化代码
        //2020.3月份更新，尝试读取har文件，并发送，此为半成品，pattern和map参数未用到
        String pattern = "";
        return templateFromHar(
                "/data/har_DepaList.json",
                pattern,
                map);

    }


}

