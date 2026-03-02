package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Admin implements User{
    @JsonProperty("name") private String name;
    @JsonProperty("email") private String email;
    @JsonProperty("password") private String password;

    /**
     * Create a new Admin with given id, name, email, and password
     * 
     * @param name of new {@link Admin}
     * @param email of new {@link Admin}
     * @param password of new {@link Admin}
     */
    public Admin( @JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.name = name;
        this.email = email;
        this.password = password;
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
        if (o instanceof Admin) {
            Admin that = (Admin) o;
            return this.name.equals(that.name) && this.email.equals(that.email) && this.password.equals(that.password);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }
}
