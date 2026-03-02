package com.ufund.api.ufundapi.controller;

import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ufund.api.ufundapi.model.Admin;
import com.ufund.api.ufundapi.model.Helper;
import com.ufund.api.ufundapi.service.LoginService;

@ExtendWith(MockitoExtension.class)

public class LoginControllerTests {

    @Mock
    private LoginService loginService;

    @InjectMocks
    private LoginController loginController;

    private Helper mockHelper;
    private Admin mockAdmin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockHelper = new Helper("Test User", "test@example.com", "securepassword", 5);
        mockAdmin = new Admin("Test_Admin", "admin@g.com", "passowrd");
    }

    @Test
    void testRegisterHelper_Success() throws IOException {
        when(loginService.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword()))
                .thenReturn(mockHelper);
        ResponseEntity<Helper> response = loginController.registerHelper(mockHelper);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockHelper.getEmail(), response.getBody().getEmail());
    }

    @Test
    void testRegisterHelper_EmailAlreadyExists() throws IOException {
        when(loginService.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword()))
                .thenReturn(null);
        ResponseEntity<Helper> response = loginController.registerHelper(mockHelper);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void testRegisterHelper_InternalServerError() throws IOException {
        when(loginService.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword()))
                .thenThrow(new IOException("Error"));
        ResponseEntity<Helper> response = loginController.registerHelper(mockHelper);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testLogIntoHelperSession_Success() throws IOException {
        int expectedId = 1;
        boolean expectedAdmin = false;
        Mockito.lenient().when(loginService.getSessionIdFromLogin(mockHelper))
                .thenReturn(expectedId);
        Mockito.lenient().when(loginService.isAdmin(mockAdmin)).thenReturn(expectedAdmin);
        ResponseEntity<ObjectNode> response = loginController.logIntoSession(mockHelper);
        ObjectNode actual = response.getBody();
        assertNotNull(actual);
        assertEquals(expectedId, actual.get("id").asInt());
        assertEquals(expectedAdmin, actual.get("admin").asBoolean());
    }

    @Test
    void testLogIntoHelperSession_BadLogin() throws IOException {
        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        Mockito.lenient().when(loginService.getSessionIdFromLogin(mockHelper))
                .thenReturn(null);
        ResponseEntity<ObjectNode> response = loginController.logIntoSession(mockHelper);
        ObjectNode actual = response.getBody();
        assertTrue(actual.isEmpty());
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Test
    void testLogIntoHelperSession_InternalServerError() throws IOException {
        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Mockito.lenient().when(loginService.getSessionIdFromLogin(mockHelper))
                .thenThrow(new IOException("Server failed"));
        ResponseEntity<ObjectNode> response = loginController.logIntoSession(mockHelper);
        ObjectNode actual = response.getBody();
        assertTrue(actual.isEmpty());
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Test
    void testLogIntoAdminSession_Success() throws IOException {
        int expectedId = 1;
        boolean expectedAdmin = true;
        Mockito.lenient().when(loginService.getSessionIdFromLogin(mockAdmin))
                .thenReturn(expectedId);
        Mockito.lenient().when(loginService.isAdmin(mockAdmin)).thenReturn(expectedAdmin);
        ResponseEntity<ObjectNode> response = loginController.logIntoSession(mockAdmin);
        ObjectNode actual = response.getBody();
        assertNotNull(actual);
        assertEquals(expectedId, actual.get("id").asInt());
        assertEquals(expectedAdmin, actual.get("admin").asBoolean());
    }

    @Test
    void testLogIntoAdminSession_BadLogin() throws IOException {
        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        Mockito.lenient().when(loginService.getSessionIdFromLogin(mockAdmin))
                .thenReturn(null);
        ResponseEntity<ObjectNode> response = loginController.logIntoSession(mockAdmin);
        ObjectNode actual = response.getBody();
        assertTrue(actual.isEmpty());
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Test
    void testLogIntoAdminSession_InternalServerError() throws IOException {
        HttpStatus expectedStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        Mockito.lenient().when(loginService.getSessionIdFromLogin(mockAdmin))
                .thenThrow(new IOException("Server failed"));
        ResponseEntity<ObjectNode> response = loginController.logIntoSession(mockAdmin);
        ObjectNode actual = response.getBody();
        assertTrue(actual.isEmpty());
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Test
    void testLogoutAdmin_Success() {
        HttpStatus expectedStatus = HttpStatus.OK;
        when(loginService.logoutAdmin(anyInt())).thenReturn(true);
        ResponseEntity<Void> response = loginController.logoutOfAdminSession(0);
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Test
    void testLogoutAdmin_Failure() {
        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        when(loginService.logoutAdmin(anyInt())).thenReturn(false);
        ResponseEntity<Void> response = loginController.logoutOfAdminSession(0);
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Test
    void testLogoutHelper_Success() {
        HttpStatus expectedStatus = HttpStatus.OK;
        when(loginService.logoutHelper(anyInt())).thenReturn(true);
        ResponseEntity<Void> response = loginController.logoutOfHelperSession(0);
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Test
    void testLogoutHelper_Failure() {
        HttpStatus expectedStatus = HttpStatus.UNAUTHORIZED;
        when(loginService.logoutHelper(anyInt())).thenReturn(false);
        ResponseEntity<Void> response = loginController.logoutOfHelperSession(0);
        assertEquals(expectedStatus, response.getStatusCode());
    }
}
