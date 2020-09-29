package com.whs.wework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarRequest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class Api {
    HashMap<String,Object> query =new HashMap<String, Object>();

    public RequestSpecification getDefaultRequestSpecification(){
        return given();
    }

    //用JsonPath读取path路径下的json文件，并用map里的值更新到此json里的值，返回带有新数据的json的字符串。比如返回一个新的body字符串
    public static String template(String path,HashMap<String,Object> map){
        DocumentContext documentContext= JsonPath.parse(Restful.class.getResourceAsStream(path));
        map.entrySet().forEach(entry ->
                documentContext.set(entry.getKey(),entry.getValue()));
        return documentContext.jsonString();
    }

    public Response templateFromHar(String path,String pattern,HashMap<String,Object> map){
        //todo：支持从har文件读取接口定义并发送
        //从har中读取请求，进行更新
        //2020.3月份更新，尝试读取har文件，并发送，此为半成品
        //调用方法：DepartmentTest类的listFromHar方法。
        DocumentContext documentContext= JsonPath.parse(Restful.class.getResourceAsStream(path));
        /*
         * map.entrySet().forEach(entry ->
         * documentContext.set(entry.getKey(),entry.getValue()));
         */
        String method=documentContext.read("$.log.entries[0].request.method");       //读取har中的信息
//        System.out.println("~~~  "+method);
        String url=documentContext.read("$.log.entries[0].request.url");
//        System.out.println("~~~  "+url);
        String cookie=documentContext.read("$.log.entries[0].request.headers[-1].value");
//        String cookie=documentContext.read("$.log.entries[0].request.headers[?(@.name == 'cookie')].value");
        System.out.println("~~~  "+cookie);
        return getDefaultRequestSpecification().given().cookie("cookie", cookie).when().request(method,url);
    }

    public Response getResponseFromHar(String path,String pattern,HashMap<String,Object> map){
        Restful restful=getApiFromHar(path, pattern);//从har文件中读取接口信息，存在restful中
        restful=updateApiFromMap(restful,map);//如果数据有更新，通过map传递，如填充restful中的query
        return getResponseFromRestful(restful);//发送请求
    }


    public Response templateFromSwagger(String path,String pattern,HashMap<String,Object> map){
        //todo：支持从swagger文件读取接口定义并发送
        //从swagger中读取请求，进行更新
        DocumentContext documentContext= JsonPath.parse(Restful.class.getResourceAsStream(path));
        map.entrySet().forEach(entry ->
                documentContext.set(entry.getKey(),entry.getValue()));

        String method=documentContext.read("method");       //读取har中的信息,伪代码
        String url=documentContext.read("url");       //读取har中的信息,伪代码
        return getDefaultRequestSpecification().when().request(method,url);
    }




    public Restful getApiFromHar(String path, String pattern) {
        HarReader harReader=new HarReader();
        try {
            Har har=harReader.readFromFile(new File(getClass().getResource(path).getFile()));//如果是中文路径，可能出现乱码问题
            HarRequest request=new HarRequest();
            Boolean match=false;
            for(HarEntry entry:har.getLog().getEntries()){
                request=entry.getRequest();
                if(request.getUrl().matches(pattern)) {
                    match=true;
                    break;
                }
            }

            if(match==false)
                throw new Exception("没有匹配到！！");

//将读取到的har里的数据，放到restful中
            Restful restful=new Restful();
            restful.method=request.getMethod().toString().toLowerCase();//示例中写的是request.getMethod().name().toLowerCase();感觉.name没啥必要
            //todo:去掉URL中的query部分
            restful.url=request.getUrl();
            request.getQueryString().forEach(q->{
                restful.query.put(q.getName(),q.getValue());
            });
            restful.body=request.getPostData().getText();
            return restful;
        } catch (HarReaderException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Restful getApiFromYaml(String path){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            //writeValueAsString()方法：将java类序列化为String，即：将类的信息（如变量）转化成yaml格式
//            System.out.println(mapper.writeValueAsString(WeworkConfig.getInstance()));

            //readValue()方法：读取yaml文件，返回类的实例
            return mapper.readValue(Api.class.getResourceAsStream(path),Restful.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Restful updateApiFromMap(Restful restful, HashMap<String,Object> map){
        if(map==null){
            return restful;
        }
        if(restful.method.toLowerCase().contains("get")){
            //用map中的数据，填充restful中的query
            map.entrySet().forEach(entry->{
                restful.query.replace(entry.getKey(),entry.getValue().toString());

            });
        }else if(restful.method.toLowerCase().contains("post")){
            String filePath=map.get("_file").toString();
            map.remove("_file");
            restful.body=template(filePath,map);
        }
        return restful;
    }

    public Response getResponseFromRestful(Restful restful){
        RequestSpecification requestSpecification=getDefaultRequestSpecification();
        //循环设置请求中的query数据，即given().queryParam()
        if(restful.query!=null) {
            restful.query.entrySet().forEach(entry -> {
                requestSpecification.queryParam(entry.getKey(), entry.getValue());
            });
        }

        if(restful.body!=null){
            requestSpecification.body(restful.body);
        }

        //todo: 多环境支持，替换url，更新host的header


        return requestSpecification
                    .log().all()
                .request(restful.method,restful.url)
                .then()
                    .log().all()
                .extract().response();
    }




    public Response templateFromYaml(String path, HashMap<String,Object> map){
        //fixed：根据yaml生成接口定义并发送

        Restful restful=getApiFromYaml(path);
        restful=updateApiFromMap(restful,map);
        return getResponseFromRestful(restful);



    }
}
