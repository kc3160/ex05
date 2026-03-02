package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FertilizerNeed extends SimpleNeed {
    @JsonProperty("organic") private boolean organic;

    public FertilizerNeed(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("cost") double cost, @JsonProperty("quantity") int quantity, @JsonProperty("organic") boolean organic) {
        super(id, name, cost, quantity);
        this.organic = organic;
    }

    public Need copyWithId(int id) {
        FertilizerNeed copy = new FertilizerNeed(id, super.getName(), super.getCost(), super.getQuantity(), this.organic);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            FertilizerNeed that = (FertilizerNeed) o;
            if (this.organic == that.organic) {
                return true;
            }
        }
        return false;
    }
}
