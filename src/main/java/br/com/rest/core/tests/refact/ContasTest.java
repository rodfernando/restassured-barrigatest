package br.com.rest.core.tests.refact;

import br.com.rest.core.BaseTest;
import br.com.rest.core.utils.FuncaoUtils;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ContasTest extends BaseTest {

    @Test
    public void deveIncluirContaComSucesso() {
        given()
                .body("{\"nome\": \"Conta Inserida\"}")
        .when()
                .post("/contas")
        .then()
                .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso() {
        Integer CONTA_ID = FuncaoUtils.getIdContaPeloNome("Conta para alterar");

        given()
                .body("{\"nome\": \"Conta alterada\"}")
                .pathParam("id", CONTA_ID)
                .when()
                .put("/contas/{id}")//mandando o parâmetro id
                .then()
                .statusCode(200)
                .body("nome", is("Conta alterada"))
        ;
    }

    @Test
    public void naoDeveInserirContaMesmoNome() {
        given()
                .body("{\"nome\": \"Conta mesmo nome\"}")
        .when()
                .post("/contas")
        .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }
}
