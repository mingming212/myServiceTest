package com.whs.wework;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.HarReaderException;
import de.sstoehr.harreader.model.Har;
import de.sstoehr.harreader.model.HarEntry;
import de.sstoehr.harreader.model.HarRequest;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.HashMap;

class ApiTest {

    @Test
    void templateFromYaml() {
        Api api=new Api();
        api.getResponseFromYaml("/api/list.yaml",null).then().statusCode(200);
    }

    @Test
        //没用，只是为了验证语法，可删
    void testResource(){
//        System.out.println(getClass().getResource("/api/app.har.json").getFile());
//        System.out.println(getClass().getResourceAsStream("/api/app.har.json"));

        HarReader harReader=new HarReader();
        try {
            Har har=harReader.readFromFile(new File(getClass().getResource("/api/app.har.json").getFile()));//？？？中文路径，可能出现乱码问题
            HarRequest request=new HarRequest();
            for(HarEntry entry:har.getLog().getEntries()){
                request=entry.getRequest();
                if(request.getUrl().matches("tid=47")) {
                    break;
                }
            }
/*                Har har = harReader.readFromFile(new File(
                        URLDecoder.decode(
                                getClass().getResource("/api/app.har.json").getPath(), "utf-8"
                        )));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            */} catch (HarReaderException e) {
            e.printStackTrace();
        }

    }

    @Test
    void getApiFromYaml() {
        Api api=new Api();
        System.out.println(api.getApiFromHar("/api/app.har.json",".*tid=67.*").url);
        System.out.println(api.getApiFromHar("/api/app.har.json",".*tid=41.*").url);//预期：报错，因为匹配不到tid=41
        System.out.println(api.getApiFromHar("/api/app.har.json",".*tid=1.*").url);//不知道为啥，如果前面写过匹配41，后面再匹配错的话，就不抛出异常了，未解之谜。。
    }

    @Test
    void testMatches(){
        String a="https://work.weixin.qq.com/api/devtools/devhandler.php?tid=67&access_token=gs4n_tfZfSWNnLxtJx_Qsww8tpRN_7fsglgvhencsjNO1uR4mvylY2vfy42sX_Oub1i1rjstiWi3D-bk4qybWhpwPHR9yQ9D-T-huOvRCO0RzLrcetj5foV1wgoXhb6fKm5f8oZa-SH4hbgenoL-FYfEuxvxOaKusrWpNAwl4NSBD_4_l4eDPFysBGTj1HDrvqt57Nij_P-jzT1jFV9v_Q&f=json";
        System.out.println(a.matches(".*tid=27.*"));
    }

    @Test
    void getResponseFromHar() {
        Api api=new Api();
        api.getResponseFromHar("/api/app.har.json",".*tid=67.*",null);
    }

    /**
     * 与其他case独立，仅演示mustache的使用
     */
    @Test
    void mustacheTest() throws IOException {
        HashMap<String, Object> scope=new HashMap<String, Object>();
        scope.put("name","11发觉sfe");

        Writer writer=new OutputStreamWriter(System.out);
        MustacheFactory mf = new DefaultMustacheFactory();
//        Mustache mustache = mf.compile(new StringReader("这个参数：{{name}} 会被替换成map里的值"),"example");//这种用法，会打印"这个参数：1111 会被替换成map里的值"，后一个参数example不知道啥作用
//        Mustache mustache = mf.compile(new StringReader("{{name}}, {{feature.description}}!"), "example");//这种用法，会打印"1111, !"
        Mustache mustache = mf.compile("data/create.mustache");//会打印文档中的内容，且替换掉文档中的{{name}}
        mustache.execute(writer,scope);
        writer.flush();
    }
}