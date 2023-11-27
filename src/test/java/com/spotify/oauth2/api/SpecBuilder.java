package com.spotify.oauth2.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class SpecBuilder {

    //static String access_token = "BQArZWXFZh9Nhkd9Dz5tH3qZs42jRSAGLQaFayr9chN8zme0P3npkwbcI3EqD_-jHzXAvdJ_03pQ58q2LZfIQvVdHnTbjH9E9kNmvNnzfu80Sk47ES66f15U8Tv_sly74MSGmaHnssez1SrHuJ-OvdQ1ywNoQA3l_TSRvkcj4EcsbMlH4E53d0Gs1n8r-yYhD-go4v74eCYlsSNKQAaHW3t2GokpNQgH0PS2ROn9e6_nD12_sr8kACE3EQ7RHQKSCSnpo-rfdDJCcaY6";
    public static String baseUri = "https://api.spotify.com";
    public static String basePath = "/v1";

    public static RequestSpecification getRequestSpec() {

        return new RequestSpecBuilder().
                setBaseUri(baseUri).
                setBasePath(basePath).
                //addHeader("Authorization", "Bearer " + access_token).
                setContentType(ContentType.JSON).
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
