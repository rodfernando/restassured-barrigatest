package br.com.rest.core.tests.refact;

import br.com.rest.core.BaseTest;
import br.com.rest.core.utils.FuncaoUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SaldoTest extends BaseTest {

    @Test
    public void deveCalcularSaldoConta() {
        Integer CONTA_ID = FuncaoUtils.getIdContaPeloNome("Conta para saldo");

        given()
        .when()
                .get("/saldo")
        .then()
                .statusCode(200)
                .body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00"))
        ;
    }
}
