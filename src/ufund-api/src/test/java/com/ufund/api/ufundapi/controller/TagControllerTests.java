package com.ufund.api.ufundapi.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.ufund.api.ufundapi.model.Tag;
import com.ufund.api.ufundapi.service.TagService;

public class TagControllerTests {

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    private Tag sampleTag;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Set<Integer> needIds = new HashSet<>();
        needIds.add(1);
        sampleTag = new Tag(1, "Sample", needIds);
    }

    @Test
    public void testGetNeedTags_Found() throws IOException {
        Tag[] tags = new Tag[]{sampleTag};
        when(tagService.getTagsByNeed(1)).thenReturn(tags);

        ResponseEntity<Tag[]> response = tagController.getNeedTags(1);

        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(tags, response.getBody());
    }

    @Test
    public void testGetNeedTags_NotFound() throws IOException {
        when(tagService.getTagsByNeed(1)).thenReturn(null);

        ResponseEntity<Tag[]> response = tagController.getNeedTags(1);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetNeedTags_InternalServerError() throws IOException {
        when(tagService.getTagsByNeed(anyInt())).thenThrow(new IOException());

        ResponseEntity<Tag[]> response = tagController.getNeedTags(1);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    public void testGetTags_Found() throws IOException {
        Tag[] tags = new Tag[]{sampleTag};
        when(tagService.getAllTags()).thenReturn(tags);

        ResponseEntity<Tag[]> response = tagController.getTags();

        assertEquals(200, response.getStatusCodeValue());
        assertArrayEquals(tags, response.getBody());
    }

    @Test
    public void testGetTags_Empty() throws IOException {
        Tag[] tags = new Tag[0];
        when(tagService.getAllTags()).thenReturn(tags);

        ResponseEntity<Tag[]> response = tagController.getTags();

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length);
    }

    @Test
    void testGetTags_InternalServerError() throws IOException {
        when(tagService.getAllTags()).thenThrow(new IOException());

        ResponseEntity<Tag[]> response = tagController.getTags();

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    public void testCreateTag_Authorized() throws IOException {
        when(tagService.createTag(sampleTag, 1)).thenReturn(sampleTag);

        ResponseEntity<Tag> response = tagController.createTag(sampleTag, 1);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(sampleTag, response.getBody());
    }

    @Test
    public void testCreateTag_Unauthorized() throws IOException {
        when(tagService.createTag(sampleTag, 1)).thenReturn(null);

        ResponseEntity<Tag> response = tagController.createTag(sampleTag, 1);

        assertEquals(401, response.getStatusCodeValue());
    }

    @Test
    void testCreateTag_InternalServerError() throws IOException {
        when(tagService.createTag(any(), anyInt())).thenThrow(new IOException());

        ResponseEntity<Tag> response = tagController.createTag(new Tag(0, "e", null), 0);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateTag_Found() throws IOException {
        when(tagService.updateTag(sampleTag, 1)).thenReturn(sampleTag);

        ResponseEntity<Tag> response = tagController.updateTag(sampleTag, 1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleTag, response.getBody());
    }

    @Test
    public void testUpdateTag_NotFound() throws IOException {
        when(tagService.updateTag(sampleTag, 1)).thenReturn(null);

        ResponseEntity<Tag> response = tagController.updateTag(sampleTag, 1);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testUpdateTag_InternalServerError() throws IOException {
        when(tagService.updateTag(any(), anyInt())).thenThrow(new IOException());

        ResponseEntity<Tag> response = tagController.updateTag(new Tag(0, "e", null), 1);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateNeedTag_Success() throws IOException {
        when(tagService.updateNeedTag(1, -1, 2, 1)).thenReturn(true);

        ResponseEntity<Void> response = tagController.updateNeedTag(1, -1, 2, 1);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testUpdateNeedTag_Failure() throws IOException {
        when(tagService.updateNeedTag(1, -1, 2, 1)).thenReturn(false);

        ResponseEntity<Void> response = tagController.updateNeedTag(1, -1, 2, 1);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testUpdateNeedTag_InternalServerError() throws IOException {
        when(tagService.updateNeedTag(anyInt(), anyInt(), anyInt(), anyInt())).thenThrow(new IOException());

        ResponseEntity<Void> response = tagController.updateNeedTag(1, 2, 4, 5);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteTag_Success() throws IOException {
        when(tagService.deleteTag(1, 1)).thenReturn(true);

        ResponseEntity<Void> response = tagController.deleteTag(1, 1);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testDeleteTag_NotFound() throws IOException {
        when(tagService.deleteTag(1, 1)).thenReturn(false);

        ResponseEntity<Void> response = tagController.deleteTag(1, 1);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteTag_InternalServerError() throws IOException {
        when(tagService.deleteTag(anyInt(), anyInt())).thenThrow(new IOException());

        ResponseEntity<Void> response = tagController.deleteTag(1, 2);

        assertEquals(500, response.getStatusCodeValue());
    }
}
