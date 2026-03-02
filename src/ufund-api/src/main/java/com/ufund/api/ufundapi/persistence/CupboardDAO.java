package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Tag;

import com.ufund.api.ufundapi.model.NeedBundle;


@Component
public class CupboardDAO implements NeedRepository {
    @Autowired
    private TagRepository tagRepository;

    private String filename;
    private ObjectMapper objectMapper;

    // ! MAY NEED TO CHANGE
    private int nextId;

    // Using this so we don't have to keep getting stuff from files
    private final Map<Integer, Need> cachedNeeds;

    public CupboardDAO(@Value("${cupboard.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.nextId = 0;

        this.cachedNeeds = new TreeMap<>();
        load();
    }

    /**
     * Return the next ID available
     * 
     * @return an int ID
     */
    private synchronized int getNextId() {
        // ? Not static because it is only instantiated once
        int thisId = this.nextId++;
        return thisId;
    }

    /**
     * Loads the need cache from file, not thread safe but not called
     * in a way that it should matter.
     * 
     * @return true if successfully retreived
     * @throws IOException
     */
    private boolean load() throws IOException {
        Need[] needArray = this.objectMapper.readValue(new File(this.filename), Need[].class);

        // ! ID MAY BE TEMPORARY AND MUST BE CHANGED LATER
        for (Need n : needArray) {
            this.cachedNeeds.put(n.getId(), n);
            if (n.getId() > this.nextId) {
                this.nextId = n.getId();
            }
        }
        this.nextId++;

        return true;
    }

    /**
     * Saves the current need cache to file
     * 
     * @return true if successfully saved
     * @throws IOException
     */
    private boolean save() throws IOException {
        synchronized (this.cachedNeeds) {
            Need[] needArray = getNeedsArray();

            this.objectMapper.writeValue(new File(this.filename), needArray);

            return true;
        }
    }

    /**
     * Turns need cache into Array of {@link Need needs}
     * 
     * @return Array of {@link Need needs} in cache
     */
    private Need[] getNeedsArray() {
        synchronized (this.cachedNeeds) {
            Need[] needsArray = new Need[this.cachedNeeds.size()];
            return this.cachedNeeds.values().toArray(needsArray);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need createNeed(Need need) throws IOException {
        Need newNeed = need.copyWithId(getNextId());
        synchronized (this.cachedNeeds) {
            this.cachedNeeds.put(newNeed.getId(), newNeed);
            save();
        }
        return newNeed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteNeed(int id) throws IOException {
        synchronized (this.cachedNeeds) {
            if (!this.cachedNeeds.containsKey(id)) {
                return false;
            }
            this.cachedNeeds.remove(id);
            save();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need[] getAllNeeds() throws IOException {
        // ! Don't know if this synchronized is necessary
        synchronized (this.cachedNeeds) {
            return getNeedsArray();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Need getNeed(int id) throws IOException {
        synchronized (this.cachedNeeds) {
            if (!this.cachedNeeds.containsKey(id)) {
                return null;
            }
            return this.cachedNeeds.get(id);
        }
    }
  
     /**
     * {@inheritDoc}
     */
    @Override
    public Need[] getNeedsByIds(int[] ids) throws IOException{
        ArrayList<Need> needs = new ArrayList<>();
        for(int id : ids){
            if(this.getNeed(id) != null) needs.add(this.getNeed(id));
        }
        return needs.toArray(new Need[needs.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override

    public Need[] searchNeeds(String search, int[] tagIds, int lBound, int uBound) throws IOException {
        synchronized (this.cachedNeeds) {
            //sets up all needs to be filtered
            Collection<Need> vals = new HashSet<>();
            vals.addAll(this.cachedNeeds.values());
            List<Need> allNeeds = new ArrayList<>(vals);

            //getting tags
            List<Tag> tags = new ArrayList<>();

            for (int tagId : tagIds) {
                Tag foundTag = tagRepository.getTag(tagId);
                if (foundTag == null) {
                    continue;
                }
                tags.add(foundTag);
            }
            
            //apply filters
            Need[] nArr = allNeeds.toArray(new Need[allNeeds.size()]);
            if (!tags.isEmpty()) {
                nArr = NeedFilter.filterByTags(tags, Arrays.asList(nArr));
            }
            if (!search.trim().equals("")) {
                nArr = NeedFilter.filterByName(Arrays.asList(nArr), search);
            }
            nArr = NeedFilter.filterByCost(Arrays.asList(nArr), lBound, uBound);

            return nArr;
        }
    }
          
          

    /**
     * {@inheritDoc}
     */
    @Override
    public Need updateNeed(Need need) throws IOException {
        synchronized (this.cachedNeeds) {
            if (!this.cachedNeeds.containsKey(need.getId())) {
                return null;
            }
            if (need instanceof NeedBundle) {
                NeedBundle bundle = (NeedBundle) need;

                double newCost = 0;
                double discount = bundle.getDiscount();

                for (Map.Entry<Integer, Integer> entry : bundle.getNeeds().entrySet()) {
                    int needId = entry.getKey();
                    int quantity = entry.getValue();

                    Need referencedNeed = this.cachedNeeds.get(needId);
                    if (referencedNeed != null) {
                        double needCost = referencedNeed.getCost();
                        newCost += quantity * (needCost - (needCost * discount));
                    }
                }
                bundle.setCost(newCost);
            }
            this.cachedNeeds.put(need.getId(), need);
            save();
            return need;
        }
    }

    /**
     * Adds a {@link Need} to a {@link NeedBundle}
     * 
     * @param id   The id of the {@link NeedBundle}
     * @param need The {@link Need} to be added
     * @return true if the operation was successful, false otherwise
     * @throws IOException
     */
    public boolean addNeedtoBundle(int id, Need need) throws IOException {
        synchronized (this.cachedNeeds) {
            if (!this.cachedNeeds.containsKey(id)) {
                return false;
            }

            Need existingNeed = this.cachedNeeds.get(id);

            if (!(existingNeed instanceof NeedBundle)) {
                return false;
            }
            NeedBundle bundle = (NeedBundle) existingNeed;
            bundle.addNeedtoBundle(need);

            this.cachedNeeds.put(id, bundle);
            save();
            return true;
        }
    }

    /**
     * Removes a {@link Need} from a {@link NeedBundle}
     * 
     * @param id   The id of the {@link NeedBundle}
     * @param need The {@link Need} to be removed
     * @return true if the operation was successful, false otherwise
     * @throws IOException
     */
    public boolean removeNeedfromBundle(int id, Need need) throws IOException {
        synchronized (this.cachedNeeds) {
            if (!this.cachedNeeds.containsKey(id)) {
                return false;
            }

            Need existingNeed = this.cachedNeeds.get(id);

            if (!(existingNeed instanceof NeedBundle)) {
                return false;
            }

            NeedBundle bundle = (NeedBundle) existingNeed;
            bundle.removeNeedfromBundle(need);

            this.cachedNeeds.put(id, bundle);
            save();
            return true;
        }
    }
}
