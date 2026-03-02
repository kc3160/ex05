package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.NeedBundle;
import com.ufund.api.ufundapi.model.TemporaryNeed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CupboardDAOTests {
    private ObjectMapper mockObjectMapper;
    private CupboardDAO cupboardDAO;

    private final String dummyFile = "test.json";

    @BeforeEach
    void setup() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);

        TemporaryNeed[] needs = {};
        when(mockObjectMapper.readValue(any(File.class), eq(Need[].class))).thenReturn(needs);

        cupboardDAO = new CupboardDAO(dummyFile, mockObjectMapper);
    }

    @Test
    void testCreateAndGetNeed() throws IOException {
        TemporaryNeed tempNeed = new TemporaryNeed(-1, "Water Bottle", 5.0, 10);
        Need created = cupboardDAO.createNeed(tempNeed);
        assertEquals("Water Bottle", created.getName());

        Need fetched = cupboardDAO.getNeed(created.getId());
        assertEquals(created, fetched);
    }

    @Test
    void testDeleteNeed() throws IOException {
        TemporaryNeed tempNeed = new TemporaryNeed(-1, "Socks", 3.0, 5);
        Need created = cupboardDAO.createNeed(tempNeed);

        assertTrue(cupboardDAO.deleteNeed(created.getId()));
        assertNull(cupboardDAO.getNeed(created.getId()));
    }

    @Test
    void testGetAllNeeds() throws IOException {
        cupboardDAO.createNeed(new TemporaryNeed(-1, "Pen", 1.0, 10));
        cupboardDAO.createNeed(new TemporaryNeed(-1, "Notebook", 2.0, 5));

        Need[] allNeeds = cupboardDAO.getAllNeeds();
        assertEquals(2, allNeeds.length);
    }

    @Test
    void testSearchNeeds() throws IOException {
        cupboardDAO.createNeed(new TemporaryNeed(-1, "Backpack", 25.0, 2));
        cupboardDAO.createNeed(new TemporaryNeed(-1, "Bag", 15.0, 1));

        Need[] searchResult = cupboardDAO.searchNeeds("back",new int[0], 0, 100000);
        assertEquals(1, searchResult.length);
        assertEquals("Backpack", searchResult[0].getName());
    }

    @Test
    void testUpdateNeed_NonBundle() throws IOException {
        TemporaryNeed temp = new TemporaryNeed(-1, "Shoes", 50.0, 1);
        Need created = cupboardDAO.createNeed(temp);

        TemporaryNeed updated = new TemporaryNeed(created.getId(), "Running Shoes", 55.0, 1);
        Need result = cupboardDAO.updateNeed(updated);
        assertEquals("Running Shoes", result.getName());
    }

    @Test
    void testUpdateNeed_BundleWithRecalculation() throws IOException {
        // Create individual needs
        TemporaryNeed food = new TemporaryNeed(-1, "Food", 10.0, 1);
        TemporaryNeed soap = new TemporaryNeed(-1, "Soap", 2.0, 1);
        Need foodCreated = cupboardDAO.createNeed(food);
        Need soapCreated = cupboardDAO.createNeed(soap);

        TestNeedBundle bundle = new TestNeedBundle(-1, "Care Package", 1, 0.1); // 10% discount
        bundle.getNeeds().put(foodCreated.getId(), 2); // 2 food
        bundle.getNeeds().put(soapCreated.getId(), 1); // 1 soap
        Need createdBundle = cupboardDAO.createNeed(bundle);

        Need updated = cupboardDAO.updateNeed(createdBundle);
        double expectedCost = (2 * 10 + 1 * 2) * 0.9;
        assertEquals(expectedCost, updated.getCost(), 0.001);
    }

    @Test
    void testAddNeedToBundle() throws IOException {
        TemporaryNeed item = new TemporaryNeed(-1, "Blanket", 20.0, 2);
        Need itemCreated = cupboardDAO.createNeed(item);

        TestNeedBundle bundle = new TestNeedBundle(-1, "Winter Pack", 1, 0.2);
        Need bundleCreated = cupboardDAO.createNeed(bundle);

        assertTrue(cupboardDAO.addNeedtoBundle(bundleCreated.getId(), itemCreated));
        Need updated = cupboardDAO.getNeed(bundleCreated.getId());

        assertTrue(((NeedBundle) updated).getNeeds().containsKey(itemCreated.getId()));
    }

    @Test
    void testRemoveNeedFromBundle() throws IOException {
        TemporaryNeed item = new TemporaryNeed(-1, "Blanket", 20.0, 2);
        Need itemCreated = cupboardDAO.createNeed(item);

        TestNeedBundle bundle = new TestNeedBundle(-1, "Winter Pack", 1, 0.2);
        bundle.addNeedtoBundle(itemCreated);
        Need bundleCreated = cupboardDAO.createNeed(bundle);

        assertTrue(cupboardDAO.removeNeedfromBundle(bundleCreated.getId(), itemCreated));
        Need updated = cupboardDAO.getNeed(bundleCreated.getId());

        assertFalse(((NeedBundle) updated).getNeeds().containsKey(itemCreated.getId()));
    }

    /**
     * Local concrete implementation of NeedBundle since it's abstract.
     */
    private static class TestNeedBundle extends NeedBundle {
        public TestNeedBundle(int id, String name, int quantity, double discount) {
            super(id, name, 0, quantity, discount);
        }

        @Override
        public Need copyWithId(int id) {
            TestNeedBundle copy = new TestNeedBundle(id, getName(), getQuantity(), getDiscount());
            for (Map.Entry<Integer, Integer> entry : getNeeds().entrySet()) {
                copy.getNeeds().put(entry.getKey(), entry.getValue());
            }
            copy.setCost(getCost());
            return copy;
        }
    }
    @Test
void testGetNeedsByIdsWithValidIds() throws IOException {
    cupboardDAO.createNeed(new TemporaryNeed(0, "Pen", 1.0, 10));
    cupboardDAO.createNeed(new TemporaryNeed(1, "Notebook", 2.0, 5));
    int[] ids = new int[] { 0, 1 };
    Need[] needs = cupboardDAO.getNeedsByIds(ids);
    assertNotNull(needs);
    assertEquals(1, needs.length);
}

@Test
void testGetNeedsByIdsWithNoIds() throws IOException {
    Need[] needs = cupboardDAO.getNeedsByIds(new int[] {});
    assertNotNull(needs);
    assertEquals(0, needs.length);
}


}