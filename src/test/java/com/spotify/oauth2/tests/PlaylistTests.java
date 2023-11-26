package com.spotify.oauth2.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;

    String access_token = "BQCvQ7wcNRpRFUrQUT2qNbCZnjaqAoJs9FCBnxWDVPD-UMpnP8hMGIYBY971-om91tL10gFd1yHXzxQ7sZlCfXJDY81d5sgrqOhuhfBgyZiHmtJYywR5a2uw6kcQ8bJ0BuCTyhqjkaZ_TBZLaYoFcmMnthAQDysurtLDhPn7ahhzrGBntETEYHgufsKEpKGCllxxSzZ7YJ-TI52cCzhjkyl0heT2w2bItVgGDk4ZeAKjeByWTAmGqe_nBBuShFBUyrPKj4GuUcRijK_Z";
    String baseUri = "https://api.spotify.com";
    String basePath = "/v1";
    @BeforeClass
    public void beforeClass() {
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri(baseUri).
                setBasePath(basePath).
                addHeader("Authorization", "Bearer " + access_token).
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                log(LogDetail.ALL);
                //expectContentType(ContentType.JSON);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void create_playlist() {
        String payload = "{\n" +
                "    \"name\": \"New Playlist\",\n" +
                "    \"description\": \"Test description\",\n" +
                "    \"public\": false\n" +
                "}";
        String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";
        given(requestSpecification).
                body(payload).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(responseSpecification).
                assertThat().
                statusCode(201).
                body("name", equalTo("New Playlist"),
                        "description", equalTo("Test description"),
                        "public", equalTo(false));
    }
    @Test
    public void get_playlist() {
        String playlist_id = "2WbBqXGoZNMHlLy7GvtrEH";
        given(requestSpecification).
        when().
                get("/playlists/" + playlist_id).
        then().spec(responseSpecification).
                assertThat().
                statusCode(200).
                body("name", equalTo("Updated Playlist 1"),
                        "description", equalTo("Updated playlist description"),
                        "public", equalTo(false));
    }
    @Test
    public void update_playlist() {
        String payload = "{\n" +
                "    \"name\": \"Updated Playlist 1\",\n" +
                "    \"description\": \"Updated playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        String playlist_id = "2WbBqXGoZNMHlLy7GvtrEH";
        given(requestSpecification).
                body(payload).
        when().
                put("/playlists/" + playlist_id).
        then().spec(responseSpecification).
                assertThat().
                statusCode(200);
    }
    @Test
    public void create_playlist_without_name() {
        String payload = "{\n" +
                "    \"name\": \"\",\n" +
                "    \"description\": \"Updated playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";
        given(requestSpecification).
                body(payload).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(responseSpecification).
                assertThat().
                statusCode(400).
                body("error.status", equalTo(400),
                "error.message", equalTo("Missing required field: name"));
    }

    @Test
    public void create_playlist_with_invalid_token() {
        String payload = "{\n" +
                "    \"name\": \"Updated Playlist 1\",\n" +
                "    \"description\": \"Updated playlist description\",\n" +
                "    \"public\": false\n" +
                "}";
        String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";
        given().
                baseUri(baseUri).
                basePath(basePath).
                header("Authorization", "Bearer " + "12345_invalid_token").
                contentType(ContentType.JSON).
                log().all().
                body(payload).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(responseSpecification).
                assertThat().
                statusCode(401).
                body("error.status", equalTo(401),
                        "error.message", equalTo("Invalid access token"));
    }
}
