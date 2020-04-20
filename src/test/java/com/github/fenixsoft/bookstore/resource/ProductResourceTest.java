package com.github.fenixsoft.bookstore.resource;

import com.github.fenixsoft.bookstore.domain.payment.Stockpile;
import com.github.fenixsoft.bookstore.domain.warehouse.Product;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.Assert;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author icyfenix@gmail.com
 * @date 2020/4/6 23:16
 **/
class ProductResourceTest extends JAXRSResourceBase {

    @Test
    void getAllProducts() {
        assertOK(get("/products"));
    }

    @Test
    void getProduct() {
        assertOK(get("/products/1"));
        assertNoContent(get("/products/10086"));
        Product book = get("/products/1").readEntity(Product.class);
        assertEquals("深入理解Java虚拟机（第3版）", book.getTitle());
    }

    @Test
    void updateProduct() {
        final Product book = get("/products/1").readEntity(Product.class);
        book.setTitle("深入理解Java虚拟机（第4版）");
        assertForbidden(put("/products", book));
        authenticatedScope(() -> assertOK(put("/products", book)));
        Product modifiedBook = get("/products/1").readEntity(Product.class);
        assertEquals("深入理解Java虚拟机（第4版）", modifiedBook.getTitle());
    }

    @Test
    void createProduct() {
        final Product book = new Product();
        book.setTitle("new book");
        book.setPrice(50.0);
        book.setRate(8.0f);
        assertForbidden(post("/products", book));
        authenticatedScope(() -> {
            Response response = post("/products", book);
            assertOK(response);
            Product fetchBook = response.readEntity(Product.class);
            assertEquals(book.getTitle(), fetchBook.getTitle());
            assertNotNull(fetchBook.getId());
        });
    }

    @Test
    void removeProduct() throws JSONException {
        int number = jsonArray(get("/products")).length();
        assertForbidden(delete("/products/1"));
        authenticatedScope(() -> assertOK(delete("/products/1")));
        assertEquals(number - 1, jsonArray(get("/products")).length());
    }

    @Test
    void updateAndQueryStockpile() {
        authenticatedScope(() -> {
            assertOK(patch("/products/stockpile/1?amount=20"));
            Stockpile stockpile = get("/products/stockpile/1").readEntity(Stockpile.class);
            assertEquals(20, stockpile.getAmount());
        });
    }
}
