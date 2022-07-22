package br.com.rest.core.utils;

import io.restassured.RestAssured;

public class FuncaoUtils {

    /*lembrar de deixar o métodos estático para que todas as classes possam utilizar
    * sem precisar instanciar a classe*/
    public static Integer getIdContaPeloNome(String nome) {
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }

    public static Integer getIdMovPelaDescricao(String desc) {
        return RestAssured.get("/transacoes?descricao="+desc).then().extract().path("id[0]");
    }
}
