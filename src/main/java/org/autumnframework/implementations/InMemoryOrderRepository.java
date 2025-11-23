package org.autumnframework.implementations;

import org.autumnframework.Order;
import org.autumnframework.interfaces.OrderRepository;

public class InMemoryOrderRepository implements OrderRepository {

    @Override
    public void save(Order order) {
        System.out.println("Saving order...");
    }
}
