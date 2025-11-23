package org.autumnframework.interfaces;

import org.reflections.Reflections;

public interface Config {
    <T> Class<? extends T> getImplementation(Class<T> type);
    Reflections getScanner();
}
