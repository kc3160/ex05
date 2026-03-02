package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ufund.api.ufundapi.model.Helper;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.service.LoginService;

@RestController
@RequestMapping("account")
public class LoginController {
    @Autowired
    private LoginService service;

    private ObjectMapper jsonMapper = new ObjectMapper();

    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());

    /**
    * API response for getting a session id by logging in
    * 
    * @param {@link User user} to get the session with 
    * 
    * @return ResponseEntity of an int and an HTTP status of OK (200) <br>
    * ResponseEntity with HTTP status of UNAUTHORIZED (401) if login details were incorrect or admin tried to login <br>
    * ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise     */
    @PostMapping("/login")
    public ResponseEntity<ObjectNode> logIntoSession(@RequestBody User user){
        LOG.info("POST /account/login "+ user);
        ObjectNode main = jsonMapper.createObjectNode();
        try{
            Integer id = service.getSessionIdFromLogin(user);
            if (id != null) {
                boolean isAdmin = service.isAdmin(user);
                main.put("id", id);
                main.put("admin", isAdmin);
                return new ResponseEntity<>(main, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(main, HttpStatus.UNAUTHORIZED);
            }
       }
        catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(main, HttpStatus.INTERNAL_SERVER_ERROR);
        }
   }

    /**
     * API response for logging a {@link User user} out
     * 
     * @param id to remove from a current session
     * 
     * @return ResponseEntity of an int and an HTTP status of OK (200) <br>
     * ResponseEntity with HTTP status of UNAUTHORIZED (401) if session id is invalid or logout failed <br>
     */
    @PutMapping("/logout/{id}")
    public ResponseEntity<Void> logoutOfHelperSession(@PathVariable int id){
        LOG.info("PUT /account/logout "+ id);
        boolean success = service.logoutHelper(id);
        if (success)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * API response for logging a {@link User user} out
     * 
     * @param id to remove from a current session
     * 
     * @return ResponseEntity of an int and an HTTP status of OK (200) <br>
     * ResponseEntity with HTTP status of UNAUTHORIZED (401) if session id is invalid or logout failed <br>
     */
    @PutMapping("/logout-admin/{id}")
    public ResponseEntity<Void> logoutOfAdminSession(@PathVariable int id){
        LOG.info("PUT /account/logout-admin "+ id);
        boolean success = service.logoutAdmin(id);
        if (success)
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * API response for registering a {@link Helper helper}
     * 
     * @param name     The name of the helper
     * @param email    The email of the helper
     * @param password The password of the helper
     * 
    * @return ResponseEntity with the created Helper and HTTP status CREATED (201) <br>
     *ResponseEntity with HTTP status CONFLICT (409) if email already exists <br>
     * ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @PostMapping("/register")
    public ResponseEntity<Helper> registerHelper(@RequestBody User user) {
        LOG.info("POST /account/register " + user.toString());
        try {
            Helper helper = service.registerHelper(user.getName(), user.getEmail(), user.getPassword());
            if (helper != null) {
                return new ResponseEntity<>(helper, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Registration Error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
