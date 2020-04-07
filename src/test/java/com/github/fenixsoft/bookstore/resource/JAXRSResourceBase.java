package com.github.fenixsoft.bookstore.resource;

import com.github.fenixsoft.bookstore.BookstoreApplication;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 单元测试基类
 * <p>
 * 提供对JAX-RS资源的HTTP访问方法、登录授权、JSON字符串访问等支持
 *
 * @author icyfenix@gmail.com
 * @date 2020/4/6 19:32
 **/
@SpringBootTest(classes = BookstoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JAXRSResourceBase extends com.github.fenixsoft.bookstore.DBRollbackBase {

    @Value("${local.server.port}")
    int port;

    private String accessToken = null;

    Invocation.Builder build(String path) {
        Invocation.Builder builder = ClientBuilder.newClient().target("http://localhost:" + port + "/restful" + path)
                .property(HttpUrlConnectorProvider.SET_METHOD_WORKAROUND, true)
                .request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        if (accessToken != null) {
            builder.header("Authorization", "bearer " + accessToken);
        }
        return builder;
    }

    JSONObject json(Response response) throws JSONException {
        return new JSONObject(response.readEntity(String.class));
    }

    JSONArray jsonArray(Response response) throws JSONException {
        return new JSONArray(response.readEntity(String.class));
    }

    /**
     * 单元测试中登陆固定使用icyfenix这个用户
     */
    void login() {
        String url = "http://localhost:" + port + "/oauth/token?username=icyfenix&password=MFfTW3uNI4eqhwDkG7HP9p2mzEUu%2Fr2&grant_type=password&client_id=bookstore_frontend&client_secret=bookstore_secret";
        Response resp = ClientBuilder.newClient().target(url).request().get();
        try {
            accessToken = json(resp).getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void logout() {
        accessToken = null;
    }

    void authenticatedScope(Runnable runnable) {
        try {
            login();
            runnable.run();
        } finally {
            logout();
        }
    }

    <T> T authenticatedGetter(Supplier<T> supplier) {
        try {
            login();
            return supplier.get();
        } finally {
            logout();
        }
    }

    Response get(String path) {
        return build(path).get();
    }

    Response delete(String path) {
        return build(path).delete();
    }

    Response post(String path, Object entity) {
        return build(path).post(Entity.json(entity));
    }

    Response put(String path, Object entity) {
        return build(path).put(Entity.json(entity));
    }

    Response patch(String path) {
        return build(path).method("PATCH", Entity.text("MUST_BE_PRESENT"));
    }

    static void assertOK(Response response) {
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus(), "期望HTTP Status Code应为：200/OK");
    }

    static void assertNoContent(Response response) {
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus(), "期望HTTP Status Code应为：204/NO_CONTENT");
    }

    static void assertBadRequest(Response response) {
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus(), "期望HTTP Status Code应为：400/BAD_REQUEST");
    }

    static void assertForbidden(Response response) {
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus(), "期望HTTP Status Code应为：403/FORBIDDEN");
    }

    static void assertServerError(Response response) {
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus(), "期望HTTP Status Code应为：500/INTERNAL_SERVER_ERROR");
    }

    static void assertNotFound(Response response) {
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus(), "期望HTTP Status Code应为：404/NOT_FOUND");
    }


}
