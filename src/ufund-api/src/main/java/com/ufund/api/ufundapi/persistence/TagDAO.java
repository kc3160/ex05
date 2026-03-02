package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Tag;

@Component
public class TagDAO implements TagRepository{
    private String filename;
    private ObjectMapper objectMapper;

    private int nextId;
    private final Map<Integer, Tag> cachedTags;
    public TagDAO(@Value("${tags.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.nextId = 0;

        this.cachedTags = new TreeMap<>();
        load();
    }
    /**
     * Return the next ID available
     * 
     * @return an int ID
     */
    private synchronized int getNextId() {
        //? Not static because it is only instantiated once
        int thisId = this.nextId++;
        return thisId;
    }

    /**
     * Loads the tag cache from file, not thread safe but not called
     * in a way that it should matter.
     * 
     * @return true if successfully retreived
     * @throws IOException
     */
    private boolean load() throws IOException {
        Tag[] tagArray = this.objectMapper.readValue(new File(this.filename), Tag[].class);

        //! ID MAY BE TEMPORARY AND MUST BE CHANGED LATER
        for (Tag t : tagArray) {
            this.cachedTags.put(t.getId(), t);
            if (t.getId() > this.nextId) {
                this.nextId = t.getId();
            }
        }
        this.nextId++;

        return true;
    }

    /**
     * Saves the current tag cache to file
     * 
     * @return true if successfully saved
     * @throws IOException
     */
    private boolean save() throws IOException {
        synchronized (this.cachedTags) {
            Tag[] tagArray = getTagsArray();

            this.objectMapper.writeValue(new File(this.filename), tagArray);
    
            return true;
        }
    }

    /**
     * Turns tag cache into Array of {@link tag tags}
     * 
     * @return Array of {@link tag tags} in cache
     */
    private Tag[] getTagsArray() {
        synchronized (this.cachedTags) {
            Tag[] tagsArray = new Tag[this.cachedTags.size()];
            return this.cachedTags.values().toArray(tagsArray);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag createTag(Tag tag) throws IOException {
        Tag newtag = tag.copyWithId(getNextId());
        synchronized (this.cachedTags) {
            this.cachedTags.put(newtag.getId(), newtag);
            save();
        }
        return newtag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteTag(int id) throws IOException {
        synchronized (this.cachedTags) {
            if (!this.cachedTags.containsKey(id)) {
                return false;
            }
            this.cachedTags.remove(id);
            save();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag[] getAllTags() throws IOException {
        //! Don't know if this synchronized is necessary
        synchronized (this.cachedTags) {
            return getTagsArray();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag getTag(int id) throws IOException {
        synchronized (this.cachedTags) {
            if (!this.cachedTags.containsKey(id)) {
                return null;
            }
            return this.cachedTags.get(id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tag updateTag(Tag tag) throws IOException {
        synchronized (this.cachedTags) {
            if (!this.cachedTags.containsKey(tag.getId())) {
                return null;
            }
            this.cachedTags.put(tag.getId(), tag);
            save();
            return tag;
        }
    }

}
