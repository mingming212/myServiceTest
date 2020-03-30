package demo;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;

import java.util.Base64;
import java.util.HashMap;

public class Demo_schemaAndFilter {

	public static RequestSpecification requestSpecification;
	public static ResponseSpecification responsSpecification;
	@BeforeClass
	public static void beforeClass() {
		useRelaxedHTTPSValidation();//信任https的任何证书
		RestAssured.proxy("127.0.0.1", 8888);//使用fiddler代理，所有请求将会走一遍fiddler
		
		//spec方法设置全局变量（以下设置所有的的case中请求/返回都会做以下修改）
		requestSpecification=new RequestSpecBuilder().build();
		requestSpecification.cookies("a","whswhs");
		requestSpecification.contentType(ContentType.JSON);
		requestSpecification.port(80);
		
		responsSpecification=new ResponseSpecBuilder().build();
		responsSpecification.statusCode(200);
		responsSpecification.body("code", equalTo(1));
		
		  //全局filter，作用于所有的case 
		RestAssured.filters((req, res, ctx) -> {
		  if(req.getURI().contains("xueqiu.com")) { //如果请求的URI中包含"xueqiu.com"
			  req.header("aaa","bbb"); 
			  req.cookie("xq_a_token=2ee68b782d6ac072e2a24d81406dd950aacaebe3; xqat=2ee68b782d6ac072e2a24d81406dd950aacaebe3; xq_r_token=f9a2c4e43ce1340d624c8b28e3634941c48f1052; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTU4NzUyMjY2MSwiY3RtIjoxNTg1MzgxODQ1MTMzLCJjaWQiOiJkOWQwbjRBWnVwIn0.JtFg3Me8iCVvUS89_zoU-oSfDEUN9ZQDtdjxU9NQF_PUuHAyHbVAUBiQkvzuJ7yo_-8TA6UdY1XK36b3GQvHW5EIrQ6E4ddsZC2IfNBymaQBajl6QIc7H4d4n8w5SpP0uhn1EpBiqd6EWv-LyJsYXwfrcboh2tbjNb3yGyIC1cvGUBzzqa3iVckfCcKWuXIzvzfJtgoUwfAWdgVVI8BmXdrUp9UphSjs9KiAT_qZUspb-WG9eN8C1Nm9ck8hwcDk-D-QqqHUdUVvMMwGHkZaWLaUD7xPDXpiRga_5LLF3FN6f8M4gvmEZOZFrRpbsxpadiDOV2Ev1mjKgFrYC18DlA; u=941585381876784; Hm_lvt_1db88642e346389874251b5a1eded6e3=1585381878; device_id=24700f9f1986800ab4fcc880530dd0ed; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1585383069"
		  );
		  } 
		  req.body("k1,v1");
		  Response responseOrigin=ctx.next(req, res); 
		  return responseOrigin; 
		}
		);
		 		
	}
	
	
	@Test
	public void testLogin() {
		Response response =given()
			.header("User-Agent"," Xueqiu iPhone 12.6.1")
			.cookie("xq_a_token","94777526221ae8fc4c81cb2149de8a17fc86ad94")
			.cookie("xq_id_token","eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjE2NzM5NzIwODAsImlzcyI6InVjIiwiZXhwIjoxNTg3NjE2NTkwLCJjdG0iOjE1ODUwMjQ1OTA4ODIsImNpZCI6IldpQ2lteHBqNUgifQ.DOdhCh6hvi8DpcVtwh2_wmKj4upaYQDmZQxtG8Yurfl4kAeUw7OJksqx9FJaXhXiN97ZzzIcj4KHeIJYGOCUpBS12LqBJzYP1s9UKeaGoWYmgLC1viAuos7EAr1CvEIPTdGFi79xz3ca3jEY6g3GcvSA5ZCUVpfLTMfU770Lp02sO45A1TKdFTZOaGgRiv1vg18BPt6qroRtA9rUwrPj_Lg1S-RlPcuZinf6kxgE9qU9WOehE0jzYCJeeQCBaHBq28wvyiUSNESxoHuylXvA1fft9EmVU-K7x2dOiXiykUX7JzMG-wnO2boi1Xr7Tvagfq-f1SXlxnhb04x8rAH-Qg")
			.cookie("u","1673972080")
			.queryParam("_s", "3e341d")
			.queryParam("_t", "82493CC3-F39D-4411-B735-9A4E2E65CCD2.1673972080.1585123614890.1585123615774")
			.formParam("client_id", "WiCimxpj5H")
			.formParam("client_secret", "TM69Da3uPkFzIdxpTEm6hp")
			.formParam("device_uuid", "82493CC3-F39D-4411-B735-9A4E2E65CCD2")
			.formParam("grant_type", "password")
			.formParam("is_register", "0")
			.formParam("password", "f82d31d5822e98e1eeb81429fb99b687")
			.formParam("username", "112563641@qq.com")
			.formParam("sid", "82493CC3-F39D-4411-B735-9A4E2E65CCD2")
		.when().post("https://api.xueqiu.com/provider/oauth/token")
		.then().log().all()
		.statusCode(400)
//		.body("error_code",equalTo("20082"))
		.extract().response();

		System.out.println(response.getBody().asString());//返回body

		System.out.println("------------------------------------");
		System.out.println(response.asString());//返回body
		System.out.println("------------------------------------");
	}
	
	@Test
	public void testSearch() {
		given()
//			.header("","")
			.contentType(ContentType.JSON)
			.cookie("Cookie","xq_a_token=94777526221ae8fc4c81cb2149de8a17fc86ad94")
			.cookie("Cookie","xq_id_token","eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOjE2NzM5NzIwODAsImlzcyI6InVjIiwiZXhwIjoxNTg3NjE2NTkwLCJjdG0iOjE1ODUwMjQ1OTA4ODIsImNpZCI6IldpQ2lteHBqNUgifQ.DOdhCh6hvi8DpcVtwh2_wmKj4upaYQDmZQxtG8Yurfl4kAeUw7OJksqx9FJaXhXiN97ZzzIcj4KHeIJYGOCUpBS12LqBJzYP1s9UKeaGoWYmgLC1viAuos7EAr1CvEIPTdGFi79xz3ca3jEY6g3GcvSA5ZCUVpfLTMfU770Lp02sO45A1TKdFTZOaGgRiv1vg18BPt6qroRtA9rUwrPj_Lg1S-RlPcuZinf6kxgE9qU9WOehE0jzYCJeeQCBaHBq28wvyiUSNESxoHuylXvA1fft9EmVU-K7x2dOiXiykUX7JzMG-wnO2boi1Xr7Tvagfq-f1SXlxnhb04x8rAH-Qg")
			.cookie("u","1673972080")
			.queryParam("count", "10")
			.queryParam("page", "1")
			.queryParam("q", "sogo")
			.queryParam("sortId", "1")
			.queryParam("source", "all")
			.queryParam("tabId", "1")
		.when().get("https://api.xueqiu.com/query/v1/search/all")
		.then().statusCode(200)
//			.body("data.list[0].find { list -> list.name=='搜狗'}.code",equalTo("SOGO") );
			.body("data.list[0].find { it.name=='搜狗'}.code",equalTo("SOGO") );

		
	}
	
	@Test
	/*
	 * 演示发送时，body用map填充
	 */
	public void testPostJson() {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("id", 1);
		map.put("name","adb");
		map.put("array", new String[] {"111","222"});
		
		given()
			.spec(requestSpecification)	//使用定义好的全局变量
			.contentType(ContentType.JSON)
			.body(map)
//			.body("{\"array\":[\"111\",\"222\"],\"name\":\"adb\",\"id\":1}")
		.when()
			.post("http://www.baidu.com")
		.then()
			.log().all()
//			.spec(responsSpecification)	//使用定义好的全局变量
//			.statusCode(200)
//			.body("",hasXPath("//*[@href='xx']"))	//这么写会报错，可以参考这种方式
			.time(lessThan(1000L))	//断言接口的响应时间低于1秒
			;
	}
	
//	@Test
	/*
	 * 本例没有真实运行，因为http://json-schema.net/网站在我的网络环境下打不开，可能需要Q
	 **schema作用：验证返回的json数据是否符合规定
	 *文件json.schema下是生成的schema文件，里面定义了各个json数据的规则，如字符串的长度大小，数值的大小等，这个文件可以通过http://json-schema.net/生成
	 */
	public void testSchema() {
		given()
		.when().get("https://testerhome.com/api/v3/topics/6040.json")
		.then()
			.statusCode(200)
//			.body(matchesJsonSchema(new File("/tmp/json.schema")))//绝对路径，但实际项目中缺少这个文件
			.body(matchesJsonSchemaInClasspath("/json.schema"))//resource目录下的文件，但实际项目中缺少这个文件
			;
	}
	
	@Test
	public void testFilterAddCookie() {
		given()
			.queryParam("symbol", "SOGO")
//			.cookie("xq_a_token=2ee68b782d6ac072e2a24d81406dd950aacaebe3; xqat=2ee68b782d6ac072e2a24d81406dd950aacaebe3; xq_r_token=f9a2c4e43ce1340d624c8b28e3634941c48f1052; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTU4NzUyMjY2MSwiY3RtIjoxNTg1MzgxODQ1MTMzLCJjaWQiOiJkOWQwbjRBWnVwIn0.JtFg3Me8iCVvUS89_zoU-oSfDEUN9ZQDtdjxU9NQF_PUuHAyHbVAUBiQkvzuJ7yo_-8TA6UdY1XK36b3GQvHW5EIrQ6E4ddsZC2IfNBymaQBajl6QIc7H4d4n8w5SpP0uhn1EpBiqd6EWv-LyJsYXwfrcboh2tbjNb3yGyIC1cvGUBzzqa3iVckfCcKWuXIzvzfJtgoUwfAWdgVVI8BmXdrUp9UphSjs9KiAT_qZUspb-WG9eN8C1Nm9ck8hwcDk-D-QqqHUdUVvMMwGHkZaWLaUD7xPDXpiRga_5LLF3FN6f8M4gvmEZOZFrRpbsxpadiDOV2Ev1mjKgFrYC18DlA; u=941585381876784; Hm_lvt_1db88642e346389874251b5a1eded6e3=1585381878; device_id=24700f9f1986800ab4fcc880530dd0ed; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1585383069")
				/* //已设置全局的filter，先把这里注释掉。这里写也可以
				 * //请求时拦截，并修改header信息：
				 * .filter((req, res, ctx) -> { req.header("aaa","bbb"); Response
				 * responseOrigin=ctx.next(req, res); return responseOrigin; })
				 */				 
			
		.when()
			.get("https://stock.xueqiu.com/v5/stock/batch/quote.json?symbol=SOGO").prettyPeek() //prettyPeek()可以美化相应结果，并打印在控制台
		.then()
			.statusCode(200)
			;
	}
	
	@Test
	/*
	 * base64.json接口返回的是base64加密的内容，演示用filter进行解密
	 */
	public void testFilterInBase64() {
			given()
				.log().all()
				.filter((req,res,ctx) -> {
					Response resOrigin=ctx.next(req, res);
					ResponseBuilder responseBuilder=new ResponseBuilder().clone(resOrigin);
					//解密
					String decodeContentString =new String(
							Base64.getDecoder().decode(
									resOrigin.body().asString().trim()
							)
					);
					responseBuilder.setBody(decodeContentString);
					responseBuilder.setContentType(ContentType.JSON);
					Response newResponse= responseBuilder.build();
					return newResponse;
				})
			
		.when()
			.log().all()
			.get("http://127.0.0.1:8000/no_base64.json")//.prettyPeek()
		.then()
			.log().all()
			.statusCode(200)
			.body("data.items[0].quote.code", equalTo("SOGO"))
			;
	}
	
}
