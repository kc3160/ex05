package com.ufund.api.ufundapi.service;

import java.io.IOException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.model.SessionHandler;
import com.ufund.api.ufundapi.model.Tag;
import com.ufund.api.ufundapi.model.TemporaryNeed;
import com.ufund.api.ufundapi.persistence.NeedRepository;
import com.ufund.api.ufundapi.persistence.TagRepository;

public class TagServiceTests {

    private TagService tagService;
    private TagRepository mockTagRepository;
    private SessionHandler mockSessionHandler;
    private NeedRepository mockNeedRepository;

    @BeforeEach
    public void setUp() {
        mockTagRepository = mock(TagRepository.class);
        mockSessionHandler = mock(SessionHandler.class);
        mockNeedRepository = mock(NeedRepository.class);

        tagService = new TagService();

        // Inject mocks manually
        tagService.getClass().getDeclaredFields();
        tagService.getClass().getDeclaredFields();
        try {
            var field = TagService.class.getDeclaredField("tagHandler");
            field.setAccessible(true);
            field.set(tagService, mockTagRepository);

            field = TagService.class.getDeclaredField("sessionHandler");
            field.setAccessible(true);
            field.set(tagService, mockSessionHandler);

            field = TagService.class.getDeclaredField("needHandler");
            field.setAccessible(true);
            field.set(tagService, mockNeedRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetTagById() throws IOException {
        Tag mockTag = new Tag(1, "Mock", new HashSet<>());
        when(mockTagRepository.getTag(1)).thenReturn(mockTag);

        Tag result = tagService.getTagById(1);

        assertNotNull(result);
        assertEquals("Mock", result.getName());
    }

    @Test
    public void testGetAllTags() throws IOException {
        Tag[] tags = new Tag[]{new Tag(1, "A", new HashSet<>())};
        when(mockTagRepository.getAllTags()).thenReturn(tags);

        Tag[] result = tagService.getAllTags();

        assertEquals(1, result.length);
    }

    @Test
    public void testCreateTagAsAdmin() throws IOException {
        Tag input = new Tag(0, "NewTag", new HashSet<>());
        Tag created = new Tag(3, "NewTag", new HashSet<>());

        when(mockSessionHandler.checkAdminSession(123)).thenReturn(true);
        when(mockTagRepository.createTag(input)).thenReturn(created);

        Tag result = tagService.createTag(input, 123);

        assertNotNull(result);
        assertEquals(3, result.getId());
    }

    @Test
    public void testCreateTagAsNonAdmin() throws IOException {
        when(mockSessionHandler.checkAdminSession(123)).thenReturn(false);

        Tag result = tagService.createTag(new Tag(0, "Test", new HashSet<>()), 123);

        assertNull(result);
    }

    @Test
    public void testUpdateTagAsAdmin() throws IOException {
        Tag tag = new Tag(1, "Updated", new HashSet<>());
        when(mockSessionHandler.checkAdminSession(1)).thenReturn(true);
        when(mockTagRepository.updateTag(tag)).thenReturn(tag);

        Tag result = tagService.updateTag(tag, 1);

        assertNotNull(result);
        assertEquals("Updated", result.getName());
    }

    @Test
    public void testUpdateTagAsNonAdmin() throws IOException {
        Tag tag = new Tag(1, "Updated", new HashSet<>());
        when(mockSessionHandler.checkAdminSession(1)).thenReturn(false);

        Tag result = tagService.updateTag(tag, 1);

        assertNull(result);
    }

    @Test
    public void testDeleteTagAsAdmin() throws IOException {
        when(mockSessionHandler.checkAdminSession(1)).thenReturn(true);
        when(mockTagRepository.deleteTag(5)).thenReturn(true);

        boolean result = tagService.deleteTag(5, 1);

        assertTrue(result);
    }

    @Test
    public void testDeleteTagAsNonAdmin() throws IOException {
        when(mockSessionHandler.checkAdminSession(1)).thenReturn(false);

        boolean result = tagService.deleteTag(5, 1);

        assertFalse(result);
    }

    @Test
    public void testGetTagsByNeed() throws IOException {
        Tag tag1 = new Tag(1, "A", new HashSet<>());
        Tag tag2 = new Tag(2, "B", new HashSet<>());

        tag1.addNeed(new TemporaryNeed(10, "desc", 0, 0));
        when(mockTagRepository.getAllTags()).thenReturn(new Tag[]{tag1, tag2});

        Tag[] result = tagService.getTagsByNeed(10);

        assertEquals(1, result.length);
        assertEquals("A", result[0].getName());
    }

    @Test
    public void testUpdateNeedTag_withFullChange() throws IOException {
        Need need = new TemporaryNeed(42, "Need", 0, 0);
        Tag oldTag = new Tag(1, "Old", new HashSet<>());
        Tag newTag = new Tag(2, "New", new HashSet<>());

        when(mockSessionHandler.checkAdminSession(1)).thenReturn(true);
        when(mockNeedRepository.getNeed(42)).thenReturn(need);
        when(mockTagRepository.getTag(2)).thenReturn(newTag);
        when(mockTagRepository.getTag(1)).thenReturn(oldTag);

        boolean result = tagService.updateNeedTag(42, 1, 2, 1);

        assertTrue(result);
        verify(mockTagRepository, times(2)).updateTag(any(Tag.class));
    }

    @Test
    public void testUpdateNeedTag_SessionFail() throws IOException {
        when(mockSessionHandler.checkAdminSession(1)).thenReturn(false);

        boolean result = tagService.updateNeedTag(42, 1, 2, 1);

        assertFalse(result);
    }

    @Test
    public void testUpdateNeedTag_NeedNoExist() throws IOException {
        Need need = new TemporaryNeed(42, "Need", 0, 0);
        when(mockSessionHandler.checkAdminSession(1)).thenReturn(false);
        when(mockNeedRepository.getNeed(42)).thenReturn(need);

        boolean result = tagService.updateNeedTag(42, 1, 2, 1);

        assertFalse(result);
    }

    @Test
    public void testUpdateNeedTag_withSwap() throws IOException {
        Need need = new TemporaryNeed(42, "Need", 0, 0);
        Tag newTag = new Tag(2, "New", new HashSet<>());

        when(mockSessionHandler.checkAdminSession(1)).thenReturn(true);
        when(mockNeedRepository.getNeed(42)).thenReturn(need);
        when(mockTagRepository.getTag(-1)).thenReturn(null);
        when(mockTagRepository.getTag(2)).thenReturn(newTag);

        boolean result = tagService.updateNeedTag(42, 2, -1, 1);

        assertTrue(result);
    }

    @Test
    public void testUpdateNeedTag_withAdd() throws IOException {
        Need need = new TemporaryNeed(42, "Need", 0, 0);
        Tag newTag = new Tag(2, "New", new HashSet<>());

        when(mockSessionHandler.checkAdminSession(1)).thenReturn(true);
        when(mockNeedRepository.getNeed(42)).thenReturn(need);
        when(mockTagRepository.getTag(2)).thenReturn(newTag);
        when(mockTagRepository.getTag(-1)).thenReturn(null);

        boolean result = tagService.updateNeedTag(42, -1, 2, 1);

        assertTrue(result);
    }
}