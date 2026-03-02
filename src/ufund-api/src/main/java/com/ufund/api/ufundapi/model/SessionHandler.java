package com.ufund.api.ufundapi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ufund.api.ufundapi.persistence.UserRepository;

@Component
public class SessionHandler {
    @Autowired
    private UserRepository userRepository;

    private final HashMap<Integer, Integer> sessions;
    private final HashMap<Integer, Integer> adminSessions;
    private Random rng;

    public SessionHandler(HashMap<Integer, Integer> sessions, HashMap<Integer, Integer> adminSessions, Random rng) {
        this.sessions = sessions;
        this.adminSessions = adminSessions;
        this.rng = rng;
    }

    /**
     * Get the session id of a {@link Helper helper}
     * @param {@link Helper user} to login
     * @return Session id from {@link Helper user}
     */
    public int startHelperSession(User user) {
        synchronized (this.sessions) {
            //int loginTime = (int) (System.currentTimeMillis() / 1000L);
            Integer lastId = getHelperSessionId(user.hashCode());
            if (lastId != null) {
                this.sessions.remove(lastId);
            }
            int sessionId = this.rng.nextInt();
            while (this.sessions.containsKey(sessionId)) {
                ++sessionId; // Do this for now
            }
            this.sessions.put(sessionId, user.hashCode());
            return sessionId;
        }
    }

    /**
     * Get the session id of a {@link Admin admin}
     * @param {@link Admin user} to login
     * @return Session id from {@link Admin user}
     */
    public int startAdminSession(User user) {
        //! Currently seperating Admin and Helper since 
        //! they could be implemented differently
        synchronized (this.adminSessions) {
            Integer lastId = getAdminSessionId(user.hashCode());
            if (lastId != null) {
                this.adminSessions.remove(lastId);
            }
            int sessionId = this.rng.nextInt();
            while (this.adminSessions.containsKey(sessionId)) {
                ++sessionId; // Do this for now
            }
            this.adminSessions.put(sessionId, user.hashCode());
            return sessionId;
        }
    }

    /**
     * Checks if the given session id exists then returns the 
     * hashcode for the {@link Admin admin}.
     * 
     * @param id - Session id to check
     * @return int Hash to refer to a {@link Admin admin}, null if session does not exist
     */
    public Admin authorizeAdminSession(int id) {
        Integer userHash;
        synchronized (this.adminSessions) {
            userHash = this.adminSessions.get(id);
        }
        if (userHash == null) {
            return null;
        }
        User foundUser = userRepository.getUserFromHash(userHash);
        if (!(foundUser instanceof Admin)) {
            return null;
        }
        return (Admin) foundUser;
    }

    /**
     * Checks if the given session id exists then returns the 
     * hashcode for the {@link Helper helper}.
     * 
     * @param id - Session id to check
     * @return int Hash to refer to a {@link Helper helper}, null if session does not exist
     */
    public Helper authorizeHelperSession(int id) {
        Integer userHash;
        synchronized (this.sessions) {
            userHash = this.sessions.get(id);
        }
        if (userHash == null) {
            return null;
        }
        User foundUser = userRepository.getUserFromHash(userHash);
        if (!(foundUser instanceof Helper)) {
            return null;
        }
        return (Helper) foundUser;
    }

    /**
     * Checks if {@link Helper helper} with session id has a current session
     * @param id - session id
     * @return true when session exists, false otherwise
     */
    public boolean checkHelperSession(int id) {
        synchronized (this.sessions) {
            return this.sessions.containsKey(id);
        }
    }

    /**
     * Checks if {@link Admin admin} with session id has a current session
     * @param id - session id
     * @return true when session exists, false otherwise
     */
    public boolean checkAdminSession(int id) {
        synchronized (this.adminSessions) {
            return this.adminSessions.containsKey(id);
        }
    }

    /**
     * Logs out the {@link Helper helper} with id
     * @param id - Session id to remove from active sessions
     * @return true if successfully removed, false otherwise
     */
    public boolean removeHelperSession(int id) {
        synchronized (this.sessions) {
            Integer removed = this.sessions.remove(id);
            if (removed == null) {
                return false;
            } 
            return true;
        }
    }

    /**
     * Logs out the {@link Admin admin} with id
     * @param id - Session id to remove from active sessions
     * @return true if successfully removed, false otherwise
     */
    public boolean removeAdminSession(int id) {
        synchronized (this.adminSessions) {
            Integer removed = this.adminSessions.remove(id);
            if (removed == null) {
                return false;
            }
            return true;
        }
    }

    private Integer getHelperSessionId(int userHash) {
        synchronized (this.sessions) {
            for (Map.Entry<Integer, Integer> entry : this.sessions.entrySet()) {
                if (entry.getValue() == userHash) {
                    return entry.getKey();
                }
            }
            return null;
        }
    }

    public boolean hasHelperSession(int userHash) {
        synchronized (this.sessions) {
            Integer id = getHelperSessionId(userHash);

            return id != null;
        }
    }

    private Integer getAdminSessionId(int userHash) {
        synchronized (this.adminSessions) {
            for (Map.Entry<Integer, Integer> entry : this.adminSessions.entrySet()) {
                if (entry.getValue() == userHash) {
                    return entry.getKey();
                }
            }
            return null;
        }
    }

    public boolean hasAdminSession(int userHash) {
        synchronized (this.adminSessions) {
            Integer id = getAdminSessionId(userHash);

            return id != null;
        }
    }
}
