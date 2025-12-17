package org.autumnframework.infra.configurators;

import lombok.SneakyThrows;
import org.autumnframework.annotations.Value;
import org.autumnframework.infra.ApplicationContext;
import org.autumnframework.infra.parsers.ValuePropertiesParser;

import java.lang.reflect.Field;
import java.util.Map;

public class ValueAnnotationBootstrapComponentConfigurator implements BootstrapComponentConfigurator {
    @SneakyThrows
    @Override
    public void configure(Object o, ApplicationContext context) {
        for (Field field : o.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Value.class)) {
                Map<String, String> properties = ValuePropertiesParser.getParsedProperties();
                Value annotation = field.getAnnotation(Value.class);
                if (properties.containsKey(annotation.value())) {
                    field.setAccessible(true);
                    field.set(o, properties.get(annotation.value()));
                }
            }
        }
    }
}
