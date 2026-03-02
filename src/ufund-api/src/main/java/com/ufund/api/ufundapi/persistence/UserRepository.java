package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Admin;
import com.ufund.api.ufundapi.model.Helper;
import com.ufund.api.ufundapi.model.User;


public interface UserRepository {
    /**
    * Creates and registers a {@link Helper} into the system
    * 
    * @param name name of the helper being registered
    * @param email email of the helper being registered
    * @param password password of the helper being registered
    * @return new {@link Helper helper} if the email does not already exist in system, otherwise null
    * @throws IOException
    */
    Helper registerHelper(String name, String email, String password) throws IOException;


    /**
     * Authenticates a {@link Helper} using email and password.
     * 
     * @param {@link User helper} of a possible helper stored in the DAO
     * @return true when credentials are correct, false otherwise
     */
    boolean checkHelper(User helper);

    /**
     * Authenticates a {@link Admin} using email and password.
     * 
     * @param {@link User admin} of a possible admin stored in the DAO
     * @param password Password provided by the user attempting to log in
     * @return true when credentials are correct, false otherwise
     */
    boolean checkAdmin(User admin);

    /**
     * Gets a {@link User} from its hash
     * 
     * @param hash hash of the User
     * @return {@link User} who has that hash
     */
    User getUserFromHash(int hash);
}   