package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleNeed implements Need {
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("cost") private double cost;
    @JsonProperty("quantity") private int quantity;

    /**
     * Create new SimpleNeed with given id, name and cost
     * 
     * @param id of new {@link SimpleNeed}
     * @param name of new {@link SimpleNeed}
     * @param cost of new {@link SimpleNeed}
     * @param quantity of new {@link SimpleNeed}
     */
    public SimpleNeed(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("cost") double cost, @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.quantity = quantity;
    }

    /**
     * {@inheritDoc}
     */
    /*@Override
    public Need copyWithId(int id) {
        SimpleNeed copy = new SimpleNeed(id, this.name, this.cost, this.quantity);
        return copy;
    }*/

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
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Need) {
            SimpleNeed that = (SimpleNeed) o;
            if (this.id == that.id && this.name.equals(that.name) && this.cost == that.cost) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.id;
    }
}
