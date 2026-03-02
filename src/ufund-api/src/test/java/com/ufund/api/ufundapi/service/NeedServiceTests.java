/*package com.ufund.api.ufundapi.service;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.SessionHandler;
import com.ufund.api.ufundapi.model.TemporaryNeed;
import com.ufund.api.ufundapi.persistence.NeedRepository;

public class NeedServiceTests{
    @Mock
    private NeedRepository needHandler;
    @Mock
    private SessionHandler sessionHandler;

    @InjectMocks
    private NeedService needService;

    private Need mockNeed;
    private Need extraMockNeed;
    private Need[] mockNeeds;
    private Need[] wrongMockNeeds;
    private int adminId = 1;
    private int helperId = 0;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockNeed = new TemporaryNeed(24, "Potato", 2.00, 2);
        extraMockNeed = new TemporaryNeed(23, "Tree", 10.00, 2);
        mockNeeds = new Need[]{mockNeed, extraMockNeed};
        wrongMockNeeds = new Need[]{mockNeed, new TemporaryNeed(20, "Potot", 3.00, 2)};
        when(sessionHandler.checkAdminSession(adminId)).thenReturn(true);
        when(sessionHandler.checkAdminSession(helperId)).thenReturn(false);
    }

    @Test
    void testGetNeedById_Success() throws IOException{
        when(needHandler.getNeed(mockNeed.getId()))
        .thenReturn(mockNeed);
        Need expectedNeed = needService.getNeedById(mockNeed.getId());
        assertEquals(mockNeed, expectedNeed);
    }

    @Test
    void testGetNeedById_Failure() throws IOException{
        when(needHandler.getNeed(mockNeed.getId()))
        .thenReturn(new TemporaryNeed(12, "Sweet Potato", 3.00, 2));
        Need expectedNeed = needService.getNeedById(mockNeed.getId());
        assertNotEquals(mockNeed, expectedNeed);
    }

    @Test
    void testGetAllNeeds_Success() throws IOException{
        when(needHandler.getAllNeeds()).thenReturn(mockNeeds);
        Need[] expectedNeeds = needService.getAllNeeds();
        assertArrayEquals(mockNeeds, expectedNeeds);
    }
    
    @Test
    void testGetAllNeeds_Failure() throws IOException{
        when(needHandler.getAllNeeds())
        .thenReturn(new Need[]{mockNeed, new TemporaryNeed(11, "Sweet Potato", 7.00, 2)});
        Need[] expectedNeeds = needService.getAllNeeds();
        assertNotEquals(mockNeeds, expectedNeeds);
    }

    @Test
    void testSearchNeedsByName_Success() throws IOException{
        int[] b = new int[0];
        when(needHandler.searchNeeds("search",b,0,100000))
        .thenReturn(mockNeeds);
        Need[] expectedNeeds = needService.searchNeeds("search",b,0,100000);
        assertArrayEquals(mockNeeds, expectedNeeds);
    }
    @Test
    void testSearchNeedsByName_Failure() throws IOException{
        int[] b = new int[0];
        when(needHandler.searchNeeds("search",b,0,100000))
        .thenReturn(wrongMockNeeds);
        Need[] expectedNeeds = needService.searchNeeds("search",b,0,100000);
        assertNotEquals(mockNeeds, expectedNeeds);
    }
    @Test
    void testCreateNeed_Success() throws IOException{
        when(needHandler.createNeed(mockNeed)).thenReturn(mockNeed);
        Need returned = needService.createNeed(mockNeed, adminId);
        assertEquals(mockNeed, returned);
    }
    @Test
    void testCreateNeed_Failure() throws IOException{
        when(needHandler.createNeed(extraMockNeed)).thenReturn(extraMockNeed);
        Need returned = needService.createNeed(extraMockNeed, helperId);
        assertNotEquals(extraMockNeed, returned);
    }
    @Test
    void testUpdateNeed_Success() throws IOException{
        when(needHandler.updateNeed(mockNeed)).thenReturn(mockNeed);
        Need returned = needService.updateNeed(mockNeed, adminId);
        assertEquals(mockNeed, returned);
    }
    @Test
    void testUpdateNeed_Failure() throws IOException{
        when(needHandler.updateNeed(extraMockNeed)).thenReturn(extraMockNeed);
        Need returned = needService.updateNeed(extraMockNeed, helperId);
        assertNotEquals(extraMockNeed, returned);
    }
    @Test
    void testDeleteNeed_Success() throws IOException{
        boolean expected = true;
        when(needHandler.deleteNeed(mockNeed.getId())).thenReturn(true);
        boolean returned = needService.deleteNeed(mockNeed.getId(), adminId);
        assertEquals(expected, returned);
    }
    @Test
    void testDeleteNeed_Failure() throws IOException{
        boolean expected = true;
        when(needHandler.deleteNeed(mockNeed.getId())).thenReturn(false);
        boolean returned = needService.deleteNeed(mockNeed.getId(), adminId);
        assertNotEquals(expected, returned);
    }
    @Test
    void testDeleteNeedAuthorize_Failure() throws IOException{
        boolean expected = true;
        when(needHandler.deleteNeed(mockNeed.getId())).thenReturn(true);
        boolean returned = needService.deleteNeed(mockNeed.getId(), helperId);
        assertNotEquals(expected, returned);
    }
    @Test
void testGetNeedsByIds_Success() throws IOException {
    int[] ids = new int[] { 24, 23 };
    when(needHandler.getNeedsByIds(ids)).thenReturn(mockNeeds);
    Need[] expectedNeeds = needService.getNeedsByIds(ids);
    assertArrayEquals(mockNeeds, expectedNeeds);
}
@Test
void testGetNeedsByIds_EmptyArray() throws IOException {
    Need[] expectedNeeds = needService.getNeedsByIds(new int[] {});
    assertEquals(0, expectedNeeds.length);
}
}
}*/
