package com.github.fenixsoft.bookstore.resource;

import com.github.fenixsoft.bookstore.domain.account.Account;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author icyfenix@gmail.com
 * @date 2020/4/6 18:52
 **/

class AccountResourceTest extends JAXRSResourceBase {

    @Test
    void getUserWithExistAccount() {
        Response resp = get("/accounts/icyfenix");
        assertOK(resp);
        Account icyfenix = resp.readEntity(Account.class);
        assertEquals("icyfenix", icyfenix.getUsername(), "should return user: icyfenix");
    }

    @Test
    void getUserWithNotExistAccount() {
        assertNoContent(get("/accounts/nobody"));
    }

    @Test
    void createUser() {
        Account newbee = new Account();
        newbee.setUsername("newbee");
        newbee.setEmail("newbee@github.com");
        assertBadRequest(post("/accounts", newbee));
        newbee.setTelephone("13888888888");
        newbee.setName("somebody");
        assertNoContent(get("/accounts/newbee"));
        assertOK(post("/accounts", newbee));
        assertOK(get("/accounts/newbee"));
    }

    @Test
    void updateUser() {
        authenticatedScope(() -> {
            Response resp = get("/accounts/icyfenix");
            Account icyfenix = resp.readEntity(Account.class);
            icyfenix.setName("zhouzhiming");
            assertOK(put("/accounts", icyfenix));
            assertEquals("zhouzhiming", get("/accounts/icyfenix").readEntity(Account.class).getName(), "should get the new name now");
        });
    }
}
