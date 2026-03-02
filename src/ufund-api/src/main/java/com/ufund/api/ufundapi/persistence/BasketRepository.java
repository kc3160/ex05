package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Basket;
import com.ufund.api.ufundapi.model.Need;

@Component
public interface BasketRepository {
    /**
     * Creates and saves given {@link Basket basket} if Basket does not exist
     * then adds Need to Basket
     * If need does not exist return false else return true
     * 
     * @param basketID the id of the basket
     * @param need the need we are adding to the basket
     * @return new {@link Basket basket}
     * @throws IOException
     */
    boolean addNeedToBasket(int basketId, Need need) throws IOException;

    Basket getBasket(int id) throws IOException;

    Basket createBasket(int id) throws IOException;

    boolean updateBasket(Basket basket) throws IOException;

    boolean checkoutBasket(int basketId) throws IOException;
}
