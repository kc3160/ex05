package com.ufund.api.ufundapi.controller;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Basket;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.TemporaryNeed;
import com.ufund.api.ufundapi.service.BasketService;

class BasketControllerTests {

    private MockMvc mockMvc;

    @Mock
    private BasketService basketService;

    @InjectMocks
    private BasketController basketController;


    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(basketController).build();
    }

    @Test
    void testGetBasket_Success() throws Exception {
        Basket need = new Basket(0);
        when(basketService.getBasket(1)).thenReturn(need);

        mockMvc.perform(get("/baskets?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBasket_NotFound() throws Exception {
        when(basketService.getBasket(1)).thenReturn(null);

        mockMvc.perform(get("/baskets?id=1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBasket_InternalServerError() throws Exception {
        when(basketService.getBasket(anyInt())).thenThrow(new IOException());

        mockMvc.perform(get("/baskets?id=1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testAddNeedToBasket_Success() throws Exception {
        Need need = new TemporaryNeed(1, "Seed Pack",10, 5);

        when(basketService.addNeedToBasket(eq(1), any(Need.class))).thenReturn(true);

        mockMvc.perform(post("/baskets/needs?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(need)))
                .andExpect(status().isOk());

        verify(basketService, times(1)).addNeedToBasket(eq(1), any(Need.class));
    }

    @Test
    void testAddNeedToBasket_NotFound() throws Exception {
        Need need = new TemporaryNeed(1, "Seed Pack",10, 5);

        when(basketService.addNeedToBasket(eq(1), any(Need.class))).thenReturn(false);

        mockMvc.perform(post("/baskets/needs?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(need)))
                .andExpect(status().isNotFound());

        verify(basketService, times(1)).addNeedToBasket(eq(1), any(Need.class));
    }

    @Test
    void testAddNeedToBasket_ServerError() throws Exception {
        Need need = new TemporaryNeed(1, "Seed Pack",10, 5);

        when(basketService.addNeedToBasket(eq(1), any(Need.class))).thenThrow(new IOException("Service error"));

        mockMvc.perform(post("/baskets/needs?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(need)))
                .andExpect(status().isInternalServerError());

        verify(basketService, times(1)).addNeedToBasket(eq(1), any(Need.class));
    }

    @Test
    void testUpdateBasket_Success() throws Exception {
        Basket basket = new Basket(1);
        when(basketService.updateBasket(eq(1), any(Basket.class))).thenReturn(true);

        mockMvc.perform(put("/baskets?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(basket)))
                .andExpect(status().isOk());

        verify(basketService, times(1)).updateBasket(eq(1), any(Basket.class));
    }

    @Test
    void testUpdateBasket_NotFound() throws Exception {
        Basket basket = new Basket(1);
        when(basketService.updateBasket(eq(1), any(Basket.class))).thenReturn(false);

        mockMvc.perform(put("/baskets?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(basket)))
                .andExpect(status().isNotFound());

        verify(basketService, times(1)).updateBasket(eq(1), any(Basket.class));
    }

    @Test
    void testCheckoutBasket_Success() throws Exception {
        when(basketService.checkoutBasket(1)).thenReturn(true);

        mockMvc.perform(post("/baskets/checkout?id=1"))
                .andExpect(status().isOk());

        verify(basketService, times(1)).checkoutBasket(1);
    }

    @Test
    void testCheckoutBasket_NotFound() throws Exception {
        when(basketService.checkoutBasket(1)).thenReturn(false);

        mockMvc.perform(post("/baskets/checkout?id=1"))
                .andExpect(status().isNotFound());

        verify(basketService, times(1)).checkoutBasket(1);
    }

    @Test
    void testCheckoutBasket_ServerError() throws Exception {
        when(basketService.checkoutBasket(1)).thenThrow(new IOException("Service error"));

        mockMvc.perform(post("/baskets/checkout?id=1"))
                .andExpect(status().isInternalServerError());

        verify(basketService, times(1)).checkoutBasket(1);
    }
}


