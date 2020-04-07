package com.github.fenixsoft.bookstore.resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author icyfenix@gmail.com
 * @date 2020/4/6 23:12
 **/
class AdvertisementResourceTest extends JAXRSResourceBase {

    @Test
    void getAllAdvertisements() {
        assertOK(get("/advertisements"));
    }
}
