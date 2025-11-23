package org.autumnframework.implementations;

import org.autumnframework.interfaces.Notifier;

public class ConsoleNotifier implements Notifier {
    @Override
    public void notify(String message) {
        System.out.println(message);
    }
}
