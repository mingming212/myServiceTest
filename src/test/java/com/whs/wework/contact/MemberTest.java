package com.whs.wework.contact;

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
}