package com.ufund.api.ufundapi.model;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Basket {
    @JsonProperty("needs") private ArrayList<Need> needs;
    @JsonProperty("id") private int id;


    public Basket(int id) {
        needs = new ArrayList<>();
        this.id = id;
    }

    public ArrayList<Need> getNeeds() {
       return needs;    
   }  

    public int getId(){
       return id;    
    }
    public void addtoBasket(Need need){
        needs.add(need);
    } 
    public boolean updateNeed(int needId, int quantity){
        for(int i=0; i<needs.size(); i++){
            Need currentNeed = needs.get(i);
            if(currentNeed.getId() == needId){
                if(quantity == 0){
                    needs.remove(i);
                }else{
                    currentNeed.changeQuantity(quantity);
                }
                return true;
            }
        }
        return false;
        
    }
    public boolean removeNeed(int needId) {
        boolean removed = false;
        for (int i = needs.size() - 1; i >= 0; i--) {
            if (needs.get(i).getId() == needId) {
                needs.remove(i);
                removed = true;
                //Not breaking out since more than one of the same need can exist
            }
        }
        return removed;
    }

    public void setNeeds(ArrayList<Need> updatedNeeds) {
        this.needs = updatedNeeds;
    }
}