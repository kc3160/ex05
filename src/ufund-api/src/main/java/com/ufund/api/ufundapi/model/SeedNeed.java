package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SeedNeed extends SimpleNeed {
    public SeedNeed(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("cost") double cost, @JsonProperty("quantity") int quantity) {
        super(id, name, cost, quantity);
        //! Add more specific qualities
    }

    public Need copyWithId(int id) {
        SeedNeed copy = new SeedNeed(id, super.getName(), super.getCost(), super.getQuantity());
        return copy;
    }
}