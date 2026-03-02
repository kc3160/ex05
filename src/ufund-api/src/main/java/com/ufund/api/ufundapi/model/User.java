package com.ufund.api.ufundapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = Helper.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Helper.class, name = "helper"),
        @JsonSubTypes.Type(value = Admin.class, name = "admin")
})

public interface User {
    /**
     * Sets the name of {@link User}
     * 
     * @param name new name of {@link User}
     */
    void setName(String name);

    /**
     * Gets the name of {@link User}
     * 
     * @return name of {@link User}
     */
    String getName();

    /**
     * Sets the email of {@link User}
     * 
     * @param email new email of {@link User}
     */
    void setEmail(String email);

    /**
     * Gets the email of {@link User}
     * 
     * @return email of {@link User}
     */
    String getEmail();

    /**
     * Sets the password of {@link User}
     * 
     * @param password new password of {@link User}
     */
    void setPassword(String password);

    /**
     * Gets the password of {@link User}
     * 
     * @param password  password of {@link User}
     */
    String getPassword();
}
