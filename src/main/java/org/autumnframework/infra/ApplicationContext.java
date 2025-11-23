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
    private static ApplicationContext INSTANCE;
    private final Map<Class, Object> componentRegistry;

    @Setter
    private ObjectFactory factory;

    @Getter
    private final Config config;

    public static ApplicationContext getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationContext();
        }
        return INSTANCE;
    }

    private ApplicationContext() {
        Map<Class, Class> ifc2impl = ComponentPropertiesParser.getParsedProperties();
        this.config = new JavaConfig("org.autumnframework", ifc2impl);
        this.componentRegistry = new ConcurrentHashMap<>();
    }

    public <T> T getComponent(Class<T> type) {
        if (componentRegistry.containsKey(type)) {
            return (T) componentRegistry.get(type);
            // Получение существующего экземпляра компонента,
            // если уже существует в реестре
        }

        Class<? extends T> impl = type;
        if (type.isInterface()) {
            impl = config.getImplementation(type); // Поиск реализации, если тип - интерфейс
        }

        T t = factory.createObject(impl); // Создание и настройка экземпляра в фабрике

        if (impl.isAnnotationPresent(Component.class)) {
            componentRegistry.put(type, t); // Кеширование компонента для последующего переиспользования
            return t;
        }

        throw new RuntimeException("Component with " + type + " not found");
    }
}
