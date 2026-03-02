package com.ufund.api.ufundapi.persistence;

import com.ufund.api.ufundapi.model.FilterType;
import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NeedFilterTests {

    private Collection<Need> needs;
    private Collection<Tag> tags;

    @BeforeEach
    public void setUp() {
        needs = new ArrayList<>();
        tags = new ArrayList<>();

        Need need1 = mock(Need.class);
        when(need1.getName()).thenReturn("Need 1");
        when(need1.getCost()).thenReturn(100.0);
        when(need1.getId()).thenReturn(1);

        Need need2 = mock(Need.class);
        when(need2.getName()).thenReturn("Need 2");
        when(need2.getCost()).thenReturn(200.0);
        when(need2.getId()).thenReturn(2);

        Need need3 = mock(Need.class);
        when(need3.getName()).thenReturn("Need 3");
        when(need3.getCost()).thenReturn(150.0);
        when(need3.getId()).thenReturn(3);

        needs.add(need1);
        needs.add(need2);
        needs.add(need3);
    }

    @Test
    public void testFilterByName() {
        Need[] result = NeedFilter.filterByName(needs, "Need 1");
        assertEquals(1, result.length);
        assertEquals("Need 1", result[0].getName());

        result = NeedFilter.filterByName(needs, "need");
        assertEquals(3, result.length);  
    }

    @Test
    public void testFilterByCost() {
        Need[] result = NeedFilter.filterByCost(needs, 100.0, 150.0);
        assertEquals(2, result.length);
        assertTrue(result[0].getCost() >= 100.0 && result[0].getCost() <= 150.0);
        assertTrue(result[1].getCost() >= 100.0 && result[1].getCost() <= 150.0);
    }

    @Test
    public void testFilterByTags() {
        Tag tag1 = mock(Tag.class);
        when(tag1.getNeeds()).thenReturn(new Integer[]{1, 2});

        Tag tag2 = mock(Tag.class);
        when(tag2.getNeeds()).thenReturn(new Integer[]{2, 3});

        tags.add(tag1);
        tags.add(tag2);

        Need[] result = NeedFilter.filterByTags(tags, needs);
        assertEquals(1, result.length);
        assertEquals(2, result[0].getId()); 
    }
    /*private ArrayList<Need> storedNeeds;

    @BeforeEach
    public void setupFileService() throws IOException {
        storedNeeds = new ArrayList<>();
        storedNeeds.add(new TemporaryNeed(0, "Need 0", 1.59, 10));
        storedNeeds.add(new TemporaryNeed(3, "Need 3", 599.99, 10));
        storedNeeds.add(new TemporaryNeed(5, "Sete Seat", 10.50, 10));
        storedNeeds.add(new TemporaryNeed(14, "Delete Me 14", 1.00, 10));
        storedNeeds.add(new TemporaryNeed(15, "A Need 15", 3.14, 10));
    }

    @Test
    public void testSearchNumOne() {
        String filter = "ete";
        Need[] expectedNeeds = new Need[2];
        expectedNeeds[0] = new TemporaryNeed(5, "Sete Seat", 10.50, 10);
        expectedNeeds[1] = new TemporaryNeed(14, "Delete Me 14", 1.00, 10);

        Need[] actualNeeds = NeedFilter.filterByName(storedNeeds, filter);
        assertEquals(expectedNeeds.length, actualNeeds.length);
        assertEquals(expectedNeeds[0], actualNeeds[0]);
        assertEquals(expectedNeeds[1], actualNeeds[1]);
    }

    @Test
    public void testSearchNumTwo() {
        String filter = "bebe";
        Need[] expectedNeeds = new Need[0];

        Need[] actualNeeds = NeedFilter.filterByName(storedNeeds, filter);
        assertEquals(expectedNeeds.length, actualNeeds.length);
    }*/
}
