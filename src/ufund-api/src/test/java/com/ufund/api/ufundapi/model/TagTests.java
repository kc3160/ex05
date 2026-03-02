package com.ufund.api.ufundapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public class TagTests {

    private Tag tag;
    private Set<Integer> initialNeeds;

    @BeforeEach
    public void setUp() {
        initialNeeds = new HashSet<>();
        initialNeeds.add(1);
        initialNeeds.add(2);
        tag = new Tag(101, "Education", initialNeeds);
    }

    @Test
    public void testConstructorAndGetters() {
        assertEquals(101, tag.getId());
        assertEquals("Education", tag.getName());

        Integer[] needs = tag.getNeeds();
        assertEquals(2, needs.length);
        assertTrue(Set.of(needs).contains(1));
        assertTrue(Set.of(needs).contains(2));
    }

    @Test
    public void testSetName() {
        tag.setName("Health");
        assertEquals("Health", tag.getName());
    }

    @Test
    public void testAddNeed() {
        Need mockNeed = mock(Need.class);
        when(mockNeed.getId()).thenReturn(3);

        tag.addNeed(mockNeed);

        Integer[] needs = tag.getNeeds();
        assertEquals(3, needs.length);
        assertTrue(Set.of(needs).contains(3));
    }

    @Test
    public void testRemoveNeed() {
        tag.removeNeed(2);

        Integer[] needs = tag.getNeeds();
        assertEquals(1, needs.length);
        assertFalse(Set.of(needs).contains(2));
    }

    @Test
    public void testCopyWithId() {
        Tag newTag = tag.copyWithId(202);

        assertEquals(202, newTag.getId());
        assertEquals(tag.getName(), newTag.getName());
        assertArrayEquals(tag.getNeeds(), newTag.getNeeds());
    }

    @Test
    public void testHasNeed() {
        assertTrue(tag.hasNeed(1));
        assertFalse(tag.hasNeed(99));
    }

    @Test
    public void testConstructorWithNullNeedIds() {
        Tag newTag = new Tag(999, "Test", null);
        assertNotNull(newTag.getNeeds());
        assertEquals(0, newTag.getNeeds().length);
    }
}