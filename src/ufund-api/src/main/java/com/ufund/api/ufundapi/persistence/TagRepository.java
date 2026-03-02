package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Tag;

@Component
public interface TagRepository {
    /**
     * Creates and saves given {@link Tag tag}
     * 
     * @param tag {@link Tag tag} object to be created
     * @return new {@link Tag tag}
     * @throws IOException
     */
    Tag createTag( Tag tag ) throws IOException;

    /**
     * Deletes and returns status of {@link Tag tag}
     * 
     * @param id of the {@link Tag tag} object to be deleted
     * @return true if {@link Tag tag} was deleted successfully
     * and false if failed
     * @throws IOException
     */
    boolean deleteTag( int id ) throws IOException;

    /**
     * Updates the data of {@link Tag tag}
     * 
     * @param tag to be updated
     * @return The updated {@link Tag tag}, null if it doesn't exist
     * @throws IOException
     */
    Tag updateTag( Tag tag ) throws IOException;

    /**
     * Gets a {@link Tag tag} of given {@link id}
     * 
     * @param id of {@link Tag tag} to get
     * @return The found {@link Tag tag}, null if not found
     * @throws IOException
     */
    Tag getTag( int id ) throws IOException;

    /**
     * Gets array of all {@link Tag tags} in the cupboard
     * 
     * @return Array of {@link Tag tags}, empty array if there are none
     * @throws IOException
     */
    Tag[] getAllTags() throws IOException;

    /**
     * Searches for {@link Tag tags} that contain the string {@link String search}
     * 
     * @param search string that should be contained in returned {@link Tag tags}
     * @return Array of {@link Tag tags} that contain the string {@link String search}
     * @throws IOException
     */
    //Tag[] searchTags( String search ) throws IOException;
}
