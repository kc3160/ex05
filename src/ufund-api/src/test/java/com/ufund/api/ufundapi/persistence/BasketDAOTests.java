package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Basket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.TemporaryNeed;

public class BasketDAOTests {
    private String tmpFileName;
    private BasketRepository basketRepository;
    private ObjectMapper mockObjectMapper;
    private NeedRepository needRepository;
    private Basket[] baskets;

    @BeforeEach
    public void setupNeedRepository() throws IOException {
        tmpFileName = "testing.tmp";
        mockObjectMapper = mock(ObjectMapper.class);
        needRepository = mock(CupboardDAO.class);

        baskets = new Basket[2];
        baskets[0] = new Basket(0);
        baskets[1] = new Basket(1);

        baskets[1].addtoBasket(new TemporaryNeed(0, "Need 0", 1.59, 10));
        baskets[1].addtoBasket(new TemporaryNeed(3, "Need 3", 599.99, 10));
        baskets[1].addtoBasket(new TemporaryNeed(4, "Need 4", 24.99, 10));
        baskets[0].addtoBasket(new TemporaryNeed(14, "Delete Me 14", 1.00, 10));

        when(mockObjectMapper.
            readValue(new File(tmpFileName), Basket[].class))
                .thenReturn(baskets);

        basketRepository = new BasketDAO(tmpFileName, mockObjectMapper, needRepository);
    }

    @Test
    void testGetBasket_Success() throws IOException {
        int basketId = 1;
        Basket expectedBasket = baskets[basketId];
        
        Basket actualBasket = basketRepository.getBasket(basketId);

        assertEquals(expectedBasket.getId(), actualBasket.getId());
        assertEquals(expectedBasket.getNeeds().get(0), actualBasket.getNeeds().get(0));
    }

    @Test
    void testGetBasketWrongBasket_Failed() throws IOException {
        int basketId = 3;
        Basket actualBasket = basketRepository.getBasket(basketId);

        assertNull(actualBasket);
    }

    @Test
    void testCreateBasket_Success() throws IOException {
        int basketId = 2;
        Basket actualBasket = basketRepository.createBasket(basketId);

        assertEquals(basketId, actualBasket.getId());
    }

    @Test
    void testCreateBasketException_Failed() throws IOException {
        int basketId = 2;
        Mockito.doThrow(new IOException()).when(mockObjectMapper).writeValue(eq(new File(tmpFileName)), any());
        Basket actualBasket = basketRepository.createBasket(basketId);

        assertNull(actualBasket);
    }

    @Test
    void testAddNeedToBasket_Success() throws IOException {
        int basketId = 0;
        int needId = 100;
        Need expectedNeed = new TemporaryNeed(needId, "Im Needed", 1.99, 2);
        when(needRepository
            .getNeed(needId)).thenReturn(expectedNeed);

        boolean addedToBasket = basketRepository.addNeedToBasket(basketId, expectedNeed);

        assertTrue(addedToBasket);

        Basket gottenBasket = basketRepository.getBasket(basketId);
        assertEquals(expectedNeed.getName(), gottenBasket.getNeeds().get(1).getName());
    }

    @Test
    void testAddNeedToBasketNewBasket_Success() throws IOException {
        int basketId = 2;
        int needId = 100;
        Need expectedNeed = new TemporaryNeed(needId, "Im Needed", 1.99, 2);
        when(needRepository
            .getNeed(needId)).thenReturn(expectedNeed);

        boolean addedToBasket = basketRepository.addNeedToBasket(basketId, expectedNeed);

        assertTrue(addedToBasket);

        Basket gottenBasket = basketRepository.getBasket(basketId);

        assertEquals(expectedNeed.getName(), gottenBasket.getNeeds().get(0).getName());
    }

    @Test
    void testAddNeedToBasketNewBasket_Failed() throws IOException {
        int basketId = 2;
        int needId = 100;
        Need expectedNeed = new TemporaryNeed(needId, "Im Needed", 1.99, 2);

        Mockito.doThrow(new IOException()).when(mockObjectMapper).writeValue(eq(new File(tmpFileName)), any());
        when(needRepository
            .getNeed(needId)).thenReturn(expectedNeed);

        boolean addedToBasket = basketRepository.addNeedToBasket(basketId, expectedNeed);

        assertFalse(addedToBasket);
    }

    @Test
    void testAddNeedToBasketNoExist_Failed() throws IOException {
        int basketId = 2;
        int needId = 100;
        Need expectedNeed = new TemporaryNeed(needId, "Im Needed", 1.99, 2);

        Mockito.doThrow(new IOException()).when(mockObjectMapper).writeValue(eq(new File(tmpFileName)), any());
        when(needRepository
            .getNeed(needId)).thenReturn(null);

        boolean addedToBasket = basketRepository.addNeedToBasket(basketId, expectedNeed);

        assertFalse(addedToBasket);
    }

    @Test
    void testUpdateBasket_Successess() throws IOException {
        Basket updatedBasket = new Basket(1);
        updatedBasket.addtoBasket(new TemporaryNeed(0, "Need 0", 1.59, 15));
        updatedBasket.addtoBasket(new TemporaryNeed(3, "Need 3", 599.99, 20));

        boolean updated = basketRepository.updateBasket(updatedBasket);

        assertTrue(updated);
        assertEquals(updatedBasket.getNeeds().get(0).getQuantity(), basketRepository.getBasket(1).getNeeds().get(0).getQuantity());
    }

    @Test
    void testUpdateBasket_Failed() throws IOException {
        Basket updatedBasket = new Basket(3);
        updatedBasket.addtoBasket(new TemporaryNeed(0, "Need 0", 1.59, 15));
        updatedBasket.addtoBasket(new TemporaryNeed(3, "Need 3", 599.99, 20));

        boolean updated = basketRepository.updateBasket(updatedBasket);

        assertFalse(updated);
    }

    @Test
    void testCheckoutBasket_Success() throws IOException {
        int basketId = 1;
    
        baskets[1].getNeeds().removeIf(n -> n.getId() == 4);
    
        when(needRepository.getNeed(0)).thenReturn(new TemporaryNeed(0, "Need 0", 1.59, 10));
        when(needRepository.getNeed(3)).thenReturn(new TemporaryNeed(3, "Need 3", 599.99, 15));
        when(needRepository.deleteNeed(anyInt())).thenReturn(true);
        boolean checkedOut = basketRepository.checkoutBasket(basketId);
        assertTrue(checkedOut);
    }

    @Test
    void testCheckoutBasket_Failed() throws IOException {
        int basketId = 3;

        boolean checkedOut = basketRepository.checkoutBasket(basketId);

        assertFalse(checkedOut);
    }
}
