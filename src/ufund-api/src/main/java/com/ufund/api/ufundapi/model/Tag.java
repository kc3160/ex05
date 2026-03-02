package com.ufund.api.ufundapi.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Tag {
    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("needIds") private Set<Integer> needIds;

    /**
     * Creates a new {@link Tag} with given id, name, and list of needIds
     * @param id of new {@link Tag}
     * @param name of new {@link Tag}
     * @param needIds of new {@link Tag}
     */
    public Tag(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("needIds") Set<Integer> needIds){
        this.id = id;
        this.name = name;
        if (needIds == null) {
            this.needIds = new HashSet<>();
        } else {
            this.needIds = needIds;
        }
    }

    /**
     * Gets the id of {@link Tag}
     * @return id of {@link Tag}
     */
    public int getId(){
        return id;
    }

    /**
     * Gets the name of {@link Tag}
     * @return name of {@link Tag}
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the name of {@link Tag}
     * @param name of {@link Tag}
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Adds a need id to the needId array of {@link Tag}
     * @param need added to the need array of {@link Tag}
     */
    public void addNeed(Need need){
        needIds.add(need.getId());
    }

    /**
     * Removes a need from the needId array of {@link Tag}
     * @param id of need removed from need array of {@link Tag}
     */
    public void removeNeed(int id){
        needIds.remove(id);
    }

    /**
     * Gets the array of needIds held by {@link Tag}
     * @return array of needIds from {@link Tag}
     */
    public Integer[] getNeeds(){
        return needIds.toArray(new Integer[0]);
    }
    
    /**
     * Creates a new {@link Tag} with given id
     * @param id of the new {@link Tag}
     * @return the new {@link Tag}
     */
    public Tag copyWithId(int id) {
        Tag copy = new Tag(id, this.name, this.needIds);
        return copy;
    }

    public boolean hasNeed(int id) {
        synchronized (this.needIds) {
            return this.needIds.contains(id);
        }
    }
}
