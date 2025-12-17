package org.autumnframework.infra;

import lombok.SneakyThrows;
import org.autumnframework.annotations.InitMethod;
import org.autumnframework.infra.configurators.BootstrapComponentConfigurator;
import org.autumnframework.infra.configurators.BootstrapProxyConfigurator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ComponentFactory {

    private final List<BootstrapComponentConfigurator> componentConfigurators;
    private final List<BootstrapProxyConfigurator> proxyConfigurators;
    private final ApplicationContext context;

    @SneakyThrows
    public ComponentFactory(ApplicationContext context) {
        this.context = context;
        this.componentConfigurators = new ArrayList<>();
        this.proxyConfigurators = new ArrayList<>();

        addComponentConfigurators(context);
        addProxyConfigurators(context);
    }

    @SneakyThrows
    private void addComponentConfigurators(ApplicationContext context) {
        Set<Class<? extends BootstrapComponentConfigurator>> componentConfigurators =
                context.getConfig().getScanner().getSubTypesOf(BootstrapComponentConfigurator.class);

        for (Class<? extends BootstrapComponentConfigurator> configurator : componentConfigurators) {
            this.componentConfigurators.add(configurator.getDeclaredConstructor().newInstance());
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
    public <T> T createComponent(Class<T> impl) {
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
        componentConfigurators.forEach(configurator -> configurator.configure(t, context));
    }
}
