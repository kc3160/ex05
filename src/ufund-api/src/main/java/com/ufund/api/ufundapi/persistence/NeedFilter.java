package com.ufund.api.ufundapi.persistence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ufund.api.ufundapi.model.Need;

import com.ufund.api.ufundapi.model.Tag;

public class NeedFilter {
    /**
     * Gets an Array of {@link Need needs} that match the {@link String} term
     * 
     * @param needs {@link Collection} of {@link Need needs} to filter
     * @param term to get the {@link Need needs} that contain it
     * @return Array of term matching {@link Need needs}
     */
    protected static Need[] filterByName(Collection<Need> needs, String term) {
        String lowerTerm = term.toLowerCase();
        ArrayList<Need> found = new ArrayList<>();

        synchronized (needs) {
            for (Need n : needs) {
                if (n.getName().toLowerCase().contains(lowerTerm)) {
                    found.add(n);
                }
            }
        }
        Need[] out = new Need[found.size()];
        return found.toArray(out);
    }

    /**
     * Gets an Array of {@link Need needs} that match the cost range
     * @param needs {@link Collection} of {@link Need needs} to filter
     * @param lowRange lower bound of cost
     * @param highRange upper bound of cost
     * @return Array of matching {@link Need needs}
     */
    protected static Need[] filterByCost(Collection<Need> needs, double lowRange, double highRange) {
        ArrayList<Need> found = new ArrayList<>();

        synchronized (needs) {
            for (Need n : needs) {
                if (n.getCost()<=highRange && n.getCost()>=lowRange) {
                    found.add(n);
                }
            }
        }
        Need[] out = new Need[found.size()];
        return found.toArray(out);
    }

    /**
     * Gets an Array of {@link Need needs} that match the given tags
     * @param tags {@link Collection} of {@link Tag tags} to match
     * @param needs {@link Collection} of {@link Need needs} to filter
     * @return Array of matching {@link Need needs}
     */
    protected static Need[] filterByTags(Collection<Tag> tags, Collection<Need> needs){
        Set<Integer> common = null;

        for (Tag t : tags) {
            Integer[] tagNeedIds = t.getNeeds();
            Set<Integer> tagNeedIdSet = new HashSet<>(Arrays.asList(tagNeedIds));

            if (common == null) common = tagNeedIdSet;
            else common.retainAll(tagNeedIdSet); 
            if (common.isEmpty()) break; 
        }
        if (common == null || common.isEmpty()) return new Need[0];
        
        List<Need> filtered = new ArrayList<>();
        synchronized (needs) {
            for (Need n : needs) {
                if (common.contains(n.getId())) {
                    filtered.add(n);
                }
            }
        }
        return filtered.toArray(new Need[filtered.size()]);
    }
}
