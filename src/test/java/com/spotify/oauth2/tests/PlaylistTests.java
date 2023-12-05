package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.spotify.oauth2.utils.FakerUtils.generateDescription;
import static com.spotify.oauth2.utils.FakerUtils.generateName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Epic("Spotify Oauth 2.0")
@Feature("Playlist API")
public class PlaylistTests extends BaseTest {

    @Step
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

    @Step
    public void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist) {

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        //assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    @Step
    public void assertStatusCode(int actualStatusCode, int expectedStatusCode) {
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }

    @Step
    public void assertError(Error responseErr, int expectedStatusCode, String expectedMsg) {

        assertThat(responseErr.getError().getStatus(), equalTo(expectedStatusCode));
        assertThat(responseErr.getError().getMessage(), equalTo(expectedMsg));
    }

    @Story("Create a playlist story")
    @Link("https://example.org")
    @Link(name = "allure", type = "mylink")
    @TmsLink("12345")
    @Issue("12345")
    @Description("create a playlist")
    @Test
    public void create_playlist() {

        //Playlist requestPlaylist = playlistBuilder("Test Playlist", "Test Description", false);
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);

        Response response = PlaylistApi.post(requestPlaylist);
        //assertThat(response.statusCode(), equalTo(201));
        assertStatusCode(response.statusCode(), StatusCode.CODE_201.getCode());

        Playlist responsePlaylist = response.as(Playlist.class);

        assertPlaylistEqual(responsePlaylist, requestPlaylist);
    }

    @Test
    public void get_playlist() {

        Playlist requestPlaylist = playlistBuilder("Updated Playlist", "Updated Description", false);

        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200.getCode());

        Playlist responsePlaylist = response.as(Playlist.class);

        assertPlaylistEqual(responsePlaylist, requestPlaylist);
    }

    @Test
    public void update_playlist() {

        Playlist requestPlaylist = playlistBuilder("Updated Playlist", "Updated Description", false);
        //Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);

        Response response = PlaylistApi.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_200.getCode());
    }

    @Story("Create a playlist story")
    @Test
    public void create_playlist_without_name() {
        //Playlist requestPlaylist = playlistBuilder("", "Updated Description", false);
        Playlist requestPlaylist = playlistBuilder("", generateDescription(), false);

        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_400.getCode());

        Error error = response.as(Error.class);

        assertError(error, StatusCode.CODE_400.getCode(), StatusCode.CODE_400.getMsg());
    }

    @Story("Create a playlist story")
    @Test
    public void create_playlist_with_invalid_token() {

        String invalid_token = "12345_invalid_token";

        //Playlist requestPlaylist = playlistBuilder("Test Playlist", "Test Description", false);
        Playlist requestPlaylist = playlistBuilder(generateName(), generateDescription(), false);

        Response response = PlaylistApi.post(invalid_token, requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_401.getCode());

        Error error = response.as(Error.class);

        assertError(error, StatusCode.CODE_401.getCode(), StatusCode.CODE_401.getMsg());
    }
}
