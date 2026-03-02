package com.ufund.api.ufundapi.model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ufund.api.ufundapi.persistence.UserRepository;

public class SessionHandlerTests {
    // Note: All users requesting a session are valid
    private UserRepository mockUserRepo;

    private SessionHandler session;

    private Random mockRand;
    private int nextInt = 12345;
    private User[] users;

    private int nextVal(boolean next) {
        if (next) {
            return nextInt++;
        }
        nextInt = 12345;
        return nextInt;
    }

    @BeforeEach
    public void setupNeedRepository() throws IOException {
        //MockitoAnnotations.openMocks(this);
        mockUserRepo = mock(UserRepository.class);
        mockRand = mock(Random.class);

        // Always return 12345 unless true;
        when(mockRand.
            nextInt())
                .thenReturn(this.nextVal(false));

        User[] baseUsers = {
            new Helper("John", "j@mail.com", "1234", 3),
            new Helper("Doe", "bruh@yahoo.net", "buh", 4),
            new Admin("Manager", "evil@goog.en", "easy"),
        };
        users = baseUsers;

        HashMap<Integer, Integer> setupSessions = new HashMap<>();
        setupSessions.put(1, this.users[0].hashCode());
        setupSessions.put(2, this.users[1].hashCode());
        
        HashMap<Integer, Integer> setupAdminSessions = new HashMap<>();
        setupAdminSessions.put(1, this.users[2].hashCode());
                
        session = new SessionHandler(setupSessions, setupAdminSessions, mockRand);

        // Very weird workaround which shouldnt need to be done
        try {
            Field userRepoField = session.getClass().getDeclaredField("userRepository");
            userRepoField.setAccessible(true);
            userRepoField.set(session, mockUserRepo);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            fail("Failed to mock UserRepository");
        }
    }

    @Test
    public void testExistCheckHelperSession() {
        int seshId = 2;
        boolean exists = session.checkHelperSession(seshId);
        assertTrue(exists);
    }

    @Test
    public void testNotExistCheckHelperSession() {
        int seshId = 64327;
        boolean exists = this.session.checkHelperSession(seshId);
        assertFalse(exists);
    }

    @Test
    public void testExistCheckAdminSession() {
        int seshId = 1;
        boolean exists = session.checkAdminSession(seshId);
        assertTrue(exists);
    }

    @Test
    public void testNotExistCheckAdminSession() {
        int seshId = 64327;
        boolean exists = this.session.checkAdminSession(seshId);
        assertFalse(exists);
    }

    @Test
    public void testValidStartHelperSession() {
        Helper h = new Helper("ran", "into@a.bar", "ow", 1);

        int id = session.startHelperSession(h);
        boolean exists = session.checkHelperSession(id);

        assertTrue(exists);
    }

    @Test
    public void testSameIdStartHelperSession() {
        when(mockRand.
            nextInt())
                .thenReturn(1);

        Helper h = new Helper("ran", "into@a.bar", "ow", 2);

        int id = session.startHelperSession(h);
        assertNotEquals(id, 1);
        boolean exists = session.checkHelperSession(id);
        assertTrue(exists);
    }

    @Test
    public void testValidStartAdminSession() {
        Admin h = new Admin("Manager2", "good@goog.en", "hard");

        int id = session.startAdminSession(h);
        boolean exists = session.checkAdminSession(id);

        assertTrue(exists);
    }

    @Test
    public void testSameIdStartAdminSession() {
        when(mockRand.
            nextInt())
                .thenReturn(1);

        Admin h = new Admin("Manager2", "good@goog.en", "hard");

        int id = session.startAdminSession(h);
        assertNotEquals(id, 1);
        boolean exists = session.checkAdminSession(id);
        assertTrue(exists);
    }

    @Test
    public void testValidAuthorizeHelperSession() {
        int seshId = 2;
        User expectedUser = users[1];
        
        when(mockUserRepo.getUserFromHash(users[1].hashCode())).thenReturn(users[1]);

        Helper user = session.authorizeHelperSession(seshId);
        assertEquals(expectedUser, user);
        assertEquals(((Helper)expectedUser).getBasketId(), user.getBasketId());
    }

    @Test
    public void testInvalidAuthorizeHelperSession() {
        int seshId = 35678;
        Helper user = session.authorizeHelperSession(seshId);
        assertNull(user);
    }

    @Test
    public void testValidAuthorizeAdminSession() {
        int seshId = 1;
        User expectedUser = users[2];

        when(mockUserRepo.getUserFromHash(users[2].hashCode())).thenReturn(users[2]);

        Admin user = session.authorizeAdminSession(seshId);
        assertEquals(expectedUser, user);
    }

    @Test
    public void testInvalidAuthorizeAdminSession() {
        int seshId = 35678;
        Admin user = session.authorizeAdminSession(seshId);
        assertNull(user);
    }

    @Test
    public void testValidremoveHelperSession() {
        int seshId = 2;
        boolean deleted = session.removeHelperSession(seshId);
        boolean exists = session.checkHelperSession(seshId);
        assertTrue(deleted);
        assertFalse(exists);
    }

    @Test
    public void testInvalidremoveHelperSession() {
        int seshId = 59292;
        boolean deleted = session.removeHelperSession(seshId);
        assertFalse(deleted);
    }

    @Test
    public void testValidremoveAdminSession() {
        int seshId = 1;
        boolean deleted = session.removeAdminSession(seshId);
        boolean exists = session.checkAdminSession(seshId);
        assertTrue(deleted);
        assertFalse(exists);
    }

    @Test
    public void testInvalidremoveAdminSession() {
        int seshId = 59292;
        boolean deleted = session.removeAdminSession(seshId);
        assertFalse(deleted);
    }

    @Test
    public void testValidHasHelperSession() {
        boolean expected = session.hasHelperSession(this.users[0].hashCode());
        assertTrue(expected);
    }

    @Test
    public void testInvalidHasHelperSession() {
        boolean expected = session.hasHelperSession(12345678);
        assertFalse(expected);
    }

    @Test
    public void testValidHasAdminSession() {
        boolean expected = session.hasAdminSession(this.users[2].hashCode());
        assertTrue(expected);
    }

    @Test
    public void testInvalidHasAdminSession() {
        boolean expected = session.hasAdminSession(12345678);
        assertFalse(expected);
    }
}