package com.ufund.api.ufundapi.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufund.api.ufundapi.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TagDAOTests {

    private ObjectMapper mockObjectMapper;
    private TagDAO tagDAO;

    private Tag[] testTags;

    @BeforeEach
    public void setUp() throws IOException {
        // Create mock ObjectMapper
        mockObjectMapper = mock(ObjectMapper.class);

        // Prepare some test data
        Set<Integer> needIds = new HashSet<>();
        needIds.add(1);
        Tag tag1 = new Tag(1, "Education", needIds);
        Tag tag2 = new Tag(2, "Health", needIds);
        testTags = new Tag[]{tag1, tag2};

        // Mock behavior for loading tags
        when(mockObjectMapper.readValue(any(File.class), eq(Tag[].class))).thenReturn(testTags);

        // Create DAO instance (this will trigger load)
        tagDAO = new TagDAO("test-tags.json", mockObjectMapper);
    }

    @Test
    public void testGetAllTags() throws IOException {
        Tag[] tags = tagDAO.getAllTags();
        assertEquals(2, tags.length);
    }

    @Test
    public void testGetTag() throws IOException {
        Tag tag = tagDAO.getTag(1);
        assertNotNull(tag);
        assertEquals("Education", tag.getName());
    }

    @Test
    public void testCreateTag() throws IOException {
        Tag newTag = new Tag(0, "Environment", new HashSet<>());
        Tag createdTag = tagDAO.createTag(newTag);

        assertNotEquals(0, createdTag.getId());
        assertEquals("Environment", createdTag.getName());

        // Verify save was called
        verify(mockObjectMapper, atLeastOnce()).writeValue(any(File.class), any(Tag[].class));
    }

    @Test
    public void testDeleteTag() throws IOException {
        boolean deleted = tagDAO.deleteTag(1);
        assertTrue(deleted);

        // Try to get the deleted tag
        Tag shouldBeNull = tagDAO.getTag(1);
        assertNull(shouldBeNull);
    }

    @Test
    public void testDeleteTagNotFound() throws IOException {
        boolean deleted = tagDAO.deleteTag(999);
        assertFalse(deleted);
    }

    @Test
    public void testUpdateTagSuccess() throws IOException {
        Tag updated = new Tag(1, "UpdatedName", new HashSet<>());
        Tag result = tagDAO.updateTag(updated);

        assertNotNull(result);
        assertEquals("UpdatedName", result.getName());

        verify(mockObjectMapper, atLeastOnce()).writeValue(any(File.class), any(Tag[].class));
    }

    @Test
    public void testUpdateTagNotFound() throws IOException {
        Tag updated = new Tag(999, "DoesNotExist", new HashSet<>());
        Tag result = tagDAO.updateTag(updated);

        assertNull(result);
    }
}