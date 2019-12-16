package com.whs.wework.contact;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.whs.wework.contact.Contact;
import io.restassured.response.Response;

import java.util.HashMap;


public class Member extends Contact {

    public Response create(HashMap<String,Object> map){
        reset();
        String body=template("/data/user.json",map);
        return requestSpecification.body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/user/create")
                .then().log().all().extract().response();
    }
}
