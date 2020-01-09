package com.whs.wework.contact;

import com.whs.wework.Wework;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class Contact extends Restful {
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
    }
}
