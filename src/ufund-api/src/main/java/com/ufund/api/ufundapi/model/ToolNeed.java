package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ToolNeed extends SimpleNeed {
    @JsonProperty("used") private boolean  used;

    public ToolNeed(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("cost") double cost, @JsonProperty("quantity") int quantity, @JsonProperty("used") boolean used) {
        super(id, name, cost, quantity);
        this.used = used;
    }

    public Need copyWithId(int id) {
        ToolNeed copy = new ToolNeed(id, super.getName(), super.getCost(), super.getQuantity(), this.used);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            ToolNeed that = (ToolNeed) o;
            if (this.used == that.used) {
                return true;
            }
        }
        return false;
    }
}