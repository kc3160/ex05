package com.ufund.api.ufundapi.persistence;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.TemporaryNeed;

public class NeedRepositoryTests {
    private String needRepositoryImplementation = "CupboardDAO";
    private NeedRepository needRepository;
    private Need[] storedNeeds;
    private ObjectMapper mockObjectMapper;
    
    @BeforeEach
    public void setupNeedRepository() throws IOException {
        String tmpFileName = "testing.tmp";
        mockObjectMapper = mock(ObjectMapper.class);
        storedNeeds = new Need[4];
        storedNeeds[0] = new TemporaryNeed(0, "Need 0", 1.59, 10);
        storedNeeds[1] = new TemporaryNeed(3, "Need 3", 599.99, 10);
        storedNeeds[2] = new TemporaryNeed(14, "Delete Me 14", 1.00, 10);
        storedNeeds[3] = new TemporaryNeed(15, "A Need 15", 3.14, 10);

        when(mockObjectMapper.
            readValue(new File(tmpFileName), Need[].class))
                .thenReturn(storedNeeds);

        needRepository = new CupboardDAO(tmpFileName, mockObjectMapper);
    }

    @Test
    void testValidCreateNeed() throws IOException {
        Need newNeed = new TemporaryNeed(3, "Added Need", 9.99, 10);
        Need expectedNeed = new TemporaryNeed(16, "Added Need", 9.99, 10);

        Need createdNeed = needRepository.createNeed(newNeed);

        assertNotNull(createdNeed);
        assertEquals(expectedNeed, createdNeed);
    }

    @Test
    void testValidDeleteNeed() throws IOException {
        int idToDelete = 14;

        boolean result = needRepository.deleteNeed(idToDelete);

        assertTrue(result);
    }

    @Test
    void testInvalidDeleteNeed() throws IOException {
        int idToDelete = 13;

        boolean result = needRepository.deleteNeed(idToDelete);

        assertFalse(result);
    }

    @Test
    void testGetAllNeeds() throws IOException {
        Need[] expectedNeeds = new Need[4];
        expectedNeeds[0] = new TemporaryNeed(0, "Need 0", 1.59, 10);
        expectedNeeds[1] = new TemporaryNeed(3, "Need 3", 599.99, 10);
        expectedNeeds[2] = new TemporaryNeed(14, "Delete Me 14", 1.00, 10);
        expectedNeeds[3] = new TemporaryNeed(15, "A Need 15", 3.14, 10);

        Need[] actualNeeds = needRepository.getAllNeeds();
        assertNotNull(actualNeeds);
        assertEquals(expectedNeeds.length, actualNeeds.length);
        
        for (int i = 0; i < actualNeeds.length; i++) {
            assertEquals(expectedNeeds[i], actualNeeds[i]);
        }
    }

    @Test
    void testValidGetNeed() throws IOException {
        int idToGet = 3;
        Need expectedNeed = new TemporaryNeed(idToGet, "Need 3", 599.99, 10);
        Need actualNeed = needRepository.getNeed(idToGet);

        assertEquals(expectedNeed, actualNeed);
    }

    @Test
    void testInvalidGetNeed() throws IOException {
        int idToGet = 4;
        Need actualNeed = needRepository.getNeed(idToGet);

        assertNull(actualNeed);
    }

    @Test
    void testValidUpdateNeed() throws IOException {
        Need expectedNeed = new TemporaryNeed(3, "Replaced Need 3", 59.99, 10);

        Need actualNeed1 = needRepository.updateNeed(expectedNeed);
        Need actualNeed2 = needRepository.getNeed(expectedNeed.getId());

        assertEquals(expectedNeed, actualNeed1);
        assertEquals(expectedNeed, actualNeed2);
    }

    @Test
    void testInvalidUpdateNeed() throws IOException {
        Need replaceNeed = new TemporaryNeed(4, "Replaced Need 4", 59.99, 10);

        Need actualNeed1 = needRepository.updateNeed(replaceNeed);
        Need actualNeed2 = needRepository.getNeed(replaceNeed.getId());

        assertNull(actualNeed1);
        assertNull(actualNeed2);
    }

    @Test
    void testFoundSearchNeeds() throws IOException {
        String searchTerm = "Need";
        Need[] expectedNeeds = new Need[3];
        expectedNeeds[0] = new TemporaryNeed(0, "Need 0", 1.59, 10);
        expectedNeeds[1] = new TemporaryNeed(3, "Need 3", 599.99, 10);
        expectedNeeds[2] = new TemporaryNeed(15, "A Need 15", 3.14, 10);

        Need[] actualNeeds = needRepository.searchNeeds(searchTerm,new int[0],0,100000);

        assertEquals(expectedNeeds.length, actualNeeds.length);
    }

    @Test
    void testNotFoundSearchNeeds() throws IOException {
        String searchTerm = "humm";
        int expectedLen = 0;
        
        Need[] actualNeeds = needRepository.searchNeeds(searchTerm,new int[0],0, 1);

        assertEquals(expectedLen, actualNeeds.length);
    }

    @Test
void testGetNeedsByIds() throws IOException {

    int[] idsToGet = new int[] { 0, 3, 15 };
    
    Need[] expectedNeeds = new Need[3];
    expectedNeeds[0] = new TemporaryNeed(0, "Need 0", 1.59, 10);
    expectedNeeds[1] = new TemporaryNeed(3, "Need 3", 599.99, 10);
    expectedNeeds[2] = new TemporaryNeed(15, "A Need 15", 3.14, 10);
    
    Need[] actualNeeds = needRepository.getNeedsByIds(idsToGet);

    assertNotNull(actualNeeds);

    assertEquals(expectedNeeds.length, actualNeeds.length);
    
    for (int i = 0; i < actualNeeds.length; i++) {
        assertEquals(expectedNeeds[i], actualNeeds[i]);
    }
}

@Test
void testGetNeedsByIdsWithNoMatch() throws IOException {
    int[] idsToGet = new int[] { 33333, 330, 1221 };
    
    Need[] actualNeeds = needRepository.getNeedsByIds(idsToGet);
    assertNotNull(actualNeeds);
    assertEquals(0, actualNeeds.length);
}
}
