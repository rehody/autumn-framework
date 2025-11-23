package org.autumnframework;

import org.autumnframework.annotations.Component;
import org.autumnframework.annotations.InitMethod;
import org.autumnframework.annotations.Value;
import org.autumnframework.infra.ApplicationContext;
import org.autumnframework.interfaces.Notifier;
import org.autumnframework.interfaces.OrderRepository;

@Component
public class OrderService {

    private final OrderRepository orderRepository =
            ApplicationContext.getInstance().getComponent(OrderRepository.class);

    private final Notifier notifier =
            ApplicationContext.getInstance().getComponent(Notifier.class);

    public OrderService() {
        System.out.println("Constructor done!");
    }

    @Value("greeting")
    private String greeting;

    @InitMethod
    private void initMethod() {
        System.out.println(greeting);
    }

    public void createOrder(Order order) {
        orderRepository.save(order);
        notifier.notify("Order created successfully");
    }
}
