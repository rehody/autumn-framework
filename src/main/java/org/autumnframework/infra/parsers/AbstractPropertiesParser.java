package org.autumnframework.infra.parsers;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

public class AbstractPropertiesParser {
    @SneakyThrows
    protected static Map<String, String> getParsedProperties0(String propertyFileName) {
        Map<String, String> propertiesMap = new HashMap<>();

        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(propertyFileName);
        if (inputStream == null) {
            System.err.println("Properties file not found: " + propertyFileName);
            return propertiesMap;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            Stream<String> lines = reader.lines();
            propertiesMap = lines
                    .filter(line -> !line.trim().isEmpty())
                    .filter(line -> !line.trim().startsWith("#"))
                    .map(line -> line.split("="))
                    .filter(parts -> parts.length == 2)
                    .peek(parts -> {
                        parts[0] = parts[0].trim();
                        parts[1] = parts[1].trim();
                    })
                    .collect(toMap(
                            parts -> parts[0],
                            parts -> parts[1])
                    );
            return propertiesMap;
        }
    }
}
