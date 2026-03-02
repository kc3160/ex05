package com.ufund.api.ufundapi.service;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ufund.api.ufundapi.model.Basket;
import com.ufund.api.ufundapi.model.Helper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.SessionHandler;
import com.ufund.api.ufundapi.persistence.BasketRepository;
import com.ufund.api.ufundapi.persistence.NeedRepository;

class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;
    
    @Mock
    private SessionHandler sessionHandler;
    
    @Mock
    private NeedRepository needRepository;

    @InjectMocks
    private BasketService basketService;

    private Helper mockHelper;
    private Basket mockBasket;
    private Need mockNeed;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        
        // Create a mock helper and basket
        mockHelper = mock(Helper.class);
        mockBasket = mock(Basket.class);
        mockNeed = mock(Need.class);
        
        // Set up session handler to return a mock helper when authorizeHelperSession is called
        when(sessionHandler.authorizeHelperSession(anyInt())).thenReturn(mockHelper);
        
        // Set up basketRepository and needRepository behavior
        when(basketRepository.getBasket(anyInt())).thenReturn(mockBasket);
        when(basketRepository.addNeedToBasket(anyInt(), any(Need.class))).thenReturn(true);
        when(basketRepository.updateBasket(any(Basket.class))).thenReturn(true);
        when(basketRepository.checkoutBasket(anyInt())).thenReturn(true);
    }

    @Test
    void testGetBasket_Success() throws IOException {
        // Arrange
        when(mockHelper.getBasketId()).thenReturn(1);
        
        // Act
        Basket basket = basketService.getBasket(1);
        
        // Assert
        assertNotNull(basket);
        verify(basketRepository).getBasket(anyInt());
        verify(sessionHandler).authorizeHelperSession(anyInt());
    }

    @Test
    void testGetBasket_HelperNotAuthorized() throws IOException {
        // Arrange
        when(sessionHandler.authorizeHelperSession(anyInt())).thenReturn(null);
        
        // Act
        Basket basket = basketService.getBasket(1);
        
        // Assert
        assertNull(basket);
    }

    @Test
    void testAddNeedToBasket_Success() throws IOException {
        // Arrange
        when(mockHelper.getBasketId()).thenReturn(1);
        
        // Act
        boolean result = basketService.addNeedToBasket(1, mockNeed);
        
        // Assert
        assertTrue(result);
        verify(basketRepository).addNeedToBasket(anyInt(), any(Need.class));
    }

    @Test
    void testAddNeedToBasket_HelperNotAuthorized() throws IOException {
        // Arrange
        when(sessionHandler.authorizeHelperSession(anyInt())).thenReturn(null);
        
        // Act
        boolean result = basketService.addNeedToBasket(1, mockNeed);
        
        // Assert
        assertFalse(result);
    }

    @Test
    void testUpdateBasket_Success() throws IOException {
        // Arrange
        when(mockHelper.getBasketId()).thenReturn(1);
        
        // Act
        boolean result = basketService.updateBasket(1, mockBasket);
        
        // Assert
        assertTrue(result);
        verify(basketRepository).updateBasket(any(Basket.class));
    }

    @Test
    void testUpdateBasket_HelperNotAuthorized() throws IOException {
        // Arrange
        when(sessionHandler.authorizeHelperSession(anyInt())).thenReturn(null);
        
        // Act
        boolean result = basketService.updateBasket(1, mockBasket);
        
        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckoutBasket_Success() throws IOException {
        // Arrange
        when(mockHelper.getBasketId()).thenReturn(1);
        when(basketRepository.getBasket(anyInt())).thenReturn(mockBasket);
        
        // Act
        boolean result = basketService.checkoutBasket(1);
        
        // Assert
        assertTrue(result);
        verify(basketRepository).checkoutBasket(anyInt());
        verify(sessionHandler).authorizeHelperSession(anyInt());
    }

    @Test
    void testCheckoutBasket_BasketNotFound() throws IOException {
        // Arrange
        when(mockHelper.getBasketId()).thenReturn(1);
        when(basketRepository.getBasket(anyInt())).thenReturn(null);
        
        // Act
        boolean result = basketService.checkoutBasket(1);
        
        // Assert
        assertFalse(result);
    }

    @Test
    void testCheckoutBasket_HelperNotAuthorized() throws IOException {
        // Arrange
        when(sessionHandler.authorizeHelperSession(anyInt())).thenReturn(null);
        
        // Act
        boolean result = basketService.checkoutBasket(1);
        
        // Assert
        assertFalse(result);
    }

    // Additional tests can be written for the syncBasketWithCupboard method
}