package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.controller.NeedController;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.SessionHandler;
import com.ufund.api.ufundapi.persistence.NeedRepository;

@Service
public class NeedService {

    @Autowired
    private NeedRepository needHandler;

    @Autowired
    private SessionHandler sessionHandler;

    private static final Logger LOG = Logger.getLogger(NeedController.class.getName());

    public Need getNeedById(int id) throws IOException {
        Need need = needHandler.getNeed(id);

        /* Other logic */

        return need;
    }

    public Need[] getAllNeeds() throws IOException {
        Need[] needs = needHandler.getAllNeeds();

        /* Other logic */

        return needs;
    }

     public Need[] getNeedsByIds(int[] ids) throws IOException{
        Need[] needs = needHandler.getNeedsByIds(ids);

        if(needs == null) return new Need[0];
        return needs;
    }

    public Need[] searchNeeds(String search, int[] tagIds, int lBound, int uBound) throws IOException {
        Need[] needs = needHandler.searchNeeds(search, tagIds, lBound, uBound);
        /* Other logic */

        return needs;
    }

    public Need createNeed(Need need, int id) throws IOException {
        if (!sessionHandler.checkAdminSession(id)) {
            return null;
        }
        Need createdNeed = needHandler.createNeed(need);

        /* Other logic */

        return createdNeed;
    }

    public Need updateNeed(Need need, int id) throws IOException {
        if (!sessionHandler.checkAdminSession(id)) {
            return null;
        }
        Need updatedNeed = needHandler.updateNeed(need);

        /* Other logic */

        return updatedNeed;
    }

    public boolean deleteNeed(int nid, int id) throws IOException {
        if (!sessionHandler.checkAdminSession(id)) {
            return false;
        }
        boolean deleted = needHandler.deleteNeed(nid);

        /* Other logic */

        return deleted;
    }

    public boolean addNeedtoBundle( int id,int bundleId, Need need) throws IOException {

        if (!sessionHandler.checkAdminSession(id)) {
            return false;
        }

        return needHandler.addNeedtoBundle(bundleId, need);
    }

    public boolean removeNeedfromBundle( int id,int bundleId, Need need) throws IOException {

        if (!sessionHandler.checkAdminSession(id)) {
            return false;
        }
        return needHandler.removeNeedfromBundle(bundleId, need);
    }
}
