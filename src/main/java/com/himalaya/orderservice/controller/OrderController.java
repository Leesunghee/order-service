package com.himalaya.orderservice.controller;

import com.himalaya.orderservice.dto.OrderDto;
import com.himalaya.orderservice.jpa.OrderEntity;
import com.himalaya.orderservice.service.OrderService;
import com.himalaya.orderservice.vo.RequestOrder;
import com.himalaya.orderservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class OrderController {

    private final Environment env;
    private final OrderService orderService;

    @Autowired
    public OrderController(Environment env, OrderService orderService) {
        this.env = env;
        this.orderService = orderService;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder order, @PathVariable("userId") String userId) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = mapper.map(order, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createdOrder = orderService.createOrder(orderDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.map(createdOrder, ResponseOrder.class));
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrder(@PathVariable("userId") String userId) {

        Iterable<OrderEntity> orderList = orderService.getOrderByUserId(userId);

        List<ResponseOrder> resultOrder = new ArrayList<>();

        orderList.forEach(v -> {
            resultOrder.add(new ModelMapper().map(v, ResponseOrder.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(resultOrder);

    }


}
