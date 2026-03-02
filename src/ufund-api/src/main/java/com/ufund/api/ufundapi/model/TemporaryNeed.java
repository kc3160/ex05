package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TemporaryNeed extends SimpleNeed {
    /**
     * Create new TemporaryNeed with given id, name and cost
     * 
     * @param id of new {@link TemporaryNeed}
     * @param name of new {@link TemporaryNeed}
     * @param cost of new {@link TemporaryNeed}
     * @param quantity of new
     */

    public TemporaryNeed(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("cost") double cost, @JsonProperty("quantity") int quantity) {
        super(id, name, cost, quantity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need copyWithId(int id) {
        TemporaryNeed copy = new TemporaryNeed(id, super.getName(), super.getCost(), super.getQuantity());
        return copy;
    }
}
