package com.himalaya.orderservice.service;

import com.himalaya.orderservice.dto.OrderDto;
import com.himalaya.orderservice.jpa.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDetails);

    OrderDto getOrderByOrderId(String orderId);

    Iterable<OrderEntity> getOrderByUserId(String userId);
}
