
package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Helper implements User {
    @JsonProperty("name") private String name;
    @JsonProperty("email") private String email;
    @JsonProperty("password") private String password;
    @JsonProperty("basketId") private int basketID;

    /**
     * Create a new Helper with given id, name, email, and password
     * 
     * @param name of new {@link Helper}
     * @param email of new {@link Helper}
     * @param password of new {@link Helper}
     */
    public Helper( @JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("password") String password, @JsonProperty("basketId") int basketID) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.basketID = basketID;

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
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Helper) {
            Helper that = (Helper) o;
            return this.name.equals(that.name) && this.email.equals(that.email) && this.password.equals(that.password) /*&& this.basketID == that.basketID*/;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }

    public int getBasketId() {
        return basketID;
    }
    
    public void setBasketId(int basketID) {
        this.basketID = basketID;
    }

    @Override
    public String toString() {
        return this.name + " " + this.email + " " + this.password + " " + this.basketID;
    }
}