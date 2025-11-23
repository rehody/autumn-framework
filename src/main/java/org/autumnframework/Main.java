package org.autumnframework;

import org.autumnframework.infra.Application;
import org.autumnframework.infra.ApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = Application.run();
        OrderService orderService = context.getComponent(OrderService.class);
        orderService.createOrder(new Order());
    }
}
