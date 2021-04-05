package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private CartController cartController;

    @Before
    public void setUp() {
        this.cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_successfullyAddSingleItemToCart() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();

        Long id = new Long(0);
        Cart cart = new Cart();
        cart.setId(id);

        User user = createUser();
        user.setCart(cart);
        cart.setUser(user);
        when(userRepository.findByUsername("testUsername")).thenReturn(user);

        Item item = createItem();
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        when(cartRepository.save(cart)).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        assertEquals(new BigDecimal(0.99), c.getTotal());
        assertEquals(1, c.getItems().size());
        assertEquals(id, c.getId());
        assertEquals(user, c.getUser());
    }

    @Test
    public void verify_failedAddToCartInvalidUser() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void verify_failedAddToCartInvalidItem() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        Long id = new Long(0);
        Cart cart = new Cart();
        cart.setId(id);

        User user = createUser();
        user.setCart(cart);
        cart.setUser(user);
        when(userRepository.findByUsername("testUsername")).thenReturn(user);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void verify_removeItemFromCart() {
        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        Long id = new Long(0);
        Cart cart = new Cart();
        cart.setId(id);

        User user = createUser();
        user.setCart(cart);
        cart.setUser(user);
        when(userRepository.findByUsername("testUsername")).thenReturn(user);

        Item item = createItem();
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        when(cartRepository.save(cart)).thenReturn(cart);

        cart.addItem(item);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart c = response.getBody();
        assertEquals(0, c.getItems().size());
        assertEquals(0, c.getTotal().intValue());

    }

    private ModifyCartRequest createModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("testUsername");
        modifyCartRequest.setItemId(0);
        modifyCartRequest.setQuantity(1);
        return modifyCartRequest;
    }

    private User createUser(){
        User user = new User();
        user.setId(0);
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        return user;
    }

    private Item createItem() {
        Item item = new Item();
        Long id = new Long(0);
        item.setId(id);
        item.setName("testItem");
        item.setDescription("testDescription");
        item.setPrice(new BigDecimal(0.99));
        return item;
    }

}
