package com.ufund.api.ufundapi.persistence;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Admin;
import com.ufund.api.ufundapi.model.Helper;
import com.ufund.api.ufundapi.model.User;

@Component
public class UserDataDAO implements UserRepository{
    private String filename;
    private final HashMap<Integer, User> users;
    private ObjectMapper objectMapper;
    private int nextId = 0;
    
    public UserDataDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.users = new HashMap<>();
        load();
    }


    private synchronized int getNextId() {
        //? Not static because it is only instantiated once
        int thisId = this.nextId++;
        return thisId;
    }

    /**
     * Loads the user cache from file, not thread safe but not called
     * in a way that it should matter.
     * 
     * @return true if successfully retreived
     * @throws IOException
     */
    private synchronized boolean load() throws IOException {
        User[] userArray = this.objectMapper.readValue(new File(this.filename), User[].class);
        for (User user : userArray) {
            users.put(user.getEmail().hashCode(), user);
            if (user instanceof Helper) { 
                Helper helper = (Helper) user; 
                if (helper.getBasketId() > this.nextId) {
                    this.nextId = helper.getBasketId();
                }
            }
        }
        this.nextId++;
        return true;
    }

     /**
     * Saves the current load cache to file
     * 
     * @return true if successfully saved
     * @throws IOException
     */
    private synchronized boolean save() {
        try {
            User[] userArray = users.values().toArray(new User[users.size()]);
            this.objectMapper.writeValue(new File(this.filename), userArray);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Helper registerHelper(String name, String email, String password) {
        synchronized (this.users) {
            if (users.containsKey(email.hashCode())) {
                return null; // Email already exists
            }
            Helper helper = new Helper(name, email, password, getNextId());
            users.put(email.hashCode(), helper);
            boolean registered = save();
            if (registered) {
                return helper;
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkHelper(User helper) {
            User possible = users.get(helper.getEmail().hashCode());
            // Check equality which may or may not be necessary
            if (possible != null && possible instanceof Helper && helper.getPassword().equals(possible.getPassword()) && helper.getName().equals(possible.getName())) {
                return true;
            }
            return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkAdmin(User admin) {
        //! currently the same but could change to where admin is  
        //! stored differently so its harder spoof being an admin
        User possible = users.get(admin.getEmail().hashCode());
        // Check equality which may or may not be necessary
        if (possible != null && possible instanceof Admin && admin.getPassword().equals(possible.getPassword()) && admin.getName().equals(possible.getName())) {
            return true;
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserFromHash(int hash) {
        synchronized (this.users) {
            return this.users.get(hash);
        }
    }
}
