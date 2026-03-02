package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Tag;
import com.ufund.api.ufundapi.service.NeedService;
import com.ufund.api.ufundapi.service.TagService;

@RestController
@RequestMapping("tags")
public class TagController {
    @Autowired
    private TagService service;

    private static final Logger LOG = Logger.getLogger(TagController.class.getName());

    /**
     * API response for getting a {@linkplain Tag tag} using an id
     * 
     * @param id the id used to get the {@link Tag tag} 
     * 
     * @return ResponseEntity of a {@link Tag Tag} and an HTTP status of OK (200) <br>
     * ResponseEntity with HTTP status of NOT FOUND (404) if no Tags are found <br>
     * ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise     */
    @GetMapping("/{nid}")
    public ResponseEntity<Tag[]> getNeedTags(@PathVariable int nid){
        LOG.info("GET /tags/"+nid);
        try{
            Tag[] tags = service.getTagsByNeed(nid);
            if (tags != null)
                return new ResponseEntity<Tag[]>(tags,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * API response for getting list of tags
     * 
     * @return ResponseEntity of tags and an HTTP status of OK (200) if tags are found
     * If no tags are found, it returns an empty array with an HTTP status of OK (200). 
     * ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise     
     */
    @GetMapping("")
    public ResponseEntity<Tag[]> getTags(){
        LOG.info("GET /tags");
        try {
            Tag[] tags = service.getAllTags();
            if (tags != null && tags.length > 0) {
                return new ResponseEntity<>(tags, HttpStatus.OK); 
            } else {
                return new ResponseEntity<>(new Tag[0], HttpStatus.OK); 
            }
        }
        catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Tag tag} with provided tag object
     * 
     * @param tag - The {@link Tag tag} to create
     * 
     * @return repsonse entity with created {@link Tag tag} object and HTTP status of CONFLICT (409) if tag
     * already exists
     * @return response with HTTP status of UNAUTHORIZED if not from an admin
     * @return response entity with created {@link Tag tag} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP error of INTERNAL_SERVER_ERROR (500) otherwise
     */
    @PostMapping("")
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag, @RequestParam int id) {
        LOG.info("POST /tags/?id="+id+" "+tag);
        try{
            Tag createdTag = service.createTag(tag, id);
            if (createdTag == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the data of {@link Tag tag}
     * 
     * @param tag - the tag which needs to be updated
     * @return ResponseEntity of the new tag of {@link Tag tag} and an HTTP status of OK (200) <br>
     * ResponseEntity with HTTP status of NOT FOUND (404) if the tag is not found <br>
     * ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @PutMapping("")
    public ResponseEntity<Tag> updateTag(@RequestBody Tag tag, @RequestParam int id) {
        LOG.info("PUT /tags "+tag);
        
        try{
            Tag newTag = service.updateTag(tag, id);
            if(newTag==null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(newTag, HttpStatus.OK);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a need to a given tag
     * @param tagId
     * @param id
     * @return
     */
    @PutMapping("/{needId}/{oldTagId}/{tagId}")
    public ResponseEntity<Void> updateNeedTag(@PathVariable int needId, @PathVariable int oldTagId, @PathVariable int tagId, @RequestParam int id) {
        LOG.info("PUT /tags/"+needId+"/"+oldTagId+"/"+tagId+"?id="+id);

        try {
            boolean result = service.updateNeedTag(needId, oldTagId, tagId, id);
            if (result) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes and returns status of {@link Tag tag}
     * 
     * @param id of the {@link Tag tag} object to be deleted
     * @return ResponseEntity with HTTP status of OK (200) if the tag is found and deleted successfully <br>
     * ResponseEntity with HTTP status of NOT FOUND (404) if no tags are found <br>
     * ResponseEntity with HTTP status INTERNAL SERVER ERROR (500) otherwise
     */
    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable int tagId, @RequestParam int id) {
        LOG.info("DELETE /tags/"+tagId);
        try{
            if(service.deleteTag(tagId, id)){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IOException e){
            LOG.log(Level.SEVERE, e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
