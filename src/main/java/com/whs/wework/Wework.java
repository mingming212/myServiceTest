package com.whs.wework;

import io.restassured.RestAssured;

public class Wework {
    public static String token;
    public static String getWeworkToken(String secret){
//        useRelaxedHTTPSValidation();//信任https的任何证书
        return RestAssured.given().queryParam("corpid", WeworkConfig.getInstance().corpid)
                .queryParam("corpsecret",secret)
                .when()
                .get("https://qyapi.weixin.qq.com/cgi-bin/gettoken")
                .then()
                .log().all()
//                .body("errmsg", Matchers.equalTo("ok"))
                .extract().path("access_token");
    }

    public static String getToken(){
        if(token==null){
            token=getWeworkToken( WeworkConfig.getInstance().contactSecret);
        }
        return token;
    }

}
