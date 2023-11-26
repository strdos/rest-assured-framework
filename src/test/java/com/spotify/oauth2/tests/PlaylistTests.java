package com.spotify.oauth2.tests;

import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
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
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Test Playlist");
        requestPlaylist.setDescription("Test Description");
        requestPlaylist.setPublic(false);

        String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";

        Playlist responsePlaylist = given(requestSpecification).
                body(requestPlaylist).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(responseSpecification).
                assertThat().
                statusCode(201).
                extract().response().as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));

    }
    @Test
    public void get_playlist() {
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Updated Playlist");
        requestPlaylist.setDescription("Updated Description");
        requestPlaylist.setPublic(false);

        String playlist_id = "2WbBqXGoZNMHlLy7GvtrEH";
        Playlist responsePlaylist = given(requestSpecification).
        when().
                get("/playlists/" + playlist_id).
        then().spec(responseSpecification).
                assertThat().
                statusCode(201).
                extract().response().as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
    }
    @Test
    public void update_playlist() {
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Updated Playlist");
        requestPlaylist.setDescription("Updated Description");
        requestPlaylist.setPublic(false);

        String playlist_id = "2WbBqXGoZNMHlLy7GvtrEH";
        Playlist responsePlaylist = given(requestSpecification).
                body(requestPlaylist).
        when().
                put("/playlists/" + playlist_id).
        then().spec(responseSpecification).
                assertThat().
                statusCode(201).
                extract().response().as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
    }
    @Test
    public void create_playlist_without_name() {
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("");
        requestPlaylist.setDescription("Updated Description");
        requestPlaylist.setPublic(false);

        String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";

        Error error = given(requestSpecification).
                body(requestPlaylist).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(responseSpecification).
                assertThat().
                statusCode(400).
                extract().response().as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(400));
        assertThat(error.getError().getMessage(), equalTo("Missing required field: name"));
    }

    @Test
    public void create_playlist_with_invalid_token() {
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Test Playlist");
        requestPlaylist.setDescription("Test Description");
        requestPlaylist.setPublic(false);

        String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";
        Error error = given().
                baseUri(baseUri).
                basePath(basePath).
                header("Authorization", "Bearer " + "12345_invalid_token").
                contentType(ContentType.JSON).
                log().all().
                body(requestPlaylist).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(responseSpecification).
                assertThat().
                statusCode(401).
                extract().response().as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(401));
        assertThat(error.getError().getMessage(), equalTo("Invalid access token"));
    }
}
