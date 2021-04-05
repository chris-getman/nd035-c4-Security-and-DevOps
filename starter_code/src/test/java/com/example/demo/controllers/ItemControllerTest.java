package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_getItemById() {
        Item item = createItem();
        Long id = new Long(0);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(id);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void verify_getItemByName() {
        Item item = createItem();
        Item item2 = createItem();
        Long id = new Long(1);
        item2.setId(id);
        Item[] items = { item , item2 };
        List<Item> itemList = Arrays.asList(items);
        when(itemRepository.findByName("testItem")).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> responseItems = response.getBody();
        assertEquals(2, responseItems.size());

    }

    private Item createItem() {
        Item item = new Item();
        Long id = new Long(0);
        item.setId(id);
        BigDecimal price = new BigDecimal(0.99);
        item.setPrice(price);
        item.setDescription("testDescription");
        item.setName("testItem");
        return item;
    }
}
