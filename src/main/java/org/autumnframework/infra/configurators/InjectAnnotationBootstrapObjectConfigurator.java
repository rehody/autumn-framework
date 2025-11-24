package org.autumnframework.infra.configurators;

import lombok.SneakyThrows;
import org.autumnframework.annotations.Inject;
import org.autumnframework.infra.ApplicationContext;

import java.lang.reflect.Field;

public class InjectAnnotationBootstrapObjectConfigurator implements BootstrapObjectConfigurator {
    @Override
    @SneakyThrows
    public void configure(Object o, ApplicationContext context) {
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object component = context.getComponent(field.getType());
                field.set(o, component);
            }
        }
    }
}
