package com.whs.wework.devtools;

import com.whs.wework.Api;
import com.whs.wework.Wework;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * 本类是为了演示，从Har文件中读取接口信息，编写case的情况，代码会看起来很清晰
 * case中的har来自企业微信官方的一个最简单的接口模拟请求，没有query和body，只要token就能发送成功: 管理企业应用 - 获取应用概况列表，https://work.weixin.qq.com/api/devtools/devtool.php
 * 但har中的URL是带token的，而token会有失效的情况，以后还需优化：去掉URL里的token，实时获取最新token
 */
public class App extends Api {

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

    public Response listApp(){
        return getResponseFromHar("/api/app.har.json",".*tid=67.*",null);
    }
}
