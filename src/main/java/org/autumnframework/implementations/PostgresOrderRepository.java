package org.autumnframework.implementations;

import org.autumnframework.Order;
import org.autumnframework.annotations.Component;
import org.autumnframework.annotations.Log;
import org.autumnframework.interfaces.OrderRepository;

@Log
@Component
public class PostgresOrderRepository implements OrderRepository {
    @Override
    public void save(Order order) {
        System.out.println("Saving order to postgres...");
    }
}
