package com.ufund.api.ufundapi.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NeedBundle implements Need {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("cost")
    private double cost;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("discount")
    private double discount;
    @JsonProperty("needs")
    private HashMap<Integer, Integer> needs;

    /**
     * Create new NeedBundle with given id, name, quantity, and discount
     * 
     * @param id       of new {@link NeedBundle}
     * @param name     of new {@link NeedBundle}
     * @param quantity of new {@link NeedBundle}
     * @param discount of new {@link NeedBundle}
     */
    public NeedBundle(@JsonProperty("id") int id, @JsonProperty("name") String name, double cost,
            @JsonProperty("quantity") int quantity, double discount) {
        this.id = id;
        this.name = name;
        this.cost = 0;
        this.quantity = quantity;
        this.needs = new HashMap<Integer, Integer>();
        this.discount = discount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getCost() {
        return this.cost;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return discount of {@link NeedBundle}
     */
    public double getDiscount() {
        return this.discount;
    }

    /**
     * get discount of {@link NeedBundle}
     * 
     * @param discount of new {@link NeedBundle}
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof NeedBundle) {
            NeedBundle that = (NeedBundle) o;
            if (this.id == that.id && this.name.equals(that.name) && this.cost == that.cost
                    && that.needs.equals(this.needs) && this.discount == that.discount) {
                return true;
            }
        }
        return false;
    }


    public Map<Integer, Integer> getNeeds() {
        return this.needs;
    }
    
    /**
     * Add need to bundle NeedBundle
     * 
     * @param {@link Need} we are adding to {@link NeedBundle}
     */
    public void addNeedtoBundle(Need need) {

        double needCost = need.getCost();
        int id = need.getId();

        int quantity = need.getQuantity();
        if(needs.containsKey(id)){
            int oldQuantity = needs.get(id);
            needs.put(id, oldQuantity + quantity);
        }
        else{
            needs.put(need.getId(), quantity);
        }
        cost += quantity*(needCost - (needCost * discount));
    }

    /**
     * remove need from bundle NeedBundle
     * 
     * @param {@link Need} we are removing from {@link NeedBundle}
     */
    public void removeNeedfromBundle(Need need) {

        int id = need.getId();

        int quantity = need.getQuantity();

        int oldQuantity = needs.get(id);

        double needCost = need.getCost();

        if (oldQuantity - quantity <= 0) {

            needs.remove(id);

        } else {
            needs.put(id, oldQuantity - quantity);
        }
        cost -= quantity*(needCost - (needCost * discount));
        if (cost<=0){
            cost = 0; 
        }
    }

    /**
     * Create a new NeedBundle with the same properties but with a new id
     * 
     * @param id the new id for the copied NeedBundle
     * @return a new NeedBundle with the specified id
     */
    public Need copyWithId(int id) {
        NeedBundle copy = new NeedBundle(id, this.name, this.cost, this.quantity, this.discount);
        copy.needs.putAll(this.needs);
        return copy;
    }
}
