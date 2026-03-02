package com.ufund.api.ufundapi.model;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

class NeedTests {
    @Test
    void testToolNeed() {
        Need base = new ToolNeed(0, "G", 1.0, 2, true);
        Need expected = new ToolNeed(2, "G", 1.0, 2, true);
        Need actual = expected.copyWithId(2);

        assertEquals(expected, actual);
    }

    @Test
    void testFertilizerNeed() {
        Need base = new FertilizerNeed(0, "G", 1.0, 2, true);
        Need expected = new FertilizerNeed(2, "G", 1.0, 2, true);
        Need actual = expected.copyWithId(2);

        assertEquals(expected, actual);
    }

    @Test
    void testSeedNeed() {
        Need base = new SeedNeed(0, "G", 1.0, 2);
        Need expected = new SeedNeed(2, "G", 1.0, 2);
        Need actual = expected.copyWithId(2);

        assertEquals(expected, actual);
    }

    @Test
    void testSimpleSetCost() {
        Need expected = new SeedNeed(1, "Uno", 1.5, 5);
        Need actual = new SeedNeed(1, "Uno", 100.5, 5);
        actual.setCost(1.5);
        assertEquals(expected, actual);
    }

    @Test
    void testSimpleSetName() {
        Need expected = new SeedNeed(1, "Uno", 1.5, 5);
        Need actual = new SeedNeed(1, "Dos", 1.5, 5);
        actual.setName("Uno");
        assertEquals(expected, actual);
    }
}