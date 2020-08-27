package com.whs.wework.contact;

import com.whs.wework.Api;
import com.whs.wework.Restful;
import com.whs.wework.Wework;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class Contact extends Api {
    String time = String.valueOf(System.currentTimeMillis());

    public Contact(){
        reset();
    }

    public void reset(){
        requestSpecification=given();
        requestSpecification
                .log().all()
                .queryParam("access_token", Wework.getToken())
                .contentType(ContentType.JSON)
                .expect()
                .log().all()
                .statusCode(200);//提前写断言

        requestSpecification.filter((req,res,ctx)->{
            //todo: 对请求 相应，做封装
            return ctx.next(req,res);
        });
    }
}
