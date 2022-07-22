package br.com.rest.core.tests.refact.suite;

import br.com.rest.core.BaseTest;
import br.com.rest.core.tests.refact.AuthTest;
import br.com.rest.core.tests.refact.ContasTest;
import br.com.rest.core.tests.refact.MovimentacaoTest;
import br.com.rest.core.tests.refact.SaldoTest;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@RunWith(org.junit.runners.Suite.class)
//Conjunto de classes de teste:
@org.junit.runners.Suite.SuiteClasses({
        ContasTest.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})
public class Suite extends BaseTest {

    @BeforeClass
    public static void setupLogin() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "rodrigo@oliveira");
        login.put("senha", "12345");

        String TOKEN = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token")
                ;

        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);

        RestAssured.get("/reset").then().statusCode(200);
    }
}
