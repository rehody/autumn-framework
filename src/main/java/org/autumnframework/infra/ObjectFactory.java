package org.autumnframework.infra;

import lombok.SneakyThrows;
import org.autumnframework.annotations.InitMethod;
import org.autumnframework.infra.configurators.BootstrapObjectConfigurator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectFactory {
    private final List<BootstrapObjectConfigurator> objectConfigurators;
    private final ApplicationContext context;

    @SneakyThrows
    public ObjectFactory(ApplicationContext context) {
        this.context = context;
        this.objectConfigurators = new ArrayList<>();

        Set<Class<? extends BootstrapObjectConfigurator>> objectConfigurators =
                context.getConfig().getScanner().getSubTypesOf(BootstrapObjectConfigurator.class);

        for (Class<? extends BootstrapObjectConfigurator> configurator : objectConfigurators) {
            this.objectConfigurators.add(configurator.getDeclaredConstructor().newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> impl) {
        T t = impl.getDeclaredConstructor().newInstance();
        configure(t);
        invokeInitMethod(impl, t);
        return t;
    }

    private <T> void invokeInitMethod(Class<T> impl, T t) throws IllegalAccessException, InvocationTargetException {
        for (Method method : impl.getDeclaredMethods()) {
            if (method.isAnnotationPresent(InitMethod.class)) {
                method.setAccessible(true);
                method.invoke(t);
            }
        }
    }

    private <T> void configure(T t) {
        objectConfigurators.forEach(configurator -> configurator.configure(t, context));
    }
}
