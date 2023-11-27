package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.SpecBuilder;
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

import static com.spotify.oauth2.api.SpecBuilder.getRequestSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests {

    @Test
    public void create_playlist() {
        // using the builder pattern with POJO classes
        Playlist requestPlaylist = new Playlist().
                setName("Test Playlist").
                setDescription("Test Description").
                setPublic(false);

        String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";

        Playlist responsePlaylist = given(getRequestSpec()).
                body(requestPlaylist).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(getResponseSpec()).
                assertThat().
                statusCode(201).
                extract().response().as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));

    }
    @Test
    public void get_playlist() {
        Playlist requestPlaylist = new Playlist().
                setName("Updated Playlist").
                setDescription("Updated Description").
                setPublic(false);

        String playlist_id = "2WbBqXGoZNMHlLy7GvtrEH";
        Playlist responsePlaylist = given(getRequestSpec()).
        when().
                get("/playlists/" + playlist_id).
        then().spec(getResponseSpec()).
                assertThat().
                statusCode(200).
                extract().response().as(Playlist.class);

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
    }
    @Test
    public void update_playlist() {
/*      not using the builder pattern
        Playlist requestPlaylist = new Playlist();
        requestPlaylist.setName("Updated Playlist");
        requestPlaylist.setDescription("Updated Description");
        requestPlaylist.setPublic(false);*/

        //using the builder pattern:
        Playlist requestPlaylist = new Playlist().
                setName("Updated Playlist").
                setDescription("Updated Description").
                setPublic(false);

        String playlist_id = "2WbBqXGoZNMHlLy7GvtrEH";
        given(getRequestSpec()).
                body(requestPlaylist).
        when().
                put("/playlists/" + playlist_id).
        then().spec(getResponseSpec()).
                assertThat().
                statusCode(200);
    }
    @Test
    public void create_playlist_without_name() {
        Playlist requestPlaylist = new Playlist().
                setName("").
                setDescription("Updated Description").
                setPublic(false);

        String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";

        Error error = given(getRequestSpec()).
                body(requestPlaylist).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(getResponseSpec()).
                assertThat().
                statusCode(400).
                extract().response().as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(400));
        assertThat(error.getError().getMessage(), equalTo("Missing required field: name"));
    }

    @Test
    public void create_playlist_with_invalid_token() {
        Playlist requestPlaylist = new Playlist().
                setName("Test Playlist").
                setDescription("Test Description").
                setPublic(false);

        String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";
        Error error = given().
                baseUri(SpecBuilder.baseUri).
                basePath(basePath).
                header("Authorization", "Bearer " + "12345_invalid_token").
                contentType(ContentType.JSON).
                log().all().
                body(requestPlaylist).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(getResponseSpec()).
                assertThat().
                statusCode(401).
                extract().response().as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(401));
        assertThat(error.getError().getMessage(), equalTo("Invalid access token"));
    }
}
