package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.controller.LoginController;
import com.ufund.api.ufundapi.model.Admin;
import com.ufund.api.ufundapi.model.Helper;
import com.ufund.api.ufundapi.model.SessionHandler;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.UserRepository;


@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionHandler session;

    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());

    /**
     * Returns the session ID for a given Helper
     * @param {@link User user} that is logging in 
     * @return Session ID
     * @throws IOException
     */
    public Integer getSessionIdFromLogin(User user) throws IOException {
        LOG.info("logging in " + user.getName());
        Integer id;
        if (userRepository.checkHelper(new Helper(user.getName(), user.getEmail(), user.getPassword(), 0))) {
            LOG.info("Helper Success " + user.getName());
            id = session.startHelperSession(user);
        }
        else if (userRepository.checkAdmin(new Admin(user.getName(), user.getEmail(), user.getPassword()))) {
            LOG.info("Admin Success " + user.getName());
            id = session.startAdminSession(user);
        }
        else {
            LOG.info("No user " + user.getName() + " exists, or account is already logged in");
            id = null;
        }
        return id;
    }

    /**
     * Logs out the {@link Helper helper} with id
     * @param id - Session ID to log out of
     * @return true if successfully logged out, false otherwise
     */
    public boolean logoutHelper(int id) {
        boolean deleted = session.removeHelperSession(id);

        return deleted;
    }

    /**
     * Logs out the {@link Admin admin} with id
     * @param id - Session ID to log out of
     * @return true if successfully logged out, false otherwise
     */
    public boolean logoutAdmin(int id) {
        boolean deleted = session.removeAdminSession(id);

        return deleted;
    }

    /**
     * Checks if a User is an admin, the user should be checked
     * to have logged in order to garuentee the user is either
     * an admin or helper.
     * @param user
     * @return true if user is admin
     */
    public boolean isAdmin(User user) {
        return userRepository.checkAdmin(user);
    }

     /**
     * Checks if a User is a helper, the user should be checked
     * to have logged in order to garuentee the user is either
     * an admin or helper.
     * @param user
     * @return true if user is admin
     */
    public boolean isHelper(User user) {
        return userRepository.checkHelper(user);
    }

    /**
     * Registers a {@link Helper helper} and stores the user information
     * @param name - Name of the helper
     * @param email - email of the helper
     * @param password - password of the helper
     * @return The Helper that has logged in
     * @throws IOException
     */
    public Helper registerHelper(String name, String email, String password) throws IOException {
        Helper helper = userRepository.registerHelper(name, email, password);

         /* Other logic */

        return helper;
    }
}


