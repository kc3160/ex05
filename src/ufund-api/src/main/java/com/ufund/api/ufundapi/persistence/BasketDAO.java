package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Basket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedBundle;

@Component
public class BasketDAO implements BasketRepository {
    private String filename;
    private ObjectMapper objectMapper;

    @Autowired
    private final NeedRepository cupboardDAO;

    private final Map<Integer, Basket> baskets;

    public BasketDAO(@Value("${basket.file}") String filename, ObjectMapper objectMapper, NeedRepository cupboardDAO)
            throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.baskets = new TreeMap<>();
        this.cupboardDAO = cupboardDAO;
        load();
    }

    private boolean load() throws IOException {
        Basket[] basketArray = this.objectMapper.readValue(new File(this.filename), Basket[].class);
        for (Basket basket : basketArray) {
            this.baskets.put(basket.getId(), basket);
        }
        return true;
    }

    /**
     * Saves the current load cache to file
     * 
     * @return true if successfully saved
     * @throws IOException
     */
    private synchronized boolean save() {
        try {
            Basket[] basketArray = baskets.values().toArray(new Basket[baskets.size()]);
            this.objectMapper.writeValue(new File(this.filename), basketArray);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Basket getBasket(int id) throws IOException {
        synchronized (this.baskets) {
            if (!this.baskets.containsKey(id)) {
                return null;
            }
            return this.baskets.get(id);
        }
    }

    /**
     * creates and saves a new basket with the given id
     * 
     * @param id the id given
     * @return the {@link Basket newBasket}
     */
    @Override
    public Basket createBasket(int id) throws IOException {
        Basket newBasket = new Basket(id);
        synchronized (this.baskets) {
            baskets.put(newBasket.getId(), newBasket);
        }
        boolean succeded = save();
        //return newBasket;
        if (succeded) {
            return newBasket;
        }
        baskets.remove(newBasket.getId());
        return null;
    }

    @Override
    public boolean addNeedToBasket(int id, Need need) throws IOException {
        synchronized (this.baskets) {
            Basket basket;
            if (!this.baskets.containsKey(id)) {
                basket = createBasket(id);
                if (basket == null) {
                    return false;
                }
            } else {
                basket = this.baskets.get(id);
            }

            // Only check if the need exists in cupboardDAO, do not modify its quantity
            Need existingNeed = cupboardDAO.getNeed(need.getId());
            if (existingNeed == null) {
                return false; // Need doesn't exist in cupboard
            }
    
            // Add the need to the basket (no change to cupboard quantity)
            boolean needFound = false;
            for (Need basketNeed : basket.getNeeds()) {
                if (basketNeed.getId() == need.getId()) {
                    basketNeed.changeQuantity(basketNeed.getQuantity() + need.getQuantity());
                    needFound = true;
                    break;
                }
            }

            Need exist = cupboardDAO.getNeed(need.getId());
            if (exist == null) {
                return false;
            }
    
            if (!needFound) {
                basket.addtoBasket(need);
            }
    
            return updateBasket(basket);
        }
    }
    /**
     * 
     * @param basketId
     * @param newNeed
     * @param quantity
     * @return
     * @throws IOException
     */
    @Override
    public boolean updateBasket(Basket basket) throws IOException {
        synchronized (this.baskets) {
            if (!this.baskets.containsKey(basket.getId())) {
                return false;
            }
            baskets.put(basket.getId(), basket);
            ArrayList<Need> needsArray = baskets.get(basket.getId()).getNeeds();
            for (int i = 0; i < needsArray.size(); i++) {
                if (needsArray.get(i).getQuantity() == 0) {
                    needsArray.remove(i);
                }
            }
            save();
            return true;
        }
    }

    @Override
    public boolean checkoutBasket(int basketId) throws IOException {
        synchronized (this.cupboardDAO) {
            Basket basket;
            if (!this.baskets.containsKey(basketId)) {
                return false;
            }
            basket = this.baskets.get(basketId);
            ArrayList<Need> nds = basket.getNeeds();
            int n = nds.size();
            boolean success = true;
            for (int i = n - 1; i >= 0; i--) {
                Need currentNeed = nds.get(i);
                int id = currentNeed.getId();
                Need cupboardNeed = cupboardDAO.getNeed(id);
                int quantity = currentNeed.getQuantity();
                if (!(currentNeed instanceof NeedBundle)) {
                    if (cupboardNeed == null || cupboardNeed.getQuantity() < quantity) {
                        success = false;
                        continue;
                    }
                } else {
                    NeedBundle bundle = (NeedBundle) currentNeed;
                    int bundleQuantity = bundle.getQuantity();
                    int flag = 0;
                    for (Map.Entry<Integer, Integer> entry : bundle.getNeeds().entrySet()) {
                        int innerId = entry.getKey();
                        int innerQtyNeeded = entry.getValue() * bundleQuantity;
                        Need cupboardInnerNeed = cupboardDAO.getNeed(innerId);
                        if (cupboardInnerNeed == null || cupboardInnerNeed.getQuantity() < innerQtyNeeded) {
                            flag = 1;
                            break;
                        }
                    }
                    if(flag ==1){
                        continue;
                    }
                    Need cupboardBundle = cupboardDAO.getNeed(bundle.getId());
                    if (cupboardBundle == null || cupboardBundle.getQuantity() < bundleQuantity) {
                        continue;
                    }
                }
                if (currentNeed instanceof NeedBundle) {
                    NeedBundle bundle = (NeedBundle) currentNeed;
                    Map<Integer, Integer> bundledNeeds = bundle.getNeeds();
                    for (Map.Entry<Integer, Integer> entry : bundledNeeds.entrySet()) {
                        int bundledId = entry.getKey();
                        int bundledQty = entry.getValue() * bundle.getQuantity();
                        Need bundledNeed = cupboardDAO.getNeed(bundledId);
                        if (bundledNeed != null) {
                            int newQty = bundledNeed.getQuantity() - bundledQty;
                            if(newQty ==0){
                                boolean deleted = cupboardDAO.deleteNeed(bundledId);
                                if (deleted) {
                                    basket.updateNeed(bundledId, 0);
                                }
                            }
                            else{
                                bundledNeed.changeQuantity(newQty);
                                cupboardDAO.updateNeed(bundledNeed);
                            }
                        }
                    }
                }
                if (cupboardNeed.getQuantity() == currentNeed.getQuantity()) {
                    boolean deleted = cupboardDAO.deleteNeed(cupboardNeed.getId());
                    if (deleted) {
                        basket.updateNeed(id, 0);
                    }
                } else {
                    int change = cupboardNeed.getQuantity() - currentNeed.getQuantity();
                    cupboardNeed.changeQuantity(change);
                    cupboardDAO.updateNeed(cupboardNeed);
                    basket.updateNeed(id, 0);
                }
            }
            save();
            return success;
        }
    }
}
