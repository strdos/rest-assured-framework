package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.pojo.Playlist;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.SpecBuilder.getRequestSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;

public class PlaylistApi {

    static String access_token = "BQDQODxTNQZ6BcG0JPso07PgKduEh4ddlfL4WZzT7rq01kAgxec81DloS3cUg0Ifd6YamLOTRu0J-abRgR51VKOx9bx2QuRA47GiOF5PY07woiyzSEaxdENzPZNxUfgdYvAeAUp3oVKhuov-7D5hPcKldHLX_7o_xwpzYP51ZNvZbbJEQA8cmSXv_sSxGuIHBdmauAU8gmZSGs2AY3mJGX7ngZCrc3mpHhQKMzPCDHbCABa4AbltrYhuGVrem4Y8hNisUFo8AjPm5FZZ";

    public static String user_id = "31mz3gmv65ac3kp7sjiuhnu3hx7i";
    //String playlist_id = "2WbBqXGoZNMHlLy7GvtrEH";
    public static Response post(Playlist requestPlaylist) {

        return given(getRequestSpec()).
                header("Authorization", "Bearer " + access_token).
                body(requestPlaylist).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response post(String token, Playlist requestPlaylist) {

        return given(getRequestSpec()).
                body(requestPlaylist).
                header("Authorization", "Bearer " + token).
        when().
                post("/users/" + user_id + "/playlists").
        then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response get(String playlistId) {
        return given(getRequestSpec()).
                header("Authorization", "Bearer " + access_token).
        when().
                get("/playlists/" + playlistId).
        then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response update(String playlistId, Playlist requestPlaylist) {

        return given(getRequestSpec()).
                header("Authorization", "Bearer " + access_token).
                body(requestPlaylist).
        when().
                put("/playlists/" + playlistId).
        then().spec(getResponseSpec()).
                extract().
                response();
    }
}
