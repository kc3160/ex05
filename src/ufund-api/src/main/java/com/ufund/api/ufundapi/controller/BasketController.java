package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Basket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.service.BasketService;

@RestController
@RequestMapping("baskets")
public class BasketController {

    @Autowired
    private BasketService service;

    private static final Logger LOG = Logger.getLogger(BasketController.class.getName());

    /**
     * API response to get basket
     * @param id basket id
     * @returnResponseEntity of an basket and an HTTP status of OK (200) <br>
     * ResponseEntity with HTTP status of NOT_FOUND (404) if not found <br>
     * ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) or BAD_REQUEST (400) otherwise 
     */
    @GetMapping("")
    public ResponseEntity<Basket> getBasket(@RequestParam int id) {
        LOG.info("GET /baskets/" + id);
    
        try {
            Basket basket = service.getBasket(id);
    
            if (basket == null) {
                LOG.warning("Basket with ID " + id + " not found.");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
    
            return new ResponseEntity<>(basket, HttpStatus.OK);
    
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Failed to fetch basket with ID " + id + ": " + e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Unexpected error fetching basket: " + e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    
    /**
     * API response for adding a need to basket
     * 
     * @param {@link Need need} that we are adding
     * @param id id of the basket
     * 
     * @return ResponseEntity of an int and an HTTP status of OK (200) <br>
     * ResponseEntity with HTTP status of UNAUTHORIZED (401) if login details were incorrect or admin tried to login <br>
     * ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise     */
    @PostMapping("/needs")
    public ResponseEntity<Void> addNeedToBasket(@RequestParam int id, @RequestBody Need need) {
        LOG.info("POST /baskets/" + id + "/needs");
        try {
            boolean needExists = service.addNeedToBasket(id, need);
            if(!needExists){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * API response for updating a basket
     *
     * @param id     ID of the basket to update
     * @param basket {@link Basket} object containing the new basket details
     * @return ResponseEntity containing:
     *         <ul>
     *           <li>Updated {@link Basket} and HTTP status {@code 200 OK} if update is successful</li>
     *           <li>HTTP status {@code 404 NOT_FOUND} if the basket does not exist</li>
     *         </ul>
     * @throws IOException if an I/O error occurs during update
     */
    @PutMapping("")
    public ResponseEntity<Basket> updateBasket(@RequestParam int id, @RequestBody Basket basket) throws IOException {
        LOG.info("PUT /baskets/" + id);
        boolean updatedBasket = service.updateBasket(id, basket);
        if (updatedBasket) {
            return new ResponseEntity<>(service.getBasket(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

        /**
         * API response to check out a basket
         *
         * @param id ID of the basket to check out
         * @return ResponseEntity containing:
         *         <ul>
         *           <li>HTTP status {@code 200 OK} if the checkout is successful</li>
         *           <li>HTTP status {@code 404 NOT_FOUND} if the basket does not exist</li>
         *           <li>HTTP status {@code 500 INTERNAL_SERVER_ERROR} on I/O failure</li>
         *         </ul>
         */
    @PostMapping("/checkout")
    public ResponseEntity<Void> checkoutBasket(@RequestParam int id) {
        LOG.info("POST /baskets/" + id + "/checkout");
        try {
            boolean checkedOut = service.checkoutBasket(id);
            if (checkedOut) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
