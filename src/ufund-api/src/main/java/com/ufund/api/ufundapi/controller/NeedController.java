package com.ufund.api.ufundapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedBundle;
import com.ufund.api.ufundapi.service.NeedService;

@RestController
@RequestMapping("needs")
public class NeedController {

    @Autowired
    private NeedService service;

    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());

    /**
     * API response for getting a {@linkplain Need need} using an id
     * 
     * @param id the id used to get the {@link Need need}
     * 
     * @return ResponseEntity of a {@link Need need} and an HTTP status of OK (200)
     *         <br>
     *         ResponseEntity with HTTP status of NOT FOUND (404) if no needs are
     *         found <br>
     *         ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Need> getNeed(@PathVariable int id) {
        LOG.info("GET /needs/" + id);
        try {
            Need need = service.getNeedById(id);
            if (need != null)
                return new ResponseEntity<Need>(need, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * API response for getting list of needs
     * 
     * @return ResponseEntity of needs and an HTTP status of OK (200) if needs are
     *         found
     *         If no needs are found, it returns an empty array with an HTTP status
     *         of OK (200).
     *         ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @GetMapping("")
    public ResponseEntity<Need[]> getNeeds() {
        LOG.info("GET /needs");
        try {
            Need[] needs = service.getAllNeeds();
            if (needs != null && needs.length > 0) {
                return new ResponseEntity<>(needs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new Need[0], HttpStatus.OK);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * API response for getting multiple {@linkplain Need needs} using an array of ids
     * 
     * @param ids the array of ids used to get the {@link Need needs}
     * 
     * @return ResponseEntity of an array of {@link Need needs} and an HTTP status of OK (200)
     *         <br>
     *         ResponseEntity with HTTP status of NOT FOUND (404) if none of the needs are found
     *         <br>
     *         ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @GetMapping("/batch")
    public ResponseEntity<Need[]> getNeedsByIds(@RequestParam(required=false) int[] ids) {
        LOG.info("GET /needs/batch?ids=" + Arrays.toString(ids));
        if(ids == null){
            return new ResponseEntity<>(new Need[0], HttpStatus.OK);
        }
        try {
            Need[] needs = service.getNeedsByIds(ids);
            if (needs != null && needs.length > 0) {
                return new ResponseEntity<>(needs, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds the the GET request for all {@linkplain Need needs} that contains
     * the text search in the name
     * 
     * @param search The search string used to find the {@link Need needs}
     * 
     * @return ResponseEntity of an array of {@link Need needs} and an HTTP status
     *         of OK (200) <br>
     *         ResponseEntity with HTTP status of NOT FOUND (404) if no needs are
     *         found <br>
     *         ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @GetMapping("/")

    public ResponseEntity<Need[]> searchNeeds(
        @RequestParam(defaultValue = "") String search,
        @RequestParam(required = false) int[] tagIds,
        @RequestParam(defaultValue = "0") int lBound,
        @RequestParam(defaultValue = "999999") int uBound){
        LOG.info("GET /needs/?search="+search);
        
        if (tagIds == null) {
        tagIds = new int[0];
        }
        try{
            Need[] needs = service.searchNeeds(search,tagIds,lBound,uBound);
            if (needs.length > 0)
                return new ResponseEntity<Need[]>(needs, HttpStatus.OK);
            else
                return new ResponseEntity<>(new Need[0], HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Need need} with provided need object
     * 
     * @param need - The {@link Need need} to create
     * 
     * @return repsonse entity with created {@link Need need} object and HTTP status
     *         of CONFLICT (409)if Need
     *         already exists
     * @return response with HTTP status of UNAUTHORIZED if nt from an admin
     * @return response entity with created {@link Need need} object and HTTP status
     *         of CREATED<br>
     *         ResponseEntity with HTTP error of INTERNAL_SERVER_ERROR (500)
     *         otherwise
     */
    @PostMapping("")
    public ResponseEntity<Need> createNeed(@RequestBody Need need, @RequestParam int id) {
        LOG.info("POST /needs/?id=" + id + " " + need);
        try {
            Need createdNeed = service.createNeed(need, id);
            if (createdNeed == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(createdNeed, HttpStatus.CREATED);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the data of {@link Need need}
     * 
     * @param need - the need which needs to be updated
     * @return ResponseEntity of the new need of {@link Need need} and an HTTP
     *         status of OK (200) <br>
     *         ResponseEntity with HTTP status of NOT FOUND (404) if the need is not
     *         found <br>
     *         ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @PutMapping("")
    public ResponseEntity<Need> updateNeed(@RequestBody Need need, @RequestParam int id) {
        LOG.info("PUT /needs " + need);

        try {
            Need newNeed = service.updateNeed(need, id);
            if (newNeed == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(newNeed, HttpStatus.OK);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes and returns status of {@link Need need}
     * 
     * @param id of the {@link Need need} object to be deleted
     * @return ResponseEntity with HTTP status of OK (200) if the need is found and
     *         deleted successfully <br>
     *         ResponseEntity with HTTP status of NOT FOUND (404) if no needs are
     *         found <br>
     *         ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @DeleteMapping("/{nid}")
    public ResponseEntity<Void> deleteNeed(@PathVariable int nid, @RequestParam int id) {
        LOG.info("DELETE /needs/" + nid);
        try {
            if (service.deleteNeed(nid, id)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Adds a {@link Need need} to a {@link NeedBundle bundle}
     * 
     * @param id of the session
     * @param bundleID of the {@link NeedBundle}
     * @param need {@link Need need} to be added to bundle
     * @return ResponseEntity with HTTP status of OK (200) if the bundle is found
     *         and need is added successfully <br>
     *         ResponseEntity with HTTP status of NOT FOUND (404) if the bundleid does not return
     *         a instance of Needbundle or if the bundle is not found
     *         found <br>
     *         ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @PutMapping("/{bundleId}/add")
    public ResponseEntity<Void> addNeedToBundle(@RequestParam int id, @PathVariable int bundleId, @RequestBody Need need) {
        LOG.info("PUT /needs/" + bundleId + "/add");

        try {
            boolean success = service.addNeedtoBundle(id,bundleId, need);
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * removes a {@link Need need} from a {@link NeedBundle bundle}
     * 
     * @param id of the session
     * @param bundleID of the {@link NeedBundle bundle}
     * @param need {@link Need need} to be removed from bundle
     * @return ResponseEntity with HTTP status of OK (200) if the bundle is found
     *         and need is removed successfully <br>
     *         ResponseEntity with HTTP status of NOT FOUND (404) if the bundleid does not return
     *         a instance of Needbundle or if the bundle is not found
     *         found <br>
     *         ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @PutMapping("/{bundleId}/remove")
    public ResponseEntity<Void> removeNeedfromBundle(@RequestParam int id,@PathVariable int bundleId, @RequestBody Need need) {
        LOG.info("PUT /needs/" + bundleId + "/remove");
    
        try {
            boolean success = service.removeNeedfromBundle(id,bundleId, need);
            if (success) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
