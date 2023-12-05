package com.spotify.oauth2.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static com.spotify.oauth2.api.Route.BASE_PATH;

public class SpecBuilder {
    public static String baseUri = "https://api.spotify.com";
    public static String baseUriAccounts = "https://accounts.spotify.com";
    public static String basePath = BASE_PATH;

    public static RequestSpecification getRequestSpec() {

        return new RequestSpecBuilder().
                //setBaseUri(System.getProperty("BASE_URI")).  //to use with mvn for multiple stands (mvn test -DBASE_URI="https://api.spotify.com")
                setBaseUri(baseUri).
                setBasePath(basePath).
                setContentType(ContentType.JSON).
                addFilter(new AllureRestAssured()).
                log(LogDetail.ALL).
                build();
    }

    public static RequestSpecification getAccountRequestSpec() {

        return new RequestSpecBuilder().
                //setBaseUri(System.getProperty("ACCOUNT_BASE_URI")).
                setBaseUri(baseUriAccounts).
                setContentType(ContentType.URLENC).
                addFilter(new AllureRestAssured()).
                log(LogDetail.ALL).
                build();
    }

    public static ResponseSpecification getResponseSpec() {

        return new ResponseSpecBuilder().
                log(LogDetail.ALL).
                //expectContentType(ContentType.JSON);
                build();
    }
}
