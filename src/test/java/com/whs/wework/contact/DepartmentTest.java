package com.whs.wework.contact;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;

class DepartmentTest extends Contact{
    Department department;
    @BeforeEach
    void setUp() {
        if (department == null) {
            department = new Department();
        }
    }

    @Test
    void list() {
        department.list("").then().statusCode(200).body("department.name[0]", equalTo("whs公司"));//list("")：id不填，默认获取全量组织架构
        department.list("1").then().statusCode(200).body("department.id[0]", equalTo(1));

    }

    @Test
    void creat() {
        department.creat("whs中文3" + time, "1").then().statusCode(200).body("errcode", equalTo(0));
//            department.creat("whs5"+time,"1").then().statusCode(200).body("errcode",equalTo(60008));

    }

    @Test
    void createByMap() {
        String name="whs"+time;
        String parentid="1";
        HashMap<String, Object> map = new HashMap<String, Object>(){
            {
                put("name",name);
                put("parentid",parentid);
                put("id",null);
            }
        };
        department.creat(map).then().statusCode(200).body("errcode",equalTo(0));
    }


    @Test
    void delete() {
/*
        //这种写法会报错
        //因为.path()返回是泛型，在这里返回的是个数组，String.valueOf()方法的参数是数组类型，里面不知道哪个转换报错了。
        //解决：不要用String.valueOf()，用.toString()方法就没问题了
            String id="";
            id=String.valueOf(department.creat("whs"+time,"1").
                then().extract()
                .response()
                .path("id"));
*/

/*          //这种写法可行
            String id="";
            id=(department.creat("whs"+time,"1").
                    then().extract()
                    .response()
                    .path("id")).toString();
*/

        //这种写法可行
        String id = "";
        Object oo = department.creat("whs" + time, "1").
                then().extract()
                .response().path("id");
//        System.out.println("---------" + oo);
        id = String.valueOf(oo);

        department.delete(id)
                .then().statusCode(200).body("errcode", equalTo(0));
    }

    @Test
    void update() {
        String name = "whs23";
        String id = department.creat(name, "1")
                .then().extract().path("id").toString();
        department.update(id, "2")
                .then().statusCode(200).body("errcode", equalTo(0));
        department.delete(id)
                .then().statusCode(200).body("errcode", equalTo(0));
    }

    @Test
        //另一种方式获取id
    void update2() {
        String name = "whs21" + time;
        department.creat(name, "1");
        String id = department.list("").path("department.find {it.name == '" + name + "'}.id").toString();
        department.update(id, "3")
                .then().statusCode(200).body("errcode", equalTo(0));
        department.delete(id)
                .then().statusCode(200).body("errcode", equalTo(0));
    }


    @Test
    void deleteAll(){
        department.deleteAll();
    }
    
    @Test
    void listFromHar() {
    	//从har文件中读取URL、method等数据，发送情趣
    	//2020.3月份更新，尝试读取har文件，并发送，此为半成品，map其实未用到，目前可以发送请求，但不知是否网站有要求，导致结果返回"message": "outsession"
    	HashMap<String,Object> map=new HashMap<String, Object>();
    	department.list(map);
    	
    }

}