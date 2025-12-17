package org.autumnframework.infra;

import lombok.Getter;
import lombok.Setter;
import org.autumnframework.annotations.Component;
import org.autumnframework.implementations.JavaConfig;
import org.autumnframework.infra.parsers.ComponentPropertiesParser;
import org.autumnframework.interfaces.Config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ApplicationContext {

    private final Map<Class, Object> componentRegistry;

    @Getter
    private final Config config;

    @Setter
    private ComponentFactory factory;

    public ApplicationContext() {
        Map<Class, Class> ifc2impl = ComponentPropertiesParser.getParsedProperties();
        this.config = new JavaConfig("org.autumnframework", ifc2impl);
        this.componentRegistry = new ConcurrentHashMap<>();
        componentRegistry.put(ApplicationContext.class, this);
    }

    public <T> T getComponent(Class<T> type) {
        if (componentRegistry.containsKey(type)) {
            return (T) componentRegistry.get(type);
        }

        Class<? extends T> impl = type;
        if (type.isInterface()) {
            impl = config.getImplementation(type);
        }

        T t = factory.createComponent(impl);

        if (impl.isAnnotationPresent(Component.class)) {
            componentRegistry.put(type, t);
            return t;
        }

        throw new RuntimeException("Component with " + type + " not found");
    }
}
