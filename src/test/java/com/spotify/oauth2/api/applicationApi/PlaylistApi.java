package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.Route.PLAYLISTS;
import static com.spotify.oauth2.api.Route.USERS;
import static com.spotify.oauth2.api.TokenManager.getToken;

public class PlaylistApi {

    public static String user_id = "/31mz3gmv65ac3kp7sjiuhnu3hx7i";
    //static String access_token = "BQDjmXBIXMdyRAdCdzcW3OVlwR1Kpy9YrmV41-MVCMtgyJcYeU9YdIRjq3wo_4m7v71QJBa6rVLAyl_k5rjyGWhNaOztxvffPe2vMREa9sADgMdaLnO730w-t-ptmovZ3ttgZMDWuqu2rEDLK0Xpv0snHU0jo3vxRBeTHIFU2AtHEyeY4tVDsDgSWAgPX0HM6hiZC-MObuR-J5fMqUuZg3szcNnF0UWYqQe49AEEoeTxsCYyL8M5q-n0emw6QydXVbd5WUp1_VZRNRaX";

    //String playlist_id = "2WbBqXGoZNMHlLy7GvtrEH";
    @Step
    public static Response post(Playlist requestPlaylist) {

        return RestResource.post(USERS + ConfigLoader.getInstance().getUserId() + PLAYLISTS, getToken(), requestPlaylist);
    }

    public static Response post(String token, Playlist requestPlaylist) {

        //return RestResource.post(USERS + "/" + ConfigLoader.getInstance().getUserId() + PLAYLISTS, token, requestPlaylist);
        return RestResource.post(USERS + ConfigLoader.getInstance().getUserId() + PLAYLISTS, token, requestPlaylist);
    }

    public static Response get(String playlistId) {

        return RestResource.get(PLAYLISTS + "/" + playlistId, getToken());
    }

    public static Response update(String playlistId, Playlist requestPlaylist) {

        return RestResource.update(PLAYLISTS + "/" + playlistId, getToken(), requestPlaylist);
    }
}
