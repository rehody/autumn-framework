package org.autumnframework.infra;

import lombok.SneakyThrows;
import org.autumnframework.annotations.InitMethod;
import org.autumnframework.infra.configurators.BootstrapObjectConfigurator;
import org.autumnframework.infra.configurators.BootstrapProxyConfigurator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ObjectFactory {

    private final List<BootstrapObjectConfigurator> objectConfigurators;
    private final List<BootstrapProxyConfigurator> proxyConfigurators;
    private final ApplicationContext context;

    @SneakyThrows
    public ObjectFactory(ApplicationContext context) {
        this.context = context;
        this.objectConfigurators = new ArrayList<>();
        this.proxyConfigurators = new ArrayList<>();

        addObjectConfigurators(context);
        addProxyConfigurators(context);
    }

    @SneakyThrows
    private void addObjectConfigurators(ApplicationContext context) {
        Set<Class<? extends BootstrapObjectConfigurator>> objectConfigurators =
                context.getConfig().getScanner().getSubTypesOf(BootstrapObjectConfigurator.class);

        for (Class<? extends BootstrapObjectConfigurator> configurator : objectConfigurators) {
            this.objectConfigurators.add(configurator.getDeclaredConstructor().newInstance());
        }
    }

    @SneakyThrows
    private void addProxyConfigurators(ApplicationContext context) {
        Set<Class<? extends BootstrapProxyConfigurator>> proxyConfigurators =
                context.getConfig().getScanner().getSubTypesOf(BootstrapProxyConfigurator.class);

        for (Class<? extends BootstrapProxyConfigurator> configurator : proxyConfigurators) {
            this.proxyConfigurators.add(configurator.getDeclaredConstructor().newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> impl) {
        T t = impl.getDeclaredConstructor().newInstance();
        configure(t);
        invokeInitMethod(impl, t);
        t = wrapWithProxy(impl, t);
        return t;
    }

    private <T> T wrapWithProxy(Class<T> impl, T t) {
        for (BootstrapProxyConfigurator proxyConfigurator : proxyConfigurators) {
            t = proxyConfigurator.replaceWithProxy(t, impl);
        }
        return t;
    }

    @SneakyThrows
    private <T> void invokeInitMethod(Class<T> impl, T t) {
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
