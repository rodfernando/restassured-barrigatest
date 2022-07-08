package br.com.rest.core;

import io.restassured.http.ContentType;

public interface Constantes {
    String APP_BASE_URL = "https://barrigarest.wcaquino.me";
    Integer APP_PORT = 443; //optativo -> http = 80
    String APP_BASE_PATH = "";

    //todas as requisições nesse projeto serão Json
    ContentType APP_CONTENT_TYPE = ContentType.JSON;

    long MAX_TIMEOUT = 5000L;
}
