package org.autumnframework.implementations;


import org.autumnframework.annotations.Component;
import org.autumnframework.interfaces.Config;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "rawtypes"})
public class JavaConfig implements Config {

    private final Reflections scanner;
    private final Map<Class, Class> ifc2impl;

    public JavaConfig(String packageName) {
        this.scanner = new Reflections(packageName);
        this.ifc2impl = new HashMap<>();
    }

    public JavaConfig(String packageName, Map<Class, Class> ifc2impl) {
        this.scanner = new Reflections(packageName);
        this.ifc2impl = new HashMap<>();

        ifc2impl.forEach((clazz, impl) -> {
            if (impl.isAnnotationPresent(Component.class)) {
                this.ifc2impl.put(clazz, impl);
            }
        });
    }

    @Override
    public <T> Class<? extends T> getImplementation(Class<T> type) {
        return ifc2impl.computeIfAbsent(type, aClass -> {
            Set<Class<? extends T>> classes = scanner.getSubTypesOf(type);
            Set<Class<? extends T>> components = classes.stream()
                    .filter(clazz -> clazz.isAnnotationPresent(Component.class))
                    .collect(Collectors.toSet());

            if (components.size() != 1) {
                throw new RuntimeException(
                        type + " must have 1 component implementation, but " +
                                components.size() + " were found: " + components
                );
            }
            return components.iterator().next();
        });
    }

    @Override
    public Reflections getScanner() {
        return scanner;
    }
}
