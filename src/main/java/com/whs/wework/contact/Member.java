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

    //读取成员信息
    public Response get(String userid){
        reset();
        return requestSpecification
                .param("userid",userid)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/user/get")
                .then()
                .log().all()
                .extract().response();
    }

    public Response update(HashMap<String ,Object> map){
        reset();
        String body=template("/data/userUpdate.json",map);
        return requestSpecification
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/user/update")
                .then()
                .extract().response();
    }

    public Response delete(String userid){
        reset();
        return requestSpecification.param("userid",userid)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/user/delete")
                .then()
                .extract().response();
    }

    //批量删除成员
    public Response batchdelete(String userid1,String userid2){
        reset();
        String body="{\"useridlist\": [\""+userid1+"\", \""+userid2+"\"]}";
        return requestSpecification
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete")
                .then().extract().response();
    }

    //获取部门成员
    public Response simplelist(String department_id){
        reset();
        return requestSpecification
                .param("department_id",department_id)
                .when().get("https://qyapi.weixin.qq.com/cgi-bin/user/simplelist")
                .then().extract().response();
    }

    public Response convert_to_openid(String body){
        reset();
        return requestSpecification
                .body(body)
                .when().post("https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid")
                .then().extract().response();
    }
}
