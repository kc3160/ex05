package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Admin;
import com.ufund.api.ufundapi.model.Helper;
import com.ufund.api.ufundapi.model.User;

public class UserdataDAOTests {
    private User[] storedUsers;
    private ObjectMapper mockObjectMapper;
    private UserDataDAO UserdataDAO;
    private String tmpfile = "test.tmp";


    @BeforeEach
    void setup() throws IOException{
        mockObjectMapper = mock(ObjectMapper.class);

        storedUsers = new User[3];
        storedUsers[0] = new Helper("John", "this@email", "321", 0);
        storedUsers[1] = new Helper("F", "Y@gmail.com", "123", 1);
        storedUsers[2] = new Admin("J", "t@email", "321");
        when(mockObjectMapper.
                readValue(new File(tmpfile), User[].class))
                    .thenReturn(storedUsers);

        UserdataDAO = new UserDataDAO(tmpfile, mockObjectMapper);
    }

    @Test
    void registerHelperTest_Success() throws IOException{
        //hot garabge unit test, this wont work compared user to helper
        Helper mockHelper = new Helper("Ethan", "that@email", "123", 2);
        doNothing().when(mockObjectMapper).writeValue(eq(new File(tmpfile)), any());
        Helper actualHelper = this.UserdataDAO.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword());
        System.out.println(actualHelper.getBasketId());
        assertEquals(mockHelper, actualHelper);

    }

    @Test
    void registerHelperTest_Failure() throws IOException{
        //Helper mockHelperJohn = new Helper("John", "this@email", "321", 0);
        Helper mockHelper = new Helper("Ethan", "this@email", "123", 0);

        doThrow(new IOException()).when(mockObjectMapper).writeValue(eq(new File(tmpfile)), any());

        //this.UserdataDAO.registerHelper("John", "this@email", "321");
        assertNull(this.UserdataDAO.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword()));

    }

    @Test
    void registerHelperTest_FailureAdminLogin() throws IOException{
        //Helper mockHelperJohn = new Helper("John", "this@email", "321", 0);
        Helper mockHelper = new Helper("J", "t@email", "321", 0);

        doThrow(new IOException()).when(mockObjectMapper).writeValue(eq(new File(tmpfile)), any());

        //this.UserdataDAO.registerHelper("John", "this@email", "321");
        assertNull(this.UserdataDAO.registerHelper(mockHelper.getName(), mockHelper.getEmail(), mockHelper.getPassword()));

    }

    @Test
    void checkHelperTest_Success() throws IOException{
        //need to add more
        Helper mockHelper = new Helper("F", "Y@gmail.com", "123", 0);
        boolean actual = this.UserdataDAO.checkHelper(mockHelper);
        
        assertTrue(actual);
    }

    @Test
    void checkHelperTest_FailInstance() throws IOException{
        Admin mockHelper = new Admin("J", "t@email", "321");
        boolean actual = this.UserdataDAO.checkHelper(mockHelper);
        
        assertFalse(actual);
    }

    @Test
    void checkHelperTest_FailHash() throws IOException{
        Helper mockHelper = new Helper("F", "Y@gmail", "123", 0);
        boolean actual = this.UserdataDAO.checkHelper(mockHelper);
        
        assertFalse(actual);
    }

    @Test
    void checkHelperTest_FailPassword() throws IOException{
        Helper mockHelper = new Helper("F", "Y@gmail.com", "1235", 0);
        boolean actual = this.UserdataDAO.checkHelper(mockHelper);
        
        assertFalse(actual);
    }

    @Test
    void checkHelperTest_FailName() throws IOException{
        Helper mockHelper = new Helper("G", "Y@gmail.com", "123", 0);
        boolean actual = this.UserdataDAO.checkHelper(mockHelper);
        
        assertFalse(actual);
    }

    @Test
    void checkAdminTest_Success() throws IOException{
        Admin mockAdmin = new Admin("J", "t@email", "321");
        boolean actual = this.UserdataDAO.checkAdmin(mockAdmin);
        
        assertTrue(actual);
    }

    @Test
    void checkAdminTest_FailInstance() throws IOException{
        Helper mockAdmin = new Helper("F", "Y@gmail", "123", 0);
        boolean actual = this.UserdataDAO.checkAdmin(mockAdmin);
        
        assertFalse(actual);
    }

    @Test
    void checkAdminTest_FailHash() throws IOException{
        Admin mockAdmin = new Admin("J", "t@email.co.uk?", "321");
        boolean actual = this.UserdataDAO.checkAdmin(mockAdmin);
        
        assertFalse(actual);
    }

    @Test
    void checkAdminTest_FailPassword() throws IOException{
        Admin mockAdmin = new Admin("J", "t@email", "3210");
        boolean actual = this.UserdataDAO.checkAdmin(mockAdmin);
        
        assertFalse(actual);
    }

    @Test
    void checkAdminTest_FailName() throws IOException{
        Admin mockAdmin = new Admin("L", "t@email", "321");
        boolean actual = this.UserdataDAO.checkAdmin(mockAdmin);
        
        assertFalse(actual);
    }

    @Test
    void checkGetUserFromHash_Success() {
        User expectedUser = this.storedUsers[1];
        int hash = expectedUser.hashCode();
        User actualUser = this.UserdataDAO.getUserFromHash(hash);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    void checkGetUserFromHash_Fail() {
        int hash = "Not@auser".hashCode();
        User actualUser = this.UserdataDAO.getUserFromHash(hash);
        assertNull(actualUser);
    }
}
