package com.whs.wework.contact;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.*;

    class DepartmentTest {
        Department department;
        String time=String.valueOf(System.currentTimeMillis());
    @BeforeEach
    void setUp() {
        if(department==null){
            department=new Department();
        }

    }

    @Test
    void list() {
        department.list("").then().statusCode(200).body("department.name[0]",equalTo("whs公司"));//list("")：id不填，默认获取全量组织架构
        department.list("1").then().statusCode(200).body("department.id[0]",equalTo(1));
    }

        @Test
        void creat() {
            department.creat("whs中文3"+time,"1").then().statusCode(200).body("errcode",equalTo(0));
//            department.creat("whs5","1").then().statusCode(200).body("errcode",equalTo(60008));
        }

        @Test
        void delete() {
/*
        //为嘛这种写法就报错呢
            String id="";
            id=String.valueOf(department.creat("whs"+time,"1").
                then().extract()
                .response()
                .path("id"));
*/

            String id="";
            Object oo=department.creat("whs"+time,"1").
                    then().extract()
                    .response().path("id");
            System.out.println("---------"+oo);
            id=String.valueOf(oo);


        department.delete(id)
                .then().statusCode(200).body("errcode",equalTo(0));
        }

        @Test
        void update() {
            String name="whs23";
            String id=department.creat(name,"1")
                .then().extract().path("id").toString();
        department.update(id,"2")
                .then().statusCode(200).body("errcode",equalTo(0));
        department.delete(id)
                .then().statusCode(200).body("errcode",equalTo(0));
        }

        @Test
        //另一种方式获取id
        void update2() {
            String name="whs21";
            department.creat(name,"1");
            String id=department.list("").path("department.find {it.name == '"+name+"'}.id").toString();
            department.update(id,"3")
                    .then().statusCode(200).body("errcode",equalTo(0));
            department.delete(id)
                    .then().statusCode(200).body("errcode",equalTo(0));
        }


    }