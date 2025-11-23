package org.autumnframework;

import org.autumnframework.implementations.ConsoleNotifier;
import org.autumnframework.implementations.InMemoryOrderRepository;
import org.autumnframework.interfaces.Notifier;
import org.autumnframework.interfaces.OrderRepository;

public class OrderService {

    private final OrderRepository orderRepository = new InMemoryOrderRepository();
    private final Notifier notifier = new ConsoleNotifier();

    public void createOrder(Order order) {
        orderRepository.save(order);
        notifier.notify("Order created successfully");
    }
}
