package org.autumnframework.infra.parsers;

import lombok.SneakyThrows;

import java.util.Map;

public class ValuePropertiesParser extends AbstractPropertiesParser {

    private static final String DEFAULT_PROPERTY_FILE_NAME = "value.properties";

    @SneakyThrows
    public static Map<String, String> getParsedProperties(String propertyFileName) {
        return getParsedProperties0(propertyFileName);
    }

    public static Map<String, String> getParsedProperties() {
        return getParsedProperties(DEFAULT_PROPERTY_FILE_NAME);
    }

}
