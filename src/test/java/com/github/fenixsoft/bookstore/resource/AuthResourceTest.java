package com.github.fenixsoft.bookstore.resource;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

/**
 * @author icyfenix@gmail.com
 * @date 2020/4/7 16:47
 **/
public class AuthResourceTest extends JAXRSResourceBase {

    @Test
    void refreshToken() throws JSONException {
        String prefix = "http://localhost:" + port + "/oauth/token?";
        String url = prefix + "username=icyfenix&password=MFfTW3uNI4eqhwDkG7HP9p2mzEUu%2Fr2&grant_type=password&client_id=bookstore_frontend&client_secret=bookstore_secret";
        Response resp = ClientBuilder.newClient().target(url).request().get();
        String refreshToken = json(resp).getString("refresh_token");
        url = prefix + "refresh_token=" + refreshToken + "&grant_type=refresh_token&client_id=bookstore_frontend&client_secret=bookstore_secret";
        resp = ClientBuilder.newClient().target(url).request().get();
        String accessToken = json(resp).getString("access_token");
        Assertions.assertNotNull(accessToken);
    }

}
