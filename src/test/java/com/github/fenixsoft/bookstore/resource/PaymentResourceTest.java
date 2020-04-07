package com.github.fenixsoft.bookstore.resource;

import com.github.fenixsoft.bookstore.applicaiton.payment.dto.Settlement;
import com.github.fenixsoft.bookstore.domain.payment.Payment;
import com.github.fenixsoft.bookstore.domain.payment.Stockpile;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author icyfenix@gmail.com
 * @date 2020/4/7 14:31
 **/
class PaymentResourceTest extends JAXRSResourceBase {

    private Settlement createSettlement() {
        Settlement settlement = new Settlement();
        Settlement.Item item = new Settlement.Item();
        Settlement.Purchase purchase = new Settlement.Purchase();
        settlement.setItems(Collections.singletonList(item));
        settlement.setPurchase(purchase);
        item.setAmount(2);
        item.setProductId(1);
        purchase.setLocation("xx rd. zhuhai. guangdong. china");
        purchase.setName("icyfenix");
        purchase.setPay("wechat");
        purchase.setTelephone("18888888888");
        return settlement;
    }

    @Test
    void executeSettlement() {
        final Settlement settlement = createSettlement();
        assertForbidden(post("/settlements", settlement));
        authenticatedScope(() -> {
            Response response = post("/settlements", settlement);
            assertOK(response);
            Payment payment = response.readEntity(Payment.class);
            assertNotNull(payment.getPayId());
        });
    }

    @Test
    void updatePaymentState() {
        final Settlement settlement = createSettlement();
        authenticatedScope(() -> {
            Payment payment = post("/settlements", settlement).readEntity(Payment.class);
            assertOK(patch("/pay/" + payment.getPayId() + "?state=PAYED"));
            assertServerError(patch("/pay/" + payment.getPayId() + "?state=CANCEL"));
            payment = post("/settlements", settlement).readEntity(Payment.class); // another
            assertOK(patch("/pay/" + payment.getPayId() + "?state=CANCEL"));
            assertServerError(patch("/pay/" + payment.getPayId() + "?state=NOT_SUPPORT"));
        });
    }

    @Test
    void updatePaymentStateAlias() {
        Payment payment = authenticatedGetter(() -> post("/settlements", createSettlement()).readEntity(Payment.class));
        assertOK(get(payment.getPaymentLink()));
    }

    @Test
    void updateAndQueryStockpile() {
        authenticatedScope(() -> {
            assertOK(patch("/pay/stockpile/1?amount=20"));
            Stockpile stockpile = get("/pay/stockpile/1").readEntity(Stockpile.class);
            assertEquals(20, stockpile.getAmount());
        });

    }

}
