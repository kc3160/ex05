package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.controller.BasketController;
import com.ufund.api.ufundapi.model.Basket;
import com.ufund.api.ufundapi.model.Helper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.SessionHandler;
import com.ufund.api.ufundapi.persistence.BasketRepository;
import com.ufund.api.ufundapi.persistence.NeedRepository;

@Service
public class BasketService {
    @Autowired
    private BasketRepository basketHandler;
    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private NeedRepository cupBoard;

    private static final Logger LOG = Logger.getLogger(BasketController.class.getName());

    public Basket getBasket(int id) throws IOException {
        Helper helper = sessionHandler.authorizeHelperSession(id);
        if (helper == null) {
            return null;
        }
        Basket basket = basketHandler.getBasket(helper.getBasketId());
        if (basket == null) {
            return null;
        }
        syncBasketWithCupboard(basket);
        return basket;
    }

    public boolean addNeedToBasket(int id, Need need) throws IOException {
        Helper helper = sessionHandler.authorizeHelperSession(id);
        if (helper == null) {
            return false;
        }
        boolean bool = basketHandler.addNeedToBasket(helper.getBasketId(), need);
        return bool;
    }
 

    public boolean updateBasket(int id, Basket basket) throws IOException {
        Helper helper = sessionHandler.authorizeHelperSession(id);
        if (helper == null) {
            return false;
        }
        boolean result = basketHandler.updateBasket(basket);
        return result;
    }

    public boolean checkoutBasket(int id) throws IOException {
        Helper helper = sessionHandler.authorizeHelperSession(id);
        if (helper == null) {
            return false;
        }
        int basketId = helper.getBasketId();
        Basket basket = basketHandler.getBasket(basketId);
        if (basket == null) {
            return false;
        }
        syncBasketWithCupboard(basket);
        return basketHandler.checkoutBasket(basketId);
    }

    protected void syncBasketWithCupboard(Basket basket) throws IOException {
        ArrayList<Need> updatedNeeds = new ArrayList<>();
    
        for (Need currentNeed : basket.getNeeds()) {
            Need latestNeed = cupBoard.getNeed(currentNeed.getId());
    
            if (latestNeed == null) {
                continue;
            }
            int originalQuantity = currentNeed.getQuantity();
            Need copiedNeed = latestNeed.copyWithId(currentNeed.getId()); 
    
            copiedNeed.changeQuantity(originalQuantity);
    
            updatedNeeds.add(copiedNeed);
        }
        basket.setNeeds(updatedNeeds);
        basketHandler.updateBasket(basket);
    }
}