package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = TemporaryNeed.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NeedBundle.class, name = "bundle"),
        @JsonSubTypes.Type(value = TemporaryNeed.class, name = "test"),
        @JsonSubTypes.Type(value = SeedNeed.class, name = "seed"),
        @JsonSubTypes.Type(value = ToolNeed.class, name = "tool"),
        @JsonSubTypes.Type(value = FertilizerNeed.class, name = "fertilizer"),
})
public interface Need {
    /**
     * Sets name of {@link Need}
     * 
     * @param name new name of {@link Need}
     */
    void setName(String name);

    /**
     * Sets the cost of {@link Need}
     * 
     * @param cost new cost of {@link Need}
     */
    void setCost(double cost);

    /**
     * Gets the id of {@link Need}
     * 
     * @return id of {@link Need}
     */
    int getId();

    /**
     * Gets the name of {@link Need}
     * 
     * @return name of {@link Need}
     */
    String getName();

    /**
     * Gets the cost of {@link Need}
     * 
     * @return cost of {@link Need}
     */
    double getCost();

     /**
     * Gets the amount of {@link Need needs} needed
     * 
     * @return required amount of {@link Need}
     */
    int getQuantity();

    /**
     * Sets the quantity of {@link Need}
     * 
     * @return quantity of {@link Need}
     */
    void changeQuantity(int quantity);

    /**
     * Creates a new {@link Need} with given id
     * 
     * @param id of the new copied {@link Need}
     * @return the copied {@link Need}
     */
    Need copyWithId(int id);
}