import com.whs.wework.Wework;
import com.whs.wework.WeworkConfig;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class TestGetTocken {
    String corpid="ww89219670ad1e9a4e";
    String corpsecret="4flRSUlS1HteEQ9yQaqcKHmN3_Q10TSkPn7E5sJh0YM";

    @Test
    public void testToken(){
        Wework wework=new Wework();
        String token=wework.getToken();
        assertThat(token,not(equalTo(null)));
    }

    @Test
    public void test(){
        System.out.println("----"+ WeworkConfig.getInstance().corpid);

    }
}
