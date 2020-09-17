package com.whs.wework.contact;

import com.whs.wework.Api;
import com.whs.wework.Restful;
import com.whs.wework.Wework;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Contact extends Api {
    String time = String.valueOf(System.currentTimeMillis());

    @Override
    public RequestSpecification getDefaultRequestSpecification(){
        RequestSpecification requestSpecification=super.getDefaultRequestSpecification();
        requestSpecification
                .log().all()
                .queryParam("access_token", Wework.getToken())
//                .queryParam("debug",1)    //debug用的，可生成一个hint值，方便在企业微信上查看请求的具体值
                .contentType(ContentType.JSON)
                .expect()
                .log().all()
                .statusCode(200);//提前写断言

        requestSpecification.filter((req,res,ctx)->{
            //todo: 对请求 相应，做封装
            return ctx.next(req,res);
        });
        return requestSpecification;
    }
}
