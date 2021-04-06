package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void verify_submitOrder() {

        Long id = new Long(0);

        User user = new User();
        user.setUsername("testUser");
        user.setId(id);

        Item item = new Item();
        item.setId(id);
        item.setName("testItem");
        BigDecimal price = new BigDecimal(0.99);
        item.setPrice(price);

        Cart cart = new Cart();
        cart.setId(id);
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        UserOrder userOrder = UserOrder.createFromCart(cart);
        when(orderRepository.save(userOrder)).thenReturn(userOrder);

        ResponseEntity<UserOrder> response = orderController.submit("testUser");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder uo = response.getBody();
        BigDecimal bd = new BigDecimal(0.99);
        assertEquals(bd, uo.getTotal());
        assertEquals(1, uo.getItems().size());



    }
}
