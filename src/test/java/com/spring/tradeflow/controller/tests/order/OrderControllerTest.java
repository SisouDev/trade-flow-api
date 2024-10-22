package com.spring.tradeflow.controller.tests.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spring.tradeflow.exceptions.ResourceNotFoundException;
import com.spring.tradeflow.model.entities.client.Address;
import com.spring.tradeflow.model.entities.client.Client;
import com.spring.tradeflow.model.entities.client.Telephone;
import com.spring.tradeflow.model.entities.order.Order;
import com.spring.tradeflow.model.entities.order.OrderItem;
import com.spring.tradeflow.model.entities.product.Product;
import com.spring.tradeflow.model.repositories.client.ClientRepository;
import com.spring.tradeflow.model.repositories.order.OrderRepository;
import com.spring.tradeflow.model.repositories.product.ProductRepository;
import com.spring.tradeflow.utils.enums.client.Gender;
import com.spring.tradeflow.utils.enums.client.States;
import com.spring.tradeflow.utils.enums.client.TelephoneType;
import com.spring.tradeflow.utils.enums.order.OrderStatus;
import com.spring.tradeflow.utils.enums.product.ProductType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;

    String baseUrl = "/api/order/";

    @Test
    public void testCreateOrder() throws Exception {
        Long idClient = 71L;
        Long productId = 2L;
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        orderItem.setProduct(product);
        orderItems.add(orderItem);

        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        Order order = new Order();
        order.setClient(client);
        order.setOrderItems(orderItems);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingPrice(10.0);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String orderJson = mapper.writeValueAsString(order);

        mockMvc.perform(post("/api/order/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated());

    }

    @Test
    public void testGetOrderById() throws Exception {
        mockMvc.perform(get(baseUrl + "/{id}", 21))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllOrders() throws Exception {
        mockMvc.perform(get(baseUrl + "/all")).andExpect(status().isOk());
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        mockMvc.perform(put(baseUrl + "/{id}/status", 21)
                        .param("status", String.valueOf(OrderStatus.PROCESSING))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @Test
    public void testCancelOrder() throws Exception {
        mockMvc.perform(post(baseUrl + "/{id}/cancel", 21)
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }

    @Test
    public void testApplyCoupon() throws Exception {
        mockMvc.perform(post(baseUrl + "/{id}/apply-coupon", 21)
                .param("couponCode", String.valueOf(2))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    public void testAddOrderItem() throws Exception {
        long orderId = 21L;
        long productId = 2L;

        Map<String, Object> orderItem = new HashMap<>();
        orderItem.put("productId", productId);
        orderItem.put("quantity", 3);

        List<Map<String, Object>> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        ObjectMapper mapper = new ObjectMapper();
        String orderItemsJson = mapper.writeValueAsString(orderItems);

        mockMvc.perform(post(baseUrl + "/" + orderId + "/add-item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderItemsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderItems").isNotEmpty());
    }

    @Test
    public void testRemoveOrderItem() throws Exception {
        long orderId = 1L;
        long itemId = 1L;
        mockMvc.perform(delete(baseUrl + "/" + orderId + "/remove-item/" + itemId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetOrdersByClientId() throws Exception {
        mockMvc.perform(get(baseUrl + "/client/{clientId}", 2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrdersByStatus() throws Exception {
        mockMvc.perform(get(baseUrl + "/status/{status}", OrderStatus.PROCESSING).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//    @Test
//    public void testGetOrdersByDateRange() throws Exception {
//        mockMvc.perform(get(baseUrl + "/date-range")
//                .param("startDate", LocalDate.now(), "endDate", LocalDate.now())
//                .contentType(MediaType.APPLICATION_JSON)
//                ).andExpect(status().isOk());
//    }

    @Test
    public void testUpdateOrderItemQuantity() throws Exception {
        long orderId = 1L;
        long orderItemId = 2L;
        Integer newQuantity = 5;
        mockMvc.perform(put(baseUrl + "/" + orderId + "/update-item/" + orderItemId)
                        .param("quantity", String.valueOf(newQuantity)))
                .andExpect(status().isOk());
    }
}
