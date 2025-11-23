package org.autumnframework.implementations;

import org.autumnframework.annotations.Component;
import org.autumnframework.interfaces.Notifier;

@Component
public class ConsoleNotifier implements Notifier {
    @Override
    public void notify(String message) {
        System.out.println(message);
    }
}
