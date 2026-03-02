package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.controller.TagController;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.SessionHandler;
import com.ufund.api.ufundapi.model.Tag;
import com.ufund.api.ufundapi.persistence.NeedRepository;
import com.ufund.api.ufundapi.persistence.TagRepository;

@Service
public class TagService {
    @Autowired
    private TagRepository tagHandler;

    @Autowired
    private SessionHandler sessionHandler;

    @Autowired
    private NeedRepository needHandler;

    private static final Logger LOG = Logger.getLogger(TagController.class.getName());

    public Tag[] getTagsByNeed(int needId) throws IOException {
        Tag[] searchTags = tagHandler.getAllTags();
        ArrayList<Tag> found = new ArrayList<>();
        for (Tag tag : searchTags) {
            if (tag.hasNeed(needId)) {
                found.add(tag);
            }
        }
        return found.toArray(new Tag[found.size()]);
    }

    public Tag getTagById(int id) throws IOException {
        Tag tag = tagHandler.getTag(id);

        /* Other logic */

        return tag;
    }

    public Tag[] getAllTags() throws IOException {
        Tag[] tags = tagHandler.getAllTags();

        /* Other logic */

        return tags;
    }

    public Tag createTag(Tag tag, int id) throws IOException {
        if (!sessionHandler.checkAdminSession(id)) {
            return null;
        }
        Tag createdTag = tagHandler.createTag(tag);

        /* Other logic */

        return createdTag;
    }

    public Tag updateTag(Tag tag, int id) throws IOException {
        if (!sessionHandler.checkAdminSession(id)) {
            return null;
        }
        Tag updatedTag = tagHandler.updateTag(tag);

        /* Other logic */

        return updatedTag;
    }

    public boolean updateNeedTag(int needId, int oldTagId, int tagId, int id) throws IOException {
        if (!sessionHandler.checkAdminSession(id)) {
            return false;
        }

        Need foundNeed = needHandler.getNeed(needId);
        if (foundNeed == null) {
            return false;
        }

        Tag foundTag = tagHandler.getTag(tagId);
        Tag foundOldTag = tagHandler.getTag(oldTagId);

        if (tagId == -1 && foundOldTag != null) {
            foundOldTag.removeNeed(needId);
            tagHandler.updateTag(foundOldTag);
        } 
        else if (oldTagId == -1 && foundTag != null) {
            foundTag.addNeed(foundNeed);
            tagHandler.updateTag(foundTag);
        }
        else if (foundOldTag != null && foundTag != null) {
            foundOldTag.removeNeed(needId);
            foundTag.addNeed(foundNeed);

            tagHandler.updateTag(foundOldTag);
            tagHandler.updateTag(foundTag);
        }
        else {
            return false;
        }
        return true;
    }

    public boolean deleteTag(int tagId, int id) throws IOException {
        if (!sessionHandler.checkAdminSession(id)) {
            return false;
        }
        boolean deleted = tagHandler.deleteTag(tagId);

        /* Other logic */

        return deleted;
    }

}
