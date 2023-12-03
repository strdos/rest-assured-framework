package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.SpecBuilder;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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

    public Playlist playlistBuilder(String name, String description, boolean _public) {

/*        Playlist playlist = new Playlist();
        playlist.setName(name);
        playlist.setDescription(description);
        playlist.set_public(_public);
        return playlist;*/

        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).
                build();
    }

    public void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist) {

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        //assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    public void assertStatusCode(int actualStatusCode, int expectedStatusCode) {
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }

    public void assertError(Error responseErr, int expectedStatusCode, String expectedMsg) {

        assertThat(responseErr.getError().getStatus(), equalTo(expectedStatusCode));
        assertThat(responseErr.getError().getMessage(), equalTo(expectedMsg));
    }

    @Test
    public void create_playlist() {

        Playlist requestPlaylist = playlistBuilder("Test Playlist", "Test Description", false);

        Response response = PlaylistApi.post(requestPlaylist);
        //assertThat(response.statusCode(), equalTo(201));
        assertStatusCode(response.statusCode(), 201);

        Playlist responsePlaylist = response.as(Playlist.class);

        assertPlaylistEqual(responsePlaylist, requestPlaylist);
    }
    @Test
    public void get_playlist() {

        Playlist requestPlaylist = playlistBuilder("Updated Playlist", "Updated Description", false);

        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertStatusCode(response.statusCode(), 200);

        Playlist responsePlaylist = response.as(Playlist.class);

        assertPlaylistEqual(responsePlaylist, requestPlaylist);
    }
    @Test
    public void update_playlist() {

        Playlist requestPlaylist = playlistBuilder("Updated Playlist", "Updated Description", false);

        Response response = PlaylistApi.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertStatusCode(response.statusCode(), 200);
    }
    @Test
    public void create_playlist_without_name() {
        Playlist requestPlaylist = playlistBuilder("", "Updated Description", false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), 400);

        Error error = response.as(Error.class);

        assertError(error, 400, "Missing required field: name");
    }

    @Test
    public void create_playlist_with_invalid_token() {

        String invalid_token = "12345_invalid_token";

        Playlist requestPlaylist = playlistBuilder("Test Playlist", "Test Description", false);

        Response response = PlaylistApi.post(invalid_token, requestPlaylist);
        assertStatusCode(response.statusCode(), 401);

        Error error = response.as(Error.class);

        assertError(error, 401, "Invalid access token");
    }
}
