package com.spotify.oauth2.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;

public class TokenManager {

    private static String access_token;
    private static Instant expiry_time;

    public static String getToken() {
        try {
            if(access_token == null || Instant.now().isAfter(expiry_time)) {
                System.out.println("renewing token...");
                Response response = renewToken();
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 300); //5 min buffer jic
            } else {
                System.out.println("token is still valid");
            }
        }
        catch (Exception e) {
            throw new RuntimeException("failed to get token");
        }
        return access_token;
    }
    private static Response renewToken() {

        HashMap<String, String> formParams = new HashMap<>();
        formParams.put("grant_type", "refresh_token");
        formParams.put("refresh_token", "AQCnjbZ-B8i0FiK3UjAMU3QA4aLadjXct2JU0e1EoHvMSKm8bP3-fDX2BPi9r7vl54AwovdDl22_ivaFLA7md94qgMZWxctpjeNr-Hvy8Z9bnOs5BUYvGA-QKJZpveOVEfA");
        formParams.put("client_id", "8ce78bcf215f483fabf368e80628f702");
        formParams.put("client_secret", "56f72d6608aa48219ac76207bccae51c");

        Response response = RestResource.postAccount(formParams);

        if (response.statusCode() != 200) {
            throw new RuntimeException("failed to renew token");
        }
        return response;
    }

}
