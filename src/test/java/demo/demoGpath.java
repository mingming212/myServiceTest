package demo;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

/*
 * 
 */
public class demoGpath {
	@Test
	/**
	 * 在restAssured中，可以用jsonpath对返回的body进行解析
	 * "jsonpath"使用的是Groovy中的Gpath的语法
	 */
	public void testJson() {
		RestAssured.registerParser("application/octet-stream",Parser.JSON);//访问本地的python -m CGIHTTPServer服务时，需要使用预定义的解析器注册要解析的自定义内容类型
		
		given()
		.when().get("http://127.0.0.1:8000/lotto.json")
		.then().body("lotto.lottoId", equalTo(5))
		.body("lotto.winners.winnerId",hasItems(54,23))//断言：json里的数组
//		.body("**.find{ it.@type == '23'}",hasItems(2))//语法不对，JsonPath不支持**
		;
		
		given()
		.when().get("http://127.0.0.1:8000/store.json")
		.then()
		.body("store.book.category", hasItems("fiction"))//断言：json里的数组	
		.body("store.book[0].author", equalTo("Nigel Rees"))
		.body("store.book.findAll { book -> book.price >=5 && book.price <=15}.size()", equalTo(3))//去book下面找price在5和15之间的元素。 findAll查找所有，返回类型是数组[]。
		.body("store.book.find { book -> book.price ==8.95f}.price", equalTo(8.95f) )//去book下面找price=8.95的元素。  浮点数8.95后面需要加f
		.body("store.book.find { it.price ==8.95f}.price", equalTo(8.95f) )//去book下面找price=8.95的元素。  浮点数8.95后面需要加f
		;
	}
	
	@Test
	/**
	 * XmlPath
	 */
	public void testXML() {
		given()
		.when().get("http://127.0.0.1:8000/shopping.xml")
		.then()
		.body("shopping.category.item[0].name",equalTo("Chocolate") )
		.body("shopping.category.item.size()", equalTo(5))
		.body("shopping.category.findAll {it.@type == 'groceries'}.size()",equalTo(1))//@代表属性，在xml中，只有两个<>之间的才叫属性，需要加@
		.body("shopping.category.item.find {it.price== 20}.name", equalTo("Coffee"))
		.body("**.find {it.price== 20}.name", equalTo("Coffee"))////xmlPath中，可以用**代表任意的，JsonPath不支持**
		;
	}
	
	/*
	 * Html
	 */
	@Test
	public void testHtml() {
		given().log().all()
		.queryParam("wd", "mp3")
		.when().get("http://www.baidu.com/s")
		.then().log().all()
		.statusCode(200)
		.body("html.head.title", equalTo("mp3_百度搜索"));
		
	}
}
