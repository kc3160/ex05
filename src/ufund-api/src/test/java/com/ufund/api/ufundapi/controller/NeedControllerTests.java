package com.ufund.api.ufundapi.controller;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.TemporaryNeed;
import com.ufund.api.ufundapi.service.NeedService;

class NeedControllerTests {

    private MockMvc mockMvc;

    private int adminSession;
    private int helperSession;

    @Mock
    private NeedService needService;

    @InjectMocks
    private NeedController needController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(needController).build();
    }

    @Test
    void testGetNeed_Success() throws Exception {
        //Not necessary
        adminSession = 1;
        helperSession = 0;

        Need need = new TemporaryNeed(1, "Seed Pack",10,5);
        when(needService.getNeedById(1)).thenReturn(need);

        mockMvc.perform(get("/needs/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNeed_NotFound() throws Exception {
        when(needService.getNeedById(1)).thenReturn(null);

        mockMvc.perform(get("/needs/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetNeed_InternalServerError() throws Exception {
        when(needService.getNeedById(anyInt())).thenThrow(new IOException());

        mockMvc.perform(get("/needs/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetNeeds_Success() throws Exception {
        Need[] needs = { new TemporaryNeed(1, "Seed Pack",10,5) };
        when(needService.getAllNeeds()).thenReturn(needs);

        mockMvc.perform(get("/needs"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNeeds_Empty() throws Exception {
        when(needService.getAllNeeds()).thenReturn(new Need[0]);

        mockMvc.perform(get("/needs"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNeeds_InternalServerError() throws Exception {
        when(needService.getAllNeeds()).thenThrow(new IOException());

        mockMvc.perform(get("/needs"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testSearchNeeds_Found() throws Exception {
        Need[] needs = { new TemporaryNeed(1, "Seed",10,5) };
        when(needService.searchNeeds("Seed",new int[0],0,999999)).thenReturn(needs);

        mockMvc.perform(get("/needs/?search=Seed"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchNeeds_Empty() throws Exception {
        when(needService.searchNeeds("Seed",new int[0],0,999999)).thenReturn(new Need[0]);

        mockMvc.perform(get("/needs/?search=Seed"))
                .andExpect(status().isOk());
    }

    @Test
    void testSearchNeeds_InternalServerError() throws Exception {
        when(needService.searchNeeds("Seed",new int[0],0,999999)).thenThrow(new IOException());

        mockMvc.perform(get("/needs/?search=Seed"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testCreateNeed_Success() throws Exception {
        TemporaryNeed need = new TemporaryNeed(0, "Seed",10,5);
        when(needService.createNeed(eq(need), eq(1))).thenReturn(need);

        mockMvc.perform(post("/needs?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(need)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateNeedAuthorize_Fail() throws Exception {
        TemporaryNeed need = new TemporaryNeed(0, "Seed",10,5);
        when(needService.createNeed(eq(need), eq(0))).thenReturn(null);

        mockMvc.perform(post("/needs?id=0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"test\",\"id\":0,\"name\":\"Seed\", \"cost\":10, \"quantity\":5}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateNeedAuthorize_InternalServerError() throws Exception {
        TemporaryNeed need = new TemporaryNeed(0, "Seed",10,5);
        when(needService.createNeed(eq(need), eq(0))).thenThrow(new IOException());

        mockMvc.perform(post("/needs?id=0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"test\",\"id\":0,\"name\":\"Seed\", \"cost\":10, \"quantity\":5}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUpdateNeed_Success() throws Exception {
        TemporaryNeed need = new TemporaryNeed(0, "Idk",10,5);
        when(needService.updateNeed(eq(need), eq(1))).thenReturn(need);

        mockMvc.perform(put("/needs?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"test\",\"id\":0,\"name\":\"Idk\", \"cost\":10, \"quantity\":5}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateNeedAuthorize_Fail() throws Exception {
        TemporaryNeed need = new TemporaryNeed(0, "Idk",10,5);
        when(needService.updateNeed(eq(need), eq(0))).thenReturn(null);

        mockMvc.perform(put("/needs?id=0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"test\",\"id\":0,\"name\":\"Idk\", \"cost\":10}, \"quantity\":5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateNeedAuthorize_InternalServerError() throws Exception {
        TemporaryNeed need = new TemporaryNeed(0, "Idk",10,5);
        when(needService.updateNeed(eq(need), eq(0))).thenThrow(new IOException());

        mockMvc.perform(put("/needs?id=0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"test\",\"id\":0,\"name\":\"Idk\", \"cost\":10}, \"quantity\":5"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteNeed_Success() throws Exception {
        when(needService.deleteNeed(eq(1), eq(1))).thenReturn(true);

        mockMvc.perform(delete("/needs/1?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteNeed_NotFound() throws Exception {
        when(needService.deleteNeed(eq(1), eq(1))).thenReturn(false);

        mockMvc.perform(delete("/needs/1?id=0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNeed_InternalServerError() throws Exception {
        when(needService.deleteNeed(eq(1), eq(1))).thenThrow(new IOException());

        mockMvc.perform(delete("/needs/1?id=1"))
                .andExpect(status().isInternalServerError());
    }
    @Test
void testGetNeedsByIds_Success() throws Exception {
    Need[] needs = { new TemporaryNeed(1, "Seed Pack", 10, 5) };
    when(needService.getNeedsByIds(new int[]{1})).thenReturn(needs);

    mockMvc.perform(get("/needs/batch?ids=1"))
            .andExpect(status().isOk());
}

@Test
void testGetNeedsByIds_EmptyArray() throws Exception {
    when(needService.getNeedsByIds(new int[]{})).thenReturn(new Need[0]);

    mockMvc.perform(get("/needs/batch"))
            .andExpect(status().isOk());
}

@Test
void testGetNeedsByIds_NotFound() throws Exception {
    when(needService.getNeedsByIds(new int[]{1})).thenReturn(new Need[0]);

    mockMvc.perform(get("/needs/batch?ids=1"))
            .andExpect(status().isNotFound());
}

@Test
void testGetNeedsByIds_InternalServerError() throws Exception {
    when(needService.getNeedsByIds(new int[]{1})).thenThrow(new IOException());

    mockMvc.perform(get("/needs/batch?ids=1"))
            .andExpect(status().isInternalServerError());
}

@Test
void testAddNeedToBundle_Success() throws Exception {
    Need need = new TemporaryNeed(1, "Seed Pack", 10, 5);
    when(needService.addNeedtoBundle(eq(1), eq(1), eq(need))).thenReturn(true);

    mockMvc.perform(put("/needs/1/add?id=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(need)))
            .andExpect(status().isOk());
}

@Test
void testAddNeedToBundle_NotFound() throws Exception {
    Need need = new TemporaryNeed(1, "Seed Pack", 10, 5);
    when(needService.addNeedtoBundle(eq(1), eq(1), eq(need))).thenReturn(false);

    mockMvc.perform(put("/needs/1/add?id=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(need)))
            .andExpect(status().isNotFound());
}

@Test
void testAddNeedToBundle_InternalServerError() throws Exception {
    Need need = new TemporaryNeed(1, "Seed Pack", 10, 5);
    when(needService.addNeedtoBundle(eq(1), eq(1), eq(need))).thenThrow(new IOException());

    mockMvc.perform(put("/needs/1/add?id=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(need)))
            .andExpect(status().isInternalServerError());
}

@Test
void testRemoveNeedFromBundle_Success() throws Exception {
    Need need = new TemporaryNeed(1, "Seed Pack", 10, 5);
    when(needService.removeNeedfromBundle(eq(1), eq(1), eq(need))).thenReturn(true);

    mockMvc.perform(put("/needs/1/remove?id=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(need)))
            .andExpect(status().isOk());
}

@Test
void testRemoveNeedFromBundle_NotFound() throws Exception {
    Need need = new TemporaryNeed(1, "Seed Pack", 10, 5);
    when(needService.removeNeedfromBundle(eq(1), eq(1), eq(need))).thenReturn(false);

    mockMvc.perform(put("/needs/1/remove?id=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(need)))
            .andExpect(status().isNotFound());
}

@Test
void testRemoveNeedFromBundle_InternalServerError() throws Exception {
    Need need = new TemporaryNeed(1, "Seed Pack", 10, 5);
    when(needService.removeNeedfromBundle(eq(1), eq(1), eq(need))).thenThrow(new IOException());

    mockMvc.perform(put("/needs/1/remove?id=1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(need)))
            .andExpect(status().isInternalServerError());
}
}


