package org.autumnframework.infra.parsers;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ComponentPropertiesParser extends AbstractPropertiesParser {

    @SneakyThrows
    public static Map<Class, Class> getParsedProperties(String propertyFileName) {
        Map<String, String> ifc2implString = getParsedProperties0(propertyFileName);
        HashMap<Class, Class> ifc2impl = new HashMap<>();

        if (ifc2implString.isEmpty()) {
            return ifc2impl;
        }

        for (Map.Entry<String, String> entry : ifc2implString.entrySet()) {
            Class<?> interfaceClass = Class.forName(entry.getKey());
            Class<?> implementationClass = Class.forName(entry.getValue());
            ifc2impl.put(interfaceClass, implementationClass);
        }

        return ifc2impl;
    }

    public static Map<Class, Class> getParsedProperties() {
        return getParsedProperties("component.properties");
    }
}