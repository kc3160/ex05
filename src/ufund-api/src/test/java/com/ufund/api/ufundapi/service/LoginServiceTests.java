package com.ufund.api.ufundapi.service;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ufund.api.ufundapi.model.Admin;
import com.ufund.api.ufundapi.model.Helper;
import com.ufund.api.ufundapi.model.SessionHandler;
import com.ufund.api.ufundapi.model.User;
import com.ufund.api.ufundapi.persistence.NeedRepository;
import com.ufund.api.ufundapi.persistence.UserRepository;

public class LoginServiceTests{
    @Mock
    private UserRepository userRepository;
    @Mock 
    private SessionHandler session;
    @Mock
    private NeedRepository needRepository;

    private User mockHelper, mockAdmin;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockHelper = new Helper("Test User", "test@example.com", "securepassword", 10);
        mockAdmin = new Admin("Test_Admin", "admin@g.com", "passowrd");
        when(userRepository.checkAdmin(mockAdmin)).thenReturn(true);
        when(userRepository.checkAdmin(mockHelper)).thenReturn(false);
        when(userRepository.checkHelper(mockAdmin)).thenReturn(false);
        when(userRepository.checkHelper(mockHelper)).thenReturn(true);
    }

    @Test
    void testGetHelperSessionIdFromLogin_Success() throws IOException{
        int expected = 5;

        when(userRepository.checkHelper((Helper) mockHelper))
            .thenReturn(true);
        when(session.startHelperSession((Helper) mockHelper))
        .thenReturn(5);
        when(session.hasHelperSession(mockHelper.hashCode()))
            .thenReturn(false);
        int returned = loginService.getSessionIdFromLogin(mockHelper);
        assertEquals(expected, returned);

    }

    @Test
    void testGetHelperSessionIdFromLogin_SessionExists() throws IOException{
        int expected = 5;
        when(userRepository.checkHelper((Helper) mockHelper))
        .thenReturn(true);
        when(session.startHelperSession((Helper) mockHelper))
        .thenReturn(5);
        when(session.hasHelperSession(mockHelper.hashCode())).thenReturn(true);
        int returned = loginService.getSessionIdFromLogin(mockHelper);
        assertEquals(expected, returned);
    }

    @Test
    void testGetHelperSessionIdFromLogin_NotExists() throws IOException{
        when(userRepository.checkHelper((Helper)mockHelper))
            .thenReturn(false);
        when(session.startHelperSession((Helper) mockHelper))
            .thenReturn(5);
        when(session.hasHelperSession(mockHelper.hashCode()))
            .thenReturn(false);
        Integer returned = loginService.getSessionIdFromLogin(mockHelper);
        assertNull(returned);
    }

    @Test
    void testGetAdminSessionIdFromLogin_Success() throws IOException{
        int expected = 6;
        when(userRepository.checkAdmin((Admin) mockAdmin))
        .thenReturn(true);
        when(session.startAdminSession((Admin) mockAdmin))
        .thenReturn(6);
        when(session.hasAdminSession(mockAdmin.hashCode()))
        .thenReturn(false);
        int returned = loginService.getSessionIdFromLogin(mockAdmin);
        assertEquals(expected, returned);

    }

    @Test
    void testGetAdminSessionIdFromLogin_SessionExists() throws IOException{
        int expected = 6;
        when(userRepository.checkAdmin((Admin) mockAdmin))
        .thenReturn(true);
        when(session.startAdminSession((Admin) mockAdmin))
        .thenReturn(6);
        when(session.hasAdminSession(mockAdmin.hashCode()))
        .thenReturn(true);
        when(session.hasAdminSession(mockAdmin.hashCode())).thenReturn(true);
        int returned = loginService.getSessionIdFromLogin(mockAdmin);
        assertEquals(expected, returned);
    }

    @Test
    void testGetAdminSessionIdFromLogin_NotExist() throws IOException{
        when(userRepository.checkAdmin((Admin) mockAdmin))
        .thenReturn(false);
        when(session.startAdminSession((Admin) mockAdmin))
        .thenReturn(6);
        when(session.hasAdminSession(mockAdmin.hashCode()))
        .thenReturn(false);
        when(session.hasAdminSession(mockAdmin.hashCode())).thenReturn(true);
        Integer returned = loginService.getSessionIdFromLogin(mockAdmin);
        assertNull(returned);
    }

    @Test
    void testLogoutHelper_Success(){
        int id = 5;
        when(session.removeHelperSession(id))
        .thenReturn(true);
        boolean returned = loginService.logoutHelper(id);
        assertTrue(returned);
    } 
    @Test
    void testLogoutHelper_Failure(){
        int id = 5;
        when(session.removeHelperSession(id))
        .thenReturn(false);
        boolean returned = loginService.logoutHelper(id);
        assertFalse(returned);
    }
    @Test
    void testLogoutAdmin_Success(){
        int id = 5;
        when(session.removeAdminSession(id))
        .thenReturn(true);
        boolean returned = loginService.logoutAdmin(id);
        assertTrue(returned);
    } 
    @Test
    void testLogoutAdmin_Failure(){
        int id = 5;
        when(session.removeAdminSession(id))
        .thenReturn(false);
        boolean returned = loginService.logoutAdmin(id);
        assertFalse(returned);
    }
    
    @Test
    void testRegisterHelper_Success() throws IOException{
        when(userRepository.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword()))
            .thenReturn((Helper) mockHelper);
        Helper expectedHelper = loginService.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword());
        assertEquals(mockHelper, expectedHelper);
    }

    @Test
    void testRegisterHelper_Failure() throws IOException{
        when(userRepository.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword()))
            .thenReturn(new Helper("Steve", "g.mal", "paswor", 10));
        Helper expectedHelper = loginService.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword());
        assertNotEquals(mockHelper, expectedHelper);
    }

    @Test
    void testIsAdmin_Success() {
        assertTrue(loginService.isAdmin(mockAdmin));
    }

    @Test
    void testIsAdmin_Failure() {
        assertFalse(loginService.isAdmin(mockHelper));
    }

    @Test
    void testIsHelper_Success() {
        assertTrue(loginService.isHelper(mockHelper));
    }

    @Test
    void testIsHelper_Failure() {
        assertFalse(loginService.isHelper(mockAdmin));
    }
}