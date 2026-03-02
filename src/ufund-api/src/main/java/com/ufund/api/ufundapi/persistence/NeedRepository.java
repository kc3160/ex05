package com.ufund.api.ufundapi.persistence;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedBundle;

@Component
public interface NeedRepository {
    /**
     * Creates and saves given {@link Need need}
     * 
     * @param need {@link Need need} object to be created
     * @return new {@link Need need}
     * @throws IOException
     */
    Need createNeed( Need need ) throws IOException;

    /**
     * Deletes and returns status of {@link Need need}
     * 
     * @param id of the {@link Need need} object to be deleted
     * @return true if {@link Need need} was deleted successfully
     * and false if failed
     * @throws IOException
     */
    boolean deleteNeed( int id ) throws IOException;

    /**
     * Updates the data of {@link Need need}
     * 
     * @param need to be updated
     * @return The updated {@link Need need}, null if it doesn't exist
     * @throws IOException
     */
    Need updateNeed( Need need ) throws IOException;

    /**
     * Gets a {@link Need need} of given {@link id}
     * 
     * @param id of {@link Need need} to get
     * @return The found {@link Need need}, null if not found
     * @throws IOException
     */
    Need getNeed( int id ) throws IOException;

    /**
     * Gets array of all {@link Need needs} in the cupboard
     * 
     * @return Array of {@link Need needs}, empty array if there are none
     * @throws IOException
     */
    Need[] getAllNeeds() throws IOException;

    /**
     * Gets array of needs from array of ids
     * @param ids Array of ids of {@link Need need} to get
     * @return Array of {@link Need needs}, empty array if there are none
     * @throws IOException
     */
    Need[] getNeedsByIds(int[] ids) throws IOException;

    /**
     * Searches for {@link Need needs} that contain the string {@link String search}
     * 
     * @param search string that should be contained in returned {@link Need needs}
     * @return Array of {@link Need needs} that contain the string {@link String search}
     * @throws IOException
     */
    Need[] searchNeeds( String search , int[] tagIds, int lBound, int uBound) throws IOException;


    /**
     * Adds a {@link Need} to a {@link NeedBundle}
     * 
     * @param id The id of the {@link NeedBundle}
     * @param need     The {@link Need} to be added
     * @return true if the operation was successful, false otherwise
     * @throws IOException
     */
    boolean addNeedtoBundle( int id,Need need) throws IOException;

    /**
     * Removes a {@link Need} from a {@link NeedBundle}
     * 
     * @param id The id of the {@link NeedBundle}
     * @param need     The {@link Need} to be removed
     * @return true if the operation was successful, false otherwise
     * @throws IOException
     */
    boolean removeNeedfromBundle( int id,Need need) throws IOException;
}
