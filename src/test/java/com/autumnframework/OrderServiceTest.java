package com.autumnframework;

import org.autumnframework.Order;
import org.autumnframework.OrderService;
import org.autumnframework.interfaces.Notifier;
import org.autumnframework.interfaces.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private Notifier notifier;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void test() {
        Order order = new Order();
        orderService.createOrder(order);

        verify(orderRepository, times(1)).save(eq(order));
        verify(notifier, times(1)).notify("Order created successfully");
    }
}
