package br.com.rest.core.tests;

import br.com.rest.core.BaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BarrigaTest extends BaseTest {

    //O token ficará numa propriedade para que os testes o vejam
    private String TOKEN;


    @Before
    public void setupLogin() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "rodrigo@oliveira");
        login.put("senha", "12345");

        TOKEN = given()
                .body(login)
        .when()
                .post("/signin")
        .then()
                .statusCode(200)
                .extract().path("token")
        ;
    }

    @Test
    public void naoDeveAcessarAPISemToken() {
        given()
        .when()
            .get("/contas")//lembrar que existe um atributo estático
        .then()
            .statusCode(401) //Não autorizado
        ;
    }

    /**
     * Precisa fazer primeiro o login para extrair o token da requisição
     */
    @Test
    public void deveIncluirContaComSucesso() {
        given()
            //{"nome": "conta qualquer"}
            .header("Authorization", "JWT " + TOKEN)
            .body("{\"nome\": \"conta qualquer\"}")
        .when()
            .post("/contas")
        .then()
            .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso() {
        given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\": \"conta alterada\"}")
        .when()
                /*O id da conta pode ser vista acessando a url da aba de contas
                * https://seubarriga.wcaquino.me/editarConta?id=1263750*/
                .put("/contas/1263750") //:id
        .then()
                .statusCode(200)
                .body("nome", is("conta alterada"))
        ;
    }

    @Test
    public void naoDeveInserirContaMesmoNome() {
        given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\": \"conta alterada\"}")
        .when()
                .post("/contas")
        .then()
                .statusCode(400) //Bad Request
                .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void deveInserirMovimentacaoComSucesso() {
        /*como forma de exemplo, poderíamos utilizar o MAP.
        * Porém, foi criada uma classe Movimentacao*/
        Movimentacao mov = getMovimentacaoValida();

        given()
                .header("Authorization", "JWT " + TOKEN)
                .body(mov)
        .when()
                .post("/transacoes")
        .then()
                .statusCode(201)
        ;
    }

    @Test
    public void deveValidarCamposObrigatoriosN() {
        given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{}") //todo par de objeto Json é encapsulado dentro de chaves
        .when()
                .post("/transacoes")
        .then()
                .statusCode(400)
                .body("$", hasSize(8)) //na Raíz possui 8 mensagens
                .body("msg", hasItems(
                        "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório"
                ))
        ;
    }

    @Test
    public void naoDeveInserirMovimentacaoComDataFutura() {
        /*como forma de exemplo, poderíamos utilizar o MAP.
         * Porém, foi criada uma classe Movimentacao*/
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao("10/07/2022"); //É uma boa estratégia não utilizar dados fututos estáticos

        given()
                .header("Authorization", "JWT " + TOKEN)
                .body(mov)
        .when()
                .post("/transacoes")
        .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItem("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    @Test
    public void naoDeveRemoverContaComMovimentacao() {
        given()
                .header("Authorization", "JWT " + TOKEN)
        .when()
                .delete("/contas/1263750")
        .then()
                .statusCode(500) //Erro de Integridade do sistema
                .body("constraint",is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void deveCalcularSaldoConta() {
        given()
                .header("Authorization", "JWT " + TOKEN)
        .when()
                .get("/saldo")
        .then()
                .statusCode(200)
                .body("find{it.conta_id == 1263750}.saldo", is("423.00"))
        ;
    }

    @Test
    public void deveRemoverMovimentacao() {
        given()
                .header("Authorization", "JWT " + TOKEN)
        .when()
                //o id foi pego no objeto Ações na URL https://seubarriga.wcaquino.me/extrato
                .delete("/transacoes/1178255")
        .then()
                .statusCode(204) //indica que a solicitação foi bem sucedida e o cliente não precisa sair da página atual
        ;
    }



    //Função
    private Movimentacao getMovimentacaoValida() {
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(1263750);
        mov.setDescricao("Descricao de movimentacao");
        mov.setEnvolvido("Rodrigo");
        mov.setTipo("REC");
        mov.setData_transacao("01/01/2022");
        mov.setData_pagamento("10/05/2022");
        mov.setValor(100f);
        mov.setStatus(true);
        return mov;
    }
}
