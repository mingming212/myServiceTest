package com.whs.wework.contact;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.Matchers.equalTo;

public class MemberTest {
    Member member;
    @BeforeEach
    void setUp(){
        member=new Member();

    }

    @ParameterizedTest
    @ValueSource(strings = { "whs1_", "wang_", "hao1_" })
    void create(String name) {
        String newName=name+member.time;
        HashMap<String,Object> map=new HashMap<>();
        map.put("userid",newName);
        map.put("name",newName);
        map.put("mobile","180"+member.time.substring(5,13));
        map.put("department", Arrays.asList(1,2));
        map.put("email", member.time+"@whs.com");
        member.create(map).then().statusCode(200).body("errcode", equalTo(0));
    }

    //读取成员信息
    @Test
    void get() {
        String userid="hao1_1576492351381";
        member.get(userid).then()
                .body("errcode",equalTo(0))
                .body("userid",equalTo(userid));
    }

    @Test
    void update() {
        String userid="hao1_1576492351381";
        HashMap<String,Object> map=new HashMap<>();
        map.put("userid",userid);
        member.update(map).then().body("errcode",equalTo(0));
    }

    @Test
    void delete() {
        create("ForDelete_");
        String userlist=member.simplelist("2")
                .then().extract().response().path("userlist.userid[0]");
        member.delete(userlist)
                .then().body("errcode",equalTo(0));
    }

    //批量删除成员
    @Test
    void batchdelete() {
        //todo:现在，删除的账户是写死的
        member.batchdelete("ForDelete_1578551361232","ForDelete_1578551517351")
                .then().body("errcode",equalTo(0));
    }

    //获取部门成员
    @Test
    void simplelist() {
        member.simplelist("2")
                .then().body("errcode",equalTo(0));
    }

    @Test
    void convert_to_openid() {
        String body="{\n" +
                "   \"userid\": \"WangHaoShuo\"\n" +
                "}";
        member.convert_to_openid(body)
                .then().body("errcode",equalTo(0));
    }
}