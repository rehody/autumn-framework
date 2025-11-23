package org.autumnframework;

public class Main {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        orderService.createOrder(new Order());
    }
}
